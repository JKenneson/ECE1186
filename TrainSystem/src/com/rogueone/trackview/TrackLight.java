/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackview;

//class for the construction and mutation of the Cabin Shape
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javafx.scene.shape.Circle;

public class TrackLight implements MyShape {

    private Arc2D redLight;
    private Path2D redLightPath;
    private AffineTransform redLightTransform;
    private Arc2D greenLight;
    private Path2D greenLightPath;
    private AffineTransform greenLightTransform;

    // X, Y and size instance variables
    private float X, Y, W, H;
    private float angle;
    private boolean flipped;

    private boolean isGo;
    private boolean isStop;

    //creation of the Cabin shape
    public TrackLight(float startX, float startY, float angle, boolean flipped) {
        X = startX;
        Y = startY;
        W = 10;
        H = 10;
        this.angle = angle;
        this.flipped = flipped;
        isGo = false;
        isStop = false;
        setUp();
    }

    //method that does most of the work creating the object
    private void setUp() {
        redLight = new Arc2D.Double(X, Y, 10, 10, 90, -180, Arc2D.PIE);
        redLightPath = new Path2D.Double();
        redLightPath.append(redLight, false);
        redLightTransform = new AffineTransform();
        redLightTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
        redLightPath.transform(redLightTransform);
        if(!flipped){
            greenLight = new Arc2D.Double(X, Y - 10, 10, 10, 90, -180, Arc2D.PIE);
        } else {
            greenLight = new Arc2D.Double(X, Y + 10, 10, 10, 90, -180, Arc2D.PIE);
        }
        greenLightPath = new Path2D.Double();
        greenLightPath.append(greenLight, false);
        greenLightTransform = new AffineTransform();
        greenLightTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
        greenLightPath.transform(greenLightTransform);
    }

    //true or false method for highlighting the shape
    public void highlight(boolean b) {
//        isHighlighted = b;
    }

    //draw method that determines the the color/fill of the shape
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.draw(redLightPath);
        if(isStop){
            g.fill(redLightPath);
        }
        g.setColor(Color.GREEN);
        g.draw(greenLightPath);
        if(isGo){
            g.fill(greenLightPath);
        }
        
        
    }

    //method that moves the shape
    public void move(int x, int y) {

    }

    //Method that checks to see if the shape contains a particular point
//    public boolean contains(double x, double y) {
//        if (roof.contains(x, y)) {
//            return true;
//        }
//        if (cabin.contains(x, y)) {
//            return true;
//        }
//        if (chimney.contains(x, y)) {
//            return true;
//        }
//        return false;
//    }

    //method to resize the shape
//    public void resize(int newsize) {
//        size = newsize;
//        setUp();
//    }

    //method to format the saving of the shape
//    public String saveData() {
//        return ("Cabin:" + X + ":" + Y + ":" + size);
//    }

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

    public boolean isIsGo() {
        return isGo;
    }

    public void setIsGo(boolean isGo) {
        this.isGo = isGo;
    }

    public boolean isIsStop() {
        return isStop;
    }

    public void setIsStop(boolean isStop) {
        this.isStop = isStop;
    }
    
    
}
