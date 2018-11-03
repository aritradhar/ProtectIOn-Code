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
				currentX = currentX + Integer.parseInt(captureDataSplits[1]);
				currentY = currentY - Integer.parseInt(captureDataSplits[1]);
				CaptureScreen.capture.getGraphics().drawImage(CaptureScreen.redSquare, currentX, currentY, null);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
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
