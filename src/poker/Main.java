package poker;

/**
 *
 * @author dpoumakis
 */
public class Main
{

    public static void main(String[] args)
    {
        // Initialize poker game
        Poker poker = new Poker();

        // Evaluate the players hands
        poker.evaluatePokerHands();

        // Output each players poker hand "score"
        for (Player player : poker.getPlayers())
        {
            System.out.println(player.getPokerHand());
        }

        // Determine winner, will be outputed in console
        poker.determineWinner();

    }
}
