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
import models.enums.Colors;
import models.enums.Types;
import models.iPlayer;

public class MainGUI extends javax.swing.JFrame {
    private Game game;
    
    // Constructor
    public MainGUI() throws Exception {
        crearFondo();
        initComponents();

        // Inicializa la primera instancia de juego
        game = new Game(this);
        // Listener para actualizar la carta en juego
        game.setListener(new GameListener() {
            @Override
            public void onCardPlayed() {
                updateCardOnGameDeck();
            }
        });
        game.startGame();
        // Layout para organizar las cartas al añadirse
        usDeck.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        // Setter del tamaño y el fondo al haber añadido un nuevo layout
        usDeck.setPreferredSize(new Dimension(200, 100));
        usDeck.setBackground(java.awt.Color.gray);
        // Ajustar la imagen del reverso de las cartas
        ImageIcon imageIcon = new ImageIcon("src\\main\\java\\img\\reverso\\carta_detras.png");
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(50, 75, java.awt.Image.SCALE_SMOOTH);  
        imageIcon = new ImageIcon(newimg);
        // Iconos del reverso de cartas para representar a los 3 rivales (Si se ponde desde la GUI la imagen queda mal)
        playerLabel1.setIcon(imageIcon);
        playerLabel2.setIcon(imageIcon);
        playerLabel3.setIcon(imageIcon);
        //Inicializa el mazo del jugador, para las cartas iniciales
        displayHumanDeck();
        // Inicializa/Añade la primera carta en el mazo de robo
        addNewCardToDeck(deck);
    }

    // Funcion para el mazo del jugador
    private void addNewCardToDeck(JPanel deckPanel) {
        // Genera/Saca una nueva carta
        Card card = game.draw();
        card.setBounds(0, 0, 50, 75); // Para especificar la posición/tamaño de la carta

        // Añade los diferentes metodos de la interfaz para la carta
        card.addInterfaceEventDraw(new InterfaceEventDraw() {
            // Metodo para cambiar la imagen del componente
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

            // Metodo para mover la carta
            @Override
            public void cardDrag() {
                Container topLevel = card.getTopLevelAncestor();
                if (!(topLevel instanceof JFrame)) {
                    return;
                }
                JFrame frame = (JFrame) topLevel;
                Container parent = card.getParent();
                /* Elimina la carta de donde este contenida (El mazo de robo o del jugador) y la reinstaura en la ventana principal
                   para poder moverla libremente*/
                if (parent != null && parent != frame.getContentPane()) {
                    parent.remove(card);
                    frame.getContentPane().add(card);
                    frame.getContentPane().setComponentZOrder(card, 0); // Mueve la carta al frente
                    // Recalcula la posicion relativa a la ventana ahora
                    Point globalPos = SwingUtilities.convertPoint(parent, card.getLocation(), frame.getContentPane());
                    card.setLocation(globalPos);
                    card.getPreferredSize();
                    parent.repaint();
                    // Si la carta no se considera robada (Primera vez que se mueve/sale del mazo de robo) entonces genera una nueva
                    if (!card.isDraw()) {
                        if(!game.getDrawDeck().isEmpty()) {
                            addNewCardToDeck(deckPanel);
                        }
                        card.setIsDraw(true);
                        /* Añade la carta sacada al mazo de la persona (En la logica), 
                        como ya estara marcada como robada, aunque se mueva no volvera a añadirse a este */
                        Human human = null;
                        for (iPlayer player : game.getPlayers()) {
                            if (player instanceof Human) {
                                human = (Human) player;
                                break;
                            }
                        }
                        human.addCard(card);
                    }
                }
            }

            // Metodo al soltar/dejar de clicar
            @Override
            public void cardDropped(Card droppedCard, Point screenPoint) {
                /* Si la carta no ha sido robada llama a la funcion de arratrar, ya que es la que tiene la logica de generar una
                   nueva carta en el mazo de robo y de añadir esta al mazo del jugador en la logica*/
                if(!droppedCard.isDraw()) {
                    droppedCard.getInterfaceEventDraw().cardDrag();
                }
                
                // Almacena la posición del raton pasada
                Point panelPointPlayDeck = new Point(screenPoint);

                // Convierte la posicion en las coordenadas relativas dentro del mazo de juego
                SwingUtilities.convertPointFromScreen(panelPointPlayDeck, gameDeck);

                // Elimina la carta del JFrame al soltarla
                Container parent = droppedCard.getParent();
                if (parent != null) {
                    parent.remove(droppedCard);
                    parent.repaint();
                }

                /* Comprueba si al soltarlo el punto/localización del raton esta dentro del mazo de juego, si es asi luego comprueba
                   que la carta se ha podido jugar correctamente/es valida, si es asi en ambas situaciones se traspasa ahi, sinó vuelve al mazo del juegador*/
                if (gameDeck.contains(panelPointPlayDeck)) {
                    game.playHumanCard(droppedCard);
                    if(game.getActualCard() == droppedCard) {
                        gameDeck.add(droppedCard);
                        gameDeck.revalidate();
                        gameDeck.repaint();
                    } else {
                        usDeck.add(droppedCard);
                        usDeck.revalidate();
                        usDeck.repaint();
                    }
                } else {
                    usDeck.add(droppedCard);
                    usDeck.revalidate();
                    usDeck.repaint();
                }
            }
        });
        //Añade la carta al mazo de robo
        deckPanel.add(card);
        deckPanel.repaint();
    }

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

    // Función para mostrar todas las cartas del mazo del jugador (Para el caso de cartas iniciales o +4/+2)
    public void displayHumanDeck() {
        // Limpia el mazo de las cartas contenidas
        usDeck.removeAll();

        // Consigue el jugador
        Human human = null;
        for (iPlayer player : game.getPlayers()) {
            if (player instanceof Human) {
                human = (Human) player;
                break;
            }
        }

        // Añade todas las cartas actuales del mazo del jugador
        for (Card card : human.getDeck()) {
            Card visualCard = card;
            if (card.getType() == Types.NUM) {
                visualCard.setImagePath("src\\main\\java\\img\\" + card.getColor() + "\\" + card.getNum() + ".png");
            } else {
                visualCard.setImagePath("src\\main\\java\\img\\" + card.getColor() + "\\" + card.getType() + ".png");
            }
            //Hay que volver a añadir los metodos, para las cartas nuevas/no robadas directamente
            visualCard.addInterfaceEventDraw(new InterfaceEventDraw() {
                // Este no hace falta, ya que todas estaran ya dadas la vuelta
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
                    if (parent != null && parent != frame.getContentPane()) {
                        parent.remove(card);
                        frame.getContentPane().add(card);
                        frame.getContentPane().setComponentZOrder(card, 0);
                        Point globalPos = SwingUtilities.convertPoint(parent, card.getLocation(), frame.getContentPane());
                        card.setLocation(globalPos);
                        card.getPreferredSize();
                        parent.repaint();
                    }
                }

                @Override
                public void cardDropped(Card droppedCard, Point screenPoint) {
                    Point panelPointPlayDeck = new Point(screenPoint);

                    SwingUtilities.convertPointFromScreen(panelPointPlayDeck, gameDeck); // <-- Your playing JPanel

                    Container parent = droppedCard.getParent();
                    if (parent != null) {
                        parent.remove(droppedCard);
                        parent.repaint();
                    }

                    if (gameDeck.contains(panelPointPlayDeck)) {
                        game.playHumanCard(droppedCard);
                        if(game.getActualCard() == droppedCard) {
                            gameDeck.add(droppedCard);
                            gameDeck.revalidate();
                            gameDeck.repaint();
                        } else {
                            usDeck.add(droppedCard);
                            usDeck.revalidate();
                            usDeck.repaint();
                        }
                    } else {
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

    public void updateCardOnGameDeck() {
        // Borra la carta previa
        gameDeck.removeAll();
        // Pide la carta actualmente en juego
        Card actualCard = game.getActualCard();
        actualCard.setBounds(0, 0, 50, 75);

        // Carga la imagen correcta dependiendo si es numero o especial (Haciendo distinción con las negras)
        if (actualCard.getType() == Types.NUM) {
            actualCard.setImagePath("src\\main\\java\\img\\" + actualCard.getColor() + "\\" + actualCard.getNum() + ".png");
        } else {
            if(actualCard.getType() == Types.DRAW4 || actualCard.getType() == Types.CHANGE_COLOR) {
                actualCard.setImagePath("src\\main\\java\\img\\" + Colors.BLACK + "\\" + actualCard.getType() + ".png");
            } else {
                actualCard.setImagePath("src\\main\\java\\img\\" + actualCard.getColor() + "\\" + actualCard.getType() + ".png");
            }
        }
        // Marca la carta como jugada
        actualCard.setIsPlayed(true);
        // La añade al JPanel que representa el mazo de juego y lo recarga/repinta
        gameDeck.add(actualCard);
        gameDeck.revalidate();
        gameDeck.repaint();
    }
    
    public void resetGame() {
        // Crea una nueva instancia de juego
        this.game = new Game(this);
        game.setListener(new GameListener() {
            @Override
            public void onCardPlayed() {
                updateCardOnGameDeck();
            }
        });
        //La inicializa y resetea la vista del mazo del jugador
        game.startGame();
        displayHumanDeck();
        addNewCardToDeck(deck);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usDeck = new javax.swing.JPanel();
        deck = new javax.swing.JPanel();
        gameDeck = new javax.swing.JPanel();
        playerLabel1 = new javax.swing.JLabel();
        playerLabel2 = new javax.swing.JLabel();
        playerLabel3 = new javax.swing.JLabel();
        deckLabel1 = new javax.swing.JLabel();
        deckLabel2 = new javax.swing.JLabel();
        deckLabel3 = new javax.swing.JLabel();
        drawDeckLabel = new javax.swing.JLabel();
        colorLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 500));

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

        deckLabel1.setForeground(new java.awt.Color(255, 255, 255));
        deckLabel1.setText("Cartas restantes: ");

        deckLabel2.setForeground(new java.awt.Color(255, 255, 255));
        deckLabel2.setText("Cartas restantes: ");

        deckLabel3.setForeground(new java.awt.Color(255, 255, 255));
        deckLabel3.setText("Cartas restantes: ");

        drawDeckLabel.setForeground(new java.awt.Color(255, 255, 255));
        drawDeckLabel.setText("Mazo: ");

        colorLabel.setForeground(new java.awt.Color(255, 255, 255));
        colorLabel.setText("Color actual: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(usDeck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deckLabel1)
                    .addComponent(playerLabel1))
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(playerLabel3)
                        .addGap(36, 36, 36))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(playerLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(gameDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(82, 82, 82)
                                        .addComponent(deck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(drawDeckLabel))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(colorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(65, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(deckLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deckLabel3)
                        .addGap(55, 55, 55))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(playerLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(deckLabel2)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(deckLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerLabel3)
                    .addComponent(playerLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(gameDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(deckLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addComponent(colorLabel)
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(drawDeckLabel)
                                    .addComponent(deck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)))
                        .addComponent(usDeck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usDeckComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_usDeckComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_usDeckComponentAdded

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel colorLabel;
    private javax.swing.JPanel deck;
    public javax.swing.JLabel deckLabel1;
    public javax.swing.JLabel deckLabel2;
    public javax.swing.JLabel deckLabel3;
    public javax.swing.JLabel drawDeckLabel;
    private javax.swing.JPanel gameDeck;
    public javax.swing.JLabel playerLabel1;
    public javax.swing.JLabel playerLabel2;
    public javax.swing.JLabel playerLabel3;
    private javax.swing.JPanel usDeck;
    // End of variables declaration//GEN-END:variables
}
