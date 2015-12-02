package com.gunaatita.demo.chatserver.common;

public class Pair<K, V> {

    private K key;
    private V val;

    public Pair(K k, V v) {
        key = k;
        val = v;
    }

    public K getKey() {
        return key;
    }

    public V getVal() {
        return val;
    }

}
