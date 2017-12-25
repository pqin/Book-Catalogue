package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import marc.field.FixedDatum;
import marc.field.Leader;
import marc.type.ConfigIdentifier;
import marc.type.ConfigType;
import marc.type.FixedDataParser;
import marc.type.Format;

public class FixedDataParserTest {
	private static FixedDataParser parser;
	private static Map<Character, Format> data;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file = new File("resource/fixedfielddata.xml");
		parser = new FixedDataParser();
		data = parser.read(file);
	}

	private void print(Format format, ConfigType config, boolean detailed, String tag){
		if (format == null){
			System.out.println("Format: null");
		} else {
			System.out.printf("Format: %s%n", format.getName());
		}
		if (config == null){
			System.out.println("Config: null");
		} else {
			System.out.printf("Config: %s %d%n", config.getName(), config.getLength());
			if (detailed){
				FixedDatum[] map = config.getMap();
				String label;
				int start, end, length;
				for (int i = 0; i < map.length; ++i){
					label = map[i].getLabel();
					length = map[i].getLength();
					start = map[i].getIndex();
					if (length > 1){
						end = (start + length) - 1;
						System.out.printf("%s/%02d-%02d: %s%n", tag, start, end, label);
					} else {
						System.out.printf("%s/%02d: %s%n", tag, start, label);
					}
				}
			}
		}
	}
	private void print(ConfigIdentifier identifier){
		if (identifier == null){
			System.out.println("Identifier: null%n");
		} else {
			System.out.printf("Identifier: %s/%d <%s> %c%n",
					identifier.getTag(), identifier.getIndex(),
					identifier.getFieldTag(), identifier.getCode());
		}
	}
	
	@Test
	public final void test1() {
		char type = 'a';
		Format format = data.get(type);
		ConfigType[] config = format.getConfiguration("008", type);
		
		if (config == null){
			fail("config[] == null");
		} else {
			assertEquals(7, config.length);
		}
	}
	
	@Test
	public final void test2() {
		String type = "am";
		String tag = "008";
		Format format = data.get(type.charAt(0));
		ConfigType book = format.getConfiguration(tag, type);
		
		if (book == null){
			fail("config == null");
		} else {
			int fieldLength = book.getLength();
			FixedDatum[] map = book.getMap();
			FixedDatum last = map[map.length - 1];
			int minimumLength = last.getIndex() + last.getLength();
			assertTrue(minimumLength <= fieldLength);
		}
	}
	
	@Test
	public final void test3(){
		String type = "am";
		String tag = "008";
		Format format = data.get(type.charAt(0));
		ConfigType book = format.getConfiguration(tag, type);
		
		if (book == null){
			fail("config == null");
		} else {
			assertEquals("Book", book.getName());
		}
	}
	
	@Test
	public final void test4() {
		String type = "q";
		String tag = "008";
		Format format = data.get(type.charAt(0));
		ConfigType community = format.getConfiguration(tag, type);
		
		if (community == null){
			fail("config == null");
		} else {
			int fieldLength = community.getLength();
			FixedDatum[] map = community.getMap();
			FixedDatum last = map[map.length - 1];
			int minimumLength = last.getIndex() + last.getLength();
			assertTrue(minimumLength <= fieldLength);
		}
	}
	
	@Test
	public final void test5() {
		String tag = "007";
		String type = "t";
		Format format = data.get(type.charAt(0));
		ConfigType text = format.getConfiguration(tag, 0, tag, type);
		
		if (text == null){
			fail("config == null");
		} else {
			assertTrue(text != null);
		}
	}
	
	@Test
	public final void test6() {
		String tag = "007";
		Format format = data.get('q');
		ConfigType community = format.getConfiguration(tag, 0, tag, "e");
		
		if (community == null){
			fail("config == null");
		} else {
			assertTrue(community != null);
		}
	}
	
	@Test
	public final void test7() {
		String tag = "008";
		final String type = "am";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 14;
		if (actual.length != expectedLength){
			print(format, null, false, tag);
			for (int i = 0; i < actual.length; ++i){
				print(actual[i]);
			}
			System.out.println();
		}
		ConfigIdentifier expected = new ConfigIdentifier(Leader.TAG, Leader.TYPE, type.charAt(0), tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}
	
	@Test
	public final void test8() {
		String tag = "008";
		final String type = "z";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 1;
		ConfigIdentifier expected = new ConfigIdentifier(Leader.TAG, Leader.TYPE, type.charAt(0), tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}
	
	@Test
	public final void test9() {
		String tag = "008";
		final String type = "u";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 4;
		if (actual.length != expectedLength){
			print(format, null, false, tag);
			for (int i = 0; i < actual.length; ++i){
				print(actual[i]);
			}
			System.out.println();
		}
		ConfigIdentifier expected = new ConfigIdentifier(Leader.TAG, Leader.TYPE, type.charAt(0), tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}
	
	@Test
	public final void test10() {
		String tag = "008";
		final String type = "w";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 1;
		ConfigIdentifier expected = new ConfigIdentifier(Leader.TAG, Leader.TYPE, type.charAt(0), tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}
	
	@Test
	public final void test11() {
		String tag = "008";
		final String type = "q";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 1;
		ConfigIdentifier expected = new ConfigIdentifier(Leader.TAG, Leader.TYPE, type.charAt(0), tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}
	
	@Test
	public final void test12() {
		String tag = "007";
		final String type = "a";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 3;
		if (actual.length == expectedLength){
			print(format, null, false, tag);
			for (int i = 0; i < actual.length; ++i){
				print(actual[i]);
			}
			System.out.println();
		}
		ConfigIdentifier expected = new ConfigIdentifier(tag, 0, 't', tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}
	@Test
	public final void test13() {
		String tag = "007";
		final String type = "q";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 1;
		if (actual.length != expectedLength){
			print(format, null, false, tag);
			for (int i = 0; i < actual.length; ++i){
				print(actual[i]);
			}
			System.out.println();
		}
		ConfigIdentifier expected = new ConfigIdentifier(tag, 0, 'e', tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}
}
