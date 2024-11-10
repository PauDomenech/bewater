/**
 * @file Coordenades.java
 * @brief Representa les coordenades geogràfiques d'un punt
 *
 * Pau Domenech Villahermosa
 */

/**
 * @class Coordenades
 * @brief Coordenades geogràfiques (latitud, longitud)
 */
public class Coordenades {
    //Descripció general: Coordenades geogràfiques (latitud, longitud)

    private float latitud = 0;
    private float longitud = 0;

    /**
     * @brief Constructor
     * @pre input is a valid coordinate string
     * @post Crea unes coordenades amb els valors indicats
     * @param input String representation of coordinates
     */
    public Coordenades(String input) {
        String[] parts = input.split(",");
        String[] latParts = parts[0].split(":");
        String[] lonParts = parts[1].split(":");

        float grausLatitud = Integer.parseInt(latParts[0]);
        float minutsLatitud = Integer.parseInt(latParts[1]);
        float segonsLatitud = Float.parseFloat(latParts[2].substring(0, latParts[2].length() - 1));
        char direccioLatitud = latParts[2].charAt(latParts[2].length() - 1);

        float grausLongitud = Integer.parseInt(lonParts[0]);
        float minutsLongitud = Integer.parseInt(lonParts[1]);
        float segonsLongitud = Float.parseFloat(lonParts[2].substring(0, lonParts[2].length() - 1));
        char direccioLongitud = lonParts[2].charAt(lonParts[2].length() - 1);

        float latitud = grausLatitud + (minutsLatitud / 60) + (segonsLatitud / 3600);
        if (direccioLatitud == 'S') {
            latitud = -latitud;
        }

        float longitud = grausLongitud + (minutsLongitud / 60) + (segonsLongitud / 3600);
        if (direccioLongitud == 'W') {
            longitud = -longitud;
        }

        this.latitud = latitud;
        this.longitud = longitud;

    }
    //Pre: 0 <= grausLatitud <= 60, 0 <= minutsLatitud <= 60, 0 <= segonsLatitud <= 60, direccioLatitud = 'N' o 'S', 0 <= grausLongitud <= 60, 0 <= minutsLongitud <= 60, 0 <= segonsLongitud <= 60, direccioLatitud = 'E' o 'W'
    //Post: Crea unes coordenades amb els valors indicats
    //Excepcions: IllegalArgumentException si es viola la precondició

    /**
     * @brief Constructor
     * @pre -90 <= latitud <= 90, -180 <= longitud <= 180
     * @post Crea unes coordenades amb els valors indicats
     * @exception IllegalArgumentException si es viola la precondició
     * @param latitud Latitude value
     * @param longitud Longitude value
     */
    public Coordenades(float latitud, float longitud) {
        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("Les coordenades no són vàlides");
        }
        this.latitud = latitud;
        this.longitud = longitud;
    }
    //Pre: -90 <= latitud <= 90, -180 <= longitud <= 180
    //Post: Crea unes coordenades amb els valors indicats
    //Excepcions: IllegalArgumentException si es viola la precondició

    /**
     * @brief Retorna la distància entre aquestes coordenades i c, expressada en km
     * @pre ---
     * @post Retorna la distància entre aquestes coordenades i c
     * @param c Other coordinates
     * @return Distance between this and c
     */
    public double distancia(Coordenades c) {
        //Pre: ---
        //Post: Retorna la distància entre aquestes coordenades i c, expressada en km

            float dx = this.longitud - c.getX();
            float dy = this.latitud - c.getY();
            return Math.sqrt(dx * dx + dy * dy);
    }


    /**
     * @brief Retorna la longitud
     * @pre ---
     * @post Retorna la longitud
     * @return Longitude value
     */
    public float getX() {
        return longitud;
    }

    /**
     * @brief Retorna la latitud
     * @pre ---
     * @post Retorna la latitud
     * @return Latitude value
     */
    public float getY() {
        return latitud;
    }
}
