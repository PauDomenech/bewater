import org.graphstream.graph.Node;

/**
 * @class Canonada.java
 * @brief Canonada de la xarxa de distribució d'aigua
 *
 * Pau Domenech Villahermosa
 */
public class Canonada {
    //Descripció general: Canonada de la xarxa de distribució d'aigua

    private final NodeClass node1;
    private final NodeClass node2;
    private float capacitat;
    private String id;

    private float demandaPropagada;


    /**
     * @brief Constructor
     * @pre capacitat > 0
     * @post Crea una canonada que connecta node1 i node2 amb la capacitat indicada
     * @param node1 Node d'inici de la canonada
     * @param node2 Node de destí de la canonada
     * @param capacitat Capacitat de la canonada
     */
    public Canonada(NodeClass node1, NodeClass node2, float capacitat){
        //Pre: capacitat > 0
        //Post: Crea una canonada que connecta node1 i node2 amb la capacitat indicada

        id = node1.getId() + node2.getId();
        this.node1 = node1;
        this.node2 = node2;
        this.capacitat = capacitat;
    }

    /**
     * @brief Retorna el node d'inici de la canonada
     * @pre ---
     * @post Retorna el node d'inici de la canonada
     * @return Node d'inici
     */
    public NodeClass node1(){
        //Pre: ---
        //Post: Retorna el node d'inici de la canonada

        return node1;
    }

    /**
     * @brief Retorna el node de destí de la canonada
     * @pre ---
     * @post Retorna el node de destí de la canonada
     * @return Node de destí
     */
    public NodeClass node2(){
        //Pre: ---
        //Post: Retorna el node de destí de la canonada

        return node2;
    }

    /**
     * @brief Retorna la capacitat de la canonada
     * @pre ---
     * @post Retorna la capacitat de la canonada
     * @return Capacitat de la canonada
     */
    public float getCapacitat(){
        //Pre: ---
        //Post: Retorna la capacitat de la canonada

        return capacitat;
    }

    /**
     * @brief Retorna l'identificador de la canonada
     * @pre ---
     * @post Retorna l'identificador de la canonada
     * @return Identificador de la canonada
     */
    public String getId(){
        //Pre: ---
        //Post: Retorna l'identificador de la canonada

        return id;
    }

    /**
     * @brief Estableix la demanda propagada per la canonada
     * @pre demanda >= 0
     * @post Estableix la demanda propagada per la canonada
     * @exception IllegalArgumentException si demanda < 0
     * @param demanda Demanda d'aigua
     */
    public void setDemanda(float demanda){
        //Pre: demanda >= 0
        //Post: Estableix la demanda propagada per la canonada
        //Excepcions: IllegalArgumentException si demanda < 0

        if (demanda < 0) {
            throw new IllegalArgumentException("La demanda d'aigua ha de ser positiva");
        }
        demandaPropagada = demanda;
    }

    /**
     * @brief Retorna la demanda propagada per la canonada
     * @pre ---
     * @post Retorna la demanda propagada per la canonada
     * @return Demanda propagada per la canonada
     */
    public float getDemanda(){
        //Pre: ---
        //Post: Retorna la demanda propagada per la canonada

        return demandaPropagada;
    }

    /**
     * @brief Estableix la capacitat de la canonada
     * @pre capacitat > 0
     * @post Estableix la capacitat de la canonada
     * @exception IllegalArgumentException si capacitat <= 0
     * @param capacitat Capacitat de la canonada
     */
    public void setCapacitat(float capacitat){
        //Pre: capacitat > 0
        //Post: Estableix la capacitat de la canonada
        //Excepcions: IllegalArgumentException si capacitat <= 0

        if (capacitat <= 0) {
            throw new IllegalArgumentException("La capacitat ha de ser positiva");
        }
        this.capacitat = capacitat;
    }


}