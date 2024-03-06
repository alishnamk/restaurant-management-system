package restaurant.management.system;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import restaurant.management.system.Conn;

public class addtable extends JFrame implements ActionListener {

    private JTextField customerNameField, aadharField, phoneField, timeeField;
    private JComboBox<String> tableSizeComboBox;
    private JDateChooser dateChooser;
    private JButton submitButton, cancelButton;

    public addtable() {
        setTitle("Table Reservation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(30, 20, 120, 25);
        panel.add(nameLabel);

        customerNameField = new JTextField(20);
        customerNameField.setBounds(180, 20, 200, 25);
        panel.add(customerNameField);

        JLabel aadharLabel = new JLabel("Aadhar Number:");
        aadharLabel.setBounds(30, 50, 120, 25);
        panel.add(aadharLabel);

        aadharField = new JTextField(12);
        aadharField.setBounds(180, 50, 200, 25);
        panel.add(aadharField);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(30, 80, 120, 25);
        panel.add(phoneLabel);

        phoneField = new JTextField(10);
        phoneField.setBounds(180, 80, 200, 25);
        panel.add(phoneField);

        JLabel dateLabel = new JLabel("Reservation Date:");
        dateLabel.setBounds(30, 110, 120, 25);
        panel.add(dateLabel);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(180, 110, 200, 25);
        panel.add(dateChooser);
        
        JLabel timeeLabel = new JLabel("Estimated time of arrival:");
        timeeLabel.setBounds(30, 170, 150, 25);
        panel.add(timeeLabel);

        timeeField = new JTextField(5);
        timeeField.setBounds(180, 170, 200, 25);
        panel.add(timeeField);

        JLabel tableSizeLabel = new JLabel("Table Size:");
        tableSizeLabel.setBounds(30, 140, 120, 25);
        panel.add(tableSizeLabel);

        String[] tableSizes = {"2", "3-4", "6-9", "10-15", "18"};
        tableSizeComboBox = new JComboBox<>(tableSizes);
        tableSizeComboBox.setBounds(180, 140, 200, 25);
        panel.add(tableSizeComboBox);


        submitButton = new JButton("Submit");
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(100, 210, 80, 25);
        submitButton.addActionListener(this);
        panel.add(submitButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(200, 210, 80, 25);
        cancelButton.addActionListener(this);
        panel.add(cancelButton);
    }

    public void actionPerformed(ActionEvent ex) {
    String customerName = customerNameField.getText();
    String aadharNumber = aadharField.getText();
    String phoneNumber = phoneField.getText();
    String tableSize = (String) tableSizeComboBox.getSelectedItem();
    String estimatedtime = timeeField.getText();
    Date selectedDate = dateChooser.getDate();
    
    if (ex.getSource() == submitButton) {
        try {
            Conn co = new Conn();
            java.sql.Date reservationDate = new java.sql.Date(selectedDate.getTime());
            String query = "INSERT INTO tablereservations (CustomerName, AadharNumber, PhoneNumber, ReservationDate, TableSize, EstimatedTime) " +
                           "VALUES ('" + customerName + "', '" + aadharNumber + "', '" + phoneNumber + "', '" + reservationDate + "', '" + tableSize + "', '" + estimatedtime + "')";

            co.s.executeUpdate(query);

            JOptionPane.showMessageDialog(this, "Table Reservation submitted successfully!");
            setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting reservation. Please try again.");
        }
    } else if (ex.getSource() == cancelButton) {
        setVisible(false);
    }
}



    private void clearFields() {
        customerNameField.setText("");
        aadharField.setText("");
        phoneField.setText("");
        dateChooser.setDate(null);
        timeeField.setText("");
        tableSizeComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new addtable();
    }
}
