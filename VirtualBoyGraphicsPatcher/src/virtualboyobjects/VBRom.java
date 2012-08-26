package virtualboyobjects;

import java.awt.Color;
import java.nio.ByteBuffer;

public class VBRom {
	
	private ByteBuffer _rom; //8bit characters
	private String _path;
	private byte[] allChars = new byte[32768]; //2048chars * 16px
	private byte[] allBGMaps = new byte[114688]; //14maps * 2bytes per char * 4096chars per map
	private boolean _bEndianFlip = true;
	
	public VBRom(String path){
		_path = path;
		LoadRomToMemory();
		getAllCharacters();
		getAllBGMaps();
	}
	
	private void LoadRomToMemory(){
		java.io.File f = new java.io.File(_path);
		_rom = ByteBuffer.allocate((int)f.length());
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
	
	private void getAllCharacters(){
		//Return all the character bytes for the addresses of
		//Rom starts ad 0700000
		//6000 - 7FFF = 0-511
		//E000 - FFFF = 512-1023
		//16000 - 17FFF = 1024 - 1535
		//1E000 = 1FFFF = 1536 - 2047
		//Switch word bytes around for little endian to big endian conversion				
		int iLength = 0x1FFF;
		//Segment 1
		for(int i=0; i<= iLength; i++){
			allChars[i] = _rom.get(0x6000+i);
		}
		//Segment 2
		for(int i=0; i<= iLength; i++){
			allChars[i+iLength+1] = _rom.get(0xE000+i);
		}
		//Segment 3
		for(int i=0; i<= iLength; i++){
			allChars[i+((iLength+1)*2)] = _rom.get(0x16000+i);
		}
		//Segment 4
		for(int i=0; i<= iLength; i++){
			allChars[i+((iLength+1)*3)] = _rom.get(0x1E000+i);
		}
	}
	
	private void getAllBGMaps(){
		//0x0002 0000 - 0x0003 C000 memory area for BGMaps		
		int iLength = (0x3C000-0x20000);
		for(int i=0x0; i<iLength; i++){
			allBGMaps[i] = _rom.get(0x20000+i);
		}
	}
	
	public java.awt.image.BufferedImage getBGMap(int bgMapNumber,int scale){
		//64x64 chars on a map
		int imageWidth = 512*scale;
		int imageHeight = 512*scale;
		int iBGMapLength = 8192;
		java.awt.image.BufferedImage i  = 
				new java.awt.image.BufferedImage(
						imageWidth, 
						imageHeight, 
						java.awt.image.BufferedImage.TYPE_INT_RGB
						);
		byte[] bgMap = new byte[iBGMapLength];
		for(int m=0; m<iBGMapLength; m++){
			bgMap[m] = allBGMaps[(bgMapNumber*iBGMapLength)+m];
		}
		
		int cell;
		int mainX = 0;
		int mainY = 0;
		
		try{
			for(int m=0; m<iBGMapLength; m+=2){
				short b1 = (short)(bgMap[m] & 0xFF);
				short b2 = (short)(bgMap[m+1] & 0xFF);
				cell = ((b2 << 8) + b1) & 0x7FF;
				
				java.awt.image.BufferedImage bi = getCharacter(cell,1);
				for(int x=0; x<bi.getWidth();x++){
					for(int y=0; y<bi.getHeight();y++){
						i.setRGB(mainX+x, mainY+y, bi.getRGB(x, y));
					}
				}
				mainX += bi.getWidth();
				
				if((m+2)%128 == 0){
					mainY += bi.getHeight();
					mainX = 0;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return i;
	}
	
	public java.awt.image.BufferedImage getCharacter(int charNumber,int scale){
		int imageWidth = 8*scale;
		int imageHeight = 8*scale;
		java.awt.image.BufferedImage i  = 
				new java.awt.image.BufferedImage(
						imageWidth, 
						imageHeight, 
						java.awt.image.BufferedImage.TYPE_INT_RGB
						);

		byte[] pixels = new byte[16];//Set a byte array
		int charOffset = charNumber*16;
		
		//Load the 16 byte character
		for(int x=0; x<16; x++){
			pixels[x] = allChars[charOffset+x];
		}
		
		//Make the image
		int pix1;
		int pix2;
		int pix3;
		int pix4;
		int x=0;
		int y=0;
		
		for(int b=0; b<pixels.length; b++){
			if(b>0 && b%2==0){ //every 2 bytes we need to move down a row
				y+=scale;
				x=0;
			}
			
			pix4 = (pixels[b]<0)?(pixels[b] >> 6 & 3 | 2):(pixels[b] >> 6);
			pix3 = pixels[b] >> 4 & 3;
			pix2 = pixels[b] >> 2 & 3;
			pix1 = pixels[b] & 3;
	
			for(int xs=0; xs<scale; xs++){
				for(int ys=0; ys<scale; ys++){
					i.setRGB(x+xs, y+ys, setColor(pix1).getRGB());
				}
			}
			x+=scale;
			for(int xs=0; xs<scale; xs++){
				for(int ys=0; ys<scale; ys++){
					i.setRGB(x+xs, y+ys, setColor(pix2).getRGB());
				}
			}
			x+=scale;
			for(int xs=0; xs<scale; xs++){
				for(int ys=0; ys<scale; ys++){
					i.setRGB(x+xs, y+ys, setColor(pix3).getRGB());
				}
			}
			x+=scale;
			for(int xs=0; xs<scale; xs++){
				for(int ys=0; ys<scale; ys++){
					i.setRGB(x+xs, y+ys, setColor(pix4).getRGB());
				}
			}
			x+=scale;
		}
		return i;
	}
	
	private Color setColor(int i){
		Color c = Color.BLACK;
		if(i == 3) c = new Color(200,200,200);
		if(i == 2) c = new Color(125,125,125);
		if(i == 1) c = new Color(75,75,75);
		
		return c;
	}
}
