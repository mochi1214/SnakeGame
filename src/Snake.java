import java.awt.*;
import java.util.ArrayList;

public class Snake {

    // 這個 Array 儲存蛇身的的部分，而每個身體的部分 -> Node
    private ArrayList<Node> snakeBody;

    public Snake() {
        snakeBody = new ArrayList<>();
        // 設定第一個身體的位置
        snakeBody.add(new Node(80, 0));
        // 設定其他的身體的位置
        snakeBody.add(new Node(60, 0));
        snakeBody.add(new Node(40, 0));
        snakeBody.add(new Node(20, 0));
    }

    public ArrayList<Node> getSnakeBody() {
        return snakeBody;
    }

    public void drawSnake(Graphics g) {
        for (int i = 0; i < snakeBody.size(); i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.ORANGE);
            }

            Node n = snakeBody.get(i);
            if (n.x >= Main.width) {
                n.x = 0;
            }
            if (n.x < 0) {
                n.x = Main.width - Main.CELL_SIZE;
            }
            if (n.y >= Main.height) {
                n.y = 0;
            }
            if (n.y < 0) {
                n.y = Main.height - Main.CELL_SIZE;
            }
            g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }
}
