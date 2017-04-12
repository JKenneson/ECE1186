/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackview;

import com.rogueone.trainsystem.TrainSystem;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author kylemonto
 */
public class LegendBox implements MyShape {

    private float X, Y;
    private float xOffset = 10;
    private float yOffset = 10;
    
    private Rectangle2D legendBox;
    private Rectangle2D sectionLegendUnoccupied;
    private Rectangle2D sectionLegendOccupied;
    private Rectangle2D trainLegendGo;
    private Rectangle2D trainLegendHalt;
    private Rectangle2D trainLegendStop;
    private Rectangle2D switchLegend;
    private TrainSystem trainSystem;
    private Rectangle2D closedBlock;
    private Rectangle2D failedBlock;


    public LegendBox(float xStart, float yStart, TrainSystem trainSystem) {
        X = xStart;
        Y = yStart;
        this.trainSystem = trainSystem;
        setUp();
    }

    private void setUp() {
        legendBox = new Rectangle2D.Float(X, Y, 350, 52);
        sectionLegendUnoccupied = new Rectangle2D.Float(X + xOffset, Y + yOffset, 50, 5);
        sectionLegendOccupied = new Rectangle2D.Float(X + xOffset, Y + (2 * yOffset), 50, 5);
        closedBlock = new Rectangle2D.Float(X + xOffset, Y + (3 * yOffset) + 1, 25, 5);
        failedBlock = new Rectangle2D.Float(X + xOffset, Y + (4 * yOffset) + 2, 25, 5);
        trainLegendGo = new Rectangle2D.Float(X + xOffset + 200, Y + (1 * yOffset), 20, 5);
        trainLegendHalt = new Rectangle2D.Float(X + xOffset + 200, Y + (2 * yOffset), 20, 5);
        trainLegendStop = new Rectangle2D.Float(X + xOffset + 200, Y + (3 * yOffset) + 1, 20, 5);
        switchLegend = new Rectangle2D.Float(X + xOffset + 200, Y + (4 * yOffset) + 2, 20, 5);
        
        
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.draw(legendBox);
        
        g.setColor(Color.GREEN);
        g.draw(sectionLegendUnoccupied);
        
        
        g.draw(sectionLegendOccupied);
        g.fill(sectionLegendOccupied);
        
        g.setColor(Color.BLUE);
        g.draw(trainLegendGo);
        g.fill(trainLegendGo);
        
        g.setColor(Color.RED);
        g.draw(trainLegendHalt);
        g.fill(trainLegendHalt);
        
        g.setColor(Color.ORANGE);
        g.draw(trainLegendStop);
        g.fill(trainLegendStop);
        
        g.setColor(Color.GREEN);
        g.draw(switchLegend);
        g.setColor(Color.YELLOW);
        g.fill(switchLegend);
        
        g.setColor(Color.MAGENTA);
        g.draw(closedBlock);
        
        g.setColor(Color.RED);
        g.draw(failedBlock);
        
        
        g.setColor(Color.WHITE);
        g.drawString("Legend", X + (350/2) - 20, Y - 3);
        g.drawString(" = Unoccupied Section", X + xOffset + 50, Y + (1 * yOffset) + 5);
        g.drawString(" = Occupied Section", X + xOffset + 50, Y + (2 * yOffset) + 6);
        g.drawString(" = Closed Block", X + xOffset + 50, Y + (3 * yOffset) + 7);
        g.drawString(" = Failed Block", X + xOffset + 50, Y + (4 * yOffset) + 8);
        
        g.drawString(" = Train Moving", X + xOffset + 50 + 180, Y + (1 * yOffset) + 5);
        g.drawString(" = Train Halted", X + xOffset + 50 + 180, Y + (2 * yOffset) + 6);
        g.drawString(" = Train Stopped", X + xOffset + 50 + 180, Y + (3 * yOffset) + 7);
        g.drawString(" = Switch", X + xOffset + 50 + 180, Y + (4 * yOffset) + 8);
        
        g.setColor(Color.GREEN);
        g.drawString(this.trainSystem.getClock().printClock(), 435, 15);
        
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

    @Override
    public int getBlockID(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getBlockIDUpdate(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
