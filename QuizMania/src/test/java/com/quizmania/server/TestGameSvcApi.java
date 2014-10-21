package com.quizmania.server;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.quizmania.repository.Game;

public class TestGameSvcApi {

	private final static String QUIZ_FILE = "quizes.csv";

	List<Game> quiz = null;
	Set<String> SetOfUUID = null;

	@Test
	public void test() {

		GameController gamecontroller = new GameController();
		Game g = new Game();

		boolean ok = gamecontroller.addGameRecord(g);

		// Test the servlet directly, without going through the network.

		assertTrue(ok);

		List<Game> games = gamecontroller.getListofGames();
		assertTrue(games.contains(g));
	}

	@BeforeClass
	public static void setUpClass() throws Exception {

		// -define .csv file in app
		// String fileNameDefined = QUIZ_FILE;
		// -File class needed to turn stringName to actual file
		readWithCsvMapReader();
	}

	private Game createGame(){
		   	Game g= new Game();
	        File file = new File(QUIZ_FILE);
	        String s[]=null;
	        try{
	            // -read from filePooped with Scanner class
	            Scanner inputStream = new Scanner(file);
	            // hashNext() loops line-by-line
	            int i=0;
	            while(inputStream.hasNext()){
	                //read single line, put in string
	                Map<String, String> m=new HashMap<String, String>();
	                m.put(getUUID(), inputStream.next());
	                switch (i){
		        	case 0:
		        		g.setVar1(m);
		        		break;
		        	case 1:
		        		g.setVar2(m);
		        		break;
		        	case 2:
		        		g.setVar3(m);
		        		break;
		        	case 3:
		        		g.setAnswer(1);
		        		break;
		        	case 4:
		        		
		        	}
            }
	            // after loop, close scanner
	            inputStream.close();
	            
	            
	        for (i=0;i<s.length;i++){
	        	
	        }

	        }catch (FileNotFoundException e){

	            e.printStackTrace();
	        }
  return g;
		   
	   }

	private String getUUID() {
		String uuid = "";
		do {

			uuid = UUID.randomUUID().toString();

		} while (!SetOfUUID.add(uuid));
		return uuid;
	}

	private static CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				// new UniqueHashCode(), // customerNo (must be unique)
				new NotNull(), // Movie 1
				new NotNull(), // Movie 2
				// new ParseDate("dd/MM/yyyy"), // birthDate
				new NotNull(), // Movie 3
				new ParseInt(), // Odd Movie
				new NotNull(), // Description
				new ParseInt() // loyaltyPoints
		};

		return processors;
	}

	private static void readWithCsvMapReader() throws Exception {

		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(new FileReader(QUIZ_FILE),
					CsvPreference.STANDARD_PREFERENCE);

			// the header columns are used as the keys to the Map
			final String[] header = mapReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			Map<String, Object> customerMap;
			while ((customerMap = mapReader.read(header, processors)) != null) {
				System.out.println(String.format(
						"lineNo=%s, rowNo=%s, customerMap=%s",
						mapReader.getLineNumber(), mapReader.getRowNumber(),
						customerMap));
			}

		} finally {
			if (mapReader != null) {
				mapReader.close();
			}
		}
	}
}