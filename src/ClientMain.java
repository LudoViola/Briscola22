import game_management.LocalGame;

public class ClientMain {
    private boolean isLocal = true;
    private final static Object lock = new Object();
    public static void main(String[] args) {

        LocalGame game = new LocalGame(lock);
        game.goToMenuScreen();
    }
}
