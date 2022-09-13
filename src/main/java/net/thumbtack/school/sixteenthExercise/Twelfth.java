package net.thumbtack.school.sixteenthExercise;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//12. Написать свой класс, аналогичный ConcurrentHashMap, используя ReadWriteLock.
// Будет ли эта реализация хуже ConcurrentHashMap, и если да, то почему ?
// Ответ: Будет хуже. Как я понял, ConcurrentHashMap делит Map на части, за счёт чего
// есть возможность взаимодействовать с разными частями Map одновременно. У нас такого нет
// поэтому как минимум производительность будет хуже во многих случаях
public class Twelfth {
    static class MyConcurrentHashMap<K, V> implements ConcurrentMap<K, V> {
        private final Map<K, V> map = new HashMap<>();
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        @Override
        public int size() {
            lock.readLock().lock();
            try {
                return map.size();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean isEmpty() {
            lock.readLock().lock();
            try {
                return map.isEmpty();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean containsKey(Object key) {
            lock.readLock().lock();
            try {
                return map.containsKey(key);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public boolean containsValue(Object value) {
            lock.readLock().lock();
            try {
                return map.containsValue(value);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public V get(Object key) {
            lock.readLock().lock();
            try {
                return map.get(key);
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public V put(K key, V value) {
            lock.writeLock().lock();
            try {
                return map.put(key, value);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public V remove(Object key) {
            lock.writeLock().lock();
            try {
                return map.remove(key);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            lock.writeLock().lock();
            try {
                map.putAll(m);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void clear() {
            lock.writeLock().lock();
            try {
                map.clear();
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public Set<K> keySet() {
            lock.readLock().lock();
            try {
                return map.keySet();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public Collection<V> values() {
            lock.readLock().lock();
            try {
                return map.values();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public Set<Entry<K, V>> entrySet() {
            lock.readLock().lock();
            try {
                return map.entrySet();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public V putIfAbsent(K key, V value) {
            lock.writeLock().lock();
            try {
                return map.putIfAbsent(key, value);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public boolean remove(Object key, Object value) {
            lock.writeLock().lock();
            try {
                return map.remove(key, value);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            lock.writeLock().lock();
            try {
                return map.replace(key, oldValue, newValue);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public V replace(K key, V value) {
            lock.writeLock().lock();
            try {
                return map.replace(key, value);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
}
