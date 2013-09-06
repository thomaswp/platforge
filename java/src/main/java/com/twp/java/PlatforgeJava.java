package com.twp.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.twp.core.Platforge;

public class PlatforgeJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    PlayN.run(new Platforge());
  }
}
