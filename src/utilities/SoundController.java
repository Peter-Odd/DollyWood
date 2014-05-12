package utilities;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import graphics.utilities.Camera;
/**
 * Sound controller.
 * This class will make sure that sounds can be played without any hassle.
 * @author OSM Group 5 - DollyWood project
 * @version 1.0
 */
public class SoundController {
	public static Camera camera;
	public static float size;
	
	/**
	 * Plays a sound with volume relative to the camera position.
	 * Note that this is not in stereo. Camera rotation does not affect levels
	 * @param sound A string with the name of the sound to be played
	 * @param x position in the world where the sound should come from
	 * @param y position in the world where the sound should come from
	 */
	public synchronized static void playSound(final String sound, final int levelBoost, final int x, final int y){
		if(camera != null){
			new Thread(new Runnable() {
				public void run(){
					try {
						int[] cameraPos = camera.getArrayPosition(size);
						int dist = Math.abs(x-cameraPos[0]) + Math.abs(y-cameraPos[1]);

						double cameraRoll = Math.toRadians(camera.getRotation().z%360);
						Math.sin(0);
						System.out.println(cameraRoll); //TODO fix this to print a value between -1 and 1 in relation to x,y cameraPos and cameraRoll. Will be used to create a 3D sound system
						
						AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("res/" + sound));
						Clip clip = AudioSystem.getClip();
						clip.open(inputStream);
						
						FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(-80+Math.abs(((Globals.height+Globals.width)-dist))+levelBoost);
						clip.loop(0);
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedAudioFileException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
