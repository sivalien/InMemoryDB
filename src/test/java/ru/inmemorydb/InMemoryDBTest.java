package ru.inmemorydb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InMemoryDBTest {

    @Test
    public void testAdd() {
        InMemoryDB db = new InMemoryDB();
        Data data = new Data(12345, "Alice", 100.0);

        assertTrue(db.add(data));
        assertFalse(db.add(data));
        assertFalse(db.add(new Data(12345, "Bob", 95.5)));
        assertTrue(db.add(new Data(13345, "Bob", 95.5)));
    }

    @Test
    public void testFindByAccount() {
        InMemoryDB db = new InMemoryDB();
        Data data1 = new Data(12345, "Alice", 100.0);
        Data data2 = new Data(13345, "Bob", 95.5);

        assertNull(db.findByAccount(12345));
        assertNull(db.findByAccount(13345));

        db.add(data1);
        assertEquals(data1, db.findByAccount(12345));
        assertNull(db.findByAccount(13345));

        db.add(data2);
        assertEquals(data1, db.findByAccount(12345));
        assertEquals(data2, db.findByAccount(13345));
    }

    @Test
    public void testFindByName() {
        InMemoryDB db = new InMemoryDB();
        Data data1 = new Data(12345, "Alice", 100.0);
        Data data2 = new Data(54321, "Bob", 200.0);
        Data data3 = new Data(11111, "Alice", 95.5);

        db.add(data1);
        db.add(data2);
        db.add(data3);

        List<Data> result1 = db.findByName("Alice");
        assertEquals(2, result1.size());
        assertTrue(result1.contains(data1));
        assertTrue(result1.contains(data3));

        List<Data> result2 = db.findByName("Bob");
        assertEquals(1, result2.size());
        assertTrue(result2.contains(data2));

        assertEquals(0, db.findByName("Charlie").size());
    }

    @Test
    public void testFindByValue() {
        InMemoryDB db = new InMemoryDB();
        Data data1 = new Data(12345, "Alice", 100.0);
        Data data2 = new Data(54321, "Bob", 100.0);
        Data data3 = new Data(11111, "Charlie", 95.5);

        db.add(data1);
        db.add(data2);
        db.add(data3);

        List<Data> result1 = db.findByValue(100.0);
        assertEquals(2, result1.size());
        assertTrue(result1.contains(data1));
        assertTrue(result1.contains(data2));

        List<Data> result2 = db.findByValue(95.5);
        assertEquals(1, result2.size());
        assertTrue(result2.contains(data3));

        assertEquals(0, db.findByValue(96).size());
    }


    @Test
    public void testRemove() {
        InMemoryDB db = new InMemoryDB();
        Data data1 = new Data(12345, "Alice", 100.0);
        Data data2 = new Data(54321, "Bob", 200.0);

        db.add(data1);
        db.add(data2);

        assertFalse(db.remove(d -> d.getName().equals("Charlie")));

        assertTrue(db.remove(d -> d.getName().equals("Bob")));
        assertEquals(0, db.findByName("Bob").size());
        assertEquals(data1, db.findByAccount(12345));
    }

    @Test
    public void testUpdate() {
        InMemoryDB db = new InMemoryDB();
        Data data1 = new Data(12345, "Alice", 100.0);
        Data data2 = new Data(54321, "Bob", 200.0);

        db.add(data1);
        db.add(data2);

        Predicate<Data> predicate = d -> d.getValue() > 150.0;
        Consumer<Data> consumer = d -> d.setName("Updated");

        db.update(predicate, consumer);

        assertEquals("Alice", db.findByAccount(12345).getName());
        assertEquals("Updated", db.findByAccount(54321).getName());
    }
}
