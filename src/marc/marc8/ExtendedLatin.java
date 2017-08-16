package marc.marc8;

import java.util.Arrays;

public class ExtendedLatin extends GraphicSet {
	public ExtendedLatin(){
		super("Extended Latin", (byte)0x45, 1, true);
		setGraphicSet(1);
		
		char[] t = new char[TABLE_LENGTH];
		Arrays.fill(t, UNKNOWN_CHAR);
		t[0] ='\u0141';
		t[1] ='\u00D8';
		t[2] ='\u0110';
		t[3] ='\u00DE';
		t[4] ='\u00C6';
		t[5] ='\u0152';
		t[6] ='\u02B9';
		t[7] ='\u00B7';
		t[8] ='\u266D';
		t[9] ='\u00AE';
		t[10] ='\u00B1';
		t[11] ='\u01A0';
		t[12] ='\u01AF';
		t[13] ='\u02BC';
		t[15] ='\u02BB';
		t[16] ='\u0142';
		t[17] ='\u00F8';
		t[18] ='\u0111';
		t[19] ='\u00FE';
		t[20] ='\u00E6';
		t[21] ='\u0153';
		t[22] ='\u02BA';
		t[23] ='\u0131';
		t[24] ='\u00A3';
		t[25] ='\u00F0';
		t[27] ='\u01A1';
		t[28] ='\u01B0';
		t[31] ='\u00B0';
		t[32] ='\u2113';
		t[33] ='\u2117';
		t[34] ='\u00A9';
		t[35] ='\u266F';
		t[36] ='\u00BF';
		t[37] ='\u00A1';
		t[38] ='\u00DF';
		t[39] ='\u20AC';
		t[63] ='\u0309';
		t[64] ='\u0300';
		t[65] ='\u0301';
		t[66] ='\u0302';
		t[67] ='\u0303';
		t[68] ='\u0304';
		t[69] ='\u0306';
		t[70] ='\u0307';
		t[71] ='\u0308';
		t[72] ='\u030C';
		t[73] ='\u030A';
		t[74] ='\u0361';
		t[75] ='\uFE21';
		t[76] ='\u0315';
		t[77] ='\u030B';
		t[78] ='\u0310';
		t[79] ='\u0327';
		t[80] ='\u0328';
		t[81] ='\u0323';
		t[82] ='\u0324';
		t[83] ='\u0325';
		t[84] ='\u0333';
		t[85] ='\u0332';
		t[86] ='\u0326';
		t[87] ='\u031C';
		t[88] ='\u032E';
		t[89] ='\u0360';
		t[90] ='\uFE22';
		t[93] ='\u0313';
		setTable(t);
	}
}
