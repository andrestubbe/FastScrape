package fastscrape;

import java.util.List;

/**
2:  * FastScrape — High-performance native HTML/XML extractor.
3:  * 
4:  * Employs SIMD/AVX2 scanning at the native JNI layer to strip HTML/XML tags,
5:  * extract hyper-links, find elements by tags, and isolate JSON-LD blocks
6:  * in microseconds without garbage-collection or DOM tree overhead.
7:  */
public interface FastScrape {

    /**
     * Opens a new FastScrape instance.
     * 
     * @return a thread-safe FastScrape implementation
     */
    static FastScrape open() {
        return new FastScrapeImpl();
    }

    /**
     * Strips all tags and returns only the visible text, highly optimized for LLM input.
     * Discards {@code <script>}, {@code <style>} and comments, normalizes white space, and injects
     * intelligent newlines on block boundary tags.
     * 
     * @param htmlData raw HTML page bytes (typically UTF-8)
     * @return clean, readable plain text
     */
    String extractReadableText(byte[] htmlData);

    /**
     * Rapidly scans the HTML data to extract all hyperlinks (hrefs of {@code <a>} tags).
     * 
     * @param htmlData raw HTML bytes
     * @return list of extracted link URLs
     */
    List<String> extractLinks(byte[] htmlData);

    /**
     * Selectively extracts the contents of all matching tags (e.g. "title", "h1", "p").
     * 
     * @param htmlData raw HTML bytes
     * @param tagName the name of the tag (case-insensitive)
     * @return list of text contents inside the tags
     */
    List<String> extractByTag(byte[] htmlData, String tagName);

    /**
     * Specifically extracts raw JSON-LD metadata contents of type {@code <script type="application/ld+json">}.
     * 
     * @param htmlData raw HTML bytes
     * @return the extracted JSON block (concatenated if multiple exist), or an empty string if none found
     */
    String extractJsonLD(byte[] htmlData);
}
