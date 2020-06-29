<?php
    $login = "root";
    $mdp = "";
    $bd = "neige_app";
    $serveur = "localhost";

    try {
        $conn = new PDO("mysql:host=$serveur;dbname=$bd;port=3308", $login, $mdp);
        return $conn;
    } catch (PDOException $e) {
        print "Erreur de connexion PDO.";
        die();
    }

    $results["error"] = false;
    $results["message"] = [];

    if (!empty($_POST)) {
        if (!empty($_POST['nom_utilisateur']) && !empty($_POST['mdp'])) {
            $nom_utilisateur = $_POST["nom_utilisateur"];
            $password = $_POST["mdp"];
            
            $sql = $db->prepare("SELECT * FROM utilisateurs WHERE nom_utilisateur = :nom_utilisateur");
            $sql->execute([":nom_utilisateur" => $nom_utilisateur]);
            $row = $sql->fetch(PDO::FETCH_OBJ);

            if ($row) {
                if (password_verify($mdp, $row->mdp)) {
                    $results["error"] = false;
                    $results["id"] = $row->id;
                    $results["pseudo"] = $row->nom_utilisateur;
                } else {
                    $results["error"] = true;
                    $results["message"] = "Nom d'utilisateur et/ou mot de passe incorrect(s).";
                }
            } else {
                $results["error"] = true;
                $results["message"] = "Veuillez remplir tous les champs.";
            }
        }
        echo json_encode($results);
    }
?>