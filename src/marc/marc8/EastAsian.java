package marc.marc8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EastAsian extends CharacterSet {
	public EastAsian(){
		super((byte) 0x31, 3);
	}
	
	@Override
	public final void build(){
		if (table == null){
			allocateTable();
			loadFile("resource/eacc2uni.txt");
		}
	}
	private void loadFile(String filename){
		File file = new File(filename);
		BufferedReader in = null;
		String line = null;
		String[] token = null;
		String b;
		int[] tmp = new int[bytesPerChar + 1];
		char[] c;
		int index;
		final int radix = 16;	// File is in hexadecimal
        try {
        	in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			while ((line = in.readLine()) != null){
				token = line.split(String.valueOf(','), 3);
				if (token.length > 2 && token[0].length() == (2*bytesPerChar)){
					// get value of bytes in first token
					for (int i = 0; i < bytesPerChar; ++i){
						b = token[0].substring(2*i, 2*(i+1));
						index = Integer.parseUnsignedInt(b, radix);
						tmp[i] = ((index & 0x7F) - START_INDEX)*base[i];
					}
					// calculate index
					index = 0;
					for (int i = 0; i < bytesPerChar; ++i){
						index += tmp[i];
					}
					// get value of second token
					tmp[bytesPerChar] = Integer.parseUnsignedInt(token[1], radix);
					// get Unicode character
					c = Character.toChars(tmp[bytesPerChar]);
					// add entry to table
					if (index >= 0 && index < table.length){
						table[index] = c[0];
					}
				}
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
	
	@Override
	protected final char[] buildTable(){
		return null;
	}
}
