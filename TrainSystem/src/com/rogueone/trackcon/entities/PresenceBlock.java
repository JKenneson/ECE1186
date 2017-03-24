/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;
import com.rogueone.trackmodel.Block;
import com.rogueone.trackmodel.TrackPiece;
import com.rogueone.trainsystem.TrainSystem;
import java.util.Objects;

/**
 *
 * @author kylemonto
 */
public class PresenceBlock {

    private int ID;
    private Block currBlock;
    private TrackPiece prevBlock;
    private TrackPiece nextBlock;

    public PresenceBlock(TrainSystem trainSystem, Global.Line line) {
        this.prevBlock = trainSystem.getTrackModel().getYard();
        this.currBlock = trainSystem.getTrackModel().getBlock(line, (line == Global.Line.GREEN) ? 152 : 77);
        this.ID = currBlock.getID();
    }

    public PresenceBlock(PresenceBlock pb) {
        this.prevBlock = pb.getPrevBlock();
        this.currBlock = pb.getCurrBlock();
        this.nextBlock = pb.getNextBlock();
    }

    public int getID() {
        return ID;
    }

    public Block getCurrBlock() {
        return currBlock;
    }

    public TrackPiece getPrevBlock() {
        return prevBlock;
    }

    public TrackPiece getNextBlock() {
        return nextBlock;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCurrBlock(Block currBlock) {
        this.currBlock = currBlock;
    }

    public void setPrevBlock(TrackPiece prevBlock) {
        this.prevBlock = prevBlock;
    }

    public void setNextBlock(TrackPiece nextBlock) {
        this.nextBlock = nextBlock;
    }

    /**
     * overridden equals method that compares only the current block and previous block for a
     * given Presence Block object
     * @param o - object (Presence Block) that will be cast to Presence Block
     * @return boolean - true if current and previous blocks match and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        PresenceBlock pb = (PresenceBlock) o;
        if (this.currBlock.equals(pb.getCurrBlock()) && this.prevBlock.equals(pb.getPrevBlock())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.currBlock);
        hash = 71 * hash + Objects.hashCode(this.prevBlock);
        hash = 71 * hash + Objects.hashCode(this.nextBlock);
        return hash;
    }

}
