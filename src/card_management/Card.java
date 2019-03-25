package card_management;


import java.awt.*;

public class Card {
    private Semi seme;
    private int valore;
    private int points;
    private Image cardImage;

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

    @Override
    public String toString() {
        return "{" +
                "seme=" + seme +
                ", valore=" + valore +
                '}';
    }

    public int getValore() {
        return valore;
    }

    public Semi getSeme() {
        return seme;
    }

    public boolean isGreaterStessoSeme(Card card) {
        if(this.valore == 1) {
            return true;
        }
        else if(this.valore == 3 && card.valore!=1) {
            return true;
        }
        else if(this.valore>card.valore && card.valore!=1 && card.valore!=3) {
            return true;
        }
        else {
            return false;
        }
    }

     @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Card card = (Card) obj;
       return (this.valore == card.valore && this.seme == card.seme);
    }

    public int getPoints() {
        return points;
    }

    public void setCardImage(Image cardImage) {
        this.cardImage = cardImage;
    }

    public Image getCardImage() {
        return cardImage;
    }
}
