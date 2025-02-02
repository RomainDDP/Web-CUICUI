<?php
include("includes/Commande_detail.php");
include("includes/Meuble.php");
session_start();
?>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CUISTO</title>
</head>
<body>
<h1>Cuisto</h1>
<form method="post" id="formulaire_commande">
    <h2>Choisissez votre meuble</h2>
        <select name="type_de_meuble" id="type_de_meuble">
            <option value = "Placard">Placard</option>
            <option value="Plan de travail">Plan de travail</option>
            <option value="Commode">Commode</option>
</select>
<select name="couleur" id="couleur">
            <option value = "Blanc">Blanc</option>
            <option value="Gris">Gris</option>
            <option value="Noir">Noir</option>
</select>
<select name="essence" id="essence">
            <option value = "CHENE">Chene</option>
            <option value="ACACIA">Accacia</option>
            <option value="SAPIN">Sapin</option>
</select>
<input type="number" id="quantite" name="quantite" placeholder="Quantité">

<input type="submit" value="Submit">
</form>

</body>

<?php

if(isset($_POST["type_de_meuble"])){
        $_SESSION["type_de_meuble"]=$_POST["type_de_meuble"];
}
if(isset($_POST["couleur"])){
    $_SESSION["couleur"]=$_POST["couleur"];
}
if(isset($_POST["essence"])){
    $_SESSION["essence"]=$_POST["essence"];
}
if(isset($_POST["quantite"])){
    $_SESSION["quantite"]=$_POST["quantite"];

}
if(isset($_SESSION['type_de_meuble'])){
$commande=new Commande_detail($_SESSION["type_de_meuble"],$_SESSION["couleur"],$_SESSION["essence"],$_SESSION["quantite"]);

$json=json_encode($commande);

$options = [
    'http' =>
        [
            'method'  => 'POST',
            'header'  => 'Content-type: application/x-www-form-urlencoded',
            'content' => $json
        ]
    ];
    $URL = "http://localhost:8082/message_reception";
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
