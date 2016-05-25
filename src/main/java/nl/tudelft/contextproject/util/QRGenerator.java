package nl.tudelft.contextproject.util;

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
 * Singleton that will generate a QR code and place it in {@link #LOCATION}.
 *
 * It possible to automatically try and find the hosting address and generate the QR code with:
 * QRGenerator.getInstance().generateQRcode();
 * 
 * or to manually set the URL and generate the QR code with:
 * QRGenerator.getInstance().setURL("URL string");
 * QRGenerator.getInstance().generateQRcode();
 */
public final class QRGenerator {
	public static final int HEIGTH = 250;
	public static final int WIDTH = 250;
	public static final String LOCATION = "qrcode.png";

	//Use eager initialization of the singleton.
	private static final QRGenerator INSTANCE = new QRGenerator();
	private static final Log LOG = Log.getLog("WebInterface");
	private String hostingAddress;
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
	 *
	 * @return
	 *		hosting address
	 */
	public String getURL() {
		return hostingAddress;
	}

	/**
	 * Set the hosting address as an URL String.
	 *
	 * @param url
	 *		hosting address
	 */ 
	public void setURL(String url) {
		hostingAddress = url;
	}

	/**
	 * @return
	 *
	 *		the QRGenerator instance
	 */
	public static QRGenerator getInstance() {
		return INSTANCE;
	}

	/**
	 * Generate a QR code in {@link #LOCATION}.
	 *
	 * <p>First, get the hostingAddress by using the Java InetAddress class.
	 * Then, create the QRgen as a ByteArrayOutputStream.
	 * And finally write the ByteArrayOutputStream to disk.
	 */
	public void generateQRcode() {
		LOG.info("Creating QR code with address: " + hostingAddress);
		ByteArrayOutputStream byteArrayOutputStream = QRCode.from(hostingAddress).to(ImageType.PNG).withSize(WIDTH, HEIGTH).stream();

		try (OutputStream outputStream = new FileOutputStream(LOCATION)) {
			byteArrayOutputStream.writeTo(outputStream);
			LOG.info("Created QR code with address: " + hostingAddress + " as " + LOCATION);
		} catch (IOException e) {
			LOG.severe("Unable to write qr code to disk.", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a QRCode and returns it as a ByteArrayOutputStream.
	 * 
	 * @return
	 * 		a byte stream with the qr code image
	 */
	public ByteArrayOutputStream streamQRcode() {
		return QRCode.from(hostingAddress).to(ImageType.PNG).withCharset("UTF-8").withSize(WIDTH, HEIGTH).stream();
	}

	/**
	 * Set the correct ipv4 address of this computer.
	 *
	 * <p>This method needs all network interfaces of the computer.
	 * It will for each network interface check all the network addresses.
	 * The IP address will be among those, so all address that are fake or are
	 * IPv6 are filtered out.
	 * Please note that you cannot have other network adapters (such from virtual machines)
	 * running as they will interfere with the correct adapter!
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
			LOG.severe("Unable to get network addresses.", e);
			e.printStackTrace();
		}
	}
}
