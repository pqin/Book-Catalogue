package marc.material;

import java.util.List;

import marc.field.FixedDatum;

public class RemoteSensingImage extends AbstractMaterial {
	public RemoteSensingImage(){
		super("Remote Sensing Image", 11);
	}
	
	@Override
	protected List<FixedDatum> getList() {
		List<FixedDatum> tmp = super.getList();
		tmp.add(new FixedDatum(1, 1, "Specific Material Designation"));
		tmp.add(new FixedDatum(3, 1, "Altitude of Sensor"));
		tmp.add(new FixedDatum(4, 1, "Attitude of Sensor"));
		tmp.add(new FixedDatum(5, 1, "Cloud Cover"));
		tmp.add(new FixedDatum(6, 1, "Platform Construction Type"));
		tmp.add(new FixedDatum(7, 1, "Platform Use Category"));
		tmp.add(new FixedDatum(8, 1, "Sensor Type"));
		tmp.add(new FixedDatum(9, 2, "Data Type"));
		return tmp;
	}
}
