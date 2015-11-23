package wordsforgre.allwords;

import java.util.ArrayList;

import wordsforgre.database.AllWordsDbQuery;
import wordsforgre.landing.R;
import wordsforgre.words.Word;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

public class AllWordsFragment extends ListFragment {

	AllWordsDbQuery allWords;
	AllWordsAdapter adapter;
	private OnFragmentInteractionListener mListener;

	public AllWordsFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    RetrieveWordListTask rwlt = new RetrieveWordListTask();
		rwlt.execute();

	    adapter = new AllWordsAdapter(getActivity(),
                R.layout.all_words_fragment, getFragmentManager());
        setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.all_words_fragment, container, false);
		
		EditText autoCompleteSearchBox = (EditText) rootView.findViewById(R.id.autoCompleteSearchBox);
		autoCompleteSearchBox.setHint("Search word");
		autoCompleteSearchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str = s.toString();
				if (str.length() > 0) {
					SearchedWordListTask swlt = new SearchedWordListTask(str);
					swlt.execute();
				} else {
					RetrieveWordListTask rwlt = new RetrieveWordListTask();
					rwlt.execute();
				}
				setListAdapter(adapter);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
	        ClassCastException tothrow = new ClassCastException(activity.toString()
	                + " must implement OnFragmentInteractionListener");
	        tothrow.initCause(e);
	        throw tothrow;
	    }
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mListener.onFragmentInteraction(item.getItemId(), 0);
		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (mListener != null) {
			mListener.onFragmentInteraction(l.getId(), adapter
					.getItem(position).id);
		}
	}

	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(int mnuId, long l);
	}

	private class RetrieveWordListTask extends AsyncTask<Void, String, ArrayList<Word>> {

        @Override
        protected ArrayList<Word> doInBackground(Void... params) {
        	AllWordsDbQuery allWords = new AllWordsDbQuery(getActivity());
			allWords.open();
			ArrayList<Word> words = allWords.getAllWords();
			allWords.close();
            return words;
        }

        @Override
        protected void onPostExecute(ArrayList<Word> result) {
            super.onPostExecute(result);
            
            adapter.clear();
            adapter.addAll(result); //which will call notifydatasetchanged
        }
	}
	
	private class SearchedWordListTask extends AsyncTask<Void, String, ArrayList<Word>> {

		String queryString = "";
		
        public SearchedWordListTask(String query) {
        	this.queryString = query;
		}

		@Override
        protected ArrayList<Word> doInBackground(Void... params) {
        	AllWordsDbQuery allWords = new AllWordsDbQuery(getActivity());
			allWords.open();
			ArrayList<Word> words = allWords.getMatchingWords(queryString);
			allWords.close();
            return words;
        }

        @Override
        protected void onPostExecute(ArrayList<Word> result) {
            super.onPostExecute(result);
            
            adapter.clear();
            adapter.addAll(result); //which will call notifydatasetchanged
        }
	}

}
