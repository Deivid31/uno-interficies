package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import models.Card;
import models.Game;
import models.GameListener;
import models.Human;
import models.InterfaceEventDraw;
import models.enums.Types;
import models.iPlayer;

public class MainGUI extends javax.swing.JFrame {

    private Game game;
    private Image img;

    // Constructor
    public MainGUI() throws Exception {
        crearFondo();
        initComponents();

        game = new Game(this);
        game.setListener(new GameListener() {
            @Override
            public void onCardPlayed() {
                updateCardOnGameDeck();
            }
        });
        game.startGame();
        addNewCardToDeck(deck);

        usDeck.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        usDeck.setPreferredSize(new Dimension(200, 100));
        usDeck.setBackground(java.awt.Color.gray);
        ImageIcon imageIcon = new ImageIcon("src\\main\\java\\img\\reverso\\carta_detras.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(50, 75, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        imageIcon = new ImageIcon(newimg);
        playerLabel1.setIcon(imageIcon);
        playerLabel2.setIcon(imageIcon);
        playerLabel3.setIcon(imageIcon);
        displayHumanDeck();
    }

    // Funcion para el mazo (Estatico de momento)
    private void addNewCardToDeck(JPanel deckPanel) {
        Card card = game.draw();
        card.setBounds(0, 0, 50, 75); //Posicion de la carta en el mazo

        card.addInterfaceEventDraw(new InterfaceEventDraw() {
            @Override
            public void cardTurn() {
                if (card.getType() == Types.NUM) {
                    card.setImagePath("src\\main\\java\\img\\" + card.getColor() + "\\" + card.getNum() + ".png");
                } else {
                    card.setImagePath("src\\main\\java\\img\\" + card.getColor() + "\\" + card.getType() + ".png");
                }
                card.repaint();
                card.setIsTurned(true);
            }

            @Override
            public void cardDrag() {
                Container topLevel = card.getTopLevelAncestor();
                if (!(topLevel instanceof JFrame)) {
                    return;
                }
                JFrame frame = (JFrame) topLevel;
                Container parent = card.getParent();
                if (parent != null) {
                    parent.remove(card);
                    frame.getContentPane().add(card);
                    frame.getContentPane().setComponentZOrder(card, 0); // Bring to front
                    Point globalPos = SwingUtilities.convertPoint(parent, card.getLocation(), frame.getContentPane());
                    card.setLocation(globalPos);
                    card.getPreferredSize();
                    card.setIsDetached(true);
                    parent.repaint();
                    // Replace card in deck
                    if (!card.isDraw()) {
                        addNewCardToDeck(deckPanel);
                        card.setIsDraw(true);
                    }
                }
            }

            @Override
            public void cardDropped(Card droppedCard, Point screenPoint) {
                Point panelPointUsDeck = new Point(screenPoint);
                Point panelPointPlayDeck = new Point(screenPoint);

                // Convert screen point to local coordinates for each panel
                SwingUtilities.convertPointFromScreen(panelPointUsDeck, usDeck);
                SwingUtilities.convertPointFromScreen(panelPointPlayDeck, gameDeck); // <-- Your playing JPanel

                Container parent = droppedCard.getParent();
                if (parent != null) {
                    parent.remove(droppedCard);
                    parent.repaint();
                }

                if (gameDeck.contains(panelPointPlayDeck)) {
                    game.playHumanCard(droppedCard);
                    if(game.getActualCard() == droppedCard) {
                        droppedCard.setIsDetached(false);
                        gameDeck.add(droppedCard);
                        gameDeck.revalidate();
                        gameDeck.repaint();
                    } else {
                        droppedCard.setIsDetached(false);
                        usDeck.add(droppedCard);
                        usDeck.revalidate();
                        usDeck.repaint();
                    }
                } else {
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

        // Añadir cartas iniciales
        for (Card card : human.getDeck()) {
            Card visualCard = card;
            if (card.getType() == Types.NUM) {
                visualCard.setImagePath("src\\main\\java\\img\\" + card.getColor() + "\\" + card.getNum() + ".png");
            } else {
                visualCard.setImagePath("src\\main\\java\\img\\" + card.getColor() + "\\" + card.getType() + ".png");
            }
            visualCard.addInterfaceEventDraw(new InterfaceEventDraw() {
                @Override
                public void cardTurn() {
                }

                @Override
                public void cardDrag() {
                    Container topLevel = card.getTopLevelAncestor();
                    if (!(topLevel instanceof JFrame)) {
                        return;
                    }
                    JFrame frame = (JFrame) topLevel;
                    Container parent = card.getParent();
                    if (parent != null) {
                        parent.remove(card);
                        frame.getContentPane().add(card);
                        frame.getContentPane().setComponentZOrder(card, 0); // Bring to front
                        Point globalPos = SwingUtilities.convertPoint(parent, card.getLocation(), frame.getContentPane());
                        card.setLocation(globalPos);
                        card.getPreferredSize();
                        card.setIsDetached(true);
                        parent.repaint();
                    }
                }

                @Override
                public void cardDropped(Card droppedCard, Point screenPoint) {
                    Point panelPointUsDeck = new Point(screenPoint);
                    Point panelPointPlayDeck = new Point(screenPoint);

                    // Convert screen point to local coordinates for each panel
                    SwingUtilities.convertPointFromScreen(panelPointUsDeck, usDeck);
                    SwingUtilities.convertPointFromScreen(panelPointPlayDeck, gameDeck); // <-- Your playing JPanel

                    Container parent = droppedCard.getParent();
                    if (parent != null) {
                        parent.remove(droppedCard);
                        parent.repaint();
                    }

                    if (gameDeck.contains(panelPointPlayDeck)) {
                        game.playHumanCard(droppedCard);
                        if(game.getActualCard() == droppedCard) { // Los hilos se reanudan antes 
                            droppedCard.setIsDetached(false);
                            gameDeck.add(droppedCard);
                            gameDeck.revalidate();
                            gameDeck.repaint();
                        } else {
                            droppedCard.setIsDetached(false);
                            usDeck.add(droppedCard);
                            usDeck.revalidate();
                            usDeck.repaint();
                        }
                    } else {
                        droppedCard.setIsDetached(false);
                        usDeck.add(droppedCard);
                        usDeck.revalidate();
                        usDeck.repaint();
                    }
                }
            });
            usDeck.add(visualCard);
        }
        usDeck.revalidate();
        usDeck.repaint();
    }

    private void updateCardOnGameDeck() {
        gameDeck.removeAll();
        Card actualCard = game.getActualCard();
        actualCard.setBounds(0, 0, 50, 75);

        // Load correct image
        if (actualCard.getType() == Types.NUM) {
            actualCard.setImagePath("src\\main\\java\\img\\" + actualCard.getColor() + "\\" + actualCard.getNum() + ".png");
        } else {
            actualCard.setImagePath("src\\main\\java\\img\\" + actualCard.getColor() + "\\" + actualCard.getType() + ".png");
        }
        actualCard.setIsPlayed(true);
        gameDeck.add(actualCard);
        gameDeck.revalidate();
        gameDeck.repaint();
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
        playerLabel1 = new javax.swing.JLabel();
        playerLabel2 = new javax.swing.JLabel();
        playerLabel3 = new javax.swing.JLabel();

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
        deck.setOpaque(false);
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
        gameDeck.setOpaque(false);
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

        playerLabel1.setForeground(new java.awt.Color(255, 255, 255));
        playerLabel1.setText("p1");

        playerLabel2.setForeground(new java.awt.Color(255, 255, 255));
        playerLabel2.setText("p2");

        playerLabel3.setForeground(new java.awt.Color(255, 255, 255));
        playerLabel3.setText("p3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(usDeck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(playerLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(playerLabel3)
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(playerLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(210, 210, 210)
                        .addComponent(gameDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(deck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(playerLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerLabel3)
                    .addComponent(playerLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(deck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(usDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(gameDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
    public javax.swing.JLabel playerLabel1;
    public javax.swing.JLabel playerLabel2;
    public javax.swing.JLabel playerLabel3;
    private javax.swing.JPanel usDeck;
    // End of variables declaration//GEN-END:variables
}
