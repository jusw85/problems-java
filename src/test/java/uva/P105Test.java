package uva;

import com.google.common.io.Resources;
import jw.problems.uva.p105.Main;
import org.junit.ClassRule;
import org.junit.Test;
import util.StreamRedirects;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class P105Test {

    @ClassRule
    public static final StreamRedirects stream = new StreamRedirects();

    @Test
    public void single() throws IOException {
        String testInput = getResourceAsString("/uva/p105/1");
        stream.sendInput(testInput);
        Main.main(new String[0]);
//        stream.getOriginalOut().println(stream.getOutput());
        assertEquals("1 11 3 13 9 0 12 7 16 3 19 18 22 3 23 13 29 0", stream.getOutput().trim());
    }

    private String getResourceAsString(String path) throws IOException {
        URL resource = Resources.getResource(getClass(), path);
        return Resources.toString(resource, StandardCharsets.UTF_8);
    }

}
