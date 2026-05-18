package parking;
public class ParkingSpot {
    private final String id;
    private final SpotType type;
    private boolean occupied;
    private Ticket currentTicket;
    public ParkingSpot(String id, SpotType type) {
        this.id = id;
        this.type = type;
        this.occupied = false;
        this.currentTicket = null;
    }
    public String getId() { return id; }
    public SpotType getType() { return type; }
    public boolean isOccupied() { return occupied; }
    public synchronized void assign(Ticket ticket) {
        if (occupied) throw new IllegalStateException("Spot already occupied: " + id);
        this.currentTicket = ticket;
        this.occupied = true;
    }
    public synchronized void release() {
        this.currentTicket = null;
        this.occupied = false;
    }
    public Ticket getCurrentTicket() { return currentTicket; }
    @Override
    public String toString() {
        return String.format("Spot[%s:%s]%s", id, type, occupied ? " (OCCUPIED)" : "");
    }
}
