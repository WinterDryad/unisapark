package localServer ;

import java.nio.ByteBuffer ;
import java.net.URL ;
import java.net.URLConnection ;
import java.net.MalformedURLException ;
import java.io.InputStreamReader ;
import java.io.BufferedReader ;
import java.io.IOException ;
import java.util.zip.CRC32 ;
import org.apache.commons.codec.binary.Base64 ;

/**
 * Remote Updater
 * 
 * This class creates the connection between the local and remote servers.
 * It takes the data from the byte buffer, packs it and sends it to the remote server.
 * Then checks the received ack for a successfull connection.
 * 
 * @author Maria Truvolo - Federico Vastarini
 * @version 1.0.0
 * @since   2017-02-05 
 */
public class RemoteUpdater extends Thread {
	private String url ; // base url
	private int len ; // data length
	private ByteBuffer bb ; // byte buffer
	
/**
 * Class standard constructor
 * Takes the url of the remote server resource,
 * a reference to the byte buffer low level memory block and an integer representing its length
 * @param url the url of the remote server resource
 * @param bb the byte buffer object
 * @param len the length of the buffer
 */
	public RemoteUpdater(String url, ByteBuffer bb, int len) {
		this.url = url ; // set the base url
		this.len = len ; // set the data length
		this.bb = bb ; // set the byte buffer reference
		}
	
/**
 * Override of the run method from Thread class
 * It is the main method that runs every x seconds in order to estabilish the data exchange
 * between the local and remote servers
 */
	@Override
	public void run() {
		URL remoteURL ; // full remote url
		CRC32 crc = new CRC32() ; // crc encoder
		long crcValue = 0 ; // crc value
		byte[] ba = new byte[this.len] ; // set the byte array
		String ack = ""; // set the ack string
		
		try {
			while(true) { // keep running
				try {
					sleep(5000) ; // wait 5 seconds
					} 
				catch (InterruptedException ie) { // if interrupted
					return ; // stop and return
					}
				
				for (int i = 0 ; i < this.len ; ++i) // for each byte in the bitmap
					ba[i] = this.bb.get(i) ; // add it to the array
					
				crc.reset() ; // reset the crc
				crc.update(Base64.encodeBase64(ba)) ; // update the crc with the new encoded byte array
				crcValue = crc.getValue() ; // get the crc value
				
				remoteURL = new URL(this.url + "?up_bm=" + Base64.encodeBase64String(ba) + "&up_ack=" + crcValue) ; // create the URL object with the base64encoded data
				
				ack = new BufferedReader(new InputStreamReader(remoteURL.openConnection().getInputStream())).readLine() ; // send the data and get back the ack
				
				try {
					if (crcValue != Long.parseLong(ack)) // try to check the ack
						System.out.println("the updater cat is on fire!!!111") ; // tell to the system that there has been a problem
					}
				catch (NumberFormatException nfe) { // caused by Long class
						System.out.println("invalid ack parse: " + ack) ; // tell if ack is uncheckable
					}
				}
			}
		catch (MalformedURLException mue) { // caused by URL class
			mue.printStackTrace() ;
			}
		catch (IOException ioe) { // caused by BufferedReader and InputStreamReader
			ioe.printStackTrace() ;
			}
		}
	}