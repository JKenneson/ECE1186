/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.ctc.gui;

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
        BlockComboBox = new javax.swing.JComboBox<>();
        TrackDisableButton = new javax.swing.JButton();
        CloseTrackDisableButton = new javax.swing.JButton();
        SegmentComboBox = new javax.swing.JComboBox<>();
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

        LineComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Green", "Red" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(LineComboBox, gridBagConstraints);

        jLabel3.setText("Block");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jLabel3, gridBagConstraints);

        BlockComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));
        BlockComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BlockComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(BlockComboBox, gridBagConstraints);

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

        SegmentComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(SegmentComboBox, gridBagConstraints);

        jLabel4.setText("Segment");
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
        String lineName = (String)LineComboBox.getSelectedItem();
        String segmentName = (String)SegmentComboBox.getSelectedItem();
        String blockName = (String)BlockComboBox.getSelectedItem();
        
        disableTrackSegment(lineName, segmentName, blockName);
        
        jLabel5.setText("Track Segment " + lineName + ":" + blockName + ":" + segmentName + " Disabled");
        
        
        //get fields
        //send disable signal
// TODO add your handling code here:
    }//GEN-LAST:event_TrackDisableButtonActionPerformed

    private void BlockComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BlockComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BlockComboBoxActionPerformed
  
    private void disableTrackSegment(String lineName, String segmentName, String blockName){
        //globally disable track segment
        //update failure table
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
