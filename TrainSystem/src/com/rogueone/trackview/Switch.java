/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author kylemonto
 */
public class Switch implements MyShape {

    private Rectangle2D.Float switchRec;
    private Path2D.Double switchPath;
    private AffineTransform switchTransform;

    private float dX, dY,aX, aY, W, H;
    private float dAngle, aAngle;
    private int switchID;

    private boolean isDefault = true;

    public Switch(float defaultX, float defaultY,float alternateX, float alternateY, float width, float height, float defaultAngle, float alternateAngle, int ID) {
        dX = defaultX;
        dY = defaultY;
        aX = alternateX;
        aY = alternateY;
        W = width;
        H = height;
        dAngle = defaultAngle;
        aAngle = alternateAngle;
        switchID = ID;
        setUp();
    }

    private void setUp() {
        switchRec = new Rectangle2D.Float(dX, dY, W, H);
        switchPath = new Path2D.Double();
        switchPath.append(switchRec, false);
        switchTransform = new AffineTransform();
        switchTransform.rotate(Math.toRadians(dAngle), dX + (W / 2), dY + (H / 2));
        switchPath.transform(switchTransform);

    }

    @Override
    public void draw(Graphics2D g) {
        

        if (isDefault) {
            switchRec = new Rectangle2D.Float(dX, dY, W, H);
            switchPath = new Path2D.Double();
            switchPath.append(switchRec, false);
            switchTransform = new AffineTransform();
            switchTransform.rotate(Math.toRadians(dAngle), dX + (W / 2), dY + (H / 2));
            switchPath.transform(switchTransform);
            g.setColor(Color.GREEN);
            g.draw(switchPath);
            g.setColor(Color.YELLOW);
            g.fill(switchPath);
        } else {
            switchRec = new Rectangle2D.Float(aX, aY, W, H);
            switchPath = new Path2D.Double();
            switchPath.append(switchRec, false);
            switchTransform = new AffineTransform();
            switchTransform.rotate(Math.toRadians(aAngle), aX + (W / 2), aY + (H / 2));
            switchPath.transform(switchTransform);
            g.setColor(Color.GREEN);
            g.draw(switchPath);
            g.setColor(Color.YELLOW);
            g.fill(switchPath);
        }
    }

    public int getSwitchID() {
        return switchID;
    }
    
    
    
    

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
        if(switchPath.contains(x,y)){
            return true;
        } 
        if(switchRec.contains(x, y)) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int newsize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String saveData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    @Override
    public int getBlockID(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getBlockIDUpdate(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
