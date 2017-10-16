package ca.uqac.inf957.pigeon;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import ca.uqac.inf957.settings.Settings;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * @author Mewena
 * materializes the pigeons  
 *
 */
public class Pigeon extends Parent 
{
	
	private int X;
	private int Y;
	private int speed;
	
	
	/**
	 * constructor 
	 * @param x position abscissa on the panel
	 * @param y position ordinate on the panel
	 */
	public Pigeon(int x, int y) throws FileNotFoundException 
	{
		super();
		X = x;
	    Y = y;
		speed = (new Random().nextInt(5) + 1)*1000;
		
		InputStream url = getClass().getResourceAsStream("pigeon.png");
		Image pigeonImage = new Image(url);
		ImageView imageView = new ImageView(pigeonImage);  
        //Setting the position of the image 
		initPosition();
      
        //setting the fit height and width of the image view 
        imageView.setFitHeight(Settings.pigeonHeight); 
        imageView.setFitWidth(Settings.pigeonWidth);
   
		this.getChildren().add(imageView);
		
		
	}

	/**
	 * initialize the pigeons position according to x,y
	 */
	public void initPosition()
	{
		this.setTranslateX(this.X);
		this.setTranslateY(this.Y);
	}
	
	/**
	 * @return the current abscissa of the pigeon
	 */
	public double getX() {
		return this.translateXProperty().intValue();
	}

    //
	public void setX(int x) {
		X = x;
	}

	/**
	 * @return the current ordinate of the pigeon
	 */
	public double getY() {
		return this.translateYProperty().intValue();
	}

    //
	public void setY(int y) {
		Y = y;
	}

	/**
	 * @return the speed of pigeon
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param the pedestrian speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * materialize the move of a pigeon from initialize position to food
	 * @param foodX food abscissa
	 * @param foodY food ordinate
	 */
	public Timeline moveTo(int foodX,int foodY)
	{         
        Duration duration = Duration.millis(speed);
        KeyValue keyValueX = new KeyValue(this.translateXProperty(), foodX);
        KeyValue keyValueY = new KeyValue(this.translateYProperty(), foodY);
        KeyFrame keyFrameX = new KeyFrame(duration , keyValueX);
        KeyFrame keyFrameY = new KeyFrame(duration ,keyValueY); 
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(keyFrameX, keyFrameY);
        return timeline;
        //timeline.play();
		
	}
	
	/**
	 * materialize the move of a pigeon to his init position
	 * 
	 */
	public Timeline backToOrigine() //la changer pour retourner à un point random
	{         
        Duration duration = Duration.millis(speed); 
        KeyValue keyValueX = new KeyValue(this.translateXProperty(), this.X);
        KeyValue keyValueY = new KeyValue(this.translateYProperty(), this.Y);
        KeyFrame keyFrameX = new KeyFrame(duration , keyValueX);
        KeyFrame keyFrameY = new KeyFrame(duration ,keyValueY); 
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(keyFrameX, keyFrameY);
        return timeline;
        //timeline.play();	
	}
	
	/**
	 * subclass instantiated in pigeon thread 
	 */
	public class Move implements Runnable {
		
		Timeline timeline;
		
		public Move(Timeline timeline) {
			this.timeline = timeline;		
		}
		
		@Override
		public void run() {
	        timeline.play();  
		}

	}

}
