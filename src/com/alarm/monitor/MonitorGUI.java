/**
 * 
 */
package com.alarm.monitor;

import java.awt.EventQueue;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.JTextPane;

import com.alarm.server.RMIServer;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;

/**
 * @author windula
 *
 */
public class MonitorGUI   {

	private JFrame frmFirealarmMonitor;
	/**
	 * @return the frmFirealarmMonitor
	 */
	public JFrame getFrmFirealarmMonitor() {
		return frmFirealarmMonitor;
	}




	private JTextArea alertBox;
	private JTextArea co2level;
	private JTextArea temperature;
	private JButton btnClose;
	private JComboBox comboBoxMonitors;
	private JTextArea textAreaCurrentReading;
	private JLabel lblSensors;
	private JLabel lblMonitors;
	
	private MonitorHandler monitor;
	private JLabel lblTemperature;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_3;
	private JTextArea SmokeLevel;
	private JLabel lblSmokeLevel;
	private JTextArea BatteryLevel;
	private JLabel lblNewLabel_4;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonitorGUI window = new MonitorGUI();
					window.frmFirealarmMonitor.setVisible(true);
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MonitorGUI() throws java.rmi.RemoteException {
		initialize();
		
		
		monitor=new MonitorHandler();
		Thread gui=new Thread(monitor);
		gui.start();
		
		
		
		
	}
	
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFirealarmMonitor = new JFrame();
		frmFirealarmMonitor.setTitle("Fire-Alarm Monitor");
		frmFirealarmMonitor.getContentPane().setForeground(Color.WHITE);
		frmFirealarmMonitor.setBounds(100, 100, 819, 504);
		frmFirealarmMonitor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFirealarmMonitor.getContentPane().setLayout(null);
		
		lblSensors = new JLabel("Sensors");
		lblSensors.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		lblSensors.setBounds(334, 20, 73, 25);
		frmFirealarmMonitor.getContentPane().add(lblSensors);
		
		lblMonitors = new JLabel("Monitors");
		lblMonitors.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		lblMonitors.setBounds(511, 20, 85, 30);
		frmFirealarmMonitor.getContentPane().add(lblMonitors);
		
		co2level = new JTextArea();
		co2level.setEditable(false);
		co2level.setVisible(true);
		//co2level.setBounds(308, 62, 189, 84);
		JScrollPane scroll_co2 = new JScrollPane (co2level, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_co2.setBounds(374, 86, 198, 116);
		//scroll_co2.setViewportView(co2level);
		frmFirealarmMonitor.getContentPane().add(scroll_co2);
		
		
		
		
		comboBoxMonitors = new JComboBox();
		comboBoxMonitors.setFont(new Font("Yu Gothic UI", Font.PLAIN, 11));
		comboBoxMonitors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					/*
					 *get reading for particular monitor
					 * */
					
					String val=String.valueOf(comboBoxMonitors.getSelectedItem());
					monitor.getSensorCurrentReading(val, monitor);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		comboBoxMonitors.setBounds(159, 257, 132, 30);
		frmFirealarmMonitor.getContentPane().add(comboBoxMonitors);
		
		textAreaCurrentReading = new JTextArea();
		//textAreaCurrentReading.setBounds(40, 258, 120, 58);
		JScrollPane textArea_current = new JScrollPane (textAreaCurrentReading, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea_current.setBounds(93, 311, 198, 126);
		frmFirealarmMonitor.getContentPane().add(textArea_current);
		
		JLabel lblNewLabel_2 = new JLabel("CO2 Level");
		lblNewLabel_2.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(374, 215, 73, 14);
		frmFirealarmMonitor.getContentPane().add(lblNewLabel_2);
		
		 alertBox = new JTextArea();
		 alertBox.setForeground(Color.RED);
		 alertBox.setLocation(558, 208);
		//alertBox.setBounds(40, 98, 120, 73);
		frmFirealarmMonitor.getContentPane().add(alertBox);
		alertBox.setEditable(false);
		JScrollPane scroll_alert = new JScrollPane (alertBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_alert.setBounds(93, 120, 194, 97);
		frmFirealarmMonitor.getContentPane().add(scroll_alert);
		
		btnClose = new JButton("Close using this");
		btnClose.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnClose.setBackground(new Color(240, 240, 240));
		btnClose.setForeground(Color.RED);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				monitor.exit();
				
				frmFirealarmMonitor.dispose();
				
			}
		});
		btnClose.setBounds(624, 424, 155, 30);
		frmFirealarmMonitor.getContentPane().add(btnClose);
		
		JLabel lblMonitors_1 = new JLabel("Monitors#");
		lblMonitors_1.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		lblMonitors_1.setBounds(417, 20, 84, 30);
		frmFirealarmMonitor.getContentPane().add(lblMonitors_1);
		
		lblTemperature = new JLabel("Temperature");
		lblTemperature.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblTemperature.setBounds(374, 397, 85, 14);
		frmFirealarmMonitor.getContentPane().add(lblTemperature);
		
		temperature = new JTextArea();
		
		JScrollPane scroll_temperature = new JScrollPane (temperature, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_temperature.setBounds(371, 259, 201, 116);
		frmFirealarmMonitor.getContentPane().add(scroll_temperature);
		
		lblNewLabel = new JLabel("Sensors#");
		lblNewLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		lblNewLabel.setBounds(245, 20, 73, 24);
		frmFirealarmMonitor.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Previous Readings\t\r\n");
		lblNewLabel_1.setFont(new Font("Yu Gothic UI", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(10, 254, 139, 30);
		frmFirealarmMonitor.getContentPane().add(lblNewLabel_1);
		
		lblNewLabel_3 = new JLabel("Alerts");
		lblNewLabel_3.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
		lblNewLabel_3.setForeground(Color.RED);
		lblNewLabel_3.setBounds(93, 79, 73, 30);
		frmFirealarmMonitor.getContentPane().add(lblNewLabel_3);
		
		SmokeLevel = new JTextArea();
		JScrollPane scroll_SmokeLevel = new JScrollPane (SmokeLevel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_SmokeLevel.setBounds(624, 87, 169, 115);
		frmFirealarmMonitor.getContentPane().add(scroll_SmokeLevel);
		
		lblSmokeLevel = new JLabel("Smoke Level");
		lblSmokeLevel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblSmokeLevel.setBounds(624, 215, 73, 14);
		frmFirealarmMonitor.getContentPane().add(lblSmokeLevel);
		
		BatteryLevel = new JTextArea();
		JScrollPane scroll_BatteryLevel = new JScrollPane (BatteryLevel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_BatteryLevel.setBounds(624, 260, 169, 115);
		frmFirealarmMonitor.getContentPane().add(scroll_BatteryLevel);
		
		lblNewLabel_4 = new JLabel("Battery Level");
		lblNewLabel_4.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblNewLabel_4.setBounds(624, 397, 73, 14);
		frmFirealarmMonitor.getContentPane().add(lblNewLabel_4);
		//JScrollPane scroll_alert = new JScrollPane (alertBox, 
		//		   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	}

	
	
	
	private class MonitorHandler extends UnicastRemoteObject implements Monitor,Runnable,Serializable{

		
		private RMIServer stub;
		//private MonitorHandler client;
		/**
		 * 
		 */
		public MonitorHandler() throws RemoteException {
			// TODO Auto-generated constructor stub
		}
		
		
		/* realtime alert generation
		 * @see com.alarm.monitor.Monitor#alertGenerator(java.lang.String, java.lang.String)
		 */
		@Override
		public void alertGenerator(String type, String value) throws RemoteException {
			// TODO Auto-generated method stub
			
			if(type.equals("TEMP")) {
				alertBox.append("Alert "+"Temperature:"+value+"\n");
			}
			
			if(type.equals("CO2")) {
				alertBox.append("Alert "+"CO2 Level:"+value+"\n");
			}
			
			if(type.equals("SENSOR")) {
				alertBox.append("Alert "+"Sensor :"+value+" down \n");
			}
			
			if(type.equals("SMOKE")) {
				alertBox.append("Alert "+"Smoke Level :"+value+"\n");
			}
			
			if(type.equals("BATTERY")) {
				alertBox.append("Alert "+"Batttery Level :"+value+"\n");
			}
			
			
		}
		
		
		

		/* run the GUI
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			//  Auto-generated method stub
			try {
				Registry registry = LocateRegistry.getRegistry(null);
				
				stub=(RMIServer)registry.lookup("RMIServer");
				
				//client=new MonitorHandler();
				
				stub.addMonitor(monitor);
			} catch (NotBoundException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		//rmeove monitor from list
		public void exit() {
			try {
				stub.removeMonitor(monitor);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//get sensor readings
		public void getSensorCurrentReading(String sensor,Monitor monitor) throws RemoteException {
			
			try {
				stub.getReadings(sensor, monitor);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
		
		

		/* RMI method to get co2 readings
		 * @see com.alarm.monitor.Monitor#getCO2Reading(java.lang.String)
		 */
		@Override
		public void getCO2Reading(String value) throws RemoteException {
			// TODO Auto-generated method stub
			co2level.append(value+"\n");
			//System.out.println(value);
		}


		
		/* RMI method to update sensor list in combobox realtime
		 * @see com.alarm.monitor.Monitor#updateList(java.lang.String[])
		 */
		@Override
		public void updateList(String[] sensors) throws RemoteException {
			//  Auto-generated method stub
			//DefaultComboBoxModel model = (DefaultComboBoxModel)comboBoxMonitors.getModel() ;
			
			DefaultComboBoxModel model = new DefaultComboBoxModel<String>(sensors);
			comboBoxMonitors.setModel(model);
			
		}


		/* RMI method to get reading from sensor on a floor by ID
		 * @see com.alarm.monitor.Monitor#getReadingByID(java.lang.String)
		 */
		@Override
		public void getReadingByID(String values) throws RemoteException {
			// TODO Auto-generated method stub
			String[] readings=values.split("\\|");
			//System.out.println(values+" "+readings[3]);
			textAreaCurrentReading.setText(null);
			
			for (String string : readings) {
				String[] arr=string.split("-");
				textAreaCurrentReading.append(arr[0]+"  "+arr[1]+"\n");
			}
		}


		/* RMI method to set sensor count on GUI
		 * @see com.alarm.monitor.Monitor#setSensorCount(java.lang.String)
		 */
		@Override
		public void setSensorCount(String value) throws RemoteException {
			// TODO Auto-generated method stub
			lblSensors.setText(value);
		}


		/* RMI method to set sensor monitor count on GUI
		 * @see com.alarm.monitor.Monitor#setMonitorCount(java.lang.String)
		 */
		@Override
		public void setMonitorCount(String value) throws RemoteException {
			// TODO Auto-generated method stub
			lblMonitors.setText(value);
		}


		/* RMI method to temperature reading
		 * @see com.alarm.monitor.Monitor#getTemperatureReading(java.lang.String)
		 */
		@Override
		public void getTemperatureReading(String value) throws RemoteException {
			// TODO Auto-generated method stub
			temperature.append(value+"\n");
		}


		/* RMI method to smoke reading
		 * @see com.alarm.monitor.Monitor#getSmokeReading(java.lang.String)
		 */
		@Override
		public void getSmokeReading(String value) throws RemoteException {
			SmokeLevel.append(value+"\n");
			
		}


		/* RMI method to battery level reading
		 * @see com.alarm.monitor.Monitor#getBatteryReading(java.lang.String)
		 */
		@Override
		public void getBatteryReading(String value) throws RemoteException {
			BatteryLevel.append(value+"\n");
			
		}


		
		
	}
	
	
	
	
	
	
}
