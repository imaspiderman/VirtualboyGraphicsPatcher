package virtualboyobjects;

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
		//Return all the character bytes for the addresses of
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
		byte[] allBGMaps = new byte[0x1C000 + 1];
		for(int i=0x0; i<=0x1C000; i++){
			allBGMaps[i] = _rom.get(0x00020000+i);
		}
		return allBGMaps;
	}
}
