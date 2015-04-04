package avayaLabUpdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ParserForMessaging implements Sui
{
	private static final String subCommandLine = "> swversion | grep \"Messaging:\"";
	private static final String subCommandLine2 = "$ swversion | grep \"Messaging:\"";
	private static final String subCommandLine3 = "# swversion | grep \"Messaging:\"";

	public String doParse(String server)
	{
		String toRet = null;
		File _f = null;
		try
		{
			_f = new File(pathToTmpFile + server + ".txt");
			if (!_f.exists())
				throw new FileNotFoundException();

			String s = "";
			Scanner in = new Scanner(_f);
			while (in.hasNext())
				s += in.nextLine() + '\n';
			in.close();

			int firstIndex = s.indexOf(subCommandLine);
			if (firstIndex == -1)
				firstIndex = s.indexOf(subCommandLine2);
			if (firstIndex == -1)
				firstIndex = s.indexOf(subCommandLine3);
			int start = firstIndex;
			while (s.charAt(start) != '\n')
				start++;
			start++;

			int end = start;

			while (s.charAt(end) != '\n')
				end++;
			toRet = s.substring(start, end);

			toRet = withoutSpace(toRet);

			MyLogger.addInfoLog("Parse message version succeeded");
			return toRet;
		}
		catch (Exception e)
		{
			MyLogger.addWarningLog("Exception in ParseForMessaging; Exception: " + e);
			return null;
		}
		finally
		{
			_f.delete();
		}
	}

	private String withoutSpace(String tmp)
	{
		tmp = tmp.replaceAll("\t", "<br />");
		int r1 = 0;
		while (tmp.charAt(r1) == ' ')
			r1++;

		return tmp.substring(r1);
	}
}
