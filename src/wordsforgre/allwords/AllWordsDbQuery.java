package wordsforgre.allwords;

import java.util.ArrayList;
import java.util.List;

import wordsforgre.words.Word;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AllWordsDbQuery {
	// Database fields
	private SQLiteDatabase database;
	private AllWordsDbCRUD dbHelper;
	private String[] allColumns = { AllWordsDbCRUD.COLUMN_ID,
			AllWordsDbCRUD.COLUMN_WORD, AllWordsDbCRUD.COLUMN_MEANING };
	private List<Word> words;

	public AllWordsDbQuery(Context context) {
		dbHelper = new AllWordsDbCRUD(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Word addWord(String word, String meaning) {
		ContentValues values = new ContentValues();
		values.put(AllWordsDbCRUD.COLUMN_WORD, word);
		values.put(AllWordsDbCRUD.COLUMN_MEANING, meaning);
		Word w = null;
		try {
			long insertId = database.insertOrThrow(AllWordsDbCRUD.TABLE_WORDS,
					null, values);
			Log.i("ADD UNIQUE WORDS", "" + insertId);
			if (insertId != -1) {
				Cursor cursor = database.query(AllWordsDbCRUD.TABLE_WORDS,
						allColumns,
						AllWordsDbCRUD.COLUMN_ID + " = " + insertId, null,
						null, null, null);
				cursor.moveToFirst();
				w = cursorToWord(cursor, insertId);
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return w;
	}

	public void deleteWord(Word word) {
		long id = word.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(AllWordsDbCRUD.TABLE_WORDS, AllWordsDbCRUD.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Word> getAllWords() {
		List<Word> words = new ArrayList<Word>();

		Cursor cursor = database.query(AllWordsDbCRUD.TABLE_WORDS, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(0);
			Word w = cursorToWord(cursor, id);
			System.out.println("word: " + w.getWord());
			words.add(w);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return words;
	}

	private Word cursorToWord(Cursor cursor, long id) {
		Word w = new Word(cursor.getString(1), cursor.getString(2));
		w.setId(id);
		return w;
	}

	public void addWordsToDb(List<Word> wordList) {
		words = wordList;
		for (int i = 0; i < words.size(); i++) {
			if (words.get(i) != null) {
				this.addWord(words.get(i).getWord(), words.get(i).getMeaning());
			}
		}
	}

}
