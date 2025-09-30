package GameGFX;
import java.awt.image.BufferedImage;
/**
 *
 * @author LENOVO
 */
public class Texturas {
    private final String folder = "/Imagenes";
    
    private final int mario_L_count = 21;
    private final int mario_S_count = 14; //mario grande
    
    private final int Tile_1_count = 28; // mario pequeño
    private final int Tile_2_count = 33; // 
    
    private CargadorImagenes cargar;
    // HOJA DE PERSONAJES O OBJETOS DE LAS IMAGENES
    private BufferedImage player_sheet , enemy_sheet_, bloque_sheet ;
    
    private BufferedImage[] mario_l , mario_s , tile1,tile2,tile3,tile4;
    
    public Texturas(){
        mario_l = new BufferedImage[mario_L_count];
        mario_s = new BufferedImage[mario_S_count];
        tile1 = new BufferedImage[Tile_1_count + Tile_2_count];
        tile2 = new BufferedImage[Tile_1_count + Tile_2_count];
        tile3 = new BufferedImage[Tile_1_count + Tile_2_count];
        tile4 = new BufferedImage[Tile_1_count + Tile_2_count];    
        cargar = new CargadorImagenes();
        
        try{
            player_sheet = cargar.loadImage(folder + "/testt.png");
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        getPlayerTexturas();
    }
    private void getPlayerTexturas(){
        int x_off = 1;
        int y_off = 1;
        int width = 16;
        int height = 16;
        // ver esto cuando anima
        for (int i = 0 ; i < mario_S_count; i++){
            mario_s[i] = player_sheet.getSubimage(x_off + i*(width+2), y_off, width, height);
        }
    }
    
    private void getBloquesTexturas(){
        
    }
    
    public BufferedImage[] getMarioL(){
        return mario_l;
    }
    
    public BufferedImage[] getMarioS(){
        return mario_s;
    }
      
    public BufferedImage[] getTile1(){
        return tile1;
    }
    
    public BufferedImage[] getTile2(){
        return tile2;
    }
    
    public BufferedImage[] getTile3(){
        return tile3;
    }
    
    public BufferedImage[] getTile4(){
        return tile4;
    }
}
