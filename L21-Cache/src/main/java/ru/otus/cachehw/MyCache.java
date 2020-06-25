package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    Map<K, V> cache = new WeakHashMap<>();
    List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.putIfAbsent(key, value);
        listeners.forEach(listener -> listener.notify(key, value, "was added to cache"));
    }

    @Override
    public void remove(K key) {
        V removedValue = cache.remove(key);
        listeners.forEach(listener -> listener.notify(key, removedValue, "was removed from cache"));
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        listeners.forEach(listener -> listener.notify(key, value, "was received from cache"));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
