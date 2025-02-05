package fenhtml;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.views.ViewsRenderer;
import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Command(name = "fenhtml", description = "Generate a HTML snippet for a FEN string",
        mixinStandardHelpOptions = true)
public class FenhtmlCommand implements Runnable {

    private static final String LICHESS_ORG = "https://lichess.org/";
    @Option(names = {"-f", "--fen"}, required = true)
    String fen;

    @Option(names = {"-t", "--title"}, required = true)
    String title;

    @Option(names = {"-d", "--description"}, required = true)
    String description;

    @Inject
    ViewsRenderer<Map<String, Object>, ?> viewsRenderer;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(FenhtmlCommand.class, args);
    }

    public void run() {

        String id = sanitizeForHtmlId(fen);

        String analysis  = UriBuilder.of(LICHESS_ORG).path("analysis").path("standard").build().toString()
                + "/" + fen.replace(" ", "_");
        URI uri = UriBuilder.of(LICHESS_ORG).queryParam("fen", fen).fragment("friend").build();
        String friend = uri.toString();
        uri = UriBuilder.of(LICHESS_ORG).queryParam("fen", fen).fragment("ai").build();
        String ai = uri.toString();

        Map<String, Object> model = Map.of("fen", fen,
                "friend", friend,
                "ai", ai,
                "id", id,
                "analysis", analysis,
                "title", title,
                "description", description);
        Writable writable = viewsRenderer.render("index.html", model, null);
        Optional<String> htmlOptional = WritableUtils.writableToString(writable);
        htmlOptional.ifPresent(System.out::println);

    }

    public static String sanitizeForHtmlId(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Replace invalid characters with an underscore
        String sanitized = input.replaceAll("[^a-zA-Z0-9-_:.]", "_");

        // Ensure the first character is a letter
        if (!Character.isLetter(sanitized.charAt(0))) {
            sanitized = "id_" + sanitized;
        }

        return sanitized;
    }

    public static String urlEncode(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }
}
