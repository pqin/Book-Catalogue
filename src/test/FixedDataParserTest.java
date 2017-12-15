package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import marc.field.FixedDatum;
import marc.type.ConfigType;
import marc.type.FixedDataParser;
import marc.type.Format;

public class FixedDataParserTest {
	private static FixedDataParser parser;
	private static Map<Character, Format> data;
	private static String RECORD_TYPE;
	private static String TAG;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file = new File("resource/fixedfielddata.xml");
		parser = new FixedDataParser();
		data = parser.read(file);
		RECORD_TYPE = "am";
		TAG = "008";
	}

	@Test
	public final void test1() {
		Format format = data.get(RECORD_TYPE.charAt(0));
		ConfigType[] config = format.getConfiguration(TAG, 6, RECORD_TYPE.charAt(0));
		
		System.out.printf("Format: %s%n", format.getName());
		if (config == null){
			fail("config[] == null");
		} else {
			assertEquals(7, config.length);
		}
	}
	
	@Test
	public final void test2() {
		Format format = data.get(RECORD_TYPE.charAt(0));
		ConfigType book = format.getConfiguration(TAG, 6, RECORD_TYPE);
		String configName = null;
		
		if (book == null){
			fail("config == null");
		} else {
			configName = book.getName();
			System.out.printf("Config: %s > %s%n", format.getName(), configName);
			assertEquals("Book", book.getName());
		}
	}
	
	@Test
	public final void test3(){
		Format format = data.get(RECORD_TYPE.charAt(0));
		ConfigType book = format.getConfiguration(TAG, 6, RECORD_TYPE);
		
		if (book == null){
			fail("config == null");
		} else {
			FixedDatum[] map = book.getMap();
			FixedDatum f = null;
			int index0, index1, length;
			for (int i = 0; i < map.length; ++i){
				f = map[i];
				index0 = f.getIndex();
				length = f.getLength();
				index1 = (index0 + length) - 1;
				if (length > 1){
					System.out.printf("%d-%d: %s%n", index0, index1, f.getLabel());
				} else {
					System.out.printf("%d: %s%n", index0, f.getLabel());
				}
				/*
				code = f.getValuesMap();
				Iterator<Character> e = code.keySet().iterator();
				while (e.hasNext()){
					k = e.next();
					System.out.printf("  %c - %s%n", k, code.get(k));
				}
				*/
			}
			assertEquals("Book", book.getName());
		}
	}
}
