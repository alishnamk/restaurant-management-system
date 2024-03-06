package restaurant.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;

public class addemp extends JFrame implements ActionListener{
    
    JTextField tfname, tfphone, tfage, tfemail, tfsalary;
    JRadioButton rbmale, rbfmale;
    JButton submit;
    JComboBox<String> cbjob;
    
    addemp(){
        setLayout(null);
        setResizable(false);
        JLabel lblname = new JLabel("NAME");
        lblname.setBounds(60, 30, 120, 30);
        lblname.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(lblname);
        
        tfname = new JTextField();
        tfname.setBounds(200, 30, 150, 30);
        add(tfname);
        
        JLabel lblage = new JLabel("AGE");
        lblage.setBounds(60, 80, 120, 30);
        lblage.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(lblage);
        
        tfage = new JTextField();
        tfage.setBounds(200, 80, 150, 30);
        add(tfage);
        
        JLabel lblgen = new JLabel("GENDER");
        lblgen.setBounds(60, 130, 120, 30);
        lblgen.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(lblgen);
        
        rbmale = new JRadioButton("MALE");
        rbmale.setBounds(200, 130, 70, 30);
        rbmale.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(rbmale);
        
        rbfmale = new JRadioButton("FEMALE");
        rbfmale.setBounds(280, 130, 70, 30);
        rbfmale.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(rbfmale);
        
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(rbmale);
        genderGroup.add(rbfmale);
        
        JLabel lbljob = new JLabel("JOB");
        lbljob.setBounds(60, 180, 120, 30);
        lbljob.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(lbljob);
       
        String[] str = {"Manager", "Accountant", "Front Desk Clerk", "Waiter/Waitress", "Chef", "Kitchen Staff", "Housekeeping"};
        cbjob = new JComboBox<>(str);
        cbjob.setBounds(200, 180, 150, 30);
        cbjob.setBackground(Color.WHITE);
        add(cbjob);
        
        JLabel lblphone = new JLabel("PHONE NO");
        lblphone.setBounds(60, 230, 120, 30);
        lblphone.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(lblphone);
        
        tfphone = new JTextField();
        tfphone.setBounds(200, 230, 150, 30);
        add(tfphone);
        
        JLabel lblemail = new JLabel("Email Id");
        lblemail.setBounds(60, 280, 120, 30);
        lblemail.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(lblemail);
        
        tfemail = new JTextField();
        tfemail.setBounds(200, 280, 150, 30);
        add(tfemail);
        
        JLabel lblsalary = new JLabel("SALARY");
        lblsalary.setBounds(60, 330, 120, 30);
        lblsalary.setFont(new Font("Tahoma", Font.PLAIN, 17));
        add(lblsalary);
        
        tfsalary = new JTextField();
        tfsalary.setBounds(200, 330, 150, 30);
        add(tfsalary);
        
        submit = new JButton("SUBMIT");
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setBounds(200, 430, 150, 30);
        submit.addActionListener(this);
        add(submit);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/four.png"));
        Image i2 = i1.getImage().getScaledInstance(450, 450, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(380, 60, 450, 450);
        add(image);
        
        getContentPane().setBackground(Color.WHITE);
        setBounds(350, 230, 850, 540);
        setTitle("Add Employee");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        
        getContentPane().setBackground(Color.WHITE);
    setSize(850, 540);
    setTitle("Add Employee");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null); // Center the frame on the screen
    setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
    String name = tfname.getText();
    String age = tfage.getText();
    String phone = tfphone.getText();
    String salary = tfsalary.getText();
    String email = tfemail.getText();
    
    String gender = null;
    
    if(rbmale.isSelected()){
        gender ="Male";
    } else if (rbfmale.isSelected()) {
        gender="Female";
    }
    
    String job = (String) cbjob.getSelectedItem();
    
    try{
        Conn conn = new Conn();

        String query="insert into employee values('"+name+"','"+age+"','"+gender+"','"+job+"','"+phone+"','"+email+"','"+salary+"')";
        conn.s.executeUpdate(query);

        JOptionPane.showMessageDialog(null,"Employee added successfully");
        setVisible(false);
    } catch(Exception ex){
        ex.printStackTrace();
    }
}

    public static void main(String[] args) {
        new addemp();
    }
}
