package poker;

import java.util.Stack;

/**
 *
 * @author dpoumakis
 */
public class Poker
{

    // Array to hold the players
    private Player[] players;

    // Deck for the poker game
    private Deck deck;

    public Poker()
    {
        // Initialize the list of players
        players = new Player[Config.numberOfPlayers];

        // Initialize the deck
        deck = new Deck();

        // Iterate to create four players
        for (int player = 0; player < Config.numberOfPlayers; player++)
        {
            // Initialize the players hand
            Stack<Card> hand = new Stack<>();

            // Iterate to create 5 cards
            for (int card = 0; card < Config.cardsPerPlayer; card++)
            {
                // Add top card to hand
                hand.add(deck.getTopCard());
            }

            // Create player with hand
            players[player] = new Player(hand);
        }
    }

    // Evaluate each players poker hand and give it a score
    public void evaluatePokerHands()
    {
        // By default have the hand be -1
        int pokerHand = -1;

        // Iterate through each player
        for (Player player : players)
        {
            //Convert the players Stack<Card> hand into an array of cards
            Card[] hand = convertHandToArrayAndSort(player);

            // Check if royal flush...
            if (isRoyalFlush(hand))
            {
                pokerHand = 0;
            }
            // Check if straight flush
            else if (isStraightFlush(hand))
            {
                pokerHand = 1;
            }
            // Check if four of a kind
            else if (isFourOfAKind(hand))
            {
                pokerHand = 2;
            }
            // Check if full house
            else if (isFullHouse(hand))
            {
                pokerHand = 3;
            }
            // Check if flush
            else if (isFlush(hand))
            {
                pokerHand = 4;
            }
            // Check if straight
            else if (isStraight(hand))
            {
                pokerHand = 5;
            }
            // Check if three of a kind
            else if (isThreeOfAKind(hand))
            {
                pokerHand = 6;
            }
            // Check if two pairs
            else if (isTwoPairs(hand))
            {
                pokerHand = 7;
            }
            // Check if pair
            else if (isPair(hand))
            {
                pokerHand = 8;
            }
            // Shit out of luck :P
            else
            {
                // Grab highest card?
                pokerHand = 9;
            }

            // Set the score into players pokerHand variable
            player.setPokerHand(pokerHand);
        }
    }
    
    // Determine the winner based on evaluation of pokerHands
    public void determineWinner()
    {
        // Bubble sort the players by their pokerHand score
        for (int player = 0; player < players.length; player++)
        {
            for (int i = 0; i < players.length - 1; i++)
            {
                if (players[i].getPokerHand() > players[i + 1].getPokerHand())
                {
                    Player temp = players[i];
                    players[i] = players[i + 1];
                    players[i + 1] = temp;
                }
            }
        }
        
        // Check for four way draw
        boolean fourWayDraw = players[0].getPokerHand() == players[3].getPokerHand();
        
        // Check for three way draw
        boolean threeWayDraw = players[0].getPokerHand() == players[2].getPokerHand();
        
        // Check for two way draw
        boolean twoWayDraw = players[0].getPokerHand() == players[1].getPokerHand();
        
        // If four way draw... 
        if (fourWayDraw)
        {
            // Output message to user via console
            System.out.println("Four way draw... Check lower cards!");
        }
        // If three way draw...
        else if (threeWayDraw)
        {
            // Output message to user via console
            System.out.println("Three way draw... Check lower cards!");
        }
        // If two way draw...
        else if (twoWayDraw)
        {
            // Output message to user via console
            System.out.println("Two way draw... Check lower cards!");
        }
        // No draws!
        else
        {
            // Output message to user via console
            System.out.println("We have a winner!");
        }
    }
    
    // Method to convert players Stack<Card> hand to an array of cards
    private Card[] convertHandToArrayAndSort(Player player)
    {
        // Initialize an array of cards
        Card[] hand = new Card[5];

        // Iterate through players hand
        for (int card = 0; card < 5; card++)
        {
            // Add players cards to the hand
            hand[card] = player.getHand().pop();
        }
        
        // Return converted array
        return hand;
    }
    
    // Check if hand is flush
    private boolean isFlush(Card[] hand)
    {
        // Initialize return boolean
        boolean isFlush = false;

        // Bubble sort the hand via suit value
        bubbleSortSuitValue(hand);

        // Store the first cards suit value
        int suitValue = hand[0].getSuitValue();
        
        // If first cards suit matches last cards suit...
        if (suitValue == hand[4].getSuitValue())
        {
            // We have a flush!
            isFlush = true;
        }
        
        // Return boolean
        return isFlush;
    }

    // Check if hand is straight
    private boolean isStraight(Card[] hand)
    {
        // Initialize return boolean
        boolean isStraight = true;
        
        // Bubble sort the hand via face value
        bubbleSortFaceValue(hand);
        
        // Store the last cards face value
        int faceValue = hand[4].getFaceValue();

        // Check if ace 
        if (faceValue == 14)
        {
            // If the ace is considered as 1...
            boolean low = hand[0].getFaceValue() == 2 && hand[3].getFaceValue() == 5;
            
            // If the ace is considered as 14...
            boolean high = hand[0].getFaceValue() == 10 && hand[3].getFaceValue() == 13;
            
            // Or the two booleans
            isStraight = low || high;
        }
        // No ace in hand
        else
        {
            // Store the first cards face value
            faceValue = hand[0].getFaceValue();
            
            // Iterate through the other 4 cards...
            for (int card = 1; card < 5; card++)
            {
                // Handle for current card
                Card currentCard = hand[card];
                
                // Increase expected face value by 1
                faceValue++;
                
                // If currentCard is "straight" with incremented face value...
                if (currentCard.getFaceValue() == faceValue)
                {
                    // Good, keep going!
                }
                // Not straight... flip flag
                else
                {
                    // Set return boolean to false
                    isStraight = false;
                }
            }
        }
        
        // Return the boolean 
        return isStraight;
    }
    
    // Check if hand is a straight flush
    private boolean isStraightFlush(Card[] hand)
    {
        // Check using two already defined methods :)
        return isStraight(hand) && isFlush(hand);
    }

    // Check if royal flush
    private boolean isRoyalFlush(Card[] hand)
    {
        // Same  method used in isStraightFlush...
        boolean straightFlush = isStraight(hand) && isFlush(hand);
        
        // Sort the hand
        bubbleSortFaceValue(hand);
        
        // Return with check if ace is in hand!
        return straightFlush && hand[4].getFaceValue() == 14;
    }
    
    // Check if four of a kind
    private boolean isFourOfAKind(Card[] hand)
    {
        // Initalize return boolean
        boolean isFourOfAKind = false;
        
        // Sort the cards by face value
        bubbleSortFaceValue(hand);
        
        // If first card matches last... it's four of a kind
        isFourOfAKind = hand[0].getFaceValue() == hand[4].getFaceValue();
        
        // Return boolean
        return isFourOfAKind;
    }
    
    // Check for full house
    private boolean isFullHouse(Card[] hand)
    {
        // Sort cards by face value
        bubbleSortFaceValue(hand);
        
        // Check if the low 3 cards are equal && high 2 cards are equal
        boolean low = hand[0].getFaceValue() == hand[2].getFaceValue()
                && hand[3].getFaceValue() == hand[4].getFaceValue();
        
        // Check if the low 2 cards are equal && high 3 cards are equal
        boolean high = hand[0].getFaceValue() == hand[1].getFaceValue()
                && hand[2].getFaceValue() == hand[4].getFaceValue();
        
        // Return the two checks OR'd together
        return low || high;
    }
    
    // Check if three of a kind
    private boolean isThreeOfAKind(Card[] hand)
    {
        // Booleans for 3 scenarios...
        boolean low = false;
        boolean middle = false;
        boolean high = false;
        
        // If not four of a kind nor full house...
        if (!isFourOfAKind(hand) && !isFullHouse(hand))
        {
            // Sort the cards by face value
            bubbleSortFaceValue(hand);
            
            // Check if low 3 cards are equal
            low = hand[0].getFaceValue() == hand[2].getFaceValue();
            
            // Check if middle 3 cards are equal
            middle = hand[1].getFaceValue() == hand[3].getFaceValue();
            
            // Check if high 3 cards are equal
            high = hand[2].getFaceValue() == hand[4].getFaceValue();
        }
        
        // Return each scenario OR'd together
        return (low || middle || high);

    }
    
    // Check if two pairs
    private boolean isTwoPairs(Card[] hand)
    {
        // Booleans for each scenario
        boolean low = false;
        boolean middle = false;
        boolean high = false;
        
        // Sort the cards by face value
        bubbleSortFaceValue(hand);
        
        // If not four of kind, full house nor three of a kind
        if (!isFourOfAKind(hand) && !isFullHouse(hand) && !isThreeOfAKind(hand))
        {
            // Check if [0-1] && [2-3] are both pairs
            low = hand[0].getFaceValue() == hand[1].getFaceValue()
                    && hand[2].getFaceValue() == hand[3].getFaceValue();
            
            // Check if [0-1] && [3-4] are both pairs
            middle = hand[0].getFaceValue() == hand[1].getFaceValue()
                    && hand[3].getFaceValue() == hand[4].getFaceValue();
            
            // Check if [1-2] && [3-4] are both pairs
            high = hand[1].getFaceValue() == hand[2].getFaceValue()
                    && hand[3].getFaceValue() == hand[4].getFaceValue();
        }
        
        // Return each scenario OR'd together
        return (low || middle || high);
    }
    
    // Check if is pair
    public boolean isPair(Card[] hand)
    {
        // Booleans for each scenario
        boolean low = false;
        boolean middle = false;
        boolean high = false;
        boolean higher = false;
        
        // If not four of kind, full house, three of kind nor two pairs...
        if (!isFourOfAKind(hand) && !isFullHouse(hand)
                && !isThreeOfAKind(hand)
                && !isTwoPairs(hand))

        {
            // Sort cards by face value
            bubbleSortFaceValue(hand);
            
            // Check if [0-1] are a pair
            low = hand[0].getFaceValue() == hand[1].getFaceValue();
            
            // Check if [1-2] are a pair
            middle = hand[1].getFaceValue() == hand[2].getFaceValue();
            
            // Check if [2-3] are a pair
            high = hand[2].getFaceValue() == hand[3].getFaceValue();
            
            // Check if [3-4] are a pair
            higher = hand[3].getFaceValue() == hand[4].getFaceValue();

        }
        
        // Return each scenario OR'd together
        return low || middle || high || higher;
    }
    
    
    // Simple bubble sort for ordering cards via face value
    private void bubbleSortFaceValue(Card... cards)
    {
        for (int length = 0; length < cards.length; length++)
        {
            for (int i = 0; i < cards.length - 1; i++)
            {
                if (cards[i].getFaceValue() > cards[i + 1].getFaceValue())
                {
                    Card temp = cards[i];
                    cards[i] = cards[i + 1];
                    cards[i + 1] = temp;
                }

            }
        }
    }
    
    // Simple bubble sort for ordering cards via suit value
    private void bubbleSortSuitValue(Card... cards)
    {
        for (int length = 0; length < cards.length; length++)
        {
            for (int i = 0; i < cards.length - 1; i++)
            {
                if (cards[i].getSuitValue() > cards[i + 1].getSuitValue())
                {
                    Card temp = cards[i];
                    cards[i] = cards[i + 1];
                    cards[i + 1] = temp;
                }

            }
        }
    }
    
    // Get the array of players
    public Player[] getPlayers()
    {
        return players;
    }
    
    // Set the array of players
    public void setPlayers(Player[] players)
    {
        this.players = players;
    }
    
    // Get the deck
    public Deck getDeck()
    {
        return deck;
    }
    
    // Set the deck
    public void setDeck(Deck deck)
    {
        this.deck = deck;
    }

}
