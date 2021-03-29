package com.ecc.model;

public class ModelCell implements Comparable<ModelCell> {

    private String left;
    private String right;

    public ModelCell(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    @Override
    public int compareTo(ModelCell o) {

        return this.toString().trim().compareTo(o.toString().trim());
    }

}
