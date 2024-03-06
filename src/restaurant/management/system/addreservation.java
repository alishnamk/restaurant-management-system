package restaurant.management.system;

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
import java.awt.Color;
import restaurant.management.system.Conn;

public class addreservation extends JFrame implements ActionListener{

    public JTextField customerNameField, aadharField, phoneField, peopleField;
    public JComboBox<String> eventTypeComboBox;
    JButton submit,cancelButton;
    public JDateChooser dateChooser;

    public addreservation() {
        setTitle("Table Reservation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
        setTitle("Event Reservation");
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel nameLabel = new JLabel("Cust Name:");
        nameLabel.setBounds(30, 20, 80, 25);
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

        JLabel eventTypeLabel = new JLabel("Event Type:");
        eventTypeLabel.setBounds(30, 140, 120, 25);
        panel.add(eventTypeLabel);

        String[] eventTypes = {"Birthday", "Anniversary", "Business Meeting", "Other"};
        eventTypeComboBox = new JComboBox<>(eventTypes);
        eventTypeComboBox.setBounds(180, 140, 200, 25);
        panel.add(eventTypeComboBox);

        JLabel peopleLabel = new JLabel("Estimated No. of People:");
        peopleLabel.setBounds(30, 170, 150, 25);
        panel.add(peopleLabel);

        peopleField = new JTextField(5);
        peopleField.setBounds(180, 170, 200, 25);
        panel.add(peopleField);

        submit = new JButton("Submit");
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setBounds(100, 210, 80, 25);
        submit.addActionListener(this);
        panel.add(submit);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(200, 210, 80, 25);
        cancelButton.addActionListener(this);
        panel.add(cancelButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            setVisible(false); // Hide the reservation window
        } else if (e.getSource() == submit) {
            // Handle submit button click
            String customer_name = customerNameField.getText();
            String aadhar_number = aadharField.getText();
            String phone_number = phoneField.getText();
            String estimated_people = peopleField.getText();
            String event_type = (String) eventTypeComboBox.getSelectedItem();

            try {
                Conn con = new Conn();
                Date selectedDate = dateChooser.getDate();
                java.sql.Date reservation_date = new java.sql.Date(selectedDate.getTime());

                String query = "INSERT INTO reservations (customer_name, aadhar_number, phone_number, reservation_date, event_type, estimated_people) VALUES ('" +
                        customer_name + "', '" + aadhar_number + "', '" + phone_number + "', '" + reservation_date + "', '" + event_type + "', '" + estimated_people + "')";

                con.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Event Reservation submitted successfully!");
                setVisible(false);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error submitting reservation. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        new addreservation();
    }
}
