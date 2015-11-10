package wordsforgre.landing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import wordsforgre.about.AboutActivity;
import wordsforgre.allwords.AllWordsActivity;
import wordsforgre.allwords.AllWordsDbQuery;
import wordsforgre.quiz.QuizActivity;
import wordsforgre.utils.Config;
import wordsforgre.words.Word;
import wordsforgre.words.WordXMLHandler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final int ABOUT_NUM = Config.ABOUT_NUM;
	private static final int ALLWORDS_NUM = Config.ALLWORDS_NUM;
	private static final int QUIZ_NUM = Config.QUIZ_NUM;
	
	List<Word> wordList = null;

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
	private ArrayList<Word> words = null;
	private SharedPreferences sharedpreferences;
	private String was_previously_started;
	public static final String MyPREFERENCES = "MyPrefs" ;

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
        	SharedPreferences.Editor editor = sharedpreferences.edit();
        	editor.putBoolean(was_previously_started, true);
        	editor.commit();
        }
	}
	
	private void copyWordsFromXMLToDbThread() {
		final Context c = this;
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					InputSource inputSource = new InputSource(getAssets()
							.open("words.xml"));
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
				AllWordsDbQuery allWords = new AllWordsDbQuery(c);
				allWords.open();
				allWords.addWordsToDb(wordList);
				allWords.close();
			}
		});
		thread.start();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		switch (position + 1) {
		case QUIZ_NUM:
			Intent iQuiz = new Intent(this, QuizActivity.class);
			startActivity(iQuiz);
			break;
		case ALLWORDS_NUM:
			AllWordsDbQuery allWords = new AllWordsDbQuery(this);
			allWords.open();
			words = allWords.getAllWords();
			allWords.close();
			Intent iAllWords = new Intent(this, AllWordsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("data", words);
			iAllWords.putExtras(bundle);
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
					PlaceholderFragment.newInstance(position + 1)).commit();
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
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
