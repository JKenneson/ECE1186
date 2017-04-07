/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.ctc.gui;

import com.rogueone.global.Global;
import com.rogueone.trackmodel.Line;
import com.rogueone.trackmodel.Section;
import com.rogueone.trackmodel.Block;
import com.rogueone.trackmodel.TrackModel;
import com.rogueone.trainsystem.TrainSystem;

/**
 *
 * @author Robert
 */
public class TrackShutdownGUI extends javax.swing.JFrame {

    /**
     * Creates new form TrackShutdownGUI
     */
    public TrackShutdownGUI() {
        initComponents();
    }
    
    private CommandTrackControlGUI ctcGUI;
    public TrainSystem trainSystem;
    private TrackModel trackModel;
    
    public TrackShutdownGUI(CommandTrackControlGUI ctcGUI, TrainSystem ts) {
        this.ctcGUI = ctcGUI;
        this.trainSystem = ts;
        this.trackModel = ts.getTrackModel();
        initComponents();
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        LineComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        SegmentComboBox = new javax.swing.JComboBox<>();
        TrackDisableButton = new javax.swing.JButton();
        CloseTrackDisableButton = new javax.swing.JButton();
        BlockComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Please select a track segment to disable:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setText("Line");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel2, gridBagConstraints);

        LineComboBox.setModel(new javax.swing.DefaultComboBoxModel(trackModel.getLineArray().toArray()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(LineComboBox, gridBagConstraints);

        jLabel3.setText("Segment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel3, gridBagConstraints);

        SegmentComboBox.setModel(new javax.swing.DefaultComboBoxModel(((Line)(LineComboBox.getSelectedItem())).getSections().toArray()));
        SegmentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SegmentComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(SegmentComboBox, gridBagConstraints);

        TrackDisableButton.setText("Disable");
        TrackDisableButton.setPreferredSize(new java.awt.Dimension(80, 29));
        TrackDisableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrackDisableButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(TrackDisableButton, gridBagConstraints);

        CloseTrackDisableButton.setText("Close");
        CloseTrackDisableButton.setPreferredSize(new java.awt.Dimension(80, 29));
        CloseTrackDisableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseTrackDisableButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(CloseTrackDisableButton, gridBagConstraints);

        BlockComboBox.setModel(new javax.swing.DefaultComboBoxModel(((Section)(SegmentComboBox.getSelectedItem())).getBlocks().toArray()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(BlockComboBox, gridBagConstraints);

        jLabel4.setText("Block");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        getContentPane().add(jLabel5, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseTrackDisableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseTrackDisableButtonActionPerformed
        this.dispose();       // TODO add your handling code here:
    }//GEN-LAST:event_CloseTrackDisableButtonActionPerformed

    private void TrackDisableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrackDisableButtonActionPerformed
        Line lineName = (Line)(LineComboBox.getSelectedItem());
//        Global.Line lineVal = Global.Line.valueOf(lineName);
        Section segmentName = (Section)SegmentComboBox.getSelectedItem();
        Block blockName = (Block)BlockComboBox.getSelectedItem();
        
        String block = String.valueOf((Object)blockName);
        String segment = String.valueOf((Object)segmentName);
        String line = String.valueOf((Object)lineName);

        
        disableTrackSegment(line, segment, block);
        
        jLabel5.setText("Track Segment " + lineName + ":" + blockName + ":" + segmentName + " Disabled");
        
        //get fields
        //send disable signal
        // TODO add your handling code here:
    }//GEN-LAST:event_TrackDisableButtonActionPerformed

    private void SegmentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SegmentComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SegmentComboBoxActionPerformed
  
    private void disableTrackSegment(String lineName, String segmentName, String blockName){
        this.ctcGUI.DisableTrack(lineName, segmentName, Integer.parseInt(blockName));
        Global.Line lineVal = Global.Line.valueOf(lineName); 
        this.trainSystem.getTrackView().setBlockStatus(lineVal, segmentName, Integer.parseInt(blockName), false);
        this.trainSystem.getTrackControllerHandler().requestMaintenance(lineVal, Integer.parseInt(blockName));
    }
                                 
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TrackShutdownGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrackShutdownGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrackShutdownGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrackShutdownGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrackShutdownGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> BlockComboBox;
    private javax.swing.JButton CloseTrackDisableButton;
    private javax.swing.JComboBox<String> LineComboBox;
    private javax.swing.JComboBox<String> SegmentComboBox;
    private javax.swing.JButton TrackDisableButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
}