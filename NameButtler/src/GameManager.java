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

class ButtleSystem {
    
    
}
