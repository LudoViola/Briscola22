package game_management.players.ai;

import card_management.Card;

public class AIPlayerHard extends AIPlayer {


    AIPlayerHard(int order) {
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
