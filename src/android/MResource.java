package com.renlianiot.music;
public class MResource {
	public static int getIdByName(String packageName, String className, String resName) {
		int id = 0;
		try {
			Class r = Class.forName(packageName + ".R");
			Class[] classes = r.getClasses();
			Class desireClass = null;
			for (Class cls : classes) {
				if (cls.getName().split("\\$")[1].equals(className)) {
				desireClass = cls;
				break;
				}
			}
			if (desireClass != null) {
				id = desireClass.getField(resName).getInt(desireClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
}