package com.twp.java;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.twp.core.Platforge;

import edu.elon.honors.price.data.PlatformGame;

public class PlatforgeJava {

	public static void main(String[] args) {
		PlatformGame game = new PlatformGame();
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tutorial3.game"));
			game = (PlatformGame) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		JavaPlatform.Config config = new JavaPlatform.Config();
		config.width = 1000;
		config.height = 700;
		// use config to customize the Java platform, if needed
		JavaPlatform.register(config);
		PlayN.run(new Platforge(game));
	}
}
