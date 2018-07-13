package deng.longer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String getToday(String type){
		Date d=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		return sdf.format(d);
	}

}
