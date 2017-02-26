package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class Map extends AbstractMaterial {
	public Map(){
		super("Map", 8);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(3, 1, "Color"));
		tmp.add(new FixedDatum(4, 1, "Physical Medium"));
		tmp.add(new FixedDatum(5, 1, "Type of Reproduction"));
		tmp.add(new FixedDatum(6, 1, "Production/Reproduction Details"));
		tmp.add(new FixedDatum(7, 1, "Positive/Negative Aspect"));
		return tmp;
	}
}
