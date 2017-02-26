package marc.type;

import java.util.List;

import marc.field.FixedDatum;

public class ComputerFile extends Bibliographic {
	public ComputerFile(){
		super("Computer File");
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = super.getConfigList();
		tmp.add(new FixedDatum(22, 1, "Audn"));
		tmp.add(new FixedDatum(23, 1, "Form"));
		tmp.add(new FixedDatum(26, 1, "File"));
		tmp.add(new FixedDatum(28, 1, "GPub"));
		return tmp;
	}
}
