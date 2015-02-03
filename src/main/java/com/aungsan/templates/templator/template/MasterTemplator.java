/**
 * 
 */
package com.aungsan.templates.templator.template;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

/**
 * @author Moe
 *
 */
public class MasterTemplator {
	private static final String LINE_SEPARATOR = "line.separator";
	private static final String NEW_LINE = System.getProperty(LINE_SEPARATOR);
	private static final String CHAR_SET_UTF_8 = "UTF-8";
	private static final String TEMPLATE_FOLDER = ".\\template\\";
	private static final String OUTPUT_FOLDER = ".\\output\\";
	private static final String OPEN_TAG = "<:=";
	private static final String CLOSE_TAG = "=:>";
	private static final String TAG_NAME_RESOURCE = "resource";
	private static final String ATTR_NAME_FILE = "file";
	private static final String TOKEN_QUOTE = "\"";
	private static final String DOUBLE_QUOTE = "&quot;";
	private static final String[] PARAMETERS = { "parameter1", "parameter2",
			"parameter3", "parameter4", "parameter5", "parameter6",
			"parameter7", "parameter8", "parameter9", "parameter10", "self" };
	private static final List<String> PARAMETERS_LIST = Arrays
			.asList(PARAMETERS);
	private static final String[][] TEMPALTE_MAP = {
			{ "index.tlr", "index.htm" }, { "speeches.tlr", "speeches.htm" },
			{ "photo.tlr", "photo.htm" }, { "timeline.tlr", "timeline.htm" },
			{ "perspectives.tlr", "perspectives.htm" },
			{ "guestbook.tlr", "guestbook.htm" }, { "links.tlr", "links.htm" },
			{ "quotes.tlr", "quotes.htm" }, { "quotes2.tlr", "quotes2.htm" },
			{ "quotes3.tlr", "quotes3.htm" },
			{ "siamesepeople.tlr", "siamesepeople.htm" },
			{ "asiaticunity.tlr", "asiaticunity.htm" },
			{ "fascistbarbarism.tlr", "fascistbarbarism.htm" },
			{ "angloburmans.tlr", "angloburmans.htm" },
			{ "situationtasks.tlr", "situationtasks.htm" },
			{ "welcomeindia.tlr", "welcomeindia.htm" },
			{ "critiquebritish.tlr", "critiquebritish.htm" },
			{ "probburma.tlr", "probburma.htm" },
			{ "resmovement.tlr", "resmovement.htm" },
			{ "as18.tlr", "as18.htm" }, { "as10.tlr", "as10.htm" },
			{ "as03.tlr", "as03.htm" }, { "as17.tlr", "as17.htm" },
			{ "as11.tlr", "as11.htm" }, { "as02.tlr", "as02.htm" },
			{ "as09.tlr", "as09.htm" }, { "as04.tlr", "as04.htm" },
			{ "as13.tlr", "as13.htm" }, { "as12.tlr", "as12.htm" },
			{ "as15.tlr", "as15.htm" }, { "as05.tlr", "as05.htm" },
			{ "as06.tlr", "as06.htm" }, { "as14.tlr", "as14.htm" },
			{ "as16.tlr", "as16.htm" }, { "as08.tlr", "as08.htm" },
			{ "as07.tlr", "as07.htm" }, { "lifesketch.tlr", "lifesketch.htm" },
			{ "britannica.tlr", "britannica.htm" },
			{ "msencarta99.tlr", "msencarta99.htm" },
			{ "y2kgrolier.tlr", "y2kgrolier.htm" },
			{ "viewguestbook.tlr", "viewguestbook.htm" } };

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (int i = 0; i < TEMPALTE_MAP.length; i++) {
			processTemplate(TEMPALTE_MAP[i][0], TEMPALTE_MAP[i][1]);
		}

	}

	private static void processTemplate(String templateName, String outputName) {
		String templateFileName = TEMPLATE_FOLDER + templateName;
		String outputFileName = OUTPUT_FOLDER + outputName;

		long start = System.currentTimeMillis();

		process(templateFileName, outputFileName, outputName);

		long stop = System.currentTimeMillis();

		System.out.println("\nProcessed the template " + templateFileName
				+ " to output file " + outputFileName + " in " + (stop - start)
				+ " milliseconds.");
	}

	private static void process(String templateFilename, String outputFileName,
			String outputName) {
		InputStream inStream = null;
		InputStreamReader inputreader;
		BufferedReader buffreader;

		OutputStream outStream = null;
		OutputStreamWriter outputwriter;
		BufferedWriter buffwrite;

		try {

			File afile = new File(templateFilename);
			inStream = new FileInputStream(afile);
			inputreader = new InputStreamReader(inStream, CHAR_SET_UTF_8);
			buffreader = new BufferedReader(inputreader);

			File bfile = new File(outputFileName);
			outStream = new FileOutputStream(bfile);
			outputwriter = new OutputStreamWriter(outStream, CHAR_SET_UTF_8);
			buffwrite = new BufferedWriter(outputwriter);

			String resourceItem;

			while ((resourceItem = buffreader.readLine()) != null) {
				if (resourceItem.trim().length() > 0) {
					processResource(resourceItem, buffwrite, outputName);
				}
			}

			buffreader.close();
			inputreader.close();
			inStream.close();

			buffwrite.close();
			outputwriter.close();
			outStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String[] getParameters(String resourceItem, String outputName) {
		String[] values = new String[PARAMETERS.length];

		for (int i = 0; i < values.length; i++) {
			values[i] = getResourceAttributes(resourceItem, PARAMETERS[i]);
		}

		if (values[PARAMETERS.length - 1] == null) {
			// set self parameter as outputName
			values[PARAMETERS.length - 1] = outputName;
		}

		return values;
	}

	private static String replaceParameters(String inputLine, String[] values) {
		int posOpenTag = inputLine.indexOf(OPEN_TAG);
		int posCloseTag = inputLine.indexOf(CLOSE_TAG, posOpenTag + 3);

		while (posOpenTag > 0 && posCloseTag > 0) {
			String parameter = inputLine.substring(posOpenTag + 3, posCloseTag)
					.trim();
			int paramIndex = PARAMETERS_LIST.indexOf(parameter);
			String value = values[paramIndex];
			if (paramIndex >= 0) {
				String paramValue = (value != null ? value.replace(
						DOUBLE_QUOTE, TOKEN_QUOTE) : "");
				inputLine = inputLine.replace(
						inputLine.substring(posOpenTag, posCloseTag + 3),
						paramValue);
			}

			posOpenTag = inputLine.indexOf(OPEN_TAG);
			posCloseTag = inputLine.indexOf(CLOSE_TAG, posOpenTag + 3);
		}

		return inputLine;
	}

	private static void processResource(String resourceItem,
			BufferedWriter outputWriter, String outputName) {
		String resourceFileName = getResourceAttributes(resourceItem,
				ATTR_NAME_FILE);

		InputStream inStream = null;
		InputStreamReader inputreader;
		BufferedReader buffreader;

		try {

			File afile = new File(resourceFileName);
			inStream = new FileInputStream(afile);
			inputreader = new InputStreamReader(inStream, CHAR_SET_UTF_8);
			buffreader = new BufferedReader(inputreader);

			String[] values = getParameters(resourceItem, outputName);
			String outputLine;

			while ((outputLine = buffreader.readLine()) != null) {

				outputWriter.write(replaceParameters(outputLine, values));
				outputWriter.write(NEW_LINE);
				outputWriter.flush();

			}

			buffreader.close();
			inputreader.close();
			inStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String getResourceAttributes(String resourceItem,
			String attribute) {
		String result = null;
		String resourceLineLower = resourceItem.toLowerCase();
		int posOpenTag = resourceLineLower.indexOf(OPEN_TAG);
		int posResourceTag = resourceLineLower.indexOf(TAG_NAME_RESOURCE);

		if (posOpenTag >= 0 && posResourceTag > 0
				&& posResourceTag > posOpenTag) {
			int posFileName = resourceLineLower.indexOf(attribute);

			if (posFileName > posResourceTag) {
				// String resourceFileStart =
				// resourceItem.substring(posFileName+LEN_ATTR_NAME_FILE);
				int posFirstQuote = resourceItem.indexOf(TOKEN_QUOTE,
						posFileName + 1);
				int posSecondQuote = resourceItem.indexOf(TOKEN_QUOTE,
						posFirstQuote + 1); // resourceFileStart.
											// .substring(posFirstQuote+1).indexOf(TOKEN_QUOTE);
				result = resourceItem.substring(posFirstQuote + 1,
						posSecondQuote);
			}
		}

		return result;
	}
}
