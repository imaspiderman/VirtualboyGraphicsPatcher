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
}
