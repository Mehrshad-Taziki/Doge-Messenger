package database.saver;

public interface Saver<T> {
    void save(T t);
}
