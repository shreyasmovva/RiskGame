package Player;

import java.util.Scanner;
import java.util.Vector;

import Card.Card;
import Driver.Main;
import Map.Map;
import Map.Map.Territory;
import Player.Player.GamePhase;
import Views.CardExchangeView;

/**
 * 
 * The Human class is implementing functions in Strategy interface
 * 
 * @author mandeepahlawat
 * @version 1.0
 * @since 24-11-2018
 *
 */

public class Human implements Strategy {
	/**
	 * The player to which this strategy belongs to
	 */
	public Player player;

	/**
	 * constructor for this class and sets the player data member
	 * 
	 * @param player assign the player value.
	 * 
	 */
	public Human(Player player) {
		this.player = player;
	}

	/**
	 *
	 * This method Calculate's the number of reinforcements that the player gets in
	 * each turn of the game.
	 *
	 * @return int the number of reinforcement armies.
	 *
	 */
	@Override
	public int calculateReinforcementArmies() {
		int totalReinforcements = 0;
		totalReinforcements = (int) (player.assignedTerritories.size() / 3);
		if (totalReinforcements < 3) {
			totalReinforcements = 3;
		}

		for (Map continent : Main.activeMap.continents) {
			int i = 0;
			for (Territory territory : continent.territories) {
				if (territory.owner != player) {
					break;
				}
				i++;
			}
			if (i == continent.territories.size()) {
				totalReinforcements = totalReinforcements + continent.score;
			}
		}

		if (player.canExchangeCards()) {
			player.cardExchangeViewOpen = true;
			CardExchangeView cardExchangeView = new CardExchangeView();
			player.addObserver(cardExchangeView);

			if (player.cards.size() == 5) {
				totalReinforcements += Card.cardExchangeValue;
				player.setChangeAndNotifyObservers();
			} else {
				Scanner keyboard = new Scanner(System.in);
				System.out.println("Do you want to exchange cards? Enter yes or no?");
				if (keyboard.nextLine().equalsIgnoreCase("yes")) {
					totalReinforcements += Card.cardExchangeValue;
					player.setChangeAndNotifyObservers();
				}
			}

			player.deleteObserver(cardExchangeView);
			player.cardExchangeViewOpen = false;
		}

		System.out.println("Player " + player.getName() + " gets " + totalReinforcements + " reinforcement armies.");
		player.totalArmiesCount += totalReinforcements;
		return totalReinforcements;
	}

	/**
	 * 
	 * This method defines Place Reinforcements in territories which also print's
	 * out player name,armies left in it.
	 * 
	 * @param reinforcements number of reinforcement armies the player in the second
	 *                       parameter gets.
	 * 
	 */
	@Override
	public void placeReinforcements(int reinforcements) {
		Scanner keyboard = new Scanner(System.in);
		String userInput = null;

		while (reinforcements != 0) {
			System.out.println("\nPlayer " + player.getName() + " has " + reinforcements + " armies left."
					+ "\nEnter name of the Country to place a reinforcement army:");

			userInput = keyboard.nextLine();

			for (Territory territory : Main.activeMap.territories) {
				if (territory.name.equalsIgnoreCase(userInput)) {
					if (territory.owner != player) {
						System.out.println("Wrong country! Try again!");
						break;
					}
					territory.numberOfArmies++;
					reinforcements--;
					break;
				}
			}
		}
	}

	/**
	 * 
	 * fortification method to allow a player to move one of his armies from one
	 * country he owns to another that is adjacent to it.
	 * 
	 */
	@Override
	public void fortification() {
		System.out.println("Player " + player.getName());
		String fromCountry = null;
		String toCountry = null;
		boolean doneFlag1 = false;
		boolean doneFlag2 = false;
		Scanner keyboard = new Scanner(System.in);

		System.out.println("Do you want to skip fortification? Enter yes or no.");
		String userInput = keyboard.nextLine();
		if (!userInput.equalsIgnoreCase("yes")) {
			do {
				System.out.println(
						"Enter the country you would like to move an army from: (enter 'exit' to skip fortification)");
				fromCountry = keyboard.nextLine();
				if (!fromCountry.equalsIgnoreCase("exit")) {
					if (player.validAssignedCountry(fromCountry)) {
						do {
							System.out.println(
									"Enter the neighbouring country you would like to move the army to: (enter 'exit' to skip fortification)");
							toCountry = keyboard.nextLine();
							if (!toCountry.equalsIgnoreCase("exit")) {
								int armiesInFromCountry = 0;

								for (Territory territory : player.assignedTerritories) {
									if (territory.name.equals(fromCountry)) {
										armiesInFromCountry = territory.numberOfArmies;
									}
								}

								if (armiesInFromCountry == 0) {
									System.out.println("=================== bug ===============");
								}

								if (player.validNeighborCountry(fromCountry, toCountry)) {
									int armiesToMove = 0;
									System.out.println("Enter the number of armies to move."
											+ "\nNote the number of armies to move must be less than"
											+ " number of armies in the from country as every country must have"
											+ " at least 1 army");
									armiesToMove = Integer.parseInt(keyboard.nextLine());
									while (armiesToMove <= 0 || armiesToMove >= armiesInFromCountry) {
										System.out.println("Wrong number of Armies!"
												+ "\nNumber of armies should be less than: " + armiesInFromCountry);
										armiesToMove = Integer.parseInt(keyboard.nextLine());
									}
									for (Territory territory : player.assignedTerritories) {
										if (territory.name.equals(fromCountry)) {
											territory.numberOfArmies -= armiesToMove;
											for (Territory neighbour : territory.neighbours) {
												if (neighbour.name.equalsIgnoreCase(toCountry)) {
													neighbour.numberOfArmies += armiesToMove;
													break;
												}
											}
											break;
										}
									}
									doneFlag2 = true;
								} else {
									System.out.println("Enter a valid Neighbour that you own.");
								}
							} else {
								doneFlag1 = true;
								doneFlag2 = true;
							}
						} while (!doneFlag2);
						doneFlag1 = true;
					} else {
						System.out.println("Wrong Country!!"
								+ "\nEnter a country you own that is having at least one neighbour that you own too!!");
					}
				} else {
					doneFlag1 = true;
					doneFlag2 = true;
				}
			} while (!doneFlag1);
		}
	}

	/**
	 * 
	 * The method will implement the attack phase. attackDone if all territories are
	 * conquered or attack lost. gameCompleted if all territories are conquered.
	 * 
	 */
	@Override
	public void attack() {

		boolean attackDone = false;
		boolean gameCompleted = false;
		boolean defenderLost = false;
		String attackFrom = "", attackAt = "", opponent = "", attacker = "";

		Scanner keyboard = new Scanner(System.in);
		System.out.println("Do you want to go ahead with the attack? Enter yes or no:");
		String answer = keyboard.nextLine();

		if (answer.equalsIgnoreCase("yes")) {
			while (!attackDone) {
				System.out.println("Enter the country you want to attack from (Enter 'exit' to skip attack phase)");
				attackFrom = keyboard.nextLine();
				if (attackFrom.equalsIgnoreCase("exit")) {
					attackDone = true;
					break;
				}
				if (player.canAttackFromThisCountry(attackFrom)) {
					System.out.println("Enter the country you want to attack (Enter 'exit' to skip attack phase)");
					attackAt = keyboard.nextLine();
					if (attackAt.equalsIgnoreCase("exit")) {
						attackDone = true;
						break;
					}
					if (player.validOpponentCountry(attackFrom, attackAt)) {
						boolean finishedAttackingThatTerritory = false; // finished attacking the present territory
						boolean allOutMode = false;

						System.out.println("Do you want to attack in ALL-OUT mode? yes or no");
						String input = keyboard.nextLine();
						if (input.equalsIgnoreCase("yes")) {
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
								System.out.println("Do you want continue the attack? yes or no");
								continueAttack = keyboard.nextLine();
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
										System.out.println("Choose Attacker's number of dice");
										attackerDiceCount = player.calculateNumberOfDiceAllowed("attacker", attackFrom,
												attackAt, allOutMode, false);
										attackerDice = player.rollDice(attackerDiceCount);

										System.out.println("Choose Defender's number of dice");
										defenderDiceCount = player.calculateNumberOfDiceAllowed("defender", attackFrom,
												attackAt, allOutMode, false);
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
									System.out.println("Leaving attack in the middle.");
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
						System.out.println("Enter the number of armies you would like to place in your new territory:");
						player.moveArmiesToNewTerritory(attackFrom, attackAt, keyboard.nextInt());
					}
				}
			}

			if (Main.activeMap.allTerritoriesOwnBySinglePlayer(false)) {
				gameCompleted = true;
				attackDone = true;
			}

			if (!gameCompleted) {
				attack();
			} else {
				System.out.println("\nGame Completed!\n" + player.getName() + "wins!!");
			}
		} else {
			player.setCurrentGamePhase(GamePhase.FORTIFICATION);
		}
	}
	
	/**
	 * This Method will return string value.
	 * 
	 */
	@Override
	 public String toString() {
		 return "human";
	 }
}
