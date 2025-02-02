<?php
class Meuble implements JSONSerializable{
    private string $type_de_meuble;
    private string $essence;

    public function getTypeDeMeuble():string{
        return $this->type_de_meuble;
    }

    public function getEssence():string{
        return $this->essence;
    }
    public function __construct(string $type_de_meuble,string $essence){
        $this->type_de_meuble=$type_de_meuble;
        $this->essence=$essence;
    }
    public function __toString():string{
        return $this->type_de_meuble." ".$this->essence;

    }
    public function jsonSerialize():array{
        return["type_de_meuble"=>$this->getTypeDeMeuble(),
        "essence"=>$this->getEssence()];
    }
    public static function fromJson(string $json):Meuble{
        $array=json_decode($json,true);
        return new Meuble($array["type_de_meuble"],$array["essence"]);
    }
}
