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
    private boolean isStopped;
    private boolean isHalted;

    private HashMap<Integer, Boolean> currentBlocks;
    private HashMap<Integer, Boolean> closedBlocks;

    private float X, Y;
    private float shiftX, shiftY;
    private float W, H;
    private float length;
    private float angle;
    private float size;
    private String sectionID;
    private Global.Line line;

    public Section(float startX, float startY, float width, float height, float angle, String sectionID, float xShift, float yShift, TrainSystem ts, Global.Line line) {
        this.X = startX;
        this.Y = startY;
        this.W = width;
        this.H = height;
        this.angle = angle;
        this.isOccupied = false;
        this.sectionID = sectionID;
        this.shiftX = xShift;
        this.shiftY = yShift;
        this.textDrawn = false;
        this.trainSystem = ts;
        this.line = line;
        this.currentBlocks = new HashMap<Integer, Boolean>();
        this.closedBlocks = new HashMap<Integer, Boolean>();
        setUp();
    }

    private void setUp() {
        section = new Rectangle2D.Float(X, Y, W, H);
        sectionPath = new Path2D.Double();
        sectionPath.append(section, false);
        sectionTransform = new AffineTransform();
        sectionTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
        sectionPath.transform(sectionTransform);
        if (this.line == Global.Line.GREEN) {
            com.rogueone.trackmodel.Section section = this.trainSystem.getTrackModel().getSection(Global.Line.GREEN, Global.Section.valueOf(this.sectionID));
            int numberOfBlocksInSection;
            if (section.getSectionID() == Global.Section.K) {
                sectionDivisions = new HashMap<Integer, Path2D>(section.getBlocks().size() + 1);
                numberOfBlocksInSection = section.getBlocks().size() + 1;
            } else if (section.getSectionID() == Global.Section.J) {
                sectionDivisions = new HashMap<Integer, Path2D>(section.getBlocks().size() - 1);
                numberOfBlocksInSection = section.getBlocks().size() - 1;
            } else {
                sectionDivisions = new HashMap<Integer, Path2D>(section.getBlocks().size());
                numberOfBlocksInSection = section.getBlocks().size();
            }

            float lengthOfBlock = W / (float) numberOfBlocksInSection;

            Iterator blockIter = section.getBlocks().iterator();
            int blockCounter = 0;
            if (section.getSectionID() == Global.Section.K) {
                Rectangle2D currentRec = new Rectangle2D.Float(X + (blockCounter * lengthOfBlock), Y, lengthOfBlock, H);
                Path2D currentBlockPath = new Path2D.Double();
                currentBlockPath.append(currentRec, false);
                AffineTransform currentBlockTransform = new AffineTransform();
                currentBlockTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
                currentBlockPath.transform(currentBlockTransform);
                sectionDivisions.put(62, currentBlockPath);
                blockCounter++;
            }
            while (blockIter.hasNext()) {
                Block currentBlock = (Block) blockIter.next();
                if (currentBlock.getID() == 62) {
                    break;
                }
                Rectangle2D currentRec = new Rectangle2D.Float(X + (blockCounter * lengthOfBlock), Y, lengthOfBlock, H);
                if (sectionID.equals("O") || sectionID.equals("Q") || sectionID.equals("F") || sectionID.equals("E")
                        || sectionID.equals("D") || sectionID.equals("B")) {
                    currentRec = new Rectangle2D.Float(X + W - (blockCounter * lengthOfBlock) - lengthOfBlock, Y, lengthOfBlock, H);
                }
                Path2D currentBlockPath = new Path2D.Double();
                currentBlockPath.append(currentRec, false);
                AffineTransform currentBlockTransform = new AffineTransform();
                currentBlockTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
                currentBlockPath.transform(currentBlockTransform);
                sectionDivisions.put(currentBlock.getID(), currentBlockPath);
                blockCounter++;
            }
        } else {
            String sectionIDString = this.sectionID.substring(0, 1);
            String sectionIDs = "";
            int sectionStart = 0;
            int sectionEnd = 0;
            if (this.sectionID.length() > 1) {
                sectionIDs = this.sectionID.substring(1, 6);
                sectionStart = Integer.parseInt(this.sectionID.substring(1, 3));
                sectionEnd = Integer.parseInt(this.sectionID.substring(4, 6));
            }

            com.rogueone.trackmodel.Section section = this.trainSystem.getTrackModel().getSection(Global.Line.RED, Global.Section.valueOf(sectionIDString));
            int numberOfBlocksInSection;
            if (section.getSectionID() == Global.Section.H && sectionIDs.equals("24_27")) {
                numberOfBlocksInSection = 4;
                sectionDivisions = new HashMap<Integer, Path2D>(numberOfBlocksInSection);
            } else if (section.getSectionID() == Global.Section.H && sectionIDs.equals("28_32")) {
                numberOfBlocksInSection = 5;
                sectionDivisions = new HashMap<Integer, Path2D>(numberOfBlocksInSection);
            } else if (section.getSectionID() == Global.Section.H && sectionIDs.equals("33_38")) {
                numberOfBlocksInSection = 6;
                sectionDivisions = new HashMap<Integer, Path2D>(numberOfBlocksInSection);
            } else if (section.getSectionID() == Global.Section.H && sectionIDs.equals("39_43")) {
                numberOfBlocksInSection = 5;
                sectionDivisions = new HashMap<Integer, Path2D>(numberOfBlocksInSection);
            } else if (section.getSectionID() == Global.Section.H && sectionIDs.equals("44_45")) {
                numberOfBlocksInSection = 2;
                sectionDivisions = new HashMap<Integer, Path2D>(numberOfBlocksInSection);
            } else if (section.getSectionID() == Global.Section.J && sectionIDs.equals("49_52")) {
                numberOfBlocksInSection = 4;
                sectionDivisions = new HashMap<Integer, Path2D>(numberOfBlocksInSection);
            } else if (section.getSectionID() == Global.Section.J && sectionIDs.equals("53_54")) {
                numberOfBlocksInSection = 2;
                sectionDivisions = new HashMap<Integer, Path2D>(numberOfBlocksInSection);
            } else {
                sectionDivisions = new HashMap<Integer, Path2D>(section.getBlocks().size());
                numberOfBlocksInSection = section.getBlocks().size();
            }

            float lengthOfBlock = W / (float) numberOfBlocksInSection;

            Iterator blockIter = section.getBlocks().iterator();
            int blockCounter = 0;
            while (blockIter.hasNext()) {
                Block currentBlock = (Block) blockIter.next();
                if ((section.getSectionID() == Global.Section.H && currentBlock.getID() >= sectionStart && currentBlock.getID() <= sectionEnd)
                        || (section.getSectionID() == Global.Section.J && currentBlock.getID() >= sectionStart && currentBlock.getID() <= sectionEnd)) {
                    Rectangle2D currentRec = new Rectangle2D.Float(X + (blockCounter * lengthOfBlock), Y, lengthOfBlock, H);
//                    if (sectionID.equals("O") || sectionID.equals("Q") || sectionID.equals("F") || sectionID.equals("E")
//                            || sectionID.equals("D") || sectionID.equals("B")) {
//                        currentRec = new Rectangle2D.Float(X + W - (blockCounter * lengthOfBlock) - lengthOfBlock, Y, lengthOfBlock, H);
//                    }
                    Path2D currentBlockPath = new Path2D.Double();
                    currentBlockPath.append(currentRec, false);
                    AffineTransform currentBlockTransform = new AffineTransform();
                    currentBlockTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
                    currentBlockPath.transform(currentBlockTransform);
                    sectionDivisions.put(currentBlock.getID(), currentBlockPath);
                    blockCounter++;
                } 
                if (section.getSectionID() != Global.Section.H && section.getSectionID() != Global.Section.J){
                    Rectangle2D currentRec = new Rectangle2D.Float(X + (blockCounter * lengthOfBlock), Y, lengthOfBlock, H);
                    if (sectionID.equals("A") || sectionID.equals("B") || sectionID.equals("C") || sectionID.equals("M") ||
                            sectionID.equals("N") || sectionID.equals("S") || sectionID.equals("P") ) {
                        currentRec = new Rectangle2D.Float(X + W - (blockCounter * lengthOfBlock) - lengthOfBlock, Y, lengthOfBlock, H);
                    }
                    Path2D currentBlockPath = new Path2D.Double();
                    currentBlockPath.append(currentRec, false);
                    AffineTransform currentBlockTransform = new AffineTransform();
                    currentBlockTransform.rotate(Math.toRadians(angle), X + (W / 2), Y + (H / 2));
                    currentBlockPath.transform(currentBlockTransform);
                    sectionDivisions.put(currentBlock.getID(), currentBlockPath);
                    blockCounter++;
                }
            }
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
                        if (isHalted) {
                            g.setColor(Color.RED);
                        } else if (isStopped && !isHalted) {
                            g.setColor(Color.ORANGE);
                        } else {
                            g.setColor(Color.BLUE);
                        }
                        g.draw(blockPath);
                        g.fill(blockPath);
                    }
                }
            }
        }
        if (!this.closedBlocks.isEmpty()) {
            Set<Entry<Integer, Boolean>> closedSet = this.closedBlocks.entrySet();
            Iterator closedIter = closedSet.iterator();
            while (closedIter.hasNext()) {
                Entry<Integer, Boolean> closedEntry = (Entry<Integer, Boolean>) closedIter.next();
                if (closedEntry.getValue() == true) {
                    Path2D blockPath = sectionDivisions.get(closedEntry.getKey());
                    g.setColor(Color.RED);
                    g.draw(blockPath);
                }
            }
        }
        g.setColor(Color.GREEN);
        g.drawString(this.sectionID, (X + (W / 2)) + shiftX, (Y + (H / 2)) + shiftY);
        textDrawn = true;
    }

    public void setIsStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    public void setIsHalted(boolean isHalted) {
        this.isHalted = isHalted;
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
        if (sectionPath.contains(x, y)) {
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

    public void addBlockToClosedBlocks(int blockID) {
        if (this.closedBlocks.containsKey(blockID)) {
            this.closedBlocks.replace(blockID, true);
        } else {
            this.closedBlocks.put(blockID, true);
        }
    }

    public void removeBlockFromCurrentBlocks(int blockID) {
        if (this.currentBlocks.containsKey(blockID)) {
            this.currentBlocks.remove(blockID);
        } else {
            //do nothing
        }
    }

    public void removeBlockFromClosedBlocks(int blockID) {
        if (this.closedBlocks.containsKey(blockID)) {
            this.closedBlocks.remove(blockID);
        } else {
            //do nothing
        }
    }

    @Override
    public int getBlockID(double x, double y) {
        for (Entry<Integer, Path2D> block : sectionDivisions.entrySet()) {
            if (block.getValue().contains(x, y)) {
                return block.getKey();
            }
        }
        return -1;
    }

    public String getSectionID() {
        return sectionID;
    }

    public boolean isIsOccupied() {
        return isOccupied;
    }

    public boolean isBlockClosed(int blockID) {
        if (closedBlocks.containsKey(blockID)) {
            if (closedBlocks.get(blockID) == true) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getBlockIDUpdate(double x, double y) {
        for (Entry<Integer, Path2D> block : sectionDivisions.entrySet()) {
            if (currentBlocks.containsKey(block.getKey())) {
                if (block.getValue().contains(x, y)
                        && currentBlocks.get(block.getKey())
                        && isStopped) {
                    return block.getKey();
                }
            }
        }
        return -1;
    }

}
