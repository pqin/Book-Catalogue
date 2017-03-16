package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.field.FixedDatum;

public final class Community extends AbstractRecordType {
	public Community(){
		super("Community", 15);
	}
	
	@Override
	protected List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(5, 1, "Rec stat"));
		tmp.add(new FixedDatum(6, 1, "Type"));
		tmp.add(new FixedDatum(7, 1, "007"));	// Kind of data
		tmp.add(new FixedDatum(9, 1, "Encoding"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entered"));
		tmp.add(new FixedDatum(6, 1, "006"));	// Volunteer opportunities
		tmp.add(new FixedDatum(7, 1, "007"));	// Volunteers provided
		tmp.add(new FixedDatum(8, 1, "008"));	// Child care arrangements
		tmp.add(new FixedDatum(9, 1, "009"));	// Speakers bureau
		tmp.add(new FixedDatum(10, 1, "010"));	// Mutual support groups
		tmp.add(new FixedDatum(11, 1, "011"));	// Meeting rooms and facilities available
		tmp.add(new FixedDatum(12, 3, "Lang"));	// Language
		return tmp;
	}
}
