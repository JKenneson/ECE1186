/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.global.Global.PieceType;

/**
 *
 * @author Dan
 */
public interface TrackPiece {
    
    public TrackPiece getNext(TrackPiece previous);
    
    public PieceType getType();
    
    public int getID();
    
}
