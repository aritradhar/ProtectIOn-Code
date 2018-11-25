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
package specification;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * @author Aritra
 *
 */
public class MakeSPecQRCode {

	public static void main(String[] args) throws Exception {
		
		byte[] bytes = Files.readAllBytes(new File("spec.json").toPath());
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(new String(bytes, StandardCharsets.UTF_8), BarcodeFormat.QR_CODE, 500, 500);

        Path path = FileSystems.getDefault().getPath("spec.png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
		
	}
}
