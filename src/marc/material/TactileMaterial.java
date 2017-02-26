package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class TactileMaterial extends AbstractMaterial {
	public TactileMaterial(){
		super("Tactile Material", 10);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(3, 2, "Class of braille writing"));
		tmp.add(new FixedDatum(5, 1, "Level of contraction"));
		tmp.add(new FixedDatum(6, 3, "Braille music format"));
		tmp.add(new FixedDatum(9, 1, "Special physical characteristics"));
		return tmp;
	}
}
