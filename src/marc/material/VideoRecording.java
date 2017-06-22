package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class VideoRecording extends AbstractMaterial {
	public VideoRecording(){
		super("Video Recording", 9);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(1, 1, "Specific Material Designation"));
		tmp.add(new FixedDatum(3, 1, "Color"));
		tmp.add(new FixedDatum(4, 1, "Videorecording Format"));
		tmp.add(new FixedDatum(5, 1, "Sound on Medium or Separate"));
		tmp.add(new FixedDatum(6, 1, "Medium for Sound"));
		tmp.add(new FixedDatum(7, 1, "Dimensions"));
		tmp.add(new FixedDatum(8, 1, "Configuration of Playback Channels"));
		return tmp;
	}
}
