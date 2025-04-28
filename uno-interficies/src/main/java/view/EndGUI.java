package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

public class EndGUI extends javax.swing.JFrame {

    private MainGUI mainGui;

    public EndGUI(String ganador, MainGUI mainGui) {
        crearFondo();
        initComponents();
        winLabel.setText("¡Ganador: " + ganador + "!");
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
        reiniciarBtton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
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
                .addContainerGap(143, Short.MAX_VALUE)
                .addComponent(reiniciarBtton)
                .addContainerGap(143, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(winLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    // Función para el fondo del juego
    private void crearFondo() {
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Se crea un objeto Graphics2D para usar gradientes
                Graphics2D g2d = (Graphics2D) g;

                // Se define el degradado: rojo oscuro (en las esquinas) a rojo claro (en el centro)
                Color rojoOscuro = new Color(139, 0, 0); // Rojo oscuro
                Color rojoClaro = new Color(255, 102, 102); // Rojo claro

                // Degradado radiactivo: centro a los bordes
                RadialGradientPaint gradiente = new RadialGradientPaint(
                        new Point2D.Float(getWidth() / 2, getHeight() / 2), // Centro del gradiente
                        getWidth() / 2, // Radio del gradiente
                        new float[]{0f, 1f}, // Distribución del color (más claro en el centro)
                        new Color[]{rojoClaro, rojoOscuro} // Colores del gradiente
                );

                g2d.setPaint(gradiente);
                g2d.fillRect(0, 0, getWidth(), getHeight()); // Rellenar con el gradiente
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton reiniciarBtton;
    private javax.swing.JLabel winLabel;
    // End of variables declaration//GEN-END:variables
}
