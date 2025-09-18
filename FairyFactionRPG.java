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
}

class Enemy {
    String name;
    int health;
    int attackPower;
}

public class FairyFactionRPG {

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
        System.out.println("\nWelcome " + player.name + "! Your adventure begins!");
        return player;

    }
    
    public static void gameLogic(Player player, Scanner scanner) {
        Random random = new Random();
        
        while (player.health > 0){
            System.out.println("\n---------------------------------------------");
            System.out.println("You awake in an abandoned Town square. What do you do?");
            System.out.println("1. Explore the mesmorizing Aspens");
            System.out.println("2. Check your status.");
            System.out.println("3. Use Health Potion.");
            System.out.println("4. Save Game.");
            System.out.println("5. Quit to Main Menue.");

            System.out.print("Enter your Choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")){
                exploreForest(player, scanner, random);
                

            } else if (choice.equals("2")) {
                System.out.println("\n---Your Status Is---");
                System.out.println("Name: " + player.name);
                System.out.println("Level: " + player.level);
                System.out.println("Experince Points: " + player.experiencePoints + "/" + player.xpNeedForNextLevel);
                System.out.println("Health: " + player.health + "/" + player.maxHealth);
                System.out.println("Attack power: " + player.attackPower);
                System.out.println("Health Potions: " + player.numHealthPotions);
                System.out.println("----------------------");
            }else if (choice.equals("3")){
                useHealthPotion(player);
            }else if (choice.equals("4")) {
                saveGame(player);
            }else if (choice.equals("5")){
                System.out.println("Returning to Main Menue...");
                break;
            }else {
                System.out.println("Invalid Choice. Please select 1, 2, 3, or 4.");
            }
            
        }

        System.out.println("\nThank you for Playing!");
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

