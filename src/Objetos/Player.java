package Objetos;

import GameGFX.*;
import Objetos.Utilidad.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import mariotest.*;

public class Player extends GameObjetos {
    private static final float WIDTH = 16; // Ancho y altura base
    private static final float HEIGHT = 16;
    
    private Handler handler;
    private Texturas textura;
    private BufferedImage[] spriteS;
    private PlayerEstado estado;
    private Animacion playerCaminaS,playerCaminaL;
    private BufferedImage[] currSprite;
    private Animacion currAnimacion;
    private boolean salto = false;
    private boolean adelante= false;
    
    public Player(float x, float y, int scale, Handler handler) {
        super(x, y, ObjetosID.Jugador, WIDTH, HEIGHT, scale);
        this.handler = handler;
        textura = Mariotest.getTextura();
        
        spriteS = textura.getMarioS();
        
        playerCaminaS = new Animacion(5,spriteS[1],spriteS[2],spriteS[3]);
        playerCaminaL = new Animacion(5,spriteS[1],spriteS[2],spriteS[3]);
        
        estado = PlayerEstado.Pequeno;
        currSprite = spriteS;
        currAnimacion = playerCaminaS;    
    }

    @Override
    public void tick() {
        aplicarGravedad();
        setX(getVelX() + getX());
        setY(getVely() + getY());
        colisiones();
        currAnimacion.runAnimacion();
    }
    
    private void colisiones(){
        for(int i = 0; i < handler.getGameObjs().size();i++){
            GameObjetos temp = handler.getGameObjs().get(i);
            
            if(temp.getId() == ObjetosID.Bloque || temp.getId() == ObjetosID.Pipe){
// getbounds devuelve un rectangulo y la clase rectangulo tiene  intersersects y comprueba si los 2 rectangulos se superponen
                if (getBounds().intersects(temp.getBounds())){
                    setY(temp.getY() - getHeight());
                    setVely(0);
                    salto = false;
                }
                
                if(getBoundsTop().intersects(temp.getBounds())){
                    setY(temp.getY() + temp.getHeight());
                    setVely(0);
                }
                if(getBoundsRight().intersects(temp.getBounds())){
                    setX(temp.getX() - getWidth());
                }
                if(getBoundsLeft().intersects(temp.getBounds())){
                    setX(temp.getX() + temp.getWidth());
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (salto) {
        if (adelante) {
            // Mantiene la imagen de salto hacia la derecha
            g.drawImage(currSprite[3], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
        } else {
            // Voltea la imagen de salto hacia la izquierda
            g.drawImage(currSprite[3], (int) (getX() + getWidth()), (int) getY(), (int) -getWidth(), (int) getHeight(), null);
        }
        } else if (getVelX() > 0) {
        // Dibuja la animacion normal hacia la derecha
        currAnimacion.drawAnimacion(g, (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
        adelante = true;
        } else if (getVelX() < 0) {
        currAnimacion.drawAnimacion(g, (int) (getX() + getWidth()), (int) getY(), (int) -getWidth(), (int) getHeight());
        adelante = false;
        } else {
        // Dibuja la imagen de reposo 
        if (adelante) {
             g.drawImage(currSprite[0], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
        } else {
             g.drawImage(currSprite[0], (int) (getX() + getWidth()), (int) getY(), (int) -getWidth(), (int) getHeight(), null);
        }
    }
        //g.drawImage(spriteS[0], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
        //showBounds(g);
    }

    @Override //POSICION DEL RECTANGULO
    public Rectangle getBounds() {
        return new Rectangle((int)(getX() + getWidth() / 2 - getWidth() / 4),
                (int)(getY() + getHeight() / 2),
                (int) getWidth() / 2,
                (int) getHeight() / 2);
    }
    
    public Rectangle getBoundsTop() {
        return new Rectangle((int) (getX() + getWidth() / 2 - getWidth() / 4),
               (int) getY(),
               (int) getWidth() / 2,
               (int) getHeight() / 2);
    }
    
    public Rectangle getBoundsRight() {
       return new Rectangle((int) (getX() + getWidth() - 5),
               (int) getY() + 5,
               5,
               (int) getHeight() - 10);
    }
    
    public Rectangle getBoundsLeft() {
        return new Rectangle((int) getX(),
        (int) (getY() + 5),
         5,
        (int) (getHeight() - 10));
    }
    
    private void showBounds(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        g.setColor(Color.red);
        g2d.draw(getBounds());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsTop());
    }
    
    public boolean hasSalto(){
        return salto;
    }
    
    public void setSalto(boolean hasSalto){
        salto = hasSalto;
    }
}