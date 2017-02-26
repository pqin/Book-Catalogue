package marc.type;

import java.util.Arrays;
import java.util.List;

import marc.field.FixedDatum;

public abstract class AbstractRecordType {
	private String typeName, configName;
	private int typeLength, configLength;
	private FixedDatum[] typeMap, configMap;
	
	protected AbstractRecordType(String name0, int length0, int length1){
		typeName = name0;
		typeLength = length0;
		typeMap = null;
		
		configName = name0;
		configLength = length1;
		configMap = null;
	}	
	protected AbstractRecordType(String name0, int length0, String name1, int length1){
		typeName = name0;
		typeLength = length0;
		typeMap = null;
		
		configName = name1;
		configLength = length1;
		configMap = null;
	}
	
	public final String getTypeName(){
		return typeName;
	}
	public final String getConfigName(){
		return configName;
	}
	
	public final int getTypeLength(){
		return typeLength;
	}
	public final int getConfigLength(){
		return configLength;
	}
	
	public final FixedDatum[] getTypeMap(){
		if (typeMap == null){
			List<FixedDatum> list = getTypeList();
			typeMap = new FixedDatum[list.size()];
			typeMap = list.toArray(typeMap);
			Arrays.sort(typeMap);
		}
		return typeMap;
	}
	public final FixedDatum[] getConfigMap(){
		if (configMap == null){
			List<FixedDatum> list = getConfigList();
			configMap = new FixedDatum[list.size()];
			configMap = list.toArray(configMap);
			Arrays.sort(configMap);
		}
		return configMap;
	}
	
	protected abstract List<FixedDatum> getTypeList();
	protected abstract List<FixedDatum> getConfigList();
}
