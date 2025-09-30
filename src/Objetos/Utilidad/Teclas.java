package Objetos.Utilidad;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author LENOVO
 * 
 */
public class Teclas extends KeyAdapter{
    private boolean[] keyAbajo = new boolean[4];
    private Handler handler;
    
    public Teclas(Handler handler){
        this.handler = handler;
    }
    
    
    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        //salto
        if(key == KeyEvent.VK_SPACE){
            if (!handler.getPlayer().hasSalto()){
                handler.getPlayer().setVely(-15);
                keyAbajo[0] = true;
                handler.getPlayer().setSalto(true);
            }
        }
        //sube escalera
        if(key == KeyEvent.VK_W){
            
        }
        
        // mueve a izquierda
        if(key == KeyEvent.VK_A){
            handler.getPlayer().setVelX(-8);
            keyAbajo[1] = true;
        }
        //mueve a derecha
        if(key == KeyEvent.VK_D){
            handler.getPlayer().setVelX(8);
            keyAbajo[2] = true;
        }
        
    }
    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_SPACE){
            keyAbajo[0]=false;
        }
        
        if(key == KeyEvent.VK_W){
             
        }
        
        if(key == KeyEvent.VK_A){
             keyAbajo[1]=false;
        }
        
        if(key == KeyEvent.VK_D){
             keyAbajo[2]=false;
        }
        
        if(!keyAbajo[1] && !keyAbajo[2]){
            handler.getPlayer().setVelX(0);
        }
    }
}
