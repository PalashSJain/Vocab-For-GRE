package wordsforgre.words;

import android.os.Parcel;
import android.os.Parcelable;
import wordsforgre.utils.Config;

public class Word implements Parcelable {
	private String word;
	private String meaning;
	private long id;
	private String category = Config.CAT_NEUTRAL;
	
	public Word(String w, String m) {
		this.word = w;
		this.meaning = m;
	}

	public Word() {
		this.word = null;
		this.meaning = null;
	}

	public Word(Parcel in) {
		this.word = in.readString();
		this.meaning = in.readString();
		this.id = in.readLong();
		this.category = in.readString();
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String comment) {
		this.meaning = comment;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
