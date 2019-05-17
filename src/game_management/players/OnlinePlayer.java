package game_management.players;

import card_management.Card;
import card_management.Hand;

import java.util.ArrayList;

public class OnlinePlayer extends Player{
    private String playerName;

    public OnlinePlayer(String playerName) {
        this.playerName = playerName;
        this.wonCards = new ArrayList<>();
        this.hand = new Hand();
        this.score = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "OnlinePlayer{" +
                "playerName='" + playerName + '\'' +
                ", hand=" + hand +
                '}';
    }
}
