package Mapa;

import GameGFX.*;
import Objetos.Bloque;
import Objetos.Player;
import Objetos.Utilidad.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class NivelesHandler {
    private final String FOLDER = "/Imagenes/";
    
    private CargadorImagenes cargador;
    private BufferedImage mapaNivel;
    private Handler handler;
    
    // Constantes del juego
    public static final int TAMANO_TILE = 8; // Donkey Kong usa tiles de 8x8
    public static final int ESCALA_VISUAL = 4; // Escalar 4x para mejor visualización
    
    // Mapa de colores RGB para diferentes elementos del juego
    private final Map<Integer, ElementoTipo> mapaColores;
    
    // Enum para tipos de elementos
    public enum ElementoTipo {
        PLATAFORMA_ROJA,
        PLATAFORMA_AZUL, 
        ESCALERA,
        ESCALERA_ROTA,
        JUGADOR_INICIO,
        MARIO_INICIO,
        DONKEY_KONG_POSICION,
        BARRIL_SPAWN,
        PRINCESA_POSICION,
        VACIO
    }
    
    public NivelesHandler(Handler handler) {
        this.handler = handler;
        this.cargador = new CargadorImagenes();
        
        // Inicializar mapa de colores
        this.mapaColores = inicializarMapaColores();
        
        // Cargar y procesar el mapa
        cargarYProcesarMapa();
    }
    
    /**
     * Inicializa el mapa de colores RGB a tipos de elementos
     */
    private Map<Integer, ElementoTipo> inicializarMapaColores() {
        Map<Integer, ElementoTipo> mapa = new HashMap<>();
        
        // Colores basados en la imagen de Donkey Kong
        mapa.put(crearRGB(255, 0, 0), ElementoTipo.PLATAFORMA_ROJA);      // Rojo puro - plataformas rojas
        mapa.put(crearRGB(255, 64, 64), ElementoTipo.PLATAFORMA_ROJA);    // Rojo claro - variación plataformas
        mapa.put(crearRGB(0, 0, 255), ElementoTipo.PLATAFORMA_AZUL);      // Azul - plataformas azules
        mapa.put(crearRGB(64, 64, 255), ElementoTipo.PLATAFORMA_AZUL);    // Azul claro - variación plataformas
        
        mapa.put(crearRGB(0, 255, 255), ElementoTipo.ESCALERA);           // Cyan - escaleras normales
        mapa.put(crearRGB(0, 200, 200), ElementoTipo.ESCALERA);           // Cyan oscuro - escaleras
        mapa.put(crearRGB(255, 255, 0), ElementoTipo.ESCALERA_ROTA);      // Amarillo - escaleras rotas
        
        mapa.put(crearRGB(0, 255, 0), ElementoTipo.MARIO_INICIO);         // Verde - posición inicial de Mario
        mapa.put(crearRGB(255, 0, 255), ElementoTipo.DONKEY_KONG_POSICION); // Magenta - posición de DK
        mapa.put(crearRGB(255, 192, 203), ElementoTipo.PRINCESA_POSICION); // Rosa - posición de la princesa
        mapa.put(crearRGB(255, 165, 0), ElementoTipo.BARRIL_SPAWN);       // Naranja - spawn de barriles
        
        return mapa;
    }
    
    /**
     * Crea un valor RGB entero a partir de componentes R, G, B
     */
    private int crearRGB(int r, int g, int b) { 
        return (r << 16) | (g << 8) | b;
    }
    
    /**
     * Carga la imagen del mapa y la procesa
     */
    private void cargarYProcesarMapa() {
        try {
            mapaNivel = cargador.loadImage(FOLDER + "1_.png");
            
            if (mapaNivel == null) {
                System.err.println("Error: No se pudo cargar la imagen del mapa");
                crearMapaPorDefecto();
                return;
            }
            
            System.out.println("Cargando mapa Donkey Kong: " + mapaNivel.getWidth() + "x" + mapaNivel.getHeight() + " píxeles");
            procesarImagenMapa(mapaNivel);
            
        } catch (Exception e) {
            System.err.println("Error cargando el mapa: " + e.getMessage());
            e.printStackTrace();
            crearMapaPorDefecto();
        }
    }
    
    /**
     * Procesa la imagen del mapa píxel por píxel y crea los objetos del juego
     */
    private void procesarImagenMapa(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        
        int elementosCreados = 0;
        
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixelRGB = imagen.getRGB(x, y);
                
                // Extraer componentes RGB
                int rojo = (pixelRGB >> 16) & 0xff;
                int verde = (pixelRGB >> 8) & 0xff;
                int azul = pixelRGB & 0xff;
                
                // Buscar coincidencia exacta primero
                ElementoTipo tipo = mapaColores.get(crearRGB(rojo, verde, azul));
                
                // Si no hay coincidencia exacta, buscar por similitud
                if (tipo == null) {
                    tipo = buscarColorSimilar(rojo, verde, azul);
                }
                
                // Crear elemento según el tipo identificado
                if (tipo != null && tipo != ElementoTipo.VACIO) {
                    crearElemento(tipo, x, y);
                    elementosCreados++;
                }
            }
        }
        
        System.out.println("Mapa procesado exitosamente. Elementos creados: " + elementosCreados);
        
        // Verificar que el jugador fue posicionado
        if (handler.getPlayer() == null) {
            System.out.println("Advertencia: No se encontró posición inicial para Mario. Creando posición por defecto.");
            crearMarioEnPosicionDefecto();
        }
    }
    
    /**
     * Busca un color similar en el mapa de colores usando tolerancia
     */
    private ElementoTipo buscarColorSimilar(int r, int g, int b) {
        final int TOLERANCIA = 30; // Tolerancia para diferencias de color
        
        for (Map.Entry<Integer, ElementoTipo> entrada : mapaColores.entrySet()) {
            int colorMapa = entrada.getKey();
            int rMapa = (colorMapa >> 16) & 0xff;
            int gMapa = (colorMapa >> 8) & 0xff;
            int bMapa = colorMapa & 0xff;
            
            // Calcular diferencia euclidiana
            double diferencia = Math.sqrt(
                Math.pow(r - rMapa, 2) + 
                Math.pow(g - gMapa, 2) + 
                Math.pow(b - bMapa, 2)
            );
            
            if (diferencia <= TOLERANCIA) {
                return entrada.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * Crea un elemento del juego según su tipo y posición
     */
    private void crearElemento(ElementoTipo tipo, int x, int y) {
        int posX = x * TAMANO_TILE * ESCALA_VISUAL;
        int posY = y * TAMANO_TILE * ESCALA_VISUAL;
        int tamano = TAMANO_TILE * ESCALA_VISUAL;
        
        switch (tipo) {
            case PLATAFORMA_ROJA:
            case PLATAFORMA_AZUL:
                // Crear bloque de plataforma
                handler.addObj(new Bloque(posX, posY, tamano, tamano, 1));
                break;
                
            case ESCALERA:
                // TODO: Crear escalera normal cuando implementes la clase
                // handler.addObj(new Escalera(posX, posY, tamano, tamano, false));
                System.out.println("Escalera detectada en (" + x + ", " + y + ") - Pendiente de implementar");
                break;
                
            case ESCALERA_ROTA:
                // TODO: Crear escalera rota cuando implementes la clase
                // handler.addObj(new Escalera(posX, posY, tamano, tamano, true));
                System.out.println("Escalera rota detectada en (" + x + ", " + y + ") - Pendiente de implementar");
                break;
                
            case MARIO_INICIO:
                // Posicionar a Mario
                if (handler.getPlayer() != null) {
                    handler.getPlayer().setX(posX);
                    handler.getPlayer().setY(posY - tamano); // Ajustar para que esté sobre la plataforma
                    System.out.println("Mario posicionado en (" + posX + ", " + (posY - tamano) + ")");
                } else {
                    System.out.println("Posición inicial de Mario encontrada, pero el jugador no ha sido creado aún");
                }
                break;
                
            case DONKEY_KONG_POSICION:
                // TODO: Crear y posicionar Donkey Kong
                System.out.println("Posición de Donkey Kong detectada en (" + x + ", " + y + ") - Pendiente de implementar");
                break;
                
            case PRINCESA_POSICION:
                // TODO: Crear y posicionar la princesa
                System.out.println("Posición de la princesa detectada en (" + x + ", " + y + ") - Pendiente de implementar");
                break;
                
            case BARRIL_SPAWN:
                // TODO: Marcar punto de spawn de barriles
                System.out.println("Punto de spawn de barriles detectado en (" + x + ", " + y + ") - Pendiente de implementar");
                break;
        }
    }
    
    /**
     * Crea un mapa básico por defecto si no se puede cargar la imagen
     */
    private void crearMapaPorDefecto() {
        System.out.println("Creando mapa por defecto de Donkey Kong...");
        
        int anchoVentana = 960; // Ajusta según tu ventana
        int altoVentana = 720;
        int tamanoBloque = 32;
        
        // Crear plataformas horizontales típicas de Donkey Kong
        // Plataforma inferior
        for (int i = 0; i < anchoVentana / tamanoBloque; i++) {
            handler.addObj(new Bloque(i * tamanoBloque, altoVentana - tamanoBloque * 2, tamanoBloque, tamanoBloque, 1));
        }
        
        // Plataformas inclinadas (simuladas con bloques)
        crearPlataformaInclinada(0,altoVentana - 100, 22, tamanoBloque, -1); // Inclinada hacia abajo
        crearPlataformaInclinada(0, altoVentana - 300, 22, tamanoBloque, 1);  // Inclinada hacia arriba
        crearPlataformaInclinada(0, altoVentana - 400, 22, tamanoBloque, -1); // Inclinada hacia abajo
        
        // Plataforma superior (donde está DK)
        for (int i = 5; i < 15; i++) {
            handler.addObj(new Bloque(i * tamanoBloque, 100, tamanoBloque, tamanoBloque, 1));
        }
    }
    
    /**
     * Crea una plataforma inclinada usando bloques
     */
    private void crearPlataformaInclinada(int inicioX, int inicioY, int longitud, int tamanoBloque, int direccion) {
        for (int i = 0; i < longitud; i++) {
            int x = inicioX + i * tamanoBloque;
            int y = inicioY + (i * direccion * 2); // Inclinación
            handler.addObj(new Bloque(x, y, tamanoBloque, tamanoBloque, 1));
        }
    }
    
    /**
     * Crea Mario en una posición por defecto si no se encontró en el mapa
     */
    private void crearMarioEnPosicionDefecto() {
        if (handler.getPlayer() != null) {
            handler.getPlayer().setX(100); // Posición X por defecto
            handler.getPlayer().setY(600); // Posición Y por defecto (cerca del suelo)
        }
    }
    
    /**
     * Obtiene información sobre el mapa cargado
     */
    public String getInfoMapa() {
        if (mapaNivel != null) {
            return String.format("Mapa: %dx%d píxeles, Escala: %dx, Tile: %dx%d", 
                mapaNivel.getWidth(), mapaNivel.getHeight(), 
                ESCALA_VISUAL, TAMANO_TILE, TAMANO_TILE);
        }
        return "Mapa por defecto activo";
    }
    
    /**
     * Recarga el mapa (útil para testing o cambios dinámicos)
     */
    public void recargarMapa() {
        // Limpiar objetos existentes (excepto el jugador)
        handler.getGameObjs().removeIf(obj -> obj.getId() != ObjetosID.Jugador);
        
        // Recargar el mapa
        cargarYProcesarMapa();
    }
}