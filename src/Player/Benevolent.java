package Player;

import java.util.ArrayList;

import Card.Card;
import Driver.Main;
import Map.Map;
import Map.Map.Territory;
import Player.Player.GamePhase;

/**
 * 
 * The Benevolent class is implementing functions 
 * in Strategy interface
 * 
 * @author mandeepahlawat
 * @version 1.0
 * @since 24-11-2018
 *
 */
public class Benevolent implements Strategy {
	/**
	 * The player to which this strategy belongs to
	 */
	public Player player;
	
	/**
	 * 
	 * constructor for this class and sets the
	 * player data member.
	 * 
	 * @param player assign the player value.
	 *
	 */
	public Benevolent(Player player) {
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
		totalReinforcements =  (int)(player.assignedTerritories.size() / 3);
		if(totalReinforcements < 3) {
			totalReinforcements = 3;
		}

		for(Map continent : Main.activeMap.continents) {
			int i = 0;
			for(Territory territory : continent.territories) {
				if(territory.owner != player) {
					break;
				}
				i++;
			}
			if(i == continent.territories.size()) {
				totalReinforcements = totalReinforcements + continent.score;
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
		Territory territory = player.territoryWithMinArmy(player.assignedTerritories);
		System.out.println("\nPlayer " + player.getName() + " places " + reinforcements
				+ " in " + territory.name + " as this has minimum armies");
		System.out.println("Armies count before move: " + territory.name + ": " + territory.numberOfArmies
				+ " armies");
		territory.numberOfArmies += reinforcements;
		System.out.println("Armies count after move: " + territory.name + ": " + territory.numberOfArmies
				+ " armies");
		
	}

	/**
	 *
	 * Fortification phase, whose implementation will
	 * vary depending on the strategy.
	 * 
	 */
	@Override
	public void fortification() {
		ArrayList<Territory> territories = new ArrayList<Territory>(player.assignedTerritories);
		Territory territory = player.territoryWithMinArmy(territories);
		
		while(!player.validAssignedCountry(territory.name)) {
			territories.remove(territory);
			if(territories.isEmpty()) {
				System.out.println("Player doesn't have any adjacent territories.");
				return;
			}
			territory = player.territoryWithMinArmy(territories);
		}
		
		Territory adjacentTerritoryWithMaxArmy = player.territoryWithMaxArmy(territory.neighbours);
		
		int armiesToMove = adjacentTerritoryWithMaxArmy.numberOfArmies - 1;
		
		System.out.println("Player " + player.getName() + " moving " + armiesToMove + " armies from "
				+ adjacentTerritoryWithMaxArmy.name + " to " + territory.name);
		
		System.out.println("Armies count before move: " + territory.name + ": " + territory.numberOfArmies
				+ " , " + adjacentTerritoryWithMaxArmy.name + " : " + adjacentTerritoryWithMaxArmy.numberOfArmies);
		
		territory.numberOfArmies += armiesToMove;
		adjacentTerritoryWithMaxArmy.numberOfArmies -= armiesToMove;
		
		System.out.println("Armies count after the move: " + territory.name + ": " + territory.numberOfArmies
				+ " , " + adjacentTerritoryWithMaxArmy.name + " : " + adjacentTerritoryWithMaxArmy.numberOfArmies);
		
	}

	/**
	 * 
	 * Attack phase, whose implementation will
	 * vary depending on the strategy.
	 *
	 */
	@Override
	public void attack() {
		System.out.println("Skiping attack phase as Benevolent player never attacks");
		player.setCurrentGamePhase(GamePhase.FORTIFICATION);
	}
/**
 * This Method will return string value.
 */
	 @Override
	 public String toString() {
		 return "benevolent";
	 }
}
