package wordsforgre.words;

public class Word {
	private String word;
	private String meaning;
	private long id;
	
	public Word(String w, String m) {
		this.word = w;
		this.meaning = m;
	}

	public Word() {
		this.word = null;
		this.meaning = null;
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
}
