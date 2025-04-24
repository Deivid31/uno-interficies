package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import models.Card;
import models.Cards;
import models.Game;
import models.Human;
import models.InterfaceEventDraw;
import models.enums.Colors;
import models.iPlayer;

public class MainGUI extends javax.swing.JFrame {

    private Game game;
    private Image img;

    // Constructor
    public MainGUI() throws Exception {
        crearFondo();
        initComponents();

        addNewCardToDeck(deck);
        game = new Game();
        usDeck.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        usDeck.setPreferredSize(new Dimension(200, 100));
        usDeck.setBackground(java.awt.Color.gray);
        gameDeck.setBackground(java.awt.Color.black);
        ImageIcon imageIcon = new ImageIcon("src\\main\\java\\img\\reverso\\carta_detras.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(50, 75,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        imageIcon = new ImageIcon(newimg);
        placeHolder1.setIcon(imageIcon);
        placeHolder2.setIcon(imageIcon);
        placeHolder3.setIcon(imageIcon);
        game.startGame();
        displayHumanDeck();
    }

    // Funcion para el mazo (Estatico de momento)
    private void addNewCardToDeck(JPanel deckPanel) {
        Card card = new Card(0, Colors.RED, models.enums.Type.NUM); // Datos por defecto
        card.setBounds(0, 0, 50, 75); //Posicion de la carta en el mazo

        card.addInterfaceEventDraw(new InterfaceEventDraw() {
            @Override
            public void cardTurn() {
                card.setImagePath("src\\main\\java\\img\\rojo\\5_rojo.png");
                card.repaint();
            }

            @Override
            public void cardDrag() {
                Container topLevel = card.getTopLevelAncestor();
                if (!(topLevel instanceof JFrame)) {
                    return;
                }
                JFrame frame = (JFrame) topLevel;

                if (!card.isDetached()) {
                    Container parent = card.getParent();
                    if (parent != null) {
                        parent.remove(card);
                        frame.getContentPane().add(card);
                        frame.getContentPane().setComponentZOrder(card, 0); // Bring to front
                        Point globalPos = SwingUtilities.convertPoint(parent, card.getLocation(), frame.getContentPane());
                        card.setLocation(globalPos);
                        card.setSize(50, 75); // or card.getPreferredSize()
                        card.setIsDetached(true);
                        parent.repaint();
                    }

                    // Replace card in deck
                    addNewCardToDeck(deckPanel);
                }
            }

            @Override
            public void cardDropped(Card droppedCard, Point screenPoint) {
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

    private void displayHumanDeck() {
        // Clear existing cards if any
        usDeck.removeAll();

        // Get human player
        Human human = null;
        for (iPlayer player : game.getPlayers()) {
            if (player instanceof Human) {
                human = (Human) player;
                break;
            }
        }

        if (human == null) {
            System.err.println("No human player found!");
            return;
        }

        // Add each card to the GUI
        for (Card card : human.getDeck()) {
            Card visualCard = card;  // your custom component
            visualCard.setImagePath("src\\main\\java\\img\\"+card.getColor()+"\\"+card.getNum()+".png"); // if needed
            visualCard.setPreferredSize(new Dimension(50, 75));
            usDeck.add(visualCard);
        }
        usDeck.revalidate();
        usDeck.repaint();
    }
    /**
     * No borrar
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usDeck = new javax.swing.JPanel();
        deck = new javax.swing.JPanel();
        gameDeck = new javax.swing.JPanel();
        placeHolder1 = new javax.swing.JLabel();
        placeHolder2 = new javax.swing.JLabel();
        placeHolder3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(500, 500));
        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(500, 500));

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

        gameDeck.setMinimumSize(new java.awt.Dimension(50, 75));
        gameDeck.setPreferredSize(new java.awt.Dimension(52, 77));

        javax.swing.GroupLayout gameDeckLayout = new javax.swing.GroupLayout(gameDeck);
        gameDeck.setLayout(gameDeckLayout);
        gameDeckLayout.setHorizontalGroup(
            gameDeckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
        );
        gameDeckLayout.setVerticalGroup(
            gameDeckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        placeHolder1.setText("p1");

        placeHolder2.setText("p2");

        placeHolder3.setText("p3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(usDeck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(placeHolder1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(placeHolder3)
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addComponent(placeHolder2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(148, Short.MAX_VALUE)
                        .addComponent(deck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)
                        .addComponent(gameDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(141, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(placeHolder2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(placeHolder3)
                    .addComponent(placeHolder1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gameDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usDeckComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_usDeckComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_usDeckComponentAdded

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel deck;
    private javax.swing.JPanel gameDeck;
    private javax.swing.JLabel placeHolder1;
    private javax.swing.JLabel placeHolder2;
    private javax.swing.JLabel placeHolder3;
    private javax.swing.JPanel usDeck;
    // End of variables declaration//GEN-END:variables
}
