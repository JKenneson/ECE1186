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
public class TrackCircuit {
    
    public byte speed;
    public short authority;
    
    public TrackCircuit() {
        speed = 0;
        authority = 0;
    }
    
    public TrackCircuit(byte newSpeed, byte newAuthority) {
        speed = newSpeed;
        authority = newAuthority;
    }
    
}
