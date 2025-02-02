package cuicui;

/**
 * Classe pour gérer l'affichage de messages dans System.out avec un préfixe.
 *
 * @author Jean-Charles BOISSON 11/2022
 *
 * @version 1.0
 *
 */
public class Messenger {

  /** Identité enregistrée pour l'instance en cours (non modifiable).*/
  private final String id;

  /** Délimiteur mis avant l'identité (non modifiable).*/
  private final char delimAV;

  /** Délimiteur mis après l'identité (non modifiable).*/
  private final char delimAP;

  /**
   * Préfixe utilisé selon le <em>pattern</em> "delimAV id delimAP : " (généré
   * à la construction et non modifiable).
   */
  private final String prefix;

  /**
   * Constructeur pour renseigner l'identité choisie avec les délimiteurs "[" et
   * "]"
   *
   * @param id Le nom choisi pour l'identité
   */
  public Messenger(String id) {
    this.id = id;
    this.delimAV = '[';
    this.delimAP = ']';
    this.prefix = this.delimAV + " " + this.id + " " + delimAP + " : ";
  }

  /**
   * Constructeur pour renseigner l'identité choisie avec des délimiteurs
   * choisis (identiques)
   *
   * @param id Le nom choisi pour l'identité
   * @param delim Le délimiteur qui sera au début et à la fin
   */
  public Messenger(String id, char delim) {
    this.id = id;
    this.delimAV = this.delimAP = delim;
    this.prefix = this.delimAV + " " + this.id + " " + delimAP + " : ";
  }

  /**
   * Constructeur pour renseigner l'identité choisie avec des délimiteurs
   * choisis
   *
   * @param id Le nom choisi pour l'identité
   * @param delimAV Le délimiteur de début (donc avant le message)
   * @param delimAP Le délimiteur de fin (donc après le message)
   */
  public Messenger(String id, char delimAV, char delimAP) {
    this.id = id;
    this.delimAV = delimAV;
    this.delimAP = delimAP;
    this.prefix = this.delimAV + " " + this.id + " " + delimAP + " : ";
  }

  /**
   * Permet d'afficher un message préfixé (selon la configuration choisie à la
   * construction).
   *
   * @param message Le message à afficher sur la sortie standard
   */
  public void afficheMessage(String message) {
    System.out.println(prefix + message);
  }

  /**
   * Redéfinition du toString d'Object pour indiquer la configuration actuelle
   *
   * @return La configuration actuelle du Messenger
   */
  @Override
  public String toString() {

    String configuration = "\nConfiguration actuelle :\n";
    configuration += "\tIdentité         = " + this.id + "\n";
    configuration += "\tDélimiteur avant = " + this.delimAV + "\n";
    configuration += "\tDélimiteur après = " + this.delimAP + "\n";
    configuration += "\tPréfix résultant = " + this.prefix + "\n";

    return configuration;
  }
}
