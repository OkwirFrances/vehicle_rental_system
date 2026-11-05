/** Van*/
public class Van extends Vehicle {

    private static final double COMMERCIAL_FEE = 40000;   // UGX per rental
    private int capacityKg;

    public Van(String plateNumber, String make, String model, double dailyRate, int capacityKg) {
        super(plateNumber, make, model, dailyRate);
        this.capacityKg = capacityKg;
    }

    public int getCapacityKg() { return capacityKg; }
    public void setCapacityKg(int capacityKg) { this.capacityKg = capacityKg; }

    @Override
    public String getVehicleType() { return "Van"; }

    @Override
    public String getDetails() { return capacityKg + " kg"; }

    @Override
    public double getExtraFee() { return COMMERCIAL_FEE; }

    @Override
    public String getFeeName() { return "Commercial cover"; }
}
