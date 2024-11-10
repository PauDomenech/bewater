/**
 * @class BeWater
 * @brief Programa principal de simulació de xarxes de distribució d'aigua
 *
 * Pau Domenech Villahermosa
 */

import java.util.Scanner;
import org.graphstream.graph.Node;

public abstract class BeWater {
    //Descripció general: Programa principal de simulació de xarxes de distribució d'aigua

    public static void main(String[] args) {
        SimuladorModeText simulador = new SimuladorModeText();
        System.out.println("Be water, my friend");
        simulador.simular(args[0], args[1]);
    }

}



