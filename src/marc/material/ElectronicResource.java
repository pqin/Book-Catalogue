package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class ElectronicResource extends AbstractMaterial {
	public ElectronicResource(){
		super("Electronic Resource", 14);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(3, 1, "Color"));
		tmp.add(new FixedDatum(4, 1, "Dimensions"));
		tmp.add(new FixedDatum(5, 1, "Sound"));
		tmp.add(new FixedDatum(6, 3, "Image Bit Depth"));
		tmp.add(new FixedDatum(9, 1, "File Formats"));
		tmp.add(new FixedDatum(10, 1, "Quality Assurance Targets"));
		tmp.add(new FixedDatum(11, 1, "Antecedent/Source"));
		tmp.add(new FixedDatum(12, 1, "Level of Compression"));
		tmp.add(new FixedDatum(13, 1, "Reformatting Quality"));
		return tmp;
	}
}
