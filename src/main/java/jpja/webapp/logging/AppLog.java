package jpja.webapp.logging;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppLog extends Log{
    private String thread;
    private String message;
    private StackTrace trace;
    private String notes;
    
    public AppLog(LocalDate date, LocalTime time, Level level, String source, String thread, String message,
            StackTrace trace, String notes) {
        super(date, time, level, source);
        this.thread = thread;
        this.message = message;
        this.trace = trace;
        this.notes = notes;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StackTrace getTrace() {
        return trace;
    }

    public void setTrace(StackTrace trace) {
        this.trace = trace;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    
}
