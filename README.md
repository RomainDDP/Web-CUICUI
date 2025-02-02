# Commandes pour la gestion de CUICUI

Toutes les commandes doivent être réalisées dans le dossier du Projet.

## Compilation

On vous recommande vivement d'utiliser le script python fournit nommé compil.py.
Celui-ci regardera sur quelle système d'exploitation vous vous trouvez avant de réaliser la compilation
Il va aussi s'occuper de générer les clés pour chaque service.

## Exécution

En ce qui concerne l'éxécution, il vous faudra éxécuter le Launcher en passant comme paramètre le fichier de config JSON fournit ainsi que le scénario que vous souhaitez tester.
 X étant le scénario de A à AC.

En ce qui concerne le serveur Web nécessaire, la commande suivante peut faire l'affaire.
<code> php -S localhost:8080 -t PHP/index.php </code>

### Linux
<code> java -cp cls:lib/json.jar cuicui.Lanceur config.json X </code>

### Windows
<code> java -cp "cls;lib\\json.jar" cuicui.Lanceur config.json X </code>

# Commandes pour la gestion de l'exemple

## Compilation et exécution dans un terminal

### Sous un système Unix (incluant aussi WSL)

+ compilation : <code> javac -d cls -cp lib/json.jar -sourcepath src src/cuicui/*.java </code>

+ exécution   : <code> java -cp cls:lib/json.jar cuicui.GenerationClesRSA </code>
+ exécution   : <code> java -cp cls:lib/json.jar cuicui.Lanceur config.json </code>

### Sous un système Windows

+ compilation : <code> javac -d cls -cp lib\\json.jar -sourcepath src src\\cuicui\\*.java </code>

+ exécution   : <code> java -cp "cls;lib\\json.jar" cuicui.GenerationClesRSA </code>
+ exécution   : <code> java -cp "cls;lib\\json.jar" cuicui.Lanceur config.json </code>

## Génération de la documentation

Pour générer la documentation (avec l'auteur, la version, les attributs privés et le lien vers la documentation de JAVA 21)

<code>javadoc -private -author -version -link https://docs.oracle.com/en/java/javase/21/docs/api/ -d doc -sourcepath src/ src/*.java -link http://stleary.github.io/JSON-java/ -cp lib/json.jar</code>
