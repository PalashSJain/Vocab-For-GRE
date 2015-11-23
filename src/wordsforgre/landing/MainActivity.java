package wordsforgre.landing;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import wordsforgre.about.AboutActivity;
import wordsforgre.allwords.AllWordsActivity;
import wordsforgre.database.AllWordsDbQuery;
import wordsforgre.database.QuizWordsDbQuery;
import wordsforgre.quiz.QuizActivity;
import wordsforgre.utils.Config;
import wordsforgre.utils.SettingsActivity;
import wordsforgre.words.Word;
import wordsforgre.words.WordXMLHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final int ABOUT_NUM = Config.ABOUT_NUM;
	private static final int ALLWORDS_NUM = Config.ALLWORDS_NUM;
	private static final int QUIZ_NUM = Config.QUIZ_NUM;
	
	List<Word> wordList = null;
	final Context c = this;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private SharedPreferences sharedpreferences;
	private String was_previously_started;
	public static final String MyPREFERENCES = "MyPrefs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean wasPreviouslyStarted = sharedpreferences.getBoolean(was_previously_started, false);
        if (!wasPreviouslyStarted) {
        	copyWordsFromXMLToDbThread();
        	copyRecordsFromAllWordsToQuiz();
        	SharedPreferences.Editor editor = sharedpreferences.edit();
        	editor.putBoolean(was_previously_started, true);
        	editor.commit();
        }
	}
	
	private void copyRecordsFromAllWordsToQuiz() {
		QuizWordsDbQuery quizWords = new QuizWordsDbQuery(
				getApplicationContext());
		quizWords.open();
		long[] ids = quizWords.getAllIdsFromAllWords();
		for (int i = 0; i < ids.length; i++) {
			quizWords.joinRecordsFromAllWordsToQuiz(ids[i]);
		}
		quizWords.close();
	}

	private void copyWordsFromXMLToDbThread() {
		try {
			InputSource inputSource = new InputSource(getAssets().open(
					"words.xml"));
			// instantiate SAX parser
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();

			// get the XML reader
			XMLReader xmlReader = saxParser.getXMLReader();

			// prepare and set the XML content or data handler before
			// parsing
			WordXMLHandler xmlContentHandler = new WordXMLHandler();
			xmlReader.setContentHandler(xmlContentHandler);

			// parse the XML input source
			xmlReader.parse(inputSource);

			// put the parsed data to a List
			wordList = xmlContentHandler.getParsedData();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		AllWordsDbQuery allWords = new AllWordsDbQuery(getApplicationContext());
		allWords.open();
		allWords.addWordsToDb(wordList);
		allWords.close();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		switch (position + 1) {
		case QUIZ_NUM:
			Intent iQuiz = new Intent(this, QuizActivity.class);
			startActivity(iQuiz);
			break;
		case ALLWORDS_NUM:
			Intent iAllWords = new Intent(this, AllWordsActivity.class);
            startActivity(iAllWords);
			break;
		case ABOUT_NUM:
			Intent iAbout = new Intent(this, AboutActivity.class);
            startActivity(iAbout);
			break;
		default:
			// update the main content by replacing fragments
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					new RandomWordsFragment()).commit();
			break;
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case QUIZ_NUM:
			mTitle = getString(R.string.title_quiz);
			break;
		case ALLWORDS_NUM:
			mTitle = getString(R.string.title_all_words);
			break;
		case ABOUT_NUM:
			mTitle = getString(R.string.title_about);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent iSettings = new Intent(this, SettingsActivity.class);
			startActivity(iSettings);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
