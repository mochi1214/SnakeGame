import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JPanel implements KeyListener {

    // 在這視窗中每一格會有多大
    public static final int CELL_SIZE = 20;

    // 加 static 所有該類的實例共享同一個 width 變量 -> Main.width
    // 如果不加 static，每個實例都有自己獨立的 width，就只能通過實例來訪問 -> new Main().width
    public static int width = 400;
    public static int height = 400;

    // 總共會有多少格橫列的數量
    public static int row = height / CELL_SIZE;
    // 直列
    public static int column = width / CELL_SIZE;
    public int testHaha = 10;
    private Snake snake;
    private Fruit fruit;
    private Timer t;
    private int speed = 100;
    private static String direction;
    private boolean allowKeyPress;
    private int score;
    private int highest_score;

    String desktop = System.getProperty("user.home") + "/Desktop/";
    String myFile = desktop + "filename.txt";

    public Main() {
        read_highest_score();
        reset();
        addKeyListener(this);
    }

    private void setTimer() {
        t = new Timer();
        // scheduleAtFixedRate 在每隔一個固定的時間執行某件事
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        },0 ,speed); // speed 設定每隔多久執行事件
    }

    private void reset() {
        score = 0;
        if (snake != null) {
            snake.getSnakeBody().clear();
        }
        allowKeyPress = true;
        direction = "Right";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }

    // 繪製圖形、定義外觀、更新畫面
    @Override
    public void paintComponent(Graphics g) {

        ArrayList<Node> snake_body = snake.getSnakeBody();
        Node head = snake_body.get(0);

        // 檢查 snake 有沒有咬到自己
        for (int i = 1; i < snake_body.size(); i++) {
            if (snake_body.get(i).x == head.x && snake_body.get(i).y == head.y) {
                allowKeyPress = false;
                t.cancel();
                t.purge();
                int response = JOptionPane.showOptionDialog(this, "最高分數為 ： " + highest_score + "\n你要繼續遊戲嗎？ \n這次分數為： " + score, "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.YES_OPTION);
                write_a_file(score);
                switch (response) {
                    case JOptionPane.CANCEL_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }

        // 畫一個黑色背景
        g.fillRect(0, 0, width, height);

        // 先畫水果再畫蛇，蛇先吃掉水果才重新畫蛇，視覺上才符合邏輯
        fruit.drawFruit(g);
        snake.drawSnake(g);

        // remove snake tail and put in head
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;
        if (direction.equals("Left")) {
            snakeX -= CELL_SIZE;
        } else if (direction.equals("Up")) {
            snakeY -= CELL_SIZE;
        } else if (direction.equals("Right")) {
            snakeX += CELL_SIZE;
        } else if (direction.equals("Down")) {
            snakeY += CELL_SIZE;
        }
        Node newHead = new Node(snakeX, snakeY);

        // 檢查 snake 有沒有吃到 fruit，判斷蛇的頭跟水果的座標有沒有重疊
        if (snake.getSnakeBody().get(0).x == fruit.getX() && snake.getSnakeBody().get(0).y == fruit.getY()) {
            // 1.把水果放在新的地方
            fruit.setNewLocation(snake);
            // 2.畫新的水果
            fruit.drawFruit(g);
            // 3.吃到水果後新增分數
            score++;
        } else {
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
        }
        snake.getSnakeBody().add(0, newHead);

        allowKeyPress = true;
        requestFocusInWindow();
    }

    // 使用 getPreferredSize 是因為這個方法影響的是元件的大小，而不是整個視窗的大小
    // 如果只是用 window.setSize(width, height) 只會影響視窗大小
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public static void main(String[] args) {
        createWindow();
    }

    public static void createWindow() {
        JFrame window = new JFrame("Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack(); // 讓元件自動依視窗調整大小
        window.setLocationRelativeTo(null); // 設定為 null，讓視窗維持在螢幕中間
        window.setVisible(true); // 顯示視窗
        window.setResizable(false); // 禁止使用者調整視窗大小
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (allowKeyPress) {
            switch (k) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                    if (!direction.equals("Right")) {
                        direction = "Left";
                    }
                    break;
                case KeyEvent.VK_UP, KeyEvent.VK_W:
                    if (!direction.equals("Down")) {
                        direction = "Up";
                    }
                    break;
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                    if (!direction.equals("Left")) {
                        direction = "Right";
                    }
                    break;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                    if (!direction.equals("Up")) {
                        direction = "Down";
                    }
                    break;
            }
            allowKeyPress = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void read_highest_score() {
        File myObj = new File(myFile);

        if (!myObj.exists()) {
            try {
                if (myObj.createNewFile()) {
                    System.out.println("File created: " + myObj.getName());
                }
                try (FileWriter myWriter = new FileWriter(myObj)) {
                    myWriter.write("" + 0);
                    highest_score = 0;
                }
            } catch (IOException e) {
                System.out.println("發生未知的錯誤");
                e.printStackTrace();
            }
        } else {
            try (Scanner myReader = new Scanner(myObj)) {
                if (myReader.hasNextInt()) {
                    highest_score = myReader.nextInt();
                } else {
                    highest_score = 0;
                }
            } catch (FileNotFoundException e) {
                System.out.println("讀取文件時發生錯誤");
                e.printStackTrace();
            }
        }
    }

    public void write_a_file(int score) {
        try {
            FileWriter myWriter = new FileWriter(myFile);
            if (score > highest_score) {
                myWriter.write("" + score);
                highest_score = score;
            } else {
                myWriter.write("" + highest_score);
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}