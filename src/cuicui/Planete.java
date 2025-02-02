package cuicui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * Classe représentant le service Planete.
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */

public class Planete extends Service {

  private DatagramSocket socketUDP;
  private Socket socketTCP;

  private int portClientUDP;
  private int portServerUDP;
  private int portServerTCP;

  private ObjectOutputStream oos;
  private ObjectInputStream oins;
  private MessengerUDP msg;

  private Map<String, List<LotDeBois>> stock;

  public Planete() {
    super();
    gestionnaireMessage = new Messenger("PLANETE");
    msg = new MessengerUDP();

    portServerUDP = config.getInt("portServeurPlaneteUDP");
    portServerTCP = config.getInt("portServeurHorus");

    stock = new HashMap<>();
    stock.put("SAPIN", new ArrayList<>());
    stock.put("CHENE", new ArrayList<>());
    stock.put("ACACIA", new ArrayList<>());
  }

  public void run() {

    setup();
    String privatePlanete = "Planete/privatePlanete.bin";
    String publicHorus = "Horus/publicHorus.bin";
    String publicRevenant = "Revenant/publicRevenant.bin";
    boolean cannotFillCommand = false;
    byte[] sent = null, received = null, data = null;
    String tmp;
    Commande cmd = null;

    try {
      while (!Thread.interrupted()) {
        received = msg.receiveMessage(socketUDP);

        received = CryptedMessage.decryptMessage(received, privatePlanete);
        tmp = decodeString(received);

        if (tmp.equals("Fennec")) {
          gestionnaireMessage.afficheMessage("Fennec envoie un Lot de Bois");

          received = msg.receiveMessage(socketUDP);
          data = CryptedMessage.decryptMessage(received, privatePlanete);

          LotDeBois current = LotDeBois.fromJson(decodeString(data));
          gestionnaireMessage.afficheMessage("J'ai reçu un " + current);

          // On envoie à Horus, il s'occupe de signer le CRADO.
          data = CryptedMessage.encryptMessage(data, publicHorus);
          sendToHorus(data);
          gestionnaireMessage.afficheMessage("J'ai envoyé le Lot à Horus.");

          // On oublie pas d'ajouter notre lDB à notre stock
          addToStock(current);

          // On a une commande en attente !
          if (cannotFillCommand) {
            if (checkQuantityInStock(cmd.getEssence(), cmd.getQuantite())) {
              gestionnaireMessage.afficheMessage(
                  "J'ai la quantité suffisante ! ");
              sendCommand(cmd, portClientUDP);
              cannotFillCommand = false;
            }
          }
        } else if (tmp.equals("Revenant")) {
          gestionnaireMessage.afficheMessage("Revenant envoie une commande");
          portClientUDP = msg.getPortMessage();

          if (cannotFillCommand) {
            gestionnaireMessage.afficheMessage(
                "J'ai déjà une commande en cours, il va attendre un peu.");
            data = encodeString("KO");
            data = CryptedMessage.encryptMessage(data, publicRevenant);
            msg.sendMessage(socketUDP, data, portClientUDP);
          }
          received = msg.receiveMessage(socketUDP);

          data = CryptedMessage.decryptMessage(received, privatePlanete);
          cmd = Commande.fromJSON(decodeString(data));
          gestionnaireMessage.afficheMessage(cmd.toJSON().toString());
          if (checkQuantityInStock(cmd.getEssence(), cmd.getQuantite())) {
            sendCommand(cmd, portClientUDP);
          } else {
            cannotFillCommand = true;
          }

        } else {
          gestionnaireMessage.afficheMessage(
              "Wtf c qui ? je panique je m'arrête !");
          System.exit(1);
        }
      }
    } finally {
      closeAll();
      gestionnaireMessage.afficheMessage("Terminé.");
    }
  }

  private void sendCommand(Commande cmd, int port) {
    List<LotDeBois> toRevenant = dealWithCommand(cmd);
    byte[] sent = null;

    for (LotDeBois ldb : toRevenant) {
      sent = encodeString(ldb.toJson().toString());
      sent = CryptedMessage.encryptMessage(sent, "Revenant/publicRevenant.bin");
      msg.sendMessage(socketUDP, sent, port);
    }
    sent = encodeString("DONE");
    sent = CryptedMessage.encryptMessage(sent, "Revenant/publicRevenant.bin");
    msg.sendMessage(socketUDP, sent, port);
  }

  private List<LotDeBois> dealWithCommand(Commande cmd) {
    int quantity = cmd.getQuantite();
    int current = 0;
    String essence = cmd.getEssence();

    List<LotDeBois> res = new ArrayList<>();
    Iterator<LotDeBois> iter = stock.get(essence).iterator();

    while (current < quantity) {
      LotDeBois tmp = iter.next();
      current += tmp.getQuantite();

      res.add(tmp);
      iter.remove();
    }

    return res;
  }

  private boolean checkQuantityInStock(String essence, int quantity) {
    List<LotDeBois> list = stock.get(essence);
    boolean res = false;
    int current = 0;

    if (list != null && !list.isEmpty()) {
      for (LotDeBois ldb : list) {
        current += ldb.getQuantite();
        if (current >= quantity) {
          res = true;
          break;
        }
      }
    }

    return res;
  }

  private void setup() {

    // Setup de la connexion UDP
    try {
      this.socketUDP = new DatagramSocket(this.portServerUDP);
    } catch (SocketException e) {
      System.out.println("Erreur dans la créations de la socket UDP : " + e);
      System.exit(1);
    }

    // Setup de la connexion TCP

    try {
      socketTCP = new Socket("localhost", this.portServerTCP);
    } catch (UnknownHostException e) {
      System.err.println("Erreur sur l'hôte : " + e);
      System.exit(0);
    } catch (IOException e) {
      System.err.println("PLANETE : Création de la socket TCP impossible : " +
                         e);
      System.exit(0);
    }

    // Création de l'ObjectStream
    try {
      oos = new ObjectOutputStream(socketTCP.getOutputStream());
      oins = new ObjectInputStream(socketTCP.getInputStream());
    } catch (IOException e) {
      System.err.println("Erreur de l'ObjectSream " + e);
    }
  }

  private void closeAll() {
    try {
      socketUDP.close();
      socketTCP.close();
    } catch (IOException e) {
      System.err.println(
          "Erreur lors de la fermeture des flux et de la socket : " + e);
      System.exit(0);
    }
  }

  private void sendToHorus(byte[] data) {

    try {
      oos.writeObject(data);
      oos.flush();
    } catch (IOException e) {
      System.err.println("Erreur lors de la sérialisation : " + e);
    }
  }

  private void addToStock(LotDeBois ldb) {
    List<LotDeBois> list = stock.get(ldb.getEssence());
    list.add(ldb);
  }
}
