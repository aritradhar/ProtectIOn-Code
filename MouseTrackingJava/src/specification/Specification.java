package specification;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Specification {
	
	public static BufferedImage makeUIFromSpecification(String jsonString)
	{
		int uiGap = 10;
		JSONObject jObject = null;
		try
		{
			jObject = new JSONObject(jsonString);
		}
		catch(JSONException ex)
		{
			return null;
		}
		String formName = jObject.getString("formName");
		String formId = jObject.getString("formId");
		int sizeH = jObject.getInt("sizeH");
		int sizeW = jObject.getInt("sizeW");
		BufferedImage renderedUi = new BufferedImage(sizeW, sizeH, BufferedImage.TYPE_3BYTE_BGR);
		
		for(int i = 0; i < sizeW; i++)
			for(int j = 0; j < sizeH; j++)	
				renderedUi.setRGB(j, i, Color.WHITE.getRGB());
		
		JSONArray jsonUiArray = jObject.getJSONArray("ui");
		
		
		int offsetX = 10, offSetY = 10;
		for(int i = 0; i < jsonUiArray.length(); i++)
		{
			JSONObject UIObject = jsonUiArray.getJSONObject(i);
			
			String type = UIObject.getString("type");
			String label = UIObject.getString("label");
			String enable = UIObject.getString("enable");
			int size = UIObject.getInt("size");
			
			Graphics2D g = (Graphics2D) renderedUi.getGraphics();
			if(type.equalsIgnoreCase("radio"))
			{
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(5));
				
				
				if(enable.equalsIgnoreCase("true"))
				{
					g.setColor(Color.GREEN);
					g.fillOval(offsetX, offSetY, size, size);

				}
				else
				{
					g.setColor(Color.BLACK);
					g.drawOval(offsetX, offSetY, size, size);
				}
				
				g.setColor(Color.BLACK);
				g.drawString(label, offsetX + size + uiGap, offSetY + size /2);
			}
			
			
			if(type.equalsIgnoreCase("button"))
			{
				g.setColor(Color.BLACK);
				g.drawRect(offsetX, offSetY, 100, 25);
				g.drawString(label, offsetX + size /2 + uiGap, offSetY + size /2);
			}
			

			offSetY +=  size + uiGap;
			
		}
		
		return renderedUi;
	}

	public static void main(String[] args) throws IOException {
		
		String fileString = new String(Files.readAllBytes(new File("spec.json").toPath()), StandardCharsets.UTF_8);
		BufferedImage bi = makeUIFromSpecification(fileString);
		ImageIO.write(bi, "png", new File("ui.png"));
		
		System.out.println("Done...");
	}
}
