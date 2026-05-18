package parking;
public class Vehicle {
    private final String regNo;
    private final VehicleType type;
    public Vehicle(String regNo, VehicleType type) {
        this.regNo = regNo;
        this.type = type;
    }
    public String getRegNo() { return regNo; }
    public VehicleType getType() { return type; }
    public SpotType requiredSpot() {
        switch (type) {
            case SMALL: return SpotType.SMALL;
            case MEDIUM: return SpotType.MEDIUM;
            case LARGE: return SpotType.LARGE;
            default: return SpotType.MEDIUM;
        }
    }

    @Override
    public String toString() {
        return String.format("Vehicle[%s, %s]", regNo, type);
    }
}
