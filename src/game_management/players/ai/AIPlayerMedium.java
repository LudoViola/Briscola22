package game_management.players.ai;

import card_management.Card;

public class AIPlayerMedium extends AIPlayer {

    AIPlayerMedium(int order) {
        super(order);
    }

    @Override
    public int chooseBet() {
        return 0;
    }


    @Override
    public Card throwCard() {
        return null;
    }
}
