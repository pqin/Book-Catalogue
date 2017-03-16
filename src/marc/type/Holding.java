package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.field.FixedDatum;

public final class Holding extends AbstractRecordType {
	public Holding(){
		super("Holding", 32);
	}
	
	@Override
	protected List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(5, 1, "Rec stat"));
		tmp.add(new FixedDatum(6, 1, "Type"));
		tmp.add(new FixedDatum(9, 1, "Encoding"));
		tmp.add(new FixedDatum(17, 1, "ELvl"));
		tmp.add(new FixedDatum(18, 1, "Item Info"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entered"));
		tmp.add(new FixedDatum(6, 1, "006"));	// Receipt or acquisition status
		tmp.add(new FixedDatum(7, 1, "007"));	// Method of acquisition
		tmp.add(new FixedDatum(8, 4, "008"));	// Expected acquisition end date
		tmp.add(new FixedDatum(12, 1, "012"));	// General retention policy
		tmp.add(new FixedDatum(13, 3, "013"));	// Specific retention policy
		tmp.add(new FixedDatum(16, 1, "016"));	// Completeness
		tmp.add(new FixedDatum(17, 3, "017"));	// Number of copies reported
		tmp.add(new FixedDatum(20, 1, "020"));	// Lending policy
		tmp.add(new FixedDatum(21, 1, "021"));	// Reproduction policy
		tmp.add(new FixedDatum(22, 3, "Lang"));	// Language
		tmp.add(new FixedDatum(25, 1, "020"));	// Separate or composite copy report
		tmp.add(new FixedDatum(26, 6, "026"));	// Date of report
		return tmp;
	}
}
