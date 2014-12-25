package com.platforge.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.platforge.data.Event.Action;
import com.platforge.data.Event.Parameters;

public class Upgrader {

	public final static int LATEST_VERSION = 20;

	@SuppressWarnings("deprecation")
	public static void upgrade(PlatformGame game) {
		int version = game._VERSION_;
		if (version  < 4) {
			//Shift from actorLayer to plain actors list
			for (Map map : game.maps) {
				if (map.actorLayer != null) {
					int[][] tiles = map.actorLayer.tiles;
					ArrayList<ActorInstance> newActors = new ArrayList<ActorInstance>();
					for (int i = 0; i < tiles.length; i++) {
						for (int j = 0; j < tiles[i].length; j++) {
							if (tiles[i][j] >= 0) {
								ActorInstance instance = map.actors.get(tiles[i][j]);
								if (instance != null) {
									instance.row = i;
									instance.column = j;
									instance.id = tiles[i][j];
									newActors.add(instance);
								}
							}
						}
					}
					Collections.sort(newActors);
					map.actors.clear();
					map.actors.addAll(newActors);
					map.actorLayer = null;
				}
			}
			upgraded(game);
		}
		if (version < 5) {
			for (ActorClass actor : game.actors) {
				actor.imageName = Directories.ACTOR_7 + actor.imageName;
				DataDebug.write(actor.imageName);
			}
			upgraded(game);
		}
		if (version < 6) {
			game.name = "New Game";
			upgraded(game);
		}
		if (version < 7) {
			game.tilesets = Arrays.copyOf(game.tilesets, 2);
			upgraded(game);
		}
		if (version < 8) {
			for (ObjectClass object : game.objects) {
				object.moves = true;
				object.rotates = true;
			}
			upgraded(game);
		}
		if (version < 9) {
			for (Event event : game.getAllEvents()) {
				event.triggers.clear();
			}
			upgraded(game);
		}
		if (version < 10) {
			for (Event event : game.getAllEvents()) {
				for (int i = 0; i < event.actions.size(); i++) {
					Action action = event.actions.get(i); 
					if (action.id == 7) { //If
						Action actionElse = new Action(22, new Parameters()); //Else
						actionElse.description = "<i><font color='#8800FF'>Else</font></i>";
						actionElse.dependsOn = action;
						actionElse.indent = action.indent;
						int j = i + 1;
						while (j < event.actions.size() && 
								event.actions.get(j).indent > action.indent) j++;
						event.actions.add(j, actionElse);
					}
				}
			}
			upgraded(game);
		}
		if (version < 11) {
			for (Map map : game.maps) {
				map.gravity = new Vector(0, 10);
			}
			upgraded(game);
		}
		if (version < 12) {
			for (ActorClass actor : game.actors) {
				actor.color = Color.WHITE;
			}
			for (ObjectClass object : game.objects) {
				object.color = Color.WHITE;
			}
			upgraded(game);
		}
		if (version < 13) {
			for (Event e : game.getAllEvents()) {
				for (Action action : e.actions) {
					if (action.id == 20) { // Wait
						int time = action.params.getInt();
						Parameters params = new Parameters();
						params.addParam(0); params.addParam(time);
						action.params = new Parameters();
						action.params.addParam(params);
						
					}
				}
			}
			upgraded(game);
		}
		if (version < 14) {
			for (ObjectClass o : game.objects) {
				o.friction = 1;
			}
			upgraded(game);
		}
		if (version < 15) {
			for (ActorClass actor : game.actors) {
				if (actor.imageName.contains("hero")) {
					actor.imageName = "a5/ninja.png";
				}
				if (actor.imageName.contains("StickMan") ||
						actor.imageName.contains("Skull")) {
					actor.imageName = "a2/skeleton.png";
				}
				if (actor.imageName.contains("Gloop")) {
					actor.imageName = "a2/gloop.png";
				}
				if (actor.imageName.contains("ghost")) {
					actor.imageName = "a2/ghost.png";
				}
			}
			upgraded(game);
		}
		if (version < 16) {
			game.tilesets = Arrays.copyOf(game.tilesets, 3);
			game.tilesets[2] = new Tileset("Castle", "castle.png", 64, 64, 8, 8);
			
			upgraded(game);
		}
		if (version < 17) {
			for (Event event : game.getAllEvents()) {
				for (Action action : event.actions) {
					if (action.id == 24) {
						action.params.addParam(0);
					}
				}
			}
			
			upgraded(game);
		}
		if (version < 18) {
			game.tilesets = Arrays.copyOf(game.tilesets, game.tilesets.length + 1);
			game.tilesets[game.tilesets.length - 1] = new Tileset("Night", "night.png", 64, 64, 8, 8);
			
			upgraded(game);
		}
		if (version < 19) {
			int[] map_grass = new int[] {
					0, 1, 2, 0, 0, 0, 0, 0,
					8, 9, 10, 0, 11, 12, 13, 14, 
					16, 16, 16, 0, 19, 0, 0, 22, 
					24, 16, 26, 0, 0, 0, 0, 0, 
					32, 33, 34, 15, 36, 37, 0, 0, 
					40, 41, 42, 0, 44, 45, 53, 52, 
					48, 49, 50, 0, 0, 0, 43, 46, 
					56, 57, 58, 0, 0, 0, 0, 0
			};
			int[] map_castle = new int[] {
					0, 0, 0, 0, 0, 0, 0, 0, 
					8, 9, 10, 15, 11, 12, 13, 14, 
					24, 16, 26, 23, 19, 36, 37, 22, 
					38, 39, 31, 0, 43, 44, 45, 46, 
					60, 61, 62, 63, 52, 53, 54, 55, 
					48, 49, 0, 0, 0, 0, 0, 0, 
					56, 57, 0, 0, 0, 0, 0, 0, 
					64, 65, 0, 0, 0, 0, 0, 0
			};
			int[] map_night = new int[] {
					0, 0, 2, 0, 0, 0, 0, 0, 
					8, 9, 10, 0, 11, 12, 13, 14, 
					16, 16, 16, 0, 19, 0, 0, 22, 
					24, 16, 26, 0, 0, 0, 0, 0, 
					32, 33, 34, 31, 36, 37, 0, 0, 
					40, 41, 42, 0, 44, 45, 53, 52, 
					48, 49, 50, 51, 0, 0, 43, 46, 
					56, 57, 58, 59, 0, 0, 54, 55
			};
			for (Map map : game.maps) {
				int[] convert;
				if (map.tilesetId == 0) {
					convert = map_grass;
				} else if (map.tilesetId == 1) {
					convert = map_castle;
				} else {
					convert = map_night;
				}
				for (int k = 0; k < map.layers.length; k++) {
					MapLayer layer = map.layers[k];
					for (int i = 0; i < layer.rows; i++) {
						for (int j = 0; j < layer.columns; j++) {
							layer.tiles[i][j] = convert[layer.tiles[i][j]];
						}
					}
				}
			}
			game.tilesets[0] = new Tileset("Grass", "grass.png", 64, 64, 10, 8);
			game.tilesets[1] = new Tileset("Castle", "castle.png", 64, 64, 10, 8);
			game.tilesets[2] = new Tileset("Night", "night.png", 64, 64, 10, 8);
			upgraded(game);
		} 
		if (version < 20) {
			game.tilesets = new Tileset[4];
			game.tilesets[0] = new Tileset("Grass", "grass.png", 64, 64, 10, 8);
			game.tilesets[1] = new Tileset("Castle", "castle.png", 64, 64, 10, 8);
			game.tilesets[2] = new Tileset("Night", "night.png", 64, 64, 10, 8);
			game.tilesets[3] = new Tileset("Snow", "snow.png", 64, 64, 10, 8);
			
			upgraded(game);
		}
	}

	private static void upgraded(PlatformGame game) {
		int from = game._VERSION_;
		game._VERSION_++;
		int to = game._VERSION_;
		DataDebug.write("Upgraded game from v%d to v%d...", from, to);
	}
}

