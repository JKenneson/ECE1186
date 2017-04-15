/**
* Interface for Block, Switch, and Yard
*
* @author: Dan Bednarczyk
* @creation date: 02/01/2017
* @modification date: 04/20/2017
*/

package com.rogueone.trackmodel;

import com.rogueone.global.Global.PieceType;

public interface TrackPiece {
    
    public TrackPiece getNext(TrackPiece previous);
    
    public PieceType getType();
    
    public int getID();
    
    public boolean equals(TrackPiece otherPiece);
    
    @Override
    public String toString();
    
    public String toStringDetail();
     
}
