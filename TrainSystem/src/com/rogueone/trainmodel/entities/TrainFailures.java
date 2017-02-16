/**
 * The TrainFailures.java file is an enumeration for the 3 types of failures a TrainModel can encounter
 *
 * @author Jonathan Kenneson
 * @Creation 2/9/17
 * @Modification 2/9/17
 */
package com.rogueone.trainmodel.entities;

/**
 *
 * @author Jon Kenneson
 */
public enum TrainFailures {
    /**
     * Train loses power to its components (doors, lights, temperature unit)
     */
    Power, 
    
    /**
     * Train loses ability to use its service brake
     */
    Brake, 
    
    /**
     * Train loses communication with the track and MBO
     */
    Antenna
}
