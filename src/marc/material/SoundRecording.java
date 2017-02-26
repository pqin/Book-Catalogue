package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class SoundRecording extends AbstractMaterial {
	public SoundRecording(){
		super("Sound Recording");
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		// TODO implement map
		return tmp;
	}
}
