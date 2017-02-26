package marc.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import marc.field.FixedDatum;

public abstract class AbstractMaterial {
	private String name;
	private int length;
	private FixedDatum[] map;
	
	protected AbstractMaterial(String name){
		this.name = name;
		this.length = 2;
		this.map = null;
	}
	protected AbstractMaterial(String name, int length){
		this.name = name;
		this.length = length;
		this.map = null;
	}
	
	public final String getName(){
		return name;
	}
	public final int getLength(){
		return length;
	}
	public final FixedDatum[] getMap(){
		if (map == null){
			List<FixedDatum> list = getList();
			map = new FixedDatum[list.size()];
			map = list.toArray(map);
			Arrays.sort(map);
		}
		return map;
	}
	
	protected List<FixedDatum> getList(){
		List<FixedDatum> tmp = new ArrayList<FixedDatum>();
		tmp.add(new FixedDatum(0, 1, "Category"));
		tmp.add(new FixedDatum(1, 1, "SMD"));
		return tmp;
	}
}
