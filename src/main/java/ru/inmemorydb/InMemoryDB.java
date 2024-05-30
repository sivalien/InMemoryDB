package ru.inmemorydb;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class InMemoryDB {
    private List<Data> sortedByAccount;
    private List<List<Data>> sortedByName;
    private List<List<Data>> sortedByValue;

    {
        sortedByAccount = new ArrayList<>();
        sortedByName = new ArrayList<>();
        sortedByValue = new ArrayList<>();
    }

    private static <T extends Comparable<T>, S> int binarySearch(T value, List<S> list, Function<S, T> function) {
        int left = -1, right = list.size();
        int mid;
        while (right - left > 1) {
            mid = (right + left) / 2;
            if (function.apply(list.get(mid)).compareTo(value) < 0) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return right;
    }

    private static <T extends Comparable<T>> void addData(T value, Data data, List<List<Data>> list, Function<List<Data>, T> function) {
        int index = binarySearch(value, list, function);
        if (index == list.size() || !function.apply(list.get(index)).equals(value)) {
            list.add(index, new ArrayList<>());
        }
        list.get(index).add(data);
    }


    public boolean add(Data data) {
        int index = binarySearch(data.getAccount(), sortedByAccount, Data::getAccount);
        if (index < sortedByAccount.size() && sortedByAccount.get(index).getAccount() == data.getAccount()) {
            return false;
        }
        sortedByAccount.add(index, data);

        addData(data.getName(), data, sortedByName, x -> x.get(0).getName());

        addData(data.getValue(), data, sortedByValue, x -> x.get(0).getValue());

        return true;
    }

    public Data findByAccount(long account) {
        int index = binarySearch(account, sortedByAccount, Data::getAccount);
        if (index == sortedByAccount.size() || sortedByAccount.get(index).getAccount() != account) {
            return null;
        }
        return sortedByAccount.get(index);
    }

    public List<Data> findByName(String name) {
        int index = binarySearch(name, sortedByName, x -> x.get(0).getName());
        if (index == sortedByName.size() || !name.equals(sortedByName.get(index).get(0).getName())) {
            return new ArrayList<>();
        }
        return sortedByName.get(index);
    }

    public List<Data> findByValue(double value) {
        int index = binarySearch(value, sortedByValue, x -> x.get(0).getValue());
        if (index == sortedByValue.size() || sortedByValue.get(index).get(0).getValue() != value) {
            return new ArrayList<>();
        }
        return sortedByValue.get(index);
    }

    private static void removeInValueList(List<List<Data>> list, Predicate<Data> predicate) {
        int i = 0;
        while (i < list.size()) {
            list.get(i).removeIf(predicate);
            if (list.get(i).size() == 0) {
                list.remove(i);
            } else {
                i++;
            }
        }
    }

    public boolean remove(Predicate<Data> predicate) {
        removeInValueList(sortedByName, predicate);
        removeInValueList(sortedByValue, predicate);
        return sortedByAccount.removeIf(predicate);
    }

    public void update(Predicate<Data> predicate, Consumer<Data> consumer) {
        List<Data> dataToChange = new ArrayList<>();
        for (Data data : sortedByAccount) {
            if (predicate.test(data)) {
                dataToChange.add(data);
            }
        }
        remove(predicate);
        for (Data data : dataToChange) {
            consumer.accept(data);
            add(data);
        }
    }
}
