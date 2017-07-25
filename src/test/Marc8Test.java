package test;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.BeforeClass;
import org.junit.Test;

import marc.marc8.Marc8;

public class Marc8Test {
	private static Charset charset;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		charset = new Marc8();
	}

	@Test
	public final void testCanEncode() {
		assertFalse(charset.canEncode());
	}

	@Test
	public final void testContainsCharset() {
		assertFalse(charset.contains(null));
		assertTrue(charset.contains(charset));
		// Dependent on state of decoder. Must explicitly set with escape sequence.
		assertFalse(charset.contains(StandardCharsets.US_ASCII));
		assertFalse(charset.contains(StandardCharsets.UTF_8));
	}
}
