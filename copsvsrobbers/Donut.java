package copsvsrobbers;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Subclass that extends Plant. Sets health, cooldown, and attack. The donut is
 * like a bomb and does full damage among collision with a robber. Health is
 * 1000 but once placed, it will move right so it practically doesn't matter.
 * Cost to purchase is $25. Overrides move() method with speed constant set to
 * 15.
 * 
 * @author Cobi Toeun
 *
 */
public class Donut extends Plant {

	// Creates constants for cop health, cooldown, attack, and speed (which
	// overrides).
	private static final int HEALTH = 1000, COOLDOWN = 0, ATTACK = 200, SPEED = 15;
	private static BufferedImage img;

	// Creates static image
	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/a9b/Donut.png"));
		} catch (IOException e) {
			System.out.println("donut image was not found");
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
	public Donut(Point2D.Double startingPosition, Point2D.Double initHitbox) {
		super(startingPosition, initHitbox, img, HEALTH, COOLDOWN, ATTACK);
	}

	/**
	 * Overrides move method and allows donut to move to the right and attack
	 * robbers.
	 */
	@Override
	public void move() {
		shiftPosition(new Point2D.Double(SPEED, 0));
	}
}