<?php
class Commande_detail implements JSONSerializable{
    private string $type_de_meuble;
    private string $couleur;
    private string $essence;
    private int $quantite;

    public function getTypeDeMeuble():string{
        return $this->type_de_meuble;
    }
    public function getCouleur():string{
        return $this->couleur;
    }
    public function getEssence():string{
        return $this->essence;
    }
    public function getQuantite():int{
        return $this->quantite;
    }
    public function __construct(string $type_de_meuble,string $couleur,string $essence,int $quantite){
        $this->type_de_meuble=$type_de_meuble;
        $this->couleur=$couleur;
        $this->essence=$essence;
        $this->quantite=$quantite;
    }
    public function __toString():string{
        return $this->type_de_meuble." ".$this->couleur. " ".$this->essence." ".$this->quantite;

    }
    public function jsonSerialize():array{
        return["type_de_meuble"=>$this->getTypeDeMeuble(),
        "couleur"=>$this->getCouleur(),
        "essence"=>$this->getEssence(),
        "quantite"=>$this->getQuantite()];
    }
    public static function fromJson(string $json):Commande_detail{
        $array=json_decode($json,true);
        return new Commande_detail($array["type_de_meuble"],$array["couleur"],$array["essence"],$array["quantite"]);
    }
}
