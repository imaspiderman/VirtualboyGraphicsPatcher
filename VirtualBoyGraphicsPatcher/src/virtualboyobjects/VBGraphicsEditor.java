package virtualboyobjects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class VBGraphicsEditor extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VBRom _myRom;
	private BufferedImage[] allImages = new BufferedImage[128];

	/**
	 * @param args
	 * @throws IOException 
	 */
	public VBGraphicsEditor(){
		this.setVisible(true);
		this.setSize(800, 600);
		init();
	}
	
	public static void main(String[] args) throws IOException {
		VBGraphicsEditor editor = new VBGraphicsEditor();
	}
	
	private void init(){		
		_myRom = new VBRom("/home/greg/VirtualBoy/VBProgrammingDemo/ss.vb");
		loadCharacters();
	}
	
	private void loadCharacters(){
		//Get all the bytes for characters
		byte[] bytes = _myRom.getAllCharacters();
		
		int startx = 30;
		int starty = 30;
		for(int image=0; image<128; image++){
			int x=0;
			int y=0;
			allImages[image] = new BufferedImage(8,8,BufferedImage.TYPE_INT_RGB);
			for(int b=(16*image); b<(16*image+16); b++){
				if(b>(16*image) && b%2==0) {
					y++;
					x=0;
				}
				int cell1 = (bytes[b]>>6); 
				int cell2 = (bytes[b]>>4 & 0x03);
				int cell3 = (bytes[b]>>2 & 0x03);
				int cell4 = (bytes[b] & 0x03);
				
				allImages[image].setRGB(x, y, setColor(cell1).getRGB());
				allImages[image].setRGB(++x, y, setColor(cell2).getRGB());
				allImages[image].setRGB(++x, y, setColor(cell3).getRGB());
				allImages[image].setRGB(++x, y, setColor(cell4).getRGB());	
	
				x++;
			}
			this.getGraphics().drawImage(allImages[image], startx, starty, null);
			startx += allImages[image].getWidth() + 2;
			if (image > 1 && image % 20 == 0){
				starty += allImages[image].getHeight() + 2;
				startx = 30;
			}
		}		
	}
	
	private Color setColor(int i){
		Color c = Color.BLACK;
		if(i == 3) c = new Color(255,0,0);
		if(i == 2) c = new Color(150,0,0);
		if(i == 1) c = new Color(75,0,0);
		
		return c;
	}

}
