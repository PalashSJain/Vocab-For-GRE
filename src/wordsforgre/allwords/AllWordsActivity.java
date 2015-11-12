package wordsforgre.allwords;

import java.util.ArrayList;

import wordsforgre.allwords.AllWordsFragment.OnFragmentInteractionListener;
import wordsforgre.landing.R;
import wordsforgre.words.Word;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AllWordsActivity extends ActionBarActivity implements OnFragmentInteractionListener{
	
//	private ArrayList<Word> words;
	AllWordsDbQuery allWords;
	AllWordsAdapter adapter;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
		setContentView(R.layout.all_words_activity);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
//		
//		Bundle bundle = getIntent().getExtras();
//		words = bundle.getParcelableArrayList("data");
		
//		getSupportFragmentManager().beginTransaction().replace(R.id.container, new AllWordsFragment(words)).commit();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, new AllWordsFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_words, menu);
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

	@Override
	public void onFragmentInteraction(int mnuId, long l) {
		// TODO ??????
	}
}
