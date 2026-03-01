package Client.strategy;

import java.util.Comparator;
import java.util.List;

public abstract class SortStrategy {
    abstract <T> List<T> sort(List<T> items, Comparator<T> comparator);
}
