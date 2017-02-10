/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.global;

/**
 *
 * @author Dan
 */
public class Global {
    
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
    
    public enum TrackGroupsGreen {
        ABC, DEF, GHI, J, KLM, N, OPQ, RSTUVWXYZ, ZZ, YY
    }
    
    public enum Presence {
        OCCUPIED, UNOCCUPIED
    }
    
    public enum LogicGroups {
        GREEN_0, GREEN_1_2, GREEN_3, GREEN_4_5
    }
    
}
