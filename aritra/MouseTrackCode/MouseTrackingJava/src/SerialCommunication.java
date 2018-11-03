import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 *
 */
public class SerialCommunication {


	public static OutputStream serialOutputStream;


	public SerialCommunication() {
		super();
	}

	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
				System.out.println("Connected");
				InputStream in = serialPort.getInputStream();
				serialOutputStream = serialPort.getOutputStream();

				//long start = System.currentTimeMillis();
				serialPort.addEventListener(new SerialReader(in));
				serialPort.notifyOnDataAvailable(true);
				//System.out.println(System.currentTimeMillis() - start);

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	
	public static Integer currentX = null;
	public static Integer currentY = null;
	
	public static class SerialReader implements SerialPortEventListener {
		private InputStream in;
		private byte[] buffer = new byte[1024];

		public SerialReader(InputStream in) {
			this.in = in;
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;

			try {
				
				int len = 0;
				while ((data = in.read()) > -1) {
					buffer[len++] = (byte) data;	
				}	
				
				//System.out.print(new String(buffer, 0, len));
				String captureData = new String(buffer, 0, len);
				String[] captureDataSplits = captureData.split(",");
				int x = Integer.parseInt(captureDataSplits[1]);
				int y = Integer.parseInt(captureDataSplits[2]);
				
				if(currentX == null && currentY == null)
				{
					currentX = CaptureScreen.curr_x;
					currentY = CaptureScreen.curr_y;
				}
				currentX = currentX + x;
				currentY = currentY + y;
				System.out.println(currentX + ", " + currentY + " | " + x + ", " + y);
				CaptureScreen.capture.getGraphics().drawImage(CaptureScreen.redSquare, currentY, currentX, null);
				
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			} 
		}
	
	
	} 

	public static void main(String[] args) {

		try {
			(new SerialCommunication()).connect("COM19");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}