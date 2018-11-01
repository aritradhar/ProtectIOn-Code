import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;

import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;


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
public class CaptureScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -725074386379024683L;
	private JPanel contentPane;
	public static ArrayList<int[]> trace = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						SerialCommunication com = new SerialCommunication();
						com.connect("COM19");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
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
	public static BufferedImage greenSquare, redSquare;
	public CaptureScreen() throws IOException, AWTException 
	{
		
		
		setSize(1000, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mirror");
        JLabel label = new JLabel();
        
        final String USER_HOME = System.getProperty("user.home");
        //Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        Rectangle screenRect = new Rectangle(new Dimension(1920, 1080));
        
        //Dimension d = new Dimension(1280, 720);
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Image cursor = ImageIO.read(new File("4871e80dee864d8cc605da971d5b8a3c-2.png"));
        
        int boxSide = 20, thickness = 5;
        
        greenSquare = new BufferedImage(boxSide, boxSide, BufferedImage.TYPE_3BYTE_BGR);
        redSquare = new BufferedImage(boxSide, boxSide, BufferedImage.TYPE_3BYTE_BGR);
        for(int i = 0; i < boxSide; i++){
            for(int j = 0; j < boxSide; j++){
                greenSquare.setRGB(j, i, Color.GREEN.getRGB());
                
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
        
        JButton btnReset = new JButton("Reset");
        panel.add(btnReset);
        
        JLabel lblCurrentPosition = new JLabel("Current position");
        panel.add(lblCurrentPosition);
        
        JLabel currentPoslbl = new JLabel("New label");
        panel.add(currentPoslbl);
        
        JLabel lblStartPosition = new JLabel("Start Position");
        panel.add(lblStartPosition);
        
        JLabel startPoslbl = new JLabel(x + ", " + y);
        panel.add(startPoslbl);
        
        JLabel lblClickPosition = new JLabel("Click position");
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
        
        Runnable myRunnable = new Runnable() {

			@Override
			public void run() {
				
				long start = System.currentTimeMillis();		
				
		        
				try {
					capture = new Robot().createScreenCapture(screenRect);
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
	            for(int[] tracePoint : trace)
	            	capture.getGraphics().drawImage(greenSquare, tracePoint[0], tracePoint[1], null);
	            
	            
	            //capture.getGraphics().drawImage(blackSquare, x, y, null);
	            //capture.getGraphics().drawImage(cursor, x, y, null);
	            
	            //capture.getGraphics().drawString(x + " : " + y, x, y);
	            //System.out.println(x + ":" + y);
		        
		        ImageIcon image = new ImageIcon(capture);
		        label.setIcon(image);
		        currentPoslbl.setText(x + ", " + y);
		        repaint();
		        
		        Long t =  1000 / (System.currentTimeMillis() - start);
		        fpslbl.setText(t.toString());
		        
		        frames = frames + 1;
				framelbl.setText(frames.toString());
		        PointerInfo pointer = MouseInfo.getPointerInfo();
	            int x = (int) pointer.getLocation().getX();
	            int y = (int) pointer.getLocation().getY();
	            
	            clickPoslbl.setText("(" + x + ", " + y + ")");
	            if(x != last_x && y != last_y)
	            {
	            	trace.add(new int[] {x, y});
	            	last_x = x;
	            	last_y = y;
	            	mouseFrames++;
	            	mouselbl.setText(mouseFrames.toString());
	            }
	            
		        
		        //System.out.println(System.currentTimeMillis() - start);
		        //System.out.println("Update");
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
