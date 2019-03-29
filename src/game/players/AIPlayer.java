package game.players;

import card_management.Card;

public abstract class AIPlayer extends Player {

    AIPlayer(int order) {
        super(order);
    }

    public abstract int chooseBet();

    public abstract Card chooseFellow();

    public abstract Card throwCard();


}
