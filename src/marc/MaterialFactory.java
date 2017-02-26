package marc;

import java.util.Arrays;
import java.util.HashMap;

import marc.field.FixedField;
import marc.material.AbstractMaterial;
import marc.material.ElectronicResource;
import marc.material.Globe;
import marc.material.Kit;
import marc.material.Map;
import marc.material.Microform;
import marc.material.MotionPicture;
import marc.material.NonprojectedGraphic;
import marc.material.NotatedMusic;
import marc.material.ProjectedMaterial;
import marc.material.RemoteSensingImage;
import marc.material.SoundRecording;
import marc.material.TactileMaterial;
import marc.material.Text;
import marc.material.UnknownMaterial;
import marc.material.Unspecified;
import marc.material.VideoRecording;

public final class MaterialFactory {
	private static final HashMap<String, Class<? extends AbstractMaterial>> mapMaterial = buildMaterialMap();
	private static final Class<? extends AbstractMaterial> UNKNOWN_MATERIAL = UnknownMaterial.class;
	
	private static final HashMap<String, Class<? extends AbstractMaterial>> buildMaterialMap(){
		HashMap<String, Class<? extends AbstractMaterial>> tmp = new HashMap<String, Class<? extends AbstractMaterial>>();
		tmp.put("a", Map.class);
		tmp.put("c", ElectronicResource.class);
		tmp.put("d", Globe.class);
		tmp.put("f", TactileMaterial.class);
		tmp.put("g", ProjectedMaterial.class);
		tmp.put("h", Microform.class);
		tmp.put("k", NonprojectedGraphic.class);
		tmp.put("m", MotionPicture.class);
		tmp.put("o", Kit.class);
		tmp.put("q", NotatedMusic.class);
		tmp.put("r", RemoteSensingImage.class);
		tmp.put("s", SoundRecording.class);
		tmp.put("t", Text.class);
		tmp.put("v", VideoRecording.class);
		tmp.put("z", Unspecified.class);
		return tmp;
	}
	
	public static final AbstractMaterial getMaterial(FixedField field){
		char[] key = null;
		Class<? extends AbstractMaterial> c = UNKNOWN_MATERIAL;
		if (field.getTag().equals("007")){
			key = Arrays.copyOfRange(field.getFieldData(), 0, 1);
			c = mapMaterial.getOrDefault(String.valueOf(key), UNKNOWN_MATERIAL);
		}
		AbstractMaterial object = new UnknownMaterial();
		try {
			object = c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return object;
	}
}
