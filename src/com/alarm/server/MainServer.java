/**
 * 
 */
package com.alarm.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;

import com.alarm.monitor.Monitor;



/**
 * @author windula
 *
 */

public class MainServer  implements RMIServer{

	/**
	 * @param args
	 */
	
	/**
     * The port that the server listens on.
     */
    private static final int PORT1 = 9001;
	
    /**
     * The set of all names of sensors in the chat room.  Maintained
     * so that we can check that new sensors are not registering name
     * already in use.
     */
    private static HashSet<String> sensors = new HashSet<String>();
    
    
    private static ArrayList<Monitor> list=new ArrayList<Monitor>();
    
    
    
    /**
	 * 
	 */
	public MainServer() throws RemoteException {
		// TODO Auto-generated constructor stub
	}
    
    
    
    
   
    /**
     * @author windula
     *
     */
    private static class Handler extends Thread {
        private String ID;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        
        
        
        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }
        
        public boolean sensorAuthenticate(String value) {
        	try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\com\\alarm\\server\\sensors.txt"))) {
        	    String line;
        	    
        	    while ((line = br.readLine()) != null) {
        	       // process the line.
        	    	/*lineArgs=line.split("-");
        	    	if(lineArgs[0].equals(type) && lineArgs[1].equals(value)) {
        	    		return true;
        	    	}*/
        	    	
        	    	if(line.equals(value)) {
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
        
       
        /**
         * write sensor data to files
         * @param input
         * @param type
         * @return void
         */
        public void WriteToFile(String input,String type) {
        	
        	try {
			    BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\src\\com\\alarm\\server\\"+ID+".txt", true));
			    
			    writer.write(input);
			    writer.close();
			} catch (IOException e) {
				 e.printStackTrace();
			}
        	
        	
        }
        
        /**
         * generate alerts
         * @param valueSet
         * @param type
         * @return void
         */
        public void alertFinder(String valueSet,String type) {
        	
        	String [] readings=valueSet.split("\\|");
        	
        	String[] sensorDeatils=ID.split("-");
        	
        	if(!list.isEmpty()) {
        		
	        	for (Monitor monitor : list) {
					
	        		for (int i = 0; i < readings.length; i++) {
	        			
	        			if(type.equals("CO2")) {
	        				
	        				try {
	        					String[] co2lvl=readings[i].split("-");
	        					
	        					if(Double.parseDouble(co2lvl[1])>300) {
	        						monitor.alertGenerator("CO2",co2lvl[1]+" Floor:"+sensorDeatils[0]+" sensor:"+sensorDeatils[1]);
	        					}
	        					
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
	        			

	        			if(type.equals("TEMP")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					
	        					if(Double.parseDouble(val[1])>50) {
	        						monitor.alertGenerator("TEMP",val[1]+"C Floor:"+sensorDeatils[0]+" sensor:"+sensorDeatils[1]);
	        					}
	        					
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
	        			if(type.equals("SMOKE")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					
	        					if(Double.parseDouble(val[1])>7) {
	        						monitor.alertGenerator("SMOKE",val[1]+" Floor:"+sensorDeatils[0]+" sensor:"+sensorDeatils[1]);
	        					}
	        					
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
	        			if(type.equals("BATTERY")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					
	        					if(Double.parseDouble(val[1])<=10) {
	        						monitor.alertGenerator("BATTERY",val[1]+"% Floor:"+sensorDeatils[0]+" sensor:"+sensorDeatils[1]);
	        					}
	        					
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
						
					}
				}
        	
        	}
        	
        	
        	
        	
        }
        
        /**
         * RMI method to send readings
         * @param valueSet
         * @param type
         * @throws RemoteException
         * @return void
         */
        public void sendValue(String valueSet,String type) throws RemoteException {
        	
        	String [] readings=valueSet.split("\\|");
        	
        	if(!list.isEmpty()) {
        		
	        	for (Monitor monitor : list) {
					
	        		for (int i = 0; i < readings.length; i++) {
	        			
	        			if(type.equals("CO2")) {
	        				
	        				try {
	        					String[] co2lvl=readings[i].split("-");
	        					monitor.getCO2Reading(co2lvl[0]+"  "+co2lvl[1]);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
	        			

	        			if(type.equals("TEMP")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					monitor.getTemperatureReading(val[0]+"  "+val[1]+"C");
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
	        			if(type.equals("SMOKE")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					monitor.getSmokeReading(val[0]+"  "+val[1]);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
	        			if(type.equals("BATTERY")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					monitor.getBatteryReading(val[0]+"  "+val[1]+"%");
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
						
					}
				}
        	
        	}
        	
        	
        }
        
        
        
        /**
         * base64 decode
         * @param input
         * @return
         * @return String
         */
        public String getBase64Decoded(String input) {
        	
    
				byte[] decodedBytes = Base64.getDecoder().decode(input);
	        	String decodedString;
				decodedString = new String(decodedBytes);
				
	
				return decodedString;
			
        	
        }
        
        
        
        
        public void run() {
        	
        	
        	
        	// Create character streams for the socket.
            try {
				in = new BufferedReader(new InputStreamReader(
				    socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				
				
				 // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.println("SUBMITNAME");
                    ID = getBase64Decoded(in.readLine());
                    if (ID == null) {
                        return;
                    }
                    synchronized (sensors) {
                        if (!sensors.contains(ID)) {
                        	//sensors.add(ID);
                            //break;
                        	
                        	out.println("TYPE-PWD");
                        	String pwd=getBase64Decoded(in.readLine());
                        	if(sensorAuthenticate(pwd)) {
                        		sensors.add(ID);
                                break;
                        	}
                        	else {
                        		out.println("UNAUTHORISED");
                        		continue;
                        	}
                        	
                        	
                        	
                        	
                        	
                        }
                    }
                }
				
				
                out.println("AUTHORISED");
                
                //update sensor count on monitors
                
                sensorUpdate();
        		
                if(!list.isEmpty()) {
                	
                	for (Monitor monitor : list) {
            			
            			
            			monitor.setSensorCount(Integer.toString(sensors.size()));
            			
            		}
                	
                }
                
                while (true) {
                	
                	String line;
                	String[] input;
					
                	
					line = getBase64Decoded(in.readLine());
					
					
					
					if(line.isEmpty() || line.equals("")) {
						
						if(!list.isEmpty()) {
				        	
				        	for (Monitor monitor : list) {
				    			
				    			//generate alert when sensor goes down without 1 hour update
				    			monitor.alertGenerator("SENSOR", ID);
				    			
				    		}
				        	
				        }
						
					}
					/*
					 * check reading type
					 * search for alerts
					 * send values
					 * write to file
					 * */
					
					if (line.startsWith("CO2")) {
						input=line.split(":");
						alertFinder(input[1], "CO2");
						sendValue(input[1], "CO2");
						System.out.println(input[1]);
						WriteToFile(input[1],"CO2");
					}
					
					if (line.startsWith("TEMP")) {
						input=line.split(":");
						alertFinder(input[1], "TEMP");
						sendValue(input[1], "TEMP");
						System.out.println(input[1]);
						WriteToFile(input[1],"TEMP");
					}
					
					
					if (line.startsWith("SMOKE")) {
						input=line.split(":");
						alertFinder(input[1], "SMOKE");
						sendValue(input[1], "SMOKE");
						System.out.println(input[1]);
						WriteToFile(input[1],"SMOKE");
					}
					
					if (line.startsWith("BATTERY")) {
						input=line.split(":");
						alertFinder(input[1], "BATTERY");
						sendValue(input[1], "BATTERY");
						System.out.println(input[1]);
						WriteToFile(input[1],"BATTERY");
					}
                	
                }
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            finally {
                // This sensor is going down!  Remove its name and its print
                
            	
            	if(ID!=null) {
            		
            		try {
            			sensors.remove(ID);
						sensorUpdate();
						
						if(!list.isEmpty()) {
				        	
				        	for (Monitor monitor : list) {
				    			
				    			
				    			monitor.setSensorCount(Integer.toString(sensors.size()));
				    			//generate alert when sensor goes down without 1 hour update
				    			monitor.alertGenerator("SENSOR", ID);
				    			
				    		}
				        	
				        }
						
						
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	
            	try {
                    socket.close();
                } catch (IOException e) {
                }
            }
            
        	
        	
        }
        
        
    }
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		try {
			//Making remote object accessible to client by binding it to remote registry
			MainServer server=new MainServer();
			
			RMIServer stub= (RMIServer) UnicastRemoteObject.exportObject(server, 0);
			
			Registry registry = LocateRegistry.createRegistry(1099); 
			registry.rebind("RMIServer", stub);
			
			System.out.println("Server RMI started");
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Server exception: " + e.toString()); 
	        e.printStackTrace(); 
		}
		
		//server socket is listening on PORT1 for client’s requests
		ServerSocket listener = new ServerSocket(PORT1);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
		
		

	}


	/* RMI for add monitor
	 * @see com.alarm.server.RMIServer#addMonitor(com.alarm.monitor.Monitor)
	 */
	@Override
	public synchronized void addMonitor(Monitor client) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("adding monitor -" + client);
		
		
		
		list.add(client);
		
		sensorUpdate();
		
		if(!list.isEmpty()) {
        	
        	for (Monitor monitor : list) {
    			
    			monitor.setMonitorCount(Integer.toString(list.size()));
    			monitor.setSensorCount(Integer.toString(sensors.size()));
    			
    		}
        	
        }
		
	}


	/* RMI method remove monitor
	 * @see com.alarm.server.RMIServer#removeMonitor(com.alarm.monitor.Monitor)
	 */
	@Override
	public synchronized void removeMonitor(Monitor client) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("removing listener -" + client);
		list.remove(client);
		
		sensorUpdate();
		
		if(!list.isEmpty()) {
        	
        	for (Monitor monitor : list) {
    			
    			monitor.setMonitorCount(Integer.toString(list.size()));
    			
    		}
        	
        }
		
	}
	
	
	
	/**
	 * RMI for update sensor count  on monitor
	 * @throws RemoteException
	 * @return void
	 */
	public synchronized  static void sensorUpdate() throws RemoteException{
		
		if(!list.isEmpty()) {
        	
        	for (Monitor monitor : list) {
    			
    			monitor.updateList(sensors.toArray(new String[sensors.size()]));
    			
    		}
        	
        }
		
	}


	


	/* RMI method for get reading
	 * @see com.alarm.server.RMIServer#getReadings(java.lang.String)
	 */
	@Override
	public synchronized void getReadings(String sensor,Monitor client) throws RemoteException {
		
		try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\com\\alarm\\server\\"+sensor+".txt"))) {
    	    String line;
    	    String lineArgs="";
    	    while ((line = br.readLine()) != null) {
    	       // process the line.
    	    	
    	    	lineArgs+=line;
    	    	
    	    	
    	    }
    	    
    	    
    	    System.out.println(client+" "+lineArgs+" "+sensor);
	    	client.getReadingByID(lineArgs);
    	    
    	    
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	


	

	

}
