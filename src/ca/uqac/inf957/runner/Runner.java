package ca.uqac.inf957.runner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import ca.uqac.inf957.environment.Food;
import ca.uqac.inf957.environment.Pedestrian;
import ca.uqac.inf957.pigeon.Pigeon;
import ca.uqac.inf957.settings.Settings;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Mewena
 * materializes the main tread   
 *
 */
public class Runner extends Application 
{
	
	ArrayList<Pigeon> pigeons = new ArrayList<Pigeon>();
	ArrayList<Food> foods = new ArrayList<Food>();
	Pedestrian pedestrian = null;
	Group group;
	ArrayList<Timeline> threads = new ArrayList<Timeline>();
	int clickX;
	int clickY;
    
	/**
	 * main
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		launch(args);

	}
    
	/**
	 * javaFx start methode 
	 */
	@Override
	public void start(Stage arg0) throws Exception 
	{
		// TODO Auto-generated method stub
		group =  new Group();
		int pigeonNumber = (new Random().nextInt(15) + 1);
		for(int i=1 ; i<= pigeonNumber ; i++)
		{
			
			int x = new Random().nextInt(Settings.pigeonXMax); 
			int y = new Random().nextInt(Settings.pigeonYMax);
			Pigeon pigeon = new Pigeon(x, y);
			group.getChildren().add(pigeon);
		    try 
		    {
		    	pigeons.add(pigeon);
		    } 
		    catch (NullPointerException e){ e.printStackTrace();}

		}
		final Button button = new Button("Simuler le passge d'un piéton");
		group.getChildren().add(button);
		Scene scene = new Scene(group, Settings.palnelWidth, Settings.panelHeight);
        scene.setFill(Color.WHITESMOKE);
        arg0.setTitle("Pigeon Square");
        arg0.setScene(scene);
        arg0.show();
        
        //add food
        scene.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
        	public void handle(MouseEvent me)
        	{ 
        		int foodX = (int)me.getSceneX();
        		//clickX = (int)me.getSceneX();
        		//clickY = (int)me.getSceneY();
        		int foodY = (int)me.getSceneY();
        		for(Iterator<Food> foodIterator = foods.iterator(); foodIterator.hasNext(); )
  	  			{
  	  				Food i = foodIterator.next();
  	  				i.changeColor(Color.BURLYWOOD);
  	  			}
        		Food food = new Food (foodX, foodY);
        		group.getChildren().add(food);
        		foods.add(food);
        		//System.out.println("x "+foodX+" y "+foodY);
        		
        		//move pigeon to new food
        		try 
        		{
					movePigeonToFood(foodX, foodY);
				} 
        		catch (Exception e)
        		{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	
        	}
        });
        
        //clik button 
        button.setOnAction(new EventHandler<ActionEvent>() 
        { 
        	  
            public void handle(ActionEvent actionEvent) 
            { 
            	int x = new Random().nextInt(Settings.pigeonXMax); 
    			int y = 20;
    			
				try {
					pedestrian = new Pedestrian(x, y);
					group.getChildren().add(pedestrian);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					movePedestrian(pedestrian, y, Settings.pigeonYMax);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    		     
            } 
        });
       
        /**
    	 * running back tread 
    	 */
        ScheduledService<Void> backgroundThread = new ScheduledService<Void>()
        {
          @Override
		  protected Task<Void> createTask() 
          {
		    return new Task<Void>(){

		     @Override
	  		 protected Void call() throws Exception 
		     {
	  		    	
	  		        Iterator<Food> foodIteratorn = foods.iterator();
  	  				while(foodIteratorn.hasNext())
	  		    	{
	  	  				Food food = foodIteratorn.next();
	  	  				// food expiration
		  	  			if(System.currentTimeMillis() > food.calculateExpiration())
	  	  				{
	  	  					food.setVisible(false); 
	  	  					foods.remove(food);
	  	  				}
		  	  				
	  	  				Iterator<Pigeon> pigeonIterator = pigeons.iterator();
	  	  				while(pigeonIterator.hasNext())
	  	  				{   					
	  	  					Pigeon pigeon = pigeonIterator.next();
	  	  					
	  	  					if((pigeon.getX() == food.getFoodX()) && (pigeon.getY() == food.getFoodY()))
	  	  					{
	  	  						food.setVisible(false);
	  	  						foods.remove(food);
	  	  						moveToInitialPosition();
	  	  					}
	  	  				}		
	  	  			}
	  		    	
	  		    	//pedestrian expiration 
	  		    	if((System.currentTimeMillis() > pedestrian.calculateExpiration()) && (pedestrian !=null))
	  				{
	  				pedestrian.setVisible(false);
	  				}
	  		    	
	  		        return null;
		      }
		    };
		  }
		};
	
	//	
	backgroundThread.start();
          
	}
	
	/**
	 *  pigeons moving to food threads 
	 */
	public void movePigeonToFood(int x, int y) throws Exception
	{
		
		if(this.pigeons.size() != 0)
		{ 
			Iterator<Pigeon> pigeonItt = this.pigeons.iterator(); 
			while(pigeonItt.hasNext())
			{
				Pigeon currentPigeon =  pigeonItt.next();
				Thread moveToFood = new Thread(currentPigeon.new Move(currentPigeon.moveTo(x, y)));
				moveToFood.start();
			}
		}
	}
	
	/**
	 *  pigeons moving to their  init position threads 
	 */
	public void moveToInitialPosition() throws Exception
	{
		
		if(this.pigeons.size() != 0)
		{ 
			Iterator<Pigeon> pigeonIterator = this.pigeons.iterator(); 
			while(pigeonIterator.hasNext())
			{
				Pigeon currentPigeon =  pigeonIterator.next();
				Thread goBack = new Thread(currentPigeon.new Move(currentPigeon.backToOrigine()));
				goBack.start();
			}
		}
	}
	
	
	/**
	 *  pedestrian moving threads 
	 */
	public void movePedestrian(Pedestrian pedestrian, int x, int y) throws Exception
	{
		Thread moveTPedestrian = new Thread(pedestrian.new Move(pedestrian.moveTo(x, y)));
		moveTPedestrian.start();
	    
		//
		if(this.pigeons.size() != 0)
		{ 
			Iterator<Pigeon> pigeonItt = this.pigeons.iterator(); 
			while(pigeonItt.hasNext())
			{
				Pigeon currentPigeon =  pigeonItt.next(); 
				int rX = new Random().nextInt(Settings.pigeonXMax); 
				int rY = new Random().nextInt(Settings.pigeonYMax);
				Thread ramdomMove = new Thread(currentPigeon.new Move(currentPigeon.moveTo(rX, rY)));
				ramdomMove.start();
			}
		}
	
	}
	
	
}
