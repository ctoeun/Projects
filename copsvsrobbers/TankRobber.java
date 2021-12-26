package copsvsrobbers;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Subclass that extends Zombie. Sets health, cooldown, speed, and attack. The
 * tank robber has slow cooldown and does high damage at 25. Movement is really
 * slow and health is 200. They are a tank enemy and a high threat.
 * 
 * @author Cobi Toeun
 *
 */
public class TankRobber extends Zombie {

	// Creates constants for cop health, cooldown, speed, and attack
	private static final int HEALTH = 200, COOLDOWN = 150, SPEED = -1, ATTACK = 25;
	private static BufferedImage img;

	// Creates static image
	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/a9b/tankRobber.png"));
		} catch (IOException e) {
			System.out.println("tank robber image was not found");
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
	public TankRobber(Double startingPosition, Double initHitbox) {
		super(startingPosition, initHitbox, img, HEALTH, COOLDOWN, SPEED, ATTACK);
	}

}
