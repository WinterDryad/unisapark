package localServer ;

import java.nio.ByteBuffer ;
import java.util.Random ;

/**
 * Local Updater Simulator
 * 
 * This class simulates the traffic of the parking areas of the system.
 * Needs to be replaced by LocalUpdater when the sensor network is deployed
 * 
 * @author Maria Truvolo - Federico Vastarini
 * @version 1.0.0
 * @since   2017-02-05 
 */
public class LocalUpdaterSim extends Thread {
	private int len ; // data length
	private ByteBuffer bb ; // byte buffer
	
/**
 * Class standard constructor
 * Takes a reference to the byte buffer low level memory block and an integer representing its length
 * @param bb the byte buffer object
 * @param len the length of the buffer
 */
	public LocalUpdaterSim(ByteBuffer bb, int len) {
		this.len = len ; // set the data length
		this.bb = bb ; // set the byte buffer reference
		}
	
/**
 * Override of the run method from Thread class
 * It is the main method that runs every x seconds in order to update the bitmap
 */
	@Override
	public void run() {
		Random rnd = new Random() ; // simulation purpose
		int byteNumber ; // byte number in the buffer array
		byte bitNumber ; // bit number in the buffer byte
		
		while(true) { // keep running
			byteNumber = rnd.nextInt(13) ; // random a byte from the first 13
			bitNumber = (byte)(1 << rnd.nextInt(8)) ; // random a bit of the first byte
			
			if ((bb.get(byteNumber) & bitNumber) == 0x00) { // if the bit is not set
				bb.put(byteNumber, (byte)(bb.get(byteNumber) | bitNumber)) ; // set it :: comment to unset all
				}
			else { // if it is set
				bb.put(byteNumber, (byte)(bb.get(byteNumber) & ~bitNumber)) ; // unset it :: comment to set all
				}
			
			try {
				sleep(1000) ; // wait 5 seconds :: modify to change the simulation speed
				} 
			catch (InterruptedException ie) { // if interrupted
				return ; // stop and return
				}
			}
		}
	}