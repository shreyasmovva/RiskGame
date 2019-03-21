package Views;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import Card.Card;
import Player.Player;
/**
 * This method will implement from observer
 * and also display's the Card exchange process 
 * between the player's in the game.Updates when
 * the observer notifies it's changes.
 * 
 * @author Mandeep Ahlawat
 * @version 1.0
 * @since 04/11/2018
 *
 */

public class CardExchangeView implements Observer{
	
	/**
	 * This method will display the selection of card's 
	 * that can exchanged between the player's in the game.
	 * 
	 * @param currentPlayer object of player which 
	 * display's current player details.
	 * 
	 */
	public static void cardExchangeSelection(Player currentPlayer) {
		System.out.println("You currently have following cards with you.");
		int i = 1;
		for(Card card : currentPlayer.cards) {
			System.out.println(i + ". " + card);
			++i;
		}
		System.out.println("Please select cards to exchange from the following cards"
				+ "\n The card numbers should be comma seperated");
		
		Scanner keyboard = new Scanner(System.in);
		String cardNumbers = keyboard.nextLine();
		
		String cardnums[] = cardNumbers.split(",");
		
		while(!currentPlayer.validCardIndexesToExchange(Integer.parseInt(cardnums[0]) - 1,
				Integer.parseInt(cardnums[1]) - 1, Integer.parseInt(cardnums[2]) - 1)) {
			System.out.println("You can only exchange cards of different types or all cards of same type");
			cardExchangeSelection(currentPlayer);
		}
		currentPlayer.exchangeCards(Integer.parseInt(cardnums[0]) - 1, Integer.parseInt(cardnums[1]) - 1,
				Integer.parseInt(cardnums[3]) - 1);
	}

	/**
	 * {@inheritDoc}
	 * This method print's out start and end 
	 * of card exchange map and display's
	 * the current player's card's after update
	 * method is called.
	 */
	@Override
	public void update(Observable o, Object arg) {
		Player currentPlayer = (Player) o;
		System.out.println("\n*********************** CARD EXCHANGE VIEW START *****************************\n");
		cardExchangeSelection(currentPlayer);
		System.out.println("\n*********************** CARD EXCHANGE VIEW ENDS *****************************\n");
	}

}
