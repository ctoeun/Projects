package copsvsrobbers;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Subclass that extends Zombie. The "ghost" robber has no cooldown and does no
 * damage. Health is 26 and the purpose is for the sprite to detect no collision
 * and move past the cops. Their speed is set high and makes them a high threat
 * to basic and fat cops. To kill them you need to place a basic cop and fat cop
 * next to each other. Overrides setCollisionStatus() method.
 * 
 * @author Cobi Toeun
 *
 */
public class GhostRobber extends Zombie {

	// Creates constants for cop health, cooldown, speed, and attack
	private static final int HEALTH = 26, COOLDOWN = 0, SPEED = -5, ATTACK = 0;
	private static BufferedImage img;

	// Creates static image
	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/copsvsrobbers/ghostRobber.png"));
		} catch (IOException e) {
			System.out.println("ghost robber image was not found");
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
	public GhostRobber(Double startingPosition, Double initHitbox) {
		super(startingPosition, initHitbox, img, HEALTH, COOLDOWN, SPEED, ATTACK);
	}

	/**
	 * Overrides collision status so ghost robber just passes every cop and loses
	 * collision ability.
	 */
	@Override
	public void setCollisionStatus(Actor other) {
	}
}
