package cli;

import java.nio.file.Path;

public class Session {
    private String rawContent;
    private Path path;
    private boolean modified;

    public boolean hasOpenDoc() { return rawContent != null; }

    public void open(Path p, String content) {
        this.path = p;
        this.rawContent = content;
        this.modified = false;
    }

    public void close() {
        this.path = null;
        this.rawContent = null;
        this.modified = false;
    }

    public Path getPath() { return path; }
    public String getRawContent() { return rawContent; }
    public void setRawContent(String c) { this.rawContent = c; this.modified = true; }
    public boolean isModified() { return modified; }
    public void clearModified() { this.modified = false; }

    public void requireOpen() {
        if (!hasOpenDoc()) throw new IllegalStateException("No file is currently open.");
    }
}
