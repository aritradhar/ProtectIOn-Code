package keyExchange;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

import org.json.JSONObject;
import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.curve25519.Curve25519KeyPair;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenerateDummyKey {
	
	public static void main(String[] args) throws Exception {
		
		Curve25519 curve = Curve25519.getInstance("best");
		Curve25519KeyPair kp = curve.generateKeyPair();
		JSONObject jo1 = new JSONObject();
		
		String pkStr = Base64.getEncoder().encodeToString(kp.getPublicKey());
		jo1.put("pk", pkStr);
		
		JSONObject jo2 = new JSONObject();
		String skStr = Base64.getEncoder().encodeToString(kp.getPrivateKey());
		jo2.put("pk", skStr);
		
		FileWriter fw = new FileWriter("pk.json");
		fw.write(jo1.toString(1));
		fw.flush();
		fw.close();
		
		fw = new FileWriter("sk.json");
		fw.write(jo1.toString(1));
		fw.flush();
		fw.close();
		
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(jo1.toString(1), BarcodeFormat.QR_CODE, 400, 400);

		Path path = FileSystems.getDefault().getPath("pk.png");
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
		
		System.out.println("Done..");
	}

}
