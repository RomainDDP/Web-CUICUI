package cuicui;

import java.io.Serializable;
import org.json.*;
/**
 * Classe représentant un Tronc
 *
 * @author Romain DUPONT & Antoine THÉOLOGIEN
 */
public class Tronc implements Serializable {

  /*
   * FR = FRance, NL = NetherLand... Même chose que les plaques
   * d'immatriculation
   */
  private String origine;
  /*
   * CH = chêne, SA = sapin, AC = Acacia, BO = Bouleau
   */
  private String essence;

  public Tronc() {
    this.origine = "FR";
    this.essence = "CH";
  }

  public Tronc(String essence, String origine) {
    this.origine = origine;
    this.essence = essence;
  }

  public String getOrigine() { return origine; }
  public String getEssence() { return essence; }

  public String toString() {
    return "Tronc d'origine : " + origine + " et d'essence : " + essence;
  }
  public JSONObject toJson() {
    JSONObject jo = new JSONObject();
    jo.put("Essence", this.essence);
    jo.put("Origine", this.origine);
    return jo;
  }

  public static Tronc fromJson(String str) {
    JSONObject jo = new JSONObject(str);

    return new Tronc(jo.getString("Essence"), jo.getString("Origine"));
  }
}
