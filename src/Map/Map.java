package Map;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Stack;

import Card.Card;
import Player.Player;

/**
 * This class is used to create a full Map, a map has a list of continents and
 * territories. A continent is just a sub Map which is part of the overall map.
 * 
 * @author Mandeep Ahlawat
 * @version 1.0
 * @since 2018-09-30
 */
public class Map extends Observable {
	/**
	 * List of continents in map. this will be used for the main map.
	 */
	public ArrayList<Map> continents;
	/**
	 * List of territories in the game.
	 */
	public ArrayList<Territory> territories;
	/**
	 * integer value of the score.
	 */
	public int score;
	/**
	 * String type name.
	 */
	public String name;
	/**
	 * Object of map is created name given as parentMap. 
	 */
	public Map parentMap;
	/**
	 * List of all the territories in the map.
	 */
	public static ArrayList<Territory> listOfAllTerritories;
	/**
	 * List of all the continents in the map.
	 */
	public static ArrayList<Map> listOfAllContinents;
	/**
	 * visited is assingned false. 
	 */
	public boolean visited = false;

	/**
	 * This method is used to find a Territory.
	 * 
	 * @param territoryName A String value of the territory name.
	 * 
	 * @return Territory if territory is found then returns territory otherwise.
	 *         returns null.
	 */
	public static Territory findTerritory(String territoryName) {
		for (Territory territory : listOfAllTerritories) {
			if (territory.name.equalsIgnoreCase(territoryName)) {
				return territory;
			}
		}
		return null;
	}

	/**
	 * The method will display all territories owned
	 * by a single player in the game.
	 * 
	 * @param notifyObserver a boolean value if you want
	 * to notify the observers or not
	 * 
	 * @return true if it satisfies all the conditions 
	 * otherwise it will return false. 
	 *  
	 */
	public boolean allTerritoriesOwnBySinglePlayer(boolean notifyObserver) {
		if(notifyObserver) {
			setChanged();
			notifyObservers(this);
		}
		ArrayList<String> ownerNames = new ArrayList<String>();
		for (Territory territory : territories) {
			if (ownerNames.isEmpty()) {
				ownerNames.add(territory.owner.getName());
			} else {
				if (!ownerNames.contains(territory.owner.getName())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This method is used to find a Continent.
	 * 
	 * @param continentName A String value of the continent name.
	 * 
	 * @return Continent if continent is found then returns continent otherwise
	 *         returns null.
	 */
	public static Map findContinent(String continentName) {
		for (Map continent : listOfAllContinents) {
			if (continent.name.equals(continentName)) {
				return continent;
			}
		}
		return null;
	}

	/**
	 * This is the default constructor of the Map
	 * class and will be used to create
	 * the main game play map.
	 */
	public Map() {
		this.territories = new ArrayList<Territory>();
		this.continents = new ArrayList<Map>();
		this.parentMap = null; // as this is the parent map
		listOfAllTerritories = new ArrayList<Territory>();
		listOfAllContinents = new ArrayList<Map>();
	}

	/**
	 * This method is a constructor of the Map class which will be mainly used to
	 * create a continent inside a map.
	 * 
	 * @param continentName  A String value of the continent name.
	 * 
	 * @param continentScore A integer value of the continent score.
	 */
	public Map(String continentName, int continentScore) {
		this.name = continentName;
		this.score = continentScore;

		this.territories = new ArrayList<Territory>();
		this.continents = null; // a continent can't have continents inside it.

		listOfAllContinents.add(this);
	}

	/**
	 * This method is used to add a continent to the Map.
	 * 
	 * @param continentName  A String value of the continent name.
	 * 
	 * @param continentScore A integer value of the continent score.
	 * 
	 * @return true if the continent is not already created, otherwise returns false
	 *         as a continent with the same already exists.
	 */
	public boolean addContinent(String continentName, int continentScore) {
		if (findContinent(continentName) == null) {
			Map continent = new Map(continentName, continentScore);
			continent.parentMap = this;
			this.continents.add(continent);
			return true;
		}
		return false;
	}

	/**
	 * This method is used to validate the map by carrying out depth first search
	 * (DFS) on a graph of territories. If after the DFS any territory is left
	 * unvisited then the graph is not connected and the function returns false.
	 * Otherwise, if all the territories are visited, the resulting graph is
	 * connected and the method returns true. Also, this method marks continent as
	 * visited as we implement DFS, in order to check that there is no continent
	 * without any territory assigned to it.
	 * 
	 * @return true if the map is connected.
	 */
	public boolean validateMap(boolean isContinent) {
		
		//make all countries not visited
		for (Territory territoryBeingVisited : this.territories) {
			territoryBeingVisited.visited = false;
		}
		
		Stack<Territory> stack = new Stack<>();
		stack.push(this.territories.get(0));
		while (!stack.isEmpty()) {
			Territory territoryBeingVisited = stack.pop();
			territoryBeingVisited.visited = true;

			if (territoryBeingVisited.continent == null) {
				System.out.println("INVALID MAP");
				System.out.println(territoryBeingVisited.name + " is not assigned any continent");
				return false;
			}
			else if (territoryBeingVisited.continent.visited == false) {
				territoryBeingVisited.continent.visited = true;
			}
			
			for (Territory neighbour : territoryBeingVisited.neighbours) {
				if(!isContinent) {
					if (!neighbour.visited) {
						stack.push(neighbour);
					}
				}
				else {
					if (!neighbour.visited && neighbour.continent.name.equals(territoryBeingVisited.continent.name)) {
						stack.push(neighbour);
					}
				}
			}
		}

		for (Territory territoryBeingVisited : this.territories) {
			if (!territoryBeingVisited.visited) {
				System.out.println("INVALID MAP");
				System.out.println(territoryBeingVisited.name + " is not connected");
				return false;
			}
		}

		if(this.continents != null) {
			for (Map continentBeingVisited : this.continents) {
				if (!continentBeingVisited.visited) {
					System.out.println("INVALID MAP");
					System.out.println(continentBeingVisited.name + " doesn't have any territory inside it");
					return false;
				}
			}
			
			// call validateMap for each continent separately to check if
			// continent is connected or not
			for(Map continent : this.continents) {
				if(!continent.validateMap(true)) {
					System.out.println(continent.name + " is not valid as it's not connected");
					return false;
				}
			}
		}
		

		return true;
	}

	/**
	 * This inner class is used to create a Map. It adds the behavior of territories
	 * to the map, where each territory has a name, belongs to a continent and has a
	 * list of neighbours.
	 * 
	 */
	public static class Territory {
		public String name;
		public Map continent;
		public ArrayList<Territory> neighbours;
		public boolean visited = false;
		public Player owner;
		public int numberOfArmies;
		public Card card;

		/**
		 * This method is the constructor of the Territory class.
		 * 
		 * @param name A String value of the territory name.
		 */
		public Territory(String name) {
			this.owner = null;
			this.numberOfArmies = 0;
			this.name = name;
			this.continent = null;
			this.neighbours = new ArrayList<Territory>();
			this.card = new Card(this);
			listOfAllTerritories.add(this);
		}

		/**
		 * This method is used to add neigbours of a territory.
		 * 
		 * @param neighbourList A String value where each neigbouring territory is
		 *                      separated by a comma (',')
		 */
		public void addNeighbours(String neighbourList) {
			String neigboursNameArray[] = neighbourList.split(",");
			for (String neighbourName : neigboursNameArray) {
				Territory territory = null;
				if ((territory = findTerritory(neighbourName)) != null) {
					if (!this.neighbours.contains(territory)) {
						this.neighbours.add(territory);
					}
					if (!territory.neighbours.contains(this)) {
						territory.neighbours.add(this);
					}
				} else {
					if (!this.neighbours.contains(territory)) {
						territory = new Territory(neighbourName);
						this.neighbours.add(territory);
					}
					if (!territory.neighbours.contains(this)) {
						territory.neighbours.add(this);
					}
				}
			}
		}

		/**
		 * This method is used to assign continent to a territory.
		 * 
		 * @param continentName A String value of the continent name.
		 * 
		 * @return true if the continent is assigned otherwise returns false.
		 */
		public boolean assignContinent(String continentName) {
			if (this.continent == null) {
				Map continent = null;
				if ((continent = findContinent(continentName)) != null) {
					this.continent = continent;
					continent.territories.add(this);
					return true;
				}
			}
			return false;
		}
	}
}
