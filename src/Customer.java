public class Customer {

    private static int nextNumber = 1;   

    private String customerId;
    private String name;
    private String phone;

    public Customer(String name, String phone) {
        this.customerId = String.format("C%03d", nextNumber);   // C001, C002, ...
        nextNumber++;
        this.name = name;
        this.phone = phone;
    }

    public String getCustomerId() { return customerId; }
    public String getName()       { return name; }
    public String getPhone()      { return phone; }

    public void setName(String name)   { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return customerId + " - " + name;
    }
}
