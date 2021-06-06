/*
 * TransferMasterdata.java
 *
 * Created on March 17, 2005, 11:53 AM
 */

package com.dimata.pos.cashier;

import com.dimata.posbo.session.transferdata.TransMasterdata;
import com.dimata.util.Validator;
import com.dimata.util.YearMonth;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;


/**
 *
 * @author  yogi
 */
//public class TransferMasterdata extends javax.swing.JFrame {
public class TransferMasterdata extends JDialog {
    public static String PATH_DATA_OUT = "C:/data_out/";
    public static String PATH_DATA_IN = "C:/data_in/";
    public static String PATH_MYSQL = "C:/mysql/bin/";
    public int language = 1;
    public static String PROP_DATA_OUT = "data.out";
    public static String PROP_DATA_IN = "data.in";
    public static String PROP_MYSQL = "mysql";
    public static String[] table_names =
     {"pos_material_temp","contact_list_temp"};
    
    /** Creates new form TransferMasterdata */
    public TransferMasterdata() {
        initComponents();
        initAll();        
    }
    
    public TransferMasterdata(JFrame parent, boolean modal) {
        super(parent,modal);
        initComponents();
        initAll();        
    }
    
    public void initAll(){
        this.jComboBox1.setModel(new DefaultComboBoxModel(cmbDate()));
        this.jComboBox2.setModel(new DefaultComboBoxModel(cmbMonth()));
        this.jComboBox4.setModel(new DefaultComboBoxModel(cmbDate()));
        this.jComboBox5.setModel(new DefaultComboBoxModel(cmbMonth()));
        
        Date dateNow = new Date();
        if(this.jComboBox1.getSelectedIndex()==0){
            jComboBox1.setSelectedIndex((dateNow.getDate()-1));
        }
        
        if(this.jComboBox4.getSelectedIndex()==0){
            jComboBox4.setSelectedIndex((dateNow.getDate()-1));
        }
        
        if(this.jComboBox2.getSelectedIndex()==0){
            jComboBox2.setSelectedIndex((Validator.getIntMonth(dateNow))-1);
        }
        if(this.jComboBox5.getSelectedIndex()==0){
            jComboBox5.setSelectedIndex((Validator.getIntMonth(dateNow))-1);
        }
        Vector vectYear = cmbYear(dateNow,5,-3);
        
        this.jComboBox3.setModel(new DefaultComboBoxModel(vectYear));
        if(this.jComboBox3.getSelectedIndex()==0){
            jComboBox3.setSelectedIndex(indexCmbYear(dateNow,vectYear));
        }
        this.jComboBox6.setModel(new DefaultComboBoxModel(vectYear));
        if(this.jComboBox6.getSelectedIndex()==0){
            jComboBox6.setSelectedIndex(indexCmbYear(dateNow,vectYear));
        }        
    }
    
     /* method to get path data */
    public void getPathData(){
         PATH_DATA_OUT = TransferAllData.getProperties(PROP_DATA_OUT);
         PATH_DATA_IN = TransferAllData.getProperties(PROP_DATA_IN);
         PATH_MYSQL = TransferAllData.getProperties(PROP_MYSQL);
    }
    
    public Vector cmbDate() {
        Vector result = new Vector();
        for(int i=1;i<=31;i++){
            result.add(""+i);
        }
        
        return result;
    }
    
    public Vector cmbMonth() {
        Vector result = new Vector();
        for(int i=1;i<=12;i++){
            result.add(YearMonth.getShortInaMonthName(i));
        }
        return result;
    }
    
    public Vector cmbYear(Date dt,int interval,int milestone) {
        Vector result = new Vector();
        int yr = Validator.getIntYear(dt);
        int loop = 0;
        boolean check = false;
        for(int i=(yr+milestone);i<=(yr+(interval+milestone));i++){
            result.add(""+i);
        }                
        return result;
    }
    
    public int indexCmbYear(Date dt, Vector cmbYear) {
        int yr = Validator.getIntYear(dt);
        int loop = 0;
        boolean check = false;
        for(int i=0;i<cmbYear.size();i++){
            String str = (String)cmbYear.get(i);
            if(str.equals(String.valueOf(yr))){
                check = true;
                return loop;
            }
            if(!check){
                loop++;
            }
        }
        return 0;
    }
    
    public Date composeDate(JComboBox jCombo1,JComboBox jCombo2,JComboBox jCombo3){
        Date dtNew = new Date();
        int date = Integer.parseInt((String)jCombo1.getSelectedItem());        
        int month = jCombo2.getSelectedIndex()+1;
        int year = Integer.parseInt((String)jCombo3.getSelectedItem());
        
        dtNew = new Date((year-1900),(month-1),date);
        return dtNew;
    }
    
    /* method for begin transfer */
    public void transferData(){
        getPathData();
        TransMasterdata transferMasterdata = new TransMasterdata(table_names,PATH_MYSQL,PATH_DATA_OUT);
        Date dateFrom = composeDate(jComboBox1, jComboBox2, jComboBox3);        
        Date dateTo = composeDate(jComboBox4, jComboBox5, jComboBox6);
        int result = transferMasterdata.transferToClient(dateFrom,dateTo);
        String msg = transferMasterdata.statusNames[language][result];
        JOptionPane.showMessageDialog(this,msg,"Transfer result",JOptionPane.OK_OPTION); 
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Transfer");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(new javax.swing.border.TitledBorder(null, "Transfer Masterdata", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        jLabel1.setText("Date transfer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jLabel1, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "31" }));
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox1KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jComboBox1, gridBagConstraints);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January" }));
        jComboBox2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox2KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jComboBox2, gridBagConstraints);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2005" }));
        jComboBox3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox3KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jComboBox3, gridBagConstraints);

        jLabel2.setText("until");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jLabel2, gridBagConstraints);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "31" }));
        jComboBox4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox4KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jComboBox4, gridBagConstraints);

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "December" }));
        jComboBox5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox5KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jComboBox5, gridBagConstraints);

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2005" }));
        jComboBox6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox6KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jComboBox6, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/BtnSearch.jpg")));
        jButton1.setText("Transfer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jButton1, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        pack();
    }//GEN-END:initComponents

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            transferData();
            jComboBox1.requestFocusInWindow();          
        }
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            //System.exit(0);
            this.dispose();
        }
    }//GEN-LAST:event_jButton1KeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Add your handling code here:
        transferData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox6KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            jButton1.requestFocusInWindow();          
        }
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            //System.exit(0);
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox6KeyPressed

    private void jComboBox5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox5KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            jComboBox6.requestFocusInWindow();          
        }
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            //System.exit(0);
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox5KeyPressed

    private void jComboBox4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox4KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            jComboBox5.requestFocusInWindow();          
        }
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            //System.exit(0);
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox4KeyPressed

    private void jComboBox3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox3KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            jComboBox4.requestFocusInWindow();          
        }
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            //System.exit(0);
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox3KeyPressed

    private void jComboBox2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox2KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            jComboBox3.requestFocusInWindow();          
        }
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            //System.exit(0);
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox2KeyPressed

    private void jComboBox1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyPressed
        // Add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            jComboBox2.requestFocusInWindow();          
        }
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){
            //System.exit(0);
            this.dispose();
        }
    }//GEN-LAST:event_jComboBox1KeyPressed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        //System.exit(0);
        this.dispose();
    }//GEN-LAST:event_exitForm
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {      
      TransferMasterdata frame =   new TransferMasterdata();  
      frame.setSize(600,100);
      frame.show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
