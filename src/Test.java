import game.GameManagement;

public class Test {
    public static void main(String[] args) {
        GameManagement game = new GameManagement();
        game.startGame();
      /*  GameManagement game = new GameManagement();
        test_0(game);
        game.startGame();
        System.out.println(game);
        */
    }

    //*****First Test, deck, players and hands randomly generated****

    private static void test_0(GameManagement game) {
        System.out.println("Deck not Shuffled");
        System.out.println(game.getDeck());
        System.out.println("Deck  Shuffled");
        System.out.println(game.getDeck());
        System.out.println("\n\n****Game started******");
        System.out.println(game);
        System.out.println("Distributing Card");
        game.distributeCard();
        System.out.println(game);
        System.out.println(game.getDeck());
    }
}
