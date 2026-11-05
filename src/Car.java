public class Car extends Vehicle {

    private static final double INSURANCE_FEE = 20000;   
    private int seats;

    public Car(String plateNumber, String make, String model, double dailyRate, int seats) {
        super(plateNumber, make, model, dailyRate);
        this.seats = seats;
    }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    @Override
    public String getVehicleType() { return "Car"; }

    @Override
    public String getDetails() { return seats + " seats"; }

    @Override
    public double getExtraFee() { return INSURANCE_FEE; }

    @Override
    public String getFeeName() { return "Insurance fee"; }
}
