package com.twp.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.platforge.game.Formatter;
import com.platforge.game.Formatter.Impl;
import com.platforge.player.core.Platforge;

public class PlatforgeJava {

	public static void main(String[] args) {
		Formatter.impl = new Impl() {
			@Override
			public String format(String format, Object... args) {
				return String.format(format, args);
			}
		};
		
//		PlatformGame game = new PlatformGame();
//		try {
//			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tutorial5.game"));
//			game = (PlatformGame) ois.readObject();
//			ois.close();
//			Upgrader.upgrade(game);
//			
//			String data = PersistData.persistData(game);
//			BufferedWriter writer = new BufferedWriter(new FileWriter("game.txt"));
//			writer.write(data);
//			writer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		

		JavaPlatform.Config config = new JavaPlatform.Config();
		config.width = 1024;
		config.height = 700;
		// use config to customize the Java platform, if needed
		JavaPlatform.register(config);
		PlayN.run(new Platforge());
	}
}
