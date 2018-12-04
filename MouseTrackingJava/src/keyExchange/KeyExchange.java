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
			
			SerialCommunicationLinux.writeQueue.add(keyBase64);
			KeyExchange.setPK = true;
		}
		else
		{
			return;
		}
	}

}
