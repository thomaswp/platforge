package com.platforge.data;

import java.io.Serializable;

public class EditorData implements Serializable {
	private static final long serialVersionUID = 1L;

	public int layer = 1, editMode = 0;
	public int tileSelectionLeft, tileSelectionTop; 
	public int tileSelectionRight = 1, tileSelectionBottom = 1;
	public int actorSelection, objectSelection;
}