package parking;
import java.time.Duration;
public class Tariff {
    private final double hourlyRate; // per hour
    private final double minCharge;
    public Tariff(double hourlyRate, double minCharge) {
        this.hourlyRate = hourlyRate;
        this.minCharge = minCharge;
    }
    public double getHourlyRate() { return hourlyRate; }
    public double getMinCharge() { return minCharge; }
    public double calculateFee(Duration duration) {
        long minutes = duration.toMinutes();
        if (minutes <= 0) return minCharge;
        long hours = (minutes + 59) / 60; // ceil
        double amount = hours * hourlyRate;
        return Math.max(amount, minCharge);
    }
}
