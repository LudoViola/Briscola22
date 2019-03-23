package card_management;

public class Card {
    private Semi seme;
    private int valore;

    public Card(Semi seme, int valore) {
        this.seme = seme;
        this.valore = valore;
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
}
