package wordsforgre.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WordsDbCRUD extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "words.db";
	private static final int DATABASE_VERSION = 25;

	public static final String TABLE_NAME_WORDS = "allwords";
	public static final String TABLE_NAME_QUIZ = "quizwords";

	public static final String COLUMN_ALLWORDS_ID_IN_ALLWORDS = "allwords_id_in_allwords";
	public static final String COLUMN_ALLWORDS_ID_IN_QUIZ = "allwords_id_in_quiz";
	public static final String COLUMN_QUIZ_ID = "quizwords_id";
	public static final String COLUMN_WORD = "word";
	public static final String COLUMN_MEANING = "meaning";
	public static final String COLUMN_WORD_EXPECTED = "expectedCount";
	public static final String COLUMN_WORD_ACTUAL = "actualCount";
	public static final String COLUMN_WORD_CATEGORY = "category";

	// Database creation sql statement
	private static final String DATABASE_CREATE_ALLWORDS = "create table if not exists "
			+ TABLE_NAME_WORDS
			+ " ("
			+ COLUMN_ALLWORDS_ID_IN_ALLWORDS
			+ " integer primary key autoincrement, "
			+ COLUMN_WORD
			+ " text not null unique, "
			+ COLUMN_MEANING
			+ " text not null,"
			+ COLUMN_WORD_ACTUAL
			+ " integer not null,"
			+ COLUMN_WORD_EXPECTED
			+ " integer not null, "
			+ COLUMN_WORD_CATEGORY
			+ " text not null);";

	private static final String DATABASE_CREATE_QUIZWORDS = "create table if not exists "
			+ TABLE_NAME_QUIZ
			+ " ("
			+ COLUMN_QUIZ_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_ALLWORDS_ID_IN_QUIZ + " integer not null);";

	public WordsDbCRUD(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_ALLWORDS);
		database.execSQL(DATABASE_CREATE_QUIZWORDS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(WordsDbCRUD.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WORDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_QUIZ);
		onCreate(db);
	}
}
