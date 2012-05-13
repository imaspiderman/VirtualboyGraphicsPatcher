package virtualboyobjects;

import java.awt.Color;
import java.nio.ByteBuffer;

public class VBRom {
	
	private ByteBuffer _rom; //8bit characters
	private String _path;
	private boolean _bEndianFlip = false;
	
	public VBRom(String path){
		_path = path;
		LoadRomToMemory();
	}
	
	private void LoadRomToMemory(){
		java.io.File f = new java.io.File(_path);
		_rom = ByteBuffer.allocate((int)f.length());
		_rom.order(java.nio.ByteOrder.LITTLE_ENDIAN);
		try {
			java.io.DataInputStream file = new java.io.DataInputStream(new java.io.FileInputStream(_path));
			for(int b=0; b<(int)f.length(); b++){
				_rom.put(file.readByte());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print(e.toString());
		}
	}
	
	public ByteBuffer getRomByteBuffer(){
		return _rom;
	}
	
	public byte[] getAllCharacters(){
		//Return all the character bytes for the addresses of
		//Rom starts ad 0700000
		//6000 - 7FFF = 0-511
		//E000 - FFFF = 512-1023
		//16000 - 17FFF = 1024 - 1535
		//1E000 = 1FFFF = 1536 - 2047
		//Switch word bytes around for little endian to big endian conversion
		byte[] allChars = new byte[(0x7FFF-0x6000)+(0xFFFF-0xE000)+(0x17FFF-0x16000)+(0x1FFFF-0x1E000) + 4];
		
		int iLength = (0x7FFF-0x6000);
		int i=0;
		//Segment 1
		for(; i<= iLength; i+=2){
			if(_bEndianFlip){
				allChars[i] = _rom.get(0x6000+i+1);
				allChars[i+1] = _rom.get(0x6000+i);
			}else {
				allChars[i] = _rom.get(0x6000+i);
				allChars[i+1] = _rom.get(0x6000+i+1);
			}
		}
		//Segment 2
		iLength += (0xFFFF-0xE000);
		for(; i<= iLength; i+=2){
			if(_bEndianFlip){
				allChars[i] = _rom.get(0xE000+i+1);
				allChars[i+1] = _rom.get(0xE000+i);
			}else {
				allChars[i] = _rom.get(0xE000+i);
				allChars[i+1] = _rom.get(0xE000+i+1);
			}
		}
		//Segment 3
		iLength += (0x17FFF-0x16000);
		for(; i<= iLength; i+=2){
			if(_bEndianFlip){
				allChars[i] = _rom.get(0x16000+i+1);
				allChars[i+1] = _rom.get(0x16000+i);
			}else {
				allChars[i] = _rom.get(0x16000+i);
				allChars[i+1] = _rom.get(0x16000+i+1);
			}
		}
		//Segment 4
		iLength += (0x1FFFF-0x1E000);
		for(; i<= iLength; i+=2){
			if(_bEndianFlip){
				allChars[i] = _rom.get(0x1E000+i+1);
				allChars[i+1] = _rom.get(0x1E000+i);
			}else {
				allChars[i] = _rom.get(0x1E000+i);
				allChars[i+1] = _rom.get(0x1E000+i+1);
			}
		}
		
		return allChars;
	}
	
	public byte[] getAllBGMaps(){
		//0x0002 0000 - 0x0003 C000 memory area for BGMaps
		
		byte[] allBGMaps = new byte[0x3C000-0x20000 + 1];
		int iLength = (0x3C000-0x20000);
		for(int i=0x0; i<=iLength; i+=2){
			if(_bEndianFlip){
				allBGMaps[i] = _rom.get(0x020000+i+1);
				allBGMaps[i+1] = _rom.get(0x20000+i);
			}else {
				allBGMaps[i] = _rom.get(0x20000+i);
				allBGMaps[i+1] = _rom.get(0x20000+i+1);
			}
		}
		return allBGMaps;
	}
	
	public java.awt.image.BufferedImage getCharacterAt(int offset,int scale){
		int segment = (int)Math.floor((offset+1)/512);
		int imageWidth = 8*scale;
		int imageHeight = 8*scale;
		int iCharSize = 16; //16 bytes per char
		int segment1 = 0x6000;//Starting address for seg 1
		int segment2 = 0xE000;//Starting address for seg 2
		int segment3 = 0x16000;//Starting address for seg 3
		int segment4 = 0x1E000;//Starting address for seg 4
		int currsegment = 0;
		
		switch(segment){
			case 1: currsegment = segment1; break;
			case 2: currsegment = segment2; break;
			case 3: currsegment = segment3; break;
			case 4: currsegment = segment4; break;
			default: currsegment = segment1; break;
		}
		
		java.awt.image.BufferedImage i  = 
				new java.awt.image.BufferedImage(
						imageWidth, 
						imageHeight, 
						java.awt.image.BufferedImage.TYPE_INT_RGB
						);
		
		_rom.position(currsegment + (offset*iCharSize));//Set the position
		byte[] pixels = new byte[16];//Set a byte array
		_rom.get(pixels);//Fill the array
		
		//Make the image
		int cell1;
		int cell2;
		int cell3;
		int cell4;
		int x=0;
		int y=0;
		
		for(int b=0; b<pixels.length; b++){
			if(b>0 && b%2==0){ //every 2 bytes we need to move down a row
				y+=scale;
				x=0;
			}
			
			cell4 = (pixels[b]<0)?((char)pixels[b] & 0xFF >> 6):((char)pixels[b]>>6); 
			cell3 = ((char)pixels[b]>>4 & 0x03);
			cell2 = ((char)pixels[b]>>2 & 0x03);
			cell1 = ((char)pixels[b] & 0x03);
	
			for(int xs=0; xs<scale; xs++){
				for(int ys=0; ys<scale; ys++){
					i.setRGB(x+xs, y+ys, setColor(cell1).getRGB());
				}
			}
			x+=scale;
			for(int xs=0; xs<scale; xs++){
				for(int ys=0; ys<scale; ys++){
					i.setRGB(x+xs, y+ys, setColor(cell2).getRGB());
				}
			}
			x+=scale;
			for(int xs=0; xs<scale; xs++){
				for(int ys=0; ys<scale; ys++){
					i.setRGB(x+xs, y+ys, setColor(cell3).getRGB());
				}
			}
			x+=scale;
			for(int xs=0; xs<scale; xs++){
				for(int ys=0; ys<scale; ys++){
					i.setRGB(x+xs, y+ys, setColor(cell4).getRGB());
				}
			}
		}
		return i;
	}
	
	private Color setColor(int i){
		Color c = Color.BLACK;
		if(i == 3) c = new Color(125,125,125);
		if(i == 2) c = new Color(100,100,100);
		if(i == 1) c = new Color(75,75,75);
		
		return c;
	}
}
