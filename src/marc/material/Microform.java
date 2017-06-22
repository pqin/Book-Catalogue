package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class Microform extends AbstractMaterial {
	public Microform(){
		super("Microform", 13);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(1, 1, "Specific Material Designation"));
		tmp.add(new FixedDatum(3, 1, "Positive/Negative aspect"));
		tmp.add(new FixedDatum(4, 1, "Dimensions"));
		tmp.add(new FixedDatum(5, 1, "Reduction Ratio Range"));
		tmp.add(new FixedDatum(6, 3, "Reduction Ratio"));
		tmp.add(new FixedDatum(9, 1, "Color"));
		tmp.add(new FixedDatum(10, 1, "Emulsion on Film"));
		tmp.add(new FixedDatum(11, 1, "Generation"));
		tmp.add(new FixedDatum(12, 1, "Base of Film"));
		return tmp;
	}
}
