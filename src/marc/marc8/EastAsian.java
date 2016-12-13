package marc.marc8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EastAsian extends LanguageEncoding {
	private static char[][][] map = buildMap();
	private int counter;
	private byte[] buffer;
	public EastAsian(){
		super((byte) 0x31, 3);
		counter = 0;
		buffer = new byte[bytesPerChar];
		Arrays.fill(buffer, (byte)0x00);
	}
	
	private static char[][][] buildMap(){
		int length = 256;
		char[][][] m = new char[length][length][length];
		for (int i = 0; i < length; ++i){
			for (int j = 0; j < length; ++j){
				for (int k = 0; k < length; ++k){
					m[i][j][k] = '?';
				}
			}
		}
		loadFile(m, "resource/hanzi.dat");
		loadFile(m, "resource/japanese.dat");
		return m;
	}
	private static void loadFile(char[][][] m, String filename){
		File file = new File(filename);
		BufferedReader in = null;
		String line = null;
		String[] token = null;
		final int length = 3;	// bytes per character
		int[] index = new int[length];
		int u;
		char[] c = {'\0'};
		final int radix = 16;	// File is in hexadecimal
        try {
        	in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			while ((line = in.readLine()) != null){
				token = line.split("\t");
				for (int i = 0; i < length; ++i){
					index[i] = Integer.parseUnsignedInt(token[i], radix);
				}
				u = Integer.parseUnsignedInt(token[length], radix);
				c = Character.toChars(u);
				m[index[0]][index[1]][index[2]] = c[0];
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (NumberFormatException nfe){
			nfe.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ioe2){
				ioe2.printStackTrace();
			}
		}
	}
	
	protected final char[] buildTable(){
		char replacement = '?';
		char[] t = buildBlankTable();
		for (int i = 0x20; i < 0x7F; ++i){
			if (t[i] == UNKNOWN_CHAR){
				t[i] = replacement;
				t[i+0x80] = replacement;
			}
		}
		return t;
	}
	
	public char decode(int b){
		char c = '\0';
		buffer[counter] = (byte)b;
		if (counter == bytesPerChar - 1){
			c = map[buffer[0]][buffer[1]][buffer[2]];
		}
		counter = (counter + 1) % bytesPerChar;
		return c;
	}
}
