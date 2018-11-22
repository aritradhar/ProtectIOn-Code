package app;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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
public class ImageEncoder {

	public static void main(String[] args) throws IOException, WriterException {

		for(int i = 1; i <= 3; i++)
		{
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(i+".jpg", BarcodeFormat.QR_CODE, 250, 250);

			Path path = FileSystems.getDefault().getPath(i+".png");
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
		}
		
		System.out.println("Done");
	}

}
