<?php
include ("includes/Commande.php");
include ("includes/Meuble.php");
session_start();
?>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Revendeur</title>
</head>
<body>
<h1>Revendeur</h1>
<h2><a href="cuisto.php">Lien vers le cuisto</a></h2>
<form method="post">
    <h1>Choisissez votre cuisine<h1>
        <select name="cuisine" id="cuisine">
            <option value = "Cuisine_chene_noir">Cuisine en chêne noire</option>
            <option value="Cuisine_accacia_bleue">Cuisine en accacia bleue</option>
            <option value="Cuisine_sapin_rouge">Cuisine en sapin rouge</option>
</select>
<input type="submit" value="Submit">
</form>


</body>

<?php
if(isset($_POST["cuisine"])){
        $_SESSION["cuisine"]=$_POST["cuisine"];
}
if(isset($_SESSION['cuisine'])){
$commande=new Commande($_SESSION["cuisine"]);
$json=json_encode($commande);
$options = [
    'http' =>
        [
            'method'  => 'POST',
            'header'  => 'Content-type: application/x-www-form-urlencoded',
            'content' => $json
        ]
    ];
    $URL = "http://localhost:8081/message_reception";
    $contexte  = stream_context_create($options);

    if(($jsonTexte = @file_get_contents($URL, false, $contexte)) !== false) {
    
        echo "<br/>";
        echo "<br/>";
        $meuble=Meuble::fromJSON($jsonTexte);
        echo "Le meuble envoyé au revendeur est : ";
        echo $meuble;

    }
    else {
        echo "<p>ERREUR : Une erreur est survenue lors de la récupération des données.</p>";
        echo "<p>ERREUR : Vérifiez notamment les ports de vos serveur et l'URL choisie pour la requête.<p>";
    }
}
session_destroy();
?>
</html>
