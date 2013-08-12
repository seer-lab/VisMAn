// This is mutant program.
// Author : ysma

package linkedlist;


import java.util.NoSuchElementException;


public class LinkedList
{

    private linkedlist.LinkedList.Node first;

    private linkedlist.LinkedList.Node last;

    public LinkedList()
    {
        first = null;
        last = null;
    }

    public java.lang.Object getFirst()
    {
        if (first == null) {
            throw new java.util.NoSuchElementException();
        }
        return first.data;
    }

    public java.lang.Object removeFirst()
    {
        if (first == null) {
            throw new java.util.NoSuchElementException();
        }
        java.lang.Object element = first.data;
        first = first.next;
        if (first == null) {
            last = null;
        } else {
            first.previous = null;
        }
        return element;
    }

    public void addFirst( java.lang.Object element )
    {
        linkedlist.LinkedList.Node newNode = new linkedlist.LinkedList.Node();
        newNode.data = element;
        newNode.next = first;
        newNode.previous = null;
        if (first == null) {
            last = newNode;
        } else {
            first.previous = newNode;
        }
        first = newNode;
    }

    public java.lang.Object getLast()
    {
        if (last == null) {
            throw new java.util.NoSuchElementException();
        }
        return last.data;
    }

    public java.lang.Object removeLast()
    {
        if (last == null) {
            throw new java.util.NoSuchElementException();
        }
        java.lang.Object element = last.data;
        last = first;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        return element;
    }

    public void addLast( java.lang.Object element )
    {
        linkedlist.LinkedList.Node newNode = new linkedlist.LinkedList.Node();
        newNode.data = element;
        newNode.next = null;
        newNode.previous = last;
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
    }

    public linkedlist.ListIterator listIterator()
    {
        return new linkedlist.LinkedList.LinkedListIterator();
    }

    class Node
    {

        public java.lang.Object data;

        public linkedlist.LinkedList.Node next;

        public linkedlist.LinkedList.Node previous;

    }

    class LinkedListIterator implements linkedlist.ListIterator
    {

        private linkedlist.LinkedList.Node position;

        private boolean isAfterNext;

        private boolean isAfterPrevious;

        public LinkedListIterator()
        {
            position = null;
            isAfterNext = false;
            isAfterPrevious = false;
        }

        public java.lang.Object next()
        {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            isAfterNext = true;
            isAfterPrevious = false;
            if (position == null) {
                position = first;
            } else {
                position = position.next;
            }
            return position.data;
        }

        public boolean hasNext()
        {
            if (position == null) {
                return first != null;
            } else {
                return position.next != null;
            }
        }

        public java.lang.Object previous()
        {
            if (!hasPrevious()) {
                throw new java.util.NoSuchElementException();
            }
            isAfterNext = false;
            isAfterPrevious = true;
            java.lang.Object result = position.data;
            position = position.previous;
            return result;
        }

        public boolean hasPrevious()
        {
            return position != null;
        }

        public void add( java.lang.Object element )
        {
            if (position == null) {
                addFirst( element );
                position = first;
            } else {
                if (position == last) {
                    addLast( element );
                    position = last;
                } else {
                    linkedlist.LinkedList.Node newNode = new linkedlist.LinkedList.Node();
                    newNode.data = element;
                    newNode.next = position.next;
                    newNode.next.previous = newNode;
                    position.next = newNode;
                    newNode.previous = position;
                    position = newNode;
                }
            }
            isAfterNext = false;
            isAfterPrevious = false;
        }

        public void remove()
        {
            linkedlist.LinkedList.Node positionToRemove = lastPosition();
            if (positionToRemove == first) {
                removeFirst();
            } else {
                if (positionToRemove == last) {
                    removeLast();
                } else {
                    positionToRemove.previous.next = positionToRemove.next;
                    positionToRemove.next.previous = positionToRemove.previous;
                }
            }
            if (isAfterNext) {
                position = position.previous;
            }
            isAfterNext = false;
            isAfterPrevious = false;
        }

        public void set( java.lang.Object element )
        {
            linkedlist.LinkedList.Node positionToSet = lastPosition();
            positionToSet.data = element;
        }

        private linkedlist.LinkedList.Node lastPosition()
        {
            if (isAfterNext) {
                return position;
            } else {
                if (isAfterPrevious) {
                    if (position == null) {
                        return first;
                    } else {
                        return position.next;
                    }
                } else {
                    throw new java.lang.IllegalStateException();
                }
            }
        }

    }

}
