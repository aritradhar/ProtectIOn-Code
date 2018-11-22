package app;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.curve25519.Curve25519KeyPair;

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
public class KeyGen {
	
	public static void main(String[] args) throws WriterException, IOException {
		Curve25519 cipher = Curve25519.getInstance(Curve25519.BEST);
		Curve25519KeyPair kp = cipher.generateKeyPair();
		byte[] pk = kp.getPublicKey();
		byte[] sk = kp.getPrivateKey();
		
		System.out.println(Base64.getEncoder().encodeToString(pk));
		
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(Base64.getEncoder().encodeToString(pk), BarcodeFormat.QR_CODE, 250, 250);

        Path path = FileSystems.getDefault().getPath("QRCode.png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        
        System.out.println("Done");
	}

}
