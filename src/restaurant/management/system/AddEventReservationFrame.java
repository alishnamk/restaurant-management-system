package restaurant.management.system;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.awt.event.ActionListener;



public class AddEventReservationFrame extends JFrame implements ActionListener {

    private JComboBox<String> customerComboBox;
    private JButton clearButton;
    private JTextArea resultTextArea;

    // Service charges
    private final double SERVICE_CHARGE = 50.0;

    public AddEventReservationFrame() {
        setTitle("Add Event Reservation");
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
            String query = "SELECT customer_name FROM reservations";
            ResultSet rs = con.s.executeQuery(query);

            // Add customer names to the combo box
            while (rs.next()) {
                String name = rs.getString("customer_name");
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
        customerLabel.setBounds(30, 50, 120, 25);
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
        String query = "SELECT event_type, reservation_date, estimated_people FROM reservations WHERE customer_name = ?";
        PreparedStatement pstmt = con.c.prepareStatement(query);
        pstmt.setString(1, customerName);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String eventType = rs.getString("event_type");
            Date reservationDate = rs.getDate("reservation_date");
            int estimatedPeople = rs.getInt("estimated_people");

            double totalPrice = calculateTotalPrice(eventType, estimatedPeople);

            // Insert transaction details into the event_bills table
            insertTransaction(customerName, eventType, reservationDate, estimatedPeople, totalPrice);

            // Format the result text
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String dateStr = dateFormat.format(reservationDate);
            String timeStr = timeFormat.format(reservationDate);
            String billText = String.format(
                    "\tPlant Day Cafe\n" +
                    "Customer Name: %s\n" +
                    "Reservation Date: %s\n" +
                    "Event Time: %s\n" +
                    "Event Type: %s\n" +
                    "Number of People: %d\n" +
                    "\tTotal: Rs %.2f",
                    customerName, dateStr, timeStr, eventType, estimatedPeople, totalPrice
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



    private double calculateTotalPrice(String eventType, int estimatedPeople) {
    // Initialize eventTypePrice with a default value
    double eventTypePrice = 300.0; // Default price for other event types

    // Set price based on event type
    if (eventType.equalsIgnoreCase("Birthday")) {
        eventTypePrice = 250.0;
    } else if (eventType.equalsIgnoreCase("Anniversary")) {
        eventTypePrice = 300.0;
    } else if (eventType.equalsIgnoreCase("Business Meeting")) {
        eventTypePrice = 500.0;
    }

    // Calculate total price including service charge
    double totalPrice = eventTypePrice * estimatedPeople + SERVICE_CHARGE;
    return totalPrice;
}


    private void insertTransaction(String customerName, String eventType, Date reservationDate, int estimatedPeople, double totalPrice) {
        try {
            Conn con = new Conn();
            String query = "INSERT INTO event_bills (customer_name, event_type, reservation_date, attendees, total_price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.c.prepareStatement(query);
            pstmt.setString(1, customerName);
            pstmt.setString(2, eventType);
            pstmt.setDate(3, new java.sql.Date(reservationDate.getTime()));
            pstmt.setInt(4, estimatedPeople);
            pstmt.setDouble(5, totalPrice);
            pstmt.executeUpdate();
            con.c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving transaction. Please try again.");
        }
    }

    public static void main(String[] args) {
        new AddEventReservationFrame();
    }
}
    