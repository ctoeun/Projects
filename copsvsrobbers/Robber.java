package copsvsrobbers;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Subclass that extends Zombie. Sets health, cooldown, speed, and attack. The
 * robber has slow cooldown and does decent damage. Health is 100 and they are a
 * basic enemy and pose little threat.
 * 
 * @author Cobi Toeun
 *
 */
public class Robber extends Zombie {

	// Creates constants for cop health, cooldown, speed, and attack
	private static final int HEALTH = 100, COOLDOWN = 50, SPEED = -2, ATTACK = 10;
	private static BufferedImage img;

	// Creates static image
	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/copsvsrobbers/robber.png"));
		} catch (IOException e) {
			System.out.println("robber image was not found");
			System.exit(0);
		}
		img = image;
	}

	/**
	 * Constructor sets position and hitbox, while callling superclass method.
	 * 
	 * @param startingPosition
	 * @param initHitbox
	 */
	public Robber(Double startingPosition, Double initHitbox) {
		super(startingPosition, initHitbox, img, HEALTH, COOLDOWN, SPEED, ATTACK);
	}

}
