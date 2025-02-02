package cuicui;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Classe représentant un message crypté, utilisé pour les transactions
 * entre un Planete et Horus.
 *
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */

public class CryptedMessage implements Serializable {

  public static byte[] encryptMessage(byte[] data, String pathToPublic) {

    byte[] res = null;
    PublicKey clePublique = GestionClesRSA.lectureClePublique(pathToPublic);
    // Chiffrement du message
    try {
      Cipher chiffreur = Cipher.getInstance("RSA");
      chiffreur.init(Cipher.ENCRYPT_MODE, clePublique);
      res = chiffreur.doFinal(data);
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Erreur lors du chiffrement : " + e);
    } catch (NoSuchPaddingException e) {
      System.err.println("Erreur lors du chiffrement : " + e);
    } catch (InvalidKeyException e) {
      System.err.println("Erreur lors du chiffrement : " + e);
    } catch (IllegalBlockSizeException e) {
      System.err.println("Erreur lors du chiffrement : " + e);
    } catch (BadPaddingException e) {
      System.err.println("Erreur lors du chiffrement : " + e);
    }

    return res;
  }

  public static byte[] decryptMessage(byte[] data, String pathToPrivate) {

    PrivateKey clePrivee = GestionClesRSA.lectureClePrivee(pathToPrivate);
    byte[] res = null;

    // Déchiffrement du message
    try {
      Cipher dechiffreur = Cipher.getInstance("RSA");
      dechiffreur.init(Cipher.DECRYPT_MODE, clePrivee);
      res = dechiffreur.doFinal(data);
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Erreur lors du déchiffrement : " + e);
    } catch (NoSuchPaddingException e) {
      System.err.println("Erreur lors du déchiffrement : " + e);
    } catch (InvalidKeyException e) {
      System.err.println("Erreur lors du déchiffrement : " + e);
    } catch (IllegalBlockSizeException e) {
      System.err.println("Erreur lors du déchiffrement : " + e);
    } catch (BadPaddingException e) {
      System.err.println("Erreur lors du déchiffrement : " + e);
    }

    return res;
  }
}
