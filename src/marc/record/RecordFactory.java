package marc.record;

import java.time.LocalDate;

import marc.Factory;
import marc.MARC;
import marc.field.FixedDataElement;
import marc.field.Leader;
import marc.type.AbstractRecordType;
import marc.type.RecordType;

public final class RecordFactory {
	public static final Record generate(RecordType type){
		Leader leader = getLeader(type);
		AbstractRecordType t = Factory.getMaterialConfig(leader);
		FixedDataElement dataElement = getFixedDataElement(t);
		
		Record record = new Record();
		record.setLeader(leader);
		record.setFixedDataElement(dataElement);
		record.setEntryDate(LocalDate.now(MARC.TIME_ZONE));
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
	
	private static FixedDataElement getFixedDataElement(AbstractRecordType type){
		FixedDataElement dataElement = new FixedDataElement(type.getConfigLength());
		return dataElement;
	}
}
