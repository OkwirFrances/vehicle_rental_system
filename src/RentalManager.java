
 //RentalManager - the "brain" of the application.

public class RentalManager {

    private static final int MAX_ITEMS = 100;

    private Vehicle[] vehicles = new Vehicle[MAX_ITEMS];
    private Customer[] customers = new Customer[MAX_ITEMS];
    private Rental[] rentals = new Rental[MAX_ITEMS];

    private int vehicleCount = 0;
    private int customerCount = 0;
    private int rentalCount = 0;


    public void addVehicle(Vehicle v) throws RentalException {
        if (vehicleCount >= MAX_ITEMS) {
            throw new RentalException("The vehicle list is full (" + MAX_ITEMS + ").");
        }
        if (findVehicle(v.getPlateNumber()) != null) {
            throw new RentalException("A vehicle with plate " + v.getPlateNumber() + " already exists.");
        }
        vehicles[vehicleCount] = v;
        vehicleCount++;
    }

    public Customer registerCustomer(String name, String phone) throws RentalException {
        if (customerCount >= MAX_ITEMS) {
            throw new RentalException("The customer list is full (" + MAX_ITEMS + ").");
        }
        if (name == null || name.trim().length() < 2) {
            throw new RentalException("Please enter the customer's full name.");
        }
        if (!isValidPhone(phone)) {
            throw new RentalException("Enter a valid phone number, e.g. 0772 123 456 or +256 772 123 456.");
        }
        Customer c = new Customer(name.trim(), phone.trim());
        customers[customerCount] = c;
        customerCount++;
        return c;
    }

    public static boolean isValidPhone(String phone) {
        String p = phone.replace(" ", "").replace("-", "");
        int start = 0;
        int expectedDigits = 10;
        if (p.startsWith("+256")) {
            start = 1;
            expectedDigits = 12;
        }
        if (p.length() - start != expectedDigits) {
            return false;
        }
        for (int i = start; i < p.length(); i++) {
            if (!Character.isDigit(p.charAt(i))) {
                return false;
            }
        }
        return start == 1 || p.charAt(0) == '0';
    }


    public Rental rentVehicle(Vehicle v, Customer c, int days) throws RentalException {
        if (v == null || c == null) {
            throw new RentalException("Please select both a vehicle and a customer.");
        }
        if (days <= 0) {
            throw new RentalException("Number of days must be at least 1.");
        }
        if (!v.isAvailable()) {
            throw new RentalException("Vehicle " + v.getPlateNumber() + " is already rented out.");
        }
        if (rentalCount >= MAX_ITEMS) {
            throw new RentalException("The rental list is full (" + MAX_ITEMS + ").");
        }
        Rental r = new Rental(v, c, days);
        rentals[rentalCount] = r;
        rentalCount++;
        return r;
    }

    public void returnVehicle(String rentalId) throws RentalException {
        Rental r = findRental(rentalId);
        if (r == null) {
            throw new RentalException("Rental " + rentalId + " was not found.");
        }
        if (!r.isActive()) {
            throw new RentalException("Rental " + rentalId + " has already been closed.");
        }
        r.closeRental();
    }


    public Vehicle findVehicle(String plate) {
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].getPlateNumber().equalsIgnoreCase(plate)) {
                return vehicles[i];
            }
        }
        return null;
    }

    public Rental findRental(String rentalId) {
        int i = 0;
        while (i < rentalCount) {
            if (rentals[i].getRentalId().equalsIgnoreCase(rentalId)) {
                return rentals[i];
            }
            i++;
        }
        return null;
    }

  
    public Vehicle[] searchVehicles(String keyword, boolean availableOnly) {
        String key = keyword.trim().toLowerCase();
        int matches = 0;
        for (int i = 0; i < vehicleCount; i++) {
            if (matchesSearch(vehicles[i], key, availableOnly)) {
                matches++;
            }
        }
        Vehicle[] result = new Vehicle[matches];
        int index = 0;
        for (int i = 0; i < vehicleCount; i++) {
            if (matchesSearch(vehicles[i], key, availableOnly)) {
                result[index] = vehicles[i];
                index++;
            }
        }
        return result;
    }

    private boolean matchesSearch(Vehicle v, String key, boolean availableOnly) {
        if (availableOnly && !v.isAvailable()) {
            return false;
        }
        String text = (v.getPlateNumber() + " " + v.getMake() + " " + v.getModel()
                + " " + v.getVehicleType()).toLowerCase();
        return text.contains(key);
    }

    /** Returns only the vehicles that can be rented right now. */
    public Vehicle[] getAvailableVehicles() {
        Vehicle[] result = new Vehicle[countAvailable()];
        int index = 0;
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].isAvailable()) {
                result[index] = vehicles[i];
                index++;
            }
        }
        return result;
    }

    public int countAvailable() {
        int count = 0;
        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].isAvailable()) {
                count++;
            }
        }
        return count;
    }

    public int countActiveRentals() {
        int count = 0;
        for (int i = 0; i < rentalCount; i++) {
            if (rentals[i].isActive()) {
                count++;
            }
        }
        return count;
    }

    public double getTotalRevenue() {
        double total = 0;
        for (int i = 0; i < rentalCount; i++) {
            if (!rentals[i].isActive()) {
                total += rentals[i].getTotalCost();
            }
        }
        return total;
    }

    

    public Vehicle[] getVehicles()   { return java.util.Arrays.copyOf(vehicles, vehicleCount); }
    public Customer[] getCustomers() { return java.util.Arrays.copyOf(customers, customerCount); }
    public Rental[] getRentals()     { return java.util.Arrays.copyOf(rentals, rentalCount); }

    

    public void loadSampleData() {
        try {
            addVehicle(new Car("UBA 123A", "Toyota", "Premio", 150000, 5));
            addVehicle(new Car("UBB 456B", "Toyota", "RAV4", 220000, 5));
            addVehicle(new Motorcycle("UFD 789C", "Bajaj", "Boxer", 35000, 150));
            addVehicle(new Motorcycle("UFE 321D", "TVS", "HLX", 30000, 125));
            addVehicle(new Van("UBC 654E", "Toyota", "Hiace", 250000, 1200));
            registerCustomer("Amina Nakato", "0772 100 200");
            registerCustomer("David Okello", "0701 300 400");
        } catch (RentalException e) {
            System.out.println("Could not load sample data: " + e.getMessage());
        }
    }
}
