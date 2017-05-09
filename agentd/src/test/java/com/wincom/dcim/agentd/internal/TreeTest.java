package com.wincom.dcim.agentd.internal;

import static java.lang.System.out;
import org.junit.Test;

/**
 *
 * @author master
 */
public class TreeTest {

    private int counter = 0;

    private int sequence() {
        counter++;
        return counter;
    }

    private void printNode(Node n) {
        out.println(n.data);
    }

    private static class Node {

        public Node parent;
        public Node leftChild;
        public Node rightChild;
        public int data;

        public Node() {
        }

        public Node(Node parent) {
            this.parent = parent;
        }
    }

    @Test
    public void test() {
        Node root = new Node();
        root.data = sequence();

        root.leftChild = new Node(root);
        root.rightChild = new Node(root);
        root.leftChild.data = sequence();
        root.rightChild.data = sequence();
        root.leftChild.leftChild = new Node(root.leftChild);
        root.leftChild.rightChild = new Node(root.leftChild);
        root.leftChild.leftChild.data = sequence();
        root.leftChild.rightChild.data = sequence();
        root.rightChild.leftChild = new Node(root.rightChild);
        root.rightChild.rightChild = new Node(root.rightChild);
        root.rightChild.leftChild.data = sequence();
        root.rightChild.rightChild.data = sequence();

        traverse(root);
    }

    private void traverse(Node root) {
        Node node = root;
        boolean backtrack = false;

        while (true) {
            if (!backtrack) {
                printNode(node);
                if (node.leftChild != null) {
                    node = node.leftChild;
                } else if (node.rightChild != null) {
                    node = node.rightChild;
                } else {
                    // is leaf
                    backtrack = true;
                }
            }
            if (backtrack) {
                if (node.parent != null) {
                    if (node.parent.rightChild != node 
                            && node.parent.rightChild != null) {
                        node = node.parent.rightChild;
                        backtrack = false;
                    } else {
                        // continue backtrack...
                        node = node.parent;
                    }
                } else {
                    // reach the root, stop 
                    break;
                }
            }
        }
    }
}
