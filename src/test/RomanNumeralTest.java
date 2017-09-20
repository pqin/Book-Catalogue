package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import gui.form.RomanNumeral;

public class RomanNumeralTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public final void testToRoman() {
		assertEquals("I", RomanNumeral.toRoman(1));
		assertEquals("MCMLXX", RomanNumeral.toRoman(1970));
		assertEquals("MCMXCIX", RomanNumeral.toRoman(1999));
		assertEquals("MM", RomanNumeral.toRoman(2000));
	}

}
