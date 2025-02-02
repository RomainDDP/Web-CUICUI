package cuicui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/*
 * Classe représentant le service Horus,
 * Communique en TCP avec le service Planete
 *
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */
public class Horus extends Service {

  private int portServer;
  private Socket socketClient;
  private ServerSocket socketServeur;

  private ObjectOutputStream oos;
  private ObjectInputStream oins;

  private Map<String, byte[]> CRADO;

  private Signature sign;

  public Horus() {
    super();

    CRADO = new HashMap<>();
    gestionnaireMessage = new Messenger("HORUS");
    portServer = config.getInt("portServeurHorus");
  }

  public void run() {
    setup();
    try {
      while (!Thread.interrupted()) {
        Socket clientSocket = socketServeur.accept();
        Thread clientHandler = new Thread(() -> handleClient(clientSocket));
        clientHandler.start();
      }
    } catch (IOException e) {
      System.err.println("HORUS : Erreur acceptation client : " + e);
    } finally {
      closeAll();
      gestionnaireMessage.afficheMessage("Terminé.");
    }
  }

  private void handleClient(Socket clientSocket) {

    try {
      oins = new ObjectInputStream(clientSocket.getInputStream());
      oos = new ObjectOutputStream(clientSocket.getOutputStream());

    } catch (IOException e) {
      System.err.println("HORUS : Problème avec l'ObjectStream : " + e);
    }

    // On prépare la Signature puis on signe le LDB
    initSignature();
    LotDeBois ldb = readLotDeBois();
    byte[] data = signLotDeBois(ldb);

    // On stock le CRADO du LDB
    String key = ldb.getIdentifiant();
    CRADO.put(key, data);
    gestionnaireMessage.afficheMessage("CRADO du " + ldb.toString() + " signé");
  }

  private void setup() {
    try {
      socketServeur = new ServerSocket(portServer);
    } catch (IOException e) {
      System.err.println("HORUS : Création de la socket impossible : " + e);
      System.exit(0);
    }
  }

  private LotDeBois readLotDeBois() {

    byte[] data;
    LotDeBois ldb = null;

    // On récupère le LDB envoyé
    try {
      data = (byte[])oins.readObject();
      data = CryptedMessage.decryptMessage(data, "Horus/privateHorus.bin");
      ldb = LotDeBois.fromJson(decodeString(data));
    } catch (ClassNotFoundException e) {
      System.err.println("Erreur de classe" + e);
    } catch (IOException e) {
      System.err.println("Erreur de lecture : " + e);
    }
    return ldb;
  }

  private void closeAll() {
    try {
      socketClient.close();
      socketServeur.close();
    } catch (IOException e) {
      System.err.println(
          "Erreur lors de la fermeture des flux et des sockets : " + e);
      System.exit(0);
    }
  }

  private void initSignature() {

    PrivateKey clePrivee =
        GestionClesRSA.lectureClePrivee("Horus/privateHorus.bin");

    try {
      sign = Signature.getInstance("SHA256withRSA");
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Erreur lors de l'init de la signature : " + e);
    }

    try {
      sign.initSign(clePrivee);
    } catch (InvalidKeyException e) {
      System.err.println("Clé privée invalide : " + e);
    }
  }

  private byte[] serialize(LotDeBois value) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try (ObjectOutputStream outputStream = new ObjectOutputStream(out)) {
      outputStream.writeObject(value);
    } catch (IOException e) {
      System.err.println("Problème de sérialisation : " + e);
    }

    return out.toByteArray();
  }

  private byte[] signLotDeBois(LotDeBois ldb) {

    byte[] data = serialize(ldb);

    try {
      sign.update(data, 0, data.length);
      data = sign.sign();
    } catch (SignatureException e) {
      System.err.println("Erreur lors de la maj de la signature : " + e);
    }

    return data;
  }
}
