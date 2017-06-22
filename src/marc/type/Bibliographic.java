package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.field.FixedDatum;

public class Bibliographic extends AbstractRecordType {
	public Bibliographic(){
		super("Bibliographic", 40);
	}
	protected Bibliographic(String name1){
		super("Bibliographic", name1, 40);
	}

	@Override
	protected final List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(5, 1, "Rec stat", "Record Status"));
		tmp.add(new FixedDatum(6, 1, "Type", "Record Type"));
		tmp.add(new FixedDatum(7, 1, "BLvl", "Bibliographic Level"));
		tmp.add(new FixedDatum(8, 1, "Ctrl", "Control Type"));
		tmp.add(new FixedDatum(9, 1, "Encoding", "Character Coding Scheme"));
		tmp.add(new FixedDatum(17, 1, "ELvl", "Encoding Level"));
		tmp.add(new FixedDatum(18, 1, "Desc", "Descriptive Cataloging Form"));
		tmp.add(new FixedDatum(19, 1, "Link", "Multipart Resource Record Level"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entered", "Date Entered"));
		tmp.add(new FixedDatum(6, 1, "DtSt", "Type of Date/Publication Status"));
		tmp.add(new FixedDatum(7, 4, "Date1", "Date 1"));
		tmp.add(new FixedDatum(11, 4, "Date2", "Date 2"));
		tmp.add(new FixedDatum(15, 3, "Ctry", "Place of publication, production, or execution"));
		tmp.add(new FixedDatum(35, 3, "Lang", "Language"));
		tmp.add(new FixedDatum(38, 1, "MRec", "Modified Record"));
		tmp.add(new FixedDatum(39, 1, "Srce", "Cataloging Source"));
		return tmp;
	}
}
