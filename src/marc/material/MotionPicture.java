package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class MotionPicture extends AbstractMaterial {
	public MotionPicture(){
		super("Motion Picture", 23);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(1, 1, "Specific Material Designation"));
		tmp.add(new FixedDatum(3, 1, "Color"));
		tmp.add(new FixedDatum(4, 1, "Motion Picture Presentation Format"));
		tmp.add(new FixedDatum(5, 1, "Sound on Medium or Separate"));
		tmp.add(new FixedDatum(6, 1, "Medium for Sound"));
		tmp.add(new FixedDatum(7, 1, "Dimensions"));
		tmp.add(new FixedDatum(8, 1, "Configuration of Playback Channels"));
		tmp.add(new FixedDatum(9, 1, "Production Elements"));
		tmp.add(new FixedDatum(10, 1, "Positive/Negative Aspect"));
		tmp.add(new FixedDatum(11, 1, "Generation"));
		tmp.add(new FixedDatum(12, 1, "Base of Film"));
		tmp.add(new FixedDatum(13, 1, "Refined Categories of Color"));
		tmp.add(new FixedDatum(14, 1, "Kind of Color Stock or Print"));
		tmp.add(new FixedDatum(15, 1, "Deterioration Stage"));
		tmp.add(new FixedDatum(16, 1, "Completeness"));
		tmp.add(new FixedDatum(17, 6, "Film Inspection Date"));
		return tmp;
	}
}
