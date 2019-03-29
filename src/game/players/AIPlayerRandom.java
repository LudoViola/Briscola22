package game.players;

import card_management.Card;
import card_management.Deck;

import java.util.Random;

public class AIPlayerRandom extends AIPlayer {

    Random random;

    public AIPlayerRandom(int order) {
        super(order);
        random  = new Random();
    }

    @Override
    public int chooseBet() {
        if (random.nextBoolean()) {
            int i = 61;
            int bound;
            if(random.nextBoolean()) {
                bound = 31;
            }
            else {
                bound = 11;
            }
            return i + random.nextInt(bound);
        } else {
            return 0;
        }
    }

    @Override
    public Card chooseFellow() {
        Deck deck = new Deck();
        deck.getDeck().removeAll(this.hand.getCards());
        return  deck.getDeck().get(random.nextInt(deck.getDeck().size()));
    }

    @Override
    public Card throwCard() {
        return this.hand.getCards().get(random.nextInt(this.hand.getCards().size()));
    }
}
