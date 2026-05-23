package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Чете и пише цели файлове. */
public class FileService {
    /** Чете целия файл като низ. */
    public String readAll(Path p) throws IOException {
        return Files.readString(p);
    }

    /** Записва низа в файла. */
    public void writeAll(Path p, String content) throws IOException {
        Files.writeString(p, content);
    }

    /** Връща дали файлът съществува. */
    public boolean exists(Path p) {
        return Files.exists(p);
    }
}
