package com.univalle._0zo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {

    private List<Card> mazo;

    public Mazo() {
        mazo = new ArrayList<>();
        for (Card.Palo palo : Card.Palo.values()) {
            for (Card.NumCard numCard : Card.NumCard.values()) {
                mazo.add(new Card(palo, numCard));
            }
        }
        barajar();
    }

    public void barajar() {
        Collections.shuffle(mazo);
    }

    public Card drawCard() {
        return mazo.remove(mazo.size() - 1);
    }

    public void addCardsToBottom(List<Card> newCards) {
        mazo.addAll(0, newCards);
    }

    public void reBarajarTable(List<Card> tableCards) {
        mazo.addAll(tableCards);
        barajar();
    }

    public boolean isEmpty() {
        return mazo.isEmpty();
    }

    public int size() {
        return mazo.size();
    }
}
