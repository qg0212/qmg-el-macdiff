Développement d'un solveur de contraintes, dédié aux contraintes de différence.

Pour compiler le projet, se placer dans le répertoire 'macdiff' et taper la commande suivante : mvn package

Pour lancer l'application, depuis le répertoire 'macdiff', taper la commande : java -jar target/macdiff-2.1.jar [-s solveur] [-a] -f fichier

	L'option '-s', facultavive, permet de choisir le solveur à utiliser ("learning" pour le solveur avec apprentissage, "basic" pour la version basique)
		Si l'option n'est pas donnée, le seulveur basique est utilisé par défaut

	L'option '-a', facultative, permet d'indiquer qu'on veut toutes les solutions
		Si l'option n'est pas donnée, le solveur s'arrête dès qu'il trouve une solution

	L'option '-f', obligatoire, indique le chemin vers le fichier d'instance à résoudre



Exemple : pour obetenir toutes les solutions du réseau décrit dans le fichier 'src/main/resources/golomb.i', avec le solveur avec apprentissage, il faut taper les instructions suivantes :

	cd macdiff
	mvn package
	java -jar target/macdiff-2.1.jar -s learning -a -f src/main/resources/golomb.i