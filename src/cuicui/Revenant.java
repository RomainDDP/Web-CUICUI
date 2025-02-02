package cuicui;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import org.json.*;

/*
 * Classe représentant le service Revenant.
 * Communique en UDP avec le service Planete.
 *
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */

public class Revenant extends Service {

  private static int compteur = 1;
  private boolean cuisto;

  private MessengerUDP msg;
  private int portPlanete = -1;
  private int portHttp = -1;

  private DatagramSocket socket = null;
  private HttpServer server = null;
  private MessageHandler handler;

  public Revenant(boolean cuisto) {
    super();

    this.cuisto = cuisto;
    this.portPlanete = config.getInt("portServeurPlaneteUDP");

    if (!this.cuisto) {
      this.portHttp = config.getInt("portServeurRevenantHttp");
    } else {
      this.portHttp = config.getInt("portCuisto");
    }
	

    msg = new MessengerUDP();
    handler = new MessageHandler(this.cuisto);
    gestionnaireMessage = new Messenger("REVENANT #" + compteur++);
  }

  public void run() {

    this.setup();
    byte[] sent = null, received = null;
    Commande cmd = null;
    String publicPlanete = "Planete/publicPlanete.bin";
    String privateRevenant = "Revenant/privateRevenant.bin";
    String tmp = null;
    boolean commandDone = false;

    try {
      while (!Thread.interrupted()) {

        this.dodo(10);
        cmd = handler.getCommande();

        if (cmd == null) {
          gestionnaireMessage.afficheMessage("Pas de commande.");
          continue;
        }

        gestionnaireMessage.afficheMessage("Commande reçu : ");
        gestionnaireMessage.afficheMessage(cmd.toString());

        sent = encodeString("Revenant");
        sent = CryptedMessage.encryptMessage(sent, publicPlanete);
        msg.sendMessage(socket, sent, portPlanete);

        sent = encodeString(cmd.toJSON().toString());
        sent = CryptedMessage.encryptMessage(sent, publicPlanete);
        msg.sendMessage(socket, sent, portPlanete);

        // Se bloque au deuxième tour en l'attente d'un message.
        while (!commandDone) {

          received = msg.receiveMessage(socket);
          received = CryptedMessage.decryptMessage(received, privateRevenant);
          tmp = decodeString(received);

          if (tmp.equals("KO")) {
            gestionnaireMessage.afficheMessage(
                "Une commande est déjà en cours, j'attends un peu. ");
          } else {
            LotDeBois cur = LotDeBois.fromJson(tmp);
            gestionnaireMessage.afficheMessage("Lot de bois reçu : ");
            gestionnaireMessage.afficheMessage(cur.toString());
            dealWithCommand(tmp);
            gestionnaireMessage.afficheMessage("La commande a été remplie !");
            commandDone = true;
          }
        }
      }
    } finally {
      closeAll();
      gestionnaireMessage.afficheMessage("Terminé.");
    }
  }

  private void dealWithCommand(String tmp) {

    byte[] received = null;

    while (!tmp.equals("DONE")) {
      received = msg.receiveMessage(socket);
      received = CryptedMessage.decryptMessage(received,
                                               "Revenant/privateRevenant.bin");
      tmp = decodeString(received);

      if (tmp.equals("DONE")) {
        break;
      }
      LotDeBois cur = LotDeBois.fromJson(tmp);
      gestionnaireMessage.afficheMessage("Lot de bois reçu : ");
      gestionnaireMessage.afficheMessage(cur.toString());
    }
  }

  private void setup() {

    // Setup du serveur HTTP
    try {
      server = HttpServer.create(new InetSocketAddress(portHttp),
                                 0); // port à changer en fonction du scénario
    } catch (IOException e) {
      System.err.println("Erreur lors de la création du serveur " + e);
      System.exit(1);
    }

    server.createContext("/message_reception", handler);
    server.setExecutor(null);
    server.start();

    // Setup du serveur UDP
    try {
      socket = new DatagramSocket();
    } catch (SocketException e) {
      System.out.println("Erreur dans la créations des sockets : " + e);
      System.exit(1);
    }
  }

  private void closeAll() {
    socket.close();
    server.stop(0);
  }
}
