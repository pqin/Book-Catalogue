package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.MARC;
import marc.field.FixedDatum;

public final class Holding extends AbstractRecordType {
	public Holding(){
		super("Holding", MARC.LEADER_FIELD_LENGTH, 32);
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
