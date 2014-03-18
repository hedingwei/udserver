package com.ambimmort.rmr.client;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import java.util.HashSet;

public class ConsistentHash<T> {

    private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<Long, T> circle = new TreeMap<Long, T>();

    public SortedMap<Long, T> getCircle() {
        return circle;
    }

    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas,
            Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;

        for (T node : nodes) {
            add(node);
        }
    }

    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFunction.hashString(node.toString() + i, Charset.defaultCharset()).asLong(),
                    node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hashString(node.toString() + i, Charset.defaultCharset()).asLong());
        }
    }

    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        long hash = hashFunction.hashString(key.toString(), Charset.defaultCharset()).asLong();
        if (!circle.containsKey(hash)) {
            SortedMap<Long, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public static void main(String[] args) {
        HashSet<Integer> set = new HashSet<Integer>();
        int c1 = "5502:192.168.101:5502".hashCode();
        int c2 = "5502:192.168.102:5502".hashCode();
        int c3 = "5502:192.168.103:5502".hashCode();
        int c4 = "5502:192.168.104:5502".hashCode();
//        int c1 = 0;
//        int c2 = Integer.MAX_VALUE/2;
//        int c3 = -Integer.MAX_VALUE/2;
//        int c4 = Integer.MAX_VALUE;
        set.add(c1);
        set.add(c2);
        set.add(c3);
        set.add(c4);

        int c1_c = 0;
        int c2_c = 0;
        int c3_c = 0;
        int c4_c = 0;

        ConsistentHash<Integer> hash = new ConsistentHash<Integer>(Hashing.murmur3_128(), 4, set);
        HashFunction hf = Hashing.goodFastHash(16);
        for (int i = 0; i < 5000000; i++) {
//            String v = hash.get(hf.hashString("0898322" +i+ "_userid", Charset.defaultCharset()));
            int v = hash.get(hf.hashInt(("0898322" + i + "_userid").hashCode()));
            if (v == c1) {
                c1_c++;
            } else if (v == c2) {
                c2_c++;
            } else if (v == c3) {
                c3_c++;
            } else if (v == c4) {
                c4_c++;
            }
        }
        System.out.println(c1_c);
        System.out.println(c2_c);
        System.out.println(c3_c);
        System.out.println(c4_c);
    }
}
