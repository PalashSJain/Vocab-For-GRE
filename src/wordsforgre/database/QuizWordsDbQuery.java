package wordsforgre.database;

import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class QuizWordsDbQuery {

	private SQLiteDatabase database;
	private WordsDbCRUD dbHelper;
	private String[] allColumns = { WordsDbCRUD.COLUMN_QUIZ_ID,
			WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ };

	public QuizWordsDbQuery(Context context) {
		dbHelper = new WordsDbCRUD(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public int getCount() {
		Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_QUIZ,
				new String[] { WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ }, null,
				null, null, null, null);
		int count = cursor.getCount();
		return count;
	}

	public long addWord(long expected) {
		ContentValues values = new ContentValues();
		values.put(WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ, expected);
		long insertId = -1;
		try {
			insertId = database.insertOrThrow(WordsDbCRUD.TABLE_NAME_QUIZ,
					null, values);
			if (insertId != -1) {
				Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_QUIZ,
						allColumns, WordsDbCRUD.COLUMN_QUIZ_ID + " = "
								+ insertId, null, null, null, null);
				cursor.moveToFirst();
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return insertId;
	}

	public void joinRecordsFromAllWordsToQuiz(long id) {
		ContentValues values = new ContentValues();
		values.put(WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ, id);
		try {
			long insertId = database.insertOrThrow(WordsDbCRUD.TABLE_NAME_QUIZ,
					null, values);
			if (insertId != -1) {
				Cursor cursor = database
						.query(WordsDbCRUD.TABLE_NAME_QUIZ,
								new String[] { WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ },
								WordsDbCRUD.COLUMN_QUIZ_ID + " = " + insertId,
								null, null, null, null);
				cursor.moveToFirst();
				cursor.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public long[] getAllIdsFromAllWords() {
		Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_WORDS,
				new String[] { WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_ALLWORDS },
				null, null, null, null, null);
		int noOfWords = cursor.getCount();
		long[] ids = new long[noOfWords];
		cursor.moveToFirst();
		for (int i = 0; i < noOfWords; i++) {
			ids[i] = cursor.getLong(0);
			cursor.moveToNext();
		}
		return ids;
	}

	public long getAllWordsIdOfRandomWord() {
		Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_QUIZ,
				new String[] { WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ }, null,
				null, null, null, null);
		int noOfRecords = cursor.getCount();
		Random random = new Random();
		int rowIndex = random.nextInt(noOfRecords);
		long l = -1;
		try {
			if (cursor.moveToPosition(rowIndex)) {
				l = cursor.getLong(0);
			} else {
				Log.i("getAllWordsIDAtIndex", "Cursor is empty for rowIndex, "
						+ rowIndex);
			}
		} catch (Exception e) {
			Log.i("getAllWordsIDAtIndex", "e.stackTrace.toString: "
					+ e.getStackTrace().toString());
		}
		return l;
	}

	public void searchAndRemoveWordInstance(long wordId) {
		Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_QUIZ,
				new String[] { WordsDbCRUD.COLUMN_QUIZ_ID },
				WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ + " = " + wordId, null,
				null, null, null);
		cursor.moveToFirst();
		long quizId = cursor.getLong(0);
		database.delete(WordsDbCRUD.TABLE_NAME_QUIZ, WordsDbCRUD.COLUMN_QUIZ_ID
				+ "=?", new String[] { "" + quizId });
	}

	public void printAllWords() {
		Cursor cursor = database.query(WordsDbCRUD.TABLE_NAME_QUIZ,
				new String[] { WordsDbCRUD.COLUMN_QUIZ_ID,
						WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ }, null, null,
				null, null, null);
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {
			System.out.println(cursor.getLong(0) + " : " + cursor.getLong(1));
			cursor.moveToNext();
		}
	}
}
