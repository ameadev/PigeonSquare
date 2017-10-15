package ca.uqac.inf957.runner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;

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
import javafx.util.Duration;



public class Runner extends Application 
{
	
	ArrayList<Pigeon> pigeons = new ArrayList<Pigeon>();
	ArrayList<Food> foods = new ArrayList<Food>();
	Group group;
	ArrayList<Timeline> threads = new ArrayList<Timeline>();
	//Timer timer;
	//boolean firstClic = false;
	//boolean SommeilPigeon = true;
	//int tempsSommeil;
	int clickX;
	int clickY;

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		launch(args);

	}

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
		final Button button = new Button("Stimuler le passge d'un piéton");
		group.getChildren().add(button);
		Scene scene = new Scene(group, Settings.palnelWidth, Settings.panelHeight);
        scene.setFill(Color.WHITESMOKE);
        arg0.setTitle("Pigeon Square");
        arg0.setScene(scene);
        arg0.show();
        
        //add food
        scene.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
        	public void handle(MouseEvent me){ 
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
        		
        		//tempsSommeil += new Random().nextInt(30000);//Temps aleatoire avant effrayer les pigeons
        		//lastClicked = System.currentTimeMillis();
        		//firstClic = true;
        	}
        });
        
        //clik button 
        button.setOnAction(new EventHandler<ActionEvent>() 
        { 
        	  
            public void handle(ActionEvent actionEvent) 
            { 
            	int x = new Random().nextInt(Settings.pigeonXMax); 
    			int y = 50;
    			Pedestrian pedestrian = null;
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
        //add running back tread
        ScheduledService<Void> backgroundThread = new ScheduledService<Void>()
        {
          @Override
		  protected Task<Void> createTask() 
          {
		    return new Task<Void>(){

		     @Override
	  		 protected Void call() throws Exception 
		     {
	  		    	
	  		    	for(Iterator<Food> n = foods.iterator(); n.hasNext(); )
	  	  			{
	  	  				Food i = (Food) n.next();
	  	  				// expiration
		  	  			if(System.currentTimeMillis() > i.calculateExpiration())
	  	  				{
	  	  					i.setVisible(false); 
	  	  					foods.remove(i);
	  	  				}
		  	  				
	  	  				//On essai de trouver quand les pigeons sont sur la nourriture
	  	  				Iterator<Pigeon> lepigeon = pigeons.iterator();
	  	  				while(lepigeon.hasNext())
	  	  				{   					
	  	  					Pigeon p = lepigeon.next();
	  	  					
	  	  					if((p.getX()==i.getFoodX()) && (p.getY()==i.getFoodY()))
	  	  					{
	  	  						i.setVisible(false);
	  	  						foods.remove(i);
	  	  						moveToInitialPosition();
	  	  					}
	  	  				}		
	  	  			}
	
	  		    	
	  		     /*if(System.currentTimeMillis() > lastClicked + tempsSommeil)//les pigeons sont effrayes
	  			{
	  		    	 	SommeilPigeon = false;
	  				if(tabPigeon.size() != 0 && firstClic && tabNourriture.size()==0 && !SommeilPigeon)
	  				{
	  					deplacementBallade();	
	  				}
	  			}*/
	  		        return null;
		      }
		    };
		  }
		};
		
	//backTask.setDelay(Duration.seconds(1));
	//backTask.setPeriod(Duration.seconds(3));
	backgroundThread.start();
        
        
	}
	
	//
	public void movePigeonToFood(int x, int y) throws Exception
	{
		
		if(this.pigeons.size() != 0)
		{ 
			Iterator<Pigeon> pigeonItt = this.pigeons.iterator(); //iteration sur arraylist tabPigeon
			while(pigeonItt.hasNext())
			{
				Pigeon currentPigeon =  pigeonItt.next(); 
				//prochain.moveTo(x, y);
				Thread moveToFood = new Thread(currentPigeon.new Move(currentPigeon.moveTo(x, y)));//lancement du thread
				moveToFood.start();
			}
		}
	}
	
	//
	public void moveToInitialPosition() throws Exception
	{
		
		if(this.pigeons.size() != 0)
		{ 
			Iterator<Pigeon> pigeonIterator = this.pigeons.iterator(); //iteration sur arraylist tabPigeon
			while(pigeonIterator.hasNext())
			{
				Pigeon currentPigeon =  pigeonIterator.next();
				Thread goBack = new Thread(currentPigeon.new Move(currentPigeon.backToOrigine()));//lancement du thread
				goBack.start();
			}
		}
	}
	
	
	// 
	//
	public void movePedestrian(Pedestrian pedestrian, int x, int y) throws Exception
	{
		Thread moveTPedestrian = new Thread(pedestrian.new Move(pedestrian.moveTo(x, y)));
		moveTPedestrian.start();
	    
		//
		if(this.pigeons.size() != 0)
		{ 
			Iterator<Pigeon> pigeonItt = this.pigeons.iterator(); //iteration sur arraylist tabPigeon
			while(pigeonItt.hasNext())
			{
				Pigeon currentPigeon =  pigeonItt.next(); 
				int rX = new Random().nextInt(Settings.pigeonXMax); 
				int rY = new Random().nextInt(Settings.pigeonYMax);
				Thread ramdomMove = new Thread(currentPigeon.new Move(currentPigeon.moveTo(rX, rY)));//lancement du thread
				ramdomMove.start();
			}
		}
		
		//couper les tread et donner de nouvelles positions
        
		System.out.println(pedestrian.getX());
        System.out.println(pedestrian.getY());
		if(System.currentTimeMillis() > pedestrian.calculateExpiration())
			{
			pedestrian.setVisible(false);
			}
	
	}
	

}
