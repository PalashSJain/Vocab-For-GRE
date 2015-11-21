package wordsforgre.quiz;

import java.util.ArrayList;
import java.util.Random;

import wordsforgre.database.AllWordsDbQuery;
import wordsforgre.database.QuizWordsDbQuery;
import wordsforgre.landing.R;
import wordsforgre.utils.Config;
import wordsforgre.words.Word;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

public class QuizQuestionFragment extends Fragment {
	private PieChart mChart;
	private Typeface tf;

	protected String[] mCategories = new String[] { Config.CAT_NEUTRAL,
			Config.CAT_GOOD, Config.CAT_BAD, Config.CAT_UGLY };

	public QuizQuestionFragment() {
		// TODO Auto-generated constructor stub
		// this.words = getWordsFromSomewhere();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.quiz_question_fragment,
				container, false);

		final Word w = getRandomRecordFromQuizTable();

		TextView tvQuizWord = (TextView) rootView.findViewById(R.id.tvQuizWord);
		tvQuizWord.setText(w.word);

		TextView tvWordType = (TextView) rootView
				.findViewById(R.id.tvQuizWordCategory);
		tvWordType.setText(w.category);
		switch (w.category) {
		case Config.CAT_GOOD:
			tvWordType.setTextColor(Config.COLOR_GREEN);
			break;
		case Config.CAT_BAD:
			tvWordType.setTextColor(Config.COLOR_YELLOW);
			break;
		case Config.CAT_UGLY:
			tvWordType.setTextColor(Config.COLOR_RED);
			break;
		default:
			tvWordType.setTextColor(Config.COLOR_BLUE);
			break;
		}

		Button bShowMeaning = (Button) rootView
				.findViewById(R.id.bQuizCheckMeaning);
		bShowMeaning.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.container, new QuizAnswerFragment(w))
						.commit();
			}
		});

		mChart = (PieChart) rootView.findViewById(R.id.chart1);
		populatePieChart();
		return rootView;
	}

	private Word getRandomRecordFromQuizTable() {
		QuizWordsDbQuery quizWords = new QuizWordsDbQuery(getContext());
		quizWords.open();
		long allWordId = quizWords.getAllWordsIdOfRandomWord();
		quizWords.close();
		AllWordsDbQuery allWords = new AllWordsDbQuery(getContext());
		allWords.open();
		Word w = allWords.getWordAtIndex(allWordId);
		allWords.close();
		return w;
	}

	private void populatePieChart() {
		mChart.setUsePercentValues(true);
		mChart.setDescription("");
		mChart.setExtraOffsets(5, 10, 5, 5);

		mChart.setDragDecelerationFrictionCoef(0.95f);

		tf = Typeface.createFromAsset(getActivity().getAssets(),
				"OpenSans-Regular.ttf");

		mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity()
				.getAssets(), "OpenSans-Light.ttf"));
		// mChart.setCenterText(generateCenterSpannableText());

		mChart.setDrawHoleEnabled(true);
		mChart.setHoleColorTransparent(true);

		mChart.setTransparentCircleColor(Color.WHITE);
		mChart.setTransparentCircleAlpha(110);

		mChart.setHoleRadius(2f);
		mChart.setTransparentCircleRadius(4f);

		mChart.setDrawCenterText(true);

		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(false);
		mChart.setHighlightPerTapEnabled(true);

		setData(3, 100);

		mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

		mChart.getLegend().setEnabled(false);
	}

	private void setData(int count, float range) {

		float mult = range;

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		// for (int i = 0; i < count + 1; i++) {
		// yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
		// }
		yVals1.add(new Entry(20, 0));
		yVals1.add(new Entry(15, 1));
		yVals1.add(new Entry(10, 2));
		yVals1.add(new Entry(5, 3));

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count + 1; i++)
			xVals.add(mCategories[i % mCategories.length]);

		PieDataSet dataSet = new PieDataSet(yVals1, "Current Stats");
		dataSet.setSliceSpace(2f);
		dataSet.setSelectionShift(5f);

		// add a lot of colors
		dataSet.setColors(new int[] { Config.COLOR_BLUE, Config.COLOR_GREEN,
				Config.COLOR_YELLOW, Config.COLOR_RED });
		// dataSet.setSelectionShift(0f);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.BLACK);
		data.setValueTypeface(tf);
		mChart.setData(data);

		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}

}
