package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class MotionPicture extends AbstractMaterial {
	public MotionPicture(){
		super("Motion Picture");
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		// TODO implement map
		return tmp;
	}
}
