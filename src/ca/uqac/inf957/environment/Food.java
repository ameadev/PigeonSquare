package ca.uqac.inf957.environment;

import java.util.Random;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Food extends Parent 
{
	private double foodX;
	private double foodY;
	private double createTime;
	private double expiration;
	private Circle cercle = new Circle();
	
	//
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

	
	//
	public double getFoodX() {
		return foodX;
	}


	public void setFoodX(double foodX) {
		this.foodX = foodX;
	}


	public double getFoodY() {
		return foodY;
	}


	public void setFoodY(double foodY) {
		this.foodY = foodY;
	}
	
	public double getCreateTime() {
		return createTime;
	}


	public void setCreateTime(double createTime) {
		this.createTime = createTime;
	}

	public double getExpiration() {
		return expiration;
	}

	public void setExpiration(double expiration) {
		this.expiration = expiration;
	}
	
	//
	public double calculateExpiration()
	{
		return createTime + expiration;
	}
	
	//
	public void changeColor(Color newColor)
	{
		this.cercle.setFill(newColor);
	}
	
	

}
