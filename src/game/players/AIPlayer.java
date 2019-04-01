package game.players;

import card_management.Card;

public abstract class AIPlayer extends Player {

    protected Card tempWinningCard;

    AIPlayer(int order) {
        super(order);
        tempWinningCard = null;
    }

    public abstract int chooseBet();

    public abstract Card chooseFellow();

    public abstract Card throwCard();

    public void setTempWinningCard(Card tempWinningCard) {
        this.tempWinningCard = tempWinningCard;
    }
}
