package restaurant.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Billing extends JFrame implements ActionListener {

    public Billing() {
        setTitle("Plant Day Cafe");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icons/five.png"));
        Image image = icon.getImage().getScaledInstance(800, 600, Image.SCALE_DEFAULT);
        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(0, 0, 800, 600);
        add(background);

        JLabel headerLabel = new JLabel("Plant Day Cafe", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Verdana", Font.BOLD, 40));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setBounds(150, 100, 500, 50);
        background.add(headerLabel);

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
        mainPanel.setBounds(200, 150, 400, 300);
        background.add(mainPanel);

        String[] menuItems = {"Event Reservation", "Table Reservation", "Food Billing"};
        for (String menuItem : menuItems) {
            JMenuItem item = new JMenuItem(menuItem);
            item.addActionListener(this);
            item.setFont(new Font("Caliber", Font.BOLD , 25)); // Set font size for JMenuItem
            mainPanel.add(item);
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    switch (command) {
        case "Event Reservation":
            new AddEventReservationFrame(); // Open Add Event Reservation frame
            break;
        case "Table Reservation":
            new AddTableReservationFrame(); // Open Add Table Reservation frame
            break;
        case "Food Billing":
            new FoodBillingFrame(); // Open Food Billing frame
            break;
        default:
            break;
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Billing::new);
    }
}
