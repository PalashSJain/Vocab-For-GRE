package wordsforgre.landing;

import wordsforgre.database.AllWordsDbQuery;
import wordsforgre.utils.Config;
import wordsforgre.words.Word;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment {

	public MainFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		AllWordsDbQuery allWords = new AllWordsDbQuery(getContext());
		allWords.open();
		final Word w = allWords.getRandomWord();
		allWords.close();

		TextView tvWord = (TextView) rootView.findViewById(R.id.tvMainWord);
		tvWord.setText(w.word.toUpperCase());

		final LinearLayout llOptions = (LinearLayout) rootView
				.findViewById(R.id.expandedOptions);
		ImageButton ibDelete = (ImageButton) rootView
				.findViewById(R.id.imageDeleteWord);
		ImageButton ibEdit = (ImageButton) rootView
				.findViewById(R.id.imageEditWord);

		ibDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: Add confirmation dialogue here
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						AllWordsDbQuery allWords = new AllWordsDbQuery(
								getContext());
						allWords.open();
						allWords.deleteWord(w);
						allWords.close();
					}
				});
				thread.start();
				getFragmentManager().beginTransaction()
						.replace(R.id.container, new MainFragment())
						.commit();
				llOptions.setVisibility(View.GONE);
			}
		});

		ibEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(),
						"Edition of Words is disabled for now.",
						Toast.LENGTH_LONG).show();
			}
		});

		TextView tvMeaning = (TextView) rootView
				.findViewById(R.id.tvMainMeaning);
		tvMeaning.setText(w.meaning);
		
		rootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
				.replace(R.id.container, new MainFragment())
				.commit();
			}
		});
		
		rootView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (llOptions.getVisibility() == View.VISIBLE) {
					llOptions.setVisibility(View.GONE);
				} else if (llOptions.getVisibility() == View.GONE) {
					llOptions.setVisibility(View.VISIBLE);
				}
				return true;
			}
		});

		switch (w.category) {
		case Config.CAT_GOOD:
			tvWord.setTextColor(Config.COLOR_GREEN);
			tvMeaning.setTextColor(Config.COLOR_GREEN);
			break;
		case Config.CAT_BAD:
			tvWord.setTextColor(Config.COLOR_YELLOW);
			tvMeaning.setTextColor(Config.COLOR_YELLOW);
			break;
		case Config.CAT_UGLY:
			tvWord.setTextColor(Config.COLOR_RED);
			tvMeaning.setTextColor(Config.COLOR_RED);
			break;
		default:
			tvWord.setTextColor(Config.COLOR_BLUE);
			tvMeaning.setTextColor(Config.COLOR_BLUE);
			break;
		}

		return rootView;
	}

}
