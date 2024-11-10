import java.util.List;

/**
 * @file Connexio.java
 * @brief Node de connexió d'una xarxa de distribució d'aigua
 *
 * Pau Domenech Villahermosa
 */

/**
 * @class Connexio
 * @brief Node de connexió d'una xarxa de distribució d'aigua
 */
public class Connexio extends NodeClass {
    //Descripció general: Node de connexió d'una xarxa de distribució d'aigua

    private NodeClass node;

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea un nou node de connexió amb identificador id i coordenades c
     * @param id Identificador del node de connexió
     * @param c Coordenades del node de connexió
     */
    public Connexio(String id, Coordenades c){
        super(id, c);
        //Pre:  ---
        //Post: S'ha creat un nou node de connexió amb identificador id i coordenades c

        this.node = new NodeClass(id, c);

    }

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea un nou node de connexió a partir d'un node existent
     * @param node Node existent
     */
    public Connexio(NodeClass node) {
        super(node.getId(), node.getCoordenades());
        this.node = node;
    }

    /**
     * @brief Retorna la demanda d'aigua del node de connexió
     * @pre ---
     * @post Retorna la demanda d'aigua del node de connexió
     * @return Demanda d'aigua del node de connexió
     */
    public float getDemanda()
    {
        //Pre: ---
        //Post: Retorna la demanda d'aigua del node de connexió

        return node.getDemanda();
    }

    /**
     * @brief Estableix la demanda d'aigua del node de connexió
     * @pre demanda >= 0
     * @post La demanda d'aigua del node de connexió és demanda
     * @exception IllegalArgumentException si demanda < 0
     * @param demanda Demanda d'aigua
     */
    public void establirDemanda(float demanda)
    {
        //Pre: demanda >= 0
        // Post: La demanda d'aigua del node de connexió és demanda
        //Excepcions: IllegalArgumentException si demanda < 0

        node.establirDemanda(demanda);
    }

    /**
     * @brief Retorna l'identificador del node de connexió
     * @pre ---
     * @post Retorna l'identificador del node de connexió
     * @return Identificador del node de connexió
     */
    public String getID() {
        return node.getId();
    }

    /**
     * @brief Obre l'aixeta del node de connexió
     * @pre ---
     * @post L'aixeta del node de connexió està oberta
     */
    public void obrirAixeta() {
        node.obrirAixeta();
    }

    /**
     * @brief Comprova si l'aixeta del node de connexió està oberta
     * @pre ---
     * @post Retorna true si l'aixeta del node de connexió està oberta, false en cas contrari
     * @return true si l'aixeta del node de connexió està oberta, false en cas contrari
     */
    public boolean aixetaOberta() {
        return node.aixetaOberta();
    }

    /**
     * @brief Tanca l'aixeta del node de connexió
     * @pre ---
     * @post L'aixeta del node de connexió està tancada
     */
    public void tancarAixeta() {
        node.tancarAixeta();
    }

    /**
     * @brief Retorna les canonades connectades al node de connexió
     * @pre ---
     * @post Retorna una llista de les canonades connectades al node de connexió
     * @return Llista de canonades connectades al node de connexió
     */
    public List<Canonada> getEdges() {
        return node.getEdges();
    }

    /**
     * @brief Retorna les coordenades del node de connexió
     * @pre ---
     * @post Retorna les coordenades del node de connexió
     * @return Coordenades del node de connexió
     */
    public Coordenades getCoordenades() {
        return node.getCoordenades();
    }

    /**
     * @brief Retorna el node de connexió
     * @pre ---
     * @post Retorna el node de connexió
     * @return Node de connexió
     */
    public NodeClass getNode() {
        return node;
    }

    /**
     * @brief Estableix el cabal potencial del node de connexió
     * @pre demandaPropagada >= 0
     * @post El cabal potencial del node de connexió és demandaPropagada
     * @param demandaPropagada Cabal potencial
     */
    public void establirCabalPotencial(float demandaPropagada) {
        node.establirCabalPotencial(demandaPropagada);
    }

    /**
     * @brief Retorna el cabal potencial del node de connexió
     * @pre ---
     * @post Retorna el cabal potencial del node de connexió
     * @return Cabal potencial del node de connexió
     */
    public float getCabalPotencial() {
        return node.getCabalPotencial();
    }



}
