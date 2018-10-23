import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
public class Test {

	public static void main(String[] args) throws IOException {


		File[] files = new File("C:\\Users\\Aritra\\Desktop\\Frames\\cap").listFiles();
		
		for(File in_f : files)
		{
			File img_ret=new File("C:\\Users\\Aritra\\Desktop\\Frames\\dec\\" + in_f.getName());

			BufferedImage img=ImageIO.read(in_f);

			int hi=img.getHeight();
			int wd=img.getWidth();
			System.out.println(in_f.getName());		

			BufferedImage img_copy = new BufferedImage(wd, hi, BufferedImage.TYPE_INT_RGB);

			for(int i = 1; i < 722; i++)
			{
				for(int j = 1; j < 432; j++)
				{

					//System.out.println(i + " " + j);
					int clr =  img.getRGB(i,j);			    
					img_copy.setRGB(i, j, clr);

					if(clr != -1)
					{
						//System.out.println(i + ":" + j + " : " + red + " " + green + " " + blue + " " + clr_alpha_el);
						img_copy.setRGB(i, j, 0x00ff0000);
					}
				}
			}

			ImageIO.write(img_copy,"jpg",img_ret);
		}
	}

}
