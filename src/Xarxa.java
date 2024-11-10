/**
 * @class Xarxa
 * @details Pot portar claus inicialment, però al laberint només agafa aliments.
 *
 * Pau Domenech Villahermosa
 */

import java.util.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;
import org.graphstream.graph.Edge;

/**
 * @file Xarxa.java
 * @brief Gestiona una xarxa de distribució d'aigua
 *
 * Pau Domenech Villahermosa
 */

public class Xarxa {

    private Map<String, List<Terminal>> abonats;
    private List<Canonada> canonades;
    private Map<String, NodeClass> nodes;
    private Graph g;
    private Stack<ArrayList<Object>> pilaOperacions = new Stack<>();

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea una xarxa de distribució d'aigua buida
     */
    public Xarxa() {
        //Pre: ---
        //Post: Crea una xarxa de distribució d'aigua buida
        System.setProperty("org.graphstream.ui", "swing");
        g = new SingleGraph("Xarxa");
        abonats = new HashMap<>();
        canonades = new Vector<>();
        nodes = new HashMap<>();
    }

    /**
     * @brief Retorna el node de la xarxa amb identificador id
     * @pre ---
     * @post Retorna el node de la xarxa amb identificador id
     * @param id Identificador del node
     * @return Node de la xarxa amb identificador id
     */
    public Node node(String id) {
        //Pre: ---
        //Post: Retorna el node de la xarxa amb identificador id
        return g.getNode(id);
    }

    /**
     * @brief Retorna el terminal de la xarxa amb identificador id
     * @pre ---
     * @post Retorna el terminal de la xarxa amb identificador id
     * @param id Identificador del terminal
     * @return Terminal de la xarxa amb identificador id
     */
    public Terminal terminal(String id) {
        //Pre: ---
        //Post: Retorna el terminal de la xarxa amb identificador id
        return g.getNode(id).getAttribute("node", Terminal.class);
    }

    /**
     * @brief Retorna un iterador que permet recórrer totes les canonades que surten del node
     * @pre node pertany a la xarxa
     * @post Retorna un iterador que permet recórrer totes les canonades que surten del node
     * @param node Node de la xarxa
     * @return Iterador de canonades que surten del node
     */
    public String sortides(Node node) {
        //Pre: node pertany a la xarxa
        //Post: Retorna un iterador que permet recórrer totes les canonades que surten del node
        return node.getId().intern();
    }

    /**
     * @brief Retorna un iterador que permet recórrer totes les canonades que entren al node
     * @pre node pertany a la xarxa
     * @post Retorna un iterador que permet recórrer totes les canonades que entren al node
     * @param node Node de la xarxa
     * @return Iterador de canonades que entren al node
     */
    public int entrades(Node node) {
        //Pre: node pertany a la xarxa
        //Post: Retorna un iterador que permet recórrer totes les canonades que entren al node
        return node.getIndex();
    }

    /**
     * @brief Afegeix un node origen a la xarxa
     * @pre ---
     * @post El node origen ha estat afegit a la xarxa
     * @param nodeOrigen Node origen a afegir
     */
    public void afegir(Origen nodeOrigen) {
        Coordenades c = nodeOrigen.getCoordenades();
        Node node = g.addNode(nodeOrigen.getID());
        node.setAttribute("ui.label", nodeOrigen.getID());
        node.setAttribute("xy", c.getX(), c.getY());
        node.setAttribute("origen", nodeOrigen.getNode());
        node.setAttribute("ui.style", "fill-color: red; size: 25px; text-size: 15;");
        nodes.put(nodeOrigen.getID(), nodeOrigen.getNode());
        establirDemandaICabalConnexions();
    }

    /**
     * @brief Afegeix un node terminal a la xarxa
     * @pre ---
     * @post El node terminal ha estat afegit a la xarxa
     * @param nodeTerminal Node terminal a afegir
     */
    public void afegir(Terminal nodeTerminal) {
        Coordenades c = nodeTerminal.getCoordenades();
        Node node = g.addNode(nodeTerminal.getID());
        node.setAttribute("ui.label", nodeTerminal.getID());
        node.setAttribute("xy", c.getX(), c.getY());
        node.setAttribute("node", nodeTerminal);
        node.setAttribute("ui.style", "fill-color: green; size: 25px; text-size: 15;");
        nodes.put(nodeTerminal.getID(), nodeTerminal.getNode());
    }

    /**
     * @brief Afegeix un node connexió a la xarxa
     * @pre ---
     * @post El node connexió ha estat afegit a la xarxa
     * @param nodeConnexio Node connexió a afegir
     */
    public void afegir(Connexio nodeConnexio) {
        Coordenades c = nodeConnexio.getCoordenades();
        Node node = g.addNode(nodeConnexio.getID());
        node.setAttribute("ui.label", nodeConnexio.getID());
        node.setAttribute("xy", c.getX(), c.getY());
        node.setAttribute("ui.style", "fill-color: blue; size: 25px; text-size: 15;");
        node.setAttribute("node", nodeConnexio);
        nodes.put(nodeConnexio.getID(), nodeConnexio.getNode());
        establirDemandaICabalConnexions();

    }

    /**
     * @brief Connecta dos nodes amb una canonada
     * @pre node1 i node2 pertanyen a la xarxa, no estan connectats, i node1 no és un node terminal
     * @post S'han connectat els nodes amb una canonada de capacitat c, amb sentit de l'aigua de node1 a node2
     * @exception NoSuchElementException si node1 o node2 no pertanyen a la xarxa
     * @exception IllegalArgumentException si els nodes ja estan connectats o node1 és un node terminal
     * @param node1 Primer node a connectar
     * @param node2 Segon node a connectar
     * @param c Capacitat de la canonada
     */
    public void connectarAmbCanonada(NodeClass node1, NodeClass node2, float c) {
        //Pre: node1 i node2 pertanyen a la xarxa, no estan connectats, i node1 no és un node terminal
        //Post: S'han connectat els nodes amb una canonada de capacitat c, amb sentit de l'aigua de node1 a node2
        //Excepcions: NoSuchElementException node1 o node2 no pertanyen a la xarxa
        //            IllegalArgumentException els nodes ja estan connectats o node1 és un node terminal

        String idCanonada = node1.getID() + node2.getID();
        String idNode1 = node1.getID();
        String idNode2 = node2.getID();

        Node node1_ = g.getNode(idNode1);
        Node node2_ = g.getNode(idNode2);

        Canonada canonada = new Canonada(node1, node2, c);

        if(node1_ != null && node2_ != null) {
            if (!node1_.hasEdgeBetween(node2_)) {
                canonades.add(canonada);
                Edge edge = g.addEdge(idCanonada, idNode1, idNode2, true);
                edge.setAttribute("ui.label", 0 + " / " + c);
                edge.setAttribute("canonada", canonada);

                // Comprovem si estem connectant dos nodes d'origen
                if (node1_.getAttribute("node") instanceof Origen && node2_.getAttribute("node") instanceof Origen) {
                    // Canviem el tipus del segon node a "connexio"
                    node2_.setAttribute("ui.style", "fill-color: blue; size: 25px; text-size: 15;");
                }

                calcularCabals();
            } else {
                System.out.println("Els nodes ja estan connectats");
            }
        }
        else {
            System.out.println("Error: Un dels nodes és null");
        }
    }

    /**
     * @brief Abona un client a un terminal
     * @pre ---
     * @post Si el client no estava abonat a aquest terminal, ara ho està i retorna true. Si ja estava abonat, no fa res i retorna false.
     * @param idClient Identificador del client
     * @param nodeTerminal Terminal al qual abonar el client
     * @return true si el client s'ha abonat, false si ja estava abonat
     */
    public boolean abonar(String idClient, Terminal nodeTerminal) {

        if (abonats.containsKey(idClient)) {
            // El client ja està abonat, però potser no a aquesta terminal
            List<Terminal> terminals = abonats.get(idClient);
            if (terminals.contains(nodeTerminal) && !terminals.isEmpty()) {
                // El client ja està abonat a aquesta terminal
                return false;
            } else {
                // Abonem el client a aquesta terminal
                terminals.add(nodeTerminal);
                abonats.put(idClient, terminals);
                return true;
            }
        } else {
            // Abonem el client
            List<Terminal> terminals = new ArrayList<>();
            terminals.add(nodeTerminal);
            abonats.put(idClient, terminals);
            return true;
        }
    }

    /**
     * @brief Calcula el cabal que rep un client
     * @pre ---
     * @post Retorna el cabal que rep el client amb identificador idClient
     * @param idClient Identificador del client
     * @return Cabal que rep el client
     */
    public float cabalAbonat(String idClient) {

        if (abonats.containsKey(idClient)) {
            List<Terminal> terminals = abonats.get(idClient);
            float cabal = 0;
            for (Terminal terminal : terminals) {
                cabal += terminal.getDemanda();
            }
            return cabal;
        } else {
            return 0;
        }
    }

    /**
     * @brief Obre l'aixeta d'un node
     * @pre ---
     * @post Si el node no és null i té una aixeta, aquesta s'obre i retorna true. Si el node és null o no té aixeta, no fa res i retorna false.
     * @param node Node del qual obrir l'aixeta
     * @return true si s'ha obert l'aixeta, false en cas contrari
     */
    public boolean obrirAixeta(Node node) {

        if (node != null) {
            NodeClass nodeClass = (NodeClass) node.getAttribute("node");
            if (nodeClass != null) {
                if (nodeClass.aixetaOberta()) {
                    // L'aixeta ja està oberta
                    ArrayList<Object> operacio = new ArrayList<>();
                    operacio.add(nodeClass);
                    operacio.add(true);
                    pilaOperacions.push(operacio);
                    return true;
                } else {
                    // Obrim l'aixeta
                    nodeClass.obrirAixeta();
                    nodes.remove(nodeClass.getId());
                    nodes.put(nodeClass.getId(), nodeClass);
                    node.setAttribute("node", nodeClass);
                    ArrayList<Object> operacio = new ArrayList<>();
                    operacio.add(nodeClass);
                    operacio.add(false);
                    pilaOperacions.push(operacio);

                    return false;
                }
            }
        }
        return false;

    }

    /**
     * @brief Tanca l'aixeta d'un node
     * @pre ---
     * @post Si el node no és null i té una aixeta, aquesta es tanca i retorna true. Si el node és null o no té aixeta, no fa res i retorna false.
     * @param node Node del qual tancar l'aixeta
     * @return true si s'ha tancat l'aixeta, false en cas contrari
     */
    public boolean tancarAixeta(Node node) {

        if (node != null) {
            NodeClass nodeClass = nodes.get(node.getId());

            if (nodeClass != null) {
                if (!nodeClass.aixetaOberta()) {
                    // L'aixeta ja està tancada
                    ArrayList<Object> operacio = new ArrayList<>();
                    operacio.add(nodeClass);
                    operacio.add(false);
                    pilaOperacions.push(operacio);
                    return true;
                } else {
                    // Tanquem l'aixeta
                    nodeClass.tancarAixeta();
                    nodes.remove(nodeClass.getId());
                    nodes.put(nodeClass.getId(), nodeClass);
                    ArrayList<Object> operacio = new ArrayList<>();
                    operacio.add(nodeClass);
                    operacio.add(true);
                    pilaOperacions.push(operacio);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * @brief Estableix el cabal potencial d'un node origen
     * @pre nodeOrigen pertany a la xarxa
     * @post Estableix el cabal potencial del node d'origen a cabal
     * @exception NoSuchElementException si nodeOrigen no pertany a la xarxa
     * @exception IllegalArgumentException si nodeOrigen no és un node d'origen
     * @param nodeOrigen Node origen
     * @param cabal Cabal potencial a establir
     */
    public void establirCabal(Origen nodeOrigen, float cabal) {
        //Pre: nodeOrigen pertany a la xarxa
        //Post: Estableix el cabal potencial del node d'origen a cabal
        //Excepcions: NoSuchElementException si nodeOrigen no pertany a la xarxa
        //            IllegalArgumentException si nodeOrigen no és un node d'origen

        Node node = g.getNode(nodeOrigen.getID());

        NodeClass nodeClass = nodes.get(nodeOrigen.getID());

        if (nodeClass.getId().startsWith("O")) {
            nodeClass.establirCabalPotencial(cabal);
            node.setAttribute("node", nodeClass);
            nodes.remove(nodeClass.getId());
            nodes.put(nodeClass.getId(), nodeClass);
            NodeClass nodeProva = nodes.get(nodeOrigen.getID());
            System.out.println(nodeProva.getCabalPotencial());
        } else {
            System.out.println("El node no és un origen");
        }
    }

    /**
     * @brief Estableix la demanda d'aigua d'un node terminal
     * @pre nodeTerminal pertany a la xarxa
     * @post Estableix la demanda d'aigua del node terminal a demanda
     * @exception NoSuchElementException si nodeTerminal no pertany a la xarxa
     * @exception IllegalArgumentException si nodeTerminal no és un node terminal
     * @param nodeTerminal Node terminal
     * @param demanda Demanda d'aigua a establir
     */
    public void establirDemanda(Terminal nodeTerminal, float demanda) {
        //Pre: nodeTerminal pertany a la xarxa
        //Post: Estableix la demanda d'aigua del node terminal a demanda
        //Excepcions: NoSuchElementException si nodeTerminal no pertany a la xarxa
        //            IllegalArgumentException si nodeTerminal no és un node terminal

        Node node = g.getNode(nodeTerminal.getID());

        if (node.getAttribute("node") instanceof Terminal) {
            NodeClass newNode = nodes.get(nodeTerminal.getID());
            nodeTerminal.setNode(newNode);
            nodeTerminal.establirDemanda(demanda);
            node.setAttribute("node", nodeTerminal);
            nodes.remove(nodeTerminal.getID());
            nodes.put(nodeTerminal.getID(), nodeTerminal.getNode());
        } else {
            System.out.println("El node no és un terminal");
        }
    }

    /**
     * @brief Estableix la demanda d'aigua d'un node terminal
     * @pre nodeTerminal pertany a la xarxa
     * @post Estableix la demanda d'aigua del node terminal a demanda
     * @exception NoSuchElementException si nodeTerminal no pertany a la xarxa
     * @exception IllegalArgumentException si nodeTerminal no és un node terminal
     * @param nodeTerminal Node terminal
     * @param demanda Demanda d'aigua a establir
     */
    public float cabal(Node node) {

        return (float) node.getAttribute("cabal");
    }

    /**
     * @brief Retorna la demanda d'un node
     * @pre ---
     * @post Retorna la demanda del node
     * @param node Node del qual obtenir la demanda
     * @return Demanda del node
     */
    public float demanda(Node node) {

        return (float) node.getAttribute("demanda");
    }

    /**
     * @brief Desfà les últimes n operacions
     * @pre ---
     * @post S'han desfet les últimes n operacions
     * @param n Número d'operacions a desfer
     */
    public void backtrack(int n) {
        for (int i = 0; i < n; i++) {
            if (!pilaOperacions.isEmpty()) {
                ArrayList<Object> operacio = pilaOperacions.pop();
                NodeClass node = (NodeClass) operacio.get(0);
                boolean estatAnterior = (boolean) operacio.get(1);
                if (estatAnterior) {
                    node.obrirAixeta();
                } else {
                    node.tancarAixeta();
                }
            }
        }
    }

    /**
     * @brief Dibuixa la xarxa de distribució d'aigua
     * @pre ---
     * @post Dibuixa la xarxa de distribució d'aigua
     */
    public void dibuixar() {
        //Pre: ---
        //Post: Dibuixa la xarxa de distribució d'aigua
        calcularCabals();
        establirDemandaICabalConnexions();
        calcularCabals();
        establirDemandaICabalConnexions();
        calcularCabals();
        establirDemandaICabalConnexions();
        calcularCabals();
        establirDemandaICabalConnexions();
        g.setAttribute("ui.quality");
        g.setAttribute("ui.antialias");
        g.setAttribute("ui.stylesheet", "edge { text-alignment: under; text-size: 14; }");
        System.setProperty("org.graphstream.ui", "swing");
        String styleSheet =
                "node {" +
                        "fill-color: black;" +
                        "size: 25px;" +
                        "text-size: 15;" +
                        "}" +
                        "node.origen {" +
                        "fill-color: red;" +
                        "}" +
                        "node.terminal {" +
                        "fill-color: green;" +
                        "}" +
                        "node.connexio {" +
                        "fill-color: blue;" +
                        "}" +
                        "edge {" +
                        "fill-color: grey;" +
                        "arrow-size: 15px, 3px;" +
                        "}";
        g.setAttribute("ui.stylesheet", styleSheet);
        for (Node node : g) {
            if (node.getDegree() > 0) {
                NodeClass nodeClass = nodes.get(node.getId());
                String aixetaEstat = nodeClass.aixetaOberta() ? "Oberta" : "Tancada";
                String coordenadesX = String.valueOf(nodeClass.getCoordenades().getX());
                String coordenadesY = String.valueOf(nodeClass.getCoordenades().getY());
                node.setAttribute("ui.label", node.getId() + " " + aixetaEstat + " " + coordenadesX + ", " + coordenadesY);
                if (node.getId().startsWith("T")) {
                    node.setAttribute("ui.label", node.getAttribute("ui.label") + " Demanda punta: " + nodeClass.getDemanda() + " Demanda actual: " + nodeClass.getDemanda());
                }
            }
        }
        Viewer viewer = g.display();
        ((Viewer) viewer).setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

    }

    /**
     * @brief Retorna el conjunt de nodes veïns d'un node
     * @pre ---
     * @post Retorna el conjunt de nodes veïns del node
     * @param node Node del qual obtenir els veïns
     * @return Conjunt de nodes veïns del node
     */
    public Set<Node> getNeighborSet(Node node) {
        // Crea un conjunt per emmagatzemar els veïns
        Set<Node> neighborsSet = new HashSet<>();

        // Obté el grau del node (nombre d'arestes)
        int degree = node.getDegree();

        // Itera sobre les arestes del node
        for (int i = 0; i < degree; i++) {
            // Obtenim l'aresta
            Edge edge = node.getEdge(i);

            // Obtenim el veí de l'aresta
            Node neighbor = edge.getOpposite(node);

            // Afegeix el vei al conjunt
            neighborsSet.add(neighbor);
        }

        // Retorna el conjunt de veïns
        return neighborsSet;
    }

    /**
     * @brief Retorna la llista de nodes veïns d'un node
     * @pre ---
     * @post Retorna la llista de nodes veïns del node
     * @param node Node del qual obtenir els veïns
     * @return Llista de nodes veïns del node
     */
    public List<Node> getNeighbors(Node node) {
        // Crea una llista per emmagatzemar els veïns
        List<Node> neighborsList = new ArrayList<>();

        // Obté el conjunt de nodes veïns
        Set<? extends Node> neighborSet = getNeighborSet(node);

        // Afegeix tots els veïns a la llista
        neighborsList.addAll(neighborSet);

        // Retorna la llista de veïns
        return neighborsList;
    }

    /**
     * @brief Retorna l'aresta corresponent a una canonada
     * @pre ---
     * @post Retorna l'aresta corresponent a la canonada
     * @param canonada Canonada de la qual obtenir l'aresta
     * @return Aresta corresponent a la canonada
     */
    public Edge getEdgeFromCanonada(Canonada canonada) {
        Node node1 = g.getNode(canonada.node1().getId());
        Node node2 = g.getNode(canonada.node2().getId());
        return node1.getEdgeBetween(node2);
    }

    /**
     * @brief Retorna la llista d'arestes corresponents a les canonades
     * @pre ---
     * @post Retorna la llista d'arestes corresponents a les canonades
     * @return Llista d'arestes corresponents a les canonades
     */
    public List<Edge> getEdgesFromCanonades() {
        List<Edge> edges = new ArrayList<>();
        for (Canonada canonada : canonades) {
            edges.add(getEdgeFromCanonada(canonada));
        }
        return edges;
    }

    //-------------------------------------------------------------------------------------------------
    //                                Repartir cabal i demanda
    //-------------------------------------------------------------------------------------------------

    /**
     * @brief Estableix la demanda i el cabal de les connexions
     * @pre ---
     * @post S'ha establert la demanda i el cabal de les connexions
     */
    public void establirDemandaICabalConnexions() {
        // Creem una copia del mapa
        Map<String, NodeClass> nodesCopy = new HashMap<>(nodes);

        // Recorrem la copia del mapa
        for (NodeClass node : nodesCopy.values()) {
            // Si el node no és un terminal i l'aixeta està oberta
            if (!node.getId().startsWith("T") && node.aixetaOberta()) {
                // Obtenim totes les canonades que arriben al node
                List<Canonada> canonadesEntrants = getCanonadesSortints(node);

                // Inicialitzem la demanda a 0
                float demanda = 0;

                // Sumem la demanda de totes les canonades entrants
                for (Canonada canonada : canonadesEntrants) {
                    demanda += canonada.getDemanda();
                }

                // Establim la demanda del node
                node.establirDemanda(demanda);

                // Si el node és una connexió, establim el seu cabal igual a la seva demanda
                if (node.getId().startsWith("C")) {
                    node.establirCabalPotencial(demanda);
                }
                nodes.remove(node.getId());
                nodes.put(node.getId(), node);
            } else if (!node.aixetaOberta()) {
                // Si l'aixeta està tancada, establim la demanda i el cabal a 0
                node.establirDemanda(0);
                nodes.remove(node.getId());
                nodes.put(node.getId(), node);
            }
        }
    }

    /**
     * @brief Ajusta la capacitat d'un origen si és necessari
     * @pre ---
     * @post S'ha ajustat la capacitat de l'origen si era necessari
     * @param origen Origen del qual ajustar la capacitat
     */
    public void ajustarCapacitatOrigenSiNecessari(NodeClass origen) {
        // Obtenim totes les canonades que surten de l'origen
        List<Canonada> canonadesSortints = getCanonadesSortints(origen);

        // Si no hi ha cap canonada sortint, no fem res
        if (canonadesSortints.isEmpty()) {
            return;
        }

        // Trobem la canonada amb la major capacitat
        Canonada canonadaAmbMajorCapacitat = canonadesSortints.get(0);
        for (Canonada canonada : canonadesSortints) {
            if (canonada.getCapacitat() > canonadaAmbMajorCapacitat.getCapacitat()) {
                canonadaAmbMajorCapacitat = canonada;
            }
        }

        // Si la capacitat de l'origen és menor que la capacitat de la canonada amb major capacitat,
        // establim la capacitat de l'origen com a la capacitat de la canonada amb major capacitat
        if (origen.getCabalPotencial() < canonadaAmbMajorCapacitat.getCapacitat()) {
            origen.establirCabalPotencial(canonadaAmbMajorCapacitat.getCapacitat());
            NodeClass nouOrigen = nodes.get(origen.getID());
            nouOrigen.establirCabalPotencial(canonadaAmbMajorCapacitat.getCapacitat());
            nodes.remove(origen.getID());
            nodes.put(origen.getID(), nouOrigen);
        }
    }

    /**
     * @brief Calcula els cabals de la xarxa
     * @pre ---
     * @post Els cabals de la xarxa han estat calculats
     */
    public void calcularCabals() {
        calcularCabalsAscendent();
        calcularCabalsDescendent();
    }

    /**
     * @brief Calcula els cabals ascendent de la xarxa
     * @pre ---
     * @post Els cabals ascendent de la xarxa han estat calculats
     */
    private void calcularCabalsAscendent() {
        // Recorrem totes les canonades
        for (Canonada canonada : canonades) {
            // Obtenim el node d'origen de la canonada
            NodeClass nodeOrigen = canonada.node1();

            // Si el node d'origen és un origen, la seva demanda serà la demanda punta
            if (nodeOrigen.getId().startsWith("O")) {
                canonada.setDemanda(nodeOrigen.getDemanda());
            }
            // Si el node d'origen és una connexió, la seva demanda serà la suma de les demandes de les canonades que li arriben
            else if (nodeOrigen.getId().startsWith("C")) {
                float demanda = 0;
                for (Canonada canonadaEntrant : getCanonadesEntrants(nodeOrigen)) {
                    demanda += canonadaEntrant.getDemanda();
                }
                canonada.setDemanda(demanda);
            }
        }
    }

    /**
     * @brief Retorna les canonades sortints d'un node
     * @pre ---
     * @post Retorna una llista de les canonades sortints del node
     * @param node Node del qual obtenir les canonades sortints
     * @return Llista de canonades sortints del node
     */
    private List<Canonada> getCanonadesSortints(NodeClass node) {
        // Creem una llista buida per emmagatzemar les canonades sortints
        List<Canonada> canonadesSortints = new ArrayList<>();

        // Recorrem totes les canonades
        for (Canonada canonada : canonades) {
            // Si el node d'origen de la canonada és el node que es passa per referència, l'afegim a la llista
            if (canonada.node1().equals(node)) {
                canonadesSortints.add(canonada);
            }
        }

        // Retornem la llista de canonades sortints
        return canonadesSortints;
    }

    /**
     * @brief Retorna les canonades entrants a un node
     * @pre ---
     * @post Retorna una llista de les canonades entrants al node
     * @param node Node del qual obtenir les canonades entrants
     * @return Llista de canonades entrants al node
     */
    private List<Canonada> getCanonadesEntrants(NodeClass node) {
        // Creem una llista buida per emmagatzemar les canonades sortints
        List<Canonada> canonadesSortints = new ArrayList<>();

        // Recorrem totes les canonades
        for (Canonada canonada : canonades) {
            // Si el node d'origen de la canonada és el node que es passa per referència, l'afegim a la llista
            if (canonada.node2().equals(node)) {
                canonadesSortints.add(canonada);
            }
        }

        // Retornem la llista de canonades sortints
        return canonadesSortints;
    }

    /**
     * @brief Calcula els cabals descendent de la xarxa
     * @pre ---
     * @post Els cabals descendent de la xarxa han estat calculats
     */
    private void calcularCabalsDescendent() {
        for (NodeClass node : nodes.values()) {
            // Comprova si el node està obert
            if (node.aixetaOberta()) {
                double demandaTotal = node.getDemanda(); // Aquesta demanda ha estat establerta en l'ascendent
                double capacitatTotal = getCanonadesEntrants(node).stream()
                        .mapToDouble(Canonada::getCapacitat).sum();

                for (Canonada canonada : getCanonadesEntrants(node)) {
                    if (demandaTotal > 0) {
                        double cabalAssignat = (canonada.getCapacitat() / capacitatTotal) * demandaTotal;
                        cabalAssignat = Math.min(cabalAssignat, canonada.getCapacitat());  // No excedir la capacitat de la canonada
                        canonada.setDemanda((float) cabalAssignat);
                        Edge edge = g.getEdge(canonada.node1().getId() + canonada.node2().getId());
                        edge.setAttribute("ui.label", cabalAssignat + " / " + canonada.getCapacitat());

                    } else {
                        canonada.setDemanda(0);
                    }
                }
            } else {
                // Si el node està tancat, establim la demanda de les canonades entrants a 0
                for (Canonada canonada : getCanonadesEntrants(node)) {
                    canonada.setDemanda(0);
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------

    /**
     * @brief Retorna el node de la xarxa amb identificador id
     * @pre ---
     * @post Retorna el node de la xarxa amb identificador id
     * @param id Identificador del node
     * @return Node de la xarxa amb identificador id
     */
    public NodeClass getNode(String id) {
        return nodes.get(id);
    }

    /**
     * @brief Retorna el terminal de la xarxa amb identificador id
     * @pre ---
     * @post Retorna el terminal de la xarxa amb identificador id
     * @param id Identificador del terminal
     * @return Terminal de la xarxa amb identificador id
     */
    public Canonada getCanonada(NodeClass node1, NodeClass node2){
        for (Canonada canonada : canonades) {
            if (canonada.node1().getId().equals(node1.getId()) && canonada.node2().getId().equals(node2.getId())) {
                System.out.println("Canonada trobada entre " + node1.getId() + " i " + node2.getId());
                return canonada;
            }
        }
        return null;
    }

    //-------------------------------------------------------------------------------------------------
    //                                Flow Max
    //-------------------------------------------------------------------------------------------------

    /**
     * @brief Clona la xarxa de distribució d'aigua
     * @pre ---
     * @post Retorna un clon de la xarxa de distribució d'aigua
     * @return Clon de la xarxa de distribució d'aigua
     */
    public Xarxa clonar() {
        Xarxa xarxa = new Xarxa();
        for (NodeClass node : nodes.values()) {
            if (node.getId().startsWith("O")) {
                Origen origen = new Origen(node);
                xarxa.afegir(origen);
            } else if (node.getId().startsWith("T")) {
                Terminal terminal = new Terminal(node);
                xarxa.afegir(terminal);
            } else if (node.getId().startsWith("C")) {
                Connexio connexio = new Connexio(node);
                xarxa.afegir(connexio);
            }
        }
        for (Canonada canonada : canonades) {
            NodeClass node1 = canonada.node1();
            NodeClass node2 = canonada.node2();
            if (node1 != null && node2 != null) {
                xarxa.connectarAmbCanonada(node1, node2, canonada.getCapacitat());
            } else {
                System.out.println("Error: Un dels nodes és null");
            }
        }
        return xarxa;
    }

    /**
     * @brief Cerca un terminal a la xarxa de distribució d'aigua
     * @pre ---
     * @post Retorna el terminal si es troba, null en cas contrari
     * @return Terminal si es troba, null en cas contrari
     */
    public Terminal buscarTerminal() {
        for (NodeClass node : nodes.values()) {
            if (node.getId().startsWith("T")) {
                Terminal terminal = new Terminal();
                terminal.setNode(node);
                return terminal;
            }
        }
        return null; // Return null if no Terminal node is found
    }

    /**
     * @brief Dibuixa el flux de la xarxa de distribució d'aigua
     * @pre ---
     * @post El flux de la xarxa de distribució d'aigua ha estat dibuixat
     */
    public void dibuixarFlux(){

        calcularCabals();
        establirDemandaICabalConnexions();
        calcularCabals();
        establirDemandaICabalConnexions();
        calcularCabals();
        establirDemandaICabalConnexions();

        for (Canonada canonada : canonades) {
            float flux = canonada.getDemanda();
            Edge edge = getEdgeFromCanonada(canonada);
            edge.setAttribute("ui.label", "Flux: " + flux + "/" + canonada.getCapacitat());
        }

        g.setAttribute("ui.quality");
        g.setAttribute("ui.antialias");
        g.setAttribute("ui.stylesheet", "edge { text-alignment: under; text-size: 14; }");
        System.setProperty("org.graphstream.ui", "swing");
        String styleSheet =
                "node {" +
                        "   text-mode: normal;" +
                        "   text-background-mode: rounded-box;" +
                        "   text-background-color: white;" +
                        "   text-alignment: above;" +
                        "}";
        g.setAttribute("ui.stylesheet", styleSheet);
        for (Node node : g) {
            node.setAttribute("ui.label", node.getId());
        }
        Viewer viewer = g.display();
        ((Viewer) viewer).setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }

    //-------------------------------------------------------------------------------------------------------
    //                        Mètodes per a la implementació de l'algorisme esArbre
    //-------------------------------------------------------------------------------------------------------

    /**
     * @brief Retorna el nombre de Canonades Entrants d'un node
     * @pre ---
     * @post Retorna el nombre de Canonades Entrants d'un node
     * @param node
     * @return Nombre de Canonades Entrants d'un node
     */
    public int getInDegree(NodeClass node){
        Node node_ = g.getNode(node.getId());
        return node_.getInDegree();
    }

    /**
     * @brief Retorna una llista de nodes connectats a un node específic
     * @pre ---
     * @post Retorna una llista de nodes que estan connectats al node específic
     * @param nodeEspecific Node específic del qual obtenir els nodes connectats
     * @return Llista de nodes connectats al node específic
     */
    public List<NodeClass> getNodesConnectats(NodeClass nodeEspecific) {

        // Creem una llista per emmagatzemar els descendents
        List<NodeClass> descendents = new ArrayList<>();

        // Creem una pila per emmagatzemar els nodes a visitar
        Stack<NodeClass> stack = new Stack<>();
        stack.push(nodeEspecific);

        // Creem un conjunt per emmagatzemar els nodes visitats
        Set<NodeClass> visited = new HashSet<>();
        visited.add(nodeEspecific);

        while (!stack.isEmpty()) {
            NodeClass actual = stack.pop();

            // Obtenim tots els fills del node actual
            List<NodeClass> fills = getChildren(actual);

            for (NodeClass fill : fills) {
                // Si el fill no ha estat visitat, l'afegim a la pila i a la llista de descendents
                if (!visited.contains(fill)) {
                    stack.push(fill);
                    descendents.add(fill);
                    visited.add(fill);
                }
            }
        }

        // Retornem la llista de descendents
        return descendents;

    }

    /**
     * @brief Retorna una llista de fills d'un node específic
     * @pre ---
     * @post Retorna una llista de nodes que són fills del node específic
     * @param pare Node específic del qual obtenir els fills
     * @return Llista de fills del node específic
     */
    private List<NodeClass> getChildren(NodeClass pare) {
        List<NodeClass> fills = new ArrayList<>();
        for (Canonada canonada : canonades) {
            if (canonada.node1().getId().equals(pare.getId())) {
                fills.add(canonada.node2());
            }
        }
        return fills;
    }

}
    

