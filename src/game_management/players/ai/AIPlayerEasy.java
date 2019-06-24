package game_management.players.ai;

import card_management.Card;
import game_management.players.PlayerRole;

import java.util.Collections;

public class AIPlayerEasy extends AIPlayer {

    public AIPlayerEasy(int order) {
        super(order);
    }

    @Override
    public int chooseBet() {
       if(Collections.max(cardsBySuit.values()) >= 4) {
           return 81;
       }
       else if(Collections.max(cardsBySuit.values()) == 3) {
           return 71;
       }
       else if(Collections.max(cardsBySuit.values()) == 2) {
           return 61;
       }
       else {
           return 0;
       }
    }



    @Override
    public Card throwCard() {
        if(!role.equals(PlayerRole.FELLOW)) {
            return getCardToWin();
            }
        else {
            if(tempWinningPlayer!=null) {
                if(isFellowWinning()) {
                    return giveCarico();
                }
                else {
                   return getCardToWin();
                }
                }
            else {
                return giveLiscio();
            }
        }
    }

    private boolean isFellowWinning() {
        return tempWinningPlayer.getRole().equals(PlayerRole.CALLER) && tempWinningCard.getSeme().equals(briscola);
    }




}
