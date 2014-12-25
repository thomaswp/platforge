package com.platforge.editor.maker;

import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;

public class TextUtils {

	public static final String COLOR_VARIABLE = "#00CC00";
	public static final String COLOR_MODE = "#FFCC00";
	public static final String COLOR_VALUE = "#5555FF";
	public static final String COLOR_ACTION = "#8800FF";
	public static final String COLOR_DISABLED = "#444444";
	
	public static void addColoredText(StringBuilder sb, int text, String color) {
		addColoredText(sb, "" + text, color);
	}
	
	public static void  addColoredText(StringBuilder sb, String text, String color) {
		text = android.text.TextUtils.htmlEncode(text);
		if (color == null) {
			sb.append(text);
			return;
		}
		sb.append("<font color='")
		.append(color)
		.append("'>")
		.append(text)
		.append("</font>");
	}

	public static void addColoredText(StringBuilder sb, float text,
			String color) {
		addColoredText(sb, String.format("%.02f", text), color);
		
	}
	
	public static String getColoredText(String text, int color) {
		return getColoredText(text, getColorString(color));
	}
	
	public static String getItalicizedText(String text) {
		return "<i>" + text + "</i>";
	}
	
	public static String getColoredText(String text, String color) {
		StringBuilder sb = new StringBuilder();
		addColoredText(sb, text, color);
		return sb.toString();
	}
	
	public static String getColorString(int color) {
		return String.format("#%02X%02X%02X", Color.red(color), Color.green(color),
				Color.blue(color));
	}
	
	public static String HTMLEscape(String text) {
		return replaceEach(text, 
				new String[]{"&", "\"", "<", ">"}, 
				new String[]{"&amp;", "&quot;", "&lt;", "&gt;"});
	}
	
	public static String replaceEach(String text, 
			String[] replace, String[] with) {
		for (int i = 0; i < replace.length; i++) {
			if (i >= with.length) return text;
			text = text.replace(replace[i], with[i]);
		}
		return text;
	}

	private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
	public static void strikeText(Spannable text) {
		text.setSpan(STRIKE_THROUGH_SPAN, 0, text.length(), 
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
}
