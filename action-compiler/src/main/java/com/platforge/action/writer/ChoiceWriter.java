package com.platforge.action.writer;

import java.io.StringWriter;

import org.xml.sax.Attributes;

public class ChoiceWriter extends ExpandableWriter {

	
	public ChoiceWriter(String name, StringWriter writer, String qName, Attributes atts, int tab) {
		super(writer, qName, atts, tab);
		this.name = name;
	}
	
}
