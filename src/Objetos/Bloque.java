package Objetos;

import Objetos.Utilidad.ObjetosID;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author LENOVO
 */
public class Bloque extends GameObjetos {
    
    public Bloque(int x , int y , int width , int height,int scale){
       super(x, y, ObjetosID.Bloque, width, height, scale);
    }
  
    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawRect((int) getX(),(int)getY() ,(int)getWidth(),(int) getHeight());
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) getX(), (int) getY(), (int) getWidth(),(int) getHeight());
    }
}
