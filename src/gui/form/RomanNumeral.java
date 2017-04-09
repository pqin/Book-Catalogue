package gui.form;

public class RomanNumeral {
	private static final int[] rValue = {50, 40, 10, 9, 5, 4, 1};
	private static final String[] rNumeral = {"L", "XL", "X", "IX", "V", "IV", "I"};
	private static String[] cache = new String[rValue[0]];
	
	public static final String parse(int value){
		String roman = null;
		if (value >= 0 && value < cache.length){
			if (cache[value] == null){
				StringBuilder b = new StringBuilder();
				for (int i = 0; i < rValue.length; ++i){
					while (value - rValue[i] >= 0){
						b.append(rNumeral[i]);
						value = value - rValue[i];
					}
				}
				roman = b.toString();
				cache[value] = roman;
			} else {
				roman = cache[value];
			}
		} else {
			roman = "?";
		}
		return roman;
	}
}
