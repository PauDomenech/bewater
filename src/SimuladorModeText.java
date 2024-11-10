/**
 * @class SimuladorModeText
 *
 *
 * Pau Domenech Villahermosa
 */

import org.graphstream.graph.Node;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.*;

/**
 * @file SimuladorModeText.java
 * @brief Simula les operacions de construcció, modificació i consulta d'una xarxa de distribució d'aigua a partir d'un fitxer de text
 *
 * Pau Domenech Villahermosa
 */

public class SimuladorModeText {
    //Descripció general: Simula les operacions de construcció, modificació i consulta d'una xarxa de distribució
    // d'aigua a partir d'un fitxer de text

    private final Xarxa x;

    private final GestorXarxes gestor;

    /**
     * @brief Constructor
     * @pre ---
     * @post Crea un nou simulador amb una xarxa i un gestor de xarxes buits
     */
    public SimuladorModeText() {

        this.gestor = new GestorXarxes();
        this.x = new Xarxa();
    }

    /*
    private Map<Edge, Float> inicialitzarFluxArestes(Xarxa x) {
        Map<Edge, Float> fluxArestes = new HashMap<>();
        for (Edge e : x.getEdges()) {
            fluxArestes.put(e, 0.0f); // Suposa que inicialment no hi ha flux a través de cap aresta.
        }
        return fluxArestes;
    }
*/
    /**
     * @brief Simula les operacions de construcció, modificació i consulta d'una xarxa de distribució d'aigua a partir d'un fitxer de text
     * @pre fitxer és el nom d'un fitxer de text que conté una seqüència d'operacions a realitzar sobre una xarxa de distribució d'aigua
     * @post S'han realitzat les operacions descrites al fitxer sobre la xarxa de distribució d'aigua
     * @param arg Nom del fitxer d'entrada
     * @param fitxer Nom del fitxer de sortida
     */
    public void simular(String arg, String fitxer){
        //Pre: fitxer és el nom d'un fitxer de text que conté una seqüència d'operacions a realitzar sobre una xarxa de
        // distribució d'aigua
        //Post: S'han realitzat les operacions descrites al fitxer sobre la xarxa de distribució d'aigua

        //Processarà el fitxer de text, localitzant els nodes corresponents a la xarxa a partir dels seus identificadors,
        // i cridant els mètodes corresponents de la classe Xarxa i GestorXarxes per realitzar les operacions descrites al
        // fitxer. Si alguna operació no es pot realitzar, es mostrarà un missatge d'error per la sortida estàndard.

        try {
            File file = new File(arg);
            Scanner scanner = new Scanner(file);
            PrintWriter writer = new PrintWriter(fitxer, "UTF-8");

            while (scanner.hasNextLine()) {
                String operacio = scanner.nextLine();

                switch (operacio) {
                    case "terminal":
                        String idTerminal = scanner.nextLine();
                        Coordenades cTerminal = new Coordenades(scanner.nextLine());
                        float demandaPuntaTerminal = Float.parseFloat(scanner.nextLine());
                        x.afegir(new Terminal(idTerminal, cTerminal, demandaPuntaTerminal));
                        break;
                    case "origen":
                        String idOrigen = scanner.nextLine();
                        Coordenades cOrigen = new Coordenades(scanner.nextLine());
                        x.afegir(new Origen(idOrigen, cOrigen));
                        break;
                    case "connexio":
                        String idConnexio = scanner.nextLine();
                        Coordenades cConnexio = new Coordenades(scanner.nextLine());
                        x.afegir(new Connexio(idConnexio, cConnexio));
                        break;
                    case "connectar":
                        String id1 = scanner.nextLine();
                        String id2 = scanner.nextLine();
                        Node node1 = x.node(id1);
                        Node node2 = x.node(id2);
                        NodeClass node1Class;
                        NodeClass node2Class;
                        node1Class = x.getNode(id1);
                        node2Class = x.getNode(id2);
                        if (x.node(id1) != null && x.node(id2) != null) {
                            float capacitat = Float.parseFloat(scanner.nextLine());

                            x.connectarAmbCanonada(node1Class, node2Class, capacitat);
                        } else {
                            writer.println("No es pot connectar: un o ambdós nodes no existeixen");
                        }
                        break;
                    case "abonar":
                        String idAbonat = scanner.nextLine();
                        String idTerminalAbonat = scanner.nextLine();
                        Terminal node = (Terminal) x.node(idTerminalAbonat).getAttribute("node");
                        Coordenades c = node.getCoordenades();
                        float demandaPunta = node.getCabalPotencial();
                        Terminal terminal = new Terminal(idTerminalAbonat, c, demandaPunta);
                        if (x.node(idTerminalAbonat) != null) {
                            if (x.abonar(idAbonat, terminal)) {
                                writer.println("Abonament realitzat correctament");
                            } else {
                                writer.println("No es pot abonar: ja hi ha un abonament a aquest terminal");
                            }
                        } else {
                            writer.println("No es pot abonar: un o ambdós nodes no existeixen");
                        }
                        break;

                    case "cabal abonat":
                        String idAbonatCabal = scanner.nextLine();
                        writer.println(x.cabalAbonat(idAbonatCabal));
                        break;
                    case "obrir":
                        String idNode = scanner.nextLine();
                        Node nodeAixeta = x.node(idNode);
                        if (nodeAixeta != null) {
                            if (x.obrirAixeta(nodeAixeta)) {
                                writer.println("L'aixeta ja estava oberta");
                            } else {
                                writer.println("L'aixeta s'ha obert");
                            }
                        } else {
                            writer.println("No es pot obrir l'aixeta: el node no existeix");
                        }
                        break;
                    case "tancar":
                        String idNodeTancar = scanner.nextLine();
                        Node nodeTancar = x.node(idNodeTancar);
                        if (nodeTancar != null) {
                            if (x.tancarAixeta(nodeTancar)) {
                                writer.println("L'aixeta ja estava tancada");
                            } else {
                                writer.println("L'aixeta s'ha tancat");
                            }
                        } else {
                            writer.println("No es pot tancar l'aixeta: el node no existeix");
                        }
                        break;
                    case "cabal":
                        String idOrigenCabal = scanner.nextLine();
                        float cabal = Float.parseFloat(scanner.nextLine());
                        Origen nodeOrigen = new Origen(idOrigenCabal, new Coordenades(0, 0));
                        if (nodeOrigen != null) {
                            x.establirCabal(nodeOrigen, cabal);
                        } else {
                            writer.println("No es pot establir el cabal: el node no existeix o no és un origen");
                        }
                        break;
                    case "demanda":
                        String idTerminalDemanda = scanner.nextLine();
                        float demanda = Float.parseFloat(scanner.nextLine());
                        Terminal nodeTerminal = new Terminal(idTerminalDemanda, new Coordenades(0, 0), 0);
                        if (nodeTerminal != null) {
                            x.establirDemanda(nodeTerminal, demanda);
                        } else {
                            writer.println("No es pot establir la demanda: el node no existeix o no és un terminal");
                        }
                        break;
                    case "backtrack":
                        int num = Integer.parseInt(scanner.nextLine());
                        x.backtrack(num);

                        break;
                    case "te cicles":
                        String idOrigenCicles = scanner.nextLine();
                        Origen origenCicles = new Origen(idOrigenCicles, new Coordenades(0, 0));  // Pots assumir que ja tens una manera de construir un objecte Origen amb les coordenades necessàries.

                        // Comprova si l'objecte Origen i la seva id existeixen a la xarxa.
                        if (x.node(idOrigenCicles) != null && origenCicles != null) {
                            boolean teCicles = GestorXarxes.teCicles(x, origenCicles);
                            writer.println("La xarxa té cicles: " + teCicles);
                        } else {
                            writer.println("No es pot comprovar si hi ha cicles: el node origen no existeix");
                        }

                        break;
                    case "arbre":
                        String idOrigenArbre = scanner.nextLine();
                        Origen origenArbre = new Origen(idOrigenArbre, new Coordenades(0, 0));  // Pots assumir que ja tens una manera de construir un objecte Origen amb les coordenades necessàries.

                        // Comprova si l'objecte Origen i la seva id existeixen a la xarxa.
                        if (x.node(idOrigenArbre) != null && origenArbre != null) {
                            boolean esArbre = gestor.esArbre(x, origenArbre);
                            writer.println("La component connexa de la xarxa és un arbre: " + esArbre);
                        } else {
                            writer.println("No es pot comprovar si la component és un arbre: el node origen no existeix");
                        }

                        break;
                    case "max-flow":
                        String idOrigenMaxFlow = scanner.nextLine();
                        Origen origenMaxFlow = new Origen(idOrigenMaxFlow, new Coordenades(0, 0));  // Pots assumir que ja tens una manera de construir un objecte Origen amb les coordenades necessàries.
                        gestor.fluxMaxim(x, origenMaxFlow);

                        break;
                    /*
                    case "exces cabal":
                        Set<Canonada> canonades = new HashSet<>();
                        int numCanonades = Integer.parseInt(scanner.nextLine());
                        for (int i = 0; i < numCanonades; i++) {
                            String idCanonada = scanner.nextLine();
                            float capacitat = Float.parseFloat(scanner.nextLine());
                            Coordenades c1 = new Coordenades(scanner.nextLine());
                            Coordenades c2 = new Coordenades(scanner.nextLine());
                            NodeClass node1 = new NodeClass(idCanonada + "1", c1); // Suposant estructura que permet crear nodes temporals
                            NodeClass node2 = new NodeClass(idCanonada + "2", c2);

                            canonades.add(new Canonada(node1, node2, capacitat));
                        }

                        // Utilitzar el mètode de GestorXarxes per determinar els excesos de cabal
                        Set<Canonada> excesCabal = GestorXarxes.excesCabal(x, canonades);
                        if (!excesCabal.isEmpty()) {
                            writer.println("Canonades amb exces de cabal:");
                            for (Canonada c : excesCabal) {
                                writer.println("Canonada entre " + c.getNodeOrigen().getID() + " i " + c.getNodeDesti().getID() + " amb capacitat " + c.getCapacitat());
                            }
                        } else {
                            writer.println("No hi ha exces de cabal en les canonades especificades.");
                        }

                        break;
                    */
                    case "cabal minim":
                        String idOrigenCabalMinim = scanner.nextLine();
                        NodeClass origenCabalMinim = x.getNode(idOrigenCabalMinim);
                        String percentatgeDemandaSatisfetStr = scanner.nextLine();

                        float percentatgeDemandaSatisfet = Float.parseFloat(percentatgeDemandaSatisfetStr.replace("%", ""));

                        // Comprova si l'objecte Origen i la seva id existeixen a la xarxa.
                        if (x.node(idOrigenCabalMinim) != null && origenCabalMinim != null) {
                            float cabalMinim = gestor.cabalMinim(x, origenCabalMinim, percentatgeDemandaSatisfet);
                            writer.println("cabal minim");
                            writer.println(cabalMinim);
                        } else {
                            writer.println("No es pot calcular el cabal mínim: el node origen no existeix");
                        }

                        break;
                    case "aixetes tancar":
                        Map<Terminal, Boolean> aiguaArriba = new HashMap<>();
                        try (Scanner Scanner = new Scanner(new File(arg))) {
                            while (Scanner.hasNextLine()) {
                                String idTerminal1 = scanner.nextLine();
                                boolean arribaAigua = Boolean.parseBoolean(scanner.nextLine());
                                Terminal terminal1 = (Terminal) x.node(idTerminal1).getAttribute("node");

                                // Comprova si el terminal existeix dins la xarxa abans d'afegir-lo al mapa
                                if (terminal1 != null) {
                                    aiguaArriba.put(terminal1, arribaAigua);
                                } else {
                                    System.out.println("Terminal no trobat: " + idTerminal1);
                                }
                            }
                        } catch (FileNotFoundException e) {
                            System.out.println("Fitxer no trobat: " + arg);
                            e.printStackTrace();
                        }

                        // Utilitza el mètode de GestorXarxes per obtenir els nodes que han de tancar les seves aixetes
                        Set<Node> nodesPerTancar = GestorXarxes.aixetesTancar(x, aiguaArriba);
                        if (!nodesPerTancar.isEmpty()) {
                            try (PrintWriter escriure = new PrintWriter(fitxer, "UTF-8")) {
                                for (Node aixetaNode : nodesPerTancar) {  // Canviat 'node' per 'aixetaNode' per evitar conflictes
                                    NodeClass nodeClass = x.getNode(aixetaNode.getId());  // Assumeix que pots obtenir un NodeClass des d'un Node
                                    if (nodeClass != null) {
                                        escriure.println("Tancar aixeta del node: " + nodeClass.getId());
                                    } else {
                                        escriure.println("Informació no disponible per al node: " + aixetaNode.getId());
                                    }
                                }
                            } catch (IOException e) {
                                System.out.println("Error en escriure al fitxer: " + fitxer);
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("No hi ha aixetes per tancar.");
                        }
                        break;

                    /*
                    case "buscar cami":
                        String idOrigen = scanner.nextLine();  // Llegir l'ID del node origen
                        NodeClass origen = x.getNode(idOrigen);

                        if (origen == null) {
                            System.out.println("El node origen no existeix.");
                            break;
                        }

                        // Suposem que tenim una estructura inicialitzada de fluxArestes per a tots els edges.
                        Map<Edge, Float> fluxArestes = inicialitzarFluxArestes(x);

                        // Cridar la funció per buscar un camí residual
                        Map<Edge, Float> camiResidual = buscarCamiResidual(x, origen, fluxArestes);

                        if (camiResidual != null && !camiResidual.isEmpty()) {
                            System.out.println("S'ha trobat un camí residual. Detalls del camí:");
                            for (Map.Entry<Edge, Float> entry : camiResidual.entrySet()) {
                                System.out.println("Aresta: " + entry.getKey().getId() + ", Flux Residual: " + entry.getValue());
                            }
                        } else {
                            System.out.println("No s'ha trobat cap camí residual des de " + idOrigen);
                        }
                        break;
                    */
                    default:
                        writer.println("Operació no reconeguda: " + operacio);
                }
            }
            scanner.close();
            writer.close();
            x.dibuixar();
        } catch (FileNotFoundException e) {
            System.out.println("Fitxer no trobat: " + fitxer);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error en crear el fitxer de sortida: " + fitxer);
            e.printStackTrace();
        }
    }
}
