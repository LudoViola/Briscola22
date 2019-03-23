import card_management.Deck;
import game.GameManagement;

public class Test {
    public static void main(String[] args) {
        Deck deck = new Deck();
        GameManagement game = new GameManagement(deck);
        test_0(deck, game);
        game.startGame();
        System.out.println(game);
    }

    //*****First Test, deck, players and hands randomly generated****

    private static void test_0(Deck deck, GameManagement game) {
        System.out.println("Deck not Shuffled");
        System.out.println(deck);
        System.out.println("Deck  Shuffled");
        deck.shuffle();
        System.out.println(deck);
        System.out.println("\n\n****Game started******");
        System.out.println(game);
        System.out.println("Distributing Card");
        game.distributeCard();
        System.out.println(game);
        System.out.println(deck);
    }
}
