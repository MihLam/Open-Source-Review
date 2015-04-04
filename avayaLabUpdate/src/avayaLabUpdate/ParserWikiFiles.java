package avayaLabUpdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserWikiFiles implements Sui
{
	private static final String[] SERVER = { "mngsp", "msg" };
	private static final String TABLE = "{| class=";
	private static final String CLOSE_TABLE = "|}";
	private static final String ETALON_TABLE = "{| class=\"wikitable\" style=\"text-align:center;  font-family:tahoma,geneva,sans-serif;  width:65%\"";
	private static final String VALING_TOP = "|- valign=\"top\"";
	private static final String VALING_BOTTOM = "|- valign=\"bottom\"";
	private static final String VALING_BASELINE = "|- valign=\"baseline\"";
	private static final String VALING_MIDDLE = "|- valign=\"middle\"";
	private static final int WE_WANT_IP = 0;
	private static final int WE_WANT_VERSION = 1;
	/**ArrayList al :
	 * 
	 * 		msg01	Ip		Version
	 * 		[]		[]		[]
	 * 
	 */
	private ArrayList<String> al;

	public ArrayList<String> doParse() throws Exception
	{
		FileInputStream _fis = new FileInputStream(new File(pathToHttpGetFile));
		byte[] buf = new byte[1024 * 1024];
		int r = 0;
		int start = 0;
		int end = 0;
		r = _fis.read(buf);
		String wikiFile = new String(buf, 0, r);
		_fis.close();

		ArrayList<String> alInt = new ArrayList<String>();
		for (int i = 1; i < 100; i++)
		{
			if (i < 10)
				alInt.add("0" + i);
			else
				alInt.add("" + i);
		}

		al = new ArrayList<String>();

		for (int serverCount = 0; serverCount < SERVER.length; serverCount++)
			for (int subnNmber = 0; subnNmber < alInt.size(); subnNmber++)
			{

				int r4 = parseWWW(wikiFile, SERVER[serverCount], alInt.get(subnNmber), WE_WANT_IP);
				if (r4 == -1)
					continue;

				// Looking up numbers (r3 here must point to a string with the IP address of the server)
				while (true)
				{
					if (wikiFile.charAt(r4) == '0' || wikiFile.charAt(r4) == '1' || wikiFile.charAt(r4) == '2' || wikiFile.charAt(r4) == '3'
							|| wikiFile.charAt(r4) == '4' || wikiFile.charAt(r4) == '5' || wikiFile.charAt(r4) == '6' || wikiFile.charAt(r4) == '7'
							|| wikiFile.charAt(r4) == '8' || wikiFile.charAt(r4) == '9')
					{
						start = r4;
						break;
					}
					r4++;
				}

				// Looking up to the first non-numerical value
				while (true)
				{
					if (!(wikiFile.charAt(r4) == '0' || wikiFile.charAt(r4) == '1' || wikiFile.charAt(r4) == '2' || wikiFile.charAt(r4) == '3'
							|| wikiFile.charAt(r4) == '4' || wikiFile.charAt(r4) == '5' || wikiFile.charAt(r4) == '6' || wikiFile.charAt(r4) == '7'
							|| wikiFile.charAt(r4) == '8' || wikiFile.charAt(r4) == '9' || wikiFile.charAt(r4) == '.'))
					{
						end = r4;
						break;
					}
					r4++;
				}

				// If do not fit the pattern
				if (!checkIP(wikiFile.substring(start, end)))
					continue;

				al.add(SERVER[serverCount] + alInt.get(subnNmber));
				al.add(wikiFile.substring(start, end));
				al.add(""); // reserve space for the version of the project.

			}
		MyLogger.addInfoLog("Parse HTML File succeeded");
		return al;
	}

	/**
	 * @param server SERVER[serverCount]    msg/mngsp and other
	 * @param alInt.get(subnNmber)			from 01 to 99
	 * @param what we want IP or Version
	 * @return
	 */
	private int parseWWW(String toParse, String _server, String _subnumber, int r) // parse WHAT WE WANT
	{

		/**
		 * text has the following frame
		 * inaccuracies allowed...
		 * 
		 * === msg07 ===
		 * {| class="wikitable" style="text-align:center;  font-family:tahoma,geneva,sans-serif;  width:65%"
		 * |-
		 * ! style="width: 25%" | hostname
		 * ! style="width: 15%" | ip
		 * ! style="width: 20%" | version
		 * ! style="width: 40%" | details
		 * |- valign="top"
		 * | msg07.example.com<br/>
		 * | 127.0.0.1
		 * | 6.3
		 * |}
		 */
		// The index of the first occurrence, or -1 if not found
		int r2 = toParse.indexOf("=== " + _server + _subnumber + " ===");
		if (r2 == -1)
		{
			r2 = toParse.indexOf("=== " + _server + _subnumber + "<br/> ===");
			if (r2 == -1)
				return -1;
		}

		// Looking up to the first |}
		int r3 = toParse.indexOf(CLOSE_TABLE, r2);
		if (r3 == -1)
			return -1;

		// Loking up to the first VALIGN (by default will Valign = "top")
		int r4 = toParse.lastIndexOf(VALING_TOP, r3);
		if (r4 == -1)
		{
			r4 = toParse.lastIndexOf(VALING_MIDDLE, r3);
			if (r4 == -1)
			{
				r4 = toParse.lastIndexOf(VALING_BASELINE, r3);
				if (r4 == -1)
				{
					r4 = toParse.lastIndexOf(VALING_BOTTOM, r3);
					if (r4 == -1)
					{
						MyLogger.addSevereLog("Cannot find valign of " + _server + _subnumber + " order to find IP ");
						return -1;
					}
				}
			}
		}

		int count;
		switch (r)
		{
			case WE_WANT_IP:
				count = 2;
				while (count > 0)
				{
					r4 = toParse.indexOf("\n", r4);
					r4++;
					count--;
				}
				return r4;
			case WE_WANT_VERSION:
				count = 3;
				while (count > 0)
				{
					r4 = toParse.indexOf("\n", r4);
					r4++;
					count--;
				}
				return r4;
			default:
				MyLogger.addWarningLog("Cannot parse");
				return -1;
		}

	}

	private int parseWWW(String toParse, String server, int r) // parse WHAT WE WANT
	{
		return parseWWW(toParse, server.substring(0, server.length() - 2), server.substring(server.length() - 2, server.length()), r);
	}

	private void toEtalonText(String toEtalon)
	{
		ArrayList<String> alInt = new ArrayList<String>();
		for (int i = 1; i < 100; i++)
		{
			if (i < 10)
				alInt.add("0" + i);
			else
				alInt.add("" + i);
		}

		for (int serverCount = 0; serverCount < SERVER.length; serverCount++)
			for (int subnNmber = 0; subnNmber < alInt.size(); subnNmber++)
			{
				// The index of the first occurrence, or -1 if not found
				int r2 = toEtalon.indexOf("=== " + SERVER[serverCount] + alInt.get(subnNmber) + " ===");
				if (r2 == -1)
					continue;

				// We are looking for before the first table
				int r3 = toEtalon.indexOf(TABLE, r2);
				if (r3 == -1)
					continue;

				// Change to standard
				toEtalon = toEtalon.substring(0, r3) + ETALON_TABLE + toEtalon.substring(toEtalon.indexOf("\n", r3), toEtalon.length());
			}
	}

	private boolean checkIP(String tmp)
	{
		// If a string was returned, say so.
		Pattern p = Pattern.compile(IPV4_PATTERN);
		Matcher m = p.matcher(tmp);
		boolean b = m.matches();
		if ((tmp != null) && (tmp.length() > 0) && b)
			return true;
		return false;
	}

	/**
	 * 
	 * @param _al input mass with "Messaging:"
	 * @throws Exception if everything is bad
	 */
	public void preparePost(ArrayList<String> _al) throws Exception
	{
		al = _al;

		FileInputStream _fis = new FileInputStream(new File(pathToHttpGetFile));
		byte[] buf = new byte[1024 * 1024];
		int r = 0;
		r = _fis.read(buf);
		String wikiFile = new String(buf, 0, r);
		_fis.close();

		for (int i = 0; i < al.size(); i += 3)
		{

			int r5 = parseWWW(wikiFile, al.get(i), WE_WANT_VERSION);
			r5 = wikiFile.indexOf("|", r5);
			// delete prevent "Messaging:"
			int r6 = wikiFile.indexOf("\n", r5);
			if (r5 == -1 || r6 == -1)
			{
				MyLogger.addInfoLog("Could not add Messaging to " + al.get(i));
				continue;
			}
			r5++;
			wikiFile = wikiFile.substring(0, r5) + " " + al.get(i + 2) + wikiFile.substring(r6, wikiFile.length());
		}
		toEtalonText(wikiFile);
		FileOutputStream _fos = new FileOutputStream(new File(pathToHttpGetFile));
		_fos.write(wikiFile.getBytes(), 0, wikiFile.length());
		_fos.close();

	}
}
