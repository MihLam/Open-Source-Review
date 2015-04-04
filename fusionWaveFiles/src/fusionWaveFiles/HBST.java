package fusionWaveFiles;

import java.io.File;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HBST
{
	static final String NAME_OF_ARRAY_WAVE = System.getProperty("user.dir") + "\\arrayConfig.xlsx";
	static final String NAME_OF_CONFIG = System.getProperty("user.dir") + "\\generalConfig.xlsx";
	CoupleArray _ca;
	WaveFusion toFusion = null;

	public void DoIt()
	{

		_ca = new CoupleArray();

		ParseEXEL pExel = new ParseEXEL();
		if (!new File(NAME_OF_ARRAY_WAVE).exists())
		{
			TestWaveFile.logger.log(Level.SEVERE, "File not found :" + NAME_OF_ARRAY_WAVE);
			return;
		}
		if (!new File(NAME_OF_CONFIG).exists())
		{
			TestWaveFile.logger.log(Level.SEVERE, "File not found :" + NAME_OF_CONFIG);
			return;
		}

		_ca = pExel.parsFileName(NAME_OF_ARRAY_WAVE);
		_ca = pExel.parsConfigFile(NAME_OF_CONFIG, _ca);
		toFusion = new WaveFusion();
		if (_ca.getNumberC2() == null)
			_ca.setNumberC2("default");

		// block of parse how much language
		try
		{
			Pattern pat = Pattern.compile("(\\d+)");
			Matcher matcher;
			matcher = pat.matcher(_ca.getHowMuchLanguage());
			if (matcher.find())
			{
				_ca.setWhatLanguageShouldDo(Integer.parseInt(matcher.group(1)) - 2); // NumberCell - 2 =
																						// numberOfLanguage
																						// in a array
				TestWaveFile.logger.log(Level.INFO, "LANGUAGE FOR DO IS " + _ca.getWhatLanguageShouldDo());
			}
			else
			{
				TestWaveFile.logger.log(Level.INFO, "TEMPLATE FOR LANGUAGE IS " + _ca.getHowMuchLanguage());
			}
		}
		catch (Exception e)
		{
			TestWaveFile.logger.log(Level.INFO, "TEMPLATE FOR LANGUAGE IS " + _ca.getHowMuchLanguage());
		}

		/**
		 * switch to CopleArray::numberC2
		 * 
		 * 1:
		 * null or default - default firstname+"_"+secondname
		 * 
		 * 2:
		 * YearDay(*) - firstFile is Day, second month  > YearDay(count) without 29feb and other
		 * 
		 * 3:
		 * str1 + dollar($)/grid(#) + str2 + grid(#)/dollar($) + str3
		 * if it shoud be parsing:  $ - first column number
		 * 							# - second column number 
		 * everything else may be another
		 * 
		 *4:
		 * add categories to switch what you want
		 */

		if (!_ca.getNumberC2().equals("YearDay(*)") && !_ca.getNumberC2().equals("default")) // should parse
		{

			boolean gridIs = false;
			boolean dollarIs = false;
			StringBuffer sb = new StringBuffer(255);
			sb.append(_ca.getNumberC2());
			_ca.setNumberC2("expression");
			int i = 0;
			int last = 0;
			// Parse String
			while (i < sb.length())
			{
				if ((sb.charAt(i) == '$') && (!dollarIs))
				{
					dollarIs = true;
					if (!gridIs)
					{
						_ca.setExpStr1(sb.substring(last, i));
						last = i + 1;
						_ca.setDollarWasFirst(true);
					}
					else
					{
						_ca.setExpStr2(sb.substring(last, i));
						last = i + 1;
						_ca.setExpStr3(sb.substring(last, sb.length()));
						break;
					}
				}
				if ((sb.charAt(i) == '#') && (!gridIs))
				{
					gridIs = true;
					if (!dollarIs)
					{
						_ca.setExpStr1(sb.substring(last, i));
						last = i + 1;
						_ca.setDollarWasFirst(false);
					}
					else
					{
						_ca.setExpStr2(sb.substring(last, i));
						last = i + 1;
						_ca.setExpStr3(sb.substring(last, sb.length()));
						break;
					}
				}
				i++;
			}

			// parse int
			Pattern pat = Pattern.compile("(\\d+)");
			Matcher matcher;

			i = 0;
			while (i < _ca.getFirstArrayList().size())
			{
				try
				{
					matcher = pat.matcher(_ca.getFirstArrayList().get(i));
					if (matcher.find())
						_ca.getDollarNum().add(Integer.parseInt(matcher.group(1)));
					i++;
				}
				catch (Exception e)
				{
					i++;
					TestWaveFile.logger.log(Level.WARNING, "Error in parse first array" + e);
					continue;
				}
			}
			i = 0;
			while (i < _ca.getSecondArrayList().size())
			{
				try
				{
					matcher = pat.matcher(_ca.getFirstArrayList().get(i));
					if (matcher.find())
						_ca.getGridNum().add(Integer.parseInt(matcher.group(1)));
					i++;
				}
				catch (Exception e)
				{
					i++;
					TestWaveFile.logger.log(Level.WARNING, "Error in parse second array" + e);
					continue;
				}
			}
		}

		if (_ca.getHowMuchLanguage().equals("ALL")) // If all language do all
		{
			for (int language = 0; language < _ca.getLanguageInput().size(); language++)
			{
				doIt(language);
			}
		}
		else if (_ca.getWhatLanguageShouldDo() >= 0)
			doIt(_ca.getWhatLanguageShouldDo());
	}

	private void doIt(int language)
	{
		int countErr = 0;
		switch (_ca.getNumberC2())
		{
			case "expression":
				for (int i = 0; i < _ca.getFirstArrayList().size(); i++)
					for (int j = 0; j < _ca.getSecondArrayList().size(); j++)
					{
						try
						{
							toFusion.makeFusion(_ca.getName(_ca.getFirstArrayList().get(i), language),
									_ca.getName(_ca.getSecondArrayList().get(j), language), _ca.getoutputNameExp(i, j, language));
						}
						catch (Exception e)
						{
							countErr++;
							TestWaveFile.logger.log(Level.WARNING, "Unable to create file" + _ca.getoutputNameExp(i, j, language) + e.toString());
						}
					}
				break;
			case "YearDay(*)":
				int countOfDate = 1;
				for (int month = 1; month < 13; month++)
					for (int day = 1; day < 32; day++)
						try
						{
							if ((day == 30 && (month == 4 || month == 6 || month == 8 || month == 11)) || (month == 2 && day > 28))
								continue;
							toFusion.makeFusion(_ca.getName(_ca.getFirstArrayList().get(day - 1), language),
									_ca.getName(_ca.getSecondArrayList().get(month - 1), language),
									_ca.getOutputName("YearDay(" + countOfDate + ")", language));
							countOfDate++;
						}
						catch (Exception e)
						{
							countOfDate++;
							countErr++;
							TestWaveFile.logger.log(Level.WARNING,
									"Unable to create file:" + _ca.getOutputName("YearDay(" + countOfDate + ")", language) + e.toString());
						}
				try
				{
					// Try to set yearDay(366) = 29 feb
					toFusion.makeFusion(_ca.getName(_ca.getFirstArrayList().get(28), language),
							_ca.getName(_ca.getSecondArrayList().get(1), language), _ca.getOutputName("YearDay(" + countOfDate + ")", language));
				}
				catch (Exception e)
				{
					countErr++;
					TestWaveFile.logger.log(Level.WARNING,
							"Unable to create file:" + _ca.getOutputName("YearDay(" + countOfDate + ")", language) + e.toString());
				}

				break;
			default:
				for (int i = 0; i < _ca.getFirstArrayList().size(); i++)
					for (int j = 0; j < _ca.getSecondArrayList().size(); j++)
					{
						try
						{
							toFusion.makeFusion(_ca.getName(_ca.getFirstArrayList().get(i), language),
									_ca.getName(_ca.getSecondArrayList().get(j), language),
									_ca.getOutputName(_ca.getFirstArrayList().get(i) + "_" + _ca.getSecondArrayList().get(j), language));
						}
						catch (Exception e)
						{
							countErr++;
							TestWaveFile.logger.log(
									Level.WARNING,
									"Unable to create file"
											+ _ca.getOutputName(_ca.getFirstArrayList().get(i) + "_" + _ca.getSecondArrayList().get(j), language)
											+ e.toString());
						}
					}
				break;
		}

		TestWaveFile.logger.log(Level.INFO, "Count Of Error: " + countErr);
	}
}
