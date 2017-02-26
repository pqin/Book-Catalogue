package marc.type;

import java.util.List;

import marc.field.FixedDatum;

public class VisualMaterial extends Bibliographic {
	public VisualMaterial(){
		super("Visual Material");
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = super.getConfigList();
		tmp.add(new FixedDatum(18, 3, "Time"));
		tmp.add(new FixedDatum(22, 1, "Audn"));
		tmp.add(new FixedDatum(28, 1, "GPub"));
		tmp.add(new FixedDatum(29, 1, "Form"));
		tmp.add(new FixedDatum(33, 1, "TMat"));
		tmp.add(new FixedDatum(34, 1, "Tech"));
		return tmp;
	}
}
