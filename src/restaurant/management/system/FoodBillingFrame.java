package restaurant.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodBillingFrame extends JFrame implements ActionListener {

    private JTextField customerNameField, quantityField;
    private JComboBox<String> itemComboBox;
    private JButton submitButton, generateBillButton, clearButton; // Added clearButton
    private JTextArea billTextArea;

    private int billNoCounter = 1; // Counter for generating unique bill numbers

    public FoodBillingFrame() {
        setTitle("Restaurant Billing System");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the GUI on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel restaurantNameLabel = new JLabel("Plant Day Cafe", SwingConstants.CENTER);
        restaurantNameLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        restaurantNameLabel.setBounds(30, 20, 700, 50);
        panel.add(restaurantNameLabel);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(30, 100, 120, 25);
        panel.add(nameLabel);

        customerNameField = new JTextField(20);
        customerNameField.setBounds(160, 100, 200, 25);
        panel.add(customerNameField);

        JLabel itemLabel = new JLabel("Item:");
        itemLabel.setBounds(30, 150, 120, 25);
        panel.add(itemLabel);

        // Populate items from database
        itemComboBox = new JComboBox<>();
        itemComboBox.setBounds(160, 150, 200, 25);
        populateItemComboBox(); // Populate items
        panel.add(itemComboBox);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 200, 120, 25);
        panel.add(quantityLabel);

        quantityField = new JTextField(5);
        quantityField.setBounds(160, 200, 50, 25);
        panel.add(quantityField);

        submitButton = new JButton("Add Item");
        submitButton.setBounds(30, 250, 150, 25);
        submitButton.addActionListener(this);
        panel.add(submitButton);

        generateBillButton = new JButton("Generate Bill");
        generateBillButton.setBounds(200, 250, 150, 25);
        generateBillButton.addActionListener(this);
        panel.add(generateBillButton);

        clearButton = new JButton("Clear"); // Added Clear button
        clearButton.setBounds(370, 250, 100, 25); // Adjusted position
        clearButton.addActionListener(this); // Added action listener
        panel.add(clearButton); // Added Clear button

        billTextArea = new JTextArea();
        // Center-align the text within the text area
        billTextArea.setBounds(30, 300, 700, 250);
        billTextArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        billTextArea.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        billTextArea.setEditable(false);
        panel.add(billTextArea);
    }

    private void populateItemComboBox() {
        String[] items = {
                "Butter Chicken", "Palak Paneer", "Chicken Biryani", "Masala Dosa",
                "Paneer Tikka", "Chole Bhature", "Mutton Rogan Josh", "Pani Puri",
                "Samosa", "Gulab Jamun", "Jalebi", "Rasgulla", "Chole Kulche",
                "Aloo Paratha", "Vada Pav", "Dhokla", "Pav Bhaji", "Matar Paneer",
                "Chicken Korma", "Tandoori Roti"
        };
        for (String item : items) {
            itemComboBox.addItem(item);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            addToBill();
        } else if (e.getSource() == generateBillButton) {
            generateBill();
        } else if (e.getSource() == clearButton) { // Added action for Clear button
            clearBill();
        }
    }

    private void addToBill() {
        String item = (String) itemComboBox.getSelectedItem();
        String quantityText = quantityField.getText();
        if (quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int quantity = Integer.parseInt(quantityText);
        double price = getItemPriceFromDatabase(item);
        double total = price * quantity;
        String newItem = String.format("%s x %d = %.2f INR\n", item, quantity, total);
        billTextArea.append(newItem);

        // Store sold item in the database
        storeTransactionInDatabase(item, quantity, total);

        quantityField.setText("");
    }

    private double getItemPriceFromDatabase(String item) {
        try {
            Conn con = new Conn();
            String query = "SELECT price_in_inr FROM menu_items WHERE item_name = ?";
            PreparedStatement pstmt = con.c.prepareStatement(query);
            pstmt.setString(1, item);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price_in_inr");
            } else {
                return 0.0; // Return default price if item not found
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0.0; // Return default price if error occurs
        }
    }

    private void generateBill() {
        double totalBill = 0.0;
        StringBuilder billSummary = new StringBuilder();

        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateAndTime = dateFormat.format(new Date());

        
        billSummary.append("Plant Day Cafe\n");
        billSummary.append("Date: ").append(currentDateAndTime).append("\n\n");
        billSummary.append("Bill Summary:\n");

        // Iterate through each line of the billTextArea
        String[] lines = billTextArea.getText().split("\n");
        for (String line : lines) {
            // Extract item name, quantity, and total price from each line
            String[] parts = line.split(" x ");
            String itemName = parts[0];
            int quantity = Integer.parseInt(parts[1].split(" = ")[0]);
            double totalPrice = Double.parseDouble(parts[1].split(" = ")[1].split(" ")[0]);

            // Calculate total bill
            totalBill += totalPrice;

            // Append item details to bill summary
            billSummary.append(String.format("%s x %d = %.2f INR\n", itemName, quantity, totalPrice));
        }

        // Append total bill amount to the bill summary
        billSummary.append(String.format("\nTotal Bill: %.2f INR", totalBill));

        // Display bill summary in the billTextArea
        billTextArea.setText(billSummary.toString());
    }

    private void storeTransactionInDatabase(String item, int quantity, double totalPrice) {
        try {
            Conn con = new Conn();

            // Fetch the maximum bill number from the database
            String maxBillNoQuery = "SELECT MAX(bill_no) FROM transactions";
            PreparedStatement maxBillNoStmt = con.c.prepareStatement(maxBillNoQuery);
            ResultSet maxBillNoRs = maxBillNoStmt.executeQuery();
            int maxBillNo = 0;
            if (maxBillNoRs.next()) {
                maxBillNo = maxBillNoRs.getInt(1);
            }

            // Increment the maximum bill number by 1
            int newBillNo = maxBillNo + 1;

            // Insert the new transaction with the incremented bill number
            String insertQuery = "INSERT INTO transactions (bill_no, item_name, quantity, total_price, transaction_date) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement insertStmt = con.c.prepareStatement(insertQuery);
            insertStmt.setInt(1, newBillNo);
            insertStmt.setString(2, item);
            insertStmt.setInt(3, quantity);
            insertStmt.setDouble(4, totalPrice);
            insertStmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearBill() {
        billTextArea.setText(""); // Clear the bill text area
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FoodBillingFrame());
    }
}
