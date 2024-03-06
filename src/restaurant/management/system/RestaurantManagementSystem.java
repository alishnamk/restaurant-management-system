package restaurant.management.system;

import java.awt.Color;
import javax.swing.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;


public class RestaurantManagementSystem extends JFrame implements ActionListener {
    RestaurantManagementSystem (){
        //setSize( 1366 , 769);
        //setLocation( 80 ,100);
        setBounds(0,0,1366,769);
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/first.png"));
        Image i2 = i1.getImage().getScaledInstance(1366, 769, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(i1);
        image.setBounds(0,0,1366,769);
        add(image);
        
        JLabel text = new JLabel("Restaurant Management System");
        text.setBounds(50,150,1000,90);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("serif", Font.BOLD ,40));
        image.add(text);
        
        
       JButton next = new JButton("next");
       next.setBounds(1150,450,150,50);
       next.setBackground(Color.WHITE);
       next.setForeground(Color.darkGray);
       next.addActionListener(this);
       next.setFont(new Font("serif", Font.BOLD ,40));
       image.add(next);
        
        
        setVisible(true);
        
        while(true)
        {
            text.setVisible(false);
            try{
                Thread.sleep(500);
            } catch (Exception e){
                e.printStackTrace();
            }
            text.setVisible(true);
            try{
                Thread.sleep(500);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        
    }
    public void actionPerformed(ActionEvent e){
        setVisible(false);
        new Login();
    }

    public static void main(String[] args) {
        RestaurantManagementSystem window = new RestaurantManagementSystem();
        
    }
    
}
