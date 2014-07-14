package ro.pub.cs.pdsd.laborator05.cartoons.utils;

import java.lang.reflect.Field;

import android.util.Log;
import ro.pub.cs.pdsd.laborator05.cartoons.R;

public class PictureFinder {
	
	public static int findPictureByName(String pictureName) {
		try {
			Class<?> RDrawableClass = (new R.drawable()).getClass();
			Field[] fields = RDrawableClass.getFields();
			for (Field field : fields) {
				if (field.getName().equals(pictureName))
					return field.getInt(R.drawable.class);
			}
		} catch (Exception exception) {
			Log.println(Log.ERROR, "error", exception.getMessage());
		}
		return -1;
	}

}
