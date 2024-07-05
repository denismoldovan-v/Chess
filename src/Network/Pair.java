package Network;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa obsługująca parę o dowolnych możliwych połączeniach.
 * @param <First> Typ pierwszej części pary.
 * @param <Second> Typ drugiej części pary.
 */
public class Pair<First, Second> implements Serializable {
    private First first;
    private Second second;

    /**
     * Pusty konstruktor. First = null, Second = null.
     */
    public Pair() {
        first = null;
        second = null;
    }

    /**
     * Konstruktor tworzący parę z początkowymi wartościami.
     * @param first Pierwsza część pary.
     * @param second Druga część pary.
     */
    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @return Pierwsza część pary.
     */
    public First getFirst() {
        return first;
    }

    /**
     * Ustawienie wartości pierwszej.
     * @param first Nowa pierwsza część pary.
     */
    public void setFirst(First first) {
        this.first = first;
    }

    /**
     * @return Druga część pary.
     */
    public Second getSecond() {
        return second;
    }

    /**
     * Ustawienie wartości pierwszej.
     * @param second Nowa druga część pary.
     */
    public void setSecond(Second second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "<"+first+","+second+">";
    }
}
