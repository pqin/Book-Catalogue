package marc.type;

import java.util.List;

import marc.field.FixedDatum;

public class Music extends Bibliographic {
	public Music(){
		super("Music");
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = super.getConfigList();
		tmp.add(new FixedDatum(18, 2, "Comp"));
		tmp.add(new FixedDatum(20, 1, "FMus"));
		tmp.add(new FixedDatum(21, 1, "Part"));
		tmp.add(new FixedDatum(22, 1, "Audn"));
		tmp.add(new FixedDatum(23, 1, "Form"));
		tmp.add(new FixedDatum(24, 6, "AccM"));
		tmp.add(new FixedDatum(30, 2, "LTxt"));
		tmp.add(new FixedDatum(33, 1, "TrAr"));
		return tmp;
	}
}
