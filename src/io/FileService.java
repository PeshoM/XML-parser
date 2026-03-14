package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {
    public String readAll(Path p) throws IOException {
        return Files.readString(p);
    }

    public void writeAll(Path p, String content) throws IOException {
        Files.writeString(p, content);
    }

    public boolean exists(Path p) {
        return Files.exists(p);
    }
}
