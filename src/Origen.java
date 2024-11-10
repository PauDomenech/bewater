import java.util.List;

/**
 * @class Origen
 * @brief Representa un origen d'una xarxa de distribució d'aigua
 *
 * Pau Domenech Villahermosa
 */

/**
 * @file Origen.java
 * @brief Representa un origen d'una xarxa de distribució d'aigua
 *
 */
public class Origen extends NodeClass {
    //Descripció general: Node origen d'una xarxa de distribució d'aigua
    private NodeClass node;

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea un nou origen amb identificador id, coordenades c
     * @param id Identificador del node origen
     * @param c Coordenades del node origen
     */
    public Origen(String id, Coordenades c) {
        super(id, c);
        //Pre: demandaPunta >= 0
        //Post: S'ha creat un nou origen amb identificador id, coordenades c i demanda punta demanda en l/s

        this.node = new NodeClass(id, c);
    }

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea un nou origen a partir d'un node existent
     * @param node Node existent
     */
    public Origen(NodeClass node) {
        super(node.getId(), node.getCoordenades());
        this.node = node;
    }

    /**
     * @brief Retorna l'identificador del node origen
     * @pre ---
     * @post Retorna l'identificador del node origen
     * @return Identificador del node origen
     */
    public String getID() {
        return node.getId();
    }

    /**
     * @brief Obre l'aixeta del node origen
     * @pre ---
     * @post L'aixeta del node origen està oberta
     */
    public void obrirAixeta() {
        node.obrirAixeta();
    }

    /**
     * @brief Comprova si l'aixeta del node origen està oberta
     * @pre ---
     * @post Retorna true si l'aixeta del node origen està oberta, false en cas contrari
     * @return true si l'aixeta del node origen està oberta, false en cas contrari
     */
    public boolean aixetaOberta() {
        return node.aixetaOberta();
    }

    /**
     * @brief Tanca l'aixeta del node origen
     * @pre ---
     * @post L'aixeta del node origen està tancada
     */
    public void tancarAixeta() {
        node.tancarAixeta();
    }

    /**
     * @brief Afegeix una canonada al node origen
     * @pre ---
     * @post La canonada e està connectada al node origen
     * @param e Canonada a connectar al node origen
     */
    public void afegirCanonada(Canonada e) {
        node.afegirEdge(e);
    }

    /**
     * @brief Retorna les canonades connectades al node origen
     * @pre ---
     * @post Retorna una llista de les canonades connectades al node origen
     * @return Llista de canonades connectades al node origen
     */
    public List<Canonada> getEdges() {
        return node.getEdges();
    }

    /**
     * @brief Retorna les coordenades del node origen
     * @pre ---
     * @post Retorna les coordenades del node origen
     * @return Coordenades del node origen
     */
    public Coordenades getCoordenades() {
        return node.getCoordenades();
    }

    /**
     * @brief Retorna l'identificador del node origen
     * @pre ---
     * @post Retorna l'identificador del node origen
     * @return Identificador del node origen
     */
    public String getId() {
        return node.getId();
    }

    /**
     * @brief Retorna el node origen
     * @pre ---
     * @post Retorna el node origen
     * @return Node origen
     */
    public NodeClass getNode() {
        return node;
    }

    /**
     * @brief Estableix la demanda d'aigua del node origen
     * @pre demandaPunta >= 0
     * @post La demanda d'aigua del node origen és demandaPunta
     * @exception IllegalArgumentException si demandaPunta < 0
     * @param demandaPunta Demanda d'aigua
     */
    public void setDemandaPunta(float demandaPunta) {
        node.establirDemanda(demandaPunta);
    }

    /**
     * @brief Estableix la demanda propagada per l'origen
     * @pre demandaPropagada >= 0
     * @post La demanda propagada per l'origen és demandaPropagada
     * @exception IllegalArgumentException si demandaPropagada < 0
     * @param demandaPropagada Demanda propagada
     */
    public void setDemandaPropagada(float demandaPropagada) {
        node.establirCabalPotencial(demandaPropagada);
    }

    /**
     * @brief Retorna la demanda d'aigua del node origen
     * @pre ---
     * @post Retorna la demanda d'aigua del node origen
     * @return Demanda d'aigua del node origen
     */
    public float getDemanda() {
        return node.getDemanda();
    }

    /**
     * @brief Retorna el cabal potencial del node origen
     * @pre ---
     * @post Retorna el cabal potencial del node origen
     * @return Cabal potencial del node origen
     */
    public float getCabalPotencial() {
        return node.getCabalPotencial();
    }

}
