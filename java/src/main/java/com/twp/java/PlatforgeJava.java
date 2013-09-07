package com.twp.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.twp.core.Platforge;

import edu.elon.honors.price.data.PlatformGame;

public class PlatforgeJava {

	public static void main(String[] args) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tutorial1.game"));
			PlatformGame game = (PlatformGame) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		JavaPlatform.Config config = new JavaPlatform.Config();
		// use config to customize the Java platform, if needed
		JavaPlatform.register(config);
		PlayN.run(new Platforge());
	}
}
