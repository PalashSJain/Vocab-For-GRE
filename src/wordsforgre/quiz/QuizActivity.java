package wordsforgre.quiz;

import java.util.ArrayList;

import wordsforgre.allwords.AllWordsDbQuery;
import wordsforgre.landing.R;
import wordsforgre.words.Word;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class QuizActivity extends ActionBarActivity {
	
	private ArrayList<Word> words;
	AllWordsDbQuery allWords;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.quiz_activity);
		if (savedInstanceState == null) {
			allWords = new AllWordsDbQuery(this);
			allWords.open();
			words = allWords.getAllWords();
			allWords.close();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new QuizQuestionFragment(words)).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
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
		if (id == R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
