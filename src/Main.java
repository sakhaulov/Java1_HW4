import java.util.*;

public class Main {

    public static Random rand = new Random();
    public static Scanner scanner = new Scanner(System.in);

    //Player
    public static int player_hp = 100;
    public static int player_attack = 25;
    public static int player_x = 0;
    public static int player_y = 0;
    public static int player_count_max = 1;
    public static String player_symbol = "@";
    public static String player_name = "Игрок";

    //Enemy
    public static int enemy_hp = 50;
    public static int enemy_attack = 5;
    public static int enemy_count_max = 3;
    public static String enemy_symbol = "E";
    public static String enemy_name = "Враг";

    //Map
    public static int map_height = 15;
    public static int map_width = 15;
    public static String[][] map = new String[map_height][map_width];
    public static String map_empty = ".";
    public static String map_explored = "X";

    //Wall
    public static String wall_symbol = "#";
    public static int wall_count_max = 10;

    //Difficulty
    public static int difficulty = 0;


    public static void main(String[] args) {
        start();
    }


    public static void start() {
        setDifficulty();
        init();
        play();
    }


    public static void setDifficulty() {
        while (difficulty == 0) {

            try {
                System.out.println(
                        "Выберите сложность:\n" +
                                "1 - Лёгкая\n" +
                                "Карта размером 5х5, 2 слабых врага\n" +
                                "2 - Средняя\n" +
                                "Карта размером 10х10, 3 обычных врага\n" +
                                "3 - Высокая\n" +
                                "Карта размером 10х10, 4 сильных врага\n"
                );

                difficulty = scanner.nextInt();

                switch (difficulty) {
                    case 1:
                        map_height = 5;
                        map_width = 5;
                        enemy_count_max = 2;
                        enemy_attack = 5;
                        enemy_hp = 50;
                        wall_count_max = 0;
                        break;
                    case 2:
                        map_height = 10;
                        map_width = 10;
                        enemy_count_max = 3;
                        enemy_attack = 10;
                        enemy_hp = 75;
                        wall_count_max = 3;
                        break;
                    case 3:
                        map_height = 15;
                        map_width = 15;
                        enemy_count_max = 4;
                        enemy_attack = 15;
                        enemy_hp = 100;
                        wall_count_max = 5;
                        break;
                    default:
                        throw new InputMismatchException();
                }

            } catch (InputMismatchException e) {
                System.out.println("Введите число, соответствующее выбранному уровню сложности: 1, 2 или 3");
                difficulty = 0;
                scanner.next();
            }

        }
    }



    public static void init() {
        //Spawn empty map
        map = spawnMap(map_height, map_width);
        //Spawn enemies
        spawn(enemy_symbol, enemy_count_max);
        //Spawn walls
        spawn(wall_symbol, wall_count_max);
        //Spawn player
        spawn(player_symbol, player_count_max);
        setPlayerPosition();
    }


    public static void play() {
        while (true) {

                displayMap();
                movePlayer();

                //Player dies
                if (player_hp <= 0) {
                    System.out.println("*************************************");
                    System.out.println("Вы погибли!\nУдачи в следующий раз");
                    break;

                //Player wins
                } else if (checkWin()) {
                    if (processWin()) {
                        player_hp = 100;
                        difficulty = 0;
                        start();
                    }
                    else break;
                }
            }
        }




    public static boolean checkWin() {
        int explored_count = countExplored();
        return map_height * map_width - wall_count_max - explored_count - 1 == 0;
    }

    public static boolean processWin() {
        while (true) {
            try {

                System.out.println("*************************************");
                displayMap();
                System.out.println("Вы победили!\nПоздравляем!");
                System.out.println(
                        "Сыграть ещё раз?\n" +
                                "1 - Да\n" +
                                "0 - Нет"
                );
                int answer = scanner.nextInt();
                if (answer != 1 && answer != 0) {
                    throw new InputMismatchException();
                }
                return answer == 1;

            } catch (InputMismatchException e) {
                System.out.println("Введите 1 или 0");
                scanner.next();
            }
        }
    }

    public static int countExplored() {
        int count = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == map_explored) count++;
            }
        }
        return count;
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


    public static void displayMap() {
        printBorder();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {

                if ( j == 0) System.out.print("#\t");

                //Закомментировать цикл, чтобы враги отображались на карте
                if (map[i][j] == enemy_symbol) {
                    System.out.print(map_empty + "\t");
                } else {
                    System.out.print(map[i][j] + "\t");
                }

                if (j == map[i].length-1) System.out.print("#");


                //Раскомментировать, чтобы враги отображались на карте
                //System.out.print(map[i][j] + "\t");
            }
            System.out.println();
        }
        printBorder();
    }


    public static void printBorder() {
        for (int i = 0; i < map_width+1; i++) {
            System.out.print("#\t");
        }
        System.out.print("#");
        System.out.println();
    }


    public static int[] getRandomPosition() {
        int[] position = new int[2];
        int x = rand.nextInt(map_height);
        int y = rand.nextInt(map_width);
        position[0] = x;
        position[1] = y;
        //Debug
        //System.out.println(Arrays.toString(position));
        return position;
    }


    public static void setPlayerPosition() {

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == player_symbol) {
                    player_x = j;
                    player_y = i;

                    //Debug
                    //System.out.println("x - " + player_x + " y - " + player_y);
                }
            }
        }
    }


    public static void setSymbol(int x, int y, String symbol) {
        map[y][x] = symbol;

        //Debug
        //System.out.println("X - " + x + " Y - " + y + " Symbol " + symbol);
    }


    public static String checkSymbol(int x, int y) {
        return map[y][x];
    }


    public static void spawn(String symbol, int count_max) {
        int count = 0;
        while (count < count_max) {
            int[] position = getRandomPosition();
            if (map[position[0]][position[1]] == map_empty) {
                map[position[0]][position[1]] = symbol;
                count++;
            }
        }
    }


    public static void movePlayer() {
        try {
            System.out.println(
                    "Выберите направление движения:\n" +
                            "Вверх - 8\n" +
                            "Вниз - 2\n" +
                            "Влево - 4\n" +
                            "Вправо - 6\n"
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
            }
        } catch (InputMismatchException e) {
            System.out.println("Введите одно из следующих значений: 8, 2, 4, 6");
        }
    }


    public static void fight(int x, int y) {
        System.out.println("*************************************");
        System.out.println(
                "На вас напал враг!\n" +
                        "Начинается бой"
        );

        while (true) {
            try {
                System.out.println("*************************************");
                System.out.print("Ваше здоровье: " + player_hp + "HP    ");
                System.out.print("Здоровье врага: " + enemy_hp + "HP \n");
                System.out.println(
                        "Выберите действие:\n" +
                                "Ударить мечом - 0\n" +
                                "Блокировать щитом - 1\n" +
                                "Атаковать магией - 2\n"
                );

                int player_action = scanner.nextInt();
                int enemy_action = enemyAction();

                processFight(player_action, enemy_action);

                if (enemy_hp <= 0) {
                    enemy_hp = 50;
                    System.out.println("*************************************");
                    System.out.println("Враг повержен!");
                    moveSuccess(x, y);
                    break;
                } else if (player_hp <= 0) {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Введите одно из следующих значений: 0, 1, 2");
                scanner.next();
            }
        }
    }


    public static int enemyAction() {
        return rand.nextInt(3);
    }


    public static void displayAction(int action, String name) {
        switch (action) {
            case 0:
                System.out.println(name + " атакует мечом"); //ножницы
                break;
            case 1:
                System.out.println(name + " поднимает щит"); //камень
                break;
            case 2:
                System.out.println(name + " читает заклинание"); //бумага
                break;
        }
    }


    public static void playerTakeDamage() {
        System.out.println("Вы получили "+enemy_attack+" урона");
        player_hp -= enemy_attack;
    }


    public static void enemyTakeDamage() {
        System.out.println("Враг получил "+player_attack+" урона");
        enemy_hp -= player_attack;
    }


    public static void moveSuccess(int x, int y) {
        setSymbol(player_x, player_y, map_explored);
        player_x = x;
        player_y = y;
        setSymbol(player_x, player_y, player_symbol);
    }


    public static void processFight(int player_action, int enemy_action) {
        displayAction(player_action, player_name);
        displayAction(enemy_action, enemy_name);

        if (player_action == enemy_action) {
            System.out.println("Ваши атаки нейтрализовали друг друга!");
        } else if (player_action == 0 && enemy_action == 1) {
            System.out.println("Враг отразил вашу атаку и ударил вас в ответ");
            playerTakeDamage();
        } else if (player_action == 0 && enemy_action == 2) {
            System.out.println("Вы успешно ударили врага");
            enemyTakeDamage();
        } else if (player_action == 1 && enemy_action == 0) {
            System.out.println("Вы отразили атаку врага и ударили его в ответ");
            enemyTakeDamage();
        } else if (player_action == 1 && enemy_action == 2) {
            System.out.println("Магия врага пробивает ваш щит");
            playerTakeDamage();
        } else if (player_action == 2 && enemy_action == 0) {
            System.out.println("Враг ударил вас");
            playerTakeDamage();
        } else if (player_action == 2 && enemy_action == 1) {
            System.out.println("Ваша магия пробила щит врага");
            enemyTakeDamage();
        }
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
                System.out.println("Нельзя ходить сквозь стены");
            } else if (checkSymbol(x, y) == enemy_symbol) {
                fight(x, y);
            }
        } else {
            System.out.println(
                    "Вы пытаетесь выйти за край карты\n"+
                    "Выберите другое направление"
            );
        }
    }


    public static boolean isValidPosition(int x, int y) {
        return (x < map_width && x >= 0 && y < map_height && y >=0);
    }
}
