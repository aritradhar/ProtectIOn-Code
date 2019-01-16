package specification;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Specification {
	
	
	String specification;
	BufferedImage renderedUI;
	List<String> inputs;
	public Map<String, LocationBox> uiLocationBoxMap;
	
	public Specification(String specification) {
		this.specification = specification;
		this.inputs = new ArrayList<>();
		this.uiLocationBoxMap = new HashMap<>();
		this.renderedUI = this.makeUIFromSpecification(this.specification);		
	}
	
	public BufferedImage getUI()
	{
		return this.renderedUI;
	}
	
	
	public String getClickedUI(int x, int y)
	{
		for(String key : this.uiLocationBoxMap.keySet())
		{
			LocationBox locationBox = this.uiLocationBoxMap.get(key);
			if(locationBox.isLocationInBox(new Location(x, y)))
				return key;
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public BufferedImage makeUIFromSpecification(String jsonString)
	{
		
		JSONObject jObject = null;
		try
		{
			jObject = new JSONObject(jsonString);
		}
		catch(JSONException ex)
		{
			return null;
		}
		if(!jObject.has("formName"))
			return null;
		
		String formName = jObject.getString("formName");
		String formId = jObject.getString("formId");
		int sizeH = jObject.getInt("sizeH");
		int sizeW = jObject.getInt("sizeW");
		int uiGap = jObject.getInt("uiGap");
		BufferedImage renderedUi = new BufferedImage(sizeW, sizeH, BufferedImage.TYPE_3BYTE_BGR);
		
		for(int i = 0; i < sizeW; i++)
			for(int j = 0; j < sizeH; j++)	
				renderedUi.setRGB(j, i, Color.WHITE.getRGB());
		
		JSONArray jsonUiArray = jObject.getJSONArray("ui");
		
		
		int offsetX = 10, offsetY = 10;
		for(int i = 0; i < jsonUiArray.length(); i++)
		{
			JSONObject UIObject = jsonUiArray.getJSONObject(i);
			
			String type = UIObject.getString("type");
			String label = UIObject.getString("label");
			String enable = UIObject.getString("enable");
			String id = UIObject.getString("id");
			
			
			
			Graphics2D g = (Graphics2D) renderedUi.getGraphics();
			if(type.equalsIgnoreCase("radio"))
			{
				
				int size = UIObject.getInt("size");
				
				LocationBox locationBox = new LocationBox(new Location(offsetX, offsetY), size, size);
				this.uiLocationBoxMap.put(id, locationBox);
				
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(5));
				
				
				if(enable.equalsIgnoreCase("true"))
				{
					g.setColor(Color.GREEN);
					g.fillOval(offsetX, offsetY, size, size);

				}
				else
				{
					g.setColor(Color.BLACK);
					g.drawOval(offsetX, offsetY, size, size);
				}
				
				g.setColor(Color.BLACK);
				g.drawString(label, offsetX + size + uiGap, offsetY + size /2);
				
				offsetY +=  size + uiGap;
			}
			
			
			if(type.equalsIgnoreCase("button"))
			{
				int size_x = UIObject.getInt("size_x");
				int size_y = UIObject.getInt("size_y");
				
				LocationBox locationBox = new LocationBox(new Location(offsetX, offsetY), size_y, size_x);
				this.uiLocationBoxMap.put(id, locationBox);
				
				
				g.setColor(Color.BLACK);
				g.drawRect(offsetX, offsetY, size_x, size_y);
				g.drawString(label, offsetX + size_x /2 + uiGap, offsetY + size_y /2);
				
				offsetY +=  size_y + uiGap;		
			}
			
			
			if(type.equalsIgnoreCase("textbox"))
			{
				int size_x = UIObject.getInt("size_x");
				int size_y = UIObject.getInt("size_y");
				
				LocationBox locationBox = new LocationBox(new Location(offsetX, offsetY), size_y, size_x);
				this.uiLocationBoxMap.put(id, locationBox);
						
				String text = UIObject.getString("text");
				
				g.setColor(Color.BLACK);
				g.drawString(label, offsetX, offsetY);
				
				g.drawRect(offsetX, offsetY + uiGap, size_x, size_y);
				g.drawString(label, offsetX + 2, offsetY + size_y + 2);
				
				offsetY +=  size_y + uiGap;		
			}
		}
		
		return renderedUi;
	}

	public static void main(String[] args) throws IOException {
		
		String fileString = new String(Files.readAllBytes(new File("spec.json").toPath()), StandardCharsets.UTF_8);
		BufferedImage bi = new Specification(fileString).renderedUI;
		ImageIO.write(bi, "png", new File("ui.png"));
		
		System.out.println("Done...");
	}
}
