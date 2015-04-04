package fusionWaveFiles;

import java.util.ArrayList;

public class CoupleArray
{
	// Frist file config generalConfig.xls
	private ArrayList<String> languageInput;
	private ArrayList<String> languageOutput;
	private String dirInput;
	private String dirOutput;
	private String howMuchLanguage; // Switch what we want - C1 //ALL ; ALL smt1 ; ALL smt2; ALL smt1 smt2; (Everything
	private int whatLanguageShouldDo; // is written with a space)
										// Or what language goes in a row. One number!!!
	private String generalConfigFieldC2; // RESERVE - C2

	// Second file config arrayConfig.xls
	private ArrayList<String> firstArrayList;
	private ArrayList<String> secondArrayList;
	private String numberC1; // RESERVE
	private String numberC2; // Switch what we want

	private String expStr1;
	private String expStr2;
	private String expStr3;
	private boolean dollarWasFirst = true;
	private ArrayList<Integer> dollarNum;
	private ArrayList<Integer> gridNum;

	private final static String dotWave = ".wav";

	String getoutputNameExp(int fNum, int sNum, int numOfLanguage) throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		if (isDollarWasFirst())
			return getDirOutput() + getLanguageOutput().get(numOfLanguage) + getExpStr1() + getDollarNum().get(fNum) + getExpStr2()
					+ getGridNum().get(sNum) + getExpStr3() + dotWave;
		else
			return getDirOutput() + getLanguageOutput().get(numOfLanguage) + getExpStr1() + getGridNum().get(sNum) + getExpStr2()
					+ getDollarNum().get(fNum) + getExpStr3() + dotWave;
	}

	String getName(String str, int numOfLanguage) throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		return getDirInput() + getLanguageInput().get(numOfLanguage) + str + dotWave;
	}

	String getOutputName(String str, int numOfLanguage) throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		return getDirOutput() + getLanguageOutput().get(numOfLanguage) + str + dotWave;
	}

	public ArrayList<String> getLanguageInput()
	{
		return languageInput;
	}

	public void setLanguageInput(ArrayList<String> languageInput)
	{
		this.languageInput = languageInput;
	}

	public ArrayList<String> getLanguageOutput()
	{
		return languageOutput;
	}

	public void setLanguageOutput(ArrayList<String> languageOutput)
	{
		this.languageOutput = languageOutput;
	}

	public String getDirInput()
	{
		return dirInput;
	}

	public void setDirInput(String dirInput)
	{
		this.dirInput = dirInput;
	}

	public String getDirOutput()
	{
		return dirOutput;
	}

	public void setDirOutput(String dirOutput)
	{
		this.dirOutput = dirOutput;
	}

	public String getHowMuchLanguage()
	{
		return howMuchLanguage;
	}

	public void setHowMuchLanguage(String howMuchLanguage)
	{
		this.howMuchLanguage = howMuchLanguage;
	}

	public int getWhatLanguageShouldDo()
	{
		return whatLanguageShouldDo;
	}

	public void setWhatLanguageShouldDo(int whatLanguageShouldDo)
	{
		this.whatLanguageShouldDo = whatLanguageShouldDo;
	}

	public String getGeneralConfigFieldC2()
	{
		return generalConfigFieldC2;
	}

	public void setGeneralConfigFieldC2(String generalConfigFieldC2)
	{
		this.generalConfigFieldC2 = generalConfigFieldC2;
	}

	public ArrayList<String> getFirstArrayList()
	{
		return firstArrayList;
	}

	public void setFirstArrayList(ArrayList<String> firstArrayList)
	{
		this.firstArrayList = firstArrayList;
	}

	public ArrayList<String> getSecondArrayList()
	{
		return secondArrayList;
	}

	public void setSecondArrayList(ArrayList<String> secondArrayList)
	{
		this.secondArrayList = secondArrayList;
	}

	public String getNumberC1()
	{
		return numberC1;
	}

	public void setNumberC1(String numberC1)
	{
		this.numberC1 = numberC1;
	}

	public String getNumberC2()
	{
		return numberC2;
	}

	public void setNumberC2(String numberC2)
	{
		this.numberC2 = numberC2;
	}

	public String getExpStr1()
	{
		return expStr1;
	}

	public void setExpStr1(String expStr1)
	{
		this.expStr1 = expStr1;
	}

	public String getExpStr2()
	{
		return expStr2;
	}

	public void setExpStr2(String expStr2)
	{
		this.expStr2 = expStr2;
	}

	public String getExpStr3()
	{
		return expStr3;
	}

	public void setExpStr3(String expStr3)
	{
		this.expStr3 = expStr3;
	}

	public boolean isDollarWasFirst()
	{
		return dollarWasFirst;
	}

	public void setDollarWasFirst(boolean dollarWasFirst)
	{
		this.dollarWasFirst = dollarWasFirst;
	}

	public ArrayList<Integer> getDollarNum()
	{
		return dollarNum;
	}

	public void setDollarNum(ArrayList<Integer> dollarNum)
	{
		this.dollarNum = dollarNum;
	}

	public ArrayList<Integer> getGridNum()
	{
		return gridNum;
	}

	public void setGridNum(ArrayList<Integer> gridNum)
	{
		this.gridNum = gridNum;
	}
}
