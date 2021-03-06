package com.platforge.player.java;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.platforge.data.Formatter;
import com.platforge.data.Formatter.Impl;
import com.platforge.data.PlatformGame;
import com.platforge.data.Upgrader;
import com.platforge.data.field.JsonSerializer;
import com.platforge.data.field.PersistData;
import com.platforge.player.core.Platforge;

public class PlatforgeJava {

	public static void main(String[] args) {
		Formatter.impl = new Impl() {
			@Override
			public String format(String format, Object... args) {
				return String.format(format, args);
			}
		};

		JavaPlatform.Config config = new JavaPlatform.Config();
		config.width = 1024;
		config.height = 700;
		// use config to customize the Java platform, if needed
		JavaPlatform.register(config);
		
		try {
			ObjectInputStream ois = new ConversionObjectInputStream(new FileInputStream("tutorial5.game"));
			PlatformGame game = (PlatformGame) ois.readObject();
			ois.close();
			Upgrader.upgrade(game);
			
			String data = JsonSerializer.toJson(game);
			System.out.println(data);
			
//			String data = PersistData.persistData(game);
//			BufferedWriter writer = new BufferedWriter(new FileWriter("game.txt"));
//			writer.write(data);
//			writer.close();
			
			PlatformGame game2 = JsonSerializer.fromJson(data, PlatformGame.class);
			PlayN.run(new Platforge(game2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
