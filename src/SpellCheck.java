import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class SpellCheck {
	
	private static final String DICTIONARY_PATH = "dictionary.txt";
	private static final String DOCUMENT_PATH = "nosilverbullet.txt";
	MyHashTable<String> hashTable = new MyHashTable<>();
	HashMap<Integer, ArrayList<String>> letterCountMap;
	
	public SpellCheck() {
		letterCountMap = new HashMap<>();
	}
	
	public static void main(String[] args) {
		SpellCheck sc = new SpellCheck();
		long startTime = System.currentTimeMillis();
		boolean loadDictionaySuccess = sc.loadDictionary(DICTIONARY_PATH);
		long endTime = System.currentTimeMillis();
		System.out.println("Time taken: "+(endTime - startTime)/1000+"s");
		if(loadDictionaySuccess)
			sc.checkDocument(DOCUMENT_PATH);
	}

	@SuppressWarnings("resource")
	private boolean loadDictionary(String dictionaryPath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(dictionaryPath));
		} catch (FileNotFoundException e) {
			System.err.println("Error reading dictionary file: "+e.getMessage());
			return false;
		}
		String line;
		try {
			line = br.readLine();
		} catch (IOException e) {
			System.err.println("Error reading dictionary file: "+e.getMessage());
			return false;
		}
		while (line != null)
		{
			String word = line.trim();
			hashTable.insert(word);
			ArrayList<String> listWithCount = letterCountMap.get(word.length());
			if(listWithCount == null)
			{
				listWithCount = new ArrayList<>();
				listWithCount.add(word);
				letterCountMap.put(word.length(), listWithCount);
			}
			else
			{
				listWithCount.add(word);
			}
			try {
				line = br.readLine();
			} catch (IOException e) {
				System.err.println("Error reading dictionary file: "+e.getMessage());
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("resource")
	private void checkDocument(String documentPath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(documentPath));
		} catch (FileNotFoundException e) {
			System.err.println("Error reading document: "+e.getMessage());
			return;
		}
		String line;
		try {
			line = br.readLine();
		} catch (IOException e) {
			System.err.println("Error reading document: "+e.getMessage());
			return;
		}
		while (line != null)
		{
			String[] words = line.split("\\s|-");
			for (String word : words) {
				word = word.replaceAll("[^a-zA-Z\\']", "");
				word = word.trim();
				if(word.equals(""))
					continue;
				if(!hashTable.contains(word.toLowerCase()))
				{
					ArrayList<String> alternateWords = suggestAlternateWords(word.toLowerCase());
					System.out.println(word+" is misspelled");
					System.out.println("Alternatives: "+alternateWords);
				}
			}
			try {
				line = br.readLine();
			} catch (IOException e) {
				System.err.println("Error reading document: "+e.getMessage());
				return;
			}
		}
	}

	private ArrayList<String> suggestAlternateWords(String word) {
		ArrayList<String> wordsWithSameLength = letterCountMap.get(word.length());
		ArrayList<String> alternateWords = new ArrayList<>();
		if(wordsWithSameLength == null)
		{
			System.out.println("Couldn't find array list for word: "+word);
			return alternateWords;
		}
		for (String word2 : wordsWithSameLength) {
			if(isOneCharacterOff(word, word2))
				alternateWords.add(word2);
		}
		return alternateWords;
	}

	private boolean isOneCharacterOff(String word, String word2) {
		int dif = 0;
		for (int i = 0; i < word.length(); i++) {
			if(word.charAt(i) != word2.charAt(i))
				dif++;
			if(dif>=2)
				return false;
		}
		if(dif == 1)
			return true;
		return false;
	}
}
