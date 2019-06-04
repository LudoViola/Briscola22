import game_management.game.Client;

public class ClientMain {

    public static void main(String[] args) {
        new Thread(() -> {
            Client user = new Client();
            user.startGame();
        }).start();

    }
}
