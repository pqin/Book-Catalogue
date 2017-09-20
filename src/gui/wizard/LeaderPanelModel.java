package gui.wizard;

import java.util.EnumMap;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import marc.field.Leader;
import marc.record.RecordFactory;
import marc.type.RecordType;

public class LeaderPanelModel {
	private static final EnumMap<RecordType, char[]> initialLeaderValue;
	static {
		initialLeaderValue = new EnumMap<RecordType, char[]>(RecordType.class);
		RecordType[] t = RecordType.values();
		for (int i = 0; i < t.length; ++i){
			char[] data = RecordFactory.generate(t[i]).getLeader().getFieldData();
			initialLeaderValue.put(t[i], data);
		}
	}
	
	private Leader leader;
	private RecordType recordType;
	private EnumMap<RecordType, CharMapComboBoxModel> typeCache;
	private EnumMap<RecordType, CharMapComboBoxModel> levelCache;
	private JComboBox<Character> typeBox, levelBox;
	
	public LeaderPanelModel(){
		leader = new Leader();
		typeCache = new EnumMap<RecordType, CharMapComboBoxModel>(RecordType.class);
		levelCache = new EnumMap<RecordType, CharMapComboBoxModel>(RecordType.class);
		typeBox = null;
		levelBox = null;
		reset();
	}
	
	public void reset(){
		recordType = RecordType.BIBLIOGRAPHIC;
		leader.setFieldData(initialLeaderValue.get(recordType));
		updateModels();
	}
	public void setTypeComboBox(JComboBox<Character> box){
		typeBox = box;
	}
	public void setLevelComboBox(JComboBox<Character> box){
		levelBox = box;
	}
	public void setRecordType(RecordType type){
		recordType = type;
		leader.setFieldData(initialLeaderValue.get(recordType));
		updateModels();
	}
	public Leader getLeader(){
		return leader;
	}
	public void setType(char type){
		leader.setData(type, Leader.TYPE);
	}
	public void setLevel(char level){
		switch (recordType){
		case BIBLIOGRAPHIC:
			leader.setData(level, 7);
			break;
		case HOLDINGS:
			leader.setData(level, 17);
			break;
		default:
			break;
		}
	}
	private void updateModels(){
		updateJComboBox(getTypeModel(recordType), typeBox);
		updateJComboBox(getLevelModel(recordType), levelBox);
	}
	private void updateJComboBox(ComboBoxModel<Character> model, JComboBox<Character> box){
		if (box != null){
			box.setModel(model);
			if (model.getSize() > 1){
				box.setEnabled(true);
				box.setSelectedIndex(0);
			} else {
				box.setEnabled(false);
			}
		}
	}
	
	private CharMapComboBoxModel getTypeModel(RecordType type){
		CharMapComboBoxModel model = null;
		if (typeCache.containsKey(type)){
			model = typeCache.get(type);
		} else {
			model = new CharMapComboBoxModel();
			switch (type){
			case BIBLIOGRAPHIC:
				model.addElement('a', "Language material");
				model.addElement('c', "Notated music");
				model.addElement('d', "Manuscript notated music");
				model.addElement('e', "Cartographic material");
				model.addElement('f', "Manuscript cartographic material");
				model.addElement('g', "Projected medium");
				model.addElement('i', "Nonmusical sound recording");
				model.addElement('j', "Musical sound recording");
				model.addElement('k', "2-D nonprojectable graphic");
				model.addElement('m', "Computer file");
				model.addElement('o', "Kit");
				model.addElement('p', "Mixed materials");
				model.addElement('r', "3-D artifact or naturally occurring object");
				model.addElement('t', "Manuscript language material");
				break;
			case AUTHORITY:
				model.addElement('z', "Authority data");
				break;
			case HOLDINGS:
				model.addElement('u', "Unknown");
				model.addElement('v', "Multipart item");
				model.addElement('x', "Single-part item");
				model.addElement('y', "Serial item");
				break;
			case CLASSIFICATION:
				model.addElement('w', "Classification data");
				break;
			case COMMUNITY:
				model.addElement('q', "Community information");
				break;
			default:
				break;
			}
			if (model.getSize() > 0){
				typeCache.put(type, model);
			}
		}
		return model;
	}
	private CharMapComboBoxModel getLevelModel(RecordType type){
		CharMapComboBoxModel model = null;
		if (levelCache.containsKey(type)){
			model = levelCache.get(type);
		} else {
			model = new CharMapComboBoxModel();
			switch (type){
			case BIBLIOGRAPHIC:
				model.addElement('a', "Monographic component part");
				model.addElement('b', "Serial component part");
				model.addElement('c', "Collection");
				model.addElement('d', "Subunit");
				model.addElement('i', "Integrating resource");
				model.addElement('m', "Monograph/Item");
				model.addElement('s', "Serial");
				break;
			case AUTHORITY:
				model.addElement(' ', "Undefined");
				break;
			case HOLDINGS:
				model.addElement('1', "Holdings level 1");
				model.addElement('2', "Holdings level 2");
				model.addElement('3', "Holdings level 3");
				model.addElement('4', "Holdings level 4");
				model.addElement('5', "Holdings level 4 with piece designation");
				model.addElement('m', "Mixed level");
				model.addElement('u', "Unknown");
				model.addElement('z', "Other level");
				break;
			case CLASSIFICATION:
				model.addElement(' ', "Undefined");
				break;
			case COMMUNITY:
				model.addElement(' ', "Undefined");
				break;
			default:
				break;
			}
			if (model.getSize() > 0){
				levelCache.put(type, model);
			}
		}
		return model;
	}
}
