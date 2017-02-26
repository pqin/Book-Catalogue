package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class VideoRecording extends AbstractMaterial {
	public VideoRecording(){
		super("Video Recording");
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		// TODO implement map
		return tmp;
	}
}
