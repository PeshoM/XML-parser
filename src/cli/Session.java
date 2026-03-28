package cli;

import model.XmlDocument;

import java.nio.file.Path;

public class Session {
    private XmlDocument doc;
    private Path path;
    private boolean modified;

    public boolean hasOpenDoc() { return doc != null; }

    public void open(Path p, XmlDocument doc) {
        this.path = p;
        this.doc = doc;
        this.modified = false;
    }

    public void close() {
        this.path = null;
        this.doc = null;
        this.modified = false;
    }

    public XmlDocument getDoc() { return doc; }
    public Path getPath() { return path; }
    public boolean isModified() { return modified; }
    public void markModified() { this.modified = true; }
    public void clearModified() { this.modified = false; }

    public void requireOpen() {
        if (!hasOpenDoc()) throw new IllegalStateException("No file is currently open.");
    }
}
