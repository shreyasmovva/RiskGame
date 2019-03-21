package Views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import Card.Card;
import Driver.Main;
import Map.Map;
import Map.Map.Territory;
import Player.Player;

/**
 * This class will implement from observer
 * and it display's the territory of 
 * neighbour armies, and also all countries
 * and territory in world map.
 *  
 * @author Mandeep Ahlawat
 * @version 1.0
 * @since 04/11/2018
 * 
 */
public class WorldDominationView implements Observer {
	
	/**
	 * This method will display the namee of the country 
	 * along with the continent and there armies.
	 * 
	 * @param territory object of territory.
	 * 
	 * @param neighbour used to check for the presence
	 * of the opponent or enemy player.
	 */
	public void countryNameWithContinentAndArmy(Territory territory, boolean neighbour) {
		if(neighbour) {
			System.out.print("\t"
					+ territory.name 
					+ " Armies : " + territory.numberOfArmies
					+ ", Continent: " + territory.continent.name
					+ ", Owner: " + territory.owner.getName());
		}
		else {
			System.out.print(territory.name 
					+ " Armies : " + territory.numberOfArmies
					+ ", Continent: " + territory.continent.name);
		}
	}
	
	/**
	 * this method will display the current status of map.
	 * Which player owns what country.
	 * How many armies that player has in that country.
	 * Which are the neighbouring territories of this country.
	 * 
	 * @param activeMap object of map is created.
	 * 
	 */
	public void display(Map activeMap) {
		for(Player player : Main.players) {
			int ownedTerritories = player.assignedTerritories.size();
			int totalTerritories = activeMap.territories.size();
			float percentTerritoriesOwn = (ownedTerritories * 100 / totalTerritories);
			System.out.println("=================="
					+ player.getName()
					+" INFORMATION ==================================");
			
			System.out.println(player.getName() + " owns " + percentTerritoriesOwn + 
					"% territories");
			System.out.println(player.getName() + " owns following countries: ");
			for(Territory territory : player.assignedTerritories) {
				countryNameWithContinentAndArmy(territory, false);
				System.out.println("");
				System.out.println("Neighbouring countries:");
				for(Territory neighbour : territory.neighbours) {
					countryNameWithContinentAndArmy(neighbour, true);
					System.out.println("");
				}
			}
			
			System.out.println(player.getName() + " owns " + player.totalArmiesCount + 
					" total armies");
			
			ArrayList<Map> ownedContinents = player.ownedContinents();
			if(ownedContinents.size() > 0) {
				System.out.println(player.getName() + " owns following continents: ");
				for(Map continent : ownedContinents) {
					System.out.println(continent.name);
				}
			}
			else {
				System.out.println(player.getName() + " doesn't own any continents");
			}
			
			if(player.cards.isEmpty()) {
				System.out.println("Player doesn't have any cards yet.");
			}
			else {
				System.out.println("Player has following "
						+ player.cards.size() + " cards.");
				for(Card card : player.cards) {
					System.out.println(card);
				}
			}
			
			System.out.println("====================================================");
		}
	}
	
	/**
	 *  {@inheritDoc}
	 * This method print's out start and end 
	 * of world domination map and display's
	 * the current map after update method 
	 * is called.
	 */
	@Override
	public void update(Observable o, Object arg) {
		Map currentMap = (Map) o;
		System.out.println("\n*********************** WORLD DOMINATION VIEW START *****************************\n");
		display(currentMap);
		System.out.println("\n*********************** WORLD DOMINATION VIEW ENDS *****************************\n");
	}

}
