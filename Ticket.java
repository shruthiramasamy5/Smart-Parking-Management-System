package parking;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Ticket {
    private final String id;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private boolean paid;
    private Payment payment;
    public Ticket(String id, Vehicle vehicle, ParkingSpot spot) {
        this.id = id;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = LocalDateTime.now();
        this.exitTime = null;
        this.paid = false;
        this.payment = null;
    }
    public String getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public boolean isPaid() { return paid; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public void markPaid(Payment payment) {
        this.paid = true;
        this.payment = payment;
    }
    public double calculateFee(Tariff tariff) {
        LocalDateTime et = (exitTime == null) ? LocalDateTime.now() : exitTime;
        Duration duration = Duration.between(entryTime, et);
        return tariff.calculateFee(duration);
    }
    public String summary() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Ticket[%s]\n Vehicle: %s\n Spot: %s\n Entry: %s",
                id, vehicle, spot.getId(), entryTime.format(df));
    }
    public String detailedFeeBreakdown(Tariff tariff) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(entryTime, now);
        long totalMinutes = duration.toMinutes();
        long hours = (totalMinutes + 59) / 60;
        double base = hours * tariff.getHourlyRate();
        double total = Math.max(base, tariff.getMinCharge());
        return String.format(
                "Ticket: %s\nEntry: %s\nNow: %s\nDuration: %d minutes (~%d hour(s) charged)\nRate: ₹%.2f/hr\nBase: ₹%.2f\nMinCharge: ₹%.2f\nTotal: ₹%.2f",
                id,
                entryTime.format(df),
                now.format(df),
                totalMinutes,
                hours,
                tariff.getHourlyRate(),
                base,
                tariff.getMinCharge(),
                total
        );
    }
    @Override
    public String toString() {
        return "Ticket[" + id + "]";
    }
}
