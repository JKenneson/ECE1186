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
public class Yard implements MyShape {

    private Rectangle2D yard;

    private float X, Y;
    private float W, H;
    private boolean textDrawn;

    public Yard(float startX, float startY) {
        this.X = startX;
        this.Y = startY;
        this.W = 40;
        this.H = 20;
        this.textDrawn = false;
        setUp();
    }

    private void setUp() {
        yard = new Rectangle2D.Float(X, Y, W, H);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.draw(yard);
        g.fill(yard);
        g.setColor(Color.BLACK);
        g.drawString("YARD", (X + (W / 20)), (Y + (H - (H / 2) + 5)));

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
