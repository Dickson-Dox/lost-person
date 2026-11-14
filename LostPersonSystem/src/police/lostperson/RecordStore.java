package police.lostperson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple in-memory store for saved records. Records live as long as the program
 * runs (they survive logout/login) and are lost on exit. Replace the internals
 * with a database or file later without touching the windows.
 */
public final class RecordStore {
    private static final List<Person> RECORDS = new ArrayList<>();
    private static int nextId = 1;

    private RecordStore() { }

    /** Returns the next case number, e.g. LP-001. */
    static String nextId() {
        return String.format("LP-%03d", nextId++);
    }

    static void add(Person person) {
        RECORDS.add(person);
    }

    /** Read-only live view of all saved records, oldest first. */
    static List<Person> getAll() {
        return Collections.unmodifiableList(RECORDS);
    }

    static int count() {
        return RECORDS.size();
    }
}
