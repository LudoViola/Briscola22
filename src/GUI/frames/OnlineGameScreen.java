package GUI.frames;

import game_management.players.Player;

import java.awt.*;

public class OnlineGameScreen extends GameScreen {

    public OnlineGameScreen(Player firstPlayer, Object lock) throws HeadlessException {
        super(firstPlayer, lock);
    }
}
