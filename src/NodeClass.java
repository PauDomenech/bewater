import java.util.ArrayList;
import java.util.List;

/**
 * @file NodeClass.java
 * @brief Representa un node d'una xarxa de distribució d'aigua
 *
 * Pau Domenech Villahermosa
 */
public class NodeClass {
    //Descripció general: Node d'una xarxa de distribució d'aigua
    private final String id;
    private final Coordenades c;
    private boolean aixetaOberta;
    private List<Canonada> edges;
    private float demanda;
    private float cabalPotencial;

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea un nou node amb identificador id i coordenades c
     * @param id Identificador del node
     * @param c Coordenades del node
     */
    public NodeClass(String id, Coordenades c){
        //Pre:  ---
        //Post: S'ha creat un nou node amb identificador id i coordenades c

        this.id = id;
        this.c = c;
        this.edges = new ArrayList<>();
        this.aixetaOberta = true;
        this.demanda = 0;
        this.cabalPotencial = 0;
    }

    /**
     * @brief Retorna l'identificador del node
     * @pre ---
     * @post Retorna l'identificador del node
     * @return Identificador del node
     */
    public String getId()
    {
        //Pre:  ---
        //Post: Retorna l'identificador del node

        return id;
    }

    /**
     * @brief Afegeix una canonada al node
     * @pre ---
     * @post La canonada e està connectada al node
     * @param e Canonada a connectar al node
     */
    public void afegirEdge(Canonada e) {
        edges.add(e);
    }

    /**
     * @brief Retorna les canonades connectades al node
     * @pre ---
     * @post Retorna una llista de les canonades connectades al node
     * @return Llista de canonades connectades al node
     */
    public List<Canonada> getEdges() {
        return edges;
    }

    /**
     * @brief Comprova si l'aixeta del node està oberta
     * @pre ---
     * @post Retorna true si l'aixeta del node està oberta, false en cas contrari
     * @return true si l'aixeta del node està oberta, false en cas contrari
     */
    public boolean aixetaOberta()
    {
        //Pre:  ---
        //Post: Diu si l'aixeta del node està oberta

        return aixetaOberta;
    }

    /**
     * @brief Obre l'aixeta del node
     * @pre ---
     * @post L'aixeta del node està oberta
     */
    public void obrirAixeta()
    {
        //Pre:  ---
        //Post: L'aixeta del node està oberta

        aixetaOberta = true;
    }

    /**
     * @brief Tanca l'aixeta del node
     * @pre ---
     * @post L'aixeta del node està tancada
     */
    public void tancarAixeta()
    {
        //Pre:  ---
        //Post: L'aixeta del node està tancada

        aixetaOberta = false;
    }

    /**
     * @brief Retorna l'id del node
     * @pre ---
     * @post Retorna l'id del node
     * @return String id del node
     */
    public String getID() {
        return id;
    }

    /**
     * @brief Retorna les coordenades del node
     * @pre ---
     * @post Retorna les coordenades del node
     * @return Coordenades del node
     */
    public Coordenades getCoordenades() {
        return c;
    }

    /**
     * @brief Retorna la demanda d'aigua del node
     * @pre ---
     * @post Retorna la demanda d'aigua del node
     * @return Demanda d'aigua del node
     */
    public float getDemanda() {
        return demanda;
    }

    /**
     * @brief Retorna el cabal potencial del node
     * @pre ---
     * @post Retorna el cabal potencial del node
     * @return Cabal potencial del node
     */
    public float getCabalPotencial() {
        return cabalPotencial;
    }

    /**
     * @brief Estableix la demanda d'aigua del node
     * @pre demandaPunta >= 0
     * @post La demanda d'aigua del node és demandaPunta
     * @exception IllegalArgumentException si demandaPunta < 0
     * @param demandaPunta Demanda d'aigua
     */
    public void establirDemanda(float demandaPunta)
    {
        //Pre: cabal >= 0
        // Post: El cabal d'aigua que surt de l'origen és cabal
        //Excepcions: IllegalArgumentException si cabal < 0

        this.demanda = demandaPunta;
    }

    /**
     * @brief Estableix el cabal potencial del node
     * @pre cabalPotencial >= 0
     * @post El cabal potencial del node és cabalPotencial
     * @exception IllegalArgumentException si cabalPotencial < 0
     * @param cabalPotencial Cabal potencial
     */
    public void establirCabalPotencial(float cabalPotencial) {
        this.cabalPotencial = cabalPotencial;
    }
}
