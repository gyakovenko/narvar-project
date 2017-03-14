package helpers;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.log4j.*;

import helpers.exceptions.*;

/**
 * DataHelper Class to handle reading data from different sources.
 */
public class DataHelper {

	private static Logger logger = Logger.getLogger(DataHelper.class);

	/**
	 * displays the data passed in a 2-d object array
	 *
	 * @param data
	 *            array whose data is it be displayed
	 */
	public static void displayData(Object[][] data) {
		// Display the data Object[][] to console using the logger.
		for (int i = 0; i < data.length; i++) {
			String dataLine = "";
			for (int j = 0; j < data[i].length; j++) {
				dataLine += data[i][j];
				dataLine += "  \t";
			}
			logger.info(dataLine.trim());
		}
	}

	/**
	 * Method to read text file of various formats and also allows DataTypes to
	 * be specified
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param dataTypes
	 * @return
	 */
	public static Object[][] getTextCSVFileData(String fileLocation, String fileName, DataType[] dataTypes) {
		Object[][] data;
		ArrayList<String> lines = openFileAndCollectData(fileLocation, fileName);
		data = parseCSVData(lines, dataTypes);
		return data;
	}

	public static Object[][] joinData(Object[][]... data) {
		Object[][] newData = new Object[][] { {} };
		for (int i = 0; i < data.length; i++) {
			newData = DataHelper.joinData(newData, data[i]);
		}
		return newData;
	}

	/**
	 * Private method to convert data based on supplied DataType.
	 *
	 * @param parameter
	 * @param dataType
	 * @return
	 * @throws BooleanFormatException
	 * @throws CharacterCountFormatException
	 */
	private static Object convertDataType(String parameter, DataType dataType)
			throws BooleanFormatException, CharacterCountFormatException {
		Object data = null;
		try {
			switch (dataType) {
			case STRING:
				data = parameter;
				break;
			case CHAR:
				if (parameter.length() > 1) {
					throw new CharacterCountFormatException();
				}
				data = parameter.charAt(0);
				break;
			case DOUBLE:
				data = Double.parseDouble(parameter);
				break;
			case FLOAT:
				data = Float.parseFloat(parameter);
				break;
			case INT:
				data = Integer.parseInt(parameter);
				break;
			case BOOLEAN:
				if (parameter.equalsIgnoreCase("true") | parameter.equalsIgnoreCase("false")) {
					data = Boolean.parseBoolean(parameter);
				} else {
					throw new BooleanFormatException();
				}
				break;
			default:
				break;
			}
		} catch (NumberFormatException | BooleanFormatException | CharacterCountFormatException e) {
			System.out.println("Converting data.. to... " + dataType + "(" + parameter + ")");
		}
		return data;
	}

	/**
	 * Private method to open a text file and collect data lines as an ArrayList
	 * collection of lines.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @return
	 */
	private static ArrayList<String> openFileAndCollectData(String fileLocation, String fileName) {
		String fullFilePath = fileLocation + fileName;
		ArrayList<String> dataLines = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader(fullFilePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			while (line != null) {
				dataLines.add(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fullFilePath + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fullFilePath + "'");
		}
		return dataLines;
	}

	/**
	 * Private method to get parse data formatted in CSV style.
	 *
	 * @param lines
	 * @param dataTypes
	 * @return
	 */
	private static Object[][] parseCSVData(ArrayList<String> lines, DataType[] dataTypes) {
		ArrayList<Object> results = new ArrayList<>();
		String pattern = "(,*)([a-zA-Z0-9\\s-\\{\\}?:;\\/\\.\\<\\>\\_=!@#\\$^~`\'\\+%&\\(\\)]+)(,*)";
		Pattern r = Pattern.compile(pattern);
		for (int i = 0; i < lines.size(); i++) {
			int curDataType = 0;
			ArrayList<Object> curMatches = new ArrayList<>();
			Matcher m = r.matcher(lines.get(i));
			while (m.find()) {
				if (dataTypes.length > 0) {
					try {
						curMatches.add(convertDataType(m.group(2).trim(), dataTypes[curDataType]));
					} catch (Exception e) {
						System.out.println("DataTypes provided do not match parsed data results.");
					}
				} else {
					curMatches.add(m.group(2).trim());
				}
				curDataType++;
			}
			Object[] resultsObj = new Object[curMatches.size()];
			curMatches.toArray(resultsObj);
			results.add(resultsObj);
		}
		Object[][] resultsObj = new Object[results.size()][];
		results.toArray(resultsObj);
		return resultsObj;
	}
}
