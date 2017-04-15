/**
* Contains Yard information
*
* @author: Dan Bednarczyk
* @creation date: 02/01/2017
* @modification date: 04/20/2017
*/

package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import com.rogueone.global.Global.PieceType;

public class Yard implements TrackPiece {
    
    private static final int YARD_ID = 0;

    /**
    * Do not call. Use TrackModel.enterTrack(line) instead.
    * @author Dan Bednarczyk
    * @param previous TrackPiece 
    * @return null
    */
    @Override
    public TrackPiece getNext(TrackPiece previous) {
        return null;
    }

    /**
    * Get type of TrackPiece
    * @author Dan Bednarczyk
    * @return PieceType Yard
    */
    @Override
    public Global.PieceType getType() {
        return PieceType.YARD;
    }

    /**
    * Get ID of Yard
    * @author Dan Bednarczyk
    * @return int indicating Yard ID
    */
    @Override
    public int getID() {
        return YARD_ID;
    }
    
    /**
    * Get String representation of Yard
    * @author Dan Bednarczyk
    * @return String representation of Yard
    */
    @Override
    public String toString() {
        return "Yard";
    }
    
    /**
    * Get detailed String representation of Yard
    * @author Dan Bednarczyk
    * @return String representation of Switch containing Yard ID
    */
    @Override
    public String toStringDetail() {
        return "Yard (" + YARD_ID + ")";
    }
    
    /**
    * Compare equality of TrackPiece
    * @author Dan Bednarczyk
    * @param otherPiece TrackPiece to compare
    * @return boolean true if equal, false otherwise
    */
    @Override
    public boolean equals(TrackPiece otherPiece) {
        if (otherPiece.getType() == this.getType() && otherPiece.getID() == this.getID()) {
            return true;
        }
        return false;
    }
    
}
