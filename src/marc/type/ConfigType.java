package marc.type;

import java.util.Arrays;
import java.util.List;

import marc.field.FixedDatum;

public class ConfigType {
	private String name;
	private int length;
	private FixedDatum[] map;
	
	public ConfigType(String name, int length){
		this.name = name;
		this.length = length;
		this.map = null;
	}

	public final String getName() {
		return name;
	}

	public final int getLength() {
		return length;
	}

	public final FixedDatum[] getMap() {
		if (map == null){
			map = new FixedDatum[0];
		}
		return map;
	}
	public final void setMap(List<FixedDatum> m){
		if (m == null){
			map = new FixedDatum[0];
		} else {
			map = new FixedDatum[m.size()];
			map = m.toArray(map);
			Arrays.sort(map);
		}
	}
	public final void setMap(FixedDatum[] m){
		if (m == null){
			map = new FixedDatum[0];
		} else {
			map = Arrays.copyOf(m, m.length);
			Arrays.sort(map);
		}
	}
}
