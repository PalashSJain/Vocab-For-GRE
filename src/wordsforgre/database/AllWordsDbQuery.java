package wordsforgre.database;

import java.util.ArrayList;
import java.util.List;

import wordsforgre.utils.Config;
import wordsforgre.words.Word;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AllWordsDbQuery {
	// Database fields
	private SQLiteDatabase database;
	private WordsDbCRUD dbHelper;
	private String[] allColumns = { WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_ALLWORDS,
			WordsDbCRUD.COLUMN_WORD, WordsDbCRUD.COLUMN_MEANING,
			WordsDbCRUD.COLUMN_WORD_ACTUAL, WordsDbCRUD.COLUMN_WORD_EXPECTED, WordsDbCRUD.COLUMN_WORD_CATEGORY };
	private String[] wordMeaningAndCategoryColumn = { WordsDbCRUD.COLUMN_WORD,
			WordsDbCRUD.COLUMN_MEANING, WordsDbCRUD.COLUMN_WORD_CATEGORY };
	private List<Word> words;

	public AllWordsDbQuery(Context context) {
		dbHelper = new WordsDbCRUD(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Word addWord(String word, String meaning, long actual, long expected, String category) {
		ContentValues values = new ContentValues();
		values.put(WordsDbCRUD.COLUMN_WORD, word);
		values.put(WordsDbCRUD.COLUMN_MEANING, meaning);
		values.put(WordsDbCRUD.COLUMN_WORD_ACTUAL, actual);
		values.put(WordsDbCRUD.COLUMN_WORD_EXPECTED, expected);
		values.put(WordsDbCRUD.COLUMN_WORD_CATEGORY, category);
		Word w = null;
		try {
			long insertId = database.insertOrThrow(
					WordsDbCRUD.TABLE_NAME_WORDS, null, values);
			if (insertId != -1) {
				Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_WORDS,
						allColumns, WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_ALLWORDS
								+ " = " + insertId, null, null, null, null);
				cursor.moveToFirst();
				w = cursorToWord(cursor);
				w.id = insertId;
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return w;
	}

	public void deleteWord(Word word) {
		long id = word.id;
		System.out.println("Comment deleted with id: " + id);
		database.delete(WordsDbCRUD.TABLE_NAME_WORDS,
				WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_ALLWORDS + " = " + id, null);
	}

	public ArrayList<Word> getAllWords() {
		ArrayList<Word> words = new ArrayList<Word>();

		Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_WORDS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(0);
			Word w = cursorToWord(cursor);
			w.id = id;
			words.add(w);
			cursor.moveToNext();
		}
		cursor.close();
		return words;
	}

	private Word cursorToWord(Cursor cursor) {
		Word w = new Word(cursor.getString(1), cursor.getString(2));
		return w;
	}

	public void addWordsToDb(List<Word> wordList) {
		words = wordList;
		for (int i = 0; i < words.size(); i++) {
			if (words.get(i) != null) {
				this.addWord(words.get(i).word, words.get(i).meaning, 1, 1, Config.CAT_NEUTRAL);
			}
		}
	}

	public Word getWordAtIndex(long allWordId) {
		Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_WORDS,
				wordMeaningAndCategoryColumn, WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_ALLWORDS + " = "
						+ allWordId, null, null, null, null);
		Word w = null;
		try {
			cursor.moveToFirst();
			w = new Word(cursor.getString(0), cursor.getString(1));
			w.category = cursor.getString(2);
			w.allWordId = allWordId;
		} catch(Exception e) {
			System.out.println("e.stackTrace: " + e.getStackTrace());
		}
		return w;
	}

	public long[] getWordInfo(long allWordId) {
		Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_WORDS,
				new String[] { WordsDbCRUD.COLUMN_WORD_ACTUAL,
						WordsDbCRUD.COLUMN_WORD_EXPECTED },
				WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_ALLWORDS + "=" + allWordId,
				null, null, null, null);
		if (cursor.moveToNext()) {
			long[] info = { cursor.getLong(0), cursor.getLong(1) };
			return info;
		}
		return new long[]{0,0};
	}

	public void increaseExpectedAndActualToMax(long wordId) {
		setActualAndExpectedValues(wordId, 10, 10, Config.CAT_UGLY);
	}

	private void setActualAndExpectedValues(long wordId, long actual, long expected, String category) {
		ContentValues values = new ContentValues();
		values.put(WordsDbCRUD.COLUMN_WORD_ACTUAL, actual);
		values.put(WordsDbCRUD.COLUMN_WORD_EXPECTED, expected);
		values.put(WordsDbCRUD.COLUMN_WORD_CATEGORY, category);
		database.update(WordsDbCRUD.TABLE_NAME_WORDS,
				values, WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_ALLWORDS + "="
						+ wordId, null);
	}

	public void setWordCategoryToGood(long wordId) {
		setActualAndExpectedValues(wordId, 1, 1, Config.CAT_GOOD);
	}

	public void setWordCategoryToBad(long wordId) {
		setActualAndExpectedValues(wordId, 8, 8, Config.CAT_BAD);
	}

	public void reduceActualFromWordsTable(long wordId, long newActual) {
		if (newActual > 1 && newActual <= 8) {
			setActualAndExpectedValues(wordId, newActual, newActual, Config.CAT_BAD);
		} else if (newActual > 8) {
			setActualAndExpectedValues(wordId, newActual, newActual, Config.CAT_UGLY);
		}
	}
}
