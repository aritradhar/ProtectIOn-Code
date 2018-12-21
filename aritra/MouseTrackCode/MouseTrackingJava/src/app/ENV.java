package app;

import java.util.Arrays;

public class ENV {

	public static final int SCREEN_X = 1919;
	public static final int SCREEN_Y = 1079;
	
	public static final String DEVICE = "/dev/ttyUSB0";
	
	public static final char[] intSend = new char[2];
	public static final char[] pkSend = new char[2];
	
	public static final int OVERLAY_OFFSET_X = 50, OVERLAY_OFFSET_Y = 50;
	
	static
	{
		Arrays.fill(intSend, 'i');
		Arrays.fill(pkSend, 'p');
	}
}
