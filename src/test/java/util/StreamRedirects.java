package util;

import org.junit.rules.ExternalResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class StreamRedirects extends ExternalResource {

    private ByteArrayInputStream inContent;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Override
    protected void before() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Override
    protected void after() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    public void sendInput(String data) {
        inContent = new ByteArrayInputStream(data.getBytes());
        System.setIn(inContent);
    }

    public String getOutput() {
        return outContent.toString();
    }

    public String getError() {
        return errContent.toString();
    }

    public PrintStream getOriginalOut() {
        return originalOut;
    }

    public PrintStream getOriginalErr() {
        return originalErr;
    }
}
