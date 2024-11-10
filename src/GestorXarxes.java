import java.util.*;
import java.util.stream.Collectors;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

/**
 * @file GestorXarxes.java
 * @brief Mòdul funcional amb funcions per a la gestió de xarxes de distribució d'aigua
 *
 * Pau Domenech Villahermosa i Fran Josè Àlvarez
 */
public class GestorXarxes {

    // Descripció general: Mòdul funcional amb funcions per a la gestió de xarxes de distribució d'aigua.

    /**
     * @brief Diu si la component connexa de la xarxa x que conté nodeOrigen té cicles
     * @pre nodeOrigen pertany a la xarxa x
     * @post Retorna true si la component connexa de la xarxa x que conté nodeOrigen té cicles, false en cas contrari
     * @param x Xarxa on es realitza la comprovació
     * @param nodeOrigen Node origen de la comprovació
     * @return true si la component connexa de la xarxa x que conté nodeOrigen té cicles, false en cas contrari
     */
    public static boolean teCicles(Xarxa x, Origen nodeOrigen) {
        // Pre: nodeOrigen pertany a la xarxa x.
        // Post: Diu si la component connexa de la xarxa x que conté nodeOrigen té cicles.

        Set<NodeClass> visitats = new HashSet<>();
        Queue<NodeClass> cua = new LinkedList<>();
        Map<NodeClass, NodeClass> pares = new HashMap<>();
        NodeClass origen = x.getNode(nodeOrigen.getID());

        cua.add(origen);
        visitats.add(origen);
        pares.put(origen, null);

        while (!cua.isEmpty()) {
            NodeClass actual = cua.poll();
            List<Node> veins = x.getNeighbors(x.node(actual.getID()));

            for (Node vei : veins) {
                NodeClass veiClasse = x.getNode(vei.getId());

                if (!visitats.contains(veiClasse)) {
                    visitats.add(veiClasse);
                    pares.put(veiClasse, actual);
                    cua.add(veiClasse);
                } else if (!veiClasse.equals(pares.get(actual))) {
                    return true; // S'ha trobat un cicle.
                }
            }
        }
        return false; // No s'han trobat cicles.
    }

    public static boolean esArbre(Xarxa x, NodeClass nodeOrigen) {
        // Comprova que el node origen no té cap canonada entrant
        if (x.getInDegree(nodeOrigen) <= 0) {
            // Per cada node restant, comprova que només té una canonada entrant
            List<NodeClass> nodes = x.getNodesConnectats(nodeOrigen);
            for (NodeClass node : nodes) {
                if (node != nodeOrigen && x.getInDegree(node) > 1) {
                    return false;
                }
            }
        }
        else {
            return false;
        }
        // Si tots els nodes compleixen les condicions, la xarxa és un arbre
        return true;
    }


    /**
     * @brief Retorna el subconjunt de canonades de cjtCanonades on es sobrepassaria la capacitat si es satisfés la demanda
     * @pre Les canonades de cjtCanonades pertanyen a una mateixa component connexa, sense cicles, de la xarxa x
     * @post Retorna el subconjunt de canonades de cjtCanonades on es sobrepassaria la capacitat si es satisfés la demanda
     * @param x Xarxa on es realitza la comprovació
     * @param cjtCanonades Conjunt de canonades a comprovar
     * @return Conjunt de canonades on es sobrepassaria la capacitat si es satisfés la demanda
     */
    public static Set<Canonada> excesCabal(Xarxa x, Set<Canonada> cjtCanonades) {
        // Pre: Les canonades de cjtCanonades pertanyen a una mateixa component connexa, sense cicles, de la xarxa x.
        // Post: Retorna el subconjunt de canonades de cjtCanonades on es sobrepassaria la capacitat si es satisfés la demanda.

        Set<Canonada> excesCabal = new HashSet<>();

        for (Canonada canonada : cjtCanonades) {
            if (canonada.getDemanda() > canonada.getCapacitat()) {
                excesCabal.add(canonada);
            }
        }

        return excesCabal;
    }

    /**
     * @brief Retorna el conjunt de nodes que s'han de tancar perquè la situació actual de la xarxa sigui coherent
     * @pre Tots els terminals de aiguaArriba pertanyen a la xarxa x, aiguaArriba.get(t) indica si arriba aigua a t, i la xarxa x té forma d'arbre
     * @post Retorna el conjunt de nodes que s'han de tancar perquè la situació actual de la xarxa sigui coherent
     * @param x Xarxa on es realitza la comprovació
     * @param aiguaArriba Mapa que indica si arriba aigua a cada terminal
     * @return Conjunt de nodes que s'han de tancar perquè la situació actual de la xarxa sigui coherent
     */
    public static Set<Node> aixetesTancar(Xarxa x, Map<Terminal, Boolean> aiguaArriba) {
        // Pre: Tots els terminals de aiguaArriba pertanyen a la xarxa x, aiguaArriba.get(t) indica si arriba aigua a t,
        // i la xarxa x té forma d'arbre.
        // Post: Retorna el conjunt de nodes que s'han de tancar perquè la situació actual de la xarxa sigui coherent.

        Set<Node> nodesTancar = new HashSet<>();

        for (Map.Entry<Terminal, Boolean> entrada : aiguaArriba.entrySet()) {
            Terminal terminal = entrada.getKey();
            boolean arribaAigua = entrada.getValue();

            Node node = x.node(terminal.getID());
            NodeClass nodeClasse = x.getNode(node.getId());

            if (!arribaAigua && nodeClasse.aixetaOberta()) {
                nodesTancar.add(node);
            }
        }

        return nodesTancar;
    }
/*
    public static List<Node> nodesOrdenats(Coordenades c, Set<Node> cjtNodes) {
        // Pre: ---
        // Post: Retorna els nodes de cjtNodes ordenats segons la seva distància a c i, en cas d'empat, per identificador.

        return cjtNodes.stream()
                .sorted(Comparator
                        .comparingDouble(node -> {
                            // Obtenim l'objecte `NodeClass` associat al node
                            NodeClass nodeClass = node.getAttribute("node", NodeClass.class);

                            // Si el nodeClass és nul (pot ser per falta de l'atribut), evitem errors retornant una gran distància
                            if (nodeClass == null) {
                                return Double.MAX_VALUE;
                            }

                            // Obtenim les coordenades associades al node
                            Coordenades coordenadesNode = nodeClass.getCoordenades();

                            // Calculem la distància a les coordenades de referència
                            return c.distancia(coordenadesNode);
                        })
                        // En cas d'empat en la distància, s'ordenen per identificador
                        .thenComparing(Node::getId))
                .collect(Collectors.toList());
    }*/


    /**
     * @brief Retorna el cabal mínim necessari per tal que cap node terminal no rebi menys d'un percentatge determinat de la seva demanda
     * @pre nodeOrigen pertany a la xarxa x, la component connexa de la xarxa x que conté nodeOrigen no té cicles, i percentatgeDemandaSatisfet > 0
     * @post Retorna el cabal mínim necessari per tal que cap node terminal no rebi menys d'un percentatge determinat de la seva demanda
     * @param x Xarxa on es realitza la comprovació
     * @param nodeOrigen Node origen de la comprovació
     * @param percentatgeDemandaSatisfet Percentatge de demanda a satisfer
     * @return Cabal mínim necessari per tal que cap node terminal no rebi menys d'un percentatge determinat de la seva demanda
     */
    public static float cabalMinim(Xarxa x, NodeClass nodeOrigen, float percentatgeDemandaSatisfet) {
        // Pre: nodeOrigen pertany a la xarxa x, la component connexa de la xarxa x que conté nodeOrigen no té cicles,
        // i percentatgeDemandaSatisfet > 0.
        // Post: Retorna el cabal mínim necessari per tal que cap node terminal no rebi menys d'un percentatge determinat de la seva demanda.

        // Pre: nodeOrigen pertany a la xarxa x, la component connexa de la xarxa x que conté nodeOrigen no té cicles,
        // i percentatgeDemandaSatisfet > 0.
        // Post: Retorna el cabal mínim necessari per tal que cap node terminal no rebi menys d'un percentatge determinat de la seva demanda.

        if (!esArbre(x, nodeOrigen) || !nodeOrigen.aixetaOberta()) {
            return -1; //La xarxa no es un arbre o l'origen no esta obert
        }

        Set<NodeClass> visitats = new HashSet<>();
        Queue<NodeClass> cua = new LinkedList<>();
        NodeClass origen = x.getNode(nodeOrigen.getID());

        cua.add(origen);
        visitats.add(origen);

        float cabalMinim = 0;

        while (!cua.isEmpty()) {
            NodeClass actual = cua.poll();
            List<Node> veins = x.getNeighbors(x.node(actual.getID()));

            for (Node vei : veins) {
                NodeClass veiClasse = x.getNode(vei.getId());

                // Check if the node is open
                if (!veiClasse.aixetaOberta()) {
                    continue;
                }

                if (!visitats.contains(veiClasse)) {
                    visitats.add(veiClasse);
                    cua.add(veiClasse);
                }

                if (veiClasse.getId().startsWith("T")) {
                    float demanda = veiClasse.getDemanda();
                    cabalMinim += (percentatgeDemandaSatisfet / 100) * demanda;
                }
            }
        }

        return cabalMinim;
    }

    /**
     * @brief Calcula el flux màxim utilitzant l'algorisme de Ford-Fulkerson
     * @pre ---
     * @post Visualitza la xarxa amb el flux màxim i imprimeix el flux màxim
     * @param x Xarxa on es realitza la comprovació
     * @param nodeOrigen Node origen de la comprovació
     */
    public static void fluxMaxim(Xarxa x, Origen nodeOrigen) {

        x.calcularCabals();
        x.establirDemandaICabalConnexions();
        x.calcularCabals();
        x.establirDemandaICabalConnexions();
        x.calcularCabals();
        x.establirDemandaICabalConnexions();
        // Troba el node terminal
        Terminal nodeTerminal = x.buscarTerminal();

        // Calcula el flux màxim utilitzant l'algorisme de Ford-Fulkerson
        float maxFlow = edmondsKarp(x, nodeOrigen, nodeTerminal);

        // Visualitza la xarxa amb el flux màxim

        x.dibuixarFlux();

        System.out.println("Flux màxim: " + maxFlow);

    }

    /**
     * @brief Calcula el flux màxim utilitzant l'algorisme de Edmonds-Karp
     * @pre ---
     * @post Retorna el flux màxim
     * @param x Xarxa on es realitza la comprovació
     * @param source Node origen de la comprovació
     * @param sink Node destí de la comprovació
     * @return Flux màxim
     */
    public static float edmondsKarp(Xarxa x, Origen source, Terminal sink) {
        float maxFlow = 0;

        // Crea un graf residual i omple'l amb les capacitats donades en el graf original
        Xarxa residualGraph = x.clonar();

        residualGraph.calcularCabals();
        residualGraph.establirDemandaICabalConnexions();
        residualGraph.calcularCabals();
        residualGraph.establirDemandaICabalConnexions();
        residualGraph.calcularCabals();
        residualGraph.establirDemandaICabalConnexions();

        Map<NodeClass, NodeClass> previousNode = new HashMap<>();

        // Comença des de l'origen i explora el graf residual utilitzant Breadth-First Search
        while (BFS(residualGraph, source, sink, previousNode)) {
            // Troba el flux màxim a través del camí trobat
            float pathFlow = Float.MAX_VALUE;
            for (NodeClass v = sink; v != source; v = previousNode.get(v)) {
                NodeClass u = previousNode.get(v);
                Canonada canonada = residualGraph.getCanonada(u, v);
                pathFlow = Math.min(pathFlow, canonada.getDemanda());
            }

            // Actualitza les capacitats residuals de les arestes i les arestes inverses al llarg del camí
            for (NodeClass v = sink; v != source; v = previousNode.get(v)) {
                NodeClass u = previousNode.get(v);
                Edge edge = residualGraph.getEdgeFromCanonada(new Canonada(u, v, 0f));
                Canonada canonada = edge.getAttribute("canonada", Canonada.class);
                canonada.setCapacitat(canonada.getCapacitat() - pathFlow);
                edge.setAttribute("canonada", canonada);
            }

            // Afegeix el flux del camí al flux total
            maxFlow += pathFlow;
        }

        // Retorna el flux màxim
        return maxFlow;
    }

    private static boolean BFS(Xarxa residualGraph, Origen source, Terminal sink, Map<NodeClass, NodeClass> previousNode) {
        Set<NodeClass> visited = new HashSet<>();
        Queue<NodeClass> queue = new LinkedList<>();
        queue.add(source);
        visited.add(source);

        residualGraph.dibuixar();

        while (!queue.isEmpty()) {
            NodeClass node = queue.poll();
            for (Node neighbor : residualGraph.getNeighbors(residualGraph.node(node.getID()))) {
                NodeClass neighborNode = residualGraph.getNode(neighbor.getId());
                Canonada canonada = residualGraph.getCanonada(node, neighborNode);
                if (canonada != null && canonada.getCapacitat() > 0.00 && !visited.contains(neighborNode)) {
                    System.out.println("Entra");
                    previousNode.put(neighborNode, node);
                    visited.add(neighborNode);
                    queue.add(neighborNode);
                    if (neighborNode.getID().equals(sink.getID())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * @brief Troba un camí residual des del node d'origen fins a qualsevol node terminal
     * @pre ---
     * @post Retorna un camí residual des del node d'origen fins a qualsevol node terminal, o null si no n'hi ha cap
     * @param x Xarxa on es realitza la comprovació
     * @param origen Node origen de la comprovació
     * @param fluxArestes Mapa de fluxos per cada aresta
     * @return Mapa que representa un camí residual des del node d'origen fins a qualsevol node terminal, o null si no n'hi ha cap
     */
    private static Map<Edge, Float> buscarCamiResidual(Xarxa x, NodeClass origen, Map<Edge, Float> fluxArestes) {
        Map<NodeClass, Edge> previ = new HashMap<>();
        Queue<NodeClass> cua = new LinkedList<>();
        cua.add(origen);
        previ.put(origen, null);

        while (!cua.isEmpty()) {
            NodeClass actual = cua.poll();
            List<Node> veins = x.getNeighbors(x.node(actual.getID()));

            for (Node vei : veins) {
                NodeClass veiClasse = x.getNode(vei.getId());
                Edge aresta = x.getEdgeFromCanonada(new Canonada(actual, veiClasse, 0f));
                float capacitat = aresta.getAttribute("canonada", Canonada.class).getCapacitat();
                float fluxActual = fluxArestes.getOrDefault(aresta, 0f);

                // Comprova si el camí és residual
                if (!previ.containsKey(veiClasse) && fluxActual < capacitat) {
                    previ.put(veiClasse, aresta);
                    cua.add(veiClasse);

                    // Si és un terminal, es pot finalitzar la cerca
                    if (veiClasse instanceof Terminal) {
                        Map<Edge, Float> camiResidual = new HashMap<>();
                        NodeClass node = veiClasse;
                        while (node != origen) {
                            Edge arestaResidual = previ.get(node);
                            float capacitatResidual = arestaResidual.getAttribute("canonada", Canonada.class).getCapacitat();
                            float fluxResidual = capacitatResidual - fluxArestes.getOrDefault(arestaResidual, 0f);
                            camiResidual.put(arestaResidual, fluxResidual);
                            node = arestaResidual.getSourceNode().getAttribute("node", NodeClass.class);
                        }
                        return camiResidual;
                    }
                }
            }
        }
        return null; // No s'ha trobat cap camí residual
    }
}
