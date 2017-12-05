package marc.record;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import marc.RecordTypeFactory;
import marc.field.FixedDataElement;
import marc.field.Leader;
import marc.type.ConfigType;
import marc.type.Format;
import marc.type.RecordType;

public final class RecordFactory {
	private static final ZoneId TIME_ZONE = ZoneId.ofOffset("", ZoneOffset.UTC);
	
	public static final Record generate(RecordType type){
		Leader leader = getLeader(type);
		Format format = RecordTypeFactory.getFormat(leader);
		ConfigType config = RecordTypeFactory.getConfigType(format, leader, FixedDataElement.TAG);
		FixedDataElement dataElement = getFixedDataElement(config);
		
		Record record = new Record();
		record.setLeader(leader);
		record.setFixedDataElement(dataElement);
		record.setEntryDate(LocalDate.now(TIME_ZONE));
		return record;
	}
	
	private static Leader getLeader(RecordType type){
		Leader leader = new Leader();
		switch (type){
		case BIBLIOGRAPHIC:
			leader.setData('a', Leader.TYPE);
			leader.setData('m', Leader.TYPE+1);
			break;
		case AUTHORITY:
			leader.setData('z', Leader.TYPE);
			break;
		case HOLDINGS:
			leader.setData('u', Leader.TYPE);
			break;
		case CLASSIFICATION:
			leader.setData('w', Leader.TYPE);
			break;
		case COMMUNITY:
			leader.setData('q', Leader.TYPE);
			break;
		default:
			break;
		}
		return leader;
	}
	
	private static FixedDataElement getFixedDataElement(ConfigType config){
		FixedDataElement dataElement = new FixedDataElement(config.getLength());
		return dataElement;
	}
}
