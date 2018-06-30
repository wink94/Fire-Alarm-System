/**
 * 
 */
package com.alarm.sensors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author windula
 *
 */
public class Temperature implements SensorInterface {
	
	
	private double temp;
	private String id;
	private Random r;
	private BufferedReader in;
    private PrintWriter out;
   
	
	
	/**
	 * 
	 */
	public Temperature(String ID) {
		temp = 33.0;
	    r = new Random();
	    id=ID;
	}
	/**
	 * get inputs from keyboard
	 * @param value
	 * @return
	 * @return String
	 */
	
	public String getInput(String value) {
		Scanner keyboard = new Scanner(System.in);
		
		if(value.equals("pwd")) {
			System.out.print("enter sensor pwd+type ('type'-'pwd'):");
		}
		
		else if(value.equals("id")) {
			System.out.print("enter sensor ID ("+id+"):");
		}
		
		String input = keyboard.nextLine();
		
		return input;
    }
	
	/**
	 * get host
	 * @return String
	 */
	
	private String getServerAddress() {
		// TODO Auto-generated method stub
		Scanner keyboard = new Scanner(System.in);
		System.out.println("enter server");
		String server=keyboard.nextLine();
		return server;
	}
	/**
	 * @param input
	 * base64 encoding
	 * @param String
	 */
	
	private String getBase64Encoding(String input) {
		String encodedString = Base64.getEncoder().encodeToString(input.getBytes());
		
		return encodedString;
	}

	/* (non-Javadoc)
	 * @see com.alarm.sensors.SensorInterface#getReading()
	 */
	@Override
	public double getReading() {

        if (temp > 50.0){
            temp = 33.0;
        }else{
            //random number to change temperature
            int num = r.nextInt();
            if (num < 0) {
                    temp += 0.5;
            } else {
                    temp -= 0.5;
            }
        }
        return temp;
	}

	/* (non-Javadoc)
	 * @see com.alarm.sensors.SensorInterface#run()
	 */
	@Override
	public void run() {
		
		String serverAddress = getServerAddress();
        Socket socket;
		
			try {
				socket = new Socket(serverAddress, 9001);
				in = new BufferedReader(new InputStreamReader(
			            socket.getInputStream()));
			    out = new PrintWriter(socket.getOutputStream(), true);
			
			
		
		 // Process all messages from server, according to the protocol.
		        while (true) {
		        	String line;
					
						line = in.readLine();
						
						
						
						if (line.startsWith("SUBMITNAME")) {
			                out.println(getBase64Encoding(getInput("id")));
			            }
						
						else if (line.startsWith("TYPE-PWD")) {
							out.println(getBase64Encoding(getInput("pwd")));
						}
						else if (line.startsWith("AUTHORISED")) {
							System.out.println("access granted");
							break;
						}
						else if (line.startsWith("UNAUTHORISED")) {
							System.out.println("access deined please continue from beginning");
							continue;
						}
						
	        	
		        }
		        
		        Timer timer = new Timer();
		      //writing data to a file hourly
		        
		        TimerTask writeToFile = new TimerTask() {

					@Override
					public void run() {
					
						
						try {
						    BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\src\\com\\alarm\\sensors\\"+id+".txt", true));
						    String timeStamp = new SimpleDateFormat("yyyy MM dd~HH mm ss").format(Calendar.getInstance().getTime());
						    writer.write(timeStamp+"-"+Double.toString(getReading())+"|");
						    writer.close();
						} catch (IOException e) {
							 e.printStackTrace();
						}
						
						
					}
		        	
		        };
		        
		      //sending data by reading a file
		        TimerTask sendData = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\com\\alarm\\sensors\\"+id+".txt"))) {
				            String line;
				            
				            while ((line = br.readLine()) != null) {
				            	out.println(getBase64Encoding("TEMP:"+line));
				            }
				        } catch (IOException e) {
				            e.printStackTrace();
				        }finally {
							
				        	File file = new File(System.getProperty("user.dir")+"\\src\\com\\alarm\\sensors\\"+id+".txt");
				            
				            if(file.delete())
				            {
				                System.out.println("File deleted successfully");
				            }
				            else
				            {
				                System.out.println("Failed to delete the file");
				            }
				        	
						}
						
					}
		        	
		        };
		      //time is scheduled to write to file
		        timer.schedule(writeToFile, 0,2000);
		        ////time is scheduled to read from file
		        timer.schedule(sendData, 11000,11000);
		        
        
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}
	
	public static void main(String arg[]) {

		 Temperature sensor=new Temperature("12-35");
		 sensor.run();
	        

	    }
	

}
