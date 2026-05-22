package fastscrape;

import fastcore.FastCore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of FastScrape interface using native AVX2 logic.
 */
class FastScrapeImpl implements FastScrape {

    static {
        // Load the JNI library using FastCore Unified Loader
        FastCore.loadLibrary("fastscrape");
    }

    // Native JNI methods
    private native String nativeExtractReadableText(byte[] htmlData);
    private native String[] nativeExtractLinks(byte[] htmlData);
    private native String[] nativeExtractByTag(byte[] htmlData, String tagName);
    private native String nativeExtractJsonLD(byte[] htmlData);

    @Override
    public String extractReadableText(byte[] htmlData) {
        if (htmlData == null || htmlData.length == 0) {
            return "";
        }
        return nativeExtractReadableText(htmlData);
    }

    @Override
    public List<String> extractLinks(byte[] htmlData) {
        if (htmlData == null || htmlData.length == 0) {
            return new ArrayList<>();
        }
        String[] links = nativeExtractLinks(htmlData);
        return links != null ? List.of(links) : new ArrayList<>();
    }

    @Override
    public List<String> extractByTag(byte[] htmlData, String tagName) {
        Objects.requireNonNull(tagName, "tagName cannot be null");
        if (htmlData == null || htmlData.length == 0 || tagName.isEmpty()) {
            return new ArrayList<>();
        }
        String[] contents = nativeExtractByTag(htmlData, tagName.toLowerCase());
        return contents != null ? List.of(contents) : new ArrayList<>();
    }

    @Override
    public String extractJsonLD(byte[] htmlData) {
        if (htmlData == null || htmlData.length == 0) {
            return "";
        }
        return nativeExtractJsonLD(htmlData);
    }
}
