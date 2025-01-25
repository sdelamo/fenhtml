package fenhtml;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FenhtmlCommandTest {
    private static final Logger LOG = LoggerFactory.getLogger(FenhtmlCommandTest.class);
    @Test
    void testWithCommandLineOption() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { "-f=3k4/8/8/8/8/8/3P4/3K4 w - - 0 1" };
            PicocliRunner.run(FenhtmlCommand.class, ctx, args);
            String html = baos.toString();
            assertTrue(html.contains("fen: '3k4/8/8/8/8/8/3P4/3K4 w - - 0 1',"));
        }
    }
}
