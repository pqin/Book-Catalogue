package marc.type;

import java.util.List;

import marc.field.FixedDatum;
import marc.field.Leader;

public abstract class AbstractRecordType {
	private ConfigType type, config;
	
	protected AbstractRecordType(String name, int length){
		type = new ConfigType(name, Leader.FIELD_LENGTH);
		config = new ConfigType(name, length);
	}
	protected AbstractRecordType(String name0, String name1, int length){
		type = new ConfigType(name0, Leader.FIELD_LENGTH);
		config = new ConfigType(name1, length);
	}
	
	public final String getTypeName(){
		return type.getName();
	}
	public final String getConfigName(){
		return config.getName();
	}
	
	public final int getTypeLength(){
		return type.getLength();
	}
	public final int getConfigLength(){
		return config.getLength();
	}
	
	public final FixedDatum[] getTypeMap(){
		if (type.getMap() == null){
			List<FixedDatum> list = getTypeList();
			type.setMap(list);
		}
		return type.getMap();
	}
	public final FixedDatum[] getConfigMap(){
		if (config.getMap() == null){
			List<FixedDatum> list = getConfigList();
			config.setMap(list);
		}
		return config.getMap();
	}
	
	protected abstract List<FixedDatum> getTypeList();
	protected abstract List<FixedDatum> getConfigList();
}
