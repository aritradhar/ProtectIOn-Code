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
	
	public static int currentX = 100;
	public static int currentY = 100;
	
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
					int x = Integer.parseInt(captureDataSplits[1]);
					int y = Integer.parseInt(captureDataSplits[2]);
					currentX = currentX + x;
					currentY = currentY + y;
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
