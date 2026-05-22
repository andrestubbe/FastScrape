# FastScrape

## 1. Vision & Kernidee
**FastScrape** ist Javas schnellster, rein nativer HTML/XML-Extraktor. 

Während `FastSpider` sich *nur* darum kümmert, die Daten aus dem Netzstrom (Netzwerk/HTTP) in den Arbeitsspeicher zu schaufeln, ist `FastScrape` der reine **Daten-Analysator**. 
Es trennt strikt die I/O-Ebene von der Parsing-Ebene.

Klassische Parser (wie JSoup) bauen aus dem HTML-Text einen riesigen, feingranularen Baum aus unzähligen Java-Objekten (DOM-Tree) im RAM auf. Das erzeugt gigantischen Garbage-Collection-Overhead. FastScrape baut **keinen** Baum. Es nutzt rohe CPU-SIMD-Befehle, um wie ein Laser durch das Byte-Array zu scannen und nur die relevanten Informationen herauszuschneiden.

## 2. Java High-Level API

```java
public interface FastScrape {
    static FastScrape open() { return new FastScrapeImpl(); }

    // Der wichtigste Befehl für KI-Agenten: Strippt alles weg, was kein lesbarer Text ist.
    String extractReadableText(byte[] htmlData);

    // Extrahiert alle Hyperlinks (<a href="...">)
    List<String> extractLinks(byte[] htmlData);

    // Selektive Extraktion (wie CSS-Selektoren, aber auf Byte-Ebene)
    List<String> extractByTag(byte[] htmlData, String tagName);
    
    // Sucht spezifisch nach JSON-LD Meta-Daten (perfekt für Produkt-Preis-Scraping)
    String extractJsonLD(byte[] htmlData);
}
```

## 3. C++ JNI Backend (AVX2 / SIMD)
Das Backend macht sich zunutze, dass HTML extrem vorhersehbar ist.

1. **Zero-Copy Access:** Java reicht das `byte[]` (z.B. frisch aus `FastSpider` geladen) an C++ weiter.
2. **SIMD-Scanning:** AVX2 vergleicht 32 Bytes gleichzeitig. Es sucht z.B. rasend schnell nach dem Muster `<script` und überspringt dann massiv Speicherblöcke, bis es das schließende `</script>` findet. Alles dazwischen wird ignoriert.
3. **Lineare Extraktion:** Der echte Text, der zwischen den übrigen HTML-Tags steht, wird direkt in einen neuen UTF-8 `FastString` kopiert und an Java zurückgegeben.

## 4. Agent-Kit (KI-Integration)
Wenn der Agent das Web durchsucht, interessiert er sich nicht für das CSS-Styling, die JavaScript-Tracking-Pixel oder die Navigation-Header. Er will den puren Artikel-Text, um Fragen zu beantworten (RAG).

**Workflow für den Agenten:**
1. Agent nutzt `FastSpider`, um `https://wikipedia.org/wiki/Java` herunterzuladen.
2. Das HTML-Byte-Array geht in `FastScrape.extractReadableText()`.
3. Aus 2 MB purem HTML-Code werden in unter 1 Millisekunde 40 KB reiner Text extrahiert.
4. Der LLM-Tokenizer zerlegt diesen Text und baut ihn in den Kontext-Prompt des Agenten ein.
