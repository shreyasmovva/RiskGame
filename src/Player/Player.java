package Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import Views.CardExchangeView;
import Driver.Main;
import Card.Card;
import Card.Card.CardType;
import Map.Map;
import Map.Map.Territory;
import Player.Player.GamePhase;

/**
 * This class is used to create players, and has all the functionality related
 * to a player like reinforcement, attacking and fortification.
 * 
 * @author Mandeep Ahlawat
 * @version 1.0
 * @since 2018-10-27
 *
 */
public class Player extends Observable {

	/**
	 * The game Phase that can be used by a player in the game
	 *
	 */
	public enum GamePhase {
	/**
	 * This is a REINFORCEMENT phase in the game.
	 */
	REINFORCEMENT,
	/**
	 * This is a ATTACK phase in the game.
	 */
	ATTACK,
	/**
	 * This is a FORTIFICATION phase in the game.
	 */
	FORTIFICATION;
	}

	/**
	 * list of cards.
	 */
	public ArrayList<Card> cards;
	/**
	 * list of territories.
	 */
	public ArrayList<Territory> assignedTerritories;
	/**
	 * Number of armies assigned to a player during startup phase.
	 */
	private int initialArmyCount;
	/**
	 * Number of armies left for a player in integer value.
	 */
	public int armiesLeft;
	/**
	 * Total number of armies a player contains.
	 */
	public int totalArmiesCount;
	/**
	 * private Name of the player in string type.
	 */
	private String name;
	/**
	 * private Players's id in a integer type.
	 */
	private int id;
	/**
	 * id counter which is initialized to zero.
	 */
	private static int idCounter = 0;
	/**
	 * public GamePhase currentGamePhase will take one of the phases mentioned in
	 * GamePhase.
	 */
	public GamePhase currentGamePhase;
	/**
	 * public boolean cardExchangeViewOpen will take true or false value for the
	 * variable if the player wants to open card exchange view.
	 */
	public boolean cardExchangeViewOpen;

	/**
	 * A public strategy object containing players strategy
	 */
	public Strategy playerStrategy;

	/**
	 *
	 * This method is the constructor of the Player class.
	 * 
	 * @param name A String value of the player name.
	 *
	 */
	public Player(String name) {
		idCounter++;
		this.name = name;
		this.id = idCounter;
		this.assignedTerritories = new ArrayList<Territory>();
		this.cards = new ArrayList<Card>();
		this.cardExchangeViewOpen = false;
	}

	/**
	 *
	 * This method sets the player strategy
	 * 
	 * @param strategy object which we wants to set
	 *
	 */
	public void setPlayerStrategy(Strategy strategy) {
		this.playerStrategy = strategy;
	}

	/**
	 * 
	 * This is a getter method, which gives the name of the player.
	 * 
	 * @return name of the player.
	 * 
	 */
	public String getName() {
		return this.name;
	}

	/**
	 *
	 * This method is used to set the state of the object to changed and notify all
	 * its observers
	 *
	 */
	public void setChangeAndNotifyObservers() {
		setChanged();
		notifyObservers();
	}

	/**
	 * 
	 * This is a setter method, which sets the initial army count and the number of
	 * armies left for each player.
	 * 
	 * @param armyCount integer value of the number of armies.
	 * 
	 */
	public void setInitialArmyCount(int armyCount) {
		initialArmyCount = armyCount;
		armiesLeft = armyCount;
		totalArmiesCount = armyCount;
	}

	/**
	 * 
	 * This is a helper method which gives the number of countries a player has with
	 * the number of armies in that country.
	 * 
	 * @return String value with a specific format.
	 *
	 */
	public String assignedTerritoryNamesWithArmies() {
		String territoriesWithArmies = "";
		for (Territory territory : assignedTerritories) {
			territoriesWithArmies += territory.name + " has armies: " + territory.numberOfArmies + "\n";
		}
		return territoriesWithArmies;
	}

	/**
	 *
	 * This method defines Initial placement of the armies and also prints the
	 * player and the name of the country where they want to place a army.
	 * 
	 */
	public void placeArmies() {
		Scanner keyboard = new Scanner(System.in);
		String userInput = null;
		System.out
				.println("Player " + name + " has " + armiesLeft + " armies left." + "\nCountries assigned to him are: "
						+ assignedTerritoryNamesWithArmies() + "\nEnter name of the Country to place an army:");

		userInput = keyboard.nextLine();
		boolean wrongCountryName = true;
		for (Territory territory : assignedTerritories) {
			if (territory.name.equalsIgnoreCase(userInput)) {
				territory.numberOfArmies++;
				armiesLeft--;
				wrongCountryName = false;
				break;
			}
		}
		if (wrongCountryName) {
			System.out.println("Wrong Country, please select the country which is assigned to you.");
			placeArmies();
		}
	}

	/**
	 * 
	 * This method defines in a helper method to place number of armies for each
	 * player automatically instead of going in a round robin fashion.
	 * 
	 */
	public void placeArmiesAutomatically() {
		Random rand = new Random();
		if (initialArmyCount < assignedTerritories.size()) {
			System.out.println("Can't have initial army less than assigned countries as we need"
					+ "to assign at least 1 army to each country");
			System.exit(1);
		}
		for (Territory territory : assignedTerritories) {
			territory.numberOfArmies++;
			armiesLeft--;
			System.out.println("Player " + name + " placed " + 1 + " army in " + territory.name
					+ ". Total armies in this territory are: " + territory.numberOfArmies);
		}
		while (armiesLeft != 0) {
			Territory territory = assignedTerritories.get(rand.nextInt(assignedTerritories.size()));
			territory.numberOfArmies++;
			armiesLeft--;
			System.out.println("Player " + name + " placed " + 1 + " army in " + territory.name
					+ ". Total armies in this territory are: " + territory.numberOfArmies);
		}
	}

	/**
	 * 
	 * Reinforcement phase of the player object
	 *
	 */
	public void reinforcement() {
		playerStrategy.placeReinforcements(playerStrategy.calculateReinforcementArmies());
		setCurrentGamePhase(GamePhase.ATTACK);
	}

	/**
	 * attack phase of the player object
	 */
	public void attack() {
		playerStrategy.attack();
//		setCurrentGamePhase(GamePhase.FORTIFICATION);
	}

	/**
	 * fortification phase of the player object
	 */
	public void fortification() {
		if (!Main.gameFinished) {
			playerStrategy.fortification();
		}
	}

	/**
	 *
	 * The method setCurrentGamePhase will set current game phase according to the
	 * given phase value in main function.
	 * 
	 * @param currentGamePhase will have one phase out of three phases mentioned in
	 *                         GamePhase as a input from the main function.
	 *
	 */
	public void setCurrentGamePhase(GamePhase currentGamePhase) {
		this.currentGamePhase = currentGamePhase;
		setChanged();
		notifyObservers(this);
	}

	/**
	 *
	 * To check if the players owns the country and check's the conditions and it
	 * will return true or false.
	 * 
	 * @param country is a string value in which the name of the country is
	 *                mentioned.
	 * 
	 * @return true if the country belongs to the player.
	 * 
	 * @return false if the country doesn't belongs to player.
	 *
	 */
	public boolean validAssignedCountry(String country) {
		for (Territory territory : assignedTerritories) {
			if (territory.name.equalsIgnoreCase(country)) {
				for (Territory neighbour : territory.neighbours) {
					if (neighbour.owner == this) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 *
	 * To check if the neighbour country is owned by the current player or not.
	 * 
	 * 
	 * @param fromCountry is a string value in which a player can mention to move a
	 *                    army from a country.
	 * 
	 * @param toCountry   is a string value in which a player can mention to move a
	 *                    army from a country to another country.
	 * 
	 * @return true if the given condition satisfies.
	 * 
	 * @return false if the given condition doesn't satisfies.
	 *
	 */
	public boolean validNeighborCountry(String fromCountry, String toCountry) {
		for (Territory territory : assignedTerritories) {
			if (territory.name.equalsIgnoreCase(fromCountry)) {
				for (Territory neighbor : territory.neighbours) {
					if (neighbor.name.equalsIgnoreCase(toCountry) && neighbor.owner == this)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 *
	 * The public method will have valid opponent's country which will be used in
	 * attack phase.
	 * 
	 * @param fromCountry will have player present country as a string value.
	 * 
	 * @param toCountry   is a string value in which player mention's the name of
	 *                    the country to move.
	 * 
	 * @return true if the given conditions pass otherwise it will return false.
	 *
	 */
	public boolean validOpponentCountry(String fromCountry, String toCountry) {
		for (Territory territory : assignedTerritories) {
			if (territory.name.equalsIgnoreCase(fromCountry)) {
				for (Territory neighbor : territory.neighbours) {
					if (neighbor.name.equalsIgnoreCase(toCountry) && neighbor.owner != this)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 *
	 * The public method opponentPlayer will return name of the player who is
	 * present in neighbour country.
	 * 
	 * @param fromCountry will have player present country as a string value.
	 * 
	 * @param toCountry   is a string value in which player mention's the name of
	 *                    the country to check neighbour.
	 * 
	 * @return name of the player in neighbour country only if it passes the
	 *         conditions if not it will return null value.
	 *
	 */
	public String opponentPlayer(String fromCountry, String toCountry) {
		for (Territory territory : assignedTerritories) {
			if (territory.name.equalsIgnoreCase(fromCountry)) {
				for (Territory neighbor : territory.neighbours) {
					if (neighbor.name.equalsIgnoreCase(toCountry))
						return neighbor.owner.name;
				}
			}
		}
		return null;
	}

	/**
	 *
	 * The public method opponentPlayer will return name of the player who is
	 * present in neighbour country.
	 * 
	 * @param fromCountry will have player present country as a string value.
	 * 
	 * @param toCountry   is a string value in which player mention's the name of
	 *                    the country to check neighbour.
	 * 
	 * @return name of the player in neighbour country only if it passes the
	 *         conditions if not it will return null value.
	 *
	 */
	public String attackingPlayer(String fromCountry, String toCountry) {
		for (Territory territory : assignedTerritories) {
			if (territory.name.equalsIgnoreCase(fromCountry)) {
				return territory.owner.name;
			}
		}
		return null;
	}

	/**
	 *
	 * The public method to check if a player can attack from this country which is
	 * present here.
	 * 
	 * @param country will have player's current country value in string type.
	 * 
	 * @return true if the given conditions passes otherwise it will return false.
	 *
	 */
	public boolean canAttackFromThisCountry(String country) {
		for (Territory territory : assignedTerritories) {
			if (territory.name.equalsIgnoreCase(country)) {
				if (territory.numberOfArmies > 1)
					return true;
			}
		}
		return false;
	}

	/**
	 *
	 * The public method will calculate the number of dice for attacker or defender
	 * depending on the number of armies that player has,will decide whether to roll
	 * 1,2 or 3 dice.
	 * 
	 * @param status          string value will have attacker or defender in it.
	 * 
	 * @param attackerCounter string value of the attacker counter.
	 * 
	 * @param defenderCountry string value of the defender country.
	 * 
	 * @return input provided in the method which is initialized to zero.
	 *
	 */
	public int calculateNumberOfDiceAllowed(String status, String attackerCounter, String defenderCountry,
			boolean isAllOut, boolean isRandom) {
		Scanner keyboard = new Scanner(System.in);
		Random random = new Random();
		int input = 0;
		if (status.equalsIgnoreCase("attacker")) {
			int numberOfArmies = 0;
			for (Territory territory : assignedTerritories) {
				if (territory.name.equalsIgnoreCase(attackerCounter)) {
					numberOfArmies = territory.numberOfArmies;
				}
			}
			if (numberOfArmies == 2) {
				input = 1;
			} else if (numberOfArmies == 3) {
				if (isAllOut) {
					return 2;
				} else if (isRandom) {
					input = random.nextInt(2)+1;
				} else {
					System.out.println("Choose if you would like to roll 1 or 2 Dice:");
					while (input == 0) {
						input = Integer.parseInt(keyboard.nextLine().trim());
						if (!(input == 1 || input == 2)) {
							System.out.println("You can only choose between 1 or 2 Dice:");
							input = 0;
						}
					}
				}
			} else {
				if (isAllOut) {
					return 3;
				} else if (isRandom) {
					input = random.nextInt(3)+1;
				} else {
					System.out.println("Choose if you would like to roll 1 or 2 or 3 Dice:");
					while (input == 0) {
						input = Integer.parseInt(keyboard.nextLine().trim());
						if (!(input == 1 || input == 2 || input == 3)) {
							System.out.println("You can only choose between 1 or 2 or 3 Dice:");
							input = 0;
						}
					}
				}
			}
		} else {
			int numberOfArmies = 0;
			for (Territory territory : assignedTerritories) {
				if (territory.name.equalsIgnoreCase(attackerCounter)) {
					for (Territory neighbor : territory.neighbours) {
						if (neighbor.name.equalsIgnoreCase(defenderCountry))
							numberOfArmies = neighbor.numberOfArmies;
					}
				}
			}
			if (numberOfArmies == 1) {
				input = 1;
			} else {
				if (isAllOut) {
					return 2;
				} else if (isRandom) {
					input = random.nextInt(2)+1;
				} else {
					System.out.println("Choose if you would like to roll 1 or 2 Dice:");
					while (input == 0) {
						input = Integer.parseInt(keyboard.nextLine().trim());
						if (!(input == 1 || input == 2)) {
							System.out.println("You can only choose between 1 or 2 Dice:");
							input = 0;
						}
					}
				}
			}
		}
		return input;
	}

	/**
	 *
	 * Public method will give the player's value of dice after rolling in the game.
	 * 
	 * @param numberOfDice a integer value will give the dice number.
	 * 
	 * @return values of the dice a object defined in a method.
	 *
	 */
	public Vector<Integer> rollDice(int numberOfDice) {
		Vector<Integer> diceValues = new Vector<Integer>(numberOfDice);
		Random r = new Random();
		while (numberOfDice != 0) {
			diceValues.addElement(r.nextInt((6 - 1) + 1) + 1);
			numberOfDice--;
		}
		Collections.sort(diceValues);
		return diceValues;
	}

	/**
	 *
	 * Public method which reduces one army in the losing players territory.
	 * 
	 * @param losingPlayer    will have string value type in it.
	 * 
	 * @param attackerCountry will have string value type in it.
	 * 
	 * @param defenderCountry will have string value type in it.
	 *
	 */
	public void reduceArmy(String losingPlayer, String attackerCountry, String defenderCountry) {
		if(losingPlayer.equalsIgnoreCase("attacker")) {
			Territory territory = Map.findTerritory(attackerCountry);
			territory.numberOfArmies--;
		}
		else {
			Territory territory = Map.findTerritory(defenderCountry);
			territory.numberOfArmies--;
		}		
	}

	/**
	 * 
	 * The method will check the number of armies in the defender whether zero or
	 * not.
	 * 
	 * @param attackerCounter which is a string value.
	 * 
	 * @param defenderCountry which is a string value.
	 * 
	 * @return true if all the conditions passes otherwise return's false.
	 *
	 */
	public boolean checkDefenderArmiesNumberZero(String attackerCounter, String defenderCountry) {
		for (Territory territory : assignedTerritories) {

			if (territory.name.equalsIgnoreCase(attackerCounter)) {
				for (Territory neighbor : territory.neighbours) {
					if (neighbor.name.equalsIgnoreCase(defenderCountry))
						if (neighbor.numberOfArmies == 0)
							return true;
				}
			}
		}
		return false;
	}

	/**
	 *
	 * The method will move armies to new territory conquered only if defender
	 * looses it.
	 * 
	 * @param attackerCounter will have string value type in it.
	 * 
	 * @param defenderCountry will have string value type in it.
	 * 
	 * @return returns a vector of number of armies left
	 *
	 */
	public Vector<Integer> returnArmiesLeft(String attackerCounter, String defenderCountry) {
		Vector<Integer> armies = new Vector<>();
		for (Territory territory : assignedTerritories) {
			if (territory.name.equalsIgnoreCase(attackerCounter)) {
				armies.add(0, territory.numberOfArmies);
				for (Territory neighbor : territory.neighbours) {
					if (neighbor.name.equalsIgnoreCase(defenderCountry))
						armies.add(1, neighbor.numberOfArmies);
				}
			}
		}
		return armies;
	}

	/**
	 *
	 * The method will move armies to new territory conquered only if defender
	 * looses it.
	 * 
	 * @param attackerCountry will have string value type in it.
	 * 
	 * @param defenderCountry      will have string value type in it.
	 * 
	 * @param numberOfArmiesToMove a integer value will have number of armies to
	 *                             move value in it.
	 *
	 */
	public void moveArmiesToNewTerritory(String attackerCountry, String defenderCountry, int numberOfArmiesToMove) {
		Territory attacker = Map.findTerritory(attackerCountry);
		Territory defender = Map.findTerritory(defenderCountry);
		
		while(numberOfArmiesToMove >= attacker.numberOfArmies) {
			Scanner input = new Scanner(System.in);
			System.out.println("Enter a valid number of troops. "
					+ "Valid number of troops should be less than : " +  attacker.numberOfArmies);
			numberOfArmiesToMove = input.nextInt();
		}
		
		attacker.numberOfArmies -= numberOfArmiesToMove;
		defender.numberOfArmies += numberOfArmiesToMove;
		
	}

	/**
	 *
	 * The method will exchange Cards between the players.
	 * 
	 * @param cardIndex1 a integer value.
	 * 
	 * @param cardIndex2 a integer value.
	 * 
	 * @param cardIndex3 a integer value.
	 *
	 */
	public void exchangeCards(int cardIndex1, int cardIndex2, int cardIndex3) {
		Card card1 = cards.get(cardIndex1);
		Card card2 = cards.get(cardIndex2);
		Card card3 = cards.get(cardIndex3);

		cards.remove(card1);
		cards.remove(card2);
		cards.remove(card3);
		Card.cardExchangeValue += 5;
	}

	/**
	 * This method will be having the maximum number of army of a territory
	 * 
	 * @param territories list of territories
	 * 
	 * @return maximum number of armies in a territory
	 *
	 */
	public Territory territoryWithMaxArmy(ArrayList<Territory> territories) {
		Territory territoryWithMaxArmy = null;
		int maxArmies = 0;
		for (Territory territory : territories) {
			if (territory.numberOfArmies >= maxArmies && territory.owner == this) {
				maxArmies = territory.numberOfArmies;
				territoryWithMaxArmy = territory;
			}
		}
		return territoryWithMaxArmy;
	}

	/**
	 * This method will be having the minimum number of army of a territory
	 * 
	 * @param territories list of territories
	 * 
	 * @return minimum number of armies in a territory
	 *
	 */
	public Territory territoryWithMinArmy(ArrayList<Territory> territories) {
		Territory territoryWithMinArmy = null;
		int minArmies = Integer.MAX_VALUE;
		for (Territory territory : territories) {
			if (territory.numberOfArmies <= minArmies && territory.owner == this) {
				minArmies = territory.numberOfArmies;
				territoryWithMinArmy = territory;
			}
		}
		return territoryWithMinArmy;
	}

	/**
	 * This method will get the random territory in the game.
	 * 
	 * @param territories list of territories
	 * 
	 * @return the size of the random territories.
	 */
	public Territory getRandomTerritory(ArrayList<Territory> territories) {
		return territories.get(new Random().nextInt(territories.size()));
	}

	/**
	 * 
	 * This method will get territories which is neighbour to other player in the
	 * game.
	 * 
	 * 
	 * @return the number of territories
	 */
	public HashSet<Territory> getTerritoriesWithNeighboursToOthers() {
		HashSet<Territory> territories = new HashSet<Territory>();
		for (Territory territory : assignedTerritories) {
			for (Territory neighbour : territory.neighbours) {
				if (neighbour.owner != this) {
					territories.add(territory);
					break;
				}
			}
		}
		return territories;
	}

	/**
	 * 
	 * This method will get territories which is neighbour to other player in the
	 * game.
	 * 
	 * @param territory list of territories
	 * 
	 * @return the number of territories
	 * 
	 */
	public HashSet<Territory> getTerritoriesWithNeighboursToOthers(Territory territory) {
		HashSet<Territory> territories = new HashSet<Territory>();
		for (Territory neighbour : territory.neighbours) {
			if (neighbour.owner != this) {
				territories.add(neighbour);
			}
		}
		return territories;
	}

	/**
	 * This method will add new owned territory to a player eliminates the pervious
	 * owner in it.
	 * 
	 * @param territory list of territories
	 * 
	 */
	public void addNewOwnedTerritory(Territory territory) {
		Player previousOwner = territory.owner;
		previousOwner.assignedTerritories.remove(territory);
		previousOwner.totalArmiesCount -= territory.numberOfArmies;
		if (previousOwner.assignedTerritories.size() == 0) {
			previousOwner.gotEliminated(this);
		}

		this.totalArmiesCount += territory.numberOfArmies;
		territory.owner = this;
		this.assignedTerritories.add(territory);
		if (territory.card.canAssignedToPlayer(this)) {
			territory.card.assignPlayer(this);
		}
	}

	/**
	 * The method will display the name of the player who got eliminated in the game
	 * 
	 * @param playerWhoEliminated
	 *
	 */
	public void gotEliminated(Player playerWhoEliminated) {
		System.out.println(" ======= " + this.name + " got eliminated =======");
		if (this.cards.size() > 0) {
			System.out.println(
					" ======= Assigning " + this.name + " cards to " + playerWhoEliminated.getName() + " =======");
			for (Card card : cards) {
				playerWhoEliminated.cards.add(card);
			}
			cards.clear();
		}
	}

	/**
	 *
	 * This method is used to automatically decides which combination of card
	 * indexes the player can exchange
	 * 
	 * @return an array list of card indexes
	 *
	 */
	public ArrayList<Integer> getCardIndexesToExchange() {
		ArrayList<Integer> cardIndexes = new ArrayList<Integer>();
		ArrayList<CardType> addedCardTypes = new ArrayList<CardType>();

		if (cards.size() == 3) {
			cardIndexes.add(0);
			cardIndexes.add(1);
			cardIndexes.add(2);
		} else {
			ArrayList<CardType> cardTypes = new ArrayList<CardType>();
			for (Card card : cards) {
				cardTypes.add(card.type);
			}

			for (Card card : cards) {
				ArrayList<CardType> tempTypes = new ArrayList<CardType>(cardTypes);
				int oldSize = tempTypes.size();
				for (int i = 0; i < 3; ++i) {
					tempTypes.remove(card.type);
				}
				int newSize = tempTypes.size();
				if (oldSize - newSize >= 3) {
					cardIndexes.clear();
					addedCardTypes.clear();
					for (int i = 0; i < cardTypes.size(); ++i) {
						if (cardIndexes.size() <= 3 && card.type.equals(cardTypes.get(i))) {
							cardIndexes.add(i);
							addedCardTypes.add(cardTypes.get(i));
						}
					}
				} else if (!addedCardTypes.contains(card.type)) {
					cardIndexes.add(cardTypes.indexOf(card.type));
				}
				if (cardIndexes.size() == 3) {
					break;
				}
			}
		}

		return cardIndexes;
	}

	/**
	 *
	 * The method will check or valid the particular card which can be exchanged or
	 * not.
	 * 
	 * @param cardIndex1 a integer value.
	 * 
	 * @param cardIndex2 a integer value.
	 * 
	 * @param cardIndex3 a integer value.
	 * 
	 * @return true if the given condition satifies.
	 * 
	 * @return false if the given condition doesn't satifies.
	 *
	 */
	public boolean validCardIndexesToExchange(int cardIndex1, int cardIndex2, int cardIndex3) {
		CardType cardType1 = cards.get(cardIndex1).type;
		CardType cardType2 = cards.get(cardIndex2).type;
		CardType cardType3 = cards.get(cardIndex3).type;

		if (cardType1 != cardType2) {
			if (cardType2 != cardType3 && cardType1 != cardType3) {
				return true;
			}
		}

		if (cardType1 == cardType2 && cardType2 == cardType3) {
			return true;
		}

		return false;
	}

	/**
	 *
	 * The boolean method canExchangeCards will gives clear clarity whether a player
	 * can exchange the card or not.
	 * 
	 * @return true if the given condition satifies.
	 * 
	 * @return false if the given condition doesn't satifies.
	 *
	 */
	public boolean canExchangeCards() {
		if (cards.size() == 5) {
			return true;
		}

		ArrayList<CardType> differentCardTypes = new ArrayList<CardType>();

		for (Card card : cards) {
			if (!differentCardTypes.contains(card.type)) {
				differentCardTypes.add(card.type);
			}
		}

		if (differentCardTypes.size() == 3) {
			return true;
		}

		ArrayList<CardType> cardTypes = new ArrayList<CardType>();
		for (Card card : cards) {
			cardTypes.add(card.type);
		}

		for (Card card : cards) {
			ArrayList<CardType> tempCardTypes = new ArrayList<CardType>(cardTypes);
			for (int i = 0; i < 3; ++i) {
				tempCardTypes.remove(card.type);
			}
			if (cardTypes.size() - tempCardTypes.size() >= 3) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * The method will have list of owned continents of player in a game.
	 * 
	 * 
	 * @return object of array list of owned continents.
	 *
	 */
	public ArrayList<Map> ownedContinents() {
		ArrayList<Map> ownedContinents = new ArrayList<Map>();
		for (Map continent : Main.activeMap.continents) {
			ArrayList<Player> territoryOwners = new ArrayList<Player>();
			boolean ownContinent = true;
			for (Territory territory : continent.territories) {
				if (territory.owner == this) {
					if (territoryOwners.isEmpty()) {
						territoryOwners.add(territory.owner);
					} else {
						if (!territoryOwners.contains(territory.owner)) {
							ownContinent = false;
							break;
						}
					}
				} else {
					ownContinent = false;
					break;
				}
			}
			if (ownContinent) {
				ownedContinents.add(continent);
			}
		}
		return ownedContinents;
	}
	
	/**
	 * This method is used to resets all the data
	 * related to players currently playing the game
	 */
	public static void resetPlayersData() {
		for(Player player : Main.players) {
			player.cards.clear();
			player.assignedTerritories.clear();
			player.initialArmyCount = 0;
			player.armiesLeft = 0;
			player.totalArmiesCount = 0;
		}
	}
}