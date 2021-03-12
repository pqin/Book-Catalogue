package test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import marc.marc8.Marc8;

public class Marc8DecoderTest {
	private static Charset charset;
	private static CharsetDecoder decoder;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		charset = new Marc8();
		decoder = charset.newDecoder();
	}
	
	private static final String printBytes(byte[] b){
		StringBuilder buf = new StringBuilder();
		buf.append('[');
		if (b == null){
			buf.append("NULL");
		} else if (b.length > 0){
			buf.append(String.format("%2H", b[0]&0xFF));
			for (int i = 1; i < b.length; ++i){
				buf.append(String.format(",%2H", b[i]&0xFF));
			}
		}
		buf.append(']');
		return buf.toString();
	}

	private static final String testDecoder(byte[] binary) {
		String actual = null;
		ByteBuffer in = (binary == null) ? null : ByteBuffer.wrap(binary);
		CharBuffer out = null;
		try {
			out = decoder.decode(in);
			actual = out.toString();
		} catch (CharacterCodingException e) {
			Assert.fail(e.getMessage());
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
		return actual;
	}
	
	@Test
	public final void testEmpty(){
		byte[] marc8 = { };
		String expected = "";
		System.out.printf("Test: \"%s\"%n", expected);
		String actual0 = testDecoder(marc8);
		String actual1 = printBytes(marc8);
		System.out.printf("Expected: %s%n", expected);
		System.out.printf("Decoded:  %s%n", actual0);
		System.out.printf("Bytes: %s%n", actual1);
		Assert.assertEquals(expected, actual0);
	}
	
	@Test
	public final void testBlank(){
		byte[] marc8 = { 0x20 };
		String expected = " ";
		System.out.printf("Test: \"%s\"%n", expected);
		String actual0 = testDecoder(marc8);
		String actual1 = printBytes(marc8);
		System.out.printf("Expected: %s%n", expected);
		System.out.printf("Decoded:  %s%n", actual0);
		System.out.printf("Bytes: %s%n", actual1);
		Assert.assertEquals(expected, actual0);
	}
	
	@Test
	public final void testControl0(){
		byte[] c0 = new byte[0x21];
		char[] tmp = new char[c0.length];
		for (int i = 0; i < c0.length; ++i){
			c0[i] = (byte) i;
			tmp[i] = (char) i;
		}
		char[] output = new char[tmp.length];
		for (int i = 0; i < output.length; ++i){
			output[i] = Marc8.decodeControl(i, 0);
		}
		String expected = new String(tmp);
		String actual = new String(output);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public final void testControl1(){
		byte[] c1 = new byte[0x21];
		char[] tmp = new char[c1.length];
		for (int i = 0; i < c1.length; ++i){
			c1[i] = (byte) (i | 0x80);
			tmp[i] = (char) (i | 0x80);
		}
		tmp[0x08] = '\u0098';
		tmp[0x09] = '\u009C';
		tmp[0x0D] = '\u200D';
		tmp[0x0E] = '\u200C';
		char[] output = new char[tmp.length];
		for (int i = 0; i < output.length; ++i){
			output[i] = Marc8.decodeControl(i, 1);
		}
		String expected = new String(tmp);
		String actual = new String(output);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public final void test1(){
		byte[] marc8 = { 0x4c, 0x61, 0x74, 0x69, 0x6e };
		String expected = "Latin";
		System.out.printf("Test: \"%s\"%n", expected);
		String actual0 = testDecoder(marc8);
		String actual1 = printBytes(marc8);
		System.out.printf("Expected: %s%n", expected);
		System.out.printf("Decoded:  %s%n", actual0);
		System.out.printf("Bytes: %s%n", actual1);
		Assert.assertEquals(expected, actual0);
	}
	
	@Test
	public final void test2(){
		byte[] marc8 = { 0x41, 0x63, 0x63, (byte)0xE2, 0x65, 0x6e, 0x74 };
		String expected = "Acce\u0301nt";
		System.out.printf("Test: \"%s\"%n", expected);
		String actual0 = testDecoder(marc8);
		String actual1 = printBytes(marc8);
		System.out.printf("Expected: %s%n", expected);
		System.out.printf("Decoded:  %s%n", actual0);
		System.out.printf("Bytes: %s%n", actual1);
		Assert.assertEquals(expected, actual0);
	}
	
	@Test
	public final void test3(){
		byte[] marc8 = { 0x1B, 0x28, 0x4E, 0x72, 0x55, 0x53, 0x53, 0x4B, 0x49, 0x49 };
		String expected = new String(new char[]{'\u0420', '\u0443', '\u0441', '\u0441', '\u043A', '\u0438', '\u0438'});
		System.out.printf("Test: \"%s\"%n", expected);
		String actual0 = testDecoder(marc8);
		String actual1 = printBytes(marc8);
		System.out.printf("Expected: %s%n", expected);
		System.out.printf("Decoded:  %s%n", actual0);
		System.out.printf("Bytes: %s%n", actual1);
		Assert.assertEquals(expected, actual0);
	}
	
	@Test
	public final void test4(){
		byte[] marc8 = { 0x1B, 0x24, 0x31, 0x21, 0x30, 0x34, 0x21, 0x42, 0x58 };
		String expected = new String(new char[]{'\u4E2D', '\u6587'});
		System.out.printf("Test: \"%s\"%n", expected);
		String actual0 = testDecoder(marc8);
		String actual1 = printBytes(marc8);
		System.out.printf("Expected: %s%n", expected);
		System.out.printf("Decoded:  %s%n", actual0);
		System.out.printf("Bytes: %s%n", actual1);
		Assert.assertEquals(expected, actual0);
	}
}
