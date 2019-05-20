package GUI.frames;

import GUI.buttons.ButtonCardImage;
import GUI.panels.TableIconPanel;
import game_management.players.ControlledPlayer;
import game_management.players.OnlinePlayer;
import game_management.players.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class OnlineGameScreen extends GameScreen {

    public OnlineGameScreen(Player firstPlayer, Object lock) throws HeadlessException {
        super(firstPlayer, lock);
        setBetAreaVisibility(false);
        removeActionListener();
        OnlinePlayer player = (OnlinePlayer) firstPlayer;
        this.playerName.setText(player.getPlayerName());
    }


    public void logHandWinner(String p) {
        String s = "Hand Winner:" + p;
        log(s);
        tableCards.update();
    }



    @Override
    public void updatePlayerCards(Player player) {
        cardsContainer.update(player.getHand(), true);
    }
    public void addNameOnIcons(ArrayList<String> players) {
        int i = 0;
        for (TableIconPanel panel : iconPanels) {
            panel.setPlayerName(players.get(i));
            i++;
        }
    }
}
