package marc.resource;

import java.util.ArrayList;
import java.util.Arrays;

import marc.MARC;
import marc.field.FixedDatum;

public class ResourceFactory {	
	public static FixedDatum[] getVariableData(ResourceType resType){
		ArrayList<FixedDatum> tmp = new ArrayList<FixedDatum>();
		switch (resType){
		case BOOK:
			tmp.add(new FixedDatum(18, 4, "Ills"));
			tmp.add(new FixedDatum(22, 1, "Audn"));
			tmp.add(new FixedDatum(23, 1, "Form"));
			tmp.add(new FixedDatum(24, 4, "Cont"));
			tmp.add(new FixedDatum(28, 1, "GPub"));
			tmp.add(new FixedDatum(29, 1, "Conf"));
			tmp.add(new FixedDatum(30, 1, "Fest"));
			tmp.add(new FixedDatum(31, 1, "Indx"));
			tmp.add(new FixedDatum(33, 1, "LitF"));
			tmp.add(new FixedDatum(34, 1, "Biog"));
			break;
		case COMPUTER_FILE:
			tmp.add(new FixedDatum(22, 1, "Audn"));
			tmp.add(new FixedDatum(23, 1, "Form"));
			tmp.add(new FixedDatum(26, 1, "File"));
			tmp.add(new FixedDatum(28, 1, "GPub"));
			break;
		case CONTINUING_RESOURCE:
			tmp.add(new FixedDatum(18, 1, "Freq"));
			tmp.add(new FixedDatum(19, 1, "Regl"));
			tmp.add(new FixedDatum(21, 1, "SrTp"));
			tmp.add(new FixedDatum(22, 1, "Orig"));
			tmp.add(new FixedDatum(23, 1, "Form"));
			tmp.add(new FixedDatum(24, 1, "EntW"));
			tmp.add(new FixedDatum(25, 3, "Cont"));
			tmp.add(new FixedDatum(28, 1, "GPub"));
			tmp.add(new FixedDatum(29, 1, "Conf"));
			tmp.add(new FixedDatum(33, 1, "Alph"));
			tmp.add(new FixedDatum(34, 1, "S/L"));
			break;
		case MAP:
			tmp.add(new FixedDatum(18, 4, "Relf"));
			tmp.add(new FixedDatum(22, 2, "Proj"));
			tmp.add(new FixedDatum(25, 1, "CrTp"));
			tmp.add(new FixedDatum(28, 1, "GPub"));
			tmp.add(new FixedDatum(29, 1, "Form"));
			tmp.add(new FixedDatum(31, 1, "Indx"));
			tmp.add(new FixedDatum(33, 2, "SpFm"));
			break;
		case MUSIC:
			tmp.add(new FixedDatum(18, 2, "Comp"));
			tmp.add(new FixedDatum(20, 1, "FMus"));
			tmp.add(new FixedDatum(21, 1, "Part"));
			tmp.add(new FixedDatum(22, 1, "Audn"));
			tmp.add(new FixedDatum(23, 1, "Form"));
			tmp.add(new FixedDatum(24, 6, "AccM"));
			tmp.add(new FixedDatum(30, 2, "LTxt"));
			tmp.add(new FixedDatum(33, 1, "TrAr"));
			break;
		case MIXED_MATERIAL:
			tmp.add(new FixedDatum(23, 1, "Form"));
			break;
		case VISUAL_MATERIAL:
			tmp.add(new FixedDatum(18, 4, "Time"));
			tmp.add(new FixedDatum(22, 1, "Audn"));
			tmp.add(new FixedDatum(28, 1, "GPub"));
			tmp.add(new FixedDatum(29, 1, "Form"));
			tmp.add(new FixedDatum(33, 1, "TMat"));
			tmp.add(new FixedDatum(34, 1, "Tech"));
			break;
		default:
			break;
		}
		FixedDatum[] data = new FixedDatum[tmp.size()];
		data = tmp.toArray(data);
		return data;
	}
	
	public char[] convert(char[] resource, FixedDatum[] src, FixedDatum[] dest){
		final int length = resource.length;
		char[] res = new char[length];
		Arrays.fill(res, MARC.FILL_CHAR);
		
		String srcLabel, destLabel;
		int length0, length1;
		int index0, index1;
		char[] value;
		for (int s = 0; s < src.length; ++s){
			srcLabel = src[s].getLabel();
			length0 = src[s].getLength();
			for (int d = 0; d < dest.length; ++d){
				destLabel = dest[d].getLabel();
				length1 = dest[d].getLength();
				if (srcLabel.equals(destLabel)){
					index0 = src[s].getIndex();
					index1 = dest[d].getIndex();
					value = Arrays.copyOfRange(resource, index0, index0 + length0);
					for (int i = 0; i < length0 && i < length1; ++i){
						res[index1 + i] = value[i];
					}
				}
			}
		}
		
		return res;
	}
}
