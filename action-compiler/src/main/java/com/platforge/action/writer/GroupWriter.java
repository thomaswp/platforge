package com.platforge.action.writer;

import java.io.StringWriter;

import org.xml.sax.Attributes;

public class GroupWriter extends ExpandableWriter {

	public GroupWriter(StringWriter writer, String qName, Attributes atts, int tab) {
		super(writer, qName, atts, tab);
	}

}
