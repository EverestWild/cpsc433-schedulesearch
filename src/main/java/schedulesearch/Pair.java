// =============================================================================
//  Pair
// -----------------------------------------------------------------------------
//  Source code mostly copied from the javafx project:
//  http://hg.openjdk.java.net/openjfx/8/master/rt/file/f89b7dc932af/modules/base/src/main/java/javafx/util/Pair.java
// =============================================================================

package schedulesearch;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        else if (o != null && o instanceof Pair) {
            Pair pair = (Pair) o;
            return (key == pair.key) && (value == pair.value);
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
    }

}
