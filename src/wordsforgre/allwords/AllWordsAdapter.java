package wordsforgre.allwords;

import wordsforgre.database.AllWordsDbQuery;
import wordsforgre.landing.R;
import wordsforgre.utils.Config;
import wordsforgre.words.Word;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class AllWordsAdapter extends ArrayAdapter<Word> implements Filterable{

	ViewHolder holder;
	Context context;
	private AllWordsAdapter adapter;

	public AllWordsAdapter(FragmentActivity activity, int fragment) {
		super(activity, fragment);
		this.context = activity;
		this.adapter = this;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final int pos = position;
		
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(R.layout.all_words_row, parent,
					false);

			holder = new ViewHolder();
			holder.tvWord = (TextView) row.findViewById(R.id.tvWord);
			holder.tvMeaning = (TextView) row.findViewById(R.id.tvMeaning);
			holder.ibDelete = (ImageButton) row.findViewById(R.id.imageDeleteWord);
			holder.ibEdit = (ImageButton) row.findViewById(R.id.imageEditWord);
			// Edit elements
			holder.etWord = (EditText) row.findViewById(R.id.etModifyWord);
			holder.etMeaning = (EditText) row.findViewById(R.id.etModifyMeaning);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		final Word word = getItem(pos);
		final ViewSwitcher options = (ViewSwitcher) row.findViewById(R.id.vsOptions);
		final ViewSwitcher wordAndMeaning = (ViewSwitcher) row.findViewById(R.id.vsWord);
		holder.tvWord.setText(word.word.toUpperCase());
		holder.tvMeaning.setText(word.meaning);
		
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
		
		holder.ibDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: Add confirmation dialogue here
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						AllWordsDbQuery allWords = new AllWordsDbQuery(context);
						allWords.open();
						allWords.deleteWord(word);
						allWords.close();
					}
				});
				thread.start();
				options.setVisibility(View.GONE); 
				adapter.remove(word);
				adapter.notifyDataSetChanged();
			}
		});
		holder.ibEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Edition of Words is disabled for now.", Toast.LENGTH_LONG).show();
//				options.showNext();
//				wordAndMeaning.showNext();
			}
		});
		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (options.getVisibility() == View.VISIBLE) {
					options.setVisibility(View.GONE);
				} else if (options.getVisibility() == View.GONE){
					options.setVisibility(View.VISIBLE);
				}
			}
		});
		return row;
	}

	private static class ViewHolder {
		TextView tvWord;
		TextView tvMeaning;
		ImageButton ibDelete;
		ImageButton ibEdit;
		
		// Edit Elements
		EditText etWord;
		EditText etMeaning;
	}

	public void clearAll() {
		// TODO Auto-generated method stub
	}
}
