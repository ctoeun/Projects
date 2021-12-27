package copsvsrobbers;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Subclass that extends Plant. Sets health, cooldown, and attack. The fat cop has
 * a slow cooldown and does high damage. Health is 200 and the cost to
 * purchase is $10.
 * 
 * @author Cobi Toeun
 *
 */
public class FatCop extends Plant {

	// Creates constants for cop health, cooldown, and attack
	private static final int HEALTH = 200, COOLDOWN = 50, ATTACK = 25;
	private static BufferedImage img;

	// Creates static image
	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/a9b/fatCop.png"));
		} catch (IOException e) {
			System.out.println("fat cop image was not found");
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
	public FatCop(Point2D.Double startingPosition, Point2D.Double initHitbox) {
		super(startingPosition, initHitbox, img, HEALTH, COOLDOWN, ATTACK);
	}

}