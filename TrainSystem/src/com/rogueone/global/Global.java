/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.global;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Dan
 * @author Jon Kenneson
 */
public class Global {
    
    /**
     * Comma Formatter will format all commas for large numbers
     */
    public static NumberFormat commaFormatter = NumberFormat.getInstance(Locale.US);
    /**
     * Decimal Formatter will create 1 comma for numbers between 1,000 and 100,000 as well as a decimal point with 2 places
     */
    public static DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");
    
    public enum PieceType {
        BLOCK, SWITCH, YARD
    }
    
    public enum Line {
        RED, GREEN, INVALID
    }
    
    public enum Section {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, ZZ, YY, INVALID
    }
    
    public enum SwitchState {
        DEFAULT, ALTERNATE
    }
    
    public enum LightState {
        GO, STOP
    }
    
    public enum TrackGroups {
        ABC, DEF, GHI, J, KLM, N, OPQ, RSTUVWXYZ, ZZ, YY, U, DE15, F16_J52, J53_N66
    }
    
    public enum Presence {
        OCCUPIED, UNOCCUPIED
    }
    
    public enum LogicGroups {
        GREEN_0, GREEN_1_2, GREEN_3, GREEN_4_5, RED_12, RED_6_11
    }
    
    public enum CrossingState{
        ACTIVE, INACTIVE
    }
    
}
