package fusionWaveFiles;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.logging.Level;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParseEXEL
{

	/**
	 * 
	 * @param fileName
	 * @param Array for update
	 * @return	updated array object
	 */
	public CoupleArray parsConfigFile(String fileName, CoupleArray ca)
	{
		CoupleArray _cp = ca;
		try
		{
			boolean FirstAr = true; // To split on arrays
			boolean firstIter = true; // To separate
			int whatGoNow = 0; // To split on columns
			boolean c2IsFull = false; // If true, it will no longer be exposed to case 3 so as not to rub s2
			FileInputStream file = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				FirstAr = true;
				whatGoNow = 1; // go first colum
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();

					switch (whatGoNow)
					{
						case 1:
							if (firstIter)
								_cp.setDirInput(cell.getStringCellValue());
							else if (FirstAr)
								_cp.getLanguageInput().add(cell.getStringCellValue());
							else
								_cp.getLanguageOutput().add(cell.getStringCellValue());
							whatGoNow = 2;
							break;
						case 2:
							if (firstIter)
								_cp.setDirOutput(cell.getStringCellValue());
							else if (FirstAr)
								_cp.getLanguageInput().add(cell.getStringCellValue());
							else
								_cp.getLanguageOutput().add(cell.getStringCellValue());
							whatGoNow = 3;
							break;
						case 3:
							if (!c2IsFull)
								if (firstIter)
								{
									if (cell.getCellType() == Cell.CELL_TYPE_STRING)
										_cp.setHowMuchLanguage(cell.getStringCellValue());
									else
										_cp.setHowMuchLanguage("" + cell.getNumericCellValue());
								}
								else
								{
									_cp.setGeneralConfigFieldC2(cell.getStringCellValue());
									c2IsFull = true;
								}
							whatGoNow = 0;
							break;
						default:
							TestWaveFile.logger.log(Level.WARNING, "Wrong param of 'whatGoNow'=" + whatGoNow + " in parse" + fileName);
							break;
					}

					FirstAr = false;
				}
				firstIter = false;
			}
			workbook.close();
			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return _cp;
	}

	/**
	 * 
	 * @param fileName
	 * @return	new generated array object
	 */
	public CoupleArray parsFileName(String fileName)
	{
		CoupleArray _cp = new CoupleArray();
		try
		{
			boolean FirstAr = true; // To split on arrays
			boolean firstIter = true; // To separate
			int whatGoNow = 0; // To split on columns
			boolean c2IsFull = false; // If true, it will no longer be exposed to case 3 so as not to rub s2
			FileInputStream file = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				FirstAr = true;
				whatGoNow = 1; // go first colum
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();

					switch (whatGoNow)
					{
						case 1:
							if (FirstAr)
								_cp.getFirstArrayList().add(cell.getStringCellValue());
							else
								_cp.getSecondArrayList().add(cell.getStringCellValue());
							whatGoNow = 2;
							break;
						case 2:
							if (FirstAr)
								_cp.getFirstArrayList().add(cell.getStringCellValue());
							else
								_cp.getSecondArrayList().add(cell.getStringCellValue());
							whatGoNow = 3;
							break;
						case 3:
							if (!c2IsFull)
								if (firstIter)
									_cp.setNumberC1(cell.getStringCellValue());
								else
								{
									_cp.setNumberC2(cell.getStringCellValue());
									c2IsFull = true;
								}
							whatGoNow = 0;
							break;
						default:
							TestWaveFile.logger.log(Level.WARNING, "Wrong param of 'whatGoNow'=" + whatGoNow + " in parsFileName");
							break;
					}

					FirstAr = false;
				}
				firstIter = false;
			}
			workbook.close();
			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return _cp;
	}
}
