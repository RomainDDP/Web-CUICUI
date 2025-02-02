package cuicui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.json.*;

public class LotDeBois implements Serializable, Comparable<LotDeBois> {

  private CodeDeSuivi code;
  private int quantite;

  public LotDeBois(Tronc typeTronc, int quantite) {
    this.code = new CodeDeSuivi(typeTronc);
    this.quantite = quantite;
  }
  private LotDeBois(CodeDeSuivi cds, int quantite) {
    this.code = cds;
    this.quantite = quantite;
  }

  public String getOrigine() { return code.getOrigine(); }
  public String getEssence() { return code.getEssence(); }
  public int getQuantite() { return this.quantite; }
  public String getIdentifiant() {
    return this.code.toString() + "-" + this.quantite;
  }

  public String toString() {
    return "Lot de bois d'identifiant : " + this.getIdentifiant();
  }

  public JSONObject toJson() {

    JSONObject jo = new JSONObject();
    jo.put("CodeDeSuivi", this.code.toJson());
    jo.put("Quantite", this.quantite);

    return jo;
  }

  public static LotDeBois fromJson(String str) {
    JSONObject jo = new JSONObject(str);

    CodeDeSuivi cds =
        CodeDeSuivi.fromJson(jo.getJSONObject("CodeDeSuivi").toString());
    LotDeBois res = new LotDeBois(cds, jo.getInt("Quantite"));

    return res;
  }

  @Override
  public int compareTo(LotDeBois obj) {
    if (this.quantite == obj.quantite)
      return 0;

    if (this.quantite > obj.quantite)
      return 1;
    else
      return -1;
  }
}
