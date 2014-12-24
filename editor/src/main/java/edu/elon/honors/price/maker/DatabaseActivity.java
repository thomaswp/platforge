package edu.elon.honors.price.maker;

import java.io.Serializable;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import edu.elon.honors.price.data.Behavior;
import edu.elon.honors.price.data.Event;
import edu.elon.honors.price.data.GameData;
import edu.elon.honors.price.data.PlatformGame;
import edu.elon.honors.price.data.tutorial.Tutorial.EditorButton;
import edu.elon.honors.price.maker.tutorial.Tutorial1;

/**
 * A {@link SaveableActivity} specifically for {@link PlatformGame}
 * objects. This class handles retrieving the game from
 * the parent Activity, checking for changes and returning it
 * when this Activity finishes.
 */
public class DatabaseActivity extends SaveableActivity implements IViewContainer {

	public static final int REQUEST_RETURN_GAME = 10;
	
	protected PlatformGame game;
	protected Button buttonHelp;
	//protected Bundle extras;
	
	/**
	 * Creates the activity, setting the appropriate window
	 * feature and flags. Also retrieves the game, either from
	 * the savedInstanceSate (if not null) or the calling
	 * Intent.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Bundle  extras = getIntent().getExtras();
		
		if (savedInstanceState != null && savedInstanceState.containsKey("game")) {
			game = (PlatformGame)savedInstanceState.getSerializable("game");
		}
		if (game == null) {
			game = (PlatformGame)extras.getSerializable("game");
		}

		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		buttonHelp = (Button)findViewById(R.id.buttonHelp);
		if (buttonHelp != null) {
			buttonHelp.setVisibility(game.tutorial != null ? View.VISIBLE : View.GONE);
		}
		TutorialUtils.addHighlightable(buttonHelp, EditorButton.DatabaseHelp, this);
	}
	
	@Override
	protected void setDefaultButtonActions() {
		super.setDefaultButtonActions();
		buttonHelp = (Button)findViewById(R.id.buttonHelp);
		if (buttonHelp != null) {
			if (game.tutorial != null) {
				buttonHelp.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						TutorialUtils.backOneMessage(DatabaseActivity.this);
					}
				});
			}
		}
	}
	
	public void showAlert(String title, String message) {
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("Ok", null)
		.show();
	}
	
	protected void populateViews(View root) {
		populateViews(root, game);
	}
	
	public static void populateViews(View root, PlatformGame game) {
		if (root instanceof IPopulatable) {
			((IPopulatable) root).populate(game);
		}
		if (root instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup)root;
			for (int i = 0; i < vg.getChildCount(); i++) {
				populateViews(vg.getChildAt(i), game);
			}
		}
	}
	
	public static int setPopulatableViewIds(View root, int startId) {
		if (root instanceof IPopulatable) {
			root.setId(startId++);
		}
		if (root instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup)root;
			for (int i = 0; i < vg.getChildCount(); i++) {
				startId = setPopulatableViewIds(
						vg.getChildAt(i), startId);
			}
		}
		return startId;
	}
	
	/**
	 * Creates a new Intent with this as its context
	 * and the given class as its class, then adds
	 * the game as an extra and returns it.
	 * @param cls The activity class to call
	 * @return The new intent
	 */
	protected Intent getNewGameIntent(Class<? extends Activity> cls) {
		Intent intent = new Intent(this, cls);
		intent.putExtra("game", game);
		return intent;
	}
	
	/**
	 * Wrapper for <code>extras.getSerializable(key)</code>
	 */
	public Serializable getExtra(String key) {
		return getIntent().getExtras().getSerializable(key);
	}
	
	/**
	 * Get the given Serializable extra from this
	 * Activity's Intent and casts it to the given class. 
	 * @param <T> The class to cast to
	 * @param key The key
	 * @param c The class to cast to
	 * @return The extra, or null if it does not exist
	 * or if it is not castable to the given type.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T getExtra(String key, Class<T> c) {
		Serializable s = getIntent().getExtras().getSerializable(key);
		if (s == null || !c.isInstance(s)) return null;
		return (T)s;
	}
	
	/**
	 * Get the given Behavior extra from this
	 * Activity's Intent.
	 * @param key The key
	 * @return The extra, or null if it does not exist
	 * or if it is not castable to the given type.
	 */
	public Behavior getBehaviorExtra(String key) {
		return getExtra(key, Behavior.class);
	}
	
	/**
	 * Get the given Event extra from this
	 * Activity's Intent.
	 * @param key The key
	 * @return The extra, or null if it does not exist
	 * or if it is not castable to the given type.
	 */
	public Event getEventExtra(String key) {
		return getExtra(key, Event.class);
	}
	
	protected void autoAssign() {
		AutoAssignUtils.autoAssign(this);
	}


	/**
	 * Compares the game field to the original
	 * that was passed to this Activity through
	 * the intent. Returns true if the two are
	 * different. This method should be overwritten
	 * if you have additional things to check, or
	 * if there is only one thing that could have changed
	 * (no variable could be modifies, for instance), and
	 * you will check this yourself.
	 * @return true if changes have been made which would
	 * require saving 
	 */
	@Override
	protected boolean hasChanged() {
		//long time = System.currentTimeMillis();
		PlatformGame oldGame = (PlatformGame)getIntent().getExtras().getSerializable("game");
		boolean r = !GameData.areEqual(oldGame, game);
		//time = System.currentTimeMillis() - time;
		//Debug.write("Game compared in " + time + "ms");
		return r;
	}

	/**
	 * Finishes with RESULT_OK status and passes
	 * an intent with the game as an extra to the
	 * parent Activity. Also calls {@link #putExtras(Intent)}
	 * for you to add any other extras to be
	 * passed back to the parent. This method cannot
	 * itself be overwritten.
	 */
	@Override
	protected final void finishOk(Intent intent) {
		intent.putExtra("game", game);
		putExtras(intent);
		super.finishOk(intent);
	}	


	/**
	 * Called when the game is finishing and passing
	 * data back to its parent. If you have any extras
	 * you want passed to the parent, add them here.
	 * @param intent
	 */
	protected void putExtras(Intent intent) { }
	
	/**
	 * Calls {@link #onFinishing()} and adds the game to
	 * the outState to be retrieved when the activity
	 * is restarted. Which the game itself it saved and
	 * reloaded with this method, most subclasses do
	 * not currently have good save state logic and will
	 * not function 100% correctly after being
	 * finished and reconstructed.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) { 
		super.onSaveInstanceState(outState);
		onFinishing();
		outState.putSerializable("game", game);
	}
	
	/**
	 * Retrieves the game from any a returning child activity
	 * if it exists and returned with RESULT_OK.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//removed so that Selectors can take advantage of the system 
		//if (requestCode == REQUEST_RETURN_GAME) { 
		if (resultCode == RESULT_OK) {
			if (data.hasExtra("game")) {
				Serializable obj = data.getExtras().getSerializable("game");;
				if (obj instanceof PlatformGame) {
					game = (PlatformGame) obj;
				}
			}
		} else {
			
		}
		//}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected String getPreferenceId() {
		return game.ID;
	}
}
