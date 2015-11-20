package wordsforgre.words;

import android.os.Parcel;
import android.os.Parcelable;
import wordsforgre.utils.Config;

public class Word implements Parcelable {
	public String word;
	public String meaning;
	public long id;
	public String category;// = Config.CAT_NEUTRAL;
	public long actual = 1, expected = 1;
	public long allWordId = -1;
	
	public Word(String w, String m) {
		this.word = w;
		this.meaning = m;
		this.category = Config.CAT_NEUTRAL;
	}

	public Word() {
		this.word = null;
		this.meaning = null;
		this.category = Config.CAT_NEUTRAL;
	}

	public Word(Parcel in) {
		this.word = in.readString();
		this.meaning = in.readString();
		this.id = in.readLong();
		this.category = in.readString();
	}

	public Word(long id, String word, String meaning, long actual, long expected) {
		this.id = id;
		this.word = word;
		this.meaning = meaning;
		this.actual = actual;
		this.expected = expected;
		this.category = Config.CAT_NEUTRAL;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(word);
		dest.writeString(meaning);
		dest.writeLong(id);
		dest.writeString(category);
	}
	
	public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
		public Word createFromParcel(Parcel in) {
			return new Word(in);
		}

		public Word[] newArray(int size) {
			return new Word[size];
		}
	};
}
