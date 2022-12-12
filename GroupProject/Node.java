package GroupProject;

public class Node {
    String data;
    boolean visited;
    Node next;

    public Node(String data) {
        this.data = data;
        this.visited = false;
        this.next = null;
    }
    public void setData(String data) {
        this.data = data;
    }
}