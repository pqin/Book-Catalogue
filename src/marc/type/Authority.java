package marc.type;

import java.util.ArrayList;
import java.util.List;

import marc.field.FixedDatum;

public final class Authority extends AbstractRecordType {
	public Authority() {
		super("Authority", 40);
	}
	
	/* FixedDatum.label values / mnemonics referenced from OCLC.
	 * "Authorities: Format and Indexes" www.oclc.org.
	 * ©2017 OCLC Online Computer Library Center
	 * 22 February 2017
	 * < https://www.oclc.org/support/services/worldcat/documentation/authorities/authformat.en.html >
	 */
	@Override
	protected List<FixedDatum> getTypeList() {
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(5, 1, "Rec stat"));
		tmp.add(new FixedDatum(6, 1, "Type"));
		tmp.add(new FixedDatum(9, 1, "Encoding"));
		tmp.add(new FixedDatum(17, 1, "ELvl"));
		tmp.add(new FixedDatum(18, 1, "Punc"));
		return tmp;
	}
	
	@Override
	protected List<FixedDatum> getConfigList() {
		List<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 6, "Entered"));
		tmp.add(new FixedDatum(6, 1, "Geo subd"));
		tmp.add(new FixedDatum(7, 1, "Roman"));
		tmp.add(new FixedDatum(9, 1, "Auth/Ref"));
		tmp.add(new FixedDatum(10, 1, "Rules"));
		tmp.add(new FixedDatum(11, 1, "Subj"));
		tmp.add(new FixedDatum(12, 1, "Series"));
		tmp.add(new FixedDatum(13, 1, "Ser num"));
		tmp.add(new FixedDatum(14, 1, "Name use"));
		tmp.add(new FixedDatum(15, 1, "Subj use"));
		tmp.add(new FixedDatum(16, 1, "Ser use"));
		tmp.add(new FixedDatum(17, 1, "Subd type"));
		tmp.add(new FixedDatum(28, 1, "Govt agn"));
		tmp.add(new FixedDatum(29, 1, "Ref status"));
		tmp.add(new FixedDatum(31, 1, "Upd status"));
		tmp.add(new FixedDatum(32, 1, "Name"));
		tmp.add(new FixedDatum(33, 1, "Auth status"));
		tmp.add(new FixedDatum(38, 1, "Mod rec"));
		tmp.add(new FixedDatum(39, 1, "Source"));
		return tmp;
	}
}
