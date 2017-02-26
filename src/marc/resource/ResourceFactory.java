package marc.resource;

import java.util.Arrays;

import marc.MARC;
import marc.field.FixedDatum;

public class ResourceFactory {
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
