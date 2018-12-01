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

	private void print(String tag, String type, Format format, ConfigIdentifier[] actual){
		print(format, null, false, tag);
		ConfigType config = null;
		for (int i = 0; i < actual.length; ++i){
			print(actual[i]);
			config = format.getConfiguration(tag, actual[i].getIndex(), tag, type);
			System.out.println(config.getName());
		}
		System.out.println();
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
	public final void biblioConfigCountTest() {
		String type = "a";
		String tag = "008";
		Format format = data.get(type.charAt(0));
		ConfigType[] config = format.getConfiguration(Leader.TAG, Leader.TYPE, tag, type.charAt(0));
		
		if (config == null){
			fail(String.format("Failed to find config: %s/%d=%s <%s>", Leader.TAG, Leader.TYPE, type, tag));
		} else {
			assertEquals(7, config.length);
		}
	}
	
	@Test
	public final void bookConfigTest(){
		String type = "am";
		String tag = "008";
		Format format = data.get(type.charAt(0));
		ConfigType book = format.getConfiguration(Leader.TAG, Leader.TYPE, tag, type);
		
		if (book == null){
			fail(String.format("Failed to find config: %s/%d=%s <%s>", Leader.TAG, Leader.TYPE, type, tag));
		} else {
			assertEquals("Book", book.getName());
		}
	}

	@Test
	public final void bookConfigDatumCountTest() {
		String type = "am";
		String tag = "008";
		Format format = data.get(type.charAt(0));
		ConfigType book = format.getConfiguration(Leader.TAG, Leader.TYPE, tag, type);
		
		if (book == null){
			fail(String.format("Failed to find config: %s/%d=%s <%s>", Leader.TAG, Leader.TYPE, type, tag));
		} else {
			int fieldLength = book.getLength();
			FixedDatum[] map = book.getMap();
			FixedDatum last = map[map.length - 1];
			int minimumLength = last.getIndex() + last.getLength();
			assertTrue(minimumLength <= fieldLength);
		}
	}
	
	@Test
	public final void biblioIdentifierLookupTest() {
		String tag = "008";
		final String type = "am";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 14;
		ConfigIdentifier expected = new ConfigIdentifier(Leader.TAG, Leader.TYPE, type.charAt(0), tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}

	@Test
	public final void authorityIdentifierLookupTest() {
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
	public final void holdingsIdentifierLookupTest() {
		String tag = "008";
		final String type = "u";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 4;
		ConfigIdentifier expected = new ConfigIdentifier(Leader.TAG, Leader.TYPE, type.charAt(0), tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}

	@Test
	public final void classIdentifierLookupTest() {
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
	public final void communityConfigLengthTest() {
		String type = "q";
		String tag = "008";
		Format format = data.get(type.charAt(0));
		ConfigType community = format.getConfiguration(Leader.TAG, Leader.TYPE, tag, type);
		
		if (community == null){
			fail(String.format("Failed to find config: %s/%d=%s <%s>", Leader.TAG, Leader.TYPE, type, tag));
		} else {
			int fieldLength = community.getLength();
			FixedDatum[] map = community.getMap();
			FixedDatum last = map[map.length - 1];
			int minimumLength = last.getIndex() + last.getLength();
			assertTrue(minimumLength <= fieldLength);
		}
	}
	
	@Test
	public final void communityIdentifierLookupTest() {
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
	public final void biblioMaterialCountTest() {
		String tag = "007";
		final String type = "a";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 15;
		ConfigIdentifier expected = new ConfigIdentifier(tag, 0, 't', tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[12]);
	}

	@Test
	public final void communityMaterialCountTest() {
		String tag = "007";
		final String type = "q";
		Format format = data.get(type.charAt(0));
		ConfigIdentifier[] actual = format.getIdentifier(tag);
		final int expectedLength = 1;
		ConfigIdentifier expected = new ConfigIdentifier(tag, 0, 'e', tag);
		assertEquals(expectedLength, actual.length);
		assertEquals(expected, actual[0]);
	}

	@Test
	public final void textMaterialTest() {
		String tag = "007";
		String type = "t";
		Format format = data.get('a');
		ConfigType text = format.getConfiguration(tag, 0, tag, type);
		
		if (text == null){
			fail(String.format("Failed to find config: %s/%d=%s <%s>", tag, 0, type, tag));
		} else {
			assertTrue(text != null);
		}
	}

	@Test
	public final void communityMaterialTest() {
		String tag = "007";
		Format format = data.get('q');
		ConfigType community = format.getConfiguration(tag, 0, tag, "e");
		
		if (community == null){
			fail(String.format("Failed to find config: %s/%d=%s <%s>", tag, 0, 'e', tag));
		} else {
			assertTrue(community != null);
		}
	}
}
