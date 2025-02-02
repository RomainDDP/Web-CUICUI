package cuicui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.xml.crypto.Data;

/*
 * Classe permettant l'envoi de données en UDP.
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */

public class MessengerUDP {

  private DatagramPacket packet = null;
  private InetAddress adresse = null;

  public MessengerUDP() {
    try {
      adresse = InetAddress.getByName(null);
    } catch (UnknownHostException e) {
      System.err.println("Erreur lors de la création du message : " + e);
    }
  }

  // Retourne le port du dernier DatagramPacket envoyé / reçu.
  // Retourne -1 si aucun DatagramPacket n'a été envoyé / reçu.
  public int getPortMessage() {
    int res = -1;

    if (packet != null)
      res = packet.getPort();

    return res;
  }

  public boolean sendMessage(DatagramSocket socket, byte[] data, int port) {
    boolean res = true;
    try {
      packet = new DatagramPacket(data, data.length, adresse, port);
      socket.send(packet);
    } catch (IOException e) {
      System.err.println("Erreur lors de la sérialisation : " + e);
      res = false;
    }
    return res;
  }

  public byte[] receiveMessage(DatagramSocket socket) {
    byte[] tampon = new byte[256];
    this.packet = new DatagramPacket(tampon, tampon.length);

    try {
      socket.receive(packet);
    } catch (IOException e) {
      System.err.println("Erreur lors de la récupération du message : " + e);
    }

    return packet.getData();
  }
}
