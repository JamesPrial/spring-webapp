package jpja.webapp.logging;

import java.util.ArrayList;
import java.util.List;

public class StackTrace {
    private String exception;
    private String message;
    private List<String> trace;
    private StackTrace causedBy;
    private int linesOmitted;
    
    

    public StackTrace(String exception, String message, List<String> trace, StackTrace causedBy, int linesOmitted) {
        this.exception = exception;
        this.message = message;
        this.trace = trace;
        this.causedBy = causedBy;
        this.linesOmitted = linesOmitted;
    }

    public StackTrace(String trace){
        parseTrace(trace);
    }



    private void parseTrace(String trace){
        int idxOfColon = trace.indexOf(":");
        this.exception = trace.substring(0, idxOfColon).trim();
        int idxOfFirstNewLine = trace.indexOf("\n");
        this.message = trace.substring(idxOfColon, idxOfFirstNewLine).trim();
        trace = trace.substring(idxOfFirstNewLine++);
        int causedByIdx = trace.indexOf("Caused by:");
        if(causedByIdx > -1){
            this.causedBy = new StackTrace(trace.substring(causedByIdx+"Caused by: ".length()));
            trace = trace.substring(0, causedByIdx);
        }else{
            this.causedBy = null;
        }
        String[] lines = trace.split("\n");
        List<String> newTrace = new ArrayList<String>();
        for(String line : lines){
            if(line.contains("\\s+at\\s+")){
                newTrace.add(line.trim().substring("at ".length()).trim());
            }else if(line.contains("...")){
                String omitted = (line.split(" "))[1];
                linesOmitted = Integer.parseInt(omitted);
                break;
            }
        }
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getTrace() {
        return trace;
    }

    public void setTrace(List<String> trace) {
        this.trace = trace;
    }

    public StackTrace getCausedBy() {
        return causedBy;
    }

    public void setCausedBy(StackTrace causedBy) {
        this.causedBy = causedBy;
    }

    public int getLinesOmitted() {
        return linesOmitted;
    }

    public void setLinesOmitted(int linesOmitted) {
        this.linesOmitted = linesOmitted;
    }

    /*
    private List<Integer> getIndicesOfNewLines(String trace){
        List<Integer> ret = new ArrayList<Integer>();
        int discardedChars = 0;
        int idx = trace.indexOf("\n");
        while(idx != -1 && idx+1 >= trace.length()){
            discardedChars = discardedChars + idx;
            ret.add(discardedChars+1);
            trace = trace.substring(discardedChars+1);
        }
        return ret;
    }
    */

    
}
