package wordsforgre.utils;

import android.graphics.Color;

public class Config {
	// Navigation Drawer Sequence of Activities
	public static final int QUIZ_NUM = 1;
	public static final int ALLWORDS_NUM = 2;
	public static final int ABOUT_NUM = 3;
	
	// Categories of words
	public static final String CAT_NEUTRAL = "Untested";
	public static final String CAT_GOOD = "Mastered";
	public static final String CAT_BAD = "Practising";
	public static final String CAT_UGLY = "Learning";
	
	// XML NODE NAMES
	public static final String XML_PARENT_NODE = "wordblock";
	public static final String XML_CHILD_NODE_WORD = "word";
	public static final String XML_CHILD_NODE_MEANING = "meaning";
	
	// Colors
	public static final int COLOR_DEFAULT = Color.rgb(91, 146, 229); //5B92E5
	public static final int COLOR_GOOD = Color.rgb(3, 192, 60); //03C03C
	public static final int COLOR_BAD = Color.rgb(255, 140, 0); // ffd700 
	public static final int COLOR_UGLY = Color.rgb(227, 38, 54); // E32636
 	
}