package fastscrape;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FastScrapeTest {

    private static FastScrape scraper;

    @BeforeAll
    public static void setup() {
        scraper = FastScrape.open();
    }

    @Test
    public void testExtractReadableText() {
        String html = "<html><head><style>body{color:#000;}</style></head><body><h1>Hello World</h1><p>Welcome to <b>FastJava</b>.</p><script>console.log('test');</script></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);

        String text = scraper.extractReadableText(bytes);
        assertNotNull(text);
        assertTrue(text.contains("Hello World"));
        assertTrue(text.contains("Welcome to FastJava."));
        // Ensure script and style were stripped
        assertFalse(text.contains("console.log"));
        assertFalse(text.contains("color"));
    }

    @Test
    public void testExtractLinks() {
        String html = "<html><body><a href=\"https://google.com\">Google</a><a href='https://github.com/andrestubbe'>GitHub</a><a href=http://example.com>Example</a></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);

        List<String> links = scraper.extractLinks(bytes);
        assertEquals(3, links.size());
        assertTrue(links.contains("https://google.com"));
        assertTrue(links.contains("https://github.com/andrestubbe"));
        assertTrue(links.contains("http://example.com"));
    }

    @Test
    public void testExtractByTag() {
        String html = "<html><body><h1>Title 1</h1><h2>Subtitle</h2><h1>Title 2</h1></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);

        List<String> h1s = scraper.extractByTag(bytes, "h1");
        assertEquals(2, h1s.size());
        assertEquals("Title 1", h1s.get(0).trim());
        assertEquals("Title 2", h1s.get(1).trim());

        List<String> h2s = scraper.extractByTag(bytes, "h2");
        assertEquals(1, h2s.size());
        assertEquals("Subtitle", h2s.get(0).trim());
    }

    @Test
    public void testExtractJsonLD() {
        String html = "<html><head><script type=\"application/ld+json\">{\"name\":\"FastScrape\"}</script></head></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);

        String json = scraper.extractJsonLD(bytes);
        assertNotNull(json);
        assertTrue(json.contains("{\"name\":\"FastScrape\"}"));
    }

    @Test
    public void testEmptyInput() {
        String text = scraper.extractReadableText(new byte[0]);
        assertEquals("", text);

        List<String> links = scraper.extractLinks(new byte[0]);
        assertTrue(links.isEmpty());

        List<String> tags = scraper.extractByTag(new byte[0], "p");
        assertTrue(tags.isEmpty());

        String json = scraper.extractJsonLD(new byte[0]);
        assertEquals("", json);
    }
}
