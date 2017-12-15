package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import marc.field.DataField;
import marc.field.Field;
import marc.field.FieldType;
import marc.field.Leader;

public class FieldTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testHasTag() {
		Leader leader = new Leader();
		assertTrue(leader.hasTag("LDR"));
		assertFalse(leader.hasTag("ldr"));
		
		Field field = new DataField("245");
		assertTrue(field.hasTag("245"));
	}

	@Test
	public final void testGetFieldType() {
		assertEquals(FieldType.UNKNOWN, Field.getFieldType(null));
		assertEquals(FieldType.UNKNOWN, Field.getFieldType(""));
		assertEquals(FieldType.UNKNOWN, Field.getFieldType("0"));
		assertEquals(FieldType.UNKNOWN, Field.getFieldType("00"));
		assertEquals(FieldType.UNKNOWN, Field.getFieldType("ldr"));
		assertEquals(FieldType.FIXED_FIELD, Field.getFieldType("LDR"));
		assertEquals(FieldType.CONTROL_FIELD, Field.getFieldType("000"));
		assertEquals(FieldType.CONTROL_FIELD, Field.getFieldType("001"));
		assertEquals(FieldType.CONTROL_FIELD, Field.getFieldType("003"));
		assertEquals(FieldType.CONTROL_FIELD, Field.getFieldType("005"));
		assertEquals(FieldType.FIXED_FIELD, Field.getFieldType("006"));
		assertEquals(FieldType.FIXED_FIELD, Field.getFieldType("007"));
		assertEquals(FieldType.FIXED_FIELD, Field.getFieldType("008"));
		assertEquals(FieldType.CONTROL_FIELD, Field.getFieldType("009"));
		assertEquals(FieldType.DATA_FIELD, Field.getFieldType("100"));
	}

	@Test
	public final void testCompareTo() {
		Field field0 = new Field();
		Field field1 = new Field();
		assertTrue(field0.compareTo(field1) == 0);
	}

}
