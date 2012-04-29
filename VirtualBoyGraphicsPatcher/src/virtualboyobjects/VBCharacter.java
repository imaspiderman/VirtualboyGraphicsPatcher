package virtualboyobjects;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class VBCharacter {

	private int _length;
	byte[] _bytes;
	int _startAddress;
	int _endAddress;
	int _imageScale = 1;
	
	public VBCharacter(int startAddress, int endAddress, VBRom romIn) throws Exception{
		_length = endAddress - startAddress;
		_startAddress = startAddress;
		_endAddress = endAddress;
		romIn.getRomByteBuffer().get(_bytes, _startAddress, _length);
	}
	
	public ImageIcon getImage(){
		int _imagex = 0;
		int _imagey = 0;
		BufferedImage i = new BufferedImage(8*_imageScale,8*_imageScale,BufferedImage.TYPE_INT_RGB);
		for(int x=0; x<_length; x++){
			int cell1 = (_bytes[x]>>6); 
			int cell2 = (_bytes[x]>>4 & 0x03);
			int cell3 = (_bytes[x]>>2 & 0x03);
			int cell4 = (_bytes[x] & 0x03);
			
			if(x>1 && x%2==0){
				_imagex = 0;
				_imagey += _imageScale;
			}
			
			for(int loop=0; loop<_imageScale; loop++){
				for(int loopy=0; loopy<_imageScale;loopy++){
					i.setRGB(_imagex+loop, _imagey+loopy, setColor(cell1).getRGB());
				}
			}
			_imagex+=_imageScale;
			for(int loop=0; loop<_imageScale; loop++){
				for(int loopy=0; loopy<_imageScale;loopy++){
					i.setRGB(_imagex+loop, _imagey+loopy, setColor(cell2).getRGB());
				}
			}
			_imagex+=_imageScale;
			for(int loop=0; loop<_imageScale; loop++){
				for(int loopy=0; loopy<_imageScale;loopy++){
					i.setRGB(_imagex+loop, _imagey+loopy, setColor(cell3).getRGB());
				}
			}
			_imagex+=_imageScale;
			for(int loop=0; loop<_imageScale; loop++){
				for(int loopy=0; loopy<_imageScale;loopy++){
					i.setRGB(_imagex+loop, _imagey+loopy, setColor(cell4).getRGB());
				}
			}
			_imagex+=_imageScale;
		}
		
		return new ImageIcon(i);
	}
	
	private Color setColor(int i){
		Color c = Color.BLACK;
		if(i == 3) c = new Color(255,0,0);
		if(i == 2) c = new Color(150,0,0);
		if(i == 1) c = new Color(75,0,0);
		
		return c;
	}
	
}
