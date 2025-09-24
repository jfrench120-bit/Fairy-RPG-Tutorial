import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

// =================================================================================
// Data Classes
// =================================================================================

/**
 * Represents the player character, holding all their stats and inventory.
 */
class Player {
    // Core Stats
    String name;
    int level;
    int experiencePoints;
    int xpNeedForNextLevel;

    // Combat Stats
    int health;
    int maxHealth;
    int mana;
    int maxMana;
    int attackPower;

    // Inventory & Abilities
    int numHealthPotions;
    ArrayList<String> knownSpells;

    // Quest & Reputation
    int saltusReputation;
    int courtOfWhispersReputation;
    String goblinQuestState; // e.g., "NotStarted", "InProgress", "Completed"
    int goblinsDefeatedCount;
    boolean isFirstVisit;
}

/**
 * Represents an enemy entity for combat encounters.
 */
class Enemy {
    String name;
    int health;
    int attackPower;
}


// =================================================================================
// Main Game Class
// =================================================================================

public class FairyFactionRPG {

    // =================================================================================
    // Class-Level Variables & Constants
    // =================================================================================

    // Game-wide utilities
    public static final Scanner scanner = new Scanner(System.in);
    public static final Random random = new Random();

    // Location constants to avoid typos
    public static final String LOCATION_TOWN = "AbandonedTown";
    public static final String LOCATION_FOREST = "Forest";


    // =================================================================================
    // Main Game Execution
    // =================================================================================

    /**
     * The main entry point of the application. Displays the main menu.
     */
    public static void main(String[] args) {
        Player player; // Declare player object to be used for a new or loaded game.

        // Main Menu Loop
        while (true) {
            System.out.println("\n=============================");
            System.out.println("  WELCOME TO THE FAIRY REALM");
            System.out.println("===============================");
            System.out.println("1. New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Exit Game");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    player = createNewPlayer();
                    gameLogic(player); // Start the game with the new player
                    break;
                case "2":
                    player = loadGame();
                    if (player != null) {
                        System.out.println("Save file loaded. Welcome back, " + player.name + "!");
                        gameLogic(player); // Start the game with the loaded player
                    }
                    break;
                case "3":
                    System.out.println("Thank you for playing, goodbye!");
                    return; // Exit the application
                default:
                    System.out.println("Invalid choice, please choose options 1, 2, or 3.");
                    break;
            }
        }
    }

    /**
     * Contains the main game loop, handling player location and actions.
     * @param player The current player object.
     */
    public static void gameLogic(Player player) {
        String playerLocation = LOCATION_TOWN; // Start the player in town

        // The game continues as long as the player is alive.
        while (player.health > 0) {
            System.out.println("\n---------------------------------------");

            // Handle actions based on the player's current location.
            if (playerLocation.equals(LOCATION_TOWN)) {
                playerLocation = handleTownActions(player);
            } else if (playerLocation.equals(LOCATION_FOREST)) {
                playerLocation = handleForestActions(player);
            }

            // If playerLocation is set to null, it's a signal to exit to the main menu.
            if (playerLocation == null) {
                break;
            }
        }

        // This message is shown if the player's health drops to 0 or they quit to the menu.
        if (player.health <= 0) {
            System.out.println("Your adventure has come to an end... for now.");
        } else {
            System.out.println("Returning to the main menu...");
        }
    }

    // =================================================================================
    // Location-Specific Action Handlers
    // =================================================================================

    /**
     * Manages player choices and outcomes within the Abandoned Town.
     * @param player The current player object.
     * @return The next location for the player, or null to quit to the main menu.
     */
    public static String handleTownActions(Player player) {
        if (player.isFirstVisit) {
        System.out.println("You awake in the center of an eerie, abandoned town.");
        player.isFirstVisit = false; // Set the flag to false after the first visit
        } else {
        System.out.println("You are back in the abandoned town. The eerie silence remains.");
        }
        System.out.println("The buildings are crumbling, and nature reclaims the cobble streets.");
        System.out.println("What do you do?");
        System.out.println("1. Venture into the dark forest.");
        System.out.println("2. Find a safe corner to rest in.");
        System.out.println("3. Talk to the nervous Pixie.");
        System.out.println("4. Check your status.");
        System.out.println("5. Save Game.");
        System.out.println("6. Quit to Main Menu.");
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("You leave the relative safety of the town...");
                return LOCATION_FOREST;
            case "2":
                System.out.println("You find a dry, sheltered ruin to rest in.");
                player.health = player.maxHealth;
                player.mana = player.maxMana;
                System.out.println("You rest for a while and feel your strength return. You are fully healed.");
                return LOCATION_TOWN;
            case "3":
                talkToPixie(player);
                return LOCATION_TOWN;
            case "4":
                displayPlayerStatus(player);
                return LOCATION_TOWN;
            case "5":
                saveGame(player);
                return LOCATION_TOWN;
            case "6":
                return null; // Signal to exit to the main menu
            default:
                System.out.println("Invalid choice.");
                return LOCATION_TOWN;
        }
    }

    /**
     * Manages player choices and outcomes within the Forest.
     * @param player The current player object.
     * @return The next location for the player.
     */
    public static String handleForestActions(Player player) {
        System.out.println("You are in the shimmering, dark forest. Strange noises echo around you.");
        System.out.println("What do you do?");
        System.out.println("1. Explore deeper for monsters.");
        System.out.println("2. Use Health Potion.");
        System.out.println("3. Return to the Abandoned Town.");
        System.out.print("Enter your choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                exploreForest(player);
                return LOCATION_FOREST;
            case "2":
                useHealthPotion(player);
                return LOCATION_FOREST;
            case "3":
                System.out.println("\nYou escape from the dark forest and enter the old town.");
                return LOCATION_TOWN;
            default:
                System.out.println("Invalid Choice.");
                return LOCATION_FOREST;
        }
    }

    // =================================================================================
    // Player Setup & Save/Load Management
    // =================================================================================

    /**
     * Creates a new Player object and prompts the user for a name.
     * @return A newly initialized Player object.
     */
    public static Player createNewPlayer() {
        Player player = new Player();
        System.out.print("\nEnter your character's name: ");
        player.name = scanner.nextLine();

        // Initialize default stats
        player.level = 1;
        player.maxHealth = 100;
        player.health = player.maxHealth;
        player.maxMana = 50;
        player.mana = player.maxMana;
        player.attackPower = 15;
        player.experiencePoints = 0;
        player.xpNeedForNextLevel = 100;
        player.numHealthPotions = 1;
        player.saltusReputation = 0;
        player.courtOfWhispersReputation = 0;
        player.goblinQuestState = "NotStarted";
        player.goblinsDefeatedCount = 0;
        player.knownSpells = new ArrayList<>();
        player.isFirstVisit = true;

        System.out.println("\nWelcome, " + player.name + "! Your adventure begins!");
        return player;
    }

    /**
     * Saves the current player's state to a text file.
     * @param player The player object to save.
     */
    public static void saveGame(Player player) {
        try (PrintWriter writer = new PrintWriter("save.txt")) {
            // Write all player stats line by line
            writer.println(player.name);
            writer.println(player.level);
            writer.println(player.maxHealth);
            writer.println(player.health);
            writer.println(player.attackPower);
            writer.println(player.experiencePoints);
            writer.println(player.xpNeedForNextLevel);
            writer.println(player.numHealthPotions);
            writer.println(player.saltusReputation);
            writer.println(player.courtOfWhispersReputation);
            writer.println(player.goblinQuestState);
            writer.println(player.goblinsDefeatedCount);
            writer.println(player.mana);
            writer.println(player.maxMana);
            writer.println(player.isFirstVisit);

            // Join the list of spells into a single comma-separated string
            if (player.knownSpells.isEmpty()) {
                writer.println(""); // Write an empty line if no spells
            } else {
                writer.println(String.join(",", player.knownSpells));
            }

            System.out.println("Game saved successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not save the game. " + e.getMessage());
        }
    }

    /**
     * Loads a player's state from a text file.
     * @return The loaded Player object, or null if no save file is found.
     */
    public static Player loadGame() {
        try {
            File saveFile = new File("save.txt");
            Scanner fileScanner = new Scanner(saveFile);
            Player loadedPlayer = new Player();

            // Read all stats in the same order they were saved
            loadedPlayer.name = fileScanner.nextLine();
            loadedPlayer.level = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.maxHealth = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.health = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.attackPower = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.experiencePoints = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.xpNeedForNextLevel = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.numHealthPotions = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.saltusReputation = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.courtOfWhispersReputation = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.goblinQuestState = fileScanner.nextLine();
            loadedPlayer.goblinsDefeatedCount = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.mana = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.maxMana = Integer.parseInt(fileScanner.nextLine());
            loadedPlayer.isFirstVisit = Boolean.parseBoolean(fileScanner.nextLine());

            // Read the spells line and split it back into an ArrayList
            String spellsLine = fileScanner.nextLine();
            loadedPlayer.knownSpells = new ArrayList<>();
            if (!spellsLine.isEmpty()) {
                loadedPlayer.knownSpells.addAll(Arrays.asList(spellsLine.split(",")));
            }

            fileScanner.close();
            return loadedPlayer;

        } catch (FileNotFoundException e) {
            System.out.println("No save file found. Please start a New Game.");
            return null;
        }
    }


    // =================================================================================
    // Player Actions & Status
    // =================================================================================

    /**
     * Displays all relevant stats for the player.
     * @param player The current player object.
     */
    public static void displayPlayerStatus(Player player) {
        System.out.println("\n--- YOUR STATUS ---");
        System.out.println("Level: " + player.level);
        System.out.println("Experience: " + player.experiencePoints + "/" + player.xpNeedForNextLevel);
        System.out.println("Health: " + player.health + "/" + player.maxHealth);
        System.out.println("Mana: " + player.mana + "/" + player.maxMana);
        System.out.println("Attack Power: " + player.attackPower);
        System.out.println("Health Potions: " + player.numHealthPotions);
        System.out.println("--- FACTIONS ---");
        System.out.println("The Saltus: " + player.saltusReputation);
        System.out.println("The Court of Whispers: " + player.courtOfWhispersReputation);
        System.out.println("-------------------");
    }

    /**
     * Allows the player to use a health potion if they have one.
     * @param player The current player object.
     */
    public static void useHealthPotion(Player player) {
        if (player.numHealthPotions > 0) {
            if (player.health >= player.maxHealth) {
                System.out.println("\n> You are already at full health.");
            } else {
                int healthRestored = 30;
                player.health += healthRestored;
                player.numHealthPotions--;

                if (player.health > player.maxHealth) {
                    player.health = player.maxHealth;
                }

                System.out.println("\n> You drink a health potion, restoring " + healthRestored + " health!");
                System.out.println("> Your health is now " + player.health + "/" + player.maxHealth);
                System.out.println("> You have " + player.numHealthPotions + " health potions left.");
            }
        } else {
            System.out.println("> You don't have any health potions left!");
        }
    }

    /**
     * Checks if the player has enough experience to level up and applies the changes.
     * @param player The current player object.
     */
    public static void checkForLevelUp(Player player) {
        // Use a while loop in case the player gains enough XP for multiple levels at once.
        while (player.experiencePoints >= player.xpNeedForNextLevel) {
            player.experiencePoints -= player.xpNeedForNextLevel;
            player.level++;
            player.xpNeedForNextLevel += 50; // Increase XP requirement for the next level

            // Apply stat boosts
            player.attackPower += 5;
            player.maxHealth += 20;
            player.maxMana += 10;
            player.health = player.maxHealth; // Fully heal on level up
            player.mana = player.maxMana;

            System.out.println("*****************************************************");
            System.out.println("  LEVEL UP! You are now level " + player.level + "!  ");
            System.out.println("*****************************************************");
            System.out.println("> Your max health, mana, and attack power have increased!");
            System.out.println("> You have been fully healed and restored.");

            pressEnterToContinue();
        }
    }


    // =================================================================================
    // Combat System
    // =================================================================================

    /**
     * Initiates a random encounter in the forest.
     * @param player The current player object.
     */
    public static void exploreForest(Player player) {
        int encounterRoll = random.nextInt(100); // Roll a number between 0 and 99

        if (encounterRoll < 60) { // 60% chance for a Goblin
            startCombat(player, "Grumpy Goblin", 30, 7, 25);
        } else if (encounterRoll < 85) { // 25% chance for a Spider
            startCombat(player, "Giant Forest Spider", 50, 12, 50);
        } else { // 15% chance for no encounter
            System.out.println("You walk through a stunning glade...");
            System.out.println("You find nothing of interest except the rustle of leaves and warmth of the sun.");
        }
    }

    /**
     * Manages a turn-based combat encounter between the player and an enemy.
     * @param player      The player object.
     * @param enemyName   The name of the enemy.
     * @param enemyHealth The enemy's starting health.
     * @param enemyAttack The enemy's attack power.
     * @param xpValue     The experience points awarded for defeating the enemy.
     */
    public static void startCombat(Player player, String enemyName, int enemyHealth, int enemyAttack, int xpValue) {
        System.out.println("\n> You stumble upon a " + enemyName + "!");

        // Create an enemy for this encounter
        Enemy enemy = new Enemy();
        enemy.name = enemyName;
        enemy.health = enemyHealth;
        enemy.attackPower = enemyAttack;

        boolean playerRanAway = false;

        // Combat loop continues until someone's health is 0 or the player runs away.
        while (player.health > 0 && enemy.health > 0) {
            // --- Player's Turn ---
            System.out.println("\n---- YOUR TURN ----");
            System.out.println("Your Health: " + player.health + "/" + player.maxHealth);
            System.out.println("Enemy Health: " + enemy.health);
            System.out.println("What will you do?");
            System.out.println("1. Attack");
            System.out.println("2. Use Health Potion");
            System.out.println("3. Spells");
            System.out.println("4. Run Away");
            System.out.print("Enter your combat choice: ");

            String combatChoice = scanner.nextLine();

            switch (combatChoice) {
                case "1": // Attack
                    int damageDealt = player.attackPower;
                    System.out.println("You attack the " + enemy.name + " for " + damageDealt + " damage!");
                    enemy.health -= damageDealt;
                    break;
                case "2": // Use Potion
                    useHealthPotion(player);
                    break;
                case "3": // Open Spellbook
                    spellBook(player);
                    break;
                case "4": // Run Away
                    if (random.nextInt(100) < 50) { // 50% chance to escape
                        System.out.println("\n> You successfully escaped from the " + enemy.name + "!");
                        playerRanAway = true;
                    } else {
                        System.out.println("> You failed to escape!");
                    }
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }

            if (playerRanAway) {
                break; // Exit the combat loop if the player escaped
            }

            // --- Enemy's Turn ---
            if (enemy.health > 0) {
                int damageTaken = enemy.attackPower;
                System.out.println("\nThe " + enemy.name + " attacks you for " + damageTaken + " damage!");
                player.health -= damageTaken;
            }
        }

        // --- Post-Combat Resolution ---
        System.out.println("\n---- COMBAT ENDS ----");

        if (playerRanAway) {
            // No rewards if the player ran away.
        } else if (enemy.health <= 0) {
            System.out.println("You defeated the " + enemy.name + "!");

            // Grant experience and check for level up
            player.experiencePoints += xpValue;
            System.out.println("You gained " + xpValue + " XP!");

            // Handle quest progression
            if (player.goblinQuestState.equals("InProgress") && enemy.name.contains("Goblin")) {
                player.goblinsDefeatedCount++;
                System.out.println("You have defeated " + player.goblinsDefeatedCount + "/3 Goblins for the Pixie.");

                if (player.goblinsDefeatedCount >= 3) {
                    player.goblinQuestState = "Completed";
                    System.out.println("You have defeated enough Goblins! You should speak with the Pixie.");
                }
            }

            // Chance for the enemy to drop a health potion
            if (random.nextInt(100) < 30) { // 30% drop chance
                player.numHealthPotions++;
                System.out.println("The " + enemyName + " dropped a health potion!");
                System.out.println("You now have " + player.numHealthPotions + " health potions.");
            }

            checkForLevelUp(player);

        } else if (player.health <= 0) {
            System.out.println("You have been defeated by the " + enemy.name + "!");
        }
    }


    // =================================================================================
    // Spell System
    // =================================================================================

    /**
     * Displays the player's known spells and allows them to cast one.
     * @param player The current player object.
     */
    public static void spellBook(Player player) {
        System.out.println("\n--- SPELL BOOK ---");
        if (player.knownSpells.isEmpty()) {
            System.out.println("You have not learned any spells yet.");
            return;
        }

        System.out.println("You recall the spells you have learned so far...");
        for (int i = 0; i < player.knownSpells.size(); i++) {
            System.out.println((i + 1) + ". " + player.knownSpells.get(i));
        }
        System.out.println((player.knownSpells.size() + 1) + ". Go Back");
        System.out.print("\nEnter your choice: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= player.knownSpells.size()) {
                String chosenSpell = player.knownSpells.get(choice - 1);
                // Execute spell based on its name
                if ("Minor Heal".equals(chosenSpell)) {
                    castMinorHeal(player);
                }
                // Add else-if for other spells here
            } else if (choice == player.knownSpells.size() + 1) {
                System.out.println("\nYou close your spellbook for now.");
            } else {
                System.out.println("\nInvalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a number.");
        }
    }

    /**
     * Logic for casting the "Minor Heal" spell.
     * @param player The current player object.
     */
    public static void castMinorHeal(Player player) {
        int manaCost = 15;
        int healthRestored = 25;

        if (player.mana >= manaCost) {
            player.mana -= manaCost;
            player.health += healthRestored;

            if (player.health > player.maxHealth) {
                player.health = player.maxHealth;
            }

            System.out.println("\n> You gather woodland energy and cast Minor Heal!");
            System.out.println("> You restore " + healthRestored + " health.");
            System.out.println("> Your health is now " + player.health + "/" + player.maxHealth);
            System.out.println("> Your mana is now " + player.mana + "/" + player.maxMana);
        } else {
            System.out.println("\n> You don't have enough mana to cast this spell!");
        }
    }


    // =================================================================================
    // Quest & NPC Interaction
    // =================================================================================

    /**
     * Handles all dialogue and quest logic for the Pixie NPC.
     * @param player The current player object.
     */
    public static void talkToPixie(Player player) {
        System.out.println("\n--- You approach the nervous Pixie ---");

        switch (player.goblinQuestState) {
            case "NotStarted":
                System.out.println("Pixie: 'Oh! H-hello there! You're... not a goblin, are you?'");
                System.out.println("Pixie: 'I'm sorry, I'm just so scared. I need to get back home to the Saltus, but the forest is full of goblins!'");
                pressEnterToContinue();
                System.out.println("Pixie: 'They never come this far south. It's like something scared them down from the mountains...'");
                System.out.println("Pixie: 'Could... could you help me? If you could just defeat 3 of them, the path might be clear enough for me to slip by.'");
                System.out.println("\nAccept the quest?");
                System.out.println("1. 'Of course. I'll handle the goblins.'");
                System.out.println("2. 'Sorry, I have other things to do.'");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();
                if (choice.equals("1")) {
                    player.goblinQuestState = "InProgress";
                    System.out.println("\nPixie: 'Oh, thank you! Thank you so much! Be careful!'");
                } else {
                    System.out.println("\nPixie: 'Oh... I understand. Please be safe.'");
                }
                break;
            case "InProgress":
                System.out.println("Pixie: 'Please be careful out there! Have you dealt with the goblins yet?'");
                System.out.println("Pixie: 'You need to defeat " + (3 - player.goblinsDefeatedCount) + " more.'");
                break;
            case "Completed":
                System.out.println("Pixie: 'You did it! You really did it! The path is clear!'");
                System.out.println("Pixie: 'I can finally get home. Thank you, thank you!'");
                pressEnterToContinue();

                // Grant rewards
                int xpReward = 75;
                int saltusRepReward = 10;
                player.experiencePoints += xpReward;
                player.saltusReputation += saltusRepReward;

                System.out.println("************************************");
                System.out.println("          QUEST COMPLETED!          ");
                System.out.println("************************************");
                System.out.println("You gained " + xpReward + " XP!");
                System.out.println("Your reputation with The Saltus has increased by " + saltusRepReward + ".");

                // Special reward for first quest completion
                if (!player.knownSpells.contains("Minor Heal")) {
                    player.knownSpells.add("Minor Heal");
                    System.out.println("\nThe Pixie tosses faint dust over you...");
                    System.out.println("At first nothing happens, then you feel a strange connection to the Pixie and the trees around you...");
                    pressEnterToContinue();
                    System.out.println("Pixie: 'There. Now all Saltus will know you as a friend, however some may take more convincing.'");
                    System.out.println("Pixie: 'It appears you also have an affinity for magic. The more you help the forest, the more the forest helps you.'");
                    System.out.println("A faint green light envelops you...");
                    System.out.println("\n>>> You have learned the spell: Minor Heal! <<<");
                }

                checkForLevelUp(player);
                player.goblinQuestState = "Finished"; // Ensure this quest isn't repeatable
                break;
            case "Finished":
                System.out.println("Pixie: 'Thank you again for your help! The Saltus is in your debt.'");
                System.out.println("Pixie: 'I hope you'll investigate the disturbance in the mountains someday.'");
                break;
        }
        System.out.println("--------------------------------------");
    }

    // =================================================================================
    // Utility Methods
    // =================================================================================

    /**
     * Pauses the game and waits for the user to press the Enter key.
     */
    public static void pressEnterToContinue() {
        System.out.println("\n[Press Enter To Continue...]");
        scanner.nextLine();
    }
}

