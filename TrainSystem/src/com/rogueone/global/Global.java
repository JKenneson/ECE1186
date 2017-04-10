/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.global;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Dan
 * @author Jon Kenneson
 */
public class Global {
    
    public static final Color GREEN = new Color(125, 236, 188);
    public static final Color RED = new Color(242, 149, 149);
    
    /**
     * Comma Formatter will format all commas for large numbers
     */
    public static NumberFormat commaFormatter = NumberFormat.getInstance(Locale.US);
    /**
     * Decimal Formatter will create 1 comma for numbers between 1,000 and 100,000 as well as a decimal point with 2 places
     */
    public static DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");
    /**
     * Decimal Formatter will always show at least 1 zero with two decimal places
     */
    public static DecimalFormat gradeFormatter = new DecimalFormat("#0.00");
    
    public enum PieceType {
        BLOCK, SWITCH, YARD
    }
    
    public enum Line {
        RED, GREEN, INVALID
    }
    
    public enum Section {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, ZZ, YY, INVALID
    }
    
    public enum SectionRed {
        A, B, C, D, E, F, G, H24_27, H28_32, H33_38, H39_43, H44_45, I, J49_52, J53_54, K, L, M, N, O, P, Q, R, S, T, U
    }
    
    public enum SwitchState {
        DEFAULT, ALTERNATE
    }
    
    public enum LightState {
        GO, STOP, OFF
    }
    
    //need to alter in order to account for red line
    public enum TrackGroupsGreen {
        ABC, DEF, GHI, J, KLM, N, OPQ, RSTUVWXYZ, ZZ, YY,
    }
    
    public enum TrackGroupsRed {
        ABC, DE, F16_J52, J53_N66, U
    }
    //H28_32, H33_38, H39_43, H44_45IJ49_52, OPQ, RST
    
    public enum TrackCrossing {
        E_19, I_47
    }
    
    public enum Presence {
        OCCUPIED, UNOCCUPIED
    }
    
    public enum LogicGroups {
        GREEN_0, GREEN_1_2, GREEN_3, GREEN_4_5, RED_12, RED_6_11, RED_7, RED_8, RED_9, RED_10
    }
    
    public enum CrossingState{
        ACTIVE, INACTIVE
    } 
    
}
