public class Rental {

    private static int nextNumber = 1;

    private String rentalId;
    private Vehicle vehicle;
    private Customer customer;
    private int days;
    private double totalCost;
    private boolean active;

    public Rental(Vehicle vehicle, Customer customer, int days) {
        this.rentalId = String.format("R%03d", nextNumber);
        nextNumber++;
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
        this.totalCost = vehicle.calculateRentalCost(days);
        this.active = true;
        vehicle.setAvailable(false);
    }

    public void closeRental() {
        active = false;
        vehicle.setAvailable(true);
    }

    public String getRentalId()  { return rentalId; }
    public Vehicle getVehicle()  { return vehicle; }
    public Customer getCustomer(){ return customer; }
    public int getDays()         { return days; }
    public double getTotalCost() { return totalCost; }
    public boolean isActive()    { return active; }
}
