import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SerialCommunicationLinux {
	
	public static BufferedReader br;
	
	public static boolean initialize() {
		
		try {
			br = new BufferedReader(new FileReader("/dev/ttyUSB0"));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static int currentX;
	public static int currentY;
	
	public static String readData()
	{
		String str = null;
		try {
			while((str = br.readLine()) != null)
			{
				String[] captureDataSplits = str.split(",");
				//wait for the capture initialized by the other thread
				if(captureDataSplits.length == 3 && CaptureScreen.capture != null)
				{
					CaptureScreen.relayPoslbl.setText("(" + currentX + ", " + currentY + ")");
					int x = Integer.parseInt(captureDataSplits[1]);
					int y = Integer.parseInt(captureDataSplits[2]);
					
					if(currentX + x >= 0 && currentX + x <= ENV.SCREEN_X)
						currentX = currentX + x;
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
					
					CaptureScreen.capture.getGraphics().drawImage(CaptureScreen.redSquare, currentX, currentY, null);
					
					System.out.println(currentX + ", " + currentY + " | " + x + ", " + y);
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

}
