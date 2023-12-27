package by.andd3dfx.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.Collections;
import java.util.List;

/**
 * Class for testing purposes to catch logs.
 */
public class MemoryAppender extends ListAppender<ILoggingEvent> {
    public void reset() {
        list.clear();
    }

    public boolean contains(String string, Level level) {
        return list.stream()
                .anyMatch(event -> event.getMessage().toString().contains(string)
                        && event.getLevel().equals(level));
    }

    public int countEventsForLogger(String loggerName) {
        return (int) list.stream()
                .filter(event -> event.getLoggerName().contains(loggerName))
                .count();
    }

    public List<ILoggingEvent> search(String string) {
        return list.stream()
                .filter(event -> event.getMessage().toString().contains(string))
                .toList();
    }

    public List<ILoggingEvent> search(String string, Level level) {
        return list.stream()
                .filter(event -> event.getMessage().toString().contains(string)
                        && event.getLevel().equals(level))
                .toList();
    }

    public int getSize() {
        return list.size();
    }

    public List<ILoggingEvent> getLoggedEvents() {
        return Collections.unmodifiableList(list);
    }
}
