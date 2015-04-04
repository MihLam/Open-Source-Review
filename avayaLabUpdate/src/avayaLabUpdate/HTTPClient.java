package avayaLabUpdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class HTTPClient implements Sui
{
	private static final String WIKI_DOMAIN_NAME = "wiki.buuc.merann.ru/mediawiki";
	private static final String SCRIPT_PATH = "";
	private static final String AVAYALAB_TITLE = "AAM:Lab";

	private static final String BOT_NAME = "";
	private static final String BOT_PASSWORD = "";

	public void newGetQuery() throws Exception
	{
		try
		{
			Wiki getWiki = null;
			getWiki = login();
			if (getWiki == null)
			{
				MyLogger.addSevereLog("Cannot login in newGetQuery()");
				return;
			}
			String str = getWiki.getPageText(AVAYALAB_TITLE);

			FileOutputStream _fos = new FileOutputStream(new File(pathToHttpGetFile));
			_fos.write(str.getBytes(), 0, str.length());
			_fos.close();

			MyLogger.addInfoLog("Succeeded get query");
		}
		catch (Exception e)
		{
			MyLogger.addSevereLog("Failed get query: " + e);
			throw new Exception("Failed get query: " + e);
		}
	}

	private Wiki login()
	{
		Wiki wiki = new Wiki(WIKI_DOMAIN_NAME, SCRIPT_PATH);
		wiki.setUsingCompressedRequests(false);
		try
		{
			wiki.login(BOT_NAME, BOT_PASSWORD);
		}
		catch (Exception e)
		{
			return null;
		}
		return wiki;
	}

	public void newPostQuery() throws Exception
	{
		Wiki postWiki = null;
		postWiki = login();
		if (postWiki == null)
		{
			MyLogger.addSevereLog("Cannot login in newPostQuery()");
			return;
		}

		StringBuffer sb = new StringBuffer();
		FileInputStream _fis = null;

		try
		{
			_fis = new FileInputStream(new File(pathToHttpGetFile));

			byte[] buf = new byte[256];
			int r = 1;
			while (r > 0)
			{
				r = _fis.read(buf);

				if (r > 0)
					sb.append(new String(buf, 0, r));
			}
			_fis.close();
		}
		catch (Exception e)
		{
			MyLogger.addWarningLog("Bug when working with file in PostQuery" + e);
			throw new Exception("Bug when working with file in PostQuery" + e);

		}
		try
		{
			postWiki.edit("AAM:Lab_test", sb.toString(), "Auto-update for AAM");
		}
		catch (Exception e)
		{
			MyLogger.addSevereLog("Error edit info in PostQuery" + e);
			throw new Exception("Error edit info in PostQuery" + e);
		}
	}
}
