// This is mutant program.
// Author : ysma

package stack;


import java.util.NoSuchElementException;


public class LinkedListStack
{

    private stack.LinkedListStack.Node first;

    public LinkedListStack()
    {
        first = null;
    }

    public void push( java.lang.Object element )
    {
        stack.LinkedListStack.Node newNode = new stack.LinkedListStack.Node();
        newNode.data = element;
        newNode.next = first;
        first = newNode;
    }

    public java.lang.Object pop()
    {
        if (first == null) {
            throw new java.util.NoSuchElementException();
        }
        java.lang.Object element = first.data;
        first = first.next;
        return element;
    }

    public boolean empty()
    {
        return first == null;
    }

    class Node
    {

        public java.lang.Object data;

        public stack.LinkedListStack.Node next;

    }

}
