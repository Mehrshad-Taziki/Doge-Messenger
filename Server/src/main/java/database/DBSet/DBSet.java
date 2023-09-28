package database.DBSet;

import java.util.LinkedList;

public interface DBSet<T> {

    T get(String id);

    LinkedList<T> getAll();

}
