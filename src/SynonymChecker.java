import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SynonymChecker {
	private static SynonymChecker instance = null;
	static class CONSTANTS {
		private static final String AMAZON_URL_PREFIX = "http://www.amazon.com/s/field-keywords=";
		private static final int FIRST_PHRASE = 0;
		private static final int SECOND_PHRASE = 1;
		private static final String OUTPUT_FILE = "/tmp/synonyms.txt"; 
	}
	
	private static String INPUT_FILENAME = "/home/varun/input.txt";
	private static int MATCHING_THRESHOLD = 10;
	
	private SynonymChecker(String inputFile, int matchingThreshold) {
		INPUT_FILENAME = inputFile;
		MATCHING_THRESHOLD = matchingThreshold;
	}
	
	public static SynonymChecker getInstance(String inputFile, int matchingThreshold) {
		if(instance == null) {
			instance = new SynonymChecker(inputFile, matchingThreshold);
		}
		return instance;
	}
	
	private static ArrayList<String> getLinks(String url) {
		Document doc;
		ArrayList<String> urls = new ArrayList<String>();
		Elements links = null;
		try {
			doc = Jsoup.connect(url).get();
			links = doc.select("a[href]");
			for (Element link : links) {
				urls.add(link.attr("abs:href"));
			}

		} catch (IOException e) {
			// Do nothing.. move on
			e.printStackTrace();
		}
		return urls; 
	}
	
	private static ArrayList<String> commonLinks(String urlOne, String urlTwo) {
		ArrayList<String> urlOneLinks = SynonymChecker.getLinks(urlOne);
		ArrayList<String> urlTwoLinks = SynonymChecker.getLinks(urlTwo);
		ArrayList<String> randomUrlLinks = SynonymChecker.getLinks(CONSTANTS.AMAZON_URL_PREFIX + UUID.randomUUID().toString());
		ArrayList<String> common = new ArrayList<String>(urlTwoLinks);
		common.retainAll(urlOneLinks);
		common.removeAll(randomUrlLinks);
		return common;
	}
	
	private static String generateURL(String phrase) {
		return CONSTANTS.AMAZON_URL_PREFIX.concat(phrase.replace(" ", "+"));
	}
	
	private static boolean synonyms(String urlOne, String urlTwo) {
		return SynonymChecker.commonLinks(urlOne, urlTwo).size() > MATCHING_THRESHOLD;
	}
	
	public static void validateSynonyms() {
		BufferedReader in = null;
		BufferedWriter out = null;
		try {
			in = new BufferedReader(new FileReader(SynonymChecker.INPUT_FILENAME));
			out = new BufferedWriter(new FileWriter(CONSTANTS.OUTPUT_FILE));		
			String strLine;	
			while ((strLine = in.readLine()) != null)   {
				String[] tokens = strLine.split(",");
				System.out.println("Checking for " + tokens[CONSTANTS.FIRST_PHRASE] + " and " + tokens[CONSTANTS.SECOND_PHRASE]);
				String output = strLine + "," + SynonymChecker.synonyms(SynonymChecker.generateURL(tokens[CONSTANTS.FIRST_PHRASE]), 
																		SynonymChecker.generateURL(tokens[CONSTANTS.SECOND_PHRASE]));
				System.out.println("Writing " + output);
				out.write(output);
				out.newLine();
			} 
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	public static void main(String[] args) {
		SynonymChecker.getInstance("/home/varun/candidate_synonyms.txt", 10);
    	SynonymChecker.validateSynonyms();
    }
}
