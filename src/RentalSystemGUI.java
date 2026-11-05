import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class RentalSystemGUI extends JFrame {

   
    private static final Color NAVY = new Color(31, 58, 95);
    private static final Color LIGHT_ROW = new Color(243, 247, 252);
    private static final Color GREEN = new Color(39, 134, 66);
    private static final Color ORANGE = new Color(205, 110, 10);
    private static final Color BLUE = new Color(30, 110, 200);
    private static final Color GREY_TEXT = new Color(90, 100, 115);

    private RentalManager manager;

   
    private JLabel availableValue, activeValue, revenueValue;

 
    private JLabel statusBar;

    private JComboBox<String> typeBox;
    private JTextField plateField, makeField, modelField, rateField, extraField, searchField;
    private JLabel extraLabel;
    private JCheckBox availableOnlyBox;
    private DefaultTableModel vehicleModel;

   
    private JTextField nameField, phoneField;
    private DefaultTableModel customerModel;

  
    private JComboBox<Vehicle> vehicleBox;
    private JComboBox<Customer> customerBox;
    private JSpinner daysSpinner;
    private JLabel sumVehicle, sumRate, sumBase, sumDiscount, sumFeeName, sumFee, sumTotal;

   
    private JCheckBox activeOnlyBox;
    private DefaultTableModel rentalModel;
    private JTable rentalTable;

    public RentalSystemGUI(RentalManager manager) {
        this.manager = manager;

        setTitle("Vehicle Rental System");
        setSize(1020, 700);
        setMinimumSize(new Dimension(900, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(tabs.getFont().deriveFont(Font.BOLD, 13f));
        tabs.addTab("  Vehicles  ", buildVehiclePanel());
        tabs.addTab("  Customers  ", buildCustomerPanel());
        tabs.addTab("  Rent a Vehicle  ", buildRentPanel());
        tabs.addTab("  Rentals & Returns  ", buildRentalsPanel());

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(10, 14, 6, 14));
        center.add(tabs, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        statusBar = new JLabel("Ready");
        statusBar.setForeground(GREY_TEXT);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(205, 212, 222)),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        add(statusBar, BorderLayout.SOUTH);

        refreshAll();
    }


    private JPanel buildHeader() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(NAVY);
        banner.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("Vehicle Rental System");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Fleet, customer and rental management");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(190, 205, 225));

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);
        titleBox.add(title);
        titleBox.add(subtitle);

        JLabel date = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")));
        date.setForeground(new Color(190, 205, 225));

        banner.add(titleBox, BorderLayout.WEST);
        banner.add(date, BorderLayout.EAST);

        availableValue = new JLabel("0");
        activeValue = new JLabel("0");
        revenueValue = new JLabel("UGX 0");

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setBorder(BorderFactory.createEmptyBorder(12, 14, 4, 14));
        cards.add(createStatCard("Available vehicles", availableValue, GREEN));
        cards.add(createStatCard("Active rentals", activeValue, ORANGE));
        cards.add(createStatCard("Revenue (returned rentals)", revenueValue, BLUE));

        JPanel header = new JPanel(new BorderLayout());
        header.add(banner, BorderLayout.NORTH);
        header.add(cards, BorderLayout.CENTER);
        return header;
    }

    private JPanel createStatCard(String caption, JLabel valueLabel, Color accent) {
        JLabel captionLabel = new JLabel(caption);
        captionLabel.setForeground(GREY_TEXT);
        captionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valueLabel.setForeground(accent);

        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);
        text.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 8));
        text.add(captionLabel);
        text.add(valueLabel);

        JPanel strip = new JPanel();
        strip.setBackground(accent);
        strip.setPreferredSize(new Dimension(6, 10));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(215, 221, 230)));
        card.add(strip, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }


    private JPanel buildVehiclePanel() {
        typeBox = new JComboBox<>(new String[]{"Car", "Motorcycle", "Van"});
        plateField = new JTextField(14);
        makeField = new JTextField(14);
        modelField = new JTextField(14);
        rateField = new JTextField(14);
        extraField = new JTextField(14);
        extraLabel = new JLabel("Seats");
        plateField.setToolTipText("e.g. UBA 123A");
        rateField.setToolTipText("Price per day in UGX, e.g. 150000");

        
        typeBox.addActionListener(e -> {
            String type = (String) typeBox.getSelectedItem();
            switch (type) {
                case "Car":
                    extraLabel.setText("Seats");
                    break;
                case "Motorcycle":
                    extraLabel.setText("Engine (cc)");
                    break;
                case "Van":
                    extraLabel.setText("Capacity (kg)");
                    break;
                default:
                    extraLabel.setText("Extra");
            }
        });

        JButton addButton = createPrimaryButton("Add Vehicle");
        addButton.addActionListener(e -> addVehicle());

        JPanel form = createFormPanel("Add a new vehicle");
        addRow(form, 0, new JLabel("Type"), typeBox);
        addRow(form, 1, new JLabel("Plate number"), plateField);
        addRow(form, 2, new JLabel("Make"), makeField);
        addRow(form, 3, new JLabel("Model"), modelField);
        addRow(form, 4, new JLabel("Daily rate (UGX)"), rateField);
        addRow(form, 5, extraLabel, extraField);
        addButtonRow(form, 6, addButton);

        
        searchField = new JTextField(18);
        searchField.setToolTipText("Search by plate, make, model or type, then press Enter");
        searchField.addActionListener(e -> refreshAll());
        availableOnlyBox = new JCheckBox("Available only");
        availableOnlyBox.addActionListener(e -> refreshAll());
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> refreshAll());
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            availableOnlyBox.setSelected(false);
            refreshAll();
        });

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(new JLabel("Search:"));
        toolbar.add(searchField);
        toolbar.add(searchButton);
        toolbar.add(clearButton);
        toolbar.add(availableOnlyBox);

        vehicleModel = createTableModel("Plate", "Type", "Make", "Model", "Details", "Rate / day", "Status");
        JTable table = createTable(vehicleModel, 6, 5);

        JPanel right = new JPanel(new BorderLayout(0, 6));
        right.add(toolbar, BorderLayout.NORTH);
        right.add(new JScrollPane(table), BorderLayout.CENTER);

        return createSplitTab(form, right);
    }

    private JPanel buildCustomerPanel() {
        nameField = new JTextField(14);
        phoneField = new JTextField(14);
        phoneField.setToolTipText("0772 123 456 or +256 772 123 456");

        JButton registerButton = createPrimaryButton("Register Customer");
        registerButton.addActionListener(e -> registerCustomer());

        JPanel form = createFormPanel("Register a new customer");
        addRow(form, 0, new JLabel("Full name"), nameField);
        addRow(form, 1, new JLabel("Phone"), phoneField);
        addButtonRow(form, 2, registerButton);

        customerModel = createTableModel("Customer ID", "Name", "Phone");
        JTable table = createTable(customerModel, -1);

        return createSplitTab(form, new JScrollPane(table));
    }

    private JPanel buildRentPanel() {
        vehicleBox = new JComboBox<>();
        customerBox = new JComboBox<>();
        vehicleBox.setPrototypeDisplayValue(null);
        daysSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 365, 1));

      
        vehicleBox.addActionListener(e -> updateSummary());
        daysSpinner.addChangeListener(e -> updateSummary());

        JButton rentButton = createPrimaryButton("Confirm Rental");
        rentButton.addActionListener(e -> rentVehicle());

        JPanel form = createFormPanel("New rental");
        addRow(form, 0, new JLabel("Available vehicle"), vehicleBox);
        addRow(form, 1, new JLabel("Customer"), customerBox);
        addRow(form, 2, new JLabel("Number of days"), daysSpinner);
        addButtonRow(form, 3, rentButton);

       
        sumVehicle = new JLabel("-");
        sumRate = new JLabel("-");
        sumBase = new JLabel("-");
        sumDiscount = new JLabel("-");
        sumFeeName = new JLabel("Extra fee");
        sumFee = new JLabel("-");
        sumTotal = new JLabel("-");
        sumTotal.setFont(new Font("SansSerif", Font.BOLD, 22));
        sumTotal.setForeground(NAVY);

        JPanel summary = createFormPanel("Price summary");
        addSummaryRow(summary, 0, new JLabel("Vehicle"), sumVehicle);
        addSummaryRow(summary, 1, new JLabel("Rate per day"), sumRate);
        addSummaryRow(summary, 2, new JLabel("Subtotal"), sumBase);
        addSummaryRow(summary, 3, new JLabel("Long-rental discount (7+ days)"), sumDiscount);
        addSummaryRow(summary, 4, sumFeeName, sumFee);
        JLabel totalCaption = new JLabel("TOTAL");
        totalCaption.setFont(new Font("SansSerif", Font.BOLD, 16));
        addSummaryRow(summary, 5, totalCaption, sumTotal);

        JPanel summaryWrap = new JPanel(new BorderLayout());
        summaryWrap.add(summary, BorderLayout.NORTH);

        return createSplitTab(form, summaryWrap);
    }

    private JPanel buildRentalsPanel() {
        rentalModel = createTableModel("Rental ID", "Customer", "Vehicle", "Days", "Total", "Status");
        rentalTable = createTable(rentalModel, 5, 4);
        rentalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        activeOnlyBox = new JCheckBox("Show active rentals only");
        activeOnlyBox.addActionListener(e -> refreshAll());

        JButton returnButton = createPrimaryButton("Return Selected Vehicle");
        returnButton.addActionListener(e -> returnVehicle());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        top.add(activeOnlyBox);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        bottom.add(returnButton);

        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(rentalTable), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }


    private void addVehicle() {
        String plate = plateField.getText().trim();
        String make = makeField.getText().trim();
        String model = modelField.getText().trim();

        if (plate.isEmpty() || make.isEmpty() || model.isEmpty()) {
            showError("Please fill in the plate number, make and model.");
            return;
        }

        try {
            double rate = Double.parseDouble(rateField.getText().trim());
            int extra = Integer.parseInt(extraField.getText().trim());
            if (extra <= 0) {
                showError(extraLabel.getText() + " must be greater than zero.");
                return;
            }
            String type = (String) typeBox.getSelectedItem();

            Vehicle v;
            if (type.equals("Car")) {
                v = new Car(plate, make, model, rate, extra);
            } else if (type.equals("Motorcycle")) {
                v = new Motorcycle(plate, make, model, rate, extra);
            } else {
                v = new Van(plate, make, model, rate, extra);
            }

            manager.addVehicle(v);
            clearFields(plateField, makeField, modelField, rateField, extraField);
            refreshAll();
            setStatus("Vehicle " + v.getPlateNumber() + " added to the fleet.");

        } catch (NumberFormatException ex) {
            showError("Daily rate and " + extraLabel.getText().toLowerCase() + " must be valid numbers.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (RentalException ex) {
            showError(ex.getMessage());
        }
    }

    private void registerCustomer() {
        try {
            Customer c = manager.registerCustomer(nameField.getText(), phoneField.getText());
            clearFields(nameField, phoneField);
            refreshAll();
            setStatus("Customer " + c.getName() + " registered with ID " + c.getCustomerId() + ".");
        } catch (RentalException ex) {
            showError(ex.getMessage());
        }
    }

    private void rentVehicle() {
        Vehicle v = (Vehicle) vehicleBox.getSelectedItem();
        Customer c = (Customer) customerBox.getSelectedItem();
        int days = (Integer) daysSpinner.getValue();

        try {
            Rental r = manager.rentVehicle(v, c, days);
            refreshAll();
            setStatus("Rental " + r.getRentalId() + " created for " + c.getName() + ".");
            showReceipt(r);
        } catch (RentalException ex) {
            showError(ex.getMessage());
        }
    }

    private void returnVehicle() {
        int row = rentalTable.getSelectedRow();
        if (row == -1) {
            showError("Select a rental in the table first.");
            return;
        }

        String rentalId = (String) rentalModel.getValueAt(row, 0);
        int answer = JOptionPane.showConfirmDialog(this,
                "Confirm that the vehicle for rental " + rentalId + " has been returned?",
                "Return vehicle", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            manager.returnVehicle(rentalId);
            refreshAll();
            setStatus("Rental " + rentalId + " closed. The vehicle is available again.");
        } catch (RentalException ex) {
            showError(ex.getMessage());
        }
    }


    private void refreshAll() {
       
        vehicleModel.setRowCount(0);
        Vehicle[] shown = manager.searchVehicles(searchField.getText(), availableOnlyBox.isSelected());
        for (Vehicle v : shown) {
            String status;
            if (v.isAvailable()) {
                status = "Available";
            } else {
                status = "Rented";
            }
            vehicleModel.addRow(new Object[]{v.getPlateNumber(), v.getVehicleType(), v.getMake(),
                    v.getModel(), v.getDetails(), money(v.getDailyRate()), status});
        }

       
        customerModel.setRowCount(0);
        for (Customer c : manager.getCustomers()) {
            customerModel.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getPhone()});
        }

       
        rentalModel.setRowCount(0);
        for (Rental r : manager.getRentals()) {
            if (activeOnlyBox.isSelected() && !r.isActive()) {
                continue;   
            }
            String status = r.isActive() ? "Active" : "Returned";
            rentalModel.addRow(new Object[]{r.getRentalId(), r.getCustomer().getName(),
                    r.getVehicle().toString(), r.getDays(), money(r.getTotalCost()), status});
        }

        vehicleBox.setModel(new DefaultComboBoxModel<>(manager.getAvailableVehicles()));
        customerBox.setModel(new DefaultComboBoxModel<>(manager.getCustomers()));
        updateSummary();

        availableValue.setText(String.valueOf(manager.countAvailable()));
        activeValue.setText(String.valueOf(manager.countActiveRentals()));
        revenueValue.setText(money(manager.getTotalRevenue()));
    }

    private void updateSummary() {
        Vehicle v = (Vehicle) vehicleBox.getSelectedItem();
        if (v == null) {
            sumVehicle.setText("No vehicle available");
            sumRate.setText("-");
            sumBase.setText("-");
            sumDiscount.setText("-");
            sumFeeName.setText("Extra fee");
            sumFee.setText("-");
            sumTotal.setText("-");
            return;
        }
        int days = (Integer) daysSpinner.getValue();
        sumVehicle.setText(v.getMake() + " " + v.getModel() + " (" + v.getPlateNumber() + ")");
        sumRate.setText(money(v.getDailyRate()));
        sumBase.setText(money(v.calculateBaseCost(days)));
        sumDiscount.setText("- " + money(v.calculateDiscount(days)));
        sumFeeName.setText(v.getFeeName());
        sumFee.setText("+ " + money(v.getExtraFee()));
        sumTotal.setText(money(v.calculateRentalCost(days)));
    }


    private JPanel createFormPanel(String title) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(6, 10, 10, 10)));
        return form;
    }

    private void addRow(JPanel form, int row, JComponent label, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(5, 2, 5, 8);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        form.add(label, c);
        c.gridx = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, c);
    }

    private void addSummaryRow(JPanel form, int row, JComponent label, JLabel value) {
        value.setHorizontalAlignment(SwingConstants.RIGHT);
        addRow(form, row, label, value);
    }

    private void addButtonRow(JPanel form, int row, JButton button) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.gridx = 0;
        c.gridwidth = 2;
        c.insets = new Insets(14, 2, 2, 2);
        c.fill = GridBagConstraints.HORIZONTAL;
        form.add(button, c);
    }

    private JPanel createSplitTab(JPanel form, JComponent content) {
        JPanel left = new JPanel(new BorderLayout());
        left.add(form, BorderLayout.NORTH);
        left.setPreferredSize(new Dimension(400, 10));

        JPanel tab = new JPanel(new BorderLayout(14, 0));
        tab.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        tab.add(left, BorderLayout.WEST);
        tab.add(content, BorderLayout.CENTER);
        return tab;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(NAVY);
        button.setForeground(Color.WHITE);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 13f));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 36));
        return button;
    }

    private DefaultTableModel createTableModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

   
    private JTable createTable(DefaultTableModel model, int statusColumn, int... moneyColumns) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : LIGHT_ROW);
                }
                return c;
            }
        };
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setFont(table.getFont().deriveFont(13f));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        table.getTableHeader().setPreferredSize(new Dimension(10, 32));

        for (int col : moneyColumns) {
            DefaultTableCellRenderer right = new DefaultTableCellRenderer();
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            table.getColumnModel().getColumn(col).setCellRenderer(right);
            table.getColumnModel().getColumn(col).setPreferredWidth(140);   // wide enough for "UGX 250,000"
        }
        if (statusColumn >= 0) {
            table.getColumnModel().getColumn(statusColumn).setCellRenderer(new StatusRenderer());
        }
        return table;
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.LEFT);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, false, false, row, column);
            setBackground(NAVY);
            setForeground(Color.WHITE);
            setFont(getFont().deriveFont(Font.BOLD, 13f));
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            return this;
        }
    }

    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(getFont().deriveFont(Font.BOLD));
            if (!isSelected) {
                String text = String.valueOf(value);
                if (text.equals("Available") || text.equals("Returned")) {
                    setForeground(GREEN);
                } else {
                    setForeground(ORANGE);
                }
            }
            return this;
        }
    }


    private String money(double amount) {
        return String.format("UGX %,.0f", amount);
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields) {
            f.setText("");
        }
    }

    private void setStatus(String message) {
        statusBar.setText(message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Please check your input", JOptionPane.WARNING_MESSAGE);
    }

    private void showReceipt(Rental r) {
        String text = String.format(
                "RENTAL RECEIPT\n"
              + "------------------------------------\n"
              + "Rental ID : %s\n"
              + "Customer  : %s (%s)\n"
              + "Vehicle   : %s\n"
              + "Days      : %d\n"
              + "Subtotal  : %s\n"
              + "Discount  : - %s\n"
              + "%-10s: + %s\n"
              + "------------------------------------\n"
              + "TOTAL     : %s\n",
                r.getRentalId(), r.getCustomer().getName(), r.getCustomer().getCustomerId(),
                r.getVehicle().toString(), r.getDays(),
                money(r.getVehicle().calculateBaseCost(r.getDays())),
                money(r.getVehicle().calculateDiscount(r.getDays())),
                r.getVehicle().getFeeName(), money(r.getVehicle().getExtraFee()),
                money(r.getTotalCost()));

        JTextArea area = new JTextArea(text);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setEditable(false);
        area.setOpaque(false);
        JOptionPane.showMessageDialog(this, area, "Rental created", JOptionPane.INFORMATION_MESSAGE);
    }
}
