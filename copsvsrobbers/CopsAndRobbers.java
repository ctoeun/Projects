package copsvsrobbers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.sun.jdi.event.Event;

/**
 * Plants vs Zombies inspired game turned into Cops vs Robbers with a Simpsons
 * theme. Class extends JPanel and implements ActionListener and MouseListener.
 * Provides different methods to allow the game to end and function. Press start
 * button to play game.
 * 
 * @author Cobi Toeun
 *
 */
public class CopsAndRobbers extends JPanel implements ActionListener, MouseListener {

	// Declaration of variables for whole class.
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private ArrayList<Actor> actors; // Plants and zombies all go in here
	private BufferedImage referenceImage; // Maybe these images should be in those classes, but easy to change here.
	private Point2D.Double position; // Variable for actors positions
	private int numRows, numCols, cellSize, copX, copY, fatX, fatY, donutX, donutY, x, y, robberGeneration = 198,
			cashGeneration = 195; // ints for positions, random generation, and grid size.
	private static Random random; // Used to generate random ints
	private static JLabel resource, scoreboard, highscore; // Labels for cash and score
	private static JButton basicCop, fatCop, donut, start, quit; // Buttons for JPanel
	private static JPanel optionsPanel, mainPanel, bottomPanel, cashPanel; // Panels for JFrame
	private static int cashAmount = 25, score, cop, checkStatus, highScore; // int variables for cash, score, cop, and status

	/**
	 * Constructor sets JFrame grid size, timer, and reference image.
	 */
	public CopsAndRobbers() {
		super();

		// Define some quantities of the scene
		numRows = 5;
		numCols = 7;
		cellSize = 110;
		setPreferredSize(new Dimension(50 + numCols * cellSize, 50 + numRows * cellSize));
		random = new Random();

		// Store all the plants and zombies in here.
		actors = new ArrayList<>();

		// Load reference image
		try {
			referenceImage = ImageIO.read(new File("src/copsvsrobbers/basicCop.png"));
		} catch (IOException e) {
			System.out.println("A test file was not found");
			System.exit(0);
		}

		// The timer updates the game each time it goes.
		// Get the javax.swing Timer, not from util.
		timer = new Timer(30, this);
		timer.start();

		addMouseListener(this);

	}

	/***
	 * Implement the paint method to draw the plants
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Actor actor : actors) {
			actor.draw(g, 0);
			actor.drawHealthBar(g);
		}
	}

	/**
	 * Changes JButton colors when cop is placed on JPanel
	 */
	public void buttonColor() {
		if (cop == 0) {
			basicCop.setBackground(null);
			fatCop.setBackground(null);
			donut.setBackground(null);
		}
	}

	/**
	 * Restarts the game if a robber passes -80.0 on x axis on the JPanel. All
	 * scores reset.
	 */
	public void restart() {
		if (position.getX() <= -80.0) {
			JOptionPane.showMessageDialog(null, "GAME OVER");
			JOptionPane.showMessageDialog(null, "FINAL SCORE: " + score);
			if (score >= highScore) { // if current score is greater than previous high score
				highScore = score;
				highscore.setText("HIGH SCORE: " + highScore);
			}
			checkStatus = 2;
			cashAmount = 25;
			score = 0;
			robberGeneration = 198;
			cashGeneration = 195;
			cop = 0;
			buttonColor();
			scoreboard.setText("SCORE: " + score);
			resource.setText("CASH: $0");
			start.setBackground(null);
		}
	}

	/**
	 * If the score reaches a certain number, the generation of resources and
	 * robbers increase at a faster rate.
	 */
	public void difficulty() {
		if (score == 2500) {
			robberGeneration -= 2;
			cashGeneration -= 5;
			cashAmount += 50;
			resource.setText("CASH: $" + cashAmount);
			score = 2600;
			JOptionPane.showMessageDialog(null, "WAVE 2 + $50");
		}
		if (score == 5000) {
			robberGeneration -= 4;
			cashGeneration -= 10;
			cashAmount += 100;
			resource.setText("CASH: $" + cashAmount);
			score = 5100;
			JOptionPane.showMessageDialog(null, "WAVE 3 + $100");
		}
		if (score == 7500) {
			robberGeneration -= 6;
			cashGeneration -= 20;
			cashAmount += 200;
			resource.setText("CASH: $" + cashAmount);
			score = 7600;
			JOptionPane.showMessageDialog(null, "WAVE 4 + $200");
		}
		if (score == 10000) {
			robberGeneration -= 50;
			cashGeneration -= 30;
			cashAmount += 1000;
			resource.setText("CASH: $" + cashAmount);
			score = 10100;
			JOptionPane.showMessageDialog(null, "GOD TIER MODE + $1000");
		}
		if (score == 15000) {
			robberGeneration -= 180;
			cashGeneration -= 100;
			cashAmount += 5000;
			resource.setText("CASH: $" + cashAmount);
			score = 15100;
			JOptionPane.showMessageDialog(null, "YOU DEAD BOI + $5000");
		}
	}

	/**
	 * Randomly generates resource and score at a reasonable rate.
	 */
	public void generation() {
		if (random.nextInt(200) > cashGeneration) {
			cashAmount += 1;
			resource.setText("CASH: $" + cashAmount);
		}
		if (random.nextInt(200) > 197) {
			score += 100;
			scoreboard.setText("SCORE: " + score);
		}
	}

	/**
	 * 
	 * This is triggered by the timer. It is the game loop of this test.
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (checkStatus == 1) { // Check if start JButton is pressed

			if (random.nextInt(200) > robberGeneration) {

				int col = random.nextInt(7);
				int row = random.nextInt(6);
				int placement = random.nextInt(11);

				int x = col * 100;
				int y = row * 100;

				if (y != 0) { // Don't want to place robber on top row.
					Robber robber = new Robber(new Point2D.Double(800, y),
							new Point2D.Double(referenceImage.getWidth(), referenceImage.getHeight()));
					TankRobber tank = new TankRobber(new Point2D.Double(800, y),
							new Point2D.Double(referenceImage.getWidth(), referenceImage.getHeight()));
					GhostRobber ghost = new GhostRobber(new Point2D.Double(800, y),
							new Point2D.Double(referenceImage.getWidth(), referenceImage.getHeight()));

					if (placement <= 7) {
						actors.add(robber);
					}
					if (placement == 8 || placement == 9) {
						actors.add(tank);
					}
					if (placement == 10) {
						actors.add(ghost);
					}
				}

			}

			// Calls methods above and checks to see if game is over
			generation();
			difficulty();

			// This method is getting a little long, but it is mostly loop code
			// Increment their cooldowns and reset collision status
			for (Actor actor : actors) {
				actor.update();
			}

			// Try to attack
			for (Actor actor : actors) {
				for (Actor other : actors) {
					actor.attack(other);
				}
			}

			// Remove plants and zombies with low health
			ArrayList<Actor> nextTurnActors = new ArrayList<>();
			for (Actor actor : actors) {
				if (actor.isAlive())
					nextTurnActors.add(actor);
				else
					actor.removeAction(actors); // any special effect or whatever on removal
			}
			actors = nextTurnActors;

			// Check for collisions between zombies and plants and set collision status
			for (Actor actor : actors) {
				for (Actor other : actors) {
					actor.setCollisionStatus(other);
				}
			}

			// Move the actors.
			for (Actor actor : actors) {
				actor.move(); // for Zombie, only moves if not colliding.
				position = actor.getPosition(); // Get robber x position.
				restart();
			}

			// Check if game is over to remove all actors
			if (checkStatus == 2) {
				actors.removeAll(actors);
			}

			// Redraw the new scene
			repaint();
		}

	}

	/**
	 * Make the game and run it
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame app = new JFrame("Cops vs Robbers");
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				CopsAndRobbers panel = new CopsAndRobbers();

				// Storing buttons, labels, and panels.
				basicCop = new JButton("$5");
				fatCop = new JButton("$10");
				donut = new JButton("$25");
				start = new JButton("START");
				quit = new JButton("QUIT");
				optionsPanel = new JPanel();
				mainPanel = new JPanel();
				bottomPanel = new JPanel();
				cashPanel = new JPanel();
				resource = new JLabel("CASH: $0");
				resource.setFont(new Font("Arial", Font.BOLD, 20));
				scoreboard = new JLabel("SCORE: 0");
				scoreboard.setFont(new Font("Arial", Font.BOLD, 20));
				highscore = new JLabel("HIGH SCORE: " + score);
				highscore.setFont(new Font("Arial", Font.BOLD, 20));
				mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

				// Load and set images for JButtons.
				try {
					Image basicImage = ImageIO.read(new File("src/copsvsrobbers/basicCop.png"));
					Image fatImage = ImageIO.read(new File("src/copsvsrobbers/fatCop.png"));
					Image donutImage = ImageIO.read(new File("src/copsvsrobbers/Donut.png"));
					basicCop.setIcon(new ImageIcon(basicImage));
					fatCop.setIcon(new ImageIcon(fatImage));
					donut.setIcon(new ImageIcon(donutImage));

				} catch (IOException e) {
					System.out.println("An icon image was not found");
					System.exit(0);
				}

				// Adding JButtons and JLabels to JPanels
				optionsPanel.add(basicCop);
				optionsPanel.add(fatCop);
				optionsPanel.add(donut);

				bottomPanel.add(start);
				bottomPanel.add(quit);

				cashPanel.add(resource);
				cashPanel.add(new JLabel("   "));
				cashPanel.add(highscore);

				panel.add(scoreboard);

				// Adding all panels to mainPanel
				mainPanel.add(panel);
				mainPanel.add(cashPanel);
				mainPanel.add(optionsPanel);
				mainPanel.add(bottomPanel);

				app.setContentPane(mainPanel);
				app.pack();
				app.setVisible(true);
				app.setLocationRelativeTo(null);

				// Listens for JButtons. Does an action once pressed.
				// BasicCop JButton listener
				basicCop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (checkStatus == 1) {
							basicCop.setBackground(Color.yellow);
							fatCop.setBackground(null);
							donut.setBackground(null);
							if (cashAmount < 5) {
								JOptionPane.showMessageDialog(null, "NOT ENOUGH CASH!");
								basicCop.setBackground(null);
							} else
								cop = 1;
						}
					}
				});
				// FatCop JButton listener
				fatCop.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (checkStatus == 1) {
							fatCop.setBackground(Color.yellow);
							basicCop.setBackground(null);
							donut.setBackground(null);
							if (cashAmount < 10) {
								JOptionPane.showMessageDialog(null, "NOT ENOUGH CASH!");
								fatCop.setBackground(null);
							} else
								cop = 2;
						}
					}
				});
				// Donut JButton listener
				donut.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (checkStatus == 1) {
							donut.setBackground(Color.yellow);
							basicCop.setBackground(null);
							fatCop.setBackground(null);
							if (cashAmount < 25) {
								JOptionPane.showMessageDialog(null, "NOT ENOUGH CASH!");
								donut.setBackground(null);
							} else
								cop = 3;
						}
					}
				});
				// Pressed start JButton to start the game.
				// Once start JButton is pressed, will store status value and share with class.
				start.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						checkStatus = 1;
						start.setBackground(Color.yellow);
						resource.setText("CASH: $" + cashAmount);
						if (score <= 0) {
							JOptionPane.showMessageDialog(null, "WAVE 1 + $25");
						}
					}
				});
				// Quits JFrame when quit JButton is pressed
				quit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
			}
		});
	}

	/**
	 * Listener for mouse clicks. Places a cop once button is pressed and stored
	 * value is called.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

		// Checks to see if basicCop JButton was pressed and game has started.
		if (cop == 1 && checkStatus == 1) {
			x = e.getX();
			y = e.getY();

			// Gets column and row of mouse click
			if (x >= 0 && x <= 100)
				x = 0;
			if (x > 100 && x <= 200)
				x = 1;
			if (x > 200 && x <= 300)
				x = 2;
			if (x > 300 && x <= 400)
				x = 3;
			if (x > 400 && x <= 500)
				x = 4;
			if (x > 500 && x <= 600)
				x = 5;
			if (x > 600 && x <= 700)
				x = 6;
			if (x > 700 && x <= 800)
				x = 7;

			if (y >= 0 && y <= 100)
				y = 0;
			if (y > 100 && y <= 200)
				y = 1;
			if (y > 200 && y <= 300)
				y = 2;
			if (y > 300 && y <= 400)
				y = 3;
			if (y > 400 && y <= 500)
				y = 4;
			if (y > 500 && y <= 600)
				y = 5;
			if (y > 600 && y <= 700)
				y = 6;

			// Store x and y location values.
			copX = x * 100;
			copY = y * 100;

			// Checks to see if cop is already placed on grid and produces an error.
			for (Actor actor : actors) {
				if (actor.isCollidingPoint(new Point2D.Double(copX, copY))) {
					JOptionPane.showMessageDialog(null, "CAN'T PLACE COP HERE");
					cop = 0;
					buttonColor();
				}
			}
			// Places basicCop on grid if there are no errors.
			if (cop == 1 && y != 0) {
				BasicCop basicCop = new BasicCop(new Point2D.Double(copX, copY),
						new Point2D.Double(referenceImage.getWidth(), referenceImage.getHeight()));
				actors.add(basicCop);

				cop = 0;
				buttonColor();
				cashAmount -= 5;
				resource.setText("CASH: $" + cashAmount);
			}
		}

		// Check to see if fatCop JButton was pressed and game has started.
		if (cop == 2 && checkStatus == 1) {
			x = e.getX();
			y = e.getY();

			// Gets column and row of mouse click
			if (x >= 0 && x <= 100)
				x = 0;
			if (x > 100 && x <= 200)
				x = 1;
			if (x > 200 && x <= 300)
				x = 2;
			if (x > 300 && x <= 400)
				x = 3;
			if (x > 400 && x <= 500)
				x = 4;
			if (x > 500 && x <= 600)
				x = 5;
			if (x > 600 && x <= 700)
				x = 6;
			if (x > 700 && x <= 800)
				x = 7;

			if (y >= 0 && y <= 100)
				y = 0;
			if (y > 100 && y <= 200)
				y = 1;
			if (y > 200 && y <= 300)
				y = 2;
			if (y > 300 && y <= 400)
				y = 3;
			if (y > 400 && y <= 500)
				y = 4;
			if (y > 500 && y <= 600)
				y = 5;
			if (y > 600 && y <= 700)
				y = 6;

			// Store x and y location values
			fatX = x * 100;
			fatY = y * 100;

			// Checks to see if cop is already placed on grid and produces an error.
			for (Actor actor : actors) {
				if (actor.isCollidingPoint(new Point2D.Double(fatX, fatY))) {
					JOptionPane.showMessageDialog(null, "CAN'T PLACE FAT COP HERE");
					cop = 0;
					buttonColor();
				}
			}

			// Places basicCop on grid if there are no errors.
			if (cop == 2 && y != 0) {
				FatCop fatCop = new FatCop(new Point2D.Double(fatX, fatY),
						new Point2D.Double(referenceImage.getWidth(), referenceImage.getHeight()));
				actors.add(fatCop);

				cop = 0;
				buttonColor();
				cashAmount -= 10;
				resource.setText("CASH: $" + cashAmount);
			}
		}

		// Check to see if donut JButton was pressed and game has started.
		if (cop == 3 && checkStatus == 1) {
			x = e.getX();
			y = e.getY();

			// Gets column and row of mouse click
			if (x >= 0 && x <= 100)
				x = 0;
			if (x > 100 && x <= 200)
				x = 1;
			if (x > 200 && x <= 300)
				x = 2;
			if (x > 300 && x <= 400)
				x = 3;
			if (x > 400 && x <= 500)
				x = 4;
			if (x > 500 && x <= 600)
				x = 5;
			if (x > 600 && x <= 700)
				x = 6;
			if (x > 700 && x <= 800)
				x = 7;

			if (y >= 0 && y <= 100)
				y = 0;
			if (y > 100 && y <= 200)
				y = 1;
			if (y > 200 && y <= 300)
				y = 2;
			if (y > 300 && y <= 400)
				y = 3;
			if (y > 400 && y <= 500)
				y = 4;
			if (y > 500 && y <= 600)
				y = 5;
			if (y > 600 && y <= 700)
				y = 6;

			// Store x and y location values.
			donutX = x * 100;
			donutY = y * 100;

			// Checks to see if cop is already placed on grid and produces an error.
			for (Actor actor : actors) {
				if (actor.isCollidingPoint(new Point2D.Double(donutX, donutY))) {
					JOptionPane.showMessageDialog(null, "CAN'T PLACE DONUT HERE");
					cop = 0;
					buttonColor();
				}
			}

			// Places basicCop on grid if there are no errors.
			if (cop == 3 && y != 0) {
				Donut donut = new Donut(new Point2D.Double(donutX, donutY),
						new Point2D.Double(referenceImage.getWidth(), referenceImage.getHeight()));
				actors.add(donut);

				cop = 0;
				buttonColor();
				cashAmount -= 25;
				resource.setText("CASH: $" + cashAmount);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}