package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class RemoteSensingImage extends AbstractMaterial {
	public RemoteSensingImage(){
		super("Remote Sensing Image");
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		// TODO implement map
		return tmp;
	}
}
