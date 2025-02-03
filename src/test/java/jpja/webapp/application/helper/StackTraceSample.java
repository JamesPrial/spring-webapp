package jpja.webapp.application.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import jpja.webapp.logging.StackTrace;

public class StackTraceSample {
    private static final String SAMPLES = "/sample-exceptions/samples/";
    private static final String EXPECTED = "/sample-exceptions/expected-outputs/";
    private static final String SAMPLE_SUFFIX = ".log";

    private String sample;
    private StackTrace expected;
    private StackTraceSample next;

   

    private StackTraceSample(File sample, File expected, StackTraceSample next) throws IOException {
        setSampleFromFile(sample);
        setExpectedFromFile(expected);
        this.next = next;
    }

    //sampleFiles = n*2 array - sampleFiles[x][0] = sample, sampleFiles[x][1] = expected
    private StackTraceSample(LinkedList<File> samples, LinkedList<File> expected) throws IOException{
        if(samples.size() > 1){
            setSampleFromFile(samples.pop());
            setExpectedFromFile(expected.pop());
            this.next = new StackTraceSample(samples, expected);
            
        }else if(samples.size() == 1){
            setSampleFromFile(samples.pop());
            setExpectedFromFile(expected.pop());
            this.next = null;
        }else{
            throw new IllegalAccessError();
        }

    }

    private void setSampleFromFile(File file) throws IOException{
        this.sample = Files.readString(file.toPath());
    }

    private void setExpectedFromFile(File file){
        this.expected = null;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            this.expected = readExpected(br);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    private StackTrace readExpected(BufferedReader br) throws IOException{
        String exception = br.readLine().trim();
        br.readLine();
        String message = br.readLine().trim();
        br.readLine();
        List<String> trace = new ArrayList<String>();
        String line = br.readLine();
        while(line != null && !line.isEmpty() && !line.equals("*****")){
            trace.add(line.trim());
            line  = br.readLine();
        }
        String omitted = br.readLine();
        if(br.readLine() == null){
            return new StackTrace(exception, message, trace, null, Integer.parseInt(omitted));
        }
        return new StackTrace(exception, message, trace, readExpected(br), Integer.parseInt(omitted));
    }

    public static StackTraceSample makeStackTraceSample(String sampleName) throws IOException {
        try {
            if (sampleName == null || sampleName.isEmpty()) {
                File sampleFolder = getSamplePath(null).toFile();
                File expectedFolder = getExpectedPath(null).toFile();
                LinkedList<File> samples = new LinkedList<File>(Arrays.asList(sampleFolder.listFiles()));
                LinkedList<File> expecteds = new LinkedList<File>(Arrays.asList(expectedFolder.listFiles()));
                if(samples.size() != expecteds.size()){
                    throw new IllegalArgumentException("samples.size() != expecteds.size()");
                }
                return new StackTraceSample(samples, expecteds);
            }else{
                File sample = getSamplePath(sampleName).toFile();
                File expected = getExpectedPath(sampleName).toFile();
                return new StackTraceSample(sample, expected, null);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Path getSamplePath(String sampleName) throws URISyntaxException {
        String pathAsString = sampleName == null || sampleName.isEmpty() ? SAMPLES
                : "" + SAMPLES + sampleName + SAMPLE_SUFFIX;
        return Path.of(StackTraceSample.class.getResource(pathAsString).toURI());
    }

    private static Path getExpectedPath(String sampleName) throws URISyntaxException {
        String pathAsString = sampleName == null || sampleName.isEmpty() ? EXPECTED : "" + EXPECTED + sampleName;
        return Path.of(StackTraceSample.class.getResource(pathAsString).toURI());
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public StackTrace getExpected() {
        return expected;
    }

    public void setExpected(StackTrace expected) {
        this.expected = expected;
    }

    public StackTraceSample getNext() {
        return next;
    }

    public void setNext(StackTraceSample next) {
        this.next = next;
    }

    
}
