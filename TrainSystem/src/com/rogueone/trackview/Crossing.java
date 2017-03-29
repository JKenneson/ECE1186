/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author kylemonto
 */
public class Crossing implements MyShape {

    private Rectangle2D firstCrossRec;
    private Path2D.Double firstCrossPath;
    private AffineTransform firstTransform;
    private Rectangle2D secondCrossRec;
    private Path2D.Double secondCrossPath;
    private AffineTransform secondTransform;
    private Arc2D redLight1;
    private Arc2D redLight2;

    private boolean isActive;

    private float X, Y;
    private float W, H;
    private float angle;

    public Crossing(float startX, float startY) {
        this.X = startX;
        this.Y = startY;
        this.W = 20;
        this.H = 5;
        this.angle = 45;
        setUp();
    }

    private void setUp() {
        firstCrossRec = new Rectangle2D.Float(X, Y, W, H);
        firstCrossPath = new Path2D.Double();
        firstCrossPath.append(firstCrossRec, false);
        firstTransform = new AffineTransform();
        firstTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
        firstCrossPath.transform(firstTransform);

        secondCrossRec = new Rectangle2D.Float(X, Y, W, H);
        secondCrossPath = new Path2D.Double();
        secondCrossPath.append(secondCrossRec, false);
        secondTransform = new AffineTransform();
        secondTransform.rotate(Math.toRadians(-angle), X + (W / 2), Y + (H / 2));
        secondCrossPath.transform(secondTransform);

        redLight1 = new Arc2D.Double(X + 3, Y + 13, 5, 5, 0, 360, Arc2D.OPEN);
        redLight2 = new Arc2D.Double(X + 12, Y + 13, 5, 5, 0, 360, Arc2D.OPEN);
    }

    @Override
    public void draw(Graphics2D g) {

        g.setColor(Color.WHITE);
        g.draw(firstCrossPath);
        g.draw(secondCrossPath);
        g.fill(firstCrossPath);
        g.fill(secondCrossPath);
        g.setColor(Color.red);
        g.draw(redLight1);
        g.draw(redLight2);
        if (isActive) {
            g.fill(redLight1);
            g.fill(redLight2);
        }
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
