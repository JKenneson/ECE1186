/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackview;

import com.rogueone.global.Global;
import com.rogueone.trainsystem.TrainSystem;
import java.awt.Window;
import javax.swing.SwingUtilities;

/**
 *
 * @author kylemonto
 */
public class UpdatePanel extends javax.swing.JPanel {

    /**
     * Creates new form UpdatePanel
     */
    
    private TrainSystem trainSystem;
    private Global.Line line;
    private int updateBlock;
    public UpdatePanel(TrainSystem trainSystem, Global.Line line, int blockID) {
        initComponents();
        this.trainSystem = trainSystem;
        this.line = line;
        this.updateBlock = blockID;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dispatchTrainLabel = new javax.swing.JLabel();
        speedLabel = new javax.swing.JLabel();
        authorityLabel = new javax.swing.JLabel();
        speedSpinner = new javax.swing.JSpinner();
        authoritySpinner = new javax.swing.JSpinner();
        updateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        dispatchTrainLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        dispatchTrainLabel.setText("Update Block Track Circuit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(dispatchTrainLabel, gridBagConstraints);

        speedLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        speedLabel.setText("Speed:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(speedLabel, gridBagConstraints);

        authorityLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        authorityLabel.setText("Authority:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(authorityLabel, gridBagConstraints);

        speedSpinner.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        speedSpinner.setModel(new javax.swing.SpinnerNumberModel(40.0d, 0.0d, 45.0d, 5.0d));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 85;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(speedSpinner, gridBagConstraints);

        authoritySpinner.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        authoritySpinner.setModel(new javax.swing.SpinnerNumberModel(30000.0d, 0.0d, 31721.0d, 1000.0d));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(authoritySpinner, gridBagConstraints);

        updateButton.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(updateButton, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(cancelButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        // TODO add your handling code here:
        double speed =  (double) speedSpinner.getValue();
        double authority =  (double) authoritySpinner.getValue();
        System.out.println("Speed " + speed + " authority " + authority);
        this.trainSystem.getTrackControllerHandler().requestUpdateSpeedAuthority(this.line, updateBlock, speed, authority);
        Window w = SwingUtilities.getWindowAncestor(this);
        w.dispose();
    }//GEN-LAST:event_updateButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        Window w = SwingUtilities.getWindowAncestor(this);
        w.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel authorityLabel;
    private javax.swing.JSpinner authoritySpinner;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel dispatchTrainLabel;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JSpinner speedSpinner;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}