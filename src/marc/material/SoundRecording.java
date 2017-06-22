package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class SoundRecording extends AbstractMaterial {
	public SoundRecording(){
		super("Sound Recording", 14);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(1, 1, "Specific Material Designation"));
		tmp.add(new FixedDatum(3, 1, "Speed"));
		tmp.add(new FixedDatum(4, 1, "Configuration of Playback Channels"));
		tmp.add(new FixedDatum(5, 1, "Groove Width/Groove Pitch"));
		tmp.add(new FixedDatum(6, 1, "Dimensions"));
		tmp.add(new FixedDatum(7, 1, "Tape Width"));
		tmp.add(new FixedDatum(8, 1, "Tape Configuration"));
		tmp.add(new FixedDatum(9, 1, "Type of Disc, Cylinder, or Tape"));
		tmp.add(new FixedDatum(10, 1, "Kind of Material"));
		tmp.add(new FixedDatum(11, 1, "Kind of Cutting"));
		tmp.add(new FixedDatum(12, 1, "Special Playback Characteristics"));
		tmp.add(new FixedDatum(13, 1, "Capture and Storage Technique"));
		return tmp;
	}
}
