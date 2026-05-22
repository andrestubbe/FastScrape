# FastScrape
Native high-speed HTML/XML extraction for Java.

[![Status](https://img.shields.io/badge/status-v0.1.0--alpha-orange.svg)]()
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Vision
`FastScrape` is the parsing layer of the FastJava web stack.

- `FastSpider` loads raw bytes from the network.
- `FastScrape` analyzes those bytes and extracts only useful content.

Instead of creating a full DOM tree with many Java objects, FastScrape scans raw byte arrays with native SIMD logic and returns the relevant data with minimal GC pressure.

## Core API (planned)
```java
public interface FastScrape {
    static FastScrape open() { return new FastScrapeImpl(); }

    String extractReadableText(byte[] htmlData);
    List<String> extractLinks(byte[] htmlData);
    List<String> extractByTag(byte[] htmlData, String tagName);
    String extractJsonLD(byte[] htmlData);
}
```

## Native design
1. **Zero-copy handoff**
   Java passes a `byte[]` directly to JNI.
2. **SIMD scanning**
   Native code scans blocks of bytes to detect patterns like `<script`, `<style>`, `<a href=...>`, or `<script type=\"application/ld+json\">`.
3. **Linear extraction**
   Relevant bytes are copied into compact output buffers and returned to Java as text/collections.

## Why this exists
Traditional HTML parsers are feature-rich but expensive for LLM pipelines that only need readable text and metadata.
FastScrape focuses on:

- low latency
- high throughput
- low allocation overhead
- predictable extraction behavior for agent/RAG workloads

## Agent workflow
1. Download page bytes (typically via `FastSpider`).
2. Call `extractReadableText(...)` for article content.
3. Call `extractLinks(...)` and `extractJsonLD(...)` for navigation and metadata.
4. Feed extracted text into tokenizer/RAG context.

## Installation
### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.github.andrestubbe</groupId>
        <artifactId>fastscrape</artifactId>
        <version>0.1.0</version>
    </dependency>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastcore</artifactId>
        <version>v1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'io.github.andrestubbe:fastscrape:0.1.0'
    implementation 'com.github.andrestubbe:fastcore:v1.0.0'
}
```

## Current status
This repository is in early alpha and currently contains JNI scaffolding.
Production-grade extraction methods are defined as the next implementation milestone.

## License
MIT License — see [LICENSE](LICENSE).
