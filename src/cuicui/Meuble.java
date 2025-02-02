package cuicui;

import org.json.*;

public class Meuble {

  private String type_de_meuble;
  private String essence;

  public Meuble(String type_de_meuble,String essence) {
    this.type_de_meuble = type_de_meuble;
    this.essence = essence;

  }

  @Override
  public String toString() {
    return "Meuble : " + this.type_de_meuble + ", " + this.essence;
  }

  public JSONObject toJSON() {

    var json = new JSONObject();

    json.put("essence", this.essence);
    json.put("type_de_meuble", this.type_de_meuble);

    return json;
  }

  public static Meuble fromJSON(String json) {
    JSONObject jo = new JSONObject(json);

    String type_m = jo.getString("type_de_meuble");

    String ess = jo.getString("essence");

    return new Meuble(type_m, ess);
  }

  public String getTypeMeuble() { return this.type_de_meuble; }
  public String getEssence() { return this.essence; }

}
