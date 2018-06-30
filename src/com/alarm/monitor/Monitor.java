/**
 * 
 */
package com.alarm.monitor;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author windula
 *
 */
public interface Monitor extends Remote,Serializable {
	
	public void alertGenerator(String type,String value) throws java.rmi.RemoteException; //callback function for getting alerts
	
	public void getCO2Reading(String value)throws java.rmi.RemoteException;//co2 sensor values
	
	public void getTemperatureReading(String value)throws java.rmi.RemoteException;//temperature sensor values
	
	public void getSmokeReading(String value)throws java.rmi.RemoteException; //smoke sensor values
	
	public void getBatteryReading(String value)throws java.rmi.RemoteException; //battery sensor values
	
	public void getReadingByID(String arr)throws RemoteException; //get readings from sensor
	
	public void setSensorCount(String value)throws RemoteException; //get active sensor count
	
	public void setMonitorCount(String value)throws RemoteException; //active monitor count
	
	public void updateList(String[] sensors) throws RemoteException; //update sensor list
}
