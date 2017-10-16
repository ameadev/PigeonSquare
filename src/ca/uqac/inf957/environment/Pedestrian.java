package ca.uqac.inf957.environment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * @author Mewena
 * materializes the pedestrian who frightens the pigeons 
 *
 */
public class Pedestrian extends Parent 
{

	private int X;
	private int Y;
	private int speed;
	private double createTime;
	private double expiration;
	
	/**
	 * constructor 
	 * @param x position abscissa on the panel
	 * @param y position ordinate on the panel
	 */
	public Pedestrian(int x, int y) throws FileNotFoundException 
	{
		super();
		X = x;
	    Y = y;
		speed = (new Random().nextInt(5) + 1)*1000;
		this.createTime = System.currentTimeMillis();
		this.expiration = (new Random().nextInt(3) + 4)*1000;
		
		
		InputStream url = getClass().getResourceAsStream("Big.png");
		Image pedestrianImage = new Image(url);
		ImageView imageView = new ImageView(pedestrianImage); 
	      
        //Setting the position of the image 
		initPosition();
      
        //setting the fit height and width of the image view 
        imageView.setFitHeight(60); 
        imageView.setFitWidth(40);
   
		this.getChildren().add(imageView);
		
		
	}
	
	/**
	 * initialize the pedestrian position according to x,y
	 */
	public void initPosition()
	{
		this.setTranslateX(this.X);
		this.setTranslateY(this.Y);
	}
	
	public double getX() {
		return this.translateXProperty().intValue();
	}

    //
	public double getY() {
		return this.translateYProperty().intValue();
	}
	
	/**
	 * @return the speed of pedestrian
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
	 * materialize the move of a pedestrian
	 * @param pedestrianX destination abscissa
	 * @param pedestrianY destination ordinate
	 */
	public Timeline moveTo(int pedestrianX,int pedestrianY)
	{         
        Duration duration = Duration.millis(speed);
        KeyValue keyValueX = new KeyValue(this.translateXProperty(), pedestrianX);
        KeyValue keyValueY = new KeyValue(this.translateYProperty(), pedestrianY);
        KeyFrame keyFrameX = new KeyFrame(duration , keyValueX);
        KeyFrame keyFrameY = new KeyFrame(duration ,keyValueY); 
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(keyFrameX, keyFrameY);
        return timeline;
		
	}
	
	/**
	 * subclass instantiated in pedestrian thread 
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
	
	/**
	 * @return create time plus expiration
	 */
	public double calculateExpiration()
	{
		return createTime + expiration;
	}
	
}
