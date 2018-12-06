package app;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class SerialCommunicationLinux {
	
	public static BufferedReader br;
	public static int writeQueueCapacity = 5000;
	public static Queue<Object> writeQueue = new ArrayBlockingQueue<>(writeQueueCapacity, true);
	
	public static boolean initialize() {
		
		try {
			br = new BufferedReader(new FileReader(ENV.DEVICE));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//mouse position from the device
	public static volatile int currentX;
	public static volatile int currentY;
	public static boolean INSIDE_OVERLAY = false;
	
	public static String readData()
	{
		String str = null;
		try {
			while((str = br.readLine()) != null)
			{
				//System.out.println(str);
				String[] captureDataSplits = str.split(",");
				//wait for the capture initialized by the other thread
				//move and drag
				if(captureDataSplits.length == 3 && CaptureScreen.capture != null)
				{
					CaptureScreen.relayPoslbl.setText("(" + currentX + ", " + currentY + ")");
					int x = Integer.parseInt(captureDataSplits[1]);
					int y = Integer.parseInt(captureDataSplits[2]);
					
					if(currentX + x >= 0 && currentX + x <= ENV.SCREEN_X)
						currentX = currentX +  x;
					else if(currentX + x < 0)
						currentX = 0;
					else 
						currentX = ENV.SCREEN_X;
					
					if(currentY + y >= 0 && currentY + y <= ENV.SCREEN_Y)
						currentY = currentY + y;
					else if(currentY + y < 0)
						currentY = 0;
					else
						currentY = ENV.SCREEN_Y;
					
					//CaptureScreen.capture.getGraphics().drawImage(CaptureScreen.redSquare, currentX, currentY, null);
					
					try
					{
						System.out.println(CaptureScreen.ui.getClickedUI(currentX - CaptureScreen.OVERLAY_X, currentY - CaptureScreen.OVERLAY_Y));
						System.out.print("");
					}
					catch(NullPointerException ex)
					{
						System.out.println("None");
					}
					if(currentX > CaptureScreen.OVERLAY_X && currentX < CaptureScreen.OVERLAY_X + CaptureScreen.OVERLAY_W &&
							currentY > CaptureScreen.OVERLAY_Y && currentY < CaptureScreen.OVERLAY_Y + CaptureScreen.OVERLAY_H )
					{
						CaptureScreen.capture.getGraphics().drawImage(CaptureScreen.blueSquare, currentX, currentY, null);
						INSIDE_OVERLAY = true;
						writeQueue.add(new Integer(1));
					}
					else
					{
						CaptureScreen.capture.getGraphics().drawImage(CaptureScreen.redSquare, currentX, currentY, null);
						INSIDE_OVERLAY = false;
						writeQueue.add(new Integer(0));
					}
					
					System.out.println(currentX + ", " + currentY + " | " + x + ", " + y + " | " + INSIDE_OVERLAY);
				}
				//press
				if(captureDataSplits.length == 2 && CaptureScreen.capture != null)
				{
					int activity = Integer.parseInt(captureDataSplits[0]);
					String button = captureDataSplits[1];
					
					String activityString = (activity == 0) ? "press" : "release";
					System.out.println("Activity : " + button + " : " + activityString + " @ " +  currentX + ", " + currentY + " inside overlay : " + INSIDE_OVERLAY);
					
					if(INSIDE_OVERLAY)
					{
						CaptureScreen.CURRENT_OVERLAY_STATE = new File("2.jpg");
					}
				}
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			//System.exit(0);
		}
		return str;
	}
	
	public static boolean close()
	{
		try{
			br.close();
			return true;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public static void writeData() throws IOException
	{
		CaptureScreen.bufferlbl.setText(writeQueue.size() + "/" + writeQueueCapacity);
		if(writeQueue.isEmpty())
			return;
		
		Object dataToWrite = writeQueue.poll();
		BufferedWriter bw = new BufferedWriter(new FileWriter(ENV.DEVICE));
		
		if(dataToWrite instanceof String)
		{
			/*
			char[] data = new char[((String) dataToWrite).length() + 1];
			data[0] = 'b';
			for(int i = 0; i < ((String) dataToWrite).length(); i++)
				data[1 + i] = ((String) dataToWrite).charAt(i);
			System.out.println(new String(data));
			bw.write(data);
			*/
			bw.write((String) dataToWrite);
		}
		else
		{
			/*
			char[] data = new char[2];
			data[0] = 'a';
			data[1] = (char) dataToWrite;
			System.out.println(new String(data));
			bw.write(data);
			*/
			bw.write((int) dataToWrite);
		}
		bw.flush();
		bw.close();
	}

}
