package random;

import models.Card;
import models.Npc;

public class Logger {

    static private String project = "uno-interficies";

    public static void error(String error) {
        System.out.println("[ERROR] [ERROR - " + project + "] -  " + error);
    }

    public static void warn(String error) {
        System.out.println("[WARNING] [WARNING - " + project + "] -  " + error);
    }

    public static void info(String str) {
        System.out.println("[INFO - " + project + "] -  " + str);
    }

    public static void npcPlays(Npc npc, Card card) {
        System.out.println("[CARD - " + project + "] -  " + npc.getName() + " ha jugado: " + card.toString());
        System.out.println("[CARD - " + project + "] -  A " + npc.getName() + " le quedan: " + npc.getDeck().size());
        System.out.println("");
    }

    public static void npcDraws(Npc npc, Card card) {
        System.out.println("[CARD - " + project + "] -  " + npc.getName() + " ha robado: " + card.toString());
        System.out.println("[CARD - " + project + "] -  A " + npc.getName() + " tiene: " + npc.getDeck().size());
        System.out.println("");
    }

}
