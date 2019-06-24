package game_management.players.ai;

import card_management.Card;

public interface AI {
    int chooseBet();

    Card chooseFellow();

    Card throwCard();
}
