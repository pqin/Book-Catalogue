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
		tmp.add(new FixedDatum(18, 4, "Ills", "Illustrations"));
		tmp.add(new FixedDatum(22, 1, "Audn", "Target Audience"));
		tmp.add(new FixedDatum(23, 1, "Form", "Form of Item"));
		tmp.add(new FixedDatum(24, 4, "Cont", "Nature of Contents"));
		tmp.add(new FixedDatum(28, 1, "GPub", "Government Publication"));
		tmp.add(new FixedDatum(29, 1, "Conf", "Conference Publication"));
		tmp.add(new FixedDatum(30, 1, "Fest", "Festschrift"));
		tmp.add(new FixedDatum(31, 1, "Indx", "Index"));
		tmp.add(new FixedDatum(33, 1, "LitF", "Literary Form"));
		tmp.add(new FixedDatum(34, 1, "Biog", "Biography"));
		return tmp;
	}
}
