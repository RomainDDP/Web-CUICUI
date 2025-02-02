package cuicui;

import java.io.Serializable;
import java.util.Random;
import org.json.*;
/**
 * Classe représentant un Code de Suivi, qui est utilisé pour les transactions
 * d'un Lot De Bois.
 *
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */

public class CodeDeSuivi implements Serializable {

  // type de Tronc correspondant.
  Tronc typeTronc;

  // Généré aléatoirement, composé de 6 caractères.
  private String ID;

  public CodeDeSuivi(Tronc c) {
    this.typeTronc = c;
    this.ID = randomIDGenerator();
  }

  private CodeDeSuivi(Tronc c, String id) {
    this.typeTronc = c;
    this.ID = id;
  }

  public String getOrigine() { return typeTronc.getOrigine(); }
  public String getEssence() { return typeTronc.getEssence(); }
  public String getID() { return ID; }

  public String toString() {
    return this.getOrigine() + "-" + this.getEssence() + "-" + this.ID;
  }
  public JSONObject toJson() {

    JSONObject jo = new JSONObject();
    jo.put("ID", this.ID);
    jo.put("TypeTronc", this.typeTronc.toJson().toString());

    return jo;
  }

  public static CodeDeSuivi fromJson(String str) {
    JSONObject jo = new JSONObject(str);

    Tronc trc = Tronc.fromJson(jo.getString("TypeTronc"));
    CodeDeSuivi res = new CodeDeSuivi(trc, jo.getString("ID"));

    return res;
  }
  // Génère un Identifiant aléatoire pour le CodeDeSuivi
  private static String randomIDGenerator() {
    String str = "";
    Random random = new Random();

    for (int i = 0; i < 6; i++) {
      // Génère une lettre majuscule aléatoire (A-Z) et l'ajoute
      char randomChar = (char)(random.nextInt(26) + 'A');
      str += randomChar;
    }

    return str;
  }
}
