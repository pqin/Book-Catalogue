package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class ProjectedGraphic extends AbstractMaterial {
	public ProjectedGraphic(){
		super("Projected Graphic", 9);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(1, 1, "Specific Material Designation"));
		tmp.add(new FixedDatum(3, 1, "Color"));
		tmp.add(new FixedDatum(4, 1, "Base of Emulsion"));
		tmp.add(new FixedDatum(5, 1, "Sound on Medium or Separate"));
		tmp.add(new FixedDatum(6, 1, "Medium for Sound"));
		tmp.add(new FixedDatum(7, 1, "Dimensions"));
		tmp.add(new FixedDatum(8, 1, "Secondary Support Material"));
		return tmp;
	}
}
