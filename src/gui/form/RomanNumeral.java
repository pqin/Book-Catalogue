package gui.form;

public class RomanNumeral {
	private static final int[] rValue = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
	private static final String[] rNumeral = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
	private static final int MAX_ROMAN = 9999;
	
	public static final String toRoman(int value){
		String roman = null;
		if (value >= 0 && value <= MAX_ROMAN){
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < rValue.length; ++i){
				while (value - rValue[i] >= 0){
					b.append(rNumeral[i]);
					value = value - rValue[i];
				}
			}
			roman = b.toString();
		} else {
			roman = "?";
		}
		return roman;
	}
}
