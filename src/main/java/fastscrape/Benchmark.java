package fastscrape;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * High-speed Benchmark comparing FastScrape native SIMD against Standard Java replacements.
 */
public class Benchmark {

    private Benchmark() {
        // Utility class
    }

    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]*>");

    /**
     * Main entry point for FastScrape benchmark.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("\u001B[35m====================================================================\u001B[0m");
        System.out.println("\u001B[36;1m📈 FastScrape SIMD Performance Race 📈\u001B[0m");
        System.out.println("\u001B[35m====================================================================\u001B[0m");

        // Generate a larger HTML document (~5 MB) by repeating block structures
        System.out.println("\u001B[33mGenerating 5MB of structured HTML...\u001B[0m");
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Benchmark</title><style>body{color:#fff;}</style></head><body>");
        for (int i = 0; i < 20_000; i++) {
            sb.append("<div>\n");
            sb.append("  <h1>Article #").append(i).append("</h1>\n");
            sb.append("  <p>This is a fast paragraph illustrating text extraction speeds. It contains some <strong>bold</strong> words.</p>\n");
            sb.append("  <a href=\"https://example.com/article/").append(i).append("\">Read article #").append(i).append("</a>\n");
            sb.append("  <script>console.log(").append(i).append(");</script>\n");
            sb.append("</div>\n");
        }
        sb.append("</body></html>");

        String htmlString = sb.toString();
        byte[] htmlBytes = htmlString.getBytes(StandardCharsets.UTF_8);
        System.out.println("Generated HTML size: " + (htmlBytes.length / (1024.0 * 1024.0)) + " MB");

        FastScrape scraper = FastScrape.open();

        // Warmup loops
        System.out.println("\u001B[33mWarming up VMs and JNI channels...\u001B[0m");
        for (int i = 0; i < 3; i++) {
            scraper.extractReadableText(htmlBytes);
            TAG_PATTERN.matcher(htmlString).replaceAll("");
        }

        // Test 1: Standard Java Regex
        System.out.println("\u001B[33mRunning Race 1: Standard Java Regex...\u001B[0m");
        long start = System.currentTimeMillis();
        String regexResult = TAG_PATTERN.matcher(htmlString).replaceAll("");
        long end = System.currentTimeMillis();
        long javaTime = end - start;

        // Test 2: FastScrape Native SIMD
        System.out.println("\u001B[33mRunning Race 2: FastScrape Native AVX2...\u001B[0m");
        start = System.currentTimeMillis();
        String nativeResult = scraper.extractReadableText(htmlBytes);
        end = System.currentTimeMillis();
        long nativeTime = end - start;

        // Visual Result Matrix
        System.out.println("\n\u001B[32m--- RACE RESULT SUMMARY ---\u001B[0m");
        System.out.printf("| %-20s | %-15s | %-15s |\n", "Operation", "Java (Regex)", "FastScrape Native");
        System.out.println("|----------------------|-----------------|-------------------|");
        System.out.printf("| %-20s | %10d ms | %12d ms |\n", "Text Strip (5MB)", javaTime, nativeTime);
        System.out.println("---------------------------");
        
        double speedup = (double) javaTime / Math.max(1, nativeTime);
        System.out.printf("\u001B[32;1m⚡ FastScrape Speedup: %.2fx faster than Standard Java! ⚡\u001B[0m\n", speedup);
        System.out.println("\u001B[35m====================================================================\u001B[0m");
    }
}
