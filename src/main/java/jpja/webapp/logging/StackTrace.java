package jpja.webapp.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackTrace {
    private String exception;
    private String message;
    private List<String> trace;
    private StackTrace causedBy;
    private int omittedLines;
    
    
    private static final Pattern EX_STACK_PATTERN = Pattern.compile("(?sm)("
                                    + "^"
                                    + "(?<mainException>[A-Za-z0-9_$.\\[\\]]+(?:Exception|Error|Throwable))"    // #1 Main exception
                                    + ": (?<message>[^\\r\\n]*)"                                                    // #2 message 
                                    + "(?<mainFrames>(\\r?\\n\\s*at\\s+[^\\r\\n]*)+)"                         // #3 Main stack frames
                                    + "(?<causedByChain>(?:\\r?\\n"                                             // #4 Repeat caused by
                                    + "^Caused by: .*)*)"
                                    + "(?<omittedCount>(?:\\r?\\n^\\s*\\.\\.\\.\\s*\\d+\\s+common frames omitted\\s*)?)"         // #5 "… N frames" line
                                    + ")");

    private static final Pattern OMITTED_PATTERN = Pattern.compile("(?:^\\s*\\.\\.\\.\\s*)(\\d+)(?:\\s+common frames omitted\\s*)");

    
    

    

    public StackTrace(String exception, String message, List<String> trace, StackTrace causedBy, int omittedLines) {
        this.exception = exception;
        this.message = message;
        this.trace = trace;
        this.causedBy = causedBy;
        this.omittedLines = omittedLines;
    }

    public StackTrace(String trace){
        parse(trace);
    }

    private void parse(String trace){
        Matcher matcher = EX_STACK_PATTERN.matcher(trace);
        if(matcher.find()){
            this.exception = matcher.group("mainException");
            this.message = matcher.group("message");
            this.trace = splitTrace(matcher.group("mainFrames"));
            this.causedBy = splitCausedBy(matcher.group("causedByChain"));
            String omittedString = matcher.group("omittedCount");
            this.omittedLines = getOmittedLinesFromString(omittedString);
        }else{
            throw new IllegalArgumentException("failed parsing trace");
        }
    }

    private int getOmittedLinesFromString(String omittedString){
        if(omittedString == null || omittedString.isEmpty()){
            return 0;
        }
        Matcher matcher = OMITTED_PATTERN.matcher(omittedString);
        if(matcher.find()){
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalArgumentException("Error parsing omitted string. String = " + omittedString);
    }

    private StackTrace splitCausedBy(String causedByBlock){
        if(causedByBlock == null || causedByBlock.isEmpty() || !causedByBlock.contains("\nCaused by: ")){
            return null;
        }
        String[] blocks = causedByBlock.split("\\r?\\nCaused by: ");
        StackTrace trace = null;
        for(int i = blocks.length-1; i > 0; i--){
            StackTrace temp = trace;
            trace = new StackTrace(blocks[i]);
            trace.setCausedBy(temp);
        }
        return trace;
    }
    /*
    private StackTrace parseCausedBy(String causedByBlock){
        if(causedByBlock == null || causedByBlock.isEmpty()){
            return null;
        }
        StackTrace head =  null;
        StackTrace ptr = null;
        Matcher causeMatcher = CAUSED_BY_PATTERN.matcher(causedByBlock);
        while (causeMatcher.find()) {
            String exception = causeMatcher.group("mainException");
            String message = causeMatcher.group("message");
            List<String> frames = splitTrace(causeMatcher.group("mainFrames"));
            StackTrace temp = new StackTrace(exception, message, frames, null);
            if(ptr == null || head == null){
                head = temp;
                ptr = temp;
            }else{
                ptr.setCausedBy(temp);
                ptr = temp;
            }
        }
        return head;
    }*/

    private List<String> splitTrace(String trace){
        List<String> ret = new ArrayList<String>();
        if(trace != null && !trace.isEmpty()){
            String[] lines = trace.split("\\r?\\n\\s*at\\s+");
            for(String line : lines){
                line = line.trim();
                if(!line.isEmpty()){
                    ret.add(line);
                }
            }
        }
        return ret;
    }
    /*
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
        */

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

    public int getOmittedLines() {
        return omittedLines;
    }

    public void setOmittedLines(int omittedLines) {
        this.omittedLines = omittedLines;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((exception == null) ? 0 : exception.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((trace == null) ? 0 : trace.hashCode());
        result = prime * result + ((causedBy == null) ? 0 : causedBy.hashCode());
        result = prime * result + omittedLines;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StackTrace other = (StackTrace) obj;
        if (exception == null) {
            if (other.exception != null)
                return false;
        } else if (!exception.equals(other.exception))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (trace == null) {
            if (other.trace != null)
                return false;
        } else if (!trace.equals(other.trace))
            return false;
        if (causedBy == null) {
            if (other.causedBy != null)
                return false;
        } else if (!causedBy.equals(other.causedBy))
            return false;
        if (omittedLines != other.omittedLines)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "StackTrace [exception={" + exception + "}\nmessage={" + message + "}\ntrace={" + trace + "}\n, causedBy={"
                + causedBy+ "}\n, omittedLines={" + omittedLines + "}]";
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
