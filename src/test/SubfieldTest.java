package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import marc.field.Subfield;

public class SubfieldTest {
	@Test
	public final void testGetCode() {
		Subfield subfield = new Subfield('a', "Sample data.");
		assertEquals('a', subfield.getCode());
	}

	@Test
	public final void testSetCode() {
		Subfield subfield = new Subfield('a', "Sample data.");
		subfield.setCode('b');
		assertEquals('b', subfield.getCode());
	}

	@Test
	public final void testGetData() {
		Subfield subfield = new Subfield('a', "Sample data.");
		assertEquals("Sample data.", subfield.getData());
	}

	@Test
	public final void testSetData() {
		Subfield subfield = new Subfield('a', "Sample data.");
		String data = "New data.";
		subfield.setData(data);
		assertEquals(data, subfield.getData());
	}

	@Test
	public final void testToString() {
		Subfield subfield = new Subfield('a', "Sample data.");
		assertEquals("$aSample data.", subfield.toString());
	}

	@Test
	public final void testEqualsObject() {
		Subfield reference = new Subfield('a', "A");
		Subfield same = new Subfield('a', "A");
		Subfield differentCode = new Subfield('b', "A");
		Subfield differentData = new Subfield('a', "B");
		assertTrue(reference.equals(same));
		assertTrue(same.equals(reference));
		assertFalse(reference.equals(differentCode));
		assertFalse(reference.equals(differentData));
	}

	@Test
	public final void testCopy() {
		Subfield original = new Subfield('a', "A");
		Subfield copy = original.copy();
		assertTrue(copy.equals(original));
		assertFalse(copy == original);
	}

}
