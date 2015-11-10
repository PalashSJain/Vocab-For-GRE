package wordsforgre.allwords;

import java.util.List;

import wordsforgre.landing.R;
import wordsforgre.words.Word;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AllWordsAdapter extends ArrayAdapter<Word> implements Filterable{

	ViewHolder holder;
	Context context;
	private AllWordsAdapter adapter;
	private List<Word> words;

	public AllWordsAdapter(Context context, List<Word> words) {
		super(context, R.layout.all_words_fragment, words);
		this.words = words;
		this.context = context;
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
			row.setTag(holder);

		} else {
			holder = (ViewHolder) row.getTag();
		}

		final Word word = getItem(pos);
		final LinearLayout exp = (LinearLayout) row.findViewById(R.id.expandedOptions);
		holder.tvWord.setText(word.getWord());
		holder.tvMeaning.setText(word.getMeaning());
		holder.ibDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: Add confirmation dialogue here
				AllWordsDbQuery allWords = new AllWordsDbQuery(context);
				allWords.open();
				allWords.deleteWord(word);
				allWords.close();
				words.remove(pos);
				adapter.notifyDataSetChanged();
				exp.setVisibility(View.GONE);
			}
		});
		final RelativeLayout rlOrigWord = (RelativeLayout) row.findViewById(R.id.shownWord);
		final RelativeLayout rlModifyWord = (RelativeLayout) row.findViewById(R.id.editWord);
		holder.etWord = (EditText) row.findViewById(R.id.etModifyWord);
		holder.etMeaning = (EditText) row.findViewById(R.id.etModifyMeaning);
		holder.ibReset = (ImageButton) row.findViewById(R.id.iReset);
		holder.ibUpdate = (ImageButton) row.findViewById(R.id.iUpdate);
		holder.ibEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rlOrigWord.setVisibility(View.GONE);
				rlModifyWord.setVisibility(View.VISIBLE);
				holder.etWord.setText(word.getWord());
				holder.etMeaning.setText(word.getMeaning());
			}
		});
		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (exp.getVisibility() == View.VISIBLE) {
					exp.setVisibility(View.GONE);
				} else {
					exp.setVisibility(View.VISIBLE);
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
		// Edit screen
		EditText etWord;
		EditText etMeaning;
		ImageButton ibReset;
		ImageButton ibUpdate;
	}
}
