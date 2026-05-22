package fastscrape;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Hero Demo demonstrating FastScrape SIMD capabilities.
 */
public class Demo {

    private Demo() {
        // Utility class
    }

    /**
     * Main entry point for FastScrape demo.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("\u001B[35m====================================================================\u001B[0m");
        System.out.println("\u001B[36;1m⚡ FastScrape SIMD-AVX2 Engine — Hero Demo ⚡\u001B[0m");
        System.out.println("\u001B[35m====================================================================\u001B[0m");

        // Mock HTML representing a standard complex web page with script, style, links, tags, and JSON-LD
        String mockHtml = "<html>\n" +
                "<head>\n" +
                "  <title>FastScrape — High-Performance JNI scraper</title>\n" +
                "  <style>\n" +
                "    body { font-family: sans-serif; background: #121212; color: #fff; }\n" +
                "    h1 { color: #00ffcc; }\n" +
                "  </style>\n" +
                "  <script type=\"application/ld+json\">\n" +
                "  {\n" +
                "    \"@context\": \"https://schema.org\",\n" +
                "    \"@type\": \"SoftwareApplication\",\n" +
                "    \"name\": \"FastScrape\",\n" +
                "    \"operatingSystem\": \"Windows\",\n" +
                "    \"applicationCategory\": \"DeveloperApplication\",\n" +
                "    \"offers\": {\n" +
                "      \"@type\": \"Offer\",\n" +
                "      \"price\": \"0.00\",\n" +
                "      \"priceCurrency\": \"USD\"\n" +
                "    }\n" +
                "  }\n" +
                "  </script>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <h1>⚡ FastScrape Native Performance</h1>\n" +
                "    <p>FastScrape utilizes <strong>AVX2 vector instructions</strong> directly on the raw HTML bytes.</p>\n" +
                "    \n" +
                "    <div class=\"card\">\n" +
                "      <h2>Key Features</h2>\n" +
                "      <ul>\n" +
                "        <li>Zero heap allocation or DOM tree generation.</li>\n" +
                "        <li>Gigabytes/sec scraping speeds.</li>\n" +
                "        <li>Cleans dirty script/style blocks immediately.</li>\n" +
                "      </ul>\n" +
                "    </div>\n" +
                "    \n" +
                "    <p>Read more at our <a href=\"https://github.com/andrestubbe/FastScrape\">GitHub page</a> or join the community at <a href=\"https://discord.gg/fastjava\">Discord</a>.</p>\n" +
                "  </div>\n" +
                "  \n" +
                "  <script>\n" +
                "    console.log(\"Track page views\");\n" +
                "    function tracking() { return true; }\n" +
                "  </script>\n" +
                "</body>\n" +
                "</html>";

        byte[] htmlBytes = mockHtml.getBytes(StandardCharsets.UTF_8);

        // Open FastScrape
        FastScrape scraper = FastScrape.open();

        System.out.println("\u001B[33m[1] Extracting Clean Readable Text (Stripping CSS & JS blocks)...\u001B[0m");
        long start = System.nanoTime();
        String readableText = scraper.extractReadableText(htmlBytes);
        long end = System.nanoTime();
        double durationUs = (end - start) / 1000.0;

        System.out.println("\u001B[32m--- CLEAN TEXT OUTPUT (Time taken: " + durationUs + " µs) ---\u001B[0m");
        System.out.println(readableText);
        System.out.println("\u001B[32m--------------------------------------------------------\u001B[0m");

        System.out.println("\n\u001B[33m[2] Extracting Hyperlinks (<a href=\"...\">)...\u001B[0m");
        start = System.nanoTime();
        List<String> links = scraper.extractLinks(htmlBytes);
        end = System.nanoTime();
        durationUs = (end - start) / 1000.0;
        System.out.println("\u001B[32mLinks found (" + links.size() + " items, " + durationUs + " µs):\u001B[0m");
        for (String link : links) {
            System.out.println("  🔗 " + link);
        }

        System.out.println("\n\u001B[33m[3] Extracting Specific HTML tags (H2 blocks)...\u001B[0m");
        start = System.nanoTime();
        List<String> h2Tags = scraper.extractByTag(htmlBytes, "h2");
        end = System.nanoTime();
        durationUs = (end - start) / 1000.0;
        System.out.println("\u001B[32mH2 tag inner content (" + h2Tags.size() + " items, " + durationUs + " µs):\u001B[0m");
        for (String text : h2Tags) {
            System.out.println("  🏷️ " + text.trim());
        }

        System.out.println("\n\u001B[33m[4] Extracting JSON-LD Meta Schema blocks...\u001B[0m");
        start = System.nanoTime();
        String jsonLD = scraper.extractJsonLD(htmlBytes);
        end = System.nanoTime();
        durationUs = (end - start) / 1000.0;
        System.out.println("\u001B[32mJSON-LD schema (" + durationUs + " µs):\u001B[0m");
        System.out.println(jsonLD.trim());

        System.out.println("\n\u001B[35m====================================================================\u001B[0m");
        System.out.println("\u001B[36;1m✔ Hero Demo Complete! FastScrape executed flawlessly.\u001B[0m");
        System.out.println("\u001B[35m====================================================================\u001B[0m");
    }
}
