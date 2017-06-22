package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class NonprojectedGraphic extends AbstractMaterial {
	public NonprojectedGraphic(){
		super("Nonprojected Graphic", 6);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(1, 1, "Specific Material Designation"));
		tmp.add(new FixedDatum(3, 1, "Color"));
		tmp.add(new FixedDatum(4, 1, "Primary Support Material"));
		tmp.add(new FixedDatum(5, 1, "Secondary Support Material"));
		return tmp;
	}
}
