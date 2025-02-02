<?php
class Commande implements JSONSerializable{
    private string $cuisine;

    public function getCuisine():string{
        return $this->cuisine;
    }
    public function __construct(string $cuisine){
        $this->cuisine=$cuisine;
    }
    public function __toString():string{
        return $this->cuisine;

    }
    public function jsonSerialize():array{
        return["cuisine"=>$this->getCuisine()];
    }

}
