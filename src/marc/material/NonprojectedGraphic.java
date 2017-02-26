package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class NonprojectedGraphic extends AbstractMaterial {
	public NonprojectedGraphic(){
		super("Nonprojected Graphic");
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		// TODO implement map
		return tmp;
	}
}
