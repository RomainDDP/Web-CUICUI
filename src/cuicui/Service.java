package cuicui;
import java.util.Base64;

/*
 * Classe dont va hériter tous nos services.
 * Rassemble toutes les fonctionnalités qu'ils peuvent partager.
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */

public abstract class Service implements Runnable {

  protected static Configuration config;
  protected Messenger gestionnaireMessage;

  protected Service() { System.out.println("Service démarré."); }

  public static void setupConfig(String path) {
    config = new Configuration(path);
  }

  protected byte[] encodeString(String message) {
    return Base64.getEncoder().encode(message.getBytes());
  }
  protected String decodeString(byte[] data) {
    return new String(Base64.getDecoder().decode(data));
  }

  protected void dodo(int seconds) {
    int ms = seconds * 1000;
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
    }
  }
}
