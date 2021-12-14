import javax.sound.midi.Soundbank;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static Random rand = new Random();
    public static Scanner scanner = new Scanner(System.in);

    //Player
    public static int player_hp = 100;
    public static int player_attack = 20;
    public static int player_x = 0;
    public static int player_y = 0;
    public static String player_symbol = "@";
    public static int player_count_max = 1;

    //Enemy
    public static int enemy_hp = 100;
    public static int enemy_attack = 10;
    public static String enemy_symbol = "E";
    public static int enemy_count_max = 3;

    //Map
    public static int map_height = 5;
    public static int map_width = 10;
    public static String[][] map = new String[map_height][map_width];
    public static String map_empty = ".";
    public static String map_explored = "X";

    //Wall
    public static String wall_symbol = "|";
    public static int wall_count_max = 5;

    public static void main(String[] args) {
        map = spawnMap(map_height, map_width);

        getRandomPosition(map_height, map_width);
        //Spawn enemies
        spawn(enemy_symbol, enemy_count_max);
        //Spawn walls
        spawn(wall_symbol, wall_count_max);
        //Spawn player
        spawn(player_symbol, player_count_max);
        setPlayerPosition(map);

        System.out.println("x - " + player_x + " y - " + player_y);

        while (true) {
            displayMap(map);
            movePlayer();
        }

    }


    public static String[][] spawnMap(int map_height, int map_width) {
        String[][] map = new String[map_height][map_width];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = map_empty;
            }
        }
        return map;
    }


    public static void displayMap(String[][] map) {

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
//                if (map[i][j] == enemy_symbol) {
//                    System.out.print(map_empty + "\t");
//                } else {
//                    System.out.print(map[i][j] + "\t");
//                }
                System.out.print(map[i][j] + "\t");
            }
            System.out.println();
        }
    }


    public static int[] getRandomPosition(int map_height, int map_width) {
        int[] position = new int[2];
        int x = rand.nextInt(map_height);
        int y = rand.nextInt(map_width);
        position[0] = x;
        position[1] = y;
        System.out.println(Arrays.toString(position));
        return position;
    }


    public static void setPlayerPosition(String[][] map) {
        int[] position = new int[2];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == player_symbol) {
                    player_x = j;
                    player_y = i;
                    System.out.println("x - " + player_x + " y - " + player_y);
                }
            }
        }
    }


    public static void setSymbol(int x, int y, String symbol) {
        map[y][x] = symbol;
        System.out.println("X - " + x + " Y - " + y + " Symbol " + symbol);
    }


    public static String checkSymbol(int x, int y) {
        return map[y][x];
    }


    public static void spawn(String symbol, int count_max) {
        int count = 1;
        while (count <= count_max) {
            int[] position = getRandomPosition(map_height, map_width);
            if (map[position[0]][position[1]] == map_empty) {
                map[position[0]][position[1]] = symbol;
                count++;
            }
        }
    }


    public static void movePlayer() {

        System.out.println(
                "Выберите направление:\nВверх - 8\nВниз - 2\nВлево - 4\nВправо - 6\n"
        );
        int direction = scanner.nextInt();
        int temp;

        switch (direction) {
            case 8:
                temp = player_y - 1;
                processMovement(player_x, temp);
                break;
            case 2:
                temp = player_y + 1;
                processMovement(player_x, temp);
                break;
            case 4:
                temp = player_x - 1;
                processMovement(temp, player_y);
                break;
            case 6:
                temp = player_x + 1;
                processMovement(temp, player_y);
                break;

            default:
                System.out.println("Неверное значение");
                break;

        }

    }
    public static void fight() {
        System.out.println("Пыщь");
    }

    public static void processMovement(int x, int y) {

        if (isValidPosition(x, y)) {
            String next_position = checkSymbol(x, y);
            if (next_position == map_empty || next_position == map_explored) {
                setSymbol(player_x, player_y, map_explored);
                player_x = x;
                player_y = y;
                setSymbol(player_x, player_y, player_symbol);
            } else if (checkSymbol(x, y) == wall_symbol) {
                System.out.println("Нельзя ходить через стены");
            } else if (checkSymbol(x, y) == enemy_symbol) {
                System.out.println("Начинается бой");
                fight();
            }
        } else {
            System.out.println("Выберите другое направление");
        }
    }

    public static boolean isValidPosition(int x, int y) {
        return (x < map_width && x >= 0 && y < map_height && y >=0);
    }
}
