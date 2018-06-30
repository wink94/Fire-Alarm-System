/**
 * 
 */
package com.alarm.monitor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;

/**
 * @author windula
 *
 */
public class Login {

	private JFrame frmLogin;
	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frmLogin;
	}

	private JTextField textFieldUsername;
	private JPasswordField passwordFieldPWD;
	
	private String pwd;
	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	private String username; 

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 450, 300);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setBounds(254, 32, 170, 39);
		frmLogin.getContentPane().add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel.setBounds(142, 35, 82, 27);
		frmLogin.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(142, 119, 82, 27);
		frmLogin.getContentPane().add(lblNewLabel_1);
		
		passwordFieldPWD = new JPasswordField();
		passwordFieldPWD.setBounds(254, 116, 170, 39);
		frmLogin.getContentPane().add(passwordFieldPWD);
		
		JButton btnLgin = new JButton("Login");
		btnLgin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				if(String.valueOf(passwordFieldPWD.getPassword()).isEmpty() || textFieldUsername.getText().isEmpty()) {
					 JOptionPane.showMessageDialog(null, "One or more filds empty", "ALERT", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					if(authenticateUser(textFieldUsername.getText(),String.valueOf(passwordFieldPWD.getPassword()))) {
						try {
							MonitorGUI window = new MonitorGUI();
							window.getFrmFirealarmMonitor().setVisible(true);
							frmLogin.dispose();
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					else {
						JOptionPane.showMessageDialog(
                                null,
                                "Failed to authenticate user! Please check username and password!",
                                "AUTHENTICATION FAILED",
                                JOptionPane.ERROR_MESSAGE);
					}
					
				}
				
			}
		});
		btnLgin.setBounds(223, 195, 89, 23);
		frmLogin.getContentPane().add(btnLgin);
	}
	
	
	
	public boolean authenticateUser(String username, String pwd)  {
		

		try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\com\\alarm\\monitor\\monitors.txt"))) {
    	    String line;
    	    String [] fields;
    	    //boolean valid;
    	    
    	    while ((line = br.readLine()) != null) {
    	       // process the line.
    	    	fields=line.split("-");
    	    	
    	    	if(fields[0].equals(username) && fields[1].equals(pwd)) {
    	    		//System.out.println("valid");
    	    		return true;
    	    	}
    	    	
    	    }
    	    
    	   
    	    
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	
	
	
}
