package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import models.Card;
import models.Cards;
import models.Game;
import models.InterfaceEventDraw;
import models.enums.Color;

public class MainGUI extends javax.swing.JFrame {
    private Game game;
    private Image img;
    // Constructor
    public MainGUI() {
        initComponents();
        addNewCardToDeck(deck);
        game = new Game();
        usDeck.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 1));
        usDeck.setPreferredSize(new Dimension(200, 100));
        usDeck.setBackground(java.awt.Color.gray);
        //game.startGame();
    }

    // Funcion para el mazo (Estatico de momento)
    private void addNewCardToDeck(JPanel deckPanel) {
        Cards card = new Cards(Color.RED, models.enums.Type.NUM, 0); // Datos por defecto
        card.setBounds(0, 0, 50, 75); //Posicion de la carta en el mazo

        card.addInterfaceEventDraw(new InterfaceEventDraw() {
            @Override
            public void cardTurn() {
                card.setImagePath("C:\\Users\\Admin\\Desktop\\Izan\\DAM2\\M7 Desarrollo de interfaces\\UF1\\Un2\\Practicas\\uno-interficies\\uno-interficies\\src\\main\\java\\img\\rojo\\cinco_rojo.png");
                card.repaint();
            }

            @Override
            public void cardDrag() {
                deckPanel.remove(card);
                deckPanel.repaint();
                addNewCardToDeck(deckPanel);
            }
            
            @Override
            public void cardDropped(Cards droppedCard, Point screenPoint) {
                // Make a copy of the screen point
                Point panelPoint = new Point(screenPoint);

                SwingUtilities.convertPointFromScreen(panelPoint, usDeck);

                // Now you can check if the point lies within usDeck
                if (usDeck.contains(panelPoint)) {
                    Container parent = droppedCard.getParent();
                    if (parent != null) {
                        parent.remove(droppedCard);
                        parent.repaint();
                    }
                    
                    droppedCard.setIsDetached(false);
                    usDeck.add(droppedCard);
                    usDeck.revalidate();
                    usDeck.repaint();
                }
            }
        });
        deckPanel.add(card);
        deckPanel.repaint();
    }
    /**
     * No borrar
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usDeck = new javax.swing.JPanel();
        deck = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        usDeck.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                usDeckComponentAdded(evt);
            }
        });

        javax.swing.GroupLayout usDeckLayout = new javax.swing.GroupLayout(usDeck);
        usDeck.setLayout(usDeckLayout);
        usDeckLayout.setHorizontalGroup(
            usDeckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        usDeckLayout.setVerticalGroup(
            usDeckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        deck.setMinimumSize(new java.awt.Dimension(50, 75));
        deck.setPreferredSize(new java.awt.Dimension(50, 75));

        javax.swing.GroupLayout deckLayout = new javax.swing.GroupLayout(deck);
        deck.setLayout(deckLayout);
        deckLayout.setHorizontalGroup(
            deckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        deckLayout.setVerticalGroup(
            deckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(219, 219, 219)
                .addComponent(deck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(243, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usDeck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(147, Short.MAX_VALUE)
                .addComponent(deck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(usDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usDeckComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_usDeckComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_usDeckComponentAdded

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel deck;
    private javax.swing.JPanel usDeck;
    // End of variables declaration//GEN-END:variables
}