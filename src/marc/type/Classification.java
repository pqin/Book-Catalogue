package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.MARC;
import marc.field.FixedDatum;

public final class Classification extends AbstractRecordType {
	public Classification(){
		super("Classification", MARC.LEADER_FIELD_LENGTH, 14);
	}
	
	@Override
	protected List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		// TODO create map
		tmp.add(new FixedDatum(6, 1, "Type"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entered"));
		// TODO create map
		return tmp;
	}
}
