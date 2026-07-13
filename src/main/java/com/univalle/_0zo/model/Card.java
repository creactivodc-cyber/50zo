package com.univalle._0zo.model;

/**
 * Representa una carta del juego Cincuentazo.
 * Cada carta tiene un palo y un valor visual (rank).
 */
public class Card {

    public enum Palo {
        CORAZONES, DIAMANTES, TREBOLES, PICAS
    }

    public enum NumCard {
        ACE, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE, DIEZ, JACK, QUEEN, KING
    }

    private final Palo palo;
    private final NumCard numCard;

    public Card(Palo palo, NumCard numCard) {
        this.palo = palo;
        this.numCard = numCard;
    }

    public Palo getPalo() {
        return palo;
    }

    public NumCard getNumCard() {
        return numCard;
    }

    /**
     * Valor de juego según reglas del Cincuentazo:
     * 2-8, 10 = su número | 9 = 0 | J,Q,K = -10 | A = 1
     */
    public int getGameValue() {
        switch (numCard) {
            case ACE:   return 1;
            case DOS:   return 2;
            case TRES: return 3;
            case CUATRO:  return 4;
            case CINCO:  return 5;
            case SEIS:   return 6;
            case SIETE: return 7;
            case OCHO: return 8;
            case NUEVE:  return 0;
            case DIEZ:   return 10;
            case JACK:  return -10;
            case QUEEN: return -10;
            case KING:  return -10;
            default:    return 0;
        }
    }

    /**
     * Valor de juego para un As (1 o 10 según convenga).
     */
    public int getGameValue(int aceValue) {
        if (numCard == NumCard.ACE && (aceValue == 1 || aceValue == 10)) {
            return aceValue;
        }
        return getGameValue();
    }

    public boolean isAce() {
        return numCard == NumCard.ACE;
    }

    @Override
    public String toString() {
        String nCardStr;
        switch (numCard) {
            case ACE:   nCardStr = "A";  break;
            case JACK:  nCardStr = "J";  break;
            case QUEEN: nCardStr = "Q";  break;
            case KING:  nCardStr = "K";  break;
            case DOS:   nCardStr = "2";  break;
            case TRES:  nCardStr = "3";  break;
            case CUATRO: nCardStr = "4";  break;
            case CINCO:  nCardStr = "5";  break;
            case SEIS:   nCardStr = "6";  break;
            case SIETE: nCardStr = "7";  break;
            case OCHO: nCardStr = "8";  break;
            case NUEVE:  nCardStr = "9";  break;
            case DIEZ:   nCardStr = "10"; break;
            default:    nCardStr = "?";
        }

        String paloStr;
        switch (palo) {
            case CORAZONES:   paloStr = "♥"; break;
            case DIAMANTES: paloStr = "♦"; break;
            case TREBOLES:    paloStr = "♣"; break;
            case PICAS:   paloStr = "♠"; break;
            default:       paloStr = "?";
        }

        return nCardStr + paloStr;
    }
}
