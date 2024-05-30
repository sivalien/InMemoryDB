package ru.inmemorydb;

import java.util.Objects;

public class Data {
    private long account;
    private String name;
    private double value;

    public Data(long account, String name, double value) {
        this.account = account;
        this.name = name;
        this.value = value;
    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Data data = (Data) o;
        return account == data.account && Double.compare(data.value, value) == 0 && name.equals(data.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, name, value);
    }
}
