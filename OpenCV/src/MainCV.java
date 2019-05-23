import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainCV {
	public static void main (String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
		System.out.println("mat = " + mat.dump());
		
		FrameProcessor fp = new FrameProcessor();
		System.out.println("Running on: " + System.getProperty("user.dir"));
		
		String inputVideo = "3";
		
		long startTime = System.currentTimeMillis();
		fp.openVideoFile("./Videos/", inputVideo, ".mp4");
		long endTime = System.currentTimeMillis();
		System.out.println("Execution time: " + (endTime - startTime));
	}
	
}
