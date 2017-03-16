package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.field.FixedDatum;

public final class Classification extends AbstractRecordType {
	public Classification(){
		super("Classification", 14);
	}
	
	@Override
	protected List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(5, 1, "Rec stat"));
		tmp.add(new FixedDatum(6, 1, "Type"));
		tmp.add(new FixedDatum(9, 1, "Encoding"));
		tmp.add(new FixedDatum(17, 1, "ELvl"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entered"));
		tmp.add(new FixedDatum(6, 1, "006"));	// Kind of record
		tmp.add(new FixedDatum(7, 1, "007"));	// Type of number
		tmp.add(new FixedDatum(8, 1, "008"));	// Classification validity
		tmp.add(new FixedDatum(9, 1, "009"));	// Standard or optional designation
		tmp.add(new FixedDatum(10, 1, "010"));	// Record update in progress
		tmp.add(new FixedDatum(11, 1, "011"));	// Level of establishment
		tmp.add(new FixedDatum(12, 1, "012"));	// Synthesized number indication
		tmp.add(new FixedDatum(13, 1, "013"));	// Display controller
		return tmp;
	}
}
