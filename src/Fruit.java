import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Fruit {

    private int x;
    private int y;
    private ImageIcon img;

    public Fruit() {
        img = new ImageIcon(getClass().getResource("/images/fruit.png"));
//        img = new ImageIcon("fruit.png");

        // 先加 floor 小數點無條件捨去; random 產出 0~1 之間亂數
        // Main.column 為了讓水果不超出視窗水平
        // 視窗 column 是20，如果不加上 Main.CELL_SIZE，那麼產生的座標就會落在 0~19 之間，畫出來的水果只會集中在左上
        this.x = (int) (Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
        this.y = (int) (Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void drawFruit(Graphics g) {
        img.paintIcon(null, g, this.x, this.y);
//        g.setColor(Color.RED);
//        g.fillOval(this.x, this.y, Main.CELL_SIZE, Main.CELL_SIZE);
    }

    // 要設定 Snake 是為了產生新的水果時要避開 Snake 本身的位置
    public void setNewLocation(Snake s) {
        int new_x;
        int new_y;
        boolean overlapping; // 看新的位置有沒有跟蛇重疊

        do {
            new_x = (int) (Math.floor(Math.random() * Main.column) * Main.CELL_SIZE);
            new_y = (int) (Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
            overlapping = check_overlap(new_x, new_y, s);
        } while (overlapping); // 如果有重疊就重新產生新的位置到沒有重疊

        this.x = new_x;
        this.y = new_y;
    }

    private boolean check_overlap(int x, int y, Snake s) {

        ArrayList<Node> snake_body = s.getSnakeBody();

        for (int j = 0; j < s.getSnakeBody().size(); j++) {
            // 檢查如果有重疊就回傳 true
            if (x == snake_body.get(j).x && y == snake_body.get(j).y) {
                return true;
            }
        }
        // 檢查如果沒有重疊就回傳 false
        return false;
    }
}
