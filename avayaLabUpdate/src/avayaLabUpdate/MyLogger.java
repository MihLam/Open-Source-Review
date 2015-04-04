package avayaLabUpdate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger
{

	private static Logger logger;
	private static String TAG; // Project name

	MyLogger(String tag)
	{
		TAG = tag;
		logger = Logger.getLogger(TAG);
		try
		{
			String timeAndDate = (new SimpleDateFormat("_yyyy_MM_dd__HH_mm_ss_")).format(new Date());
			FileHandler fh = new FileHandler(System.getProperty("user.dir") + "\\" + TAG + timeAndDate + ".xml");
			logger.addHandler(fh);

		}
		catch (SecurityException e)
		{
			logger.log(Level.SEVERE, "Failed to create a log file of the security policy.", e);
		}
		catch (IOException e)
		{
			logger.log(Level.SEVERE, "Failed to create a log file because of IO error.", e);
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "Failed to create a log file because of some other error.", e);
		}
	}

	public static void addSevereLog(String str)
	{
		logger.log(Level.SEVERE, str);
	}

	public static void addInfoLog(String str)
	{
		logger.log(Level.INFO, str);
	}

	public static void addWarningLog(String str)
	{
		logger.log(Level.WARNING, str);
	}

}
