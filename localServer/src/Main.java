package localServer ;

import java.nio.ByteBuffer ;
import java.lang.IllegalArgumentException ;

import localServer.LocalUpdaterSim ;
import localServer.RemoteUpdater ;
/**
 * Main Class of the local server
 *
 * Main Class of the local server.
 * It contains the main method and creates the threads for the remote server
 * and local sensor network connections.
 *
 * @author Maria Truvolo - Federico Vastarini
 * @version 1.0.0
 * @since   2017-02-05
 */
public class Main {
	public static final int dataLength = 1024 ; // set the data length constant

/**
 * Main method, Standard starting point of the application
 * @param args Standard arguments
 */
	public static void main(String[] args) {
		try {
			ByteBuffer sensorsBitMap = ByteBuffer.allocateDirect(dataLength) ; // create the bit string of the sensor

			LocalUpdaterSim lu = new LocalUpdaterSim(sensorsBitMap, dataLength) ; // create a new local updater simulator
			RemoteUpdater ru = new RemoteUpdater("http://www.remotedomain.com/setbitmapack.php", sensorsBitMap, dataLength) ; // create a new remote updater

			lu.start() ; // start the local updater thread
			ru.start() ; // start the remote updater thread

			int q = 0 ; // testing purpose
			while(q == 0) {} // testing purpose

			lu.interrupt() ; // stop the local updater thread
			ru.interrupt() ; // stop the remote updater thread

			sensorsBitMap.clear() ; // clear the buffer
			}
		catch (IllegalArgumentException iae) { // caused by ByteBuffer allocation
			iae.printStackTrace() ;
			}
		}
	}
