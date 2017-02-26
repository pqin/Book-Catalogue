package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class ProjectedMaterial extends AbstractMaterial {
	public ProjectedMaterial(){
		super("Projected Material");
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		// TODO implement map
		return tmp;
	}
}
