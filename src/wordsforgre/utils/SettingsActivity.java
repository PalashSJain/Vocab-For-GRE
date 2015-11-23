package wordsforgre.utils;

import wordsforgre.database.AllWordsDbQuery;
import wordsforgre.database.QuizWordsDbQuery;
import wordsforgre.landing.R;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final Context context = getContext();

			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);

			Button bResetQuestionList = (Button) rootView
					.findViewById(R.id.bResetQuestionList);
			bResetQuestionList.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);
					alertDialogBuilder
							.setMessage("Resetting the questions list will bring back all the words to the Untest status. Press Yes to confirm, No to reject.");
					alertDialogBuilder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									QuizWordsDbQuery quizWords = new QuizWordsDbQuery(
											context);
									quizWords.open();
									quizWords.resetList();
									quizWords.close();
									AllWordsDbQuery allWords = new AllWordsDbQuery(
											context);
									allWords.open();
									allWords.reinitializeWords();
									allWords.close();
								}
							});
					alertDialogBuilder.setNegativeButton("Nope",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
								}
							});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
			});

			return rootView;
		}
	}

}
