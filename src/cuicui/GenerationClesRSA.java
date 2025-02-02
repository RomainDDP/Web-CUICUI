package cuicui;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 * Classe permettant de générer une paire de clés privée/publique et de les
 * sauvegarder dans des fichiers.
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */
public class GenerationClesRSA {

  /**
   * Méthode principale.
   */
  public static void main(String[] args) {
    // Création d'un générateur RSA
    KeyPairGenerator generateurCles = null;
    try {
      generateurCles = KeyPairGenerator.getInstance("RSA");
      generateurCles.initialize(2048);
    } catch (NoSuchAlgorithmException e) {
      System.err.println(
          "Erreur lors de l'initialisation du générateur de clés : " + e);
      System.exit(-1);
    }

    // Génération de la paire de clés Planete
    KeyPair paireCles = generateurCles.generateKeyPair();

    GestionClesRSA.sauvegardeClePrivee(paireCles.getPrivate(),
                                       "Planete/privatePlanete.bin");
    GestionClesRSA.sauvegardeClePublique(paireCles.getPublic(),
                                         "Planete/publicPlanete.bin");

    // Génération de la paire de clés Horus
    paireCles = generateurCles.generateKeyPair();

    GestionClesRSA.sauvegardeClePrivee(paireCles.getPrivate(),
                                       "Horus/privateHorus.bin");
    GestionClesRSA.sauvegardeClePublique(paireCles.getPublic(),
                                         "Horus/publicHorus.bin");

    // Génération de la paire de clés Horus
    paireCles = generateurCles.generateKeyPair();

    GestionClesRSA.sauvegardeClePrivee(paireCles.getPrivate(),
                                       "Revenant/privateRevenant.bin");
    GestionClesRSA.sauvegardeClePublique(paireCles.getPublic(),
                                         "Revenant/publicRevenant.bin");
    System.out.println("Clés générés.");
  }
}
