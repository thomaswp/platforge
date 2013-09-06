package com.twp.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.twp.core.Platforge;

public class PlatforgeActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new Platforge());
  }
}
