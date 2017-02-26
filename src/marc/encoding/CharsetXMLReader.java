package marc.encoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import marc.marc8.CharacterSet;

final class CharsetXMLReader {
	private static final String ROOT = "codeTables";
	private static final String CODE_TABLE = "codeTable";
	private static final String CHARACTER_SET = "characterSet";
	private static final String NAME = "name";
	private static final String FINAL = "ISOcode";
	private static final String CODE = "code";
	private static final String MARC = "marc";
	private static final String UTF8 = "utf-8";
	
	/*
	 <?xml version="1.0"?>
	<codeTables>
	<codeTable name="Basic and Extended Latin" date="January 2000" number="1">
		<note></note>
		<characterSet name="Basic Latin (ASCII)" ISOcode="42">
			<code>
				<marc>20</marc>
				<ucs>0020</ucs>
				<utf-8>20</utf-8>
				<name>SPACE, BLANK / SPACE</name>
			</code>
		</characterSet>
		<characterSet name="Extended Latin (ANSEL)" date="January 2000, Updated September 2004" ISOcode="45">
			<code>
				<isCombining>true</isCombining>
				<marc>FB</marc>
				<ucs></ucs>
				<utf-8></utf-8>
				<alt>FE23</alt>
				<altutf-8>EFB8A3</altutf-8>
				<name>DOUBLE TILDE, SECOND HALF / COMBINING DOUBLE TILDE RIGHT HALF</name>
				<note></note>
			</code>
		</characterSet>
	</codeTable>
	 */
	
	Map<Byte, CharacterSet> readFile(String filename){
		HashMap<Byte, CharacterSet> map = new HashMap<Byte, CharacterSet>();
		
		File file = new File(filename);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		XMLStreamReader reader = null;
		String localName, content;
		try {
			reader = factory.createXMLStreamReader(in);
			while (reader.hasNext()){
				// TODO parse XML
				switch (reader.next()){
				case XMLStreamConstants.START_ELEMENT:
					localName = reader.getLocalName();
					break;
				case XMLStreamConstants.CHARACTERS:
					content = reader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					localName = reader.getLocalName();
				default:
					break;
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return map;
	}
}
