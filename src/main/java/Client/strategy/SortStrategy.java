package Client.strategy;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public abstract class SortStrategy {
    public abstract <T> List<T> sort(List<T> items, Comparator<T> comparator);

    public <T> List<T> sortWithEvenOddCondition(List<T> items, Comparator<T> comparator,
                                                ToIntFunction<T> numericFieldExtractor) {
        if (items == null || items.isEmpty()) return new java.util.ArrayList<>();

        List<T> result = new java.util.ArrayList<>(items);
        List<Integer> evenIndices = new java.util.ArrayList<>();
        List<T> evenValues = new java.util.ArrayList<>();

        // Собираем элементы с четными значениями
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            if (numericFieldExtractor.applyAsInt(item) % 2 == 0) {
                evenIndices.add(i);
                evenValues.add(item);
            }
        }

        evenValues.sort(comparator);

        for (int j = 0; j < evenIndices.size(); j++) {
            result.set(evenIndices.get(j), evenValues.get(j));
        }

        return result;
    }
}