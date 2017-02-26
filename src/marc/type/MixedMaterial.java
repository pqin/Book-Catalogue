package marc.type;

import java.util.List;

import marc.field.FixedDatum;

public class MixedMaterial extends Bibliographic {
	public MixedMaterial(){
		super("Mixed Material");
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = super.getConfigList();
		tmp.add(new FixedDatum(23, 1, "Form"));
		return tmp;
	}
}
