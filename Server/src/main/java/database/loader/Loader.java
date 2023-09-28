package database.loader;

import java.io.File;

public interface Loader<T> {
    T load(String ID);
    File getFile(String ID);
}
