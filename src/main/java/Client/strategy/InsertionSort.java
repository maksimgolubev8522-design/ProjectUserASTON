package Client.strategy;

import java.util.Comparator;
import java.util.List;

public class InsertionSort extends SortStrategy {
    @Override
    public <T> List<T> sort(List<T> items, Comparator<T> comparator) {
        if (items == null) return null;
        List<T> list = new java.util.ArrayList<>(items);
        for (int i = 1; i < list.size(); i++) {
            T key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
        return list;
    }
}
