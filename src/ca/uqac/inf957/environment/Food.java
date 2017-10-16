package ca.uqac.inf957.environment;

import java.util.Random;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * @author Mewena
 * materializes the food that will be eaten by the pigeons 
 *
 */
public class Food extends Parent 
{
	private double foodX;
	private double foodY;
	private double createTime;
	private double expiration;
	private Circle cercle = new Circle();
	
	/**
	 * constructor 
	 * @param x position abscissa on the panel
	 * @param y position ordinate on the panel
	 */
	public Food(double x, double y) 
	{
		super();
		this.foodX = x;
		this.foodY = y;
		this.createTime = System.currentTimeMillis();
		this.expiration = (new Random().nextInt(3) + 5)*1000;
		cercle.setRadius(5);
		cercle.setFill(Color.YELLOWGREEN);
		cercle.setCenterX(foodX);
		cercle.setCenterY(foodY);
		this.getChildren().add(cercle);
	}

	
	/**
	 * @return the initial abscissa
	 */
	public double getFoodX() 
	{
		return foodX;
	}

	/**
	 * @param x 
	 */
	public void setFoodX(double foodX) 
	{
		this.foodX = foodX;
	}

	/**
	 * @return the initial ordered
	 */
	public double getFoodY() 
	{
		return foodY;
	}

	/**
	 * @param y
	 */
	public void setFoodY(double foodY) 
	{
		this.foodY = foodY;
	}
	
	/**
	 * @return create date time
	 */
	public double getCreateTime() 
	{
		return createTime;
	}


	public void setCreateTime(double createTime) 
	{
		this.createTime = createTime;
	}

	/**
	 * @return the expiration date time
	 */
	public double getExpiration() 
	{
		return expiration;
	}

	public void setExpiration(double expiration) 
	{
		this.expiration = expiration;
	}
	
	/**
	 * @return create time plus expiration
	 */
	public double calculateExpiration()
	{
		return createTime + expiration;
	}
	
	/**
	 * change food color
	 * @param newColor
	 */
	public void changeColor(Color newColor)
	{
		this.cercle.setFill(newColor);
	}
	

}
