package test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import marc.marc8.Marc8;

public class Marc8EncoderTest {
	private static Charset charset;
	private static CharsetEncoder encoder;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		charset = new Marc8();
		encoder = charset.newEncoder();
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
	
	private static final byte[] testEncoder(String text){
		byte[] actual = null;
		CharBuffer in = (text == null) ? null : CharBuffer.wrap(text);
		ByteBuffer out = null;
		try {
			out = encoder.encode(in);
			byte[] a = out.array();
			actual = Arrays.copyOf(a, out.remaining());
		} catch (CharacterCodingException e){
			Assert.fail(e.getMessage());
		} catch (Exception e){
			Assert.fail(e.getMessage());
		} finally {
			System.out.printf("encode:   %s%n", printBytes(actual));
		}
		return actual;
	}
	private static final byte[] testString(String text){
		byte[] actual = null;
		try {
			actual = text.getBytes(charset);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		} finally {
			System.out.printf("getBytes: %s%n", printBytes(actual));
		}
		return actual;
	}
	
	@Test
	public final void testEmpty(){
		String text = "";
		byte[] expected = {};
		System.out.printf("Test: \"%s\"%n", text);
		System.out.printf("Expected: %s%n", printBytes(expected));
		byte[] actual0 = testEncoder(text);
		byte[] actual1 = testString(text);
		Assert.assertArrayEquals(expected, actual0);
		Assert.assertArrayEquals(expected, actual1);
	}
	
	@Test
	public final void testBlank(){
		String text = " ";
		byte[] expected = { 0x20 };
		System.out.printf("Test: \"%s\"%n", text);
		System.out.printf("Expected: %s%n", printBytes(expected));
		byte[] actual0 = testEncoder(text);
		byte[] actual1 = testString(text);
		Assert.assertArrayEquals(expected, actual0);
		Assert.assertArrayEquals(expected, actual1);
	}
	
	@Test
	public final void testControl0(){
		char[] data = new char[0x20];
		byte[] expected = new byte[data.length];
		for (int i = 0; i < data.length; ++i){
			data[i] = (char) i;
			expected[i] = (byte) i;
		}
		String text = new String(data);
		
		System.out.printf("Test: \"%s\"%n", text);
		System.out.printf("Expected: %s%n", printBytes(expected));
		byte[] actual0 = testEncoder(text);
		byte[] actual1 = testString(text);
		Assert.assertArrayEquals(expected, actual0);
		Assert.assertArrayEquals(expected, actual1);
	}
	
	@Test
	public final void testControl1(){
		char[] data = {
				'\u0080',
				'\u0088',
				'\u0089',
				'\u008D',
				'\u008E',
				'\u0090',
				'\u0098',
				'\u009C',
				'\u200D',
				'\u200C',
				'\u00A0'
		};
		byte[] expected = {
				(byte) 0x80,
				(byte) 0x88,
				(byte) 0x89,
				(byte) 0x8D,
				(byte) 0x8E,
				(byte) 0x90,
				(byte) 0x88,
				(byte) 0x89,
				(byte) 0x8D,
				(byte) 0x8E,
				(byte) 0xA0
		};
		String text = new String(data);
		
		System.out.printf("Test: \"%s\"%n", text);
		System.out.printf("Expected: %s%n", printBytes(expected));
		byte[] actual0 = testEncoder(text);
		byte[] actual1 = testString(text);
		Assert.assertArrayEquals(expected, actual0);
		Assert.assertArrayEquals(expected, actual1);
	}
	
	@Test
	public final void test1(){
		String text = "Latin";
		byte[] expected = { 0x4c, 0x61, 0x74, 0x69, 0x6e };
		System.out.printf("Test: \"%s\"%n", text);
		System.out.printf("Expected: %s%n", printBytes(expected));
		byte[] actual0 = testEncoder(text);
		byte[] actual1 = testString(text);
		Assert.assertArrayEquals(expected, actual0);
		Assert.assertArrayEquals(expected, actual1);
	}
	
	@Test
	public final void test2(){
		String text = "Acce\u0301nt";
		byte[] expected = { 0x41, 0x63, 0x63, (byte)0xE2, 0x65, 0x6e, 0x74 };
		System.out.printf("Test: \"%s\"%n", text);
		System.out.printf("Expected: %s%n", printBytes(expected));
		byte[] actual0 = testEncoder(text);
		byte[] actual1 = testString(text);
		Assert.assertArrayEquals(expected, actual0);
		Assert.assertArrayEquals(expected, actual1);
	}
	
	@Test
	public final void test3(){
		String text = new String(new char[]{'\u0420', '\u0443', '\u0441', '\u0441', '\u043A', '\u0438', '\u0438'});
		byte[] expected = { 0x1B, 0x28, 0x4E, 0x72, 0x55, 0x53, 0x53, 0x4B, 0x49, 0x49 };
		System.out.printf("Test: \"%s\"%n", text);
		System.out.printf("Expected: %s%n", printBytes(expected));
		byte[] actual0 = testEncoder(text);
		byte[] actual1 = testString(text);
		Assert.assertArrayEquals(expected, actual0);
		Assert.assertArrayEquals(expected, actual1);
	}
	
	@Test
	public final void test4(){
		String text = new String(new char[]{'\u4E2D', '\u6587'});
		byte[] expected = { 0x1B, 0x24, 0x31, 0x21, 0x30, 0x34, 0x21, 0x42, 0x58 };
		System.out.printf("Test: \"%s\"%n", text);
		System.out.printf("Expected: %s%n", printBytes(expected));
		byte[] actual0 = testEncoder(text);
		byte[] actual1 = testString(text);
		Assert.assertArrayEquals(expected, actual0);
		Assert.assertArrayEquals(expected, actual1);
	}
}
