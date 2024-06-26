package apple.utilities.structures;

import java.util.Objects;

public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("<%s,%s>", key == null ? "null" : key.toString(), value == null ? "null" : value.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?> p) {
            return p.getKey().equals(key) && p.getValue().equals(value);
        }
        return false;
    }
}
