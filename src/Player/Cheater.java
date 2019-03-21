package Player;

import java.util.ArrayList;
import java.util.Random;

import Card.Card;
import Driver.Main;
import Map.Map.Territory;
import Player.Player.GamePhase;
import Map.Map;

/**
 * 
 * The Cheater class is implementing functions 
 * in Strategy interface
 * 
 * @author mandeepahlawat
 * @version 1.0
 * @since 24-11-2018
 *
 */
public class Cheater implements Strategy {
	/**
	 * The player to which this strategy belongs to
	 */
	public Player player;
	
	/**
	 * 
	 * constructor for this class and sets the
	 * player data member
	 * 
	 * @param player assign the player value.
	 * 
	 */
	public Cheater(Player player) {
		this.player = player;
	}
	
	/**
	 *
	 * Calculation of reinforcement armies,
	 * which will vary depending on the strategy.
	 * 
	 * @return int value of the total reinforcement armies
	 * 
	 */
	@Override
	public int calculateReinforcementArmies() {
		int totalReinforcements = 0;
		for(Territory territory : player.assignedTerritories) {
			totalReinforcements += territory.numberOfArmies;
		}
		
		if(player.ownedContinents().size() > 0) {
			for(Map continent : player.ownedContinents()) {
				totalReinforcements += continent.score;
			}
		}
		
		if(player.canExchangeCards()) {
			totalReinforcements += Card.cardExchangeValue;
			ArrayList<Integer> cardIndexes = player.getCardIndexesToExchange();
			player.exchangeCards(cardIndexes.get(0), cardIndexes.get(1), cardIndexes.get(2));
		}
		
		System.out.println("Player " + player.getName() + " gets " + totalReinforcements + " reinforcement armies.");
		player.totalArmiesCount += totalReinforcements;
		return totalReinforcements;
	}

	/**
	 * 
	 * Placement of reinforcement armies,
	 * which will vary depending on the strategy.
	 * 
	 * @param reinforcements - number of reinforcements
	 * to be place.
	 * 
	 */
	
	@Override
	public void placeReinforcements(int reinforcements) {
		System.out.println("\nPlayer " + player.getName() + " doubles the number of armies in each"
				+ "owned country");
		for(Territory territory : player.assignedTerritories) {
			System.out.println("Armies count before move: " + territory.name + ": " + territory.numberOfArmies
					+ " armies");
			reinforcements = reinforcements - territory.numberOfArmies;
			territory.numberOfArmies += territory.numberOfArmies;
			System.out.println("Armies count after move: " + territory.name + ": " + territory.numberOfArmies
					+ " armies");
		}
		while(reinforcements > 0) {
			Territory territory = player.getRandomTerritory(player.assignedTerritories);
			System.out.println("Placing extra reinforcement army in: " + territory.name + " , before placing: " + territory.numberOfArmies
					+ " armies");
			territory.numberOfArmies += 1;
			System.out.println("Armies count after move: " + territory.name + ": " + territory.numberOfArmies
					+ " armies");
			reinforcements -= 1;
		}
	}

	/**
	 *
	 * Fortification phase, whose implementation will
	 * vary depending on the strategy.
	 * 
	 */
	@Override
	public void fortification() {
		System.out.println("\nPlayer " + player.getName() + " doubles the number of armies in each"
				+ " owned country that has neighbouring country with other player");
		for(Territory territory : player.getTerritoriesWithNeighboursToOthers()) {
			System.out.println("Armies count before move: " + territory.name + ": " + territory.numberOfArmies
					+ " armies");
			player.totalArmiesCount += territory.numberOfArmies;
			territory.numberOfArmies += territory.numberOfArmies;
			System.out.println("Armies count after move: " + territory.name + ": " + territory.numberOfArmies
					+ " armies");
		}
	}

	/**
	 * 
	 * Attack phase, whose implementation will
	 * vary depending on the strategy.
	 *
	 */
	@Override
	public void attack() {
		ArrayList<Territory> assignedTerritoriesBeforeAttack = new ArrayList<Territory>(player.assignedTerritories);
		for(Territory territory : assignedTerritoriesBeforeAttack) {
			for(Territory neighbour : player.getTerritoriesWithNeighboursToOthers(territory)) {
				System.out.println(territory.name + " attacks " + neighbour.name);
				System.out.println("Player " + player.getName() + " won " + neighbour.name);
				player.addNewOwnedTerritory(neighbour);
			}
		}
		if(Main.activeMap.allTerritoriesOwnBySinglePlayer(false)) {
			Main.gameFinished = true;
		}
		else {
			player.setCurrentGamePhase(GamePhase.FORTIFICATION);
		}
	}
	
	/**
	 * This Method will return string value.
	 */
	@Override
	 public String toString() {
		 return "cheater";
	 }
	

}
