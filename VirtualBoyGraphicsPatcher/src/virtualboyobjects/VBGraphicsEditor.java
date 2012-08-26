package virtualboyobjects;

import java.awt.BorderLayout;
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
	private JPanel leftPanel;
	private JScrollPane leftScroll;
	private JScrollPane rightScroll;
	private JPanel rightPanel;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public VBGraphicsEditor(){
		init();	
		//loadCharacters();
		loadChar();
		loadBGMap();
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
	
	private void loadBGMap(){
		BufferedImage i = _myRom.getBGMap(0, 1);
		javax.swing.JButton b = new javax.swing.JButton();			
		b.setBorderPainted(false);
		b.setBounds(b.getX(), b.getY(), i.getWidth(), i.getHeight());
		b.setContentAreaFilled(false);
		b.setIcon(new ImageIcon(i));
		b.setSize(i.getWidth(), i.getHeight());
		b.setVisible(true);
		
		rightPanel.add(b);
	}
	
	private void loadChar(){
		for(int c=0; c<2048; c++){
			BufferedImage i = _myRom.getCharacter(c, 2);
			javax.swing.JButton b = new javax.swing.JButton();			
			b.setBorderPainted(false);
			b.setBounds(b.getX(), b.getY(), i.getWidth(), i.getHeight());
			b.setContentAreaFilled(false);
			b.setIcon(new ImageIcon(i));
			b.setSize(i.getWidth(), i.getHeight());
			b.setVisible(true);
			b.setToolTipText(Integer.toString(c));
			
			leftPanel.add(b);
		}
	}
}
