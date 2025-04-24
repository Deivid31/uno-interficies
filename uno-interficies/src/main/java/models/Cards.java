package models;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.Serializable;
import javax.swing.*;
import models.enums.Color;
import models.enums.Type;

public class Cards extends JPanel implements Serializable {

    private final Color color;
    private final Type power;
    private final int number;

    private String imagePath = "src\\main\\java\\img\\reverso\\carta_detras.png";
    private InterfaceEventDraw interfaceEventDraw;
    private Point clickOffset;
    private boolean isDetached = false;

    public Cards() {
        this(Color.RED, Type.NUM, 0); // Default values
    }

    public Cards(Color color, Type power, int number) {
        this.color = color;
        this.power = power;
        this.number = number;
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickOffset = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (interfaceEventDraw != null) {
                    interfaceEventDraw.cardTurn(); // Will handle image + spawning
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Container topLevel = getTopLevelAncestor();
                if (!(topLevel instanceof JFrame)) {
                    return;
                }
                JFrame frame = (JFrame) topLevel;

                // Saca la carta actual del mazo
                if (!isDetached && getParent() != null) {
                    Container parent = getParent();
                    parent.remove(Cards.this);
                    frame.getContentPane().add(Cards.this);
                    frame.getContentPane().setComponentZOrder(Cards.this, 0);
                    Point globalPos = SwingUtilities.convertPoint(parent, getLocation(), frame.getContentPane());
                    setLocation(globalPos);
                    setSize(50, 75);
                    isDetached = true;

                    if (interfaceEventDraw != null) {
                        interfaceEventDraw.cardDrag();
                    }
                }

                // Para mover la carta
                Point mousePos = SwingUtilities.convertPoint(Cards.this, e.getPoint(), frame.getContentPane());
                setLocation(mousePos.x - clickOffset.x, mousePos.y - clickOffset.y);
                frame.repaint();
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

    public Color getColor() {
        return color;
    }

    public Type getPower() {
        return power;
    }

    public int getNum() {
        return number;
    }

    public void addInterfaceEventDraw(InterfaceEventDraw ied) {
        this.interfaceEventDraw = ied;
    }
}
