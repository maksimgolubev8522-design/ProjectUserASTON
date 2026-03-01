package Client.strategy;

import java.util.Comparator;
import java.util.List;

public class BubbleSort extends SortStrategy {
    @Override
    public <T> List<T> sort(List<T> items, Comparator<T> comparator) {
        if (items == null) return null;
        int n = items.size();
        List<T> list = new java.util.ArrayList<>(items);
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    java.util.Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return list;
    }
}