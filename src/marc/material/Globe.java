package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class Globe extends AbstractMaterial {
	public Globe(){
		super("Globe", 6);
	}

	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(3, 1, "Color"));
		tmp.add(new FixedDatum(4, 1, "Physical Medium"));
		tmp.add(new FixedDatum(5, 1, "Type of Reproduction"));
		return tmp;
	}
}
