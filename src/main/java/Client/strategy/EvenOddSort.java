package Client.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public class EvenOddSort extends SortStrategy {
    @Override
    public <T> List<T> sort(List<T> items, Comparator<T> comparator) {
        if (items == null || items.isEmpty()) return new java.util.ArrayList<>();

        List<T> list = new java.util.ArrayList<>(items);
        int n = list.size();
        boolean sorted = false;

        while (!sorted) {
            sorted = true;

            for (int i = 0; i <= n - 2; i += 2) {
                if (comparator.compare(list.get(i), list.get(i + 1)) > 0) {
                    java.util.Collections.swap(list, i, i + 1);
                    sorted = false;
                }
            }

            for (int i = 1; i <= n - 2; i += 2) {
                if (comparator.compare(list.get(i), list.get(i + 1)) > 0) {
                    java.util.Collections.swap(list, i, i + 1);
                    sorted = false;
                }
            }
        }

        return list;
    }

    public <T> List<T> sortWithEvenOdd(List<T> items, Comparator<T> comparator,
                                       ToIntFunction<T> numericFieldExtractor) {
        return sortWithEvenOddCondition(items, comparator, numericFieldExtractor);
    }
}