package Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import Card.Card;
import Driver.Main;
import Map.Map;
import Map.Map.Territory;
import Player.Player.GamePhase;

/**
 * The RandomStrategy class is implementing functions 
 * in Strategy interface
 * 
 * @author mandeepahlawat
 * @version 1.0
 * @since 24-11-2018
 *
 */
public class RandomStrategy implements Strategy {
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
	 */
	public RandomStrategy(Player player) {
		this.player = player;
	}
	
	/**
	 *
	 * Calculation of reinforcement armies,
	 * which will vary depending on the strategy.
	 * 
	 * @return int value of the reinforcement armies
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
		Territory territory = player.getRandomTerritory(player.assignedTerritories);
		System.out.println("\nPlayer " + player.getName() + " places " + reinforcements
				+ " in " + territory.name + " as this is selected randomly");
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
		Territory territory = player.getRandomTerritory(territories);
		
		while(!player.validAssignedCountry(territory.name)) {
			territories.remove(territory);
			if(territories.isEmpty()) {
				System.out.println("Player doesn't have any adjacent territories.");
				return;
			}
			territory = player.getRandomTerritory(territories);
		}
		
		Territory adjacentRandomTerritory = player.getRandomTerritory(territory.neighbours);
		
		int armiesToMove = new Random().nextInt(adjacentRandomTerritory.numberOfArmies);
		
		System.out.println("Player " + player.getName() + " moving " + armiesToMove + " armies from "
				+ adjacentRandomTerritory.name + " to " + territory.name);
		
		System.out.println("Armies count before move: " + territory.name + ": " + territory.numberOfArmies
				+ " , " + adjacentRandomTerritory.name + " : " + adjacentRandomTerritory.numberOfArmies);
		
		territory.numberOfArmies += armiesToMove;
		adjacentRandomTerritory.numberOfArmies -= armiesToMove;
		
		System.out.println("Armies count after the move: " + territory.name + ": " + territory.numberOfArmies
				+ " , " + adjacentRandomTerritory.name + " : " + adjacentRandomTerritory.numberOfArmies);
		
	}

	/**
	 * 
	 * Attack phase, whose implementation will
	 * vary depending on the strategy.
	 *
	 */
	
	@Override
	public void attack() {

		boolean attackDone = false;
		boolean gameCompleted = false;
		boolean defenderLost = false;
		String attackFrom = "", attackAt = "", opponent = "", attacker = "";
		Random random = new Random();

		ArrayList<String> answers = new ArrayList<String>(); 
		answers.add("yes");
		answers.add("no");
		String answer = answers.get(random.nextInt(2));

		if (answer.equalsIgnoreCase("yes")) {
			while (!attackDone) {
				attackFrom = player.assignedTerritories.get(random.nextInt(player.assignedTerritories.size())).name;
				while(!player.canAttackFromThisCountry(attackFrom)) {
					attackFrom = player.assignedTerritories.get(random.nextInt(player.assignedTerritories.size())).name;
				}
				if (player.canAttackFromThisCountry(attackFrom)) {
					System.out.println("Attacker chose to attack from "+ attackFrom + " randomly");
					Territory attacking = Map.findTerritory(attackFrom);
					attackAt = attacking.neighbours.get(random.nextInt(attacking.neighbours.size())).name;
					while(!player.validOpponentCountry(attackFrom, attackAt)) {
						attackAt = attacking.neighbours.get(random.nextInt(attacking.neighbours.size())).name;
					}
					if (player.validOpponentCountry(attackFrom, attackAt)) {
						System.out.println("Attacker chose to attack "+ attackAt + " randomly");
						boolean finishedAttackingThatTerritory = false; // finished attacking the present territory
						boolean allOutMode = false;

						String input = answers.get(random.nextInt(2));
						if (input.equalsIgnoreCase("yes")) {
							System.out.println("Attacking in all out mode");
							allOutMode = true;
						}
						
						while (!finishedAttackingThatTerritory) {
							Vector<Integer> armies = player.returnArmiesLeft(attackFrom, attackAt);
							System.out.println(
									attackFrom + ":" + armies.get(0) + " vs " + attackAt + ":" + armies.get(1));
							opponent = player.opponentPlayer(attackFrom, attackAt);
							attacker = player.attackingPlayer(attackFrom, attackAt);

							if (armies.get(0) == 1) {
								System.out.println("The attacker cannot proceed with the attack with just 1 army");
								finishedAttackingThatTerritory = true;
								attackDone = true;
								break;
							} else if (armies.get(1) == 0) {
								System.out.println(attackFrom + " won the battle!");
								finishedAttackingThatTerritory = true;
								attackDone = true;
								defenderLost = true;
								break;
							} else {
								String continueAttack = "yes";
								if(!allOutMode) {
								continueAttack = answers.get(random.nextInt(2));
								if(continueAttack.equalsIgnoreCase("yes")) {
									System.out.println("Player chose to continue attacking the territory");
								}
								}
								if (continueAttack.equalsIgnoreCase("yes")) {
									Vector<Integer> attackerDice, defenderDice;
									int attackerDiceCount = 0;
									int defenderDiceCount = 0;
									if (allOutMode) {
										attackerDiceCount = player.calculateNumberOfDiceAllowed("attacker", attackFrom,
												attackAt, allOutMode, false);
										attackerDice = player.rollDice(attackerDiceCount);

										defenderDiceCount = player.calculateNumberOfDiceAllowed("defender", attackFrom,
												attackAt, allOutMode, false);
										defenderDice = player.rollDice(defenderDiceCount);
									} else {
										attackerDiceCount = player.calculateNumberOfDiceAllowed("attacker", attackFrom,
												attackAt, allOutMode, true);
										System.out.println("Attacker chose to roll " + attackerDiceCount + " dice randomly.");
										attackerDice = player.rollDice(attackerDiceCount);

										defenderDiceCount = player.calculateNumberOfDiceAllowed("defender", attackFrom,
												attackAt, allOutMode, true);
										System.out.println("Attacker chose to roll " + defenderDiceCount + " dice randomly.");
										defenderDice = player.rollDice(defenderDiceCount);
									}
									while (!attackerDice.isEmpty() && !defenderDice.isEmpty()) {
										int attackerDiceValue = attackerDice.remove(attackerDice.size() - 1);
										int defenderDiceValue = defenderDice.remove(defenderDice.size() - 1);
										if (attackerDiceValue > defenderDiceValue) {
											player.reduceArmy("defender", attackFrom, attackAt);
											Map.findTerritory(attackAt).owner.totalArmiesCount--;
										} else {
											player.reduceArmy("attacker", attackFrom, attackAt);
											player.totalArmiesCount--;
										}
									}
								}
								else {
									System.out.println("Player chose to Leave attack in the middle.");
									finishedAttackingThatTerritory = true;
									attackDone = true;
									break;
								}
							}
						}
					} else {
						System.out.println("Enter a valid country you would like to attack");
					}
				} else {
					System.out.println("Enter a valid country you would like to attack from");
				}
			}

			if (defenderLost) {
				for (Player player : Main.players) {
					if (player.getName().equalsIgnoreCase(attacker)) {
						Territory territory = Map.findTerritory(attackAt);
						player.addNewOwnedTerritory(territory);
						
						Territory attacking = Map.findTerritory(attackFrom);
						int armiesToMove = random.nextInt(attacking.numberOfArmies-1) + 1;
						System.out.println("Player randomly chose to move "+ armiesToMove + " armies to newly conquered territory");
						player.moveArmiesToNewTerritory(attackFrom, attackAt, armiesToMove);
					}
				}
			}

			if (Main.activeMap.allTerritoriesOwnBySinglePlayer(false)) {
				gameCompleted = true;
				attackDone = true;
			}

			if (!gameCompleted) {
				player.setCurrentGamePhase(GamePhase.FORTIFICATION);
			} else {
				System.out.println("\nGame Completed!\n" + player.getName() + "wins!!");
			}
		} else {
			System.out.println("This player chose to skip attack phase");
			player.setCurrentGamePhase(GamePhase.FORTIFICATION);
		}
	}

	/**
	 * This Method will return string value.
	 */
	 @Override
	 public String toString() {
		 return "random";
	 }
}
