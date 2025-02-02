package cuicui;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import org.json.*;

/**
 * @author Romain Dupont & Antoine Théologien
 */
class MessageHandler implements HttpHandler {

  boolean cuisto;
  Commande commande_detail = null;
  HttpExchange exchange = null;

  public MessageHandler(boolean cuisto) { this.cuisto = cuisto; }

  public void handle(HttpExchange exchange) {

    this.exchange = exchange;
    String query = null;
    // Utilisation d'un flux pour lire les données du message Http
    BufferedReader br = null;
    try {
      br = new BufferedReader(
          new InputStreamReader(exchange.getRequestBody(), "utf-8"));
    } catch (UnsupportedEncodingException e) {
      System.err.println("Erreur lors de la récupération du flux " + e);
      System.exit(1);
    }

    // Récupération des données en POST
    try {
      query = br.readLine();
    } catch (IOException e) {
      System.err.println("Erreur lors de la lecture d'une ligne " + e);
      System.exit(1);
    }

    String answer = null;

    if (cuisto) {
      JSONObject jo = new JSONObject(query);
      int val = getQuantityFromType(jo.getString("type_de_meuble"));
      val *= jo.getInt("quantite");
      jo.put("quantite", val);
      query = jo.toString();
      commande_detail = Commande.fromJSON(query);
    } else
      setCommande(query);
  
	Meuble meuble = new Meuble(commande_detail.getTypeMeuble(),commande_detail.getEssence());
	
    answer = meuble.toJSON().toString();

    try {
      Headers h = exchange.getResponseHeaders();
      h.set("Content-Type", "application/json; charset=utf-8");
      exchange.sendResponseHeaders(200, answer.getBytes().length);
    } catch (IOException e) {
      System.err.println("Erreur lors de l'envoi de l'en-tête : " + e);
      System.exit(0);
    }

    // Envoi du corps (données HTML)
    try {
      OutputStream os = exchange.getResponseBody();
      os.write(answer.getBytes());
      os.close();
    } catch (IOException e) {
      System.err.println("Erreur lors de l'envoi du corps : " + e);
    }
  }

  public Commande getCommande() {

    Commande tmp = this.commande_detail;
    this.commande_detail = null;
    return tmp;
  }

  private void setCommande(String query) {
    JSONObject jo = new JSONObject(query);
    String type = jo.getString("cuisine");

    switch (type) {

    case "Cuisine_chene_noir":
      commande_detail = new Commande("plan_de_travail", "noir", "CHENE", 50);
      break;

    case "Cuisine_accacia_bleue":
      commande_detail = new Commande("placard", "bleu", "ACACIA", 50);
      break;

    case "Cuisine_sapin_rouge":
      commande_detail = new Commande("tiroir", "rouge", "SAPIN", 50);
      break;

    default:
      commande_detail = null;
      System.err.println("Erreur dans le type de cuisine");
      System.exit(1);
    }
  }
  private static int getQuantityFromType(String type) {
    int res;

    switch (type) {
    case "Placard":
      res = 20;
      break;

    case "Commode":
      res = 30;
      break;

    case "Plan de travail":
      res = 40;
      break;

    default:
      res = 0;
      break;
    }

    return res;
  }
}
