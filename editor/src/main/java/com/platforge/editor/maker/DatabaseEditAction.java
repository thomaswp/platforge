package com.platforge.editor.maker;

import java.io.IOException;
import java.io.InputStream;

import com.platforge.data.Event.Action;
import com.platforge.data.Event.Parameters;
import com.platforge.editor.data.Data;
import com.platforge.player.core.action.Element;
import com.platforge.player.core.action.EventContext;
import com.platforge.player.core.action.ScriptParser;
import com.platforge.player.core.game.Debug;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class DatabaseEditAction extends DatabaseEditScript implements IEventContextual {

	@Override
	protected InputStream getXmlStream(int id) throws IOException {
		return Data.loadAction(id, this);
	}

	@Override
	protected void putExtras(Intent intent, Parameters params) {
		Action action = new Action(id, params);
		action.description = rootElement.getDescription(game);
		intent.putExtra("action", action);
		Debug.write(params);
	}

	@Override
	protected Parameters getOriginalParameters() {
		return (Parameters)getIntent().getExtras().getSerializable("params");
	}
}
