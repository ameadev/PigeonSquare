package ca.uqac.inf957.pigeon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Random;

import ca.uqac.inf957.settings.Settings;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Pigeon extends Parent {
	
	private int X;
	private int Y;
	private int speed;
	
	
	//
	public Pigeon(int x, int y) throws FileNotFoundException {
		super();
		X = x;
	    Y = y;
		speed = (new Random().nextInt(5) + 1)*1000;
		
		
		
		// a remplacer par un chemin relatif
		Image pigeonImage = new Image(new FileInputStream("/Users/macpro/Desktop/UQAC/automne_2017/POAvancé/Travaux/workspace_PO/PigeonSquare/resources/pigeon.png"));
		//URL url = Pigeon.class.getResource("pigeon.png");
		//Image pigeonImage = new Image(new FileInputStream(url.toString()));
		ImageView imageView = new ImageView(pigeonImage); 
	      
        //Setting the position of the image 
		initPosition();
      
        //setting the fit height and width of the image view 
        imageView.setFitHeight(Settings.pigeonHeight); 
        imageView.setFitWidth(Settings.pigeonWidth);
   
		this.getChildren().add(imageView);
		
		
	}

    //
	public void initPosition()
	{
		this.setTranslateX(this.X);
		this.setTranslateY(this.Y);
	}
	
	//
	public double getX() {
		return this.translateXProperty().intValue();
	}

    //
	public void setX(int x) {
		X = x;
	}

    //
	public double getY() {
		return this.translateYProperty().intValue();
	}

    //
	public void setY(int y) {
		Y = y;
	}

    //
	public int getSpeed() {
		return speed;
	}

    //
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	//
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
	
	//
	public Timeline backToOrigine() //la changer pour retourner à un point random
	{         
        Duration duration = Duration.millis(speed);
        //Duration duration = Duration.seconds(1); 
        KeyValue keyValueX = new KeyValue(this.translateXProperty(), this.X);
        KeyValue keyValueY = new KeyValue(this.translateYProperty(), this.Y);
        KeyFrame keyFrameX = new KeyFrame(duration , keyValueX);
        KeyFrame keyFrameY = new KeyFrame(duration ,keyValueY); 
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(keyFrameX, keyFrameY);
        return timeline;
        //timeline.play();	
	}
	
	//
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
