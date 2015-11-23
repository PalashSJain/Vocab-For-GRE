package wordsforgre.allwords;

import wordsforgre.landing.R;
import wordsforgre.utils.Config;
import wordsforgre.words.Word;
import wordsforgre.words.WordFragment;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

public class AllWordsAdapter extends ArrayAdapter<Word> implements Filterable {

	ViewHolder holder;
	Context context;
	FragmentManager fm;

	public AllWordsAdapter(Activity activity, int fragment, FragmentManager fm) {
		super(activity, fragment);
		this.context = activity;
		this.fm = fm;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final int pos = position;

		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(R.layout.all_words_row, parent, false);

			holder = new ViewHolder();
			holder.tvWord = (TextView) row.findViewById(R.id.tv_f_word);
			holder.tvMeaning = (TextView) row.findViewById(R.id.tvMeaning);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		final Word word = getItem(pos);
		holder.tvWord.setText(word.word.toUpperCase());
		holder.tvMeaning.setText(word.meaning);

		if (pos % 2 == 1) {
			holder.tvWord.setGravity(Gravity.START);
			holder.tvMeaning.setGravity(Gravity.START);
		} else {
			holder.tvWord.setGravity(Gravity.END);
			holder.tvMeaning.setGravity(Gravity.END);
		}
		switch (word.category) {
		case Config.CAT_GOOD:
			holder.tvWord.setTextColor(Config.COLOR_GOOD);
			holder.tvMeaning.setTextColor(Config.COLOR_GOOD);
			break;
		case Config.CAT_BAD:
			holder.tvWord.setTextColor(Config.COLOR_BAD);
			holder.tvMeaning.setTextColor(Config.COLOR_BAD);
			break;
		case Config.CAT_UGLY:
			holder.tvWord.setTextColor(Config.COLOR_UGLY);
			holder.tvMeaning.setTextColor(Config.COLOR_UGLY);
			break;
		default:
			holder.tvWord.setTextColor(Config.COLOR_DEFAULT);
			holder.tvMeaning.setTextColor(Config.COLOR_DEFAULT);
			break;
		}

		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fm.beginTransaction()
						.replace(R.id.container, new WordFragment(word))
						.addToBackStack(null).commit();
			}
		});
		return row;
	}

	private static class ViewHolder {
		TextView tvWord;
		TextView tvMeaning;
	}

}
