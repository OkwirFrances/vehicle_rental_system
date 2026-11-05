/** Motorcycle */
public class Motorcycle extends Vehicle {

    private static final double HELMET_FEE = 5000;   // UGX per rental
    private int engineCC;

    public Motorcycle(String plateNumber, String make, String model, double dailyRate, int engineCC) {
        super(plateNumber, make, model, dailyRate);
        this.engineCC = engineCC;
    }

    public int getEngineCC() { return engineCC; }
    public void setEngineCC(int engineCC) { this.engineCC = engineCC; }

    @Override
    public String getVehicleType() { return "Motorcycle"; }

    @Override
    public String getDetails() { return engineCC + " cc"; }

    @Override
    public double getExtraFee() { return HELMET_FEE; }

    @Override
    public String getFeeName() { return "Helmet fee"; }
}
