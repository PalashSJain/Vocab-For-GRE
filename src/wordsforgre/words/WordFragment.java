package wordsforgre.words;

import wordsforgre.database.AllWordsDbQuery;
import wordsforgre.landing.R;
import wordsforgre.utils.Config;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WordFragment extends Fragment {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	LinearLayout llOptions;
	Word w = null;

	public WordFragment(Word word) {
		this.w = word;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		if (w == null) {
			AllWordsDbQuery allWords = new AllWordsDbQuery(getContext());
			allWords.open();
			w = allWords.getRandomWord();
			allWords.close();
		}

		TextView tvWord = (TextView) rootView.findViewById(R.id.tvMainWord);
		tvWord.setText(w.word.toUpperCase());

		llOptions = (LinearLayout) rootView
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
						.replace(R.id.container, new WordFragment(w)).commit();
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

		final GestureDetector gdt = new GestureDetector(getContext(),
				new GestureListener());
		rootView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gdt.onTouchEvent(event);
				return true;
			}
		});

		switch (w.category) {
		case Config.CAT_GOOD:
			tvWord.setTextColor(Config.COLOR_GOOD);
			tvMeaning.setTextColor(Config.COLOR_GOOD);
			break;
		case Config.CAT_BAD:
			tvWord.setTextColor(Config.COLOR_BAD);
			tvMeaning.setTextColor(Config.COLOR_BAD);
			break;
		case Config.CAT_UGLY:
			tvWord.setTextColor(Config.COLOR_UGLY);
			tvMeaning.setTextColor(Config.COLOR_UGLY);
			break;
		default:
			tvWord.setTextColor(Config.COLOR_DEFAULT);
			tvMeaning.setTextColor(Config.COLOR_DEFAULT);
			break;
		}

		return rootView;
	}
	
	private class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				llOptions.setVisibility(View.GONE);
				return false; // Bottom to top
			} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				llOptions.setVisibility(View.VISIBLE);
				return false; // Top to bottom
			}
			return false;
		}
	}
}
