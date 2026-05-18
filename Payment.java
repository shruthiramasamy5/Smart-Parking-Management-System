package parking;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Payment {
    private final String id;
    private final double amount;
    private final String method;
    private final LocalDateTime time;
    public Payment(String id, double amount, String method) {
        this.id = id;
        this.amount = amount;
        this.method = method;
        this.time = LocalDateTime.now();
    }
    public String receipt() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Payment[%s]\nAmount: ₹%.2f\nMethod: %s\nTime: %s",
                id, amount, method, time.format(df));
    }
    public double getAmount() { return amount; }
}
