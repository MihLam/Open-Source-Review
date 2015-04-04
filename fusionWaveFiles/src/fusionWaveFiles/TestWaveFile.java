package fusionWaveFiles;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestWaveFile
{

	public static Logger logger = null;

	public static void main(String[] args)
	{
		logger = Logger.getLogger(TestWaveFile.class.getName());
		try
		{
			String timeAndDate = (new SimpleDateFormat("_yyyy_MM_dd__HH_mm_ss_")).format(new Date());
			FileHandler fh = new FileHandler(System.getProperty("user.dir") + "\\logFileWaveFusion" + timeAndDate + ".xml");
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

		try
		{
			HBST hbst = new HBST();
			hbst.DoIt();
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "Fatal error in hbst.DoIt(). Contact with Administrator." + e);
		}
	}

}
