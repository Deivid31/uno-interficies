package models;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.Serializable;
import javax.swing.*;
import models.enums.Colors;
import models.enums.Type;

public class Cards extends JPanel implements Serializable {

    private final Colors color;
    private final Type power;
    private final int number;

    private String imagePath = "src\\main\\java\\img\\reverso\\carta_detras.png";
    private InterfaceEventDraw interfaceEventDraw;
    private Point clickOffset;
    private boolean isDetached = false;

    public Cards() {
        this(Colors.RED, Type.NUM, 0); // Default values
    }

    public Cards(Colors color, Type power, int number) {
        this.color = color;
        this.power = power;
        this.number = number;
        setPreferredSize(new Dimension(40, 65));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickOffset = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (interfaceEventDraw != null) {
                    interfaceEventDraw.cardTurn(); // Will handle image + spawning

                    Point screenPoint = e.getLocationOnScreen();
                    //interfaceEventDraw.cardDropped(Cards.this, screenPoint);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (interfaceEventDraw != null) {
                    interfaceEventDraw.cardDrag();
                }

                // Para mover la carta
                setLocation(getX() + e.getX() - clickOffset.x, getY() + e.getY() - clickOffset.y);
            }
        });
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Llamada a super para asegurar que el panel se dibuje correctamente
        setOpaque(false);

        // Ruta de la imagen
        File imgFile = new File(imagePath);

        if (imgFile.exists()) {
            try {
                // Cargar la imagen
                ImageIcon imageIcon = new ImageIcon(imgFile.getAbsolutePath());
                Image img = imageIcon.getImage();

                // Dibujar la imagen respetando su transparencia
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            } catch (Exception e) {
                g.drawString("Error al cargar la imagen", 10, 20);  // Mostrar mensaje de error si algo falla
                e.printStackTrace();
            }
        } else {
            g.drawString("Imagen no encontrada", 10, 20);  // Si no encuentra la imagen
        }
    }

    public Colors getColor() {
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

    public void setIsDetached(boolean isDetached) {
        this.isDetached = isDetached;
    }

    public boolean isDetached() {
        return isDetached;
    }
}
