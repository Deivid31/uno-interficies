package view;

public class EndGUI extends javax.swing.JFrame {
    private MainGUI mainGui;

    public EndGUI(String ganador, MainGUI mainGui) {
        initComponents();
        winLabel.setText("Ha ganado: " + ganador);
        this.mainGui = mainGui;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        winLabel = new javax.swing.JLabel();
        reiniciarBtton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        winLabel.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        winLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        winLabel.setText("jLabel1");

        reiniciarBtton.setBackground(new java.awt.Color(0, 0, 0));
        reiniciarBtton.setForeground(new java.awt.Color(255, 102, 102));
        reiniciarBtton.setText("Jugar de nuevo");
        reiniciarBtton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reiniciarBttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(reiniciarBtton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(winLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(143, 143, 143))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(winLabel)
                .addGap(40, 40, 40)
                .addComponent(reiniciarBtton)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reiniciarBttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reiniciarBttonActionPerformed
        mainGui.resetGame();
        this.dispose();
    }//GEN-LAST:event_reiniciarBttonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton reiniciarBtton;
    private javax.swing.JLabel winLabel;
    // End of variables declaration//GEN-END:variables
}
