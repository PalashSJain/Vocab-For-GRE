package wordsforgre.quiz;

import java.util.ArrayList;

import wordsforgre.landing.R;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

public class QuizQuestionFragment extends Fragment {
	private PieChart mChart;
	private Typeface tf;

	protected String[] mCategories = new String[] {"Unread", "Mastered", "Practising", "Learning"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.quiz_question_fragment,
				container, false);
		
		Button bShowMeaning = (Button) rootView.findViewById(R.id.bQuizCheckMeaning);
		bShowMeaning.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showMeaning(v);
			}
		});

		mChart = (PieChart) rootView.findViewById(R.id.chart1);
		populatePieChart();
		return rootView;
	}
	
	private void showMeaning(View v) {
		Toast.makeText(getContext(), "ShowMeaning Baby", Toast.LENGTH_LONG).show();
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
//		mChart.setCenterText(generateCenterSpannableText());

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
		// mChart.spin(2000, 0, 360);
		
		mChart.getLegend().setEnabled(false);

//		Legend l = mChart.getLegend();
//		l.setPosition(LegendPosition.RIGHT_OF_CHART);
//		l.setXEntrySpace(7f);
//		l.setYEntrySpace(0f);
//		l.setYOffset(0f);
	}

	private void setData(int count, float range) {

		float mult = range;

		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
//		for (int i = 0; i < count + 1; i++) {
//			yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
//		}
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
		dataSet.setColors(new int[] { Color.rgb(91, 146, 229), Color.rgb(3, 192, 60), Color.rgb(255, 191, 0), Color.rgb(227, 38, 54) });
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
