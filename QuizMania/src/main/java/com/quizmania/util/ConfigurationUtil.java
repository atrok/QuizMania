package com.quizmania.util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.quizmania.repository.Game;


public class ConfigurationUtil {

	
	private final static String QUIZ_FILE = "quizes.csv";
	private final static Logger logger=Logger.getLogger(ConfigurationUtil.class);
	
	private static CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				// new UniqueHashCode(), // customerNo (must be unique)
				new NotNull(), // Movie 1
				new NotNull(), // Movie 2
				// new ParseDate("dd/MM/yyyy"), // birthDate
				new NotNull(), // Movie 3
				new NotNull(), // Movie 4
				new ParseInt(), // Odd Movie
				new NotNull(), // Description
				new Optional(new ParseInt()) // loyaltyPoints
		};

		return processors;
	}

	public static List<Game> readWithCsvReader() throws Exception {

		ICsvBeanReader mapReader = null;
		Game game;
		List<Game> games=new ArrayList<Game>();
		try {
			mapReader = new CsvBeanReader(new FileReader(QUIZ_FILE),
					CsvPreference.STANDARD_PREFERENCE);

			// the header columns are used as the keys to the Map
			final String[] header = mapReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();

			
			while ((game = mapReader.read(Game.class,header, processors)) != null) {
				logger.debug(String.format(
						"lineNo=%s, rowNo=%s, customerMap=%s",
						mapReader.getLineNumber(), mapReader.getRowNumber(),
						game));
				games.add(game);
			}
		}
		catch(Exception e){
			logger.error("readWithCsvReader has failed");
			e.printStackTrace();
			throw e;
		} finally {
			if (mapReader != null) {
				mapReader.close();
			}
		}
		return games;
	}
}
