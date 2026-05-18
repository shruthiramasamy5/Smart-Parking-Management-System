package parking;
import java.time.LocalDateTime;
import java.util.*;
public class ParkingLot {
    private final String name;
    private final Map<String, ParkingSpot> spots = new LinkedHashMap<>();
    private final Map<String, Ticket> activeTickets = new HashMap<>();
    private final Tariff tariff;
    private final IdGenerator idGen = new IdGenerator();
    public ParkingLot(String name, Tariff tariff) {
        this.name = name;
        this.tariff = tariff;
    }
    public Tariff getTariff() { return tariff; }
    public void addSpots(SpotType type, int count) {
        int start = spots.size() + 1;
        for (int i = 0; i < count; i++) {
            String id = String.format("%s-%03d", type.toString().charAt(0) + "", start + i);
            ParkingSpot s = new ParkingSpot(id, type);
            spots.put(id, s);
        }
    }
    public int totalSpots() { return spots.size(); }
    public Optional<ParkingSpot> findSpotFor(Vehicle v) {
        SpotType required = v.requiredSpot();
        for (ParkingSpot s : spots.values()) {
            if (!s.isOccupied() && canFit(s.getType(), required)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
    private boolean canFit(SpotType spotType, SpotType required) {
        if (spotType == SpotType.LARGE) return true;
        if (spotType == SpotType.MEDIUM) return required != SpotType.LARGE;
        return spotType == required;
    }
    public Ticket issueTicket(Vehicle v, ParkingSpot spot) {
        String ticketId = "TKT-" + idGen.next();
        Ticket ticket = new Ticket(ticketId, v, spot);
        spot.assign(ticket);               // prevents double allocation
        activeTickets.put(ticketId, ticket);
        return ticket;
    }
    public Optional<Ticket> findTicket(String id) {
        Ticket t = activeTickets.get(id);
        return Optional.ofNullable(t);
    }
    public Payment acceptPayment(Ticket ticket, double amount, String method) {
        String pid = "PAY-" + idGen.next();
        Payment p = new Payment(pid, amount, method);
        ticket.markPaid(p);
        return p;
    }
    public boolean processExit(String ticketId) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) return false;
        ticket.setExitTime(LocalDateTime.now());
        ParkingSpot s = ticket.getSpot();
        s.release();
        activeTickets.remove(ticket.getId());
        return true;
    }
    public String occupancySnapshot() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parking Lot: ").append(name).append("\n");
        sb.append("Tariff: ₹").append(String.format("%.2f/hr (min ₹%.2f)\n", tariff.getHourlyRate(), tariff.getMinCharge()));
        sb.append("Total spots: ").append(spots.size()).append("\n");
        sb.append("Occupied:\n");
        int occ = 0;
        for (ParkingSpot s : spots.values()) {
            if (s.isOccupied()) {
                occ++;
                Ticket t = s.getCurrentTicket();
                sb.append(String.format("  %s - %s - Ticket: %s - In: %s\n",
                        s.getId(), s.getType(), t.getId(), t.getEntryTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))));
            }
        }
        sb.append("Free spots: ").append(spots.size() - occ).append("\n");
        return sb.toString();
    }
}
