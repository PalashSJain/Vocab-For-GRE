package wordsforgre.allwords;

import java.util.List;

import wordsforgre.words.Word;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class AllWordsFragment extends ListFragment {

	private List<Word> words;
	AllWordsDbQuery allWords;
	AllWordsAdapter adapter;

	public AllWordsFragment(List<Word> words) {
		this.words = words;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new AllWordsAdapter(getContext(), words);
		setListAdapter(adapter);
	}
}
