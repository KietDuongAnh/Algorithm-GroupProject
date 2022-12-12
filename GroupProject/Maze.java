package GroupProject;

public class Maze {
    int rows;
    int cols;
    String[] map;
    int robotRow;
    int robotCol;
    int steps;

    public Maze() {
        // Note: in my real test, I will create much larger
        // and more complicated map
        rows = 4;
        cols = 5;
        map = new String[rows];
        map[0] = ".X...";
        map[1] = ".   .";
        map[2] = ".   .";
        map[3] = ".....";
        robotRow = 2;
        robotCol = 1;
        steps = 0;
    }

    public String go(String direction) {
        if (!direction.equals("UP") &&
                !direction.equals("DOWN") &&
                !direction.equals("LEFT") &&
                !direction.equals("RIGHT")) {
            // invalid direction
            steps++;
            return "false";
        }
        int currentRow = robotRow;
        int currentCol = robotCol;
        if (direction.equals("UP")) {
            currentRow--;
        } else if (direction.equals("DOWN")) {
            currentRow++;
        } else if (direction.equals("LEFT")) {
            currentCol--;
        } else {
            currentCol++;
        }

        System.out.println(direction);
        // check the next position
        if (map[currentRow].charAt(currentCol) == 'X') {
            // Exit gate
            steps++;
            System.out.println("Steps to reach the Exit gate " + steps);
            return "win";
        } else if (map[currentRow].charAt(currentCol) == '.') {
            // Wall
            steps++;
            return "false";
        } else {
            // Space => update robot location
            steps++;
            robotRow = currentRow;
            robotCol = currentCol;
            return "true";
        }
    }

    public static void main(String[] args) {
        (new Robot()).navigate();
    }
}

class Point {
    String stage;
    String[] directions;
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.stage = "unvisited";
        this.directions = new String[]{"UP", "DOWN", "RIGHT", "LEFT"};
    }

    static Point getNextPoint(int curX, int curY, int x, int y) {
        return new Point(curX + x, curY + y);
    }

     Point goUp() {
        Point p = getNextPoint(this.x, this.y, -1, 0);

        return p;
    }

    Point goDown() {
        Point p = getNextPoint(this.x, this.y, 1, 0);

        return p;
    }

    Point goRight() {
        Point p = getNextPoint(this.x, this.y, 0, 1);

        return p;
    }

    Point goLeft() {
        Point p = getNextPoint(this.x, this.y, 0, -1);

        return p;
    }

    void setStage(String stage) {
        this.stage = stage;
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }

    boolean isWrongPath() {
        for (int i = 0; i < directions.length; i++) {
            if (!directions[i].equals("wrong") && !directions[i].equals("visited")) {
                return false;
            }
        }
        return true;
    }
}

class PointCollection {
    Point[] points;
    int size;

    public PointCollection () {
        size = 0;
        points = new Point[1000*1000];
    }

    boolean isExplored(int x, int y) {
        for (int i = 0; i < size; i++) {
            if (points[i].getX() == x && points[i].getY() == y) {
                return true;
            }
        }
        return false;
    }

    boolean addPoint(Point p) {
        if (isExplored(p.getX(), p.getY())) {
            return false;
        }
        points[size] = p;
        size++;
        return true;
    }

    boolean isEmpty() {
        return size == 0;
    }

    boolean pop() {
        if (isEmpty()) {
            return false;
        }
        size--;
        return true;
    }

    Point peek() {
        return points[size - 1];
    }

    void setPointStage (int x, int y, String stage) {
        for (int i = 0; i < size; i++) {
            if (points[i].getX() == x && points[i].getY() == y) {
                points[i].setStage(stage);
            }
        }
    }
}

class Robot {
    // A very simple implementation
    // where the robot just go randomly
    public void navigate() {
        Maze maze = new Maze();
        Point start = new Point(0, 0);
        PointCollection path = new PointCollection();

        if (testMove(maze, start, path)) {
            System.out.println("Solved");
        } else {
            System.out.println("No solution");
        }
    }

//    public boolean explore(Maze maze, Point p, PointCollection collection, ArrayStack path) {
//
//    }

    public boolean testMove(Maze maze, Point p, PointCollection path) {
        String[] moveForward = {"UP", "RIGHT", "LEFT", "DOWN"};
        String[] moveBack = {"DOWN", "LEFT", "RIGHT", "UP"};
        String res;

        path.addPoint(p);
//        if (path.isEmpty()) {
//            return false;
//        }
//
//        if (p.isWrongPath()) {
//            return false;
//        }

        for (int i = 0; i < 4; i++) {
            if (p.directions[i].equals("wrong") || p.directions[i].equals("visited")) {
                continue;
            }
            res = maze.go(moveForward[i]);

            if (res.equals("win")) {
                return true;
            }
            if (res.equals("true")) {
                if (moveForward[i].equals("UP")) {
                    p.directions[i] = "visited";
                    Point newPoint = p.goUp();
                    newPoint.directions[3 - i] = "visited";
                    if (testMove(maze, newPoint, path)) {
                        return true;
                    }
                }
                if (moveForward[i].equals("DOWN")) {
                    p.directions[i] = "visited";
                    Point newPoint = p.goDown();
                    newPoint.directions[3 - i] = "visited";
                    if (testMove(maze, newPoint, path)) {
                        return true;
                    }
                }
                if (moveForward[i].equals("RIGHT")) {
                    p.directions[i] = "visited";
                    Point newPoint = p.goRight();
                    newPoint.directions[3 - i] = "visited";
                    if (testMove(maze, newPoint, path)) {
                        return true;
                    }
                }
                if (moveForward[i].equals("LEFT")) {
                    p.directions[i] = "visited";
                    Point newPoint = p.goLeft();
                    newPoint.directions[3 - i] = "visited";
                    if (testMove(maze, newPoint, path)) {
                        return true;
                    }
                }
            }
            if (res.equals("false")) {
                p.directions[i] = "wrong";
                maze.go(moveBack[i]);
                path.pop();
                return false;
            }
        }
        return false;
    }
}
