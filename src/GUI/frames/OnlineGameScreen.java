package GUI.frames;

import GUI.buttons.ButtonCardImage;
import game_management.players.ControlledPlayer;
import game_management.players.OnlinePlayer;
import game_management.players.Player;

import java.awt.*;
import java.awt.event.ActionEvent;

public class OnlineGameScreen extends GameScreen {

    public OnlineGameScreen(Player firstPlayer, Object lock) throws HeadlessException {
        super(firstPlayer, lock);
        setBetAreaVisibility(false);
        removeActionListener();
        OnlinePlayer player = (OnlinePlayer) firstPlayer;
        this.playerName.setText(player.getPlayerName());
    }

    @Override
    public void updatePlayerCards(Player player) {
        cardsContainer.update(player.getHand(), true);
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
            imageString = e.getActionCommand();
            synchronized (lock) {
                turnDone = true;
                lock.notifyAll();
            }
        }
        else if(e.getSource() == exitButton) {
            synchronized (lock) {
                gameEnded = true;
                lock.notifyAll();
            }
        }
    }
}
