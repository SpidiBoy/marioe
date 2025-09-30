package mariotest;
import GameGFX.*;
import Mapa.NivelesHandler;
import Objetos.*;
import Objetos.Utilidad.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class Mariotest extends Canvas implements Runnable {
    // JUEGO CONSTANTES
    private static final int MILLIS_PER_SEC = 1000; // MILI SEGUNDOS POR SEGUNDO
    private static final int NANOS_PER_SEC = 1000000000; //
    private static final double NUM_TICKS = 60.0; // FPS 60 ACTUALIZACIONES
    private static final String Nombre = "Diego Kong"; // FPS 60 ACTUALIZACIONES
    private static final int Ventana_Width = 960; // ANCHO px
    private static final int Ventana_Height = 720; // ALTURA px
    
    // JUEGO VARIABLES
    public boolean running;
    // JUEGO COMPONENTES
    private Thread thread;
    private Handler handler;
    private static Texturas textura;
    
    
    public Mariotest(){
        initialize();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Mariotest();
    }
    
    private void initialize(){
        textura = new Texturas();
        
        handler = new Handler();
        this.addKeyListener(new Teclas(handler));
       
        handler.setPlayer(new Player(32,32,2 ,handler));
        /*
        for (int i = 0 ; i < 20 ; i++){
        handler.addObj(new Bloque(i*32,50*8,32,32,1));
        }
        for (int i =0 ; i <30 ; i++){
            handler.addObj(new Bloque(i*32,32*20,32,32,1));
        }
        */
        NivelesHandler nivel = new NivelesHandler(handler); 

        new Ventana(Ventana_Width,Ventana_Height,Nombre,this);
        start();
    }
    
    private synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    
    private synchronized void stop(){
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
        
    }

    @Override
    public void run() {
        long lasTime = System.nanoTime();
        double amountOfTicks = NUM_TICKS;
        double ns = NANOS_PER_SEC / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;
        
        this.requestFocus();
        
        while(running){
            long now = System.nanoTime();
            delta += (now - lasTime) / ns;
            lasTime = now;
            
            while(delta >= 1){
                tick();
                updates++;
                delta--;
            }
            if(running){
                render();
                frames++;
            }
            if(System.currentTimeMillis() - timer > MILLIS_PER_SEC){
                timer += MILLIS_PER_SEC;
                System.out.println("FPS: " + frames + " TPS: " + updates);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }
    
    private void tick(){
        handler.tick();
    }
    
    private void render(){
        BufferStrategy buf = this.getBufferStrategy(); // ES UNA FORMA DE CREAR MULTIPLES FOTOFRAMAS
        if( buf == null){
            this.createBufferStrategy(3);//SE VAN A PASAR 3 IMAGENES
            return;
        }
        // DIBUJAR LOS GRAFICOS
        Graphics g = buf.getDrawGraphics();
        
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Ventana_Width,Ventana_Height);
        
        handler.render(g);
        // limpiar para el siguente fotograma
        g.dispose();
        buf.show();
    }
    
    public static int getVentana_Width(){
        return Ventana_Width;
    }
    
    public static int getVentana_Height(){
        return Ventana_Height;
    }
    
    public static Texturas getTextura(){
        return textura;
    }
}
