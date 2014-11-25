/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transportproblem;

import java.util.Enumeration;
import java.util.Vector;
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
            Class[] types = new Class[]{
                java.lang.Float.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        dilersTable.setModel(dilersModel);
        customersTable.setModel(customersModel);
        pricesTable.setModel(pricesModel);
//        dilersSpinner.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                if ((Integer) dilersSpinner.getValue() < 0) {
//                    dilersSpinner.setValue(0);
//                    return;
//                }
//                Integer value = (Integer) dilersSpinner.getValue();
//                int diff = value - dilersModel.getRowCount();
//                if (diff > 0) {
//                    for (int i = 0; i < diff; i++) {
//                        dilersModel.addRow(new Object[]{0});
//                        Integer[] data = new Integer[pricesModel.getColumnCount()];
//                        for (int k = 0; k < data.length; k++) {
//                            data[k] = k;
//                        }
//                        pricesModel.addRow(data);
//                    }
//                } else if (diff < 0) {
//                    for (int i = 0; i < -diff; i++) {
//                        dilersModel.removeRow(dilersModel.getRowCount() - 1);
//                        pricesModel.removeRow(pricesModel.getRowCount() - 1);
//                    }
//                }
//            }
//        });
//        customersSpinner.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                if ((Integer) customersSpinner.getValue() < 0) {
//                    customersSpinner.setValue(0);
//                    return;
//                }
//                Integer value = (Integer) customersSpinner.getValue();
//                int diff = value - customersModel.getRowCount();
//                if (diff > 0) {
//                    for (int i = 0; i < diff; i++) {
//                        customersModel.addRow(new Object[]{0});
//                        final Integer[] data = new Integer[pricesModel.getRowCount()];
//                        for (int k = 0; k < pricesModel.getRowCount(); k++) {
//                            data[k] = k;
//                        }
//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                pricesModel.addColumn(pricesModel.getColumnCount());
//                            }
//                        });
//
//                    }
//                } else if (diff < 0) {
//                    for (int i = 0; i < -diff; i++) {
//                        customersModel.removeRow(customersModel.getRowCount() - 1);
//                        removeColumnAndData(pricesTable, pricesModel.getColumnCount() - 1);
//                    }
//                }
//
//            }
//        });
        dilersSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if ((Integer) dilersSpinner.getValue() < 1) {
                    dilersSpinner.setValue(1);
                    return;
                }
                Integer value = (Integer) dilersSpinner.getValue();
                int diff = value - dilersModel.getRowCount();
                if (diff > 0) {
                    for (int i = 0; i < diff; i++) {
                        dilersModel.addRow(new Object[]{0});
                        pricesModel.addRow(new Object[]{0});
                    }
                } else if (diff < 0) {
                    for (int i = 0; i < -diff; i++) {
                        dilersModel.removeRow(dilersModel.getRowCount() - 1);
                    }
                }
            }
        });
        customersSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if ((Integer) customersSpinner.getValue() < 1) {
                    customersSpinner.setValue(1);
                    return;
                }
                Integer value = (Integer) customersSpinner.getValue();
                int diff = value - customersModel.getRowCount();
                if (diff > 0) {
                    for (int i = 0; i < diff; i++) {
                        customersModel.addRow(new Object[]{0});
                        pricesModel.addColumn("ad");
                    }
                } else if (diff < 0) {
                    for (int i = 0; i < -diff; i++) {
                        customersModel.removeRow(dilersModel.getRowCount() - 1);
                    }
                }

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
        jPanel3 = new javax.swing.JPanel();

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
                        .addGap(375, 375, 375)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dilersSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customersSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ввод данных", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 872, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable pricesTable;
    // End of variables declaration//GEN-END:variables
}
