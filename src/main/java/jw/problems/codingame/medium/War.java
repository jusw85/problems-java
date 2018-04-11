package jw.problems.codingame.medium;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/winamax-battle
 *
 * The Goal
 * Let's go back to basics with this simple card game: war!
 *
 * Your goal is to write a program which finds out which player is the winner for a given card distribution of the "war" game.
 * Rules
 * War is a card game played between two players. Each player gets a variable number of cards of the beginning of the game: that's the player's deck. Cards are placed face down on top of each deck.
 *
 * Step 1 : the fight
 * At each game round, in unison, each player reveals the top card of their deck – this is a "battle" – and the player with the higher card takes both the cards played and moves them to the bottom of their stack. The cards are ordered by value as follows, from weakest to strongest:
 * 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K, A.
 *
 * Step 2 : war
 * If the two cards played are of equal value, then there is a "war". First, both players place the three next cards of their pile face down. Then they go back to step 1 to decide who is going to win the war (several "wars" can be chained). As soon as a player wins a "war", the winner adds all the cards from the "war" to their deck.
 *
 * Special cases:
 *
 * If a player runs out of cards during a "war" (when giving up the three cards or when doing the battle), then the game ends and both players are placed equally first.
 * The test cases provided in this puzzle are built in such a way that a game always ends (you do not have to deal with infinite games)
 *
 * Each card is represented by its value followed by its suit: D, H, C, S. For example: 4H, 8C, AS.
 *
 * When a player wins a battle, they put back the cards at the bottom of their deck in a precise order. First the cards from the first player, then the one from the second player (for a "war", all the cards from the first player then all the cards from the second player).
 *
 * For example, if the card distribution is the following:
 * Player 1 : 10D 9S 8D KH 7D 5H 6S
 * Player 2 : 10H 7H 5C QC 2C 4H 6D
 * Then after one game turn, it will be:
 * Player 1 : 5H 6S 10D 9S 8D KH 7D 10H 7H 5C QC 2C
 * Player 2 : 4H 6D
 *
 * Victory Conditions
 * A player wins when the other player no longer has cards in their deck.
 * Game Input
 * Input
 *
 * Line 1: the number N of cards for player one.
 *
 * N next lines: the cards of player one.
 *
 * Next line: the number M of cards for player two.
 *
 * M next lines: the cards of player two.
 * Output
 *
 * If players are equally first: PAT
 * Otherwise, the player number (1 or 2) followed by the number of game rounds separated by a space character. A war or a succession of wars count as one game round.
 *
 * Constraints
 * 0 < N, M < 1000
 */
public class War {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        Queue<Card> cardsp1 = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            String card = in.next();
            cardsp1.add(parseCard(card));
        }

        int m = in.nextInt();
        Queue<Card> cardsp2 = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            String card = in.next();
            cardsp2.add(parseCard(card));
        }

        int numRounds = 0;
        boolean gameover = false;
        Queue<Card> p1q = new LinkedList<>();
        Queue<Card> p2q = new LinkedList<>();

        while (!gameover) {
            numRounds++;
            boolean isBattling = true;
            Card c1 = cardsp1.poll();
            Card c2 = cardsp2.poll();
            Queue<Card> winq = null;
            while (isBattling) {
                p1q.add(c1);
                p2q.add(c2);

                if (c1.value > c2.value) {
                    winq = cardsp1;
                    isBattling = false;
                } else if (c1.value < c2.value) {
                    winq = cardsp2;
                    isBattling = false;
                } else {
                    if (cardsp1.size() < 4 || cardsp2.size() < 4) {
                        System.out.println("PAT");
                        gameover = true;
                        break;
                    }
                    p1q.add(cardsp1.poll());
                    p1q.add(cardsp1.poll());
                    p1q.add(cardsp1.poll());
                    p2q.add(cardsp2.poll());
                    p2q.add(cardsp2.poll());
                    p2q.add(cardsp2.poll());
                    c1 = cardsp1.poll();
                    c2 = cardsp2.poll();
                }
            }

            if (!gameover) {
                while (!p1q.isEmpty()) {
                    winq.add(p1q.poll());
                }
                while (!p2q.isEmpty()) {
                    winq.add(p2q.poll());
                }

                if (cardsp1.isEmpty()) {
                    System.out.println("2 " + numRounds);
                    gameover = true;
                }
                if (cardsp2.isEmpty()) {
                    System.out.println("1 " + numRounds);
                    gameover = true;
                }
            }
        }
    }

    public static Card parseCard(String card) {
        Card c = new Card();
        c.name = card;
        card = card.substring(0, card.length() - 1);
        int value;
        switch (card) {
            case "J":
                value = 11;
                break;
            case "Q":
                value = 12;
                break;
            case "K":
                value = 13;
                break;
            case "A":
                value = 14;
                break;
            default:
                value = Integer.parseInt(card);
                break;
        }
        c.value = value;
        return c;
    }

    public static class Card {
        public int value;
        public String name;

        public String toString() {
            return name;
        }
    }

}
