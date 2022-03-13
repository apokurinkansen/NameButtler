import java.util.Random;
import java.util.Scanner;


public class GameManager {
    public static void main(String[] args) {
        Human player = new Human(10);
        Human enemy = new Human(10);

        System.out.println("player 1 の名前を入力してください :");
        player.SetName();
        System.out.println("player 2 の名前を入力してください :");
        enemy.SetName();

        BattleSystem battleSystem = new BattleSystem(player.hp, player.attack, player.name, enemy.hp, enemy.attack,enemy.name);
        
        battleSystem.Start();

        //Debug
        System.out.println(player.name);
        System.out.println(enemy.name);        
    }
}

class Human {
    
    String name;
    int hp;
    int attack;

    Scanner scanner = new Scanner(System.in);

    Human(int i) {

        Random random = new Random();
        this.attack = random.nextInt(3) + 1;
        this.hp = i;
    }

    int SetHp() {
        return hp;
    }

    int SetAttack() {
        return attack;
    }

    String SetName() {
        name = scanner.nextLine();
        while (name.equals("")) {
            System.out.println("もう一度入力してください");
            SetName();
        }
        return name;
    }
}

class BattleSystem {
    int playerHp;
    int enemyHp;
    int playerAt;
    int enemyAt;

    String playerName;
    String enemyName;
    
    public BattleSystem(int hp, int attack, String name, int hp2, int attack2, String name2) {
        this.playerHp = hp;
        this.enemyHp = hp2;
        this.playerAt = attack;
        this.enemyAt = attack2;
        this.playerName = name;
        this.enemyName = name2;
    }
    
    public void Start() {
        System.out.println("----開始-----");

        while (playerHp < 0 | enemyHp < 0) {
            
        }

        
    }
}