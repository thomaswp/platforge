package com.platforge.action.writer;

import java.io.StringWriter;

import org.xml.sax.Attributes;

public class ScriptableWriter extends ActionFragmentWriter {
	
	protected final static String[] IMPORTS = new String[] {
			"com.platforge.data.*",
			"com.platforge.data.types.*",
			"com.platforge.data.Event.Parameters",
			"com.platforge.data.Event.Parameters.Iterator",
			"com.platforge.player.core.input.*",
			"com.platforge.player.core.platform.*",
			"com.platforge.player.core.action.*",
	};
	
	protected final static String PACKAGE = "com.platforge.data.action";
	
	public String fileName;
	protected String readableName;
	protected int id;
	protected String category;
	protected boolean parent;
	
	@Override
	protected String getSuperclass() {
		return "ScriptableInstance";
	}
	
	public ScriptableWriter(StringWriter writer, String qName, Attributes atts, 
			boolean isAction) {
		super(writer, qName, atts);
		name = (isAction ? "Action" : "Trigger") + name;
		fileName = camel(name, true);
		readableName = atts.getValue("name");
		id = Integer.parseInt(atts.getValue("id"));
		String parentS = atts.getValue("parent");
		if (parentS != null) parent = Boolean.parseBoolean(parentS);
		category = atts.getValue("category");
	}

	@Override
	public void writeHeader() {
		writeLn("package %s;", PACKAGE);
		writeLn();
		
		for (String s : IMPORTS) {
			writeLn("import %s;", s);
		}
		writeLn();

		javadoc.add(String.format("%03d <b><i>%s</i></b> (%s)<br />", id, readableName, category));
		writeLn("@SuppressWarnings(%s)", quote("unused"));
		super.writeHeader();
		
		writeConstant("String", "NAME", quote(readableName));
		writeConstant("int", "ID", "" + id);
		writeConstant("String", "CATEGORY", category == null ? "null" : quote(category));
		
		writeLn();
		
		
	}
	
//	@Override
//	public void writeFooter() {
//		writeJavadoc();
//		super.writeFooter();
//	}
}
