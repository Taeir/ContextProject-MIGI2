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
 * QR code generator class. 
 * Singleton that will generate a QR code and place it in {@link #location}.
 *
 * It possible to automatically try and find the hosting address and generate the QR code with:
 * QRGenerator.getInstance().generateQRcode();
 * 
 * or to manually set the URL and generate the QR code with:
 * QRGenerator.getInstance().setURL("URL string");
 * QRGenerator.getInstance().generateQRcode();
 */
public final class QRGenerator {

	//Use eager initialization of the singleton.
	private static final QRGenerator INSTANCE = new QRGenerator();

	//Location and name of QR image.
	public final String location = "qrcode.png";
	//Width of QR image.
	public final int width = 250;
	//Height of QR image.
	public final int heigth = 250;

	//Holds IP of server.
	private String hostingAddress;
	//Port number of server.
	private int portNumber = Main.PORT_NUMBER;

	/**
	 * Private constructor to prevent initialization elsewhere.
	 * Will try to find IP.
	 */
	private QRGenerator() {
		searchForHostAddress();
	}

	/**
	 * Get the hosting address as an URL String.
	 * @return
	 * 			hosting address
	 */
	public String getURL() {
		return hostingAddress;
	}

	/**
	 * Set the hosting address as an URL String.
	 * @param url
	 * 				hosting addres
	 */ 
	public void setURL(String url) {
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
	 * Generate a QR code in {@link #location}.
	 * First, get the hostingAddress by using the Java InetAddress class.
	 * Then, create the QRgen as a ByteArrayOutputStream.
	 * And finally write the ByteArrayOutputStream to disk.
	 */
	public void generateQRcode() {
		Log.getLog("WebInterface").info("Creating QR code with address: " + hostingAddress);
		ByteArrayOutputStream byteArrayOutputStream = QRCode.from(hostingAddress).to(ImageType.PNG).withSize(width, heigth).stream();

		try (OutputStream outputStream = new FileOutputStream(location)) {
			byteArrayOutputStream.writeTo(outputStream);
			Log.getLog("WebInterface").info("Created QR code with address: " + hostingAddress + " as " + location);
		} catch (IOException e) {
			Log.getLog("WebInterface").severe("Unable to write qr code to disk.", e);
			e.printStackTrace();
		}
	}

	/**
	 * Set the correct ipv4 address of this computer.
	 * This method needs all network interfaces of the computer.
	 * It will for each network interface check all the network addresses.
	 * The IP address will be among those, so all address that are fake or contain : are filtered out.
	 * Please note that you cannot have other network adapters (such from virtual machines)
	 * running as they will interfere with the correct adapter. 
	 */
	protected void searchForHostAddress() {
		hostingAddress = "";
		Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface e = networkInterfaces.nextElement();
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
			hostingAddress = "http://" + hostingAddress + ":" + portNumber + "/";
		} catch (SocketException e) {
			Log.getLog("WebInterface").severe("Unable to get network addresses.", e);
			e.printStackTrace();
		}
	}
}
