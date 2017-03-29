/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackview;

import com.rogueone.global.Global;
import com.rogueone.trackmodel.Block;
import com.rogueone.trainsystem.TrainSystem;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author kylemonto
 */
public class Section implements MyShape {

    private Rectangle2D section;
    private Path2D.Double sectionPath;
    private AffineTransform sectionTransform;
    private HashMap<Integer, Path2D> sectionDivisions;
    private TrainSystem trainSystem;

    private boolean isOccupied;
    private boolean isClosed;
    private boolean isBroken;
    private boolean textDrawn;

    private HashMap<Integer, Boolean> currentBlocks;

    private float X, Y;
    private float shiftX, shiftY;
    private float W, H;
    private float length;
    private float angle;
    private float size;
    private String sectionID;

    public Section(float startX, float startY, float width, float height, float angle, String sectionID, float xShift, float yShift, TrainSystem ts) {
        this.X = startX;
        this.Y = startY;
        this.W = width;
        this.H = height;
        this.angle = angle;
        this.sectionID = sectionID;
        this.shiftX = xShift;
        this.shiftY = yShift;
        this.textDrawn = false;
        this.trainSystem = ts;
        this.currentBlocks = new HashMap<Integer, Boolean>();
        setUp();
    }

    private void setUp() {
        section = new Rectangle2D.Float(X, Y, W, H);
        sectionPath = new Path2D.Double();
        sectionPath.append(section, false);
        sectionTransform = new AffineTransform();
        sectionTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
        sectionPath.transform(sectionTransform);
        com.rogueone.trackmodel.Section section = this.trainSystem.getTrackModel().getSection(Global.Line.GREEN, Global.Section.valueOf(this.sectionID));
        int numberOfBlocksInSection = section.getBlocks().size();
        sectionDivisions = new HashMap<Integer, Path2D>(section.getBlocks().size());

        float lengthOfBlock = W / (float) numberOfBlocksInSection;

        Iterator blockIter = section.getBlocks().iterator();
        int blockCounter = 0;
        while (blockIter.hasNext()) {
            Block currentBlock = (Block) blockIter.next();
            Rectangle2D currentRec = new Rectangle2D.Float(X + (blockCounter * lengthOfBlock), Y, lengthOfBlock, H);
            if (sectionID.equals("O") || sectionID.equals("Q") || sectionID.equals("F") || sectionID.equals("E") || 
                    sectionID.equals("D") || sectionID.equals("B") ){
                currentRec = new Rectangle2D.Float(X + W - (blockCounter * lengthOfBlock) - lengthOfBlock, Y, lengthOfBlock, H);
            }
            Path2D currentBlockPath = new Path2D.Double();
            currentBlockPath.append(currentRec, false);
            AffineTransform currentBlockTransform = new AffineTransform();
//            currentBlockTransform.rotate(Math.toRadians(angle), (X + (blockCounter * lengthOfBlock)) + (lengthOfBlock / 2), Y + (H / 2));
            currentBlockTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
            currentBlockPath.transform(currentBlockTransform);
            sectionDivisions.put(currentBlock.getID(), currentBlockPath);
            blockCounter++;
        }

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.draw(sectionPath);
        if (isOccupied) {
            g.setColor(Color.GREEN);
            g.fill(sectionPath);
            if (!this.currentBlocks.isEmpty()) {
                Set<Entry<Integer, Boolean>> blockSet = this.currentBlocks.entrySet();
                Iterator blockIter = blockSet.iterator();
                while (blockIter.hasNext()) {
                    Entry<Integer, Boolean> blockEntry = (Entry<Integer, Boolean>) blockIter.next();
                    if (blockEntry.getValue() == true) {
                        Path2D blockPath = sectionDivisions.get(blockEntry.getKey());
                        g.setColor(Color.GREEN);
                        g.draw(blockPath);
                        g.setColor(Color.MAGENTA);
                        g.fill(blockPath);
                    }
                }
            }
        }
        g.setColor(Color.GREEN);
        g.drawString(this.sectionID, (X + (W / 2)) + shiftX, (Y + (H / 2)) + shiftY);
        textDrawn = true;
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

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public void setIsBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    public void addBlockToCurrentBlocks(int blockID) {
        if (this.currentBlocks.containsKey(blockID)) {
            this.currentBlocks.replace(blockID, true);
        } else {
            this.currentBlocks.put(blockID, true);
        }

    }

    public void removeBlockFromCurrentBlocks(int blockID) {
        if (this.currentBlocks.containsKey(blockID)) {
            this.currentBlocks.remove(blockID);
        } else {
            //do nothing
            System.out.println("nothing to be done");
        }
    }

}
