package marc;

import java.util.HashMap;

import marc.field.Leader;
import marc.type.AbstractRecordType;
import marc.type.Authority;
import marc.type.Bibliographic;
import marc.type.Book;
import marc.type.Classification;
import marc.type.Community;
import marc.type.ComputerFile;
import marc.type.ContinuingResource;
import marc.type.Holding;
import marc.type.Map;
import marc.type.MixedMaterial;
import marc.type.Music;
import marc.type.UnknownType;
import marc.type.VisualMaterial;

public final class RecordTypeFactory {
	private static final HashMap<String, Class<? extends AbstractRecordType>> typeMap = buildTypeMap();
	private static final HashMap<String, Class<? extends AbstractRecordType>> configMap = buildConfigMap();
	private static final Class<? extends AbstractRecordType> UNKNOWN_TYPE = UnknownType.class;
	private static final HashMap<Class<? extends AbstractRecordType>, AbstractRecordType> cache = buildCache();
	
	private static final HashMap<String, Class<? extends AbstractRecordType>> buildTypeMap(){
		HashMap<String, Class<? extends AbstractRecordType>> tmp = new HashMap<String, Class<? extends AbstractRecordType>>();
		
		tmp.put("a", Bibliographic.class);
		tmp.put("c", Bibliographic.class);
		tmp.put("d", Bibliographic.class);
		tmp.put("e", Bibliographic.class);
		tmp.put("f", Bibliographic.class);
		tmp.put("g", Bibliographic.class);
		tmp.put("i", Bibliographic.class);
		tmp.put("j", Bibliographic.class);
		tmp.put("k", Bibliographic.class);
		tmp.put("m", Bibliographic.class);
		tmp.put("o", Bibliographic.class);
		tmp.put("p", Bibliographic.class);
		tmp.put("q", Community.class);
		tmp.put("r", Bibliographic.class);
		tmp.put("t", Bibliographic.class);
		tmp.put("u", Holding.class);
		tmp.put("v", Holding.class);
		tmp.put("w", Classification.class);
		tmp.put("x", Holding.class);
		tmp.put("y", Holding.class);
		tmp.put("z", Authority.class);
		return tmp;
	}
	
	private static final HashMap<String, Class<? extends AbstractRecordType>> buildConfigMap(){
		HashMap<String, Class<? extends AbstractRecordType>> tmp = new HashMap<String, Class<? extends AbstractRecordType>>();
		
		tmp.put("aa", Book.class);
		tmp.put("ab", ContinuingResource.class);
		tmp.put("ac", Book.class);
		tmp.put("ad", Book.class);
		tmp.put("ai", ContinuingResource.class);
		tmp.put("am", Book.class);
		tmp.put("as", ContinuingResource.class);
		tmp.put("c", Music.class);
		tmp.put("d", Music.class);
		tmp.put("e", Map.class);
		tmp.put("f", Map.class);
		tmp.put("g", VisualMaterial.class);
		tmp.put("i", Music.class);
		tmp.put("j", Music.class);
		tmp.put("k", VisualMaterial.class);
		tmp.put("m", ComputerFile.class);
		tmp.put("o", VisualMaterial.class);
		tmp.put("p", MixedMaterial.class);
		tmp.put("r", VisualMaterial.class);
		tmp.put("t", Book.class);
		return tmp;
	}
	
	private static final HashMap<Class<? extends AbstractRecordType>, AbstractRecordType> buildCache(){
		HashMap<Class<? extends AbstractRecordType>, AbstractRecordType> tmp = new HashMap<Class<? extends AbstractRecordType>, AbstractRecordType>();
		try {
			tmp.put(UNKNOWN_TYPE, UNKNOWN_TYPE.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return tmp;
	}
	private static final AbstractRecordType getInstance(Class<? extends AbstractRecordType> c){
		AbstractRecordType object = null;
		if (cache.containsKey(c)){
			object = cache.get(c);
		} else {
			try {
				object = c.newInstance();
				cache.put(c, object);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return object;
	}
	
	public static final AbstractRecordType getMaterialConfig(Leader leader){
		char[] type = leader.getData(Leader.TYPE, 1);
		Class<? extends AbstractRecordType> c = null;
		c = typeMap.getOrDefault(String.valueOf(type), UNKNOWN_TYPE);
		if (c == Bibliographic.class){
			char[] key = leader.getData(Leader.TYPE, (type[0] == 'a') ? 2 : 1);
			c = configMap.getOrDefault(String.valueOf(key), UNKNOWN_TYPE);
		}
		return getInstance(c);
	}
}
