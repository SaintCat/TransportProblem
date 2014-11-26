/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transportproblem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Chernyshov
 */
public class MainViewPanel extends javax.swing.JDialog {

    DefaultTableModel dilersModel;
    DefaultTableModel customersModel;
    DefaultTableModel pricesModel;

    /**
     * Creates new form MainViewPanel
     */
    public MainViewPanel(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        dilersModel = new MyDefaultTableModel(new Object[][]{}, new String[]{"Отправитель"}) {
            Class[] types = new Class[]{
                java.lang.Float.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        customersModel = new MyDefaultTableModel(new Object[][]{}, new String[]{"Получатель"}) {
            Class[] types = new Class[]{
                java.lang.Float.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        pricesModel = new MyDefaultTableModel(new Object[][]{}, new String[]{}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return Float.class;
            }
        };
        dilersTable.setModel(dilersModel);
        customersTable.setModel(customersModel);
        pricesTable.setModel(pricesModel);
        dilersSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if ((Integer) dilersSpinner.getValue() < 0) {
                    dilersSpinner.setValue(0);
                    return;
                }
                Integer value = (Integer) dilersSpinner.getValue();
                int diff = value - dilersModel.getRowCount();
                if (diff > 0) {
                    for (int i = 0; i < diff; i++) {
                        dilersModel.addRow(new Object[]{0});
                        Integer[] data = new Integer[pricesModel.getColumnCount()];
                        for (int k = 0; k < data.length; k++) {
                            data[k] = 0;
                        }
                        pricesModel.addRow(data);
                    }
                } else if (diff < 0) {
                    for (int i = 0; i < -diff; i++) {
                        dilersModel.removeRow(dilersModel.getRowCount() - 1);
                        pricesModel.removeRow(pricesModel.getRowCount() - 1);
                    }
                }
            }
        });
        customersSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if ((Integer) customersSpinner.getValue() < 0) {
                    customersSpinner.setValue(0);
                    return;
                }
                Integer value = (Integer) customersSpinner.getValue();
                int diff = value - customersModel.getRowCount();
                if (diff > 0) {
                    for (int i = 0; i < diff; i++) {
                        customersModel.addRow(new Object[]{0});
                        final Integer[] data = new Integer[pricesModel.getRowCount()];
                        for (int k = 0; k < pricesModel.getRowCount(); k++) {
                            data[k] = 0;
                        }
                        pricesModel.addColumn(pricesModel.getColumnCount(), data);

                    }
                } else if (diff < 0) {
                    for (int i = 0; i < -diff; i++) {
                        customersModel.removeRow(customersModel.getRowCount() - 1);
                        removeColumnAndData(pricesTable, pricesModel.getColumnCount() - 1);
                    }
                }

            }
        });

        maxRadioBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                maxRadioBtn.setSelected(true);
                minRadioBtn.setSelected(false);
            }

        });
        minRadioBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                maxRadioBtn.setSelected(false);
                minRadioBtn.setSelected(true);
            }

        });
        nordWestRadioBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nordWestRadioBtn.setSelected(true);
                minElementRadioBtn.setSelected(false);
            }

        });
        minElementRadioBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nordWestRadioBtn.setSelected(false);
                minElementRadioBtn.setSelected(true);
            }

        });

    }

    class MyDefaultTableModel extends DefaultTableModel {

        public MyDefaultTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        public Vector getColumnIdentifiers() {
            return columnIdentifiers;
        }
    }

    public void removeColumnAndData(JTable table, int vColIndex) {
        MyDefaultTableModel model = (MyDefaultTableModel) table.getModel();
        TableColumn col = table.getColumnModel().getColumn(vColIndex);
        int columnModelIndex = col.getModelIndex();
        Vector data = model.getDataVector();
        Vector colIds = model.getColumnIdentifiers();

        // Remove the column from the table
        table.removeColumn(col);

        // Remove the column header from the table model
        colIds.removeElementAt(columnModelIndex);

        // Remove the column data
        for (int r = 0; r < data.size(); r++) {
            Vector row = (Vector) data.get(r);
            row.removeElementAt(columnModelIndex);
        }
        model.setDataVector(data, colIds);

        // Correct the model indices in the TableColumn objects
        // by decrementing those indices that follow the deleted column
        Enumeration en = table.getColumnModel().getColumns();
        for (; en.hasMoreElements();) {
            TableColumn c = (TableColumn) en.nextElement();
            if (c.getModelIndex() >= columnModelIndex) {
                c.setModelIndex(c.getModelIndex() - 1);
            }
        }
        model.fireTableStructureChanged();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        dilersSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dilersTable = new javax.swing.JTable();
        customersSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customersTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        pricesTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        maxRadioBtn = new javax.swing.JRadioButton();
        minRadioBtn = new javax.swing.JRadioButton();
        nordWestRadioBtn = new javax.swing.JRadioButton();
        minElementRadioBtn = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Количество культур");

        dilersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(dilersTable);

        jLabel2.setText("Количество полей");

        customersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(customersTable);

        pricesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(pricesTable);

        jLabel3.setText("Стоимости");

        jButton1.setText("Загрузить из файла");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(dilersSpinner, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customersSpinner)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(375, 375, 375))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dilersSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customersSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ввод данных", jPanel2);

        maxRadioBtn.setText("Решать на максимум");

        minRadioBtn.setText("Решать на минимум");

        nordWestRadioBtn.setText("Метод северо-западного угла");

        minElementRadioBtn.setText("Метод минимального(максимального) элемента");

        jButton2.setText("Решить");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(maxRadioBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(minRadioBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(minElementRadioBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nordWestRadioBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(239, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(maxRadioBtn)
                            .addComponent(nordWestRadioBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(minRadioBtn)
                            .addComponent(minElementRadioBtn)))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                .addGap(48, 48, 48))
        );

        jTabbedPane1.addTab("Результат", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            try {
                String[] rows = new Scanner(file).useDelimiter("\\Z").next().split("\n");
                if (rows.length != 3) {
                    showErrorMessage();
                    return;
                }
                Float[] dil = null;
                Float[] customers = null;
                Float[][] pr = null;
                for (int i = 0; i < rows.length; i++) {
                    String[] splitted = rows[i].split(" ");
                    Float[] tmp = new Float[splitted.length];
                    for (int k = 0; k < splitted.length; k++) {
                        Float integ = Float.valueOf(splitted[k]);
                        tmp[k] = integ;
                    }
                    if (i == 0) {
                        dil = tmp;
                    }
                    if (i == 1) {
                        customers = tmp;
                    }
                    if (i == 2) {
                        if (tmp.length != dil.length * customers.length) {
                            showErrorMessage();
                            return;
                        }
                        pr = new Float[dil.length][];
                        for (int z = 0; z < dil.length; z++) {
                            pr[z] = new Float[customers.length];
                            for (int j = 0; j < customers.length; j++) {
                                pr[z][j] = tmp[z * customers.length + j];
                            }
                        }
                    }
                }

                dilersSpinner.setValue(dil.length);
                customersSpinner.setValue(customers.length);
                for (int i = 0; i < dil.length; i++) {
                    dilersModel.setValueAt(dil[i], i, 0);
                }
                for (int i = 0; i < customers.length; i++) {
                    customersModel.setValueAt(customers[i], i, 0);
                }
                for (int i = 0; i < pr.length; i++) {
                    for (int j = 0; j < pr[i].length; j++) {
                        pricesTable.setValueAt(pr[i][j], i, j);
                    }
                }

            } catch (FileNotFoundException ex) {
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void showErrorMessage() {
        JOptionPane.showMessageDialog(rootPane, "ERROR", "ERRoR", JOptionPane.ERROR_MESSAGE);
    }

    public float[][] getTableData() {
        int nRow = dilersModel.getRowCount(), nCol = dilersModel.getColumnCount();
        float[][] tableData = new float[nRow][nCol];
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                tableData[i][j] = Float.valueOf(dilersModel.getValueAt(i, j).toString());
            }
        }
        return tableData;
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainViewPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainViewPanel dialog = new MainViewPanel(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner customersSpinner;
    private javax.swing.JTable customersTable;
    private javax.swing.JSpinner dilersSpinner;
    private javax.swing.JTable dilersTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton maxRadioBtn;
    private javax.swing.JRadioButton minElementRadioBtn;
    private javax.swing.JRadioButton minRadioBtn;
    private javax.swing.JRadioButton nordWestRadioBtn;
    private javax.swing.JTable pricesTable;
    // End of variables declaration//GEN-END:variables
}
