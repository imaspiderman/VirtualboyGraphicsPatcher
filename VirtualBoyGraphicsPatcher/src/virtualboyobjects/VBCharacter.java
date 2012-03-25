package virtualboyobjects;

public class VBCharacter extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] _character = new byte[2048];//Should contain 2048 bytes
	
	private int[] _cell = new int[64*128];//Represents all cells of bits
	private int _cellStartX = 30;
	private int _cellStartY = 30;
	
	public VBCharacter(byte[] charIn) throws Exception{
		this.setVisible(true);
		this.setSize(640, 480);
		if(charIn.length != 2048){throw new Exception("Character is not 16*2048 Bytes!");}
		this._character = charIn;
		processCharacter();
	}
	
	private void processCharacter(){
		//Lets try 8-bit little Endian first
		//1  2  3  4  5  6  7  8  <--cells
		//01 01 01 01 11 11 11 11 --2 bits per cell
		int j=0;
		for(int i=0; i<64; i++)
		{			
			//First cell
			_cell[i] = 0;
			if((_character[j] & 0x40) != 0 ){
				_cell[i] = 1;
			}
			if((_character[j] & 0x80) != 0){
				_cell[i] = 2;
			}
			if((_character[j] & 0xC0) == 0xC0){
				_cell[i] = 3;
			}
			//Second cell
			_cell[++i] = 0;
			if((_character[j] & 0x10) != 0 ){
				_cell[i] = 1;
			}
			if((_character[j] & 0x20) != 0){
				_cell[i] = 2;
			}
			if((_character[j] & 0x30) == 0x30){
				_cell[i] = 3;
			}
			
			//Third cell
			_cell[++i] = 0;
			if((_character[j] & 0x04) != 0 ){
				_cell[i] = 1;
			}
			if((_character[j] & 0x08) != 0){
				_cell[i] = 2;
			}
			if((_character[j] & 0x0C) == 0x0C){
				_cell[i] = 3;
			}
			
			//Fourth cell
			_cell[++i] = 0;
			if((_character[j] & 0x01) != 0 ){
				_cell[i] = 1;
			}
			if((_character[j] & 0x02) != 0){
				_cell[i] = 2;
			}
			if((_character[j] & 0x03) == 0x03){
				_cell[i] = 3;
			}
			
			j++;
			i++;
		}
	}
}
