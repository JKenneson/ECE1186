/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackview;

// CS 401 Fall 2014
// MyShape interface for Assignment 5
// See the method descriptions below and see the example implementation
// in classes Snowflake and Tree.  For the assignment you must complete
// the additional implementations in classes Cloud and Cabin.

import java.awt.*;
public interface MyShape
{                            
	// Draw the shape onto the Graphics2D context g.
	public void draw(Graphics2D g);

	// Move the shape's position from its previous location to location
	// (x,y).  Note that (x,y) is simply a reference point for the
	// shape (ex: the lower right corner).
	public void move(int x, int y);

	// Set the shape to be highlighted or not.  This method can be used
	// to indicate that the shape has been selected.  There are different
	// ways to hightlight a object.  Two examples are:
	// 1) Change its color when it is drawn
	// 2) Draw the perimeter of the underlying Shapes rather than filling
	//    them in.  For more information on this, see the Shape interface
	//    and the draw() and fill() methods.
	public void highlight(boolean b);

	// Check to see if the point (x,y) is within the shape.  This can
	// be implemented by testing to see if (x,y) is within the underlying
	// components of the shape.
	public boolean contains(double x, double y);

	// Resize the object to newsize.  Keep it in the same location.
	public void resize(int newsize);
	
	// Return a String representing this object.  The string should
	// have the following format:
	// ClassName:X:Y:size
	public String saveData();
}
