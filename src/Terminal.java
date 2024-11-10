import org.graphstream.graph.Edge;
import java.util.List;

/**
 * @class Terminal
 * @brief Representa un node terminal d'una xarxa de distribució d'aigua
 *
 * Pau Domenech Villahermosa
 */

/**
 * @file Terminal.java
 * @brief Representa un node terminal d'una xarxa de distribució d'aigua
 *
 * Pau Domenech Villahermosa
 */
public class Terminal extends NodeClass {
    //Descripció general: Node terminal d'una xarxa de distribució d'aigua
    private NodeClass node;

    /**
     * @brief Constructor
     * @pre demandaPunta >= 0
     * @post Crea un nou terminal amb identificador id, coordenades c i demanda punta demanda en l/s
     * @param id Identificador del node terminal
     * @param c Coordenades del node terminal
     * @param demandaPunta Demanda punta del node terminal
     */
    public Terminal(String id, Coordenades c, float demandaPunta) {
        super(id, c);
        //Pre:  demandaPunta >= 0
        //Post: S'ha creat un nou terminal amb identificador id, coordenades c i demanda punta demanda en l/s

        node = new NodeClass(id, c);
        node.establirCabalPotencial(demandaPunta);
        node.establirDemanda(0);

    }

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea un nou terminal a partir d'un node existent
     * @param node Node existent
     */
    public Terminal(NodeClass node) {
        super(node.getId(), node.getCoordenades());
        this.node = node;
    }

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea un nou terminal amb identificador id i coordenades c
     */
    public Terminal() {
        this("0", new Coordenades(0, 0), 0);

        //Pre:  cert
        //Post: S'ha creat un nou terminal amb identificador id i coordenades c

        node.establirCabalPotencial(0);
        node.establirDemanda(0);
    }

    /**
     * @brief Retorna l'identificador del node terminal
     * @pre ---
     * @post Retorna l'identificador del node terminal
     * @return Identificador del node terminal
     */
    public String getID() {
        return node.getId();
    }

    /**
     * @brief Obre l'aixeta del node terminal
     * @pre ---
     * @post L'aixeta del node terminal està oberta
     */
    public void obrirAixeta() {
        node.obrirAixeta();
    }

    /**
     * @brief Comprova si l'aixeta del node terminal està oberta
     * @pre ---
     * @post Retorna true si l'aixeta del node terminal està oberta, false en cas contrari
     * @return true si l'aixeta del node terminal està oberta, false en cas contrari
     */
    public boolean aixetaOberta() {
        return node.aixetaOberta();
    }

    /**
     * @brief Afegeix una canonada al node terminal
     * @pre ---
     * @post La canonada e està connectada al node terminal
     * @param e Canonada a connectar al node terminal
     */
    public void afegirEdge(Canonada e) {
        node.afegirEdge(e);
    }

    /**
     * @brief Estableix el cabal potencial del node terminal
     * @pre cabalPotencial >= 0
     * @post El cabal potencial del node terminal és cabalPotencial
     * @exception IllegalArgumentException si cabalPotencial < 0
     * @param cabalPotencial Cabal potencial
     */
    public void estableixCabalPotencial(float cabalPotencial) {
        node.establirCabalPotencial(cabalPotencial);
    }

    /**
     * @brief Retorna el cabal potencial del node terminal
     * @pre ---
     * @post Retorna el cabal potencial del node terminal
     * @return Cabal potencial del node terminal
     */
    public float getCabalPotencial() {
        return node.getCabalPotencial();
    }

    /**
     * @brief Estableix la demanda d'aigua del node terminal
     * @pre demanda >= 0
     * @post La demanda d'aigua del node terminal és demanda
     * @exception IllegalArgumentException si demanda < 0
     * @param demanda Demanda d'aigua
     */
    public void establirDemanda(float demanda) {
        node.establirDemanda(demanda);
    }

    /**
     * @brief Retorna la demanda d'aigua del node terminal
     * @pre ---
     * @post Retorna la demanda d'aigua del node terminal
     * @return Demanda d'aigua del node terminal
     */
    public float getDemanda() {
        return node.getDemanda();
    }

    /**
     * @brief Retorna les canonades connectades al node terminal
     * @pre ---
     * @post Retorna una llista de les canonades connectades al node terminal
     * @return Llista de canonades connectades al node terminal
     */
    public List<Canonada> getEdges() {
        return node.getEdges();
    }

    /**
     * @brief Tanca l'aixeta del node terminal
     * @pre ---
     * @post L'aixeta del node terminal està tancada
     */
    public void tancarAixeta() {
        node.tancarAixeta();
    }

    /**
     * @brief Retorna el node terminal
     * @pre ---
     * @post Retorna el node terminal
     * @return Node terminal
     */
    public NodeClass getNode() {
        return node;
    }

    /**
     * @brief Estableix el node terminal
     * @pre ---
     * @post El node terminal és node
     * @param node Node terminal
     */
    public void setNode(NodeClass node) {
        this.node = node;
    }

}
