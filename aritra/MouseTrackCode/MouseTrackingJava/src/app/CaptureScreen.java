package app;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import keyExchange.KeyExchange;
import specification.Specification;


//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar 																* *
//PhD Researcher																  	* *
//ETH Zurich													   				    * *
//Zurich, Switzerland															    * *
//--------------------------------------------------------------------------------- * * 
///////////////////////////////////////////////// 									* *
//This program is meant to do world domination... 									* *
///////////////////////////////////////////////// 									* *
//*********************************************************************************** *
//*************************************************************************************

/**
 * @author Aritra
 *
 */

class SerialReaderThread extends Thread{
	public void run() {
		SerialCommunicationLinux.initialize();
		SerialCommunicationLinux.readData();
	}
}


class SerialReaderThreadWindows extends Thread{
	public void run() {
		SerialCommunicationLinux.initialize();
		SerialCommunicationLinux.readData();
		if(SerialCommunicationLinux.INSIDE_OVERLAY)
			CaptureScreen.capture.getGraphics().drawImage(CaptureScreen.blueSquare, SerialCommunicationLinux.currentX, SerialCommunicationLinux.currentY, null);
	}
}

public class CaptureScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -725074386379024683L;
	@SuppressWarnings("unused")
	private JPanel contentPane;
	public static ArrayList<int[]> trace = new ArrayList<>();
	public static boolean selectedTrace = false;
	public static File CURRENT_OVERLAY_STATE = null;
	public static int OVERLAY_X, OVERLAY_Y, OVERLAY_H, OVERLAY_W;
	public static volatile Specification ui;
	
	public static JLabel bufferlbl = new JLabel("New label");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("linux"))
					{
						SerialReaderThread th = new SerialReaderThread();
						th.start();
					}
					else
					{
						try {
							SerialCommunication com = new SerialCommunication();
							com.connect("COM19");
						} catch (Exception e) {
							System.err.println("Relayless mode");
						}
					}
					
					
					 Runnable runnable = new Runnable() {
					      public void run() {
					        try {
								SerialCommunicationLinux.writeData();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					      }
					    };
					    
					    ScheduledExecutorService service = Executors
					                    .newSingleThreadScheduledExecutor();
					    service.scheduleAtFixedRate(runnable, 0, 30, TimeUnit.MILLISECONDS);
					

					CaptureScreen frame = new CaptureScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	

	int last_x = 0, last_y = 0;
	Long frames = 0l, mouseFrames = 0l;

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws AWTException 
	 */
	public static BufferedImage capture = null;
	public static boolean firstRun = true; 
	public static Integer curr_x = null, curr_y = null;
	public static BufferedImage greenSquare, redSquare, blueSquare;


	public static BufferedImage makeRectangle(int w, int h)
	{
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		for(int i = 0; i < w; i++){
			for(int j = 0; j < h; j++){
				bi.setRGB(i, j, Color.GRAY.getRGB());
			}
		}
		return bi;
	}

	public static JLabel relayPoslbl;

	public CaptureScreen() throws IOException, AWTException 
	{
		setSize(1000, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Mirror");
		JLabel label = new JLabel();

		//final String USER_HOME = System.getProperty("user.home");
		//Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		Rectangle screenRect = new Rectangle(new Dimension(1920, 1080));
		//Dimension d = new Dimension(1280, 720);

		//GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		//int width = gd.getDisplayMode().getWidth();
		//int height = gd.getDisplayMode().getHeight();
		//Image cursor = ImageIO.read(new File("4871e80dee864d8cc605da971d5b8a3c-2.png"));

		int boxSide = 10, thickness = 5;

		greenSquare = new BufferedImage(boxSide, boxSide, BufferedImage.TYPE_3BYTE_BGR);
		redSquare = new BufferedImage(boxSide, boxSide, BufferedImage.TYPE_3BYTE_BGR);
		blueSquare = new BufferedImage(boxSide, boxSide, BufferedImage.TYPE_3BYTE_BGR);
		for(int i = 0; i < boxSide; i++)
		{
			for(int j = 0; j < boxSide; j++)
			{
				greenSquare.setRGB(j, i, Color.GREEN.getRGB());
				blueSquare.setRGB(j, i, Color.BLUE.getRGB());

				if(i < boxSide)
					redSquare.setRGB(j, i, Color.RED.getRGB());
				if((i >= boxSide && j < thickness) || (i >= boxSide && j > boxSide - thickness))
					redSquare.setRGB(j, i, Color.RED.getRGB());
				if(i > boxSide - thickness)
					redSquare.setRGB(j, i, Color.RED.getRGB());
			}
		}
		PointerInfo pointer = MouseInfo.getPointerInfo();
		int x = (int) pointer.getLocation().getX();
		int y = (int) pointer.getLocation().getY();

		JScrollPane scrollPane = new JScrollPane(label);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);

		JCheckBox chckbxNewCheckBox = new JCheckBox("enableTrace");
		panel.add(chckbxNewCheckBox);

		JButton btnReset = new JButton("Reset");
		panel.add(btnReset);

		//JLabel lblCurrentPosition = new JLabel("Current position");
		//panel.add(lblCurrentPosition);

		//JLabel currentPoslbl = new JLabel("(0, 0)");
		//panel.add(currentPoslbl);

		//JLabel lblStartPosition = new JLabel("Start Position");
		//panel.add(lblStartPosition);

		JLabel startPoslbl = new JLabel(x + ", " + y);
		panel.add(startPoslbl);

		JLabel lblRelayMousePosition = new JLabel("Relay pos: ");
		panel.add(lblRelayMousePosition);

		CaptureScreen.relayPoslbl = new JLabel("(0,0)");
		panel.add(CaptureScreen.relayPoslbl);

		JLabel lblClickPosition = new JLabel("Host pos: ");
		panel.add(lblClickPosition);

		JLabel clickPoslbl = new JLabel("(0,0)");
		panel.add(clickPoslbl);

		JLabel lblFrames = new JLabel("Frames");
		panel.add(lblFrames);

		JLabel framelbl = new JLabel("New label");
		panel.add(framelbl);

		JLabel lblMouse = new JLabel("Mouse");
		panel.add(lblMouse);

		JLabel mouselbl = new JLabel("New label");
		panel.add(mouselbl);

		JLabel lblFps = new JLabel("FPS");
		panel.add(lblFps);

		JLabel fpslbl = new JLabel("New label");
		panel.add(fpslbl);		
		
		JLabel lblBuffer = new JLabel("Buffer health");
		panel.add(lblBuffer);

		panel.add(bufferlbl);
		

		Runnable myRunnable = new Runnable() {

			@Override
			public void run() 
			{
				if(firstRun)
				{
					firstRun = false;
					SerialCommunicationLinux.currentX = x;
					SerialCommunicationLinux.currentY = y;
				}
				long start = System.currentTimeMillis();		

				try {
					capture = new Robot().createScreenCapture(screenRect);

					Result result = QRCodeReader.decodeQRCode(capture);
					//
					if(result != null)
					{
						String decodedText = result.getText();
						ResultPoint[] points = result.getResultPoints();
						//capture.getGraphics().drawString(result.getText(), (int) points[1].getX(), (int) points[1].getY() + 15);
						int h = (int) points[0].getY() - (int) points[1].getY();
						int w = (int) points[2].getX() - (int) points[1].getX();
						
						CaptureScreen.ui = new Specification(decodedText);
						BufferedImage retrievedImage = CaptureScreen.ui.getUI();
						
						if(retrievedImage != null)
						{
							OVERLAY_X = (int) points[1].getX();
							OVERLAY_Y = (int) points[1].getY();
							OVERLAY_H = retrievedImage.getHeight();
							OVERLAY_W = retrievedImage.getWidth();

							capture.getGraphics().drawImage(
									retrievedImage, 
									(int) points[1].getX(), (int) points[1].getY(), null);
						}
						else
						{
							/*capture.getGraphics().drawImage(
									makeRectangle(h,w), 
									(int) points[1].getX(), (int) points[1].getY(), null);
									*/
							KeyExchange.sendPKtoDevice(decodedText);
						}
					}

				} catch (AWTException | IOException e1) {
					e1.printStackTrace();
				}

				chckbxNewCheckBox.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED)
						{
							selectedTrace = true;
							trace = new ArrayList<>();
						}
						else
							selectedTrace = false;
					}
				});

				PointerInfo pointer = MouseInfo.getPointerInfo();
				int x = (int) pointer.getLocation().getX();
				int y = (int) pointer.getLocation().getY();

				if(selectedTrace)
				{
					for(int[] tracePoint : trace)
						capture.getGraphics().drawImage(greenSquare, tracePoint[0], tracePoint[1], null);
				}

				capture.getGraphics().drawImage(greenSquare, x, y, null);


				//capture.getGraphics().drawImage(blackSquare, x, y, null);
				//capture.getGraphics().drawImage(cursor, x, y, null);

				//capture.getGraphics().drawString(x + " : " + y, x, y);
				//System.out.println(x + ":" + y);

				ImageIcon image = new ImageIcon(capture);
				label.setIcon(image);
				curr_x = x;
				curr_y = y;
				//currentPoslbl.setText(x + ", " + y);
				repaint();

				Long t =  1000 / (System.currentTimeMillis() - start);
				fpslbl.setText(t.toString());

				frames++;
				framelbl.setText(frames.toString());


				clickPoslbl.setText("(" + x + ", " + y + ")");
				if(x != last_x && y != last_y)
				{
					if(selectedTrace)
						trace.add(new int[] {x, y});
					//System.out.println("Screen diff:" + (x - last_x) + ", " + (y - last_y));
					last_x = x;
					last_y = y;

					mouseFrames++;
					mouselbl.setText(mouseFrames.toString());
				}	        
			}
		};


		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trace = new ArrayList<>();
			}
		});
		//pack();

		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
		executor.scheduleAtFixedRate(myRunnable, 0, 1, TimeUnit.MILLISECONDS);
	}

	private static class Listener implements AWTEventListener {
		@Override
		public void eventDispatched(AWTEvent event) {
			System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
			System.out.println(event);
		}
	}


}
