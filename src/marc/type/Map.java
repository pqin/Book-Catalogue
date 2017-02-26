package marc.type;

import java.util.List;

import marc.field.FixedDatum;

public class Map extends Bibliographic {
	public Map(){
		super("Map");
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = super.getConfigList();
		tmp.add(new FixedDatum(18, 4, "Relf"));
		tmp.add(new FixedDatum(22, 2, "Proj"));
		tmp.add(new FixedDatum(25, 1, "CrTp"));
		tmp.add(new FixedDatum(28, 1, "GPub"));
		tmp.add(new FixedDatum(29, 1, "Form"));
		tmp.add(new FixedDatum(31, 1, "Indx"));
		tmp.add(new FixedDatum(33, 2, "SpFm"));
		return tmp;
	}
}
