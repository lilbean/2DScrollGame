import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

/**
 * Loki the Rabbit
 * How to play: Use left and right arrow keys to move. Jump with space bar.
 * Objective: Navigate Loki to the end of the level within 30 seconds-- this is easy mode. 
 * Making it to the end of the level earns a player 100 points!
 * Pick up carrots along the way to earn extra points
 * 
 * @author Courtney Connery
 * @version march 2017, adapted from class demo "Scroll"
 */
public class Scroll extends Application {
	//declaring variables
	final String appName = "Loki the Rabbit";
	final int FPS = 25; // frames per second
	final static int VWIDTH = 800;
	final static int VHEIGHT = 600;
	final static int BWIDTH = 1000;
	final static int EDGE = 30;
	public static final int SCROLL = 50; // Set edge limit for scrolling
	public static int vleft = 0; // Pixel coord of left edge of viewable
	public static int points = 0;
	int goalFlag = 0; 
	int timeFlag = 0;
	int introScreen= 1; //flag for if it is displaying the intro screen
	//public static int level = 1;  
	public static int currlevel = 2; 
	int playOnce = 0; 
	
	Font font; 
	Goal goal;
	Items carrots[] = new Items[14];
	ScoreBoard time;
	HeroSprite hero;
	Grid grid;
	Image loki, carrot, carrotGoal, background;
	AudioClip itemCollect; 
	AudioClip youWin;
	/**
	 * Set up initial data structures/values
	 */
	void initialize() {
		background = new Image("background.jpg");
		loki = new Image("loki1.gif");
		carrot = new Image("carrot.gif");
		carrotGoal = new Image("carrotGoal.gif");
		grid = new Grid();grid = new Grid();grid = new Grid();
		hero = new HeroSprite(grid, 100, 499, loki);
		goal = new Goal(1400,500, carrotGoal);
		itemCollect = new AudioClip(ClassLoader.getSystemResource("itemCollect.wav").toString());
		youWin = new AudioClip(ClassLoader.getSystemResource("youWin.wav").toString());
		setLevel1();
	}
	void reset(){	//reset for new game 
		hero = new HeroSprite(grid, 100, 499, loki);
		points = 0; 
		goalFlag = 0; 
		playOnce = 0;
		setLevel1();
	}
	public void setLevel1(){	//select level
		// Put in a ground level
		for (int i = 0; i < Grid.MWIDTH; i++)
			grid.setBlock(i, Grid.MHEIGHT - 1);

		// Now place specific blocks (depends on current map size)

		grid.setBlock(5, 10);grid.setBlock(6, 10);grid.setBlock(7,10);
		grid.setBlock(5, 6);grid.setBlock(6, 6);grid.setBlock(7, 6);
		grid.setBlock(10, 13);grid.setBlock(11, 13);grid.setBlock(12, 13);
		grid.setBlock(13, 9);grid.setBlock(14, 9);grid.setBlock(15,9);
		grid.setBlock(17, 13);grid.setBlock(18, 13);grid.setBlock(19, 13);
		grid.setBlock(19, 8);grid.setBlock(20, 8);grid.setBlock(21, 8);
		grid.setBlock(23, 5);grid.setBlock(24, 5);grid.setBlock(25, 5);
		grid.setBlock(28, 8);grid.setBlock(29, 8);grid.setBlock(30, 8);
		grid.setBlock(27, 13);grid.setBlock(28, 13);grid.setBlock(29, 13);
		//place some carrots
		carrots[0] = new Items(150, -10, carrot);
		carrots[1] = new Items(150, 140, carrot);
		carrots[2] = new Items(475, 100, carrot);
		int add =40;	//just using this for my for loops
		for(int i = 3; i <6; i++){
			carrots[i] = new Items(400+add, 290, carrot);
			add+=40;
		}
		carrots[6] = new Items(710, 75, carrot);
		carrots[7] = new Items(870, -50, carrot);
		carrots[8] = new Items(1075, 80, carrot);
		add=20; 
		for (int i = 9; i < 14; i ++){
			carrots[i] = new Items(700+add, 320, carrot);
			add+=60;
		}
	}
	void setHandlers(Scene scene) {
		scene.setOnKeyPressed(e -> {
			KeyCode key = e.getCode();
			if(introScreen == 1){
				reset();
				switch(key){
				case E:
					currlevel = 1;
					time = new ScoreBoard(30.0);
					introScreen = 0;
					
					break;
				case M: 
					currlevel = 2;
					time = new ScoreBoard(25.0);
					introScreen = 0;
					break;
				case H:
					currlevel = 3;
					time = new ScoreBoard(20.0);
					introScreen =0;
					break;
				default:
					currlevel = 1;
					break;
				}
			}
			else if(introScreen == 0){
			switch (key) {
			case LEFT:
			case A:
				hero.dir = 1;
				break;
			case RIGHT:
			case D:
				hero.dir = 2;
				break;
			// add a Jump key here
			case SPACE:
			case W:
				hero.jmp = 1;
				break;
			case B:
				introScreen = 1;
				break;
			default:
				break;
			}
		}
		});
		scene.setOnKeyReleased(e -> {
			KeyCode key = e.getCode();
			if ((key == KeyCode.A) || (key == KeyCode.LEFT) || (key == KeyCode.D) || (key == KeyCode.RIGHT))
				hero.dir = 0;
		});
	}

	/**
	 * Update variables for one time step
	 */
	public void update() {
		hero.update();
		checkScrolling();
		if(goalFlag !=1){
		checkCollision();
		checkGoal();
		}
	}

	public void checkCollision() { // check if the bunny hit the item
		// if collision, gain point, "erase" item
		for (int i = 0; i < carrots.length; i++) {
			if(hero.locy() + hero.height() >= carrots[i].locy()
			&& hero.locy() <= carrots[i].locy() + carrots[i].height()
			&& hero.locx() <= carrots[i].locx()+ carrots[i].width()
			&& hero.locx() + hero.width() >= carrots[i].locx()){
				if(carrots[i].color ==0){
					points++;
					itemCollect.play();
					carrots[i].eraseItem();
				}
			}
		}
	}

			
		// else do nothing
	
	void checkGoal(){
		//check if hero has reached goal
			double dx = hero.locx() - goal.locx();
			double dy =  hero.locy() - goal.locy();
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			double minDistance = 80;
			if (distance < minDistance) {
				goalFlag = 1;
				if (playOnce != 1){
					points +=100; //extra points for making it to finish!
					youWin.play();
					playOnce = 1;
				}
			}
	 }
	void checkScrolling() {
		// Test if hero is at edge of view window and scroll appropriately
		if (hero.locx() < (vleft + SCROLL)) {
			vleft = hero.locx() - SCROLL;
			if (vleft < 0)
				vleft = 0;
		}
		if ((hero.locx() + hero.width()) > (vleft + VWIDTH - SCROLL)) {
			vleft = hero.locx() + hero.width() - VWIDTH + SCROLL;
			if (vleft > (grid.width() - VWIDTH))
				vleft = grid.width() - VWIDTH;
		}
	}

	/**
	 * Draw the game world
	 */
	void render(GraphicsContext gc) {
		//render for the intro screen
		if(introScreen == 1){
			gc.setFill(Color.LIGHTBLUE);
			gc.fillRect(0.0, 0.0, VWIDTH, VHEIGHT);
			int cut = (vleft/2) % BWIDTH;
			gc.drawImage(background, -cut, 0);
			gc.drawImage(background, BWIDTH-cut, 0);
			
		}
		//after the intro screen 
		else{
			gc.setFill(Color.LIGHTBLUE);
			gc.fillRect(0.0, 0.0, VWIDTH, VHEIGHT);
			time.render(gc);
			
			for (int i = 0; i < carrots.length; i++) {
				carrots[i].render(gc);
			}
			grid.render(gc); 
			goal.render(gc);
			hero.render(gc);
			

			if(goalFlag == 1 || time.checkTime() == true){
				if(goalFlag == 1){
					gc.setFill(Color.BLACK);
					gc.fillText("You Win!", Scroll.VWIDTH-15*Scroll.EDGE, Scroll.EDGE+60);
					gc.fillText("Press 'B' twice to return to home screen", Scroll.VWIDTH-20*Scroll.EDGE, Scroll.EDGE+100);
				}
				else{ 
				goalFlag = 1;
				gc.setFill(Color.BLACK);
				gc.fillText("GAME OVER!", Scroll.VWIDTH-15*Scroll.EDGE, Scroll.EDGE+60);
				gc.fillText("Press 'B' twice to return to home screen", Scroll.VWIDTH-20*Scroll.EDGE, Scroll.EDGE+100);
				}
			}
			//for debugging coordinates
			//gc.fillText("Hero x:" + hero.locx + " y:" + hero.locy , Scroll.VWIDTH-15*Scroll.EDGE, Scroll.EDGE );
			//gc.fillText("C x:" + carrots[1].locx() + " y:" + carrots[1].locy() , Scroll.VWIDTH-21*Scroll.EDGE, Scroll.EDGE );
		}
}
	
	/*
	 * Begin boiler-plate code... [Animation and events with initialization]
	 */
	public static void main(String[] args) {
		launch(args);
		
	}

	@Override
	public void start(Stage theStage) {
		theStage.setTitle(appName);

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(VWIDTH, VHEIGHT);
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Initial setup
		initialize();
		setHandlers(theScene);
		
		
		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS), e -> {
			// update position
			update();
			// draw frame
			render(gc);
		});
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		theStage.show();
	}
	/*
	 * ... End boiler-plate code
	 */
}