package cuicui;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * Lanceur pour démarrer les services nécessaire suivant le scénario donné.
 *
 * @author Jean-Charles BOISSON 2022-2023 + Romain DUPONT & Antoine THÉOLOGIEN
 *
 *
 */
public class Lanceur {

  private static List<Thread> threads;

  /**
   * Constructeur par défaut (pour éviter un warning lors de la génération de
   * la documentation)
   */
  Lanceur() {}

  /**
   * @param args Les arguments de la ligne de commande notamment pour fournir le
   *     chemin vers le fichier de configuration json.
   */
  public static void main(String[] args) {

    threads = new ArrayList<>();

    if (args.length < 2) {
      System.out.println(
          "Merci de donner un fichier de configuration json ainsi que le scénario choisi. ");
      System.exit(0);
    }

    Service.setupConfig(args[0]);
    String scenario = args[1];

    switch (scenario) {

    case "A":
      scenarioA();
      break;
    case "B":
      scenarioB();
      break;
    case "C":
      scenarioC();
      break;
    case "D":
      scenarioD();
      break;
    case "A2":
      scenarioA2();
      break;
    case "AC":
      scenarioAC();
      break;

    default:
      System.out.println("Ce scénario n'existe pas.");
      System.exit(1);
    }

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Arrêt de l'utilisateur, on arrête CUICUI...");
      stopAll();
    }));
  }

  private static void scenarioA() {

    threads.add(new Thread(new Planete()));
    threads.add(new Thread(new Fennec(50, 10)));
    threads.add(new Thread(new Horus()));
    threads.add(new Thread(new Revenant(false)));

    for (Thread th : threads) {
      th.start();
    }
  }

  private static void scenarioB() {
    threads.add(new Thread(new Planete()));
    threads.add(new Thread(new Fennec(10, 5)));
    threads.add(new Thread(new Horus()));
    threads.add(new Thread(new Revenant(false)));

    for (Thread th : threads) {
      th.start();
    }
  }

  private static void scenarioC() {

    List<String> ess = new ArrayList<>();
    ess.add("CHENE");
    ess.add("SAPIN");

    threads.add(new Thread(new Planete()));
    threads.add(new Thread(new Fennec(50, 999, ess)));

    threads.add(new Thread(new Horus()));
    threads.add(new Thread(new Revenant(false)));

    for (Thread th : threads) {
      th.start();
    }

    ess.clear();
    ess.add("ACACIA");
    Thread fen2 = new Thread(new Fennec(50, 999, ess));
    dodo(3);
    threads.add(fen2);
    fen2.start();
  }

  private static void scenarioD() {}

  private static void scenarioA2() {}

  private static void scenarioAC() {
	 threads.add(new Thread(new Planete()));
    threads.add(new Thread(new Fennec(50, 10)));
    threads.add(new Thread(new Horus()));
    threads.add(new Thread(new Revenant(true)));

    for (Thread th : threads) {
      th.start();
    }
  }

  private static void stopAll() {
    for (Thread th : threads) {
      th.interrupt();
    }
  }
  /*
   * Vérifie si un port est disponible en tentant de se connecter dessus pour
   * une courte période.
   * @param port Le port dont on souhaite vérifier la disponibilité
   * @return true si le port est disponible, false sinon.
   */
  public static boolean isPortDisponible(int port) {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress("localhost", port), 1000);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static void dodo(int seconds) {
    int ms = seconds * 1000;
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
    }
  }
}
