<<<<<<< HEAD
package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.field.FixedDatum;

public final class UnknownType extends AbstractRecordType {
	public UnknownType(){
		super("Unknown", 6);
	}
	
	@Override
	protected List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(6, 1, "Type"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entry Date"));
		return tmp;
	}
}
=======
package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.MARC;
import marc.field.FixedDatum;

public final class UnknownType extends AbstractRecordType {
	public UnknownType(){
		super("Unknown", MARC.LEADER_FIELD_LENGTH, 6);
	}
	
	@Override
	protected List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(6, 1, "Type"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entry Date"));
		return tmp;
	}
}
>>>>>>> marc-8
