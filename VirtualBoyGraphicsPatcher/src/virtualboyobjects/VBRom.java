package virtualboyobjects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VBRom {
	
	private ByteBuffer _rom; //8bit characters
	private String _path;
	private boolean _bEndianFlip = true;
	
	public VBRom(String path){
		_path = path;
		LoadRomToMemory();
	}
	
	private void LoadRomToMemory(){
		java.io.File f = new java.io.File(_path);
		_rom = ByteBuffer.allocate((int)f.length());
		try {
			java.io.DataInputStream file = new java.io.DataInputStream(new java.io.FileInputStream(_path));			
			for(int b=0; b<(int)f.length(); b+=2){
				_rom.put(file.readByte());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.print(e.toString());
		}
	}
	
	public byte[] getAllCharacters(){
		//Return all the character bytes for the addresses of 0x00078000 - 0x0007FFFF
		//Switch word bytes around for little endian to big endian conversion
		byte[] allChars = new byte[0x7FF + 1];
		for(int i=0x0; i<= 0x7FF; i+=2){
			if(_bEndianFlip){
				allChars[i] = _rom.get(0x0007800+i+1);
				allChars[i+1] = _rom.get(0x0007800+i);
			}
		}
		
		return allChars;
	}
	
	public byte[] getAllBGMaps(){
		byte[] allBGMaps = new byte[0x1C000 + 1];
		for(int i=0x0; i<=0x1C000; i++){
			allBGMaps[i] = _rom.get(0x00020000+i);
		}
		return allBGMaps;
	}
}
