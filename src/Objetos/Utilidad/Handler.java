package Objetos.Utilidad;

import Objetos.GameObjetos;
import Objetos.Player;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author LENOVO
 */
public class Handler {
    // creamos una lista enlazada
    private List<GameObjetos> gameobjs;
    private Player player;
    
    public Handler(){
        gameobjs = new LinkedList<GameObjetos>(); // unizializa una lista de objetos de juego
    }
    
    public void tick(){
        for(GameObjetos obj : gameobjs){ // recorre la lista
            obj.tick();
        }
    }
    
    public void render(Graphics g){
        for(GameObjetos obj : gameobjs){
            obj.render(g);
        }
    }
    //PASAR OBJETO
    public void addObj(GameObjetos obj){
        gameobjs.add(obj);
    }
    //ELMINAR OBJETO
    public void removeObj(GameObjetos obj){
        gameobjs.remove(obj);
    }
    
    public List<GameObjetos> getGameObjs(){
        return gameobjs;
    } 
    
    public int setPlayer(Player player){
        if(this.player !=null){
            return -1;
        }
      
        addObj(player);
        this.player = player;
        return 0;
    }
    
    public int removePlayer(){
        if(player == null){
            return -1;
        }
        
        removeObj(player);
        this.player = null;
        return 0;
    }
    
    public Player getPlayer(){
        return player;
    }
}
