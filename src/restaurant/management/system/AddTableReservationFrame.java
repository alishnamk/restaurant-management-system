package restaurant.management.system;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class AddTableReservationFrame extends JFrame implements ActionListener {

    private JComboBox<String> customerComboBox;
    private JButton clearButton;
    private JTextArea resultTextArea;

    public AddTableReservationFrame() {
        setTitle("Add Table Reservation");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize JComboBox
        customerComboBox = new JComboBox<>();
        loadCustomers();

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            customerComboBox.setSelectedIndex(-1); // Clear selection
            resultTextArea.setText(""); // Clear result
        } else if (e.getActionCommand().equals("Generate Bill")) {
            generateBill();
        }
    }

    private void loadCustomers() {
        try {
            // Connect to the database
            Conn con = new Conn();

            // Execute query to fetch customer names
            String query = "SELECT CustomerName FROM tablereservations";
            ResultSet rs = con.s.executeQuery(query);

            // Add customer names to the combo box
            while (rs.next()) {
                String name = rs.getString("CustomerName");
                customerComboBox.addItem(name);
            }

            // Close connections
            rs.close();
            con.c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel customerLabel = new JLabel("Customer Name:");
        customerLabel.setBounds
        (30, 50, 120, 25);
        panel.add(customerLabel);

        customerComboBox.setBounds(160, 50, 200, 25);
        panel.add(customerComboBox);

        clearButton = new JButton("Clear");
        clearButton.setBounds(30, 100, 100, 30);
        clearButton.addActionListener(this);
        panel.add(clearButton);

        JButton generateButton = new JButton("Generate Bill");
        generateButton.setBounds(160, 100, 150, 30);
        generateButton.addActionListener(this);
        panel.add(generateButton);

        resultTextArea = new JTextArea();
        resultTextArea.setBounds(30, 150, 310, 150);
        resultTextArea.setEditable(false);
        panel.add(resultTextArea);
    }

    private void generateBill() {
    String customerName = (String) customerComboBox.getSelectedItem();

    if (customerName == null || customerName.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a customer.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Fetch reservation details for the selected customer
    try {
        Conn con = new Conn();
        String query = "SELECT ReservationDate, TableSize, EstimatedTime FROM tablereservations WHERE CustomerName = ?";
        PreparedStatement pstmt = con.c.prepareStatement(query);
        pstmt.setString(1, customerName);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Date reservationDate = rs.getDate("ReservationDate");
            String tableSize = rs.getString("TableSize");
            String estimatedTime = rs.getString("EstimatedTime");

            // Calculate total price
            double totalPrice = calculateTotalPrice(tableSize);

            // Insert bill into the database
            insertBill(customerName, reservationDate, tableSize, estimatedTime, totalPrice);

            // Format the result text
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(reservationDate);
            String billText = String.format(
                    "\tTable Reservation Bill\n" +
                            "Customer Name: %s\n" +
                            "Reservation Date: %s\n" +
                            "Table Size: %s\n" +
                            "Estimated Time: %s\n" +
                            "Total Price: %.2f",
                    customerName, dateStr, tableSize, estimatedTime, totalPrice
            );

            // Display result
            resultTextArea.setText(billText);
        } else {
            JOptionPane.showMessageDialog(this, "No reservation found for the selected customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        rs.close();
        pstmt.close();
        con.c.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error generating bill. Please try again.");
    }
}

private double calculateTotalPrice(String tableSize) {
    // Define prices for different table sizes
    double price;
    switch (tableSize) {
        case "2":
            price = 200.0;
            break;
        case "3-4":
            price = 300.0;
            break;
        case "6-9":
            price = 400.0;
            break;
        case "10-15":
            price = 500.0;
            break;
        case "18":
            price = 600.0;
            break;
        default:
            price = 100.0; // Default price
    }

    // Add service charge
    double serviceCharge = 10.0;
    return price + serviceCharge;
}

private void insertBill(String customerName, Date reservationDate, String tableSize, String estimatedTime, double totalPrice) {
    try {
        Conn con = new Conn();
        String query = "INSERT INTO table_bills (CustomerName, ReservationDate, TableSize, EstimatedTime, TotalPrice) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.c.prepareStatement(query);
        pstmt.setString(1, customerName);
        pstmt.setDate(2, new java.sql.Date(reservationDate.getTime()));
        pstmt.setString(3, tableSize);
        pstmt.setString(4, estimatedTime);
        pstmt.setDouble(5, totalPrice);
        pstmt.executeUpdate();
        pstmt.close();
        con.c.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error inserting bill into the database.");
    }
}


    public static void main(String[] args) {
        new AddTableReservationFrame();
    }
}
