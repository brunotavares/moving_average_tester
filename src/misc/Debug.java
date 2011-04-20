package misc;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {

	private static SimpleDateFormat timeFormat = new SimpleDateFormat("H:m:s.S");
	
	public static String getTime()
	{
		return timeFormat.format(new Date());
	}
}
