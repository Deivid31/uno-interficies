package models;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.Serializable;
import javax.swing.*;
import models.enums.Colors;
import static models.enums.Colors.BLUE;
import static models.enums.Colors.GREEN;
import static models.enums.Colors.RED;
import models.enums.Types;

public class Card extends JPanel implements Serializable {

    private Colors color;
    private final Types type;
    private final int number;

    private String imagePath = "src\\main\\java\\img\\reverso\\carta_detras.png";
    private InterfaceEventDraw interfaceEventDraw;
    private Point pointPressed;
    private boolean isTurned = false; // Booleana para comprobar si la carta esta dada la vuelta
    private boolean isDraw = false; // Boleana para comprobar si la carta se ha sacado del mazo de robo o no
    private boolean isPlayed = false; // Booleana para checkear si es una carta jugada o no (Para no interactuar con las del mazo de juego)

    public Card() {
        this(0, Colors.RED, Types.NUM);
    }

    public Card(int number, Colors color, Types type) {
        this.color = color;
        this.type = type;
        this.number = number;
        setOpaque(false);
        setPreferredSize(new Dimension(40, 65));

        // Listener para las acciones del raton
        addMouseListener(new MouseAdapter() {
            // Override del evento al clicar con el raton
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isPlayed) {
                    pointPressed = e.getPoint(); // Para la posicion relativa en el componente al clicar en el
                }
            }

            // Override del evento al dejar de presionar el raton
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isPlayed) {
                    if (interfaceEventDraw != null) {
                        if (!isTurned) {
                            // Si no esta dada la vuelta llama al metodo
                            interfaceEventDraw.cardTurn();
                        }
                        Point screenPoint = e.getLocationOnScreen(); // Pasa la posición absoluta de donde esta el raton
                        // Llama al metodo pasandose a si mismo y la localización del raton
                        interfaceEventDraw.cardDropped(Card.this, screenPoint);
                    }
                }
            }
        });

        // Listener que comprueba cuando el raton esta en movimiento
        addMouseMotionListener(new MouseMotionAdapter() {
            // Override del evento al arrastrar el raton (Moverlo mientras esta presionado)
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isPlayed) {
                    if (interfaceEventDraw != null) {
                        // Llama al metodo cada que se arrastra el raton
                        interfaceEventDraw.cardDrag();
                    }
                    // Para mover/cambiar de ubicacion la carta
                    setLocation(getX() + e.getX() - pointPressed.x, getY() + e.getY() - pointPressed.y);
                }
            }
        });
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            ImageIcon imageIcon = new ImageIcon(imgFile.getAbsolutePath());
            g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawString("Imagen no encontrada", 10, 20);
        }
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public Types getType() {
        return type;
    }

    public int getNum() {
        return number;
    }

    public void addInterfaceEventDraw(InterfaceEventDraw ied) {
        this.interfaceEventDraw = ied;
    }

    public InterfaceEventDraw getInterfaceEventDraw() {
        return interfaceEventDraw;
    }

    public boolean isTurned() {
        return isTurned;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setIsTurned(boolean isTurned) {
        this.isTurned = isTurned;
    }

    public void setIsDraw(boolean isDraw) {
        this.isDraw = isDraw;
    }

    public void setIsPlayed(boolean isPlayed) {
        this.isPlayed = isPlayed;
    }

    @Override
    public String toString() {
        return this.getColor().toString() + " " + this.getNum() + " (" + this.getType().toString() + ")";
    }

    public String actColorStr() {
        switch (color) {
            case BLUE:
                return "Azul";
            case RED:
                return "Rojo";
            case GREEN:
                return "Verde";
            default:
                return "Amarillo";
        }
    }

    public Color actColor() {
        switch (color) {
            case BLUE:
                return Color.BLUE;
            case RED:
                return new Color(139, 0, 0);
            case GREEN:
                return Color.GREEN;
            default:
                return Color.YELLOW;
        }
    }
}
