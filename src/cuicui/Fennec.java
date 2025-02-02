package cuicui;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fennec extends Service {

  private List<String> essencesList;
  private int timer;
  private static int compteur = 1;
  private int nbTronc;

  private int portPlanete;
  private DatagramSocket socket;

  public Fennec(int startingQuantity, int countdown) {

    this.portPlanete = config.getInt("portServeurPlaneteUDP");
    this.gestionnaireMessage = new Messenger("Fennec #" + compteur);

    this.nbTronc = startingQuantity;
    this.timer = countdown;

    this.essencesList = new ArrayList<>();

    this.essencesList.add("CHENE");
    this.essencesList.add("ACACIA");
    this.essencesList.add("SAPIN");
    compteur++;
  }

  public Fennec(int startingQuantity, int countdown, List<String> list) {
    this.portPlanete = config.getInt("portServeurPlaneteUDP");
    this.gestionnaireMessage = new Messenger("Fennec #" + compteur);

    this.nbTronc = startingQuantity;
    this.timer = countdown;

    this.essencesList = new ArrayList<>();
    this.essencesList.clear();
    this.essencesList.addAll(list);

    compteur++;
  }

  public int getQuantity() { return this.nbTronc; }

  public void run() {
    setup();
    String PlaneteKey = "Planete/publicPlanete.bin";
    MessengerUDP msg = new MessengerUDP();
    byte[] sent = null;
    LotDeBois current;

    try {
      while (!Thread.interrupted()) {

        for (String str : this.essencesList) {

          // Je préviens le Planete qui je suis.
          sent = encodeString("Fennec");
          sent = CryptedMessage.encryptMessage(sent, PlaneteKey);
          msg.sendMessage(socket, sent, portPlanete);

          // On envoie les lots de bois !
          current = new LotDeBois(new Tronc(str, randomOrigine()), nbTronc);

          gestionnaireMessage.afficheMessage("J'envoie un " + current);

          // On envoie le lot de bois.
          sent = encodeString(current.toJson().toString());
          sent = CryptedMessage.encryptMessage(sent, PlaneteKey);
          msg.sendMessage(socket, sent, portPlanete);
        }

        // gestionnaireMessage.afficheMessage("Lots envoyés !");

        // On "produit"...
        this.dodo(timer);
      }

    } finally {
      gestionnaireMessage.afficheMessage("Terminé.");
    }
  }

  private void setup() {
    try {
      socket = new DatagramSocket();
    } catch (SocketException e) {
      System.out.println("Erreur dans la créations des sockets : " + e);
      System.exit(1);
    }
  }

  private String randomOrigine() {
    String[] codes = {"FR", "NL", "IT", "ES", "BE", "DE"};
    Random random = new Random();
    int index = random.nextInt(codes.length);
    return codes[index];
  }
}
