package MazeSolverRecursive;

import java.util.Arrays;

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
            System.out.println("Current: " + robotRow + ", " + robotCol);
            steps++;
            return "false";
        } else {
            // Space => update robot location
            steps++;
            robotRow = currentRow;
            robotCol = currentCol;
            System.out.println("Current: " + robotRow + ", " + robotCol);
            return "true";
        }
    }

    public static void main(String[] args) {
        (new Robot()).navigate();
    }
}

class Robot {
    String[] directions;
    ArrayList<String> path;
    // A very simple implementation
    // where the robot just go randomly
    public Robot() {
        directions = new String[]{"UP", "LEFT", "RIGHT", "DOWN"};
        path = new ArrayList<String>();
    }

    public void navigate() {
        Maze maze = new Maze();
        Point start = new Point(0, 0);
//        start.setPosDir(2, false);
        PointCollection visited = new PointCollection();
//        visited.push(start);
        PointCollection wall = new PointCollection();
        System.out.println("Current: " + maze.robotRow + ", " + maze.robotCol);
        if (explore(maze, start, visited, wall)) {
            System.out.println("Solved");
            System.out.println("Path: ");
            path.display();
        } else {
            System.out.println("No solution");
        }
    }

    boolean explore(Maze maze, Point p, PointCollection visited, PointCollection wall) {

        if (p.isExplored(maze, visited, wall) || visited.isExisting(p.getX(), p.getY()) || wall.isExisting(p.getX(), p.getY())) {
            System.out.println("GO BACK");
            // Not finished
//            p.setStage("visited");
//            visited.push(p);
//            int currentDir = p.getIntDirection();
//            Point prev = p.getPrev();
//            if (prev == null) {
//                return false;
//            }
//            boolean[] prevPosDir = prev.getPosDir();
//            prev.setPosDir(currentDir, false);
////            System.out.println(p.getDirectionTo(prev));
//            maze.go(p.getDirectionTo(prev));
//            for (int i = 0; i < prevPosDir.length; i++) {
//                if (prevPosDir[i]) {
//                    prev.setDirection(i);
//                    break;
//                }
//            }
//            System.out.println("Current " + p);
//            System.out.println("Prev " + prev);
//            return explore(maze, prev, visited, wall);
            return false;
        }

//        System.out.println(p);

        int currentDir = p.getIntDirection();
        System.out.println(currentDir);
        int x = p.getX();
        int y = p.getY();
        boolean[] posDirections = p.getPosDir();

        String feedback = maze.go(p.getStrDirection());
        path.push(p.getStrDirection());
        System.out.println("Response:" + feedback);
        if (feedback.equals("win")) {
            return true;
        }

        if (feedback.equals("false")) {
            if (currentDir == 0) {
                Point wallPoint = new Point(x - 1, y);
                wallPoint.setStage("wall");
                wall.push(wallPoint);
            }
            if (currentDir == 1) {
                Point wallPoint = new Point(x, y - 1);
                wallPoint.setStage("wall");
                wall.push(wallPoint);
            }
            if (currentDir == 2) {
                Point wallPoint = new Point(x + 1, y);
                wallPoint.setStage("wall");
                wall.push(wallPoint);
            }
            if (currentDir == 3) {
                Point wallPoint = new Point(x, y + 1);
                wallPoint.setStage("wall");
                wall.push(wallPoint);
            }
            p.setPosDir(currentDir, false);
            boolean[] nextDirections = p.getPosDir();
            for (int i = 0; i < nextDirections.length; i++) {
                if (posDirections[i]) {
                    p.setDirection(i);
                    break;
                }
            }
            if (p.getIntDirection() == 3) {
                Point right = new Point(x, y + 1);
                right.setDirection(3);
//                p.setPosDir(3, false);
                Point prev = p.getPrev();
                int prevDir = prev.currentDir;
                if (prevDir == 0) {
                    right.setPosDir(2, false);
                }
                if (prevDir == 1) {
                    right.setPosDir(3, false);
                }
                if (prevDir == 2) {
                    right.setPosDir(0, false);
                }
                if (prevDir == 3) {
                    right.setPosDir(1, false);
                }
                right.setPrev(p);
                return explore(maze, right, visited, wall);
            }
//            System.out.println("False:" + p);
            return explore(maze, p, visited, wall);
        }

        if (feedback.equals("true")) {
            p.setStage("visited");
            visited.push(p);
            if (!visited.isExisting(x - 1, y) && !wall.isExisting(x - 1, y) && posDirections[0]) {
                // Go up
                Point up = new Point(x - 1, y);
                up.setDirection(0);
                p.setPosDir(currentDir, false);
                up.setPrev(p);
                if (currentDir == 0) {
                    up.setPosDir(2, false);
                }
                if (currentDir == 1) {
                    up.setPosDir(3, false);
                }
                if (currentDir == 2) {
                    up.setPosDir(0, false);
                }
                if (currentDir == 3) {
                    up.setPosDir(1, false);
                }
//            System.out.println(p);
//            System.out.println(up);
                if (explore(maze, up, visited, wall)) {
                    return true;
                }
            }
            if (!visited.isExisting(x, y - 1) && !wall.isExisting(x, y - 1) && posDirections[1]) {
                // Go left
                Point left = new Point(x, y - 1);
                left.setDirection(1);
                p.setPosDir(1, false);
                left.setPrev(p);
                if (currentDir == 0) {
                    left.setPosDir(2, false);
                }
                if (currentDir == 1) {
                    left.setPosDir(3, false);
                }
                if (currentDir == 2) {
                    left.setPosDir(0, false);
                }
                if (currentDir == 3) {
                    left.setPosDir(1, false);
                }
                if (explore(maze, left, visited, wall)) {
                    return true;
                }
            }
            if (!visited.isExisting(x + 1, y) && !wall.isExisting(x + 1, y) && posDirections[2]) {
                // Go down
                Point down = new Point(x + 1, y);
                down.setDirection(2);
                p.setPosDir(2, false);
                down.setPrev(p);
                if (currentDir == 0) {
                    down.setPosDir(2, false);
                }
                if (currentDir == 1) {
                    down.setPosDir(3, false);
                }
                if (currentDir == 2) {
                    down.setPosDir(0, false);
                }
                if (currentDir == 3) {
                    down.setPosDir(1, false);
                }
                if (explore(maze, down, visited, wall)) {
                    return true;
                }
            }
            if (!visited.isExisting(x, y + 1) && !wall.isExisting(x, y + 1) && posDirections[3]) {
                // Go right
                Point right = new Point(x, y + 1);
                right.setDirection(3);
                p.setPosDir(3, false);
                right.setPrev(p);
                if (currentDir == 0) {
                    right.setPosDir(2, false);
                }
                if (currentDir == 1) {
                    right.setPosDir(3, false);
                }
                if (currentDir == 2) {
                    right.setPosDir(0, false);
                }
                if (currentDir == 3) {
                    right.setPosDir(1, false);
                }
                if (explore(maze, right, visited, wall)) {
                    return true;
                }
            }
        }
        return false;
    }
}

class Point {
    int x, y;
    int currentDir; // o -> UP; 1 -> LEFT; 2 -> DOWN; 3 -> RIGHT
    boolean[] posDir; // possible directions the point can move
    String stage;
    Point prev;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.stage = "unvisited";
        this.currentDir = 0;
        this.posDir = new boolean[]{true, true, true, true};
        this.prev = null;
    }

    void setStage(String stage) {
        this.stage = stage;
    }

    public void setPrev(Point prev) {
        this.prev = prev;
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }

    Point getPrev() {
        return this.prev;
    }

    int getIntDirection() {
        return currentDir;
    }

    String getStrDirection() {
        if (currentDir == 0) {
            return "UP";
        }
        if (currentDir == 1) {
            return "LEFT";
        }
        if (currentDir == 2) {
            return "DOWN";
        }
        if (currentDir == 3) {
            return "RIGHT";
        }
        return "START";
    }

    String getStage() {
        return stage;
    }

    public boolean[] getPosDir() {
        return posDir;
    }

    public void setDirection(int direction) {
        this.currentDir = direction;
    }

    public void setPosDir(int direction, boolean bool) {
        this.posDir[direction] = bool;
    }

    boolean isExplored(Maze maze, PointCollection visited, PointCollection wall) {
        int count = 0;
        // UP
        if (visited.isExisting(x - 1, y) || wall.isExisting(x - 1, y) || !this.getPosDir()[0]) {
            count++;
        }
        // LEFT
        if (visited.isExisting(x, y - 1) || wall.isExisting(x, y - 1) || !this.getPosDir()[1]) {
            count++;
        }
        // DOWN
        if (visited.isExisting(x + 1, y) || wall.isExisting(x + 1, y) || !this.getPosDir()[2]) {
            count++;
        }
        //RIGHT
        if (visited.isExisting(x, y + 1) || wall.isExisting(x, y + 1) || !this.getPosDir()[3]) {
            count++;
        }
        return count == 4;
    }

    // Return direction from one point to an adjacent point
    String getDirectionTo (Point next) {
        int currX = this.getX();
        int currY = this.getY();
        int nextX = next.getX();
        int nextY = next.getY();
        String res = "";
        if (currX < nextX && currY == nextY) {
            res = "DOWN";
        }
        if (currX > nextX && currY == nextY) {
            res = "UP";
        }
        if (currX == nextX && currY > nextY) {
            res = "LEFT";
        }
        if (currX == nextX && currY < nextY) {
            res = "RIGHT";
        }
        return res;
    }

    @Override
    public String toString() {
        if (this == null) {
            return "null";
        }
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", currentDir=" + currentDir +
                ", posDir=" + Arrays.toString(posDir) +
                ", stage='" + stage + '\'' +
                '}';
    }
}

class PointCollection {
    Point[] points;
    int size;

    public PointCollection() {
        size = 0;
        points = new Point[1000 * 1000];
    }

    boolean isExisting(int x, int y) {
        for (int i = 0; i < size; i++) {
            if (points[i].getX() == x && points[i].getY() == y) {
                return true;
            }
        }
        return false;
    }

    boolean push(Point p) {
        points[size] = p;
        size++;
//        System.out.println("Push is called!");
        return true;
    }

    boolean isEmpty() {
        return size == 0;
    }

    boolean pop() {
        if (isEmpty()) {
            return false;
        }
//        System.out.println("Pop is called!");
        size--;
        return true;
    }

    Point peek() {
        return points[size - 1];
    }

    public void display() {
        System.out.println("Visited: ");
        for (int i = 0; i < size; i++) {
            System.out.println(points[i]);
        }
    }
}

class ArrayList<T> {
    T[] items;
    int size;
    static final int MAX_SIZE = 1000*1000;

    public ArrayList() {
        size = 0;
        items = (T[]) new Object[MAX_SIZE];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean push(T item) {
        if (size < MAX_SIZE) {
            items[size] = item;
            size++;
            return true;
        }
        return false;
    }

    public T getItem(int i) {
        if (i < size) {
            return items[i];
        }
        System.out.println("Out of index!");
        return null;
    }

    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.printf(getItem(i) + " ");
        }
    }
}



