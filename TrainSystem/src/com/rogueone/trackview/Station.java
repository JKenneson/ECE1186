/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;

/**
 *
 * @author kylemonto
 */
public class Station implements MyShape {

    private Polygon roof;
    private Polygon building, platform;
//    private Polygon door;

    // X, Y and size instance variables
    private int X, Y;
    private int size;

    private boolean isHighlighted;

    //creation of the Cabin shape
    public Station(int startX, int startY, int sz) {
        X = startX;
        Y = startY;
        size = sz;

        setUp();
    }

    //method that does most of the work creating the object
    private void setUp() {
        roof = new Polygon();
        roof.addPoint(X - (size / 2), Y - (size / 2));
//        roof.addPoint(X - ((size / 4) + (size / 2)), Y - ((8 * size / 10) + (size / 2)));
        roof.addPoint(X - size, Y );
        roof.addPoint(X, Y );

        building = new Polygon();
        building.addPoint(X - (size / 4), Y);
        building.addPoint(X - (size / 4) - (size / 2), Y);
        building.addPoint(X - (size / 4) - (size / 2), Y + (size / 4));
        building.addPoint(X - (size / 4), Y + (size / 4));

//        door = new Polygon();
//        door.addPoint(X - (4 * size / 10), Y);
//        door.addPoint(X - (6 * size / 10), Y);
//        door.addPoint(X - (6 * size / 10), Y - (4 * size / 10));
//        door.addPoint(X - (4 * size / 10), Y - (4 * size / 10));
        platform = new Polygon();
        platform.addPoint(X + (size / 4), Y + (size / 4));
        platform.addPoint(X + (size / 4) - size - (size / 2), Y + (size / 4));
        platform.addPoint(X + (size / 4) - size - (size / 2), Y + (size / 4) + (size / 4));
        platform.addPoint(X + (size / 4), Y + (size / 4) + (size / 4));
    }

    //true or false method for highlighting the shape
//	public void highlight(boolean b)
//	{
//		isHighlighted = b;
//	}
    //draw method that determines the the color/fill of the shape
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
//        if (isHighlighted) {
//            g.draw(roof);
//        } else {
        g.fill(roof);
//        }
        g.setColor(Color.gray);
//        if (isHighlighted) {
//            g.draw(building);
//        } else {
        g.fill(building);
//        }
        g.setColor(Color.LIGHT_GRAY);
//        if (isHighlighted) {
//            g.draw(platform);
//        } else {
        g.fill(platform);
//        }

    }

    //method that moves the shape
//	public void move(int x, int y)
//	{
//		int deltaX = x - X;
//		int deltaY = y - Y;
//		roof.translate(deltaX, deltaY);
//		cabin.translate(deltaX, deltaY);
//		chimney.translate(deltaX, deltaY);
//		door.translate(deltaX, deltaY);
//		X = x;
//		Y = y;
//		cline0.setLine(X,Y,X-size,Y);
//		cline1.setLine(X,Y-(size/10),X-size,Y-(size/10));
//		cline2.setLine(X,Y-(2*size/10),X-size,Y-(2*size/10));
//		cline3.setLine(X,Y-(3*size/10),X-size,Y-(3*size/10));
//		cline4.setLine(X,Y-(4*size/10),X-size,Y-(4*size/10));
//		cline5.setLine(X,Y-(5*size/10),X-size,Y-(5*size/10));
//		cline6.setLine(X,Y-(6*size/10),X-size,Y-(6*size/10));
//		cline7.setLine(X,Y-(7*size/10),X-size,Y-(7*size/10));
//		cline8.setLine(X,Y-(8*size/10),X-size,Y-(8*size/10));
//		clineR.setLine(X,Y,X,Y-(8*size/10));
//		clineL.setLine(X-size,Y,X-size,Y-(8*size/10));
//	}
    //Method that checks to see if the shape contains a particular point
//	public boolean contains(double x, double y)
//	{
//		if (roof.contains(x,y))
//			return true;
//		if (cabin.contains(x,y))
//			return true;
//		if (chimney.contains(x,y))
//			return true;
//		return false;
//	}
    //method to resize the shape
//	public void resize(int newsize)
//	{
//		size = newsize;
//		setUp();
//	}
    //method to format the saving of the shape
//	public String saveData()
//	{
//		return ("Cabin:" + X + ":" + Y + ":" + size);
//	}
    @Override
    public void move(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void highlight(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resize(int newsize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String saveData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
