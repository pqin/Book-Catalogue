package marc.type;

import java.util.List;

import marc.field.FixedDatum;

public class ContinuingResource extends Bibliographic {
	public ContinuingResource(){
		super("Continuing Resource");
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = super.getConfigList();
		tmp.add(new FixedDatum(18, 1, "Freq"));
		tmp.add(new FixedDatum(19, 1, "Regl"));
		tmp.add(new FixedDatum(21, 1, "SrTp"));
		tmp.add(new FixedDatum(22, 1, "Orig"));
		tmp.add(new FixedDatum(23, 1, "Form"));
		tmp.add(new FixedDatum(24, 1, "EntW"));
		tmp.add(new FixedDatum(25, 3, "Cont"));
		tmp.add(new FixedDatum(28, 1, "GPub"));
		tmp.add(new FixedDatum(29, 1, "Conf"));
		tmp.add(new FixedDatum(33, 1, "Alph"));
		tmp.add(new FixedDatum(34, 1, "S/L"));
		return tmp;
	}
}
