package card_management;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;
    private final static int NUMBER_OF_CARDS = 40;

    public Deck() {
        deck = new ArrayList<>();
        createDeck();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    private  void createDeck() {
        int factor = 0;
        for(Semi s: Semi.values()) {
            for(int i = 1; i < 11; i++) {
                Card card = new Card(s,i);
                URL resource;
                if(s == Semi.COPPE) {
                    resource = getClass().getClassLoader().getResource( "card_images/" + "a" + i + ".png" );
                }
                else {
                    resource = getClass().getClassLoader().getResource( "card_images/" + "a" + (i+factor) + ".png" );
                }
                BufferedImage body = null;
                try {
                    assert resource != null;
                    body = ImageIO.read( resource );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                card.setCardImage(body);
                deck.add(card);
            }
            factor +=10;

        }
    }

    public Card distributeCard() {
        if(deck.size()>0) {
            Card card = this.deck.get(deck.size() - 1);
            this.deck.remove(deck.size() - 1);
            return card;
        }
        else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck.toString() +
                '}';
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }
}
