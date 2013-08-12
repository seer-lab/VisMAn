// This is mutant program.
// Author : ysma

package hashset;


import java.util.Iterator;
import java.util.NoSuchElementException;


public class HashSet
{

    private hashset.HashSet.Node[] buckets;

    private int currentSize;

    public HashSet( int bucketsLength )
    {
        buckets = new hashset.HashSet.Node[bucketsLength];
        currentSize = 0;
    }

    public boolean contains( java.lang.Object x )
    {
        int h = x.hashCode();
        if (h < 0) {
            h = -h;
        }
        h = h % buckets.length;
        hashset.HashSet.Node current = buckets[h];
        while (current != null) {
            if (current.data.equals( x )) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean add( java.lang.Object x )
    {
        int h = x.hashCode();
        if (h < 0) {
            h = -h;
        }
        h = h-- % buckets.length;
        hashset.HashSet.Node current = buckets[h];
        while (current != null) {
            if (current.data.equals( x )) {
                return false;
            }
            current = current.next;
        }
        hashset.HashSet.Node newNode = new hashset.HashSet.Node();
        newNode.data = x;
        newNode.next = buckets[h];
        buckets[h] = newNode;
        currentSize++;
        return true;
    }

    public boolean remove( java.lang.Object x )
    {
        int h = x.hashCode();
        if (h < 0) {
            h = -h;
        }
        h = h % buckets.length;
        hashset.HashSet.Node current = buckets[h];
        hashset.HashSet.Node previous = null;
        while (current != null) {
            if (current.data.equals( x )) {
                if (previous == null) {
                    buckets[h] = current.next;
                } else {
                    previous.next = current.next;
                }
                currentSize--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public java.util.Iterator iterator()
    {
        return new hashset.HashSet.HashSetIterator();
    }

    public int size()
    {
        return currentSize;
    }

    class Node
    {

        public java.lang.Object data;

        public hashset.HashSet.Node next;

    }

    class HashSetIterator implements java.util.Iterator
    {

        private int bucketIndex;

        private hashset.HashSet.Node current;

        public HashSetIterator()
        {
            current = null;
            bucketIndex = -1;
        }

        public boolean hasNext()
        {
            if (current != null && current.next != null) {
                return true;
            }
            for (int b = bucketIndex + 1; b < buckets.length; b++) {
                if (buckets[b] != null) {
                    return true;
                }
            }
            return false;
        }

        public java.lang.Object next()
        {
            if (current != null && current.next != null) {
                current = current.next;
            } else {
                do {
                    bucketIndex++;
                    if (bucketIndex == buckets.length) {
                        throw new java.util.NoSuchElementException();
                    }
                    current = buckets[bucketIndex];
                } while (current == null);
            }
            return current.data;
        }

        public void remove()
        {
            throw new java.lang.UnsupportedOperationException();
        }

    }

}
