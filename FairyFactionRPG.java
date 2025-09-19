import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

class Player{
    String name;
    int health;
    int maxHealth;
    int attackPower;
    int numHealthPotions;
    int level;
    int experiencePoints;
    int xpNeedForNextLevel;
    int saltusReputation;
    int courtOfWhispersReputation;
    String goblinQuestState;
    int goblinsDefeatedCount;
}

class Enemy {
    String name;
    int health;
    int attackPower;
}

public class FairyFactionRPG {

    public static final String LOCATION_TOWN = "AbandonedTown";
    public static final String LOCATION_FOREST = "Forest";

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Player player = null;

        while (true){
            System.out.println("\n=============================");
            System.out.println("  WELCOME TO THE FAIRY REALM");
            System.out.println("===============================");

            System.out.println("1. New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Exit Game");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")){
                player = createNewPlayer(scanner);
                gameLogic(player, scanner);
            }else if (choice.equals("2")){
                player = loadGame(scanner);
                if (player != null){
                    System.out.println("Save file loaded. Welcome back " + player.name + "!");
                    gameLogic(player, scanner);
                }
            }else if (choice.equals("3")){
                System.out.println("Thank you for playing, goodbye!");
                break;
            }else{
                System.out.println("Invalid choice, please choose options 1, 2, or 3.");
            }
        }
    }

    public static Player createNewPlayer(Scanner scanner){
        Player player = new Player();
        System.out.println("\nEnter your charecter's name: ");
        player.name = scanner.nextLine();
        player.maxHealth = 100;
        player.health = player.maxHealth;
        player.attackPower = 15;
        player.numHealthPotions = 3;
        player.level = 1;
        player.experiencePoints = 0;
        player.xpNeedForNextLevel = 100;
        player.saltusReputation = 0;
        player.courtOfWhispersReputation = 0;
        player.goblinQuestState = "NotStarted";
        player.goblinsDefeatedCount = 0;
        System.out.println("\nWelcome " + player.name + "! Your adventure begins!");
        return player;

    }
    
    public static void gameLogic(Player player, Scanner scanner) {
        Random random = new Random();

        String playerLocation = LOCATION_TOWN;

        while (player.health > 0){
            System.out.println("\n---------------------------------------");

            if (playerLocation.equals(LOCATION_TOWN)){
                System.out.println("You awake in the center of an eerie, abandoned town.");
                System.out.println("The buildings are crumbling, and nature reclaims the cobble streets.");
                System.out.println("What do you do?");

                System.out.println("1. Venture into the dark forest.");
                System.out.println("2. Find a safe corner to rest in.");
                System.out.println("3. Talk to the nervous Pixie.");
                System.out.println("4. Check your status.");
                System.out.println("5. Save Game.");
                System.out.println("6. Quit to Main Menue.");
                System.out.print("Enter your choice: ");

                String choice = scanner.nextLine();

                if (choice.equals("1")){
                    System.out.println("You leave the safety of the town...");
                    playerLocation = LOCATION_FOREST;
                }else if(choice.equals("2")){
                    System.out.println("You find a dry, shelterd ruin to rest in.");
                    player.health = player.maxHealth;
                    System.out.println("You rest for awile and feel your strength return. You are now fully healed.");
                }else if(choice.equals("3")){
                    talkToPixie(player, scanner);
                }else if(choice.equals("4")){
                    displayPlayerStatus(player);
                }else if(choice.equals("5")){
                    saveGame(player);
                }else if(choice.equals("6")){
                    System.out.println("Returning to Main Menue...");
                    break;
                }else{
                    System.out.println("Invalid choice");
                }
            }else if(playerLocation.equals(LOCATION_FOREST)){
                System.out.println("You are in the shimmering, dark forest. Strange noises echo around you.");
                System.out.println("What do you do?");
                System.out.println("1. Explore deeper for monsters.");
                System.out.println("2. Use Health Potion.");
                System.out.println("3. Return to the Abandoned Town.");
                System.out.print("Enter your choice: ");

                String choice = scanner.nextLine();

                if (choice.equals("1")){
                    exploreForest(player, scanner, random);
                }else if (choice.equals("2")){
                    useHealthPotion(player);
                }else if(choice.equals("3")){
                    System.out.println("\nYou escape from the dark forest and enter the old town.");
                    playerLocation = LOCATION_TOWN;
                }else{
                    System.out.println("Invalid Choice");
                }
            }
        }

        System.out.println("Another adventure bekons you...");
    }

    public static void talkToPixie(Player player, Scanner scanner){
        System.out.println("---You approach the nervous Pixie---");

        if (player.goblinQuestState.equals("NotStarted")){
            System.out.println("Pixie: 'Oh! H-hello there! You're... not a goblin, are you?'");
            System.out.println("Pixie: 'I'm sorry, I'm just so scared. I need to get back home to the Saltus, but the forest is full of goblins!'");
            System.out.println("Pixie: 'They never come this far south. It's like something scared them down from the mountains...'");
            System.out.println("Pixie: 'Could... could you help me? If you could just defeat 3 of them, the path might be clear enough for me to slip by.'");
            System.out.println("\nAccept the quest?");
            System.out.println("1. 'Of course. I'll handle the goblins.'");
            System.out.println("2. 'Sorry, I have other things to do.'");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")){
                player.goblinQuestState = "InProgress";
                System.out.println("\nPixie: 'Oh, thank you! Thank you so much! Be careful!'");
            }else{
                System.out.println("\nPixie: 'Oh... I understand. Please be safe.'");
            }
        }else if (player.goblinQuestState.equals("InProgress")){
            System.out.println("Pixie: 'Please be careful out there! Have you dealt with the goblins yet?'");
            System.out.println("Pixie: 'You need to defeat " + (3 - player.goblinsDefeatedCount) + " more.'");
        }else if (player.goblinQuestState.equals("Completed")){
            System.out.println("Pixie: 'You did it! You really did it! The path is clear!'");
            System.out.println("Pixie: 'I can finally get home. Thank you, thank you!'");

            int xpReward = 75;
            int saltusRepReward = 10;

            player.experiencePoints += xpReward;
            player.saltusReputation += saltusRepReward;

            System.out.println("\n************************************");
            System.out.println("          QUEST COMPLETED             ");
            System.out.println("You gained " + xpReward + "XP!");
            System.out.println("Your reputation with The Saltus has increased by " + saltusRepReward);

            player.goblinQuestState = "Finished";
        }else if(player.goblinQuestState.equals("Finished")){
            System.out.println("Pixie: 'Thank you again for your help! The Saltus is in your debt.'");
            System.out.println("Pixie: 'I hope you'll investigate the disturbance in the mountains someday.'");
        }

        System.out.println("---------------------------------");
    }

    public static void displayPlayerStatus(Player player){
        System.out.println("\n--- YOUR STATUS ---");
        System.out.println("Level: " + player.level);
        System.out.println("Experience: " + player.experiencePoints + "/" + player.xpNeedForNextLevel);
        System.out.println("Health: " + player.health + "/" + player.maxHealth);
        System.out.println("Attack Power: " + player.attackPower);
        System.out.println("Health Potions: " + player.numHealthPotions);
        System.out.println("---FACTIONS---");
        System.out.println("The Saltus: " + player.saltusReputation);
        System.out.println("The Cout Of Whispers: " + player.courtOfWhispersReputation);
        System.out.println("-------------------");
    }

    public static void saveGame(Player player){
        try {
            PrintWriter writer = new PrintWriter("save.txt");

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

            writer.close();
            System.out.println("Saved game successfully!");

        } catch (FileNotFoundException e){
            System.out.println("Error: Could not save the game. " + e.getMessage());

        }
    }

    public static Player loadGame(Scanner scanner){
        try{
            File saveFile = new File("save.txt");

            Scanner fileScanner = new Scanner(saveFile);

            Player loadedPlayer = new Player();

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

            fileScanner.close();

            return loadedPlayer;

        }catch (FileNotFoundException e){
            System.out.println("No save file found. Please start a New Game.");
            return null;
        }
    }

    public static void exploreForest(Player player, Scanner scanner, Random random) {

        int encounterRoll = random.nextInt(100);
        
        if (encounterRoll < 50){
            startCombat(player, scanner, random, "Grumpy Goblin", 30, 7, 25);
        }else if (encounterRoll < 85){
            startCombat(player, scanner, random, "Giant Forest Spider", 50, 12, 50);
        }else{
            System.out.println("You walk through a stunning glade...");
            System.out.println("You discover someone has left a gift for you...");
            System.out.println("You find a health potion.");
            player.numHealthPotions ++;
        }

        
    }

    public static void useHealthPotion(Player player){
        if (player.numHealthPotions > 0){
            if (player.health >= player.maxHealth){
                System.out.println("\n>You are already at full health...you dont need to heal.");
            }else{
                int healthRestored = 30;
                player.health += healthRestored;
                player.numHealthPotions --;

                if (player.health > player.maxHealth){
                    player.health = player.maxHealth;
                }

                System.out.println("\n>You drink a health potion, restoring " + healthRestored + " health!");
                System.out.println(">Your health is now " + player.health + "/" + player.maxHealth);
                System.out.println(">You now have " + player.numHealthPotions + " health potions left.");
            }
        }else{
            System.out.println(">You dont have any health potions left!");
        }
    }

    public static void checkForLevelUp(Player player){
        while ( player.experiencePoints >= player.xpNeedForNextLevel){
            player.experiencePoints -= player.xpNeedForNextLevel;
            player.level ++;
            player.xpNeedForNextLevel += 50;

            player.attackPower += 5;
            player.maxHealth += 20;
            player.health = player.maxHealth;

            System.out.println("*****************************************************");
            System.out.println("  LEVEL UP! You are now level " + player.level + "!  ");
            System.out.println("*****************************************************");

            System.out.println(">Your max health and attack power have increased!");
            System.out.println("You have been fully healed");
        }
    }

    public static void startCombat(Player player, Scanner scanner, Random random, String enemyName, int enemyHealth, int enemyAttack, int xpValue){
        System.out.println("\n>You stumble upon a " + enemyName + "!");

        Enemy enemy = new Enemy();
        enemy.name = enemyName;
        enemy.health = enemyHealth;
        enemy.attackPower = enemyAttack;

        boolean playerRanAway = false;

        while (player.health > 0 && enemy.health > 0){
            System.out.println("\n----Your Turn----");
            System.out.println("Your Health: " + player.health);
            System.out.println("What will you do?");
            System.out.println("1. Attack");
            System.out.println("2. Use Health Potion");
            System.out.println("3. Run Away");
            System.out.print("Enter your combat choice: ");

            String combatChoice = scanner.nextLine();

            if (combatChoice.equals("1")) {
                System.out.println("You attack the " + enemy.name + "!");
                enemy.health -= player.attackPower;
                System.out.println("The " + enemy.name + "'s health is now: " + enemy.health);
            }else if (combatChoice.equals("2")){
                useHealthPotion(player);
            }else if (combatChoice.equals("3")){
                if (random.nextInt(100) < 50) {
                    System.out.println("\n> You successfuly escaped from the " + enemy.name + "!");
                    playerRanAway = true;
                    break;
                } else {
                    System.out.println("You failed to escape!");
                }
            }else{
                System.out.println("Invalid input!");
            }

            if (enemy.health > 0) {
                System.out.println("The " + enemy.name + " attacks you!");
                player.health -= enemy.attackPower;
                System.out.println("Your health is now: " + player.health);

            }
        }

        System.out.println("\n----Combat Ends----");

        if (playerRanAway) {
                    
        } else if (enemy.health <= 0) {
            System.out.println("You defeated the " + enemy.name + "!");

            if (player.goblinQuestState.equals("InProgress")){
                if (enemy.name.contains("Goblin")){
                    player.goblinsDefeatedCount ++;
                    System.out.println("You have defeated " + player.goblinsDefeatedCount + "/3 Goblins for the Pixie.");

                    if (player.goblinsDefeatedCount >= 3){
                        player.goblinQuestState = "Completed";
                        System.out.println("You have defeated enoguh Goblins, you should speak with the Pixie.");
                    }
                }
            }
            player.experiencePoints += xpValue;
            System.out.println("You gained " + xpValue + "XP!");
            checkForLevelUp(player);
            player.numHealthPotions ++;
            System.out.println("You found 1 health potion!");
        } else if (player.health <= 0) {
             System.out.println("You have been defeated by the " + enemy.name + "!");
        }
    }
}

