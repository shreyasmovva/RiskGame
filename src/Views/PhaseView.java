package Views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import Driver.Main;
import Map.Map;
import Player.Player;
import Player.Player.GamePhase;

/**
 * This class will implement from observer
 * and it displays player's name and different
 * functionality of the phase's.
 * 
 * @author shreyas
 * @version 1.0
 * 
 */
public class PhaseView implements Observer {
	
	/**
	 * This method will display player's name 
	 * and also calculate's the reinforcement armies.
	 * 
	 * @param player creation of new player in the
	 * game.
	 */
	public void reinforcementPhaseView(Player player) {
		System.out.println("\n***********************"
							+ player.getName()
							+ " REINFORCEMENT PHASE BEGINS*****************************\n");
		player.reinforcement();
	}
	
	/**
	 * This method will display the player's 
	 * name and also allow player to move his
	 * armies to defend in player's class.
	 * 
	 * @param player Creation of player in game.
	 */
	public void fortificationPhaseView(Player player) {
		System.out.println("\n***********************"
							+ player.getName()
							+" FORTIFICATION PHASE BEGINS*****************************\n");
		player.fortification();
	}
	
	/**
	 * This method will display the player's 
	 * name and also allow player to move his
	 * armies to attack in player's class.
	 * 
	 * @param player creation of player in game.
	 */
	public void attackPhaseView(Player player) {
		System.out.println("\n***********************"
							+ player.getName()
							+ " ATTACK PHASE BEGINS*****************************\n");
		player.attack();
	}
	
	/**
	 * {@inheritDoc}
	 * This method will update the different phases 
	 * only after passing the condition.
	 * 
	 */
	@Override
	public void update(Observable o, Object arg) {
		Player currentPlayer = ((Player) o);
		if(!currentPlayer.cardExchangeViewOpen) {
			switch(currentPlayer.currentGamePhase) {
				case REINFORCEMENT:
					reinforcementPhaseView(currentPlayer);
					break;
				case ATTACK:
					attackPhaseView(currentPlayer);
					break;
				case FORTIFICATION:
					fortificationPhaseView(currentPlayer);
					break;
			}
		}
	}
	
}
