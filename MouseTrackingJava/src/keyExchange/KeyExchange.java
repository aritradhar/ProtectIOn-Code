package keyExchange;

import java.io.IOException;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import app.SerialCommunicationLinux;


public class KeyExchange {

	public static boolean setPK = false;
	public static void sendPKtoDevice(String pkJSON) throws IOException
	{
		if(!setPK)
		{
			JSONObject jObject = new JSONObject(pkJSON);
			String keyBase64 = null;
			try
			{
				keyBase64 = jObject.getString("pk");
			}
			catch(JSONException ex)
			{
				return;
			}
			
			//SerialCommunicationLinux.writeQueue.add(keyBase64);
			for(int i = 0; i < keyBase64.length(); i++)
			{
				SerialCommunicationLinux.writeQueue.add((int) keyBase64.charAt(i));
			}
			
			KeyExchange.setPK = true;
			System.out.println("Received pk from the server -> Device : " + keyBase64 + " | " + keyBase64.length());
		}
		else
		{
			return;
		}
	}

}
