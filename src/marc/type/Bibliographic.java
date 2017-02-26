package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.MARC;
import marc.field.FixedDatum;

public class Bibliographic extends AbstractRecordType {
	public Bibliographic(){
		super("Bibliographic", MARC.LEADER_FIELD_LENGTH, 40);
	}
	protected Bibliographic(String name1){
		super("Bibliographic", MARC.LEADER_FIELD_LENGTH, name1, 40);
	}

	@Override
	protected final List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(5, 1, "Rec stat"));
		tmp.add(new FixedDatum(6, 1, "Type"));
		tmp.add(new FixedDatum(7, 1, "BLvl"));
		tmp.add(new FixedDatum(8, 1, "Ctrl"));
		tmp.add(new FixedDatum(9, 1, "Encoding"));
		tmp.add(new FixedDatum(17, 1, "ELvl"));
		tmp.add(new FixedDatum(18, 1, "Desc"));
		tmp.add(new FixedDatum(19, 1, "Link"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entered"));
		tmp.add(new FixedDatum(6, 1, "DtSt"));
		tmp.add(new FixedDatum(7, 4, "Date1"));
		tmp.add(new FixedDatum(11, 4, "Date2"));
		tmp.add(new FixedDatum(15, 3, "Ctry"));
		tmp.add(new FixedDatum(35, 3, "Lang"));
		tmp.add(new FixedDatum(38, 1, "MRec"));
		tmp.add(new FixedDatum(39, 1, "Srce"));
		return tmp;
	}
}
