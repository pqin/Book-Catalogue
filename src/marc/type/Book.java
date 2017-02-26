package marc.type;

import java.util.List;

import marc.field.FixedDatum;

public class Book extends Bibliographic {
	public Book(){
		super("Book");
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = super.getConfigList();
		tmp.add(new FixedDatum(18, 4, "Ills"));
		tmp.add(new FixedDatum(22, 1, "Audn"));
		tmp.add(new FixedDatum(23, 1, "Form"));
		tmp.add(new FixedDatum(24, 4, "Cont"));
		tmp.add(new FixedDatum(28, 1, "GPub"));
		tmp.add(new FixedDatum(29, 1, "Conf"));
		tmp.add(new FixedDatum(30, 1, "Fest"));
		tmp.add(new FixedDatum(31, 1, "Indx"));
		tmp.add(new FixedDatum(33, 1, "LitF"));
		tmp.add(new FixedDatum(34, 1, "Biog"));
		return tmp;
	}
}
