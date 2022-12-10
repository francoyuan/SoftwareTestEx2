package org.example;



public class Node {
    String method;
    int row;
    int node;
    int parent;
    int height;
    int startx;
    int starty;
    int endx;
    int endy;
    String content;

    @Override
    public String toString() {
        return "Node{" +
                "method='" + method + '\'' +
                ", row=" + row +
                ", node=" + node +
                ", parent=" + parent +
                ", height=" + height +
                ", startx=" + startx +
                ", starty=" + starty +
                ", endx=" + endx +
                ", endy=" + endy +
                ", content='" + content + '\'' +
                '}';
    }

    public Node(String method, int row, int node, int parent, int height, int startx, int starty, int endx, int endy, String content) {
        this.method = method;
        this.row = row;
        this.node = node;
        this.parent = parent;
        this.height = height;
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        this.content = content;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getStartx() {
        return startx;
    }

    public void setStartx(int startx) {
        this.startx = startx;
    }

    public int getStarty() {
        return starty;
    }

    public void setStarty(int starty) {
        this.starty = starty;
    }

    public int getEndx() {
        return endx;
    }

    public void setEndx(int endx) {
        this.endx = endx;
    }

    public int getEndy() {
        return endy;
    }

    public void setEndy(int endy) {
        this.endy = endy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
