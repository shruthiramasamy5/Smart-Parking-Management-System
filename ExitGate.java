package parking;
public class ExitGate {
    private final ParkingLot lot;
    public ExitGate(ParkingLot lot) {
        this.lot = lot;
    }
    /** Process exit by ticket id. Returns true if successful. */
    public boolean processExit(String ticketId) {
        return lot.processExit(ticketId);
    }
}
