package virtualboyobjects;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class VBGraphicsEditor extends javax.swing.JFrame implements java.awt.event.MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VBRom _myRom;
	private JPanel leftPanel;
	private JScrollPane leftScroll;
	private JScrollPane rightScroll;
	private JPanel rightPanel;
	private java.awt.Polygon[] polys;
	private int cellClicked = -1;
	private int selectedChar = -1;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public VBGraphicsEditor(){
		init();	
		//loadCharacters();
		//loadChar();
		//loadBGMap();
		loadBGMapCompressed();
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
		_myRom = new VBRom("/home/greg/VirtualBoy/RealityBoy/ss.vb",
				"/home/greg/VirtualBoy/RealityBoy/ram_vip.bin");
		//_myRom = new VBRom("/home/greg/VirtualBoy/RealityBoy/ram_vip.bin");
	}
	
	private void loadBGMapCompressed(){
		_myRom.LoadCompressedData(0x417b8, 0x41c31);
		_myRom.DeCompressData();
		loadChar();
		loadBGMap();
	}
	
	private void loadBGMap(){
		rightPanel.removeAll();
		java.awt.image.BufferedImage i = _myRom.getBGMap(0, 1);
		polys = _myRom.getPolygons();
		javax.swing.JButton b = new javax.swing.JButton();	
		b.setName("BGMapButton");
		b.setBounds(b.getX(), b.getY(), i.getWidth(), i.getHeight());
		b.setContentAreaFilled(false);
		b.setIcon(new ImageIcon(i));
		b.setSize(i.getWidth(), i.getHeight());
		b.setVisible(true);
		b.setBorder(null);
		b.addMouseListener(this);
		
		rightPanel.add(b);
	}
	
	private void loadChar(){
		for(int c=0; c<2048; c++){
			BufferedImage i = _myRom.getCharacter(c, 2);
			javax.swing.JButton b = new javax.swing.JButton();	
			b.setName("CharButton");
			b.setBorder(null);
			b.setBounds(b.getX(), b.getY(), i.getWidth(), i.getHeight());
			b.setIcon(new ImageIcon(i));
			b.setSize(i.getWidth(), i.getHeight());
			b.setVisible(true);
			b.setToolTipText(Integer.toString(c));
			b.addMouseListener(this);
			
			leftPanel.add(b);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(arg0.getSource().getClass() != javax.swing.JButton.class) return;
		javax.swing.JButton b = (javax.swing.JButton)arg0.getSource();
		
		System.out.print("First Event" + cellClicked + " " + selectedChar + "\n");
		if(b.getName() == "CharButton"){
			this.selectedChar = Integer.parseInt(((javax.swing.JButton)arg0.getSource()).getToolTipText());
			cellClicked = -1;
		}
		
		if(b.getName() == "BGMapButton"){
			for(int i=0; i< this.polys.length; i++){
				if(this.polys[i].contains(arg0.getPoint())){
					cellClicked = i;
				}
			}
		}
		
		if(this.selectedChar != -1 && this.cellClicked != -1){
			System.out.print("Function: " + cellClicked + " " + selectedChar + "\n");
			_myRom.alterBGCell(0, cellClicked, selectedChar);
			this.loadBGMap();
			this.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}