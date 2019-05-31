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
    private boolean cardsEnabled;

    public OnlineGameScreen(Player firstPlayer, Object lock) throws HeadlessException {
        super(firstPlayer, lock);
        setBetAreaVisibility(false);
        removeActionListener();
        OnlinePlayer player = (OnlinePlayer) firstPlayer;
        this.playerName.setText(player.getPlayerName());
        cardsEnabled = false;
    }

    public void setCardsEnabled(boolean cardsEnabled) {
        this.cardsEnabled = cardsEnabled;
    }

    public void logHandWinner(String p) {
        String s = "Hand Winner:" + p;
        log(s);
        tableCards.update();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttonBet) {
            bet = (Integer) betSpinner.getValue();
            if(bet > higherBet) {
                synchronized (lock) {
                    updateSpinner();
                    betDone = true;
                    lock.notifyAll();
                }
            }
        }
        else if(e.getSource() == buttonPass) {
            bet = 0;
            synchronized (lock) {
                betDone = true;
                lock.notifyAll();
            }
        }
        else if(e.getSource() instanceof ButtonCardImage) {
            if(cardsEnabled) {
                imageString = e.getActionCommand();
                synchronized (lock) {
                    turnDone = true;
                    lock.notifyAll();
                }
            }
        }
        else if(e.getSource() == exitButton) {
            synchronized (lock) {
                gameEnded = true;
                lock.notifyAll();
            }
        }
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
