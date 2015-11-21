package wordsforgre.quiz;

import wordsforgre.database.AllWordsDbQuery;
import wordsforgre.database.QuizWordsDbQuery;
import wordsforgre.landing.R;
import wordsforgre.utils.Config;
import wordsforgre.words.Word;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuizAnswerFragment extends Fragment {

	Word w;

	public QuizAnswerFragment(Word w) {
		this.w = w;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.quiz_answer_fragment,
				container, false);

		TextView tvAnswerWord = (TextView) rootView
				.findViewById(R.id.tvAnswerWord);
		tvAnswerWord.setText(w.word.toUpperCase());
		switch (w.category) {
		case Config.CAT_GOOD:
			tvAnswerWord.setTextColor(Config.COLOR_GREEN);
			break;
		case Config.CAT_BAD:
			tvAnswerWord.setTextColor(Config.COLOR_YELLOW);
			break;
		case Config.CAT_UGLY:
			tvAnswerWord.setTextColor(Config.COLOR_RED);
			break;
		default:
			tvAnswerWord.setTextColor(Config.COLOR_BLUE);
			break;
		}

		TextView tvAnswerMeaning = (TextView) rootView
				.findViewById(R.id.tvAnswerMeaning);
		tvAnswerMeaning.setText(w.meaning);

		TextView tvYes = (TextView) rootView.findViewById(R.id.tvYes);
		tvYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AllWordsDbQuery allWords = new AllWordsDbQuery(getContext());
				allWords.open();
				long wordId = w.allWordId;
				long[] wordInfo = allWords.getWordInfo(wordId);
				allWords.close();
				long wordActual = wordInfo[0];
				
				if (wordActual == 1) {
					allWords.open();
					allWords.setWordCategoryToGood(wordId);
					allWords.close();
				} else if (wordActual == 2) {
					allWords.open();
					allWords.setWordCategoryToGood(wordId);
					allWords.close();
					removeWordInstanceFromQuizTable(wordId);
				} else if (wordActual > 2 && wordActual <= 8) {
					allWords.open();
					allWords.reduceActualFromWordsTable(wordId, wordActual - 1);
					allWords.close();
					removeWordInstanceFromQuizTable(wordId);
				} else if (wordActual == 9) {
					allWords.open();
					allWords.setWordCategoryToBad(wordId);
					allWords.close();
					removeWordInstanceFromQuizTable(wordId);
				} else {
					allWords.open();
					allWords.reduceActualFromWordsTable(wordId, wordActual - 1);
					allWords.close();
					removeWordInstanceFromQuizTable(wordId);
				}
				
				getFragmentManager().beginTransaction()
						.replace(R.id.container, new QuizQuestionFragment())
						.commit();
			}

			private void removeWordInstanceFromQuizTable(long wordId) {
				QuizWordsDbQuery quizWords = new QuizWordsDbQuery(getContext());
				quizWords.open();
				quizWords.searchAndRemoveWordInstance(wordId);
				quizWords.close();
			}
		});

		TextView tvNo = (TextView) rootView.findViewById(R.id.tvNo);
		tvNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AllWordsDbQuery allWords = new AllWordsDbQuery(getContext());
				allWords.open();
				long wordId = w.allWordId;
				long[] wordInfo = allWords.getWordInfo(wordId);
				allWords.close();
				if (wordInfo[0] != 10 && wordInfo[1] != 10) {
					long wordActual = wordInfo[0];
					long wordExpected = 10; //wordInfo[1];
					allWords.open();
					allWords.increaseExpectedAndActualToMax(wordId);
					allWords.close();
					long numberOfTimesToAddWordInQuizWords = wordExpected - wordActual;
					if (numberOfTimesToAddWordInQuizWords != 0) {
						QuizWordsDbQuery quizWords = new QuizWordsDbQuery(
								getContext());
						quizWords.open();
						for (int i = 0; i < numberOfTimesToAddWordInQuizWords; i++) {
							quizWords.addWord(wordId);
						}
						quizWords.close();
					}
				}
				getFragmentManager().beginTransaction()
				.replace(R.id.container, new QuizQuestionFragment())
				.commit();
			}
		});

		return rootView;
	}
}