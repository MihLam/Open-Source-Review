package avayaLabUpdate;

import java.util.ArrayList;
import java.util.List;

public class MakeAvayaLabUpdate implements Sui
{
	/**
	 * 0. one clickto:		MakeAvayaLabUpdate.java
	 * 
	 * 1. GetHTTP query		HTTPClient.java 
	 * 2. Parse HTTPFile	ParserWikiFiles.java
	 * loop:
	 * 3. get Version		Shell.java
	 * 3.1 parse output		ParserForMessaging.java
	 * endloop
	 * 
	 * 3.99 prepare html 	ParserWikiFiles.java
	 * for post 
	 * 4. postHttp query	HTTPClient.java
	 * 
	 */

	ArrayList<String> al = null;
	int i = 0;

	public void makeIt()
	{

		boolean first = true;
		while (true)
		{
			if (!first)
				al.clear();
			first = false;

			try
			{
				// 1. GetHTTP querry
				new HTTPClient().newGetQuery();

				// 2. Parse HTTPFile
				ParserWikiFiles pHTML = new ParserWikiFiles();
				al = pHTML.doParse();

				// loop

				List<Thread> threads = new ArrayList<Thread>();

				for (i = 0; i < al.size(); i += 3)
				{
					try
					{

						// Every query in different thread
						threads.add(new Thread(new Runnable()
						{
							{// instance initializers
								iThread = i;

							}
							private int iThread = 0;

							@Override
							public void run()
							{
								// 3. get Version
								try
								{
									Shell sh = new Shell();
									sh.getSwversion(al.get(iThread + 1), al.get(iThread));
								}
								catch (Exception e)
								{
									MyLogger.addSevereLog("ERROR: " + al.get(iThread) + "\t" + al.get(iThread + 1) + " " + e);
									return;
								}

								ParserForMessaging pfm = new ParserForMessaging();
								// 3.1 parse output
								al.set(iThread + 2, pfm.doParse(al.get(iThread)));
								return;
							}
						}));
						threads.get(i / 3).start();

					}
					catch (Exception e)
					{
						MyLogger.addSevereLog("ERROR: " + al.get(i) + "\t" + al.get(i + 1) + " " + e);
						continue;
					}

				}// endloop

				for (int ii = 0; ii < threads.size(); ii++)
				{
					threads.get(ii).join();
				}

				for (int i = 0; i < al.size(); i += 3)
				{
					if (al.get(i + 2).equals("") || al.get(i + 2).equals(null))
					{
						al.remove(i + 2);
						al.remove(i + 1);
						al.remove(i);
						i += -3;
						continue;
					}
				}

				// PrePostQuery
				pHTML.preparePost(al);

				// Post HTTP query
				new HTTPClient().newPostQuery();
			}
			catch (Exception e)
			{
				MyLogger.addSevereLog("ERROR: " + e);
			}
			break;
		}
	}
}
