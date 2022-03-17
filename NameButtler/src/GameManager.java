import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;
import java.util.Scanner;

//TODO 防御力追加(名前から取得)
//TODO 攻撃時に相手に与えるダメージの計算式を以下の方法に変更する 変更前：１～攻撃力(STR)の値の乱数 変更後：攻撃側の攻撃力(STR) ー 防御側の防御力(DEF)
//TODO ダメージが０以下の場合は、ダメージを０として扱う
//TODO ダメージが０の場合は「攻撃がミス」と表示する

public class GameManager {
    public static void main(String[] args) {
        Human player = new Human();
        Human enemy = new Human();
        Random random = new Random();
        System.out.println("player 1 の名前を入力してください:");
        player.SetName();
        player.hp = generateNumber(player.name, 0);
        System.out.println("playerHp:" + player.hp);
        player.attack = generateNumber(player.name, 2);
        System.out.println("playerAt:" + player.attack);
        player.def = random.nextInt(generateNumber(player.name, 2));
        System.out.println("playerDef:" + player.def);
        player.luck = generateNumber(player.name, 3);
        System.out.println("player 2 の名前を入力してください:");
        enemy.SetName();
        enemy.hp = generateNumber(enemy.name, 0);
        System.out.println("enemyHp:" + enemy.hp);
        enemy.attack = generateNumber(enemy.name, 2);
        System.out.println("enemyAt:" + enemy.attack);
        enemy.def = random.nextInt(generateNumber(enemy.name, 2));
        System.out.println("enemyDef:" + enemy.def);
        enemy.luck = generateNumber(enemy.name, 3);
        System.out.println("enemyLuck:" + enemy.luck);
        BattleSystem battleSystem = new BattleSystem(player.hp, player.attack,player.def,player.luck, player.name, enemy.hp, enemy.attack,enemy.def,
                enemy.luck,enemy.name);
        battleSystem.Start();
    }

    public static int generateNumber(String name, int index) {
        try {
            String digest = getHashDigest(name);
            String hex = digest.substring(
                index * 2, index * 2 + 2
            );
            return Integer.parseInt(hex, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String getHashDigest(String name) {
        try {
            // ハッシュ値を取得する
            byte[] result =
                MessageDigest.getInstance("SHA-1")
                .digest(name.getBytes());
            return String.format(
                "%040x",
                new BigInteger(1, result)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

class Human {
    String name;
    int hp;
    int attack;
    int def;
    int luck;
    
    Scanner scanner = new Scanner(System.in);

    Human() {
        
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

enum TrunFase {
    playerTurn,
    enemyTurn,
    endFase
}

class BattleSystem {
    int playerHp;
    int enemyHp;
    int playerAt;
    int enemyAt;
    int playerDef;
    int enemyDef;

    int playerLuck;
    int enemyLuck;

    TrunFase state;

    String playerName;
    String enemyName;
    Random random = new Random();
    private int allDamage;
    private int criticalParcent;
    private int criticalRandom;
    private int criticalLuck;
    
    public BattleSystem(int hp, int attack, int def, int luck, String name, int hp2, int attack2, int def2, int luck2, String name2) {
        this.playerHp = hp * 2;
        this.enemyHp = hp2 * 2;
        this.playerAt = attack;
        this.enemyAt = attack2;
        this.playerName = name;
        this.enemyName = name2;
        this.playerDef = def;
        this.enemyDef = def2;
        this.playerLuck = luck / 10;
        this.enemyLuck = luck2 / 10;
        System.out.println("playerLuck:" + playerLuck);
        System.out.println("enemyLuck:" + enemyLuck);
    }
    
    public void Start() {
        System.out.println("----ゲーム開始-----");
        state = TrunFase.playerTurn;
        while (!(state == TrunFase.endFase)) {
            if (playerHp > 0 && state == TrunFase.playerTurn) {
                PlayerAction();
                if (enemyHp < 0) {
                    PlayerWinner();
                    break;
                }
                state = TrunFase.enemyTurn;
            }

            if (enemyHp > 0 && state == TrunFase.enemyTurn) {
                EnemyAction();
                if (playerHp < 0) {
                    EnemyWinner();
                    break;
                }
                state = TrunFase.playerTurn;
            }
            System.out.println("--------次のターン--------");
            System.out.format("プレイヤー1:%s (HP:%d)\n", playerName, playerHp);
            System.out.format("プレイヤー2:%s (HP:%d)\n", enemyName, enemyHp);
        }
    }
        
    public void PlayerAction() {
        System.out.format("%sの攻撃!\n", playerName);
        allDamage = 0;
        criticalParcent = 0;
        criticalRandom = 0;
        criticalLuck = random.nextInt(playerLuck);

        criticalParcent = Criticaler(criticalParcent, criticalLuck);
        //Debug
        System.out.println("criticalParsent:" + criticalParcent);

        criticalRandom = random.nextInt(100) + 1;

        System.out.println(criticalRandom); //43

        //TODO ランダムで数値を決定し、2で割り切れる数値の時のみ会心の一撃を。
        //TDOO luckから数値を取得　OK
        //TODO 数値によって確率を変更 例えば......luckが0から10ならcritical%を10に。luckが11から50ならcritical%を50に。luckが51から255ならcriticalを80%に。　OK
        //TODO critical%を決めたら、criticalDamageをrandomで数値を決める。 OK
        //TODO criticalRandomの数値が、critical%を上回っていたら決定

        if (criticalRandom < criticalParcent) {
            System.out.println("クリティカル!");
            allDamage = playerAt;
            enemyHp -= allDamage;
        } else {
            allDamage = playerAt - enemyDef;
            enemyHp -= allDamage;
        }

        if (allDamage <= 0) {
            System.out.format("%sの攻撃はミス!", playerName);
        } else {
            System.out.format("%sに%dのダメージ!\n", enemyName, allDamage);
        }
        if (enemyHp <= 0) {
            state = TrunFase.endFase;
        }
    }

    public void EnemyAction() {
        System.out.format("%sの攻撃!\n", enemyName);
        int allDamage = 0;

        if (enemyAt > playerDef) {
            allDamage = enemyAt - playerDef;
            playerHp -= allDamage;
        }
        if (allDamage <= 0) {
            System.out.format("%sの攻撃はミス!", enemyName);
        } else {
            System.out.format("%sに%dのダメージ!\n", playerName, allDamage);
        }
    }
    
    private int Criticaler(int criticalParcent, int criticalLuck) {
        if (criticalLuck <= 5) {
            criticalParcent = 10;
        }
        if (criticalLuck > 5 && criticalLuck <= 7) {
            criticalParcent = 30;
        }
        if (criticalLuck > 7 && criticalLuck <= 20) {
            criticalParcent = 50;
        }
        if (criticalLuck > 20) {
            criticalParcent = 80;
        }
        return criticalParcent;
    }
    
    private void EnemyWinner() {
        System.out.format("%sは力尽きた....", playerName);
        System.out.format("%sの勝利!!", enemyName);
        state = TrunFase.endFase;
    }

    private void PlayerWinner() {
        System.out.format("%sは力尽きた....",enemyName);
        System.out.format("%sの勝利!!\n",playerName);
        state = TrunFase.endFase;
    }
}