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
	public static final int COLOR_BLUE = Color.rgb(91, 146, 229);
	public static final int COLOR_GREEN = Color.rgb(3, 192, 60);
	public static final int COLOR_RED = Color.rgb(255, 191, 0);
	public static final int COLOR_YELLOW = Color.rgb(91, 146, 229);
	
}