package card_management;


import game_management.players.PlayerRole;

import java.awt.*;

public class Card implements Comparable<Card> {
    private Semi seme;
    private int valore;
    private int points;
    private Image cardImage;
    private Valore value;
    private PlayerRole owner;

    public Card(Semi seme, int valore) {
        this.seme = seme;
        this.valore = valore;
        pointsCalculator();
    }

    private void pointsCalculator() {
        switch(this.valore) {
            case 1:
                points = 11;
                break;
            case 3:
                points = 10;
                break;
            case 10:
                points = 4;
                break;
            case 9:
                points = 3;
                break;
            case 8:
                points = 2;
                break;
                default:
                    points = 0;
                    break;
        }
    }
    public boolean isGreaterStessoSeme(Card card) {
        return this.value.ordinal() > card.getValue().ordinal();
    }

    @Override
    public String toString() {
        return "{" + "seme=" + seme + '}' +value;
    }

     @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Card card = (Card) obj;
       return (this.valore == card.valore && this.seme == card.seme);
    }

    @Override
    public int compareTo(Card o) {
        if (this.seme.ordinal() == o.getSeme().ordinal()) {
            if (this.value.ordinal() > o.getValue().ordinal()) {
                return 1;
            }
            else {
                return -1;
            }
        }else if(this.seme.ordinal() > o.getSeme().ordinal()) {
            return 1;
        }
        else {
            return -1;
        }
    }

    int getValor() {
        return valore;
    }
    public Semi getSeme() {
        return seme;
    }
    private Valore getValue() {
        return value;
    }
    public int getPoints() {
        return points;
    }
    public Image getCardImage() {
        return cardImage;
    }
    void setCardImage(Image cardImage) {
        this.cardImage = cardImage;
    }
    void setValue(Valore value) {
        this.value = value;
    }

    public PlayerRole getOwner() {
        return owner;
    }

    public void setOwner(PlayerRole owner) {
        this.owner = owner;
    }
}
