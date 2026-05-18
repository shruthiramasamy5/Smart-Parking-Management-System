package parking;
import java.util.Optional;
import java.util.Scanner;
public class ParkingApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static ParkingLot parkingLot = null;
    public static void main(String[] args) {
        System.out.println("=== Parking Lot Management Console ===");
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    configureLot();
                    break;
                case "2":
                    vehicleEntry();
                    break;
                case "3":
                    vehicleExit();
                    break;
                case "4":
                    displayOccupancy();
                    break;
                case "5":
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private static void printMainMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Configure Lot & Spots");
        System.out.println("2. Vehicle Entry (Issue Ticket)");
        System.out.println("3. Vehicle Exit (Calculate Fee & Pay)");
        System.out.println("4. Display Occupancy");
        System.out.println("5. Exit");
        System.out.print("Enter option: ");
    }
    private static void configureLot() {
        System.out.print("Enter lot name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter number of small spots: ");
        int small = readInt();
        System.out.print("Enter number of medium spots: ");
        int medium = readInt();
        System.out.print("Enter number of large spots: ");
        int large = readInt();
        System.out.print("Enter hourly rate (e.g. 30.0): ");
        double hourly = readDouble();
        System.out.print("Enter minimum charge (e.g. 10.0): ");
        double minCharge = readDouble();
        Tariff tariff = new Tariff(hourly, minCharge);
        parkingLot = new ParkingLot(name, tariff);
        parkingLot.addSpots(SpotType.SMALL, small);
        parkingLot.addSpots(SpotType.MEDIUM, medium);
        parkingLot.addSpots(SpotType.LARGE, large);
        System.out.printf("Configured lot '%s' with %d total spots. Tariff: ₹%.2f/hr, min ₹%.2f\n",
                name, parkingLot.totalSpots(), hourly, minCharge);
    }
    private static void vehicleEntry() {
        if (!ensureLotConfigured()) return;
        System.out.print("Enter vehicle reg no: ");
        String reg = scanner.nextLine().trim();
        System.out.println("Select vehicle size: 1.Small 2.Medium 3.Large");
        String c = scanner.nextLine().trim();
        VehicleType vt = VehicleType.SMALL;
        if ("2".equals(c)) vt = VehicleType.MEDIUM;
        else if ("3".equals(c)) vt = VehicleType.LARGE;
        Vehicle vehicle = new Vehicle(reg, vt);
        Optional<ParkingSpot> spotOpt = parkingLot.findSpotFor(vehicle);
        if (!spotOpt.isPresent()) {
            System.out.println("No suitable spot available. Entry denied.");
            return;
        }
        ParkingSpot spot = spotOpt.get();
        Ticket ticket = parkingLot.issueTicket(vehicle, spot);
        System.out.println("\n--- TICKET ISSUED ---");
        System.out.println(ticket.summary());
    }
    private static void vehicleExit() {
        if (!ensureLotConfigured()) return;
        System.out.print("Enter ticket id: ");
        String tid = scanner.nextLine().trim();
        Optional<Ticket> t = parkingLot.findTicket(tid);
        if (!t.isPresent()) {
            System.out.println("Ticket not found.");
            return;
        }
        Ticket ticket = t.get();
        ExitGate gate = new ExitGate(parkingLot);
        if (ticket.isPaid()) {
            System.out.println("Ticket already paid. Processing exit...");
            gate.processExit(ticket.getId());
            System.out.println("Exit completed. Spot freed.");
            return;
        }
        double fee = ticket.calculateFee(parkingLot.getTariff());
        System.out.println("\n--- FEE BREAKDOWN ---");
        System.out.println(ticket.detailedFeeBreakdown(parkingLot.getTariff()));
        System.out.printf("Total payable: ₹%.2f\n", fee);
        System.out.print("Pay now? (y/n): ");
        String resp = scanner.nextLine().trim().toLowerCase();
        if (!resp.equals("y")) {
            System.out.println("Payment required before exit. Aborting exit.");
            return;
        }
        System.out.print("Enter payment method (CASH/CARD/UPI): ");
        String method = scanner.nextLine().trim();
        Payment payment = parkingLot.acceptPayment(ticket, fee, method);
        System.out.println("\n--- PAYMENT RECEIPT ---");
        System.out.println(payment.receipt());
        boolean success = gate.processExit(ticket.getId());
        if (success) System.out.println("Exit completed and spot freed.");
        else System.out.println("Exit failed.");
    }
    private static void displayOccupancy() {
        if (!ensureLotConfigured()) return;
        System.out.println(parkingLot.occupancySnapshot());
    }
    private static boolean ensureLotConfigured() {
        if (parkingLot == null) {
            System.out.println("Configure a parking lot first (Menu option 1).");
            return false;
        }
        return true;
    }
    private static int readInt() {
        while (true) {
            try {
                String s = scanner.nextLine().trim();
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.print("Enter a valid integer: ");
            }
        }
    }
    private static double readDouble() {
        while (true) {
            try {
                String s = scanner.nextLine().trim();
                return Double.parseDouble(s);
            } catch (Exception e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}

