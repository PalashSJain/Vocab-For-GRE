package wordsforgre.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

	public long getAllWordsIDAtIndex(long rowIndex) {
		String sql = "select (" + WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ
				+ ") from " + WordsDbCRUD.TABLE_NAME_QUIZ + " where "
				+ WordsDbCRUD.COLUMN_QUIZ_ID + "=" + rowIndex;
		Cursor cursor = database.rawQuery(sql, null);
		long l = -1;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToLast();
			l = cursor.getLong(0);
		}
		return l;
	}

	public void searchAndRemoveWordInstance(long wordId) {
		String subSql = "select " + WordsDbCRUD.COLUMN_QUIZ_ID + " from "
				+ WordsDbCRUD.TABLE_NAME_QUIZ + " where "
				+ WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ + "=" + wordId
				+ " order by " + WordsDbCRUD.COLUMN_QUIZ_ID + " desc limit 1";
		String sql = "delete from " + WordsDbCRUD.TABLE_NAME_QUIZ + " where "
				+ WordsDbCRUD.COLUMN_ALLWORDS_ID_IN_QUIZ + " = (" + subSql
				+ ")";
		database.execSQL(sql);
	}
}
