package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class Microform extends AbstractMaterial {
	public Microform(){
		super("Microform");
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		// TODO implement map
		return tmp;
	}
}
