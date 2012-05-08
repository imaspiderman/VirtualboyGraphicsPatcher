package virtualboyobjects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class VBGraphicsEditor extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VBRom _myRom;
	private BufferedImage allChars;
	private JPanel leftPanel;
	private JScrollPane leftScroll;
	private JScrollPane rightScroll;
	private JPanel rightPanel;

	/**
import java.nio.ByteOrder;
	 * @param args
	 * @throws IOException 
	 */
	public VBGraphicsEditor(){
		init();	
		//loadCharacters();
		loadCharacters();
		this.add(leftScroll, BorderLayout.WEST);
		this.add(rightScroll, BorderLayout.EAST);
		this.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException {
		VBGraphicsEditor editor = new VBGraphicsEditor();
		editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void init(){
		this.setLayout(new BorderLayout());
		this.setBounds(this.getX(), this.getY(), 800, 600);
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftScroll = new JScrollPane(leftPanel);
		leftScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightScroll = new JScrollPane(rightPanel);
		rightScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		_myRom = new VBRom("/home/greg/VirtualBoy/RealityBoy/ram_vip.bin");
		//_myRom = new VBRom("/home/greg/VirtualBoy/VBProgrammingDemo/ss.vb");		
	}
	
	private void loadCharacters(){
		//Get all the bytes for characters
		byte[] bytes = _myRom.getAllCharacters();
		int startx = 0;
		int starty = 0;
		int x=0;
		int y=0;
		int imageWidth=360;
		int imageHeight=500;
		
		allChars = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB);
		
		//Loop through all the bytes
		//16 bytes will be a full character of 64 pixels 8x8
		//We'll show 40 characters across with 1 pixel in between them
		//We'll put 1 pixel in between the characters vertically as well
		//There are 2048 characters * 64 pixels = 131072 total pixels
		int cell1;
		int cell2;
		int cell3;
		int cell4;
		for(int b=0; b<bytes.length; b++){
			if(b>0 && b%2==0){ //every 2 bytes we need to move down a row
				allChars.setRGB(++x, y, (new Color(50,50,50)).getRGB());//border
				y++;
				x=startx;
			}
			if(b>0 && b%16==0){//every 16 bytes we need to reset y and move start x
				y = starty;
				startx += 9; //8 pixels = 1 for space;
				x=startx;
			}
			if(b>0 && b%640==0){//every 640 bytes (40characters * 16bytes/character) we'll increment starty
				starty += 9;
				y=starty;
				startx = 0;
				x=startx;
			}
			
			cell4 = (bytes[b]<0)?((char)bytes[b] & 0xFF >> 6):((char)bytes[b]>>6); 
			cell3 = ((char)bytes[b]>>4 & 0x03);
			cell2 = ((char)bytes[b]>>2 & 0x03);
			cell1 = ((char)bytes[b] & 0x03);
			
			allChars.setRGB(x, y, setColor(cell1).getRGB());
			allChars.setRGB(++x, y, setColor(cell2).getRGB());
			allChars.setRGB(++x, y, setColor(cell3).getRGB());
			allChars.setRGB(++x, y, setColor(cell4).getRGB());
		}
		
		javax.swing.JButton b = new javax.swing.JButton();			
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setBounds(b.getX(), b.getY(), imageWidth, imageHeight);
		b.setIcon(new ImageIcon(allChars));
		b.setSize(imageWidth, imageHeight);
		b.setVisible(true);
		
		leftPanel.add(b);
	}
	
	private Color setColor(int i){
		Color c = Color.BLACK;
		if(i == 3) c = new Color(200,0,0);
		if(i == 2) c = new Color(150,0,0);
		if(i == 1) c = new Color(100,0,0);
		
		return c;
	}
}
