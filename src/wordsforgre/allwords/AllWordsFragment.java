package wordsforgre.allwords;

import java.util.List;

import wordsforgre.words.Word;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class AllWordsFragment extends ListFragment {

	private List<Word> words;
	AllWordsDbQuery allWords;
	AllWordsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			allWords = new AllWordsDbQuery(getContext());
			allWords.open();
			words = allWords.getAllWords();
			allWords.close();
			adapter = new AllWordsAdapter(getContext(), words);
			setListAdapter(adapter);
		}
	}
}
