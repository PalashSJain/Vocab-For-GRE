package wordsforgre.words;


import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WordXMLHandler extends DefaultHandler {

	boolean currentElement = false;
	String currentValue = "";

	String word;
	String meaning;
	// the list of data
    private List<Word> words = new ArrayList<Word>();
	// new object
    private Word w;

	public String getWord() {
		return word;
	}

	public String getMeaning() {
		return meaning;
	}

	public List<Word> getWordList() {
		return words;
	}
	
    // accumulate the values
    private StringBuilder mStringBuilder = new StringBuilder();
 
    /*
     * Called when parsed data is requested.
     */
    public List<Word> getParsedData() {
        return this.words;
    }
 
    // Methods below are built in, we just have to do the tweaks.
 
    /*
     * @Receive notification of the start of an element.
     *
     * @Called in opening tags such as <Owner>
     */
    @Override
    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
        if (localName.equals("wordblock")) {
            // meaning new data object will be made
            this.w = new Word();
        }
    }
 
    /*
     * @Receive notification of the end of an element.
     *
     * @Called in end tags such as </Owner>
     */
    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        // Owners
        if (localName.equals("word")) {
        	w.setWord(mStringBuilder.toString().trim());
        } else if (localName.equals("meaning")) {
            w.setMeaning(mStringBuilder.toString().trim());
        } else if (localName.equals("wordblock")) {
            this.words.add(w);
        }
        // empty our string builder
        mStringBuilder.setLength(0);
    }
 
    /*
     * @Receive notification of character data inside an element.
     *
     * @Gets be called on the following structure: <tag>characters</tag>
     */
    @Override
    public void characters(char ch[], int start, int length) {
        // append the value to our string builder
        mStringBuilder.append(ch, start, length);
    }

}