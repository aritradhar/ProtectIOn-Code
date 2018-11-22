package specification;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.json.JSONArray;
import org.json.JSONObject;

public class Specification {
	
	public static BufferedImage makeUIFromSpecification(String jsonString)
	{
		JSONObject jObject = new JSONObject(jsonString);
		String formName = jObject.getString("formName");
		String formId = jObject.getString("formId");
		int sizeH = jObject.getInt("sizeH");
		int sizeW = jObject.getInt("sizeW");
		BufferedImage renderedUi = new BufferedImage(sizeW, sizeH, BufferedImage.TYPE_3BYTE_BGR);
		
		for(int i = 0; i < sizeW; i++)
			for(int j = 0; j < sizeH; j++)	
				renderedUi.setRGB(j, i, Color.LIGHT_GRAY.getRGB());
		
		JSONArray jsonUiArray = jObject.getJSONArray("ui");
		
		for(int i = 0; i < jsonUiArray.length(); i++)
		{
			JSONObject UIObject = jsonUiArray.getJSONObject(i);
			
			String type = UIObject.getString("type");
			String loc = UIObject.getString("loc");
			String label = UIObject.getString("label");

			
		}
		
		return renderedUi;
	}

}
