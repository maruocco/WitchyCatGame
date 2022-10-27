package Screens;

import javax.swing.JFrame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Engine.GamePanel;
import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;

import Level.*;
import Maps.SummerMap;
import Maps.TestMap;
import Maps.WinterMap;
import Maps.FallMap;
import Maps.InteriorMap;
import Players.Cat;
import Scripts.Sounds;
import Scripts.UI;
import Utils.Direction;
import Utils.Point;

// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen {
	protected ScreenCoordinator screenCoordinator;
	protected Map map;
	protected Map springMap;
	protected Map summerMap;
	protected Map winterMap;
	protected Map fallMap;
	protected Map interiorMap;
	protected Player player;
	protected PlayLevelScreenState playLevelScreenState;
	// protected OptionsState optionsMenuState;
	protected WinScreen winScreen;
	protected UI options;
	protected FlagManager flagManager;
	protected KeyLocker keyLocker = new KeyLocker();
	private Key volUpKey = Key.A;
	private Key volDownKey = Key.D;
	private Key volMuteKey = Key.S;
	protected GamePanel gp;
	Sounds sound = new Sounds();

	public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
		this.screenCoordinator = screenCoordinator;

//       
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new GridLayout(1,3));
//        
//        JButton volUp = new JButton("Volume Up");
//        volUp.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				sound.volumeUp();
//			}
//		});
//        frame.add(volUp);
//        
//        JButton volDown = new JButton("Volume Down");
//        volDown.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				sound.volumeDown();
//			}
//		});
//        frame.add(volDown);
//        
//        JButton mute = new JButton("Mute/Unmute");
//        mute.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				sound.volumeMute();
//			}
//		});
//        frame.add(mute);
//        
//        frame.pack();
//        frame.setVisible(true);
		
		

		playMusic(0);
	}

	public void initialize() {
		// setup state
		flagManager = new FlagManager();
		flagManager.addFlag("rightSpot", false);
		flagManager.addFlag("hasLostBall", false);
		flagManager.addFlag("hasTalkedToWalrus", false);
		flagManager.addFlag("hasTalkedToDinosaur", false);
		flagManager.addFlag("hasFoundBall", false);
		flagManager.addFlag("hasTalkedToBlorbo", false);
		flagManager.addFlag("hasTalkedtoPizza", false);
		flagManager.addFlag("hasGivenSwordItem", false);
		flagManager.addFlag("hasDropped", false);
		flagManager.addFlag("hasTalkedtoBruce", false);
		flagManager.addFlag("hasGoldLeaf", false);
		flagManager.addFlag("hasIceSphere", false);
		flagManager.addFlag("hasFireRose", false);
		flagManager.addFlag("inSpringArea", false);
		flagManager.addFlag("inventoryCheck",true);
		flagManager.addFlag("optionsCheck",true);

		// define/setup map
		springMap = new TestMap();
		summerMap = new SummerMap();
		summerMap.setCameraX(48);
		winterMap = new WinterMap();
		fallMap = new FallMap();
		interiorMap = new InteriorMap();
		this.map = springMap;
		map.reset();
		map.setFlagManager(flagManager);

		// setup player
		this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
		this.player.setMap(map);
		Point playerStartPosition = map.getPlayerStartPosition();
		this.player.setLocation(playerStartPosition.x, playerStartPosition.y);
		this.playLevelScreenState = PlayLevelScreenState.RUNNING;
		this.player.setFacingDirection(Direction.LEFT);

		this.options = new UI(gp);

		// let pieces of map know which button to listen for as the "interact" button
		map.getTextbox().setInteractKey(player.getInteractKey());

		// setup map scripts to have references to the map and player
		for (MapTile mapTile : map.getMapTiles()) {
			if (mapTile.getInteractScript() != null) {
				mapTile.getInteractScript().setMap(map);
				mapTile.getInteractScript().setPlayer(player);
			}
		}
		for (NPC npc : map.getNPCs()) {
			if (npc.getInteractScript() != null) {
				npc.getInteractScript().setMap(map);
				npc.getInteractScript().setPlayer(player);
			}
		}
		for (EnhancedMapTile enhancedMapTile : map.getEnhancedMapTiles()) {
			if (enhancedMapTile.getInteractScript() != null) {
				enhancedMapTile.getInteractScript().setMap(map);
				enhancedMapTile.getInteractScript().setPlayer(player);
			}
		}
		for (Trigger trigger : map.getTriggers()) {
			if (trigger.getTriggerScript() != null) {
				trigger.getTriggerScript().setMap(map);
				trigger.getTriggerScript().setPlayer(player);
			}
		}
		
		
		
		
		winScreen = new WinScreen(this);
	}

	public void update() {
		// based on screen state, perform specific actions
		switch (playLevelScreenState) {
		// if level is "running" update player and map to keep game logic for the
		// platformer level going
		case RUNNING:
			player.update();
			map.update(player);
			break;
		// if level has been completed, bring up level cleared screen
		case LEVEL_COMPLETED:
			winScreen.update();
			break;
		}
		
//		if (Keyboard.isKeyDown(volUpKey) && !keyLocker.isKeyLocked(volUpKey)) {
//			keyLocker.lockKey(volUpKey);
//		} else if (Keyboard.isKeyUp(volUpKey)) {
//			keyLocker.unlockKey(volUpKey);
//		}
//		
//		if (Keyboard.isKeyDown(volDownKey) && !keyLocker.isKeyLocked(volDownKey)) {
//			keyLocker.lockKey(volDownKey);
//		} else if (Keyboard.isKeyUp(volDownKey)) {
//			keyLocker.unlockKey(volDownKey);
//		}
//		
//		if (Keyboard.isKeyDown(volMuteKey) && !keyLocker.isKeyLocked(volMuteKey)) {
//			keyLocker.lockKey(volMuteKey);
//			
////		} else if (Keyboard.isKeyUp(volMuteKey)) {
//			keyLocker.unlockKey(volMuteKey);
//			
//		}
		
		if (Keyboard.isKeyDown(volUpKey) && !keyLocker.isKeyLocked(volUpKey)) {
			keyLocker.lockKey(volUpKey);
			sound.volumeUp();
			System.out.println("works");
		} else if (Keyboard.isKeyUp(volUpKey)) {
			keyLocker.unlockKey(volUpKey);
		}
		
		if (Keyboard.isKeyDown(volDownKey) && !keyLocker.isKeyLocked(volDownKey)) {
			keyLocker.lockKey(volDownKey);
			sound.volumeDown();
		} else if (Keyboard.isKeyUp(volDownKey)) {
			keyLocker.unlockKey(volDownKey);
		}
		
		if (Keyboard.isKeyDown(volMuteKey) && !keyLocker.isKeyLocked(volMuteKey)) {
			keyLocker.lockKey(volMuteKey);
			sound.volumeMute();
		} else if (Keyboard.isKeyUp(volMuteKey)) {
			keyLocker.unlockKey(volMuteKey);
		}

		// if flag is set at any point during gameplay, game is "won"
		if (map.getFlagManager().isFlagSet("hasFoundBall")) {
			playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
		}
		
		

		//////////// Temporary thing, get player location by pressing B
		//////////// ///////////
		if (Keyboard.isKeyDown(Key.B) && !keyLocker.isKeyLocked(Key.B)) {
			System.out.println("player coords: " + player.getLocation());
			System.out.println("cameraY: " + this.map.getCamera().getY());
			System.out.println("cameraX: " + this.map.getCamera().getX());
		}
		////////////////////////////////////////////////////////////////////////////////////////

		// map switching, saves state of current map, changes map, places player at
		// certain location

		// leaving spring
		if (map.getMapFileName().equals("test_map.txt")) {

			// Walrus house interior
			if (player.getLocation().x > 160 && player.getLocation().x < 196 && player.getLocation().y == 1260) {
				springMap = this.map;
				interiorMap.setCoinCounter(this.map.getCoinCounter());
				this.map = interiorMap;
				this.player.setLocation(374, 408);
				map.setFlagManager(flagManager);
				this.player.setMap(map);
				
				if(map.getFlagManager().isFlagSet("inventoryCheck")) map.getInventory().setIsActive(false);
				else map.getInventory().setIsActive(true);
				
				if(map.getFlagManager().isFlagSet("optionsCheck")) map.getOptions().setIsActive(false);
				else map.getOptions().setIsActive(true);
			}

			// summer map
			if (player.getFacingDirection().getVelocity() > 0) {
				if (player.getLocation().x > this.map.getEndBoundX() - 426) {
					springMap = this.map;
					summerMap.setCoinCounter(this.map.getCoinCounter());
					summerMap.setCameraY(this.map.getCamera().getY());
					this.player.setLocation(438,
							(player.getLocation().y / springMap.getHeight()) * summerMap.getHeight());
					this.map = summerMap;
					map.setFlagManager(flagManager);
					this.player.setMap(map);
					
					if(map.getFlagManager().isFlagSet("inventoryCheck")) map.getInventory().setIsActive(false);
					else map.getInventory().setIsActive(true);
					
					if(map.getFlagManager().isFlagSet("optionsCheck")) map.getOptions().setIsActive(false);
					else map.getOptions().setIsActive(true);
				}
			}

			// winter map

			// fall map

			 if (Keyboard.isKeyDown(volUpKey) && !keyLocker.isKeyLocked(volUpKey)) {
					keyLocker.lockKey(volUpKey);
					sound.volumeUp();
				} else if (Keyboard.isKeyUp(volUpKey)) {
					keyLocker.unlockKey(volUpKey);
				}
				
				if (Keyboard.isKeyDown(volDownKey) && !keyLocker.isKeyLocked(volDownKey)) {
					keyLocker.lockKey(volDownKey);
					sound.volumeDown();
				} else if (Keyboard.isKeyUp(volDownKey)) {
					keyLocker.unlockKey(volDownKey);
				}
				
				if (Keyboard.isKeyDown(volMuteKey) && !keyLocker.isKeyLocked(volMuteKey)) {
					keyLocker.lockKey(volMuteKey);
					sound.volumeMute();
				} else if (Keyboard.isKeyUp(volMuteKey)) {
					keyLocker.unlockKey(volMuteKey);
				}
		}

		// leaving summer
		if (map.getMapFileName().equals("summer_map.txt")) {

			// spring map
			if (player.getFacingDirection().getVelocity() < 0) {
				if (player.getLocation().x < 438) {
					summerMap = this.map;
					springMap.setCoinCounter(this.map.getCoinCounter());
					// System.out.println("springPre: " + springMap.getCamera().getY());
					springMap.setCameraY(this.map.getCamera().getY());
					// System.out.println("springPost: " + springMap.getCamera().getY());
					this.player.setLocation(springMap.getEndBoundX() - 426,
							(player.getLocation().y / summerMap.getHeight()) * springMap.getHeight());
					this.map = springMap;
					map.setFlagManager(flagManager);
					this.player.setMap(map);
					
					if(map.getFlagManager().isFlagSet("inventoryCheck")) map.getInventory().setIsActive(false);
					else map.getInventory().setIsActive(true);
					
					if(map.getFlagManager().isFlagSet("optionsCheck")) map.getOptions().setIsActive(false);
					else map.getOptions().setIsActive(true);
				}
			}
		}

		// leaving interior
		if (map.getMapFileName().equals("interior_map.txt")) {
			if (player.getLocation().x > 364 && player.getLocation().x < 380 && player.getLocation().y > 423) {
				interiorMap = this.map;
				springMap.setCoinCounter(this.map.getCoinCounter());
				this.map = springMap;
				this.player.setLocation(173, 1264);
				map.setFlagManager(flagManager);
				this.player.setMap(map);
				
				if(map.getFlagManager().isFlagSet("inventoryCheck")) map.getInventory().setIsActive(false);
				else map.getInventory().setIsActive(true);
				
				if(map.getFlagManager().isFlagSet("optionsCheck")) map.getOptions().setIsActive(false);
				else map.getOptions().setIsActive(true);
			}
		}
		
		
		
		
	}

	public void draw(GraphicsHandler graphicsHandler) {
// based on screen state, draw appropriate graphics
		switch (playLevelScreenState) {
		case RUNNING:
			map.draw(player, graphicsHandler);
			break;
		case LEVEL_COMPLETED:
			winScreen.draw(graphicsHandler);
			break;
		}

	}

	public PlayLevelScreenState getPlayLevelScreenState() {
		return playLevelScreenState;
	}

	public void resetLevel() {
		initialize();
	}

	public void goBackToMenu() {
		screenCoordinator.setGameState(GameState.MENU);
	}

//This enum represents the different states this screen can be in
	private enum PlayLevelScreenState {
		RUNNING, LEVEL_COMPLETED
	}

	public void playMusic(int i) {
		sound.setFile(i);
		sound.play();
		sound.loop();
	}

	public void stopMusic() {
		sound.stop();
	}

}
