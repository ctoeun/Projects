package copsvsrobbers;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Subclass that extends Plant. Sets health, cooldown, and attack. The basic cop has
 * a fast cooldown and does little damage. Health is 100 and the cost to
 * purchase is $5.
 * 
 * @author Cobi Toeun
 *
 */
public class BasicCop extends Plant {

	// Creates constants for cop health, cooldown, and attack
	private static final int HEALTH = 100, COOLDOWN = 2, ATTACK = 1;
	private static BufferedImage img;

	// Creates static image
	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/copsvsrobbers/basicCop.png"));
		} catch (IOException e) {
			System.out.println("basic cop image was not found");
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
	public BasicCop(Point2D.Double startingPosition, Point2D.Double initHitbox) {
		super(startingPosition, initHitbox, img, HEALTH, COOLDOWN, ATTACK);
	}

}