package cuicui;

import org.json.*;

public class Commande {

  private String type_de_meuble;
  private String couleur;
  private String essence;
  private int quantite;

  public Commande(String type_de_meuble, String couleur, String essence,
                  int quantite) {
    this.type_de_meuble = type_de_meuble;
    this.couleur = couleur;
    this.essence = essence;
    this.quantite = quantite;
  }

  @Override
  public String toString() {
    return "Commande détaillée : " + this.type_de_meuble + ", " + this.couleur +
        ", " + this.essence + ", " + this.quantite;
  }

  public JSONObject toJSON() {

    var json = new JSONObject();

    json.put("essence", this.essence);
    json.put("couleur", this.couleur);
    json.put("type_de_meuble", this.type_de_meuble);
    json.put("quantite", this.quantite);

    return json;
  }

  public static Commande fromJSON(String json) {
    JSONObject jo = new JSONObject(json);

    String type_m = jo.getString("type_de_meuble");
    String coul = jo.getString("couleur");

    String ess = jo.getString("essence");

    int quant = jo.getInt("quantite");
    return new Commande(type_m, coul, ess, quant);
  }

  public String getTypeMeuble() { return this.type_de_meuble; }
  public String getCouleur() { return this.couleur; }
  public String getEssence() { return this.essence; }
  public int getQuantite() { return this.quantite; }
}
