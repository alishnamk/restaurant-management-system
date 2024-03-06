package restaurant.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame implements ActionListener {

    Dashboard() {
        setBounds(0, 0, 1500, 1000);
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 1000, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 1550, 1000);
        add(image);

        JLabel text = new JLabel("Welcome to Plant Day Restaurant");
        text.setBounds(400, 80, 1000, 50);
        text.setFont(new Font("tahoma", Font.BOLD, 40));
        text.setForeground(Color.white);
        image.add(text);

        JMenuBar mb = new JMenuBar();
        mb.setBounds(0, 0, 1550, 30);
        image.add(mb);

        JMenu restaurant = new JMenu("Restaurant Management");
        restaurant.setForeground(Color.BLACK);
        mb.add(restaurant);

        JMenuItem billing = new JMenuItem("Billing");
        billing.addActionListener(this);
        restaurant.add(billing);

        JMenuItem addReservation = new JMenuItem("Add Event Reservation");
        addReservation.addActionListener(this);
        restaurant.add(addReservation);

        JMenuItem addTable = new JMenuItem("Add Table Reservation");
        addTable.addActionListener(this);
        restaurant.add(addTable);

        JMenu admin = new JMenu("Admin");
        admin.setForeground(Color.BLACK);
        mb.add(admin);

        JMenuItem addEmployee = new JMenuItem("Add Employee");
        addEmployee.addActionListener(this);
        admin.add(addEmployee);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Billing":
                new Billing();
                break;
            case "Add Event Reservation":
                new addreservation();
                break;
            case "Add Table Reservation":
                new addtable();
                break;
            case "Add Employee":
                new addemp();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
