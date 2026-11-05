//Vehicle - the base class for everything that can be rented.
//Car, Motorcycle and Van extend this class and override

public class Vehicle {

    private String plateNumber;
    private String make;
    private String model;
    private double dailyRate;      
    private boolean available;     

    public static final int DISCOUNT_DAYS = 7;         
    public static final double DISCOUNT_RATE = 0.10;   

    public Vehicle(String plateNumber, String make, String model, double dailyRate) {
        this.plateNumber = plateNumber.toUpperCase();
        this.make = make;
        this.model = model;
        setDailyRate(dailyRate);   
        this.available = true;     
    }

    public String getPlateNumber() { return plateNumber; }
    public String getMake()        { return make; }
    public String getModel()       { return model; }
    public double getDailyRate()   { return dailyRate; }
    public boolean isAvailable()   { return available; }

    public final void setDailyRate(double dailyRate) {
        if (dailyRate > 0) {
            this.dailyRate = dailyRate;
        } else {
            throw new IllegalArgumentException("Daily rate must be greater than zero.");
        }
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


    public String getVehicleType() {
        return "Vehicle";
    }

    public String getDetails() {
        return "-";
    }

    public double getExtraFee() {
        return 0;
    }

    public String getFeeName() {
        return "Extra fee";
    }

    public double calculateBaseCost(int days) {
        return dailyRate * days;
    }

    public double calculateDiscount(int days) {
        if (days >= DISCOUNT_DAYS) {
            return calculateBaseCost(days) * DISCOUNT_RATE;
        }
        return 0;
    }

    public double calculateRentalCost(int days) {
        return calculateBaseCost(days) - calculateDiscount(days) + getExtraFee();
    }

    @Override
    public String toString() {
        return plateNumber + " - " + make + " " + model + " (" + getVehicleType() + ")";
    }
}
