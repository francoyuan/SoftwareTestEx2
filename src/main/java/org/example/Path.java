package org.example;

import java.util.ArrayList;

public class Path {
    ArrayList<Integer> path;
    boolean state; //current path can't be longer(used for simple path generation)

    public Path(ArrayList<Integer> path, boolean state) {
        this.path = path;
        this.state = state;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void setPath(ArrayList<Integer> path) {
        this.path = path;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Path{" +
                "path=" + path +
                ", state=" + state +
                '}';
    }
}
