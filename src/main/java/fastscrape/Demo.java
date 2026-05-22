package fastscrape;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Hero Demo — fetches a real Wikipedia page and visually scans it.
 * Shows an animated HTML-shrinking / text-growing progress bar,
 * extraction stats, and a clean text preview — all in the terminal.
 */
public class Demo {

    private Demo() {}

    // ── ANSI helpers ────────────────────────────────────────────────────────
    private static final String R  = "\033[0m";
    private static final String B  = "\033[1m";
    private static final String CY = "\033[36m";
    private static final String GR = "\033[32m";
    private static final String YL = "\033[33m";
    private static final String MG = "\033[35m";
    private static final String RD = "\033[31m";
    private static final String DM = "\033[90m";
    private static final String ER = "\033[K";
    private static final String[] SPIN = {"⠋","⠙","⠹","⠸","⠼","⠴","⠦","⠧","⠇","⠏"};

    private static final String TARGET_URL =
            "https://en.wikipedia.org/wiki/SIMD";

    /**
     * Main entry point for FastScrape demo.
     *
     * @param args command line arguments
     * @throws Exception if the HTTP fetch fails
     */
    public static void main(String[] args) throws Exception {
        banner();

        FastScrape scraper = FastScrape.open();

        // ── Phase 1: download real Wikipedia page ───────────────────────────
        header("Phase 1", "Downloading real Wikipedia page");
        System.out.println("  " + CY + "→" + R + " " + TARGET_URL);
        System.out.println();

        HttpClient http = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(TARGET_URL))
                .header("User-Agent", "FastScrape/0.1.0 Demo")
                .build();

        CompletableFuture<HttpResponse<byte[]>> future =
                http.sendAsync(req, HttpResponse.BodyHandlers.ofByteArray());

        int s = 0;
        while (!future.isDone()) {
            System.out.printf("\r  " + CY + SPIN[s % SPIN.length] + R + " Downloading..." + ER);
            System.out.flush();
            Thread.sleep(80);
            s++;
        }
        byte[] html = future.get().body();
        System.out.printf("\r  " + GR + "✔ Downloaded:" + R + "  " + B + "%,d" + R + " bytes of HTML" + ER + "\n\n",
                html.length);

        // ── Phase 2: extract (fast) then animate ────────────────────────────
        header("Phase 2", "Native AVX2 scan — watch the tags disappear");
        System.out.println();

        long t0 = System.nanoTime();
        String cleanText  = scraper.extractReadableText(html);
        List<String> links = scraper.extractLinks(html);
        long parseUs = (System.nanoTime() - t0) / 1_000;

        int htmlLen  = html.length;
        int cleanLen = cleanText.length();
        int steps    = 40;

        for (int i = 0; i <= steps; i++) {
            int  filled   = i * 22 / steps;
            int  empty    = 22 - filled;
            int  bytesNow = htmlLen - (htmlLen * i / steps);
            int  charsNow = cleanLen * i / steps;
            int  pct      = i * 100 / steps;

            String bar = GR + "█".repeat(filled) + R + DM + "░".repeat(empty) + R;
            System.out.printf(
                "\r  HTML [%s] " + B + "%3d%%" + R
                + "   " + RD + "%,7d" + R + " bytes  →  " + GR + "%,6d" + R + " chars" + ER,
                bar, pct, bytesNow, charsNow);
            System.out.flush();
            Thread.sleep(35);
        }
        System.out.println();
        System.out.println();

        // ── Phase 3: extraction stats ────────────────────────────────────────
        header("Phase 3", "Extraction results");
        System.out.println();

        double reduction = 100.0 * (htmlLen - cleanLen) / htmlLen;
        stat("HTML input",      String.format("%,d bytes", htmlLen));
        stat("Clean text",      String.format("%,d chars", cleanLen));
        stat("Noise removed",   String.format("%.1f%%", reduction));
        stat("Links found",     String.format("%d hrefs", links.size()));
        stat("Native parse",    String.format("%,d µs  (AVX2)", parseUs));
        System.out.println();

        // ── Phase 4: clean text preview ─────────────────────────────────────
        header("Phase 4", "Extracted text preview");
        System.out.println();

        String preview = cleanText.strip();
        if (preview.length() > 700) preview = preview.substring(0, 700) + "...";
        for (String line : preview.split("\n")) {
            String trimmed = line.strip();
            if (!trimmed.isEmpty()) System.out.println("  " + trimmed);
        }
        System.out.println();

        // ── Footer ───────────────────────────────────────────────────────────
        footer(String.format("%,d bytes of Wikipedia HTML  →  %,d chars of clean text in %,d µs.",
                htmlLen, cleanLen, parseUs));
    }

    // ── Helpers ─────────────────────────────────────────────────────────────
    private static void stat(String label, String value) {
        System.out.printf("  " + CY + "%-20s" + R + "  " + B + "%s" + R + "%n", label + ":", value);
    }

    private static void banner() {
        System.out.println(MG + "═══════════════════════════════════════════════════════════════");
        System.out.println(B + CY + "  🔍 FastScrape  —  Native AVX2 HTML Scraper" + R);
        System.out.println(MG + "═══════════════════════════════════════════════════════════════" + R);
        System.out.println();
    }

    private static void header(String phase, String msg) {
        System.out.println(YL + B + "[" + phase + "]" + R + " " + msg);
    }

    private static void footer(String msg) {
        System.out.println(MG + "═══════════════════════════════════════════════════════════════");
        System.out.println(B + GR + "  ✔ " + msg + R);
        System.out.println(MG + "═══════════════════════════════════════════════════════════════" + R);
    }
}
