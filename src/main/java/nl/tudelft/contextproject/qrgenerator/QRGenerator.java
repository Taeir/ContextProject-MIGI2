package nl.tudelft.contextproject.qrgenerator;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.logging.Log;

/**
 *	QR code generator class. 
 *	Singleton that will generate a QR code and place it in {@link #LOCATION}.
 */
public final class QRGenerator {

	//Location and name of QR image.
	public static final String LOCATION = "qrcode.png";
	//Width of QR image.
	public static final int WIDTH = 250;
	//Height of QR image.
	public static final int HEIGTH = 250;

	//Use eager initialization of the singleton.
	private static final QRGenerator INSTANCE = new QRGenerator();

	//Holds IP of server.
	private static String hostingAddress;
	//Port number of server.
	private static int portNumber = Main.PORT_NUMBER;

	/**
	 * Private constructor to prevent initialization elsewhere.
	 * Will try to find IP.
	 * @throws SocketException 
	 */
	private QRGenerator() {
		try {
			setHostingAddress(NetworkInterface.getNetworkInterfaces());
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the hosting address as an URL String.
	 * @return
	 * 			hosting address
	 */
	public static String getURL() {
		return hostingAddress;
	}
	
	/**
	 * Set the hosting address as an URL String.
	 * @param url
	 * 				hosting addres
	 */ 
	public static void setURL(String url) {
		hostingAddress = url;
	}

	/**
	 * Get the QRgenerator instance.
	 * @return
	 * 			QRgenerator
	 */
	public static QRGenerator getInstance() {
		return INSTANCE;
	}

	/**
	 * Generate a QR code in {@link #LOCATION}.
	 * First, get the hostingAddress by using the Java InetAddress class.
	 * Then, create the QRgen as a ByteArrayOutputStream.
	 * And finally write the ByteArrayOutputStream to disk.
	 * 
	 * @param portNumber
	 * 				the port number which hosts the application
	 */
	public static void generateQRcode(String portNumber) {
		Log.getLog("WebInterface").info("Creating QRcode with address: " + hostingAddress);
		ByteArrayOutputStream byteArrayOutputStream = QRCode.from(hostingAddress).to(ImageType.PNG).withSize(WIDTH, HEIGTH).stream();

		try (OutputStream outputStream = new FileOutputStream(LOCATION)) {
			byteArrayOutputStream.writeTo(outputStream);
			Log.getLog("WebInterface").info("Created QRcode with address: " + hostingAddress + " as " + LOCATION);
		} catch (IOException e) {
			Log.getLog("WebInterface").severe("Unable to write qr code to disk.", e);
			e.printStackTrace();
		}
	}

	/**
	 * Set the correct ipv4 address of this computer.
	 * This method needs all network interfaces of the computer.
	 * It will for each network interface check all the network addresses.
	 * The IP address will be among those, so all address that are personally or locally based
	 * are filtered out.
	 * Please note that you cannot have other network adapters (such from virtual machines)
	 * running as they will interfere with the correct adapter.
	 * @param n
	 * 			an enumeration of all network interfaces on this device
	 */
	protected static void setHostingAddress(Enumeration<NetworkInterface> n) {
		hostingAddress = "";
		while (n.hasMoreElements()) {
			NetworkInterface e = n.nextElement();
			Enumeration<InetAddress> a = e.getInetAddresses();

			while (a.hasMoreElements()) {
				InetAddress addr = a.nextElement();
				String hostAddress = addr.getHostAddress();
				if (!hostAddress.startsWith("127.") 
						&& !hostAddress.contains(":")) {
					hostingAddress = addr.getHostAddress();
				}
			}
		}
		hostingAddress = "https://" + hostingAddress + ":" + portNumber + "/";
	}
}
