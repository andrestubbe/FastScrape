# FastScrape — High-performance native HTML/XML extractor for Java

**High-performance SIMD/AVX2-powered HTML and XML data-mining engine for the JVM.**

FastScrape is the data-extraction substrate of the **FastJava** web stack. It provides highly-optimized native algorithms to strip formatting blocks, find hyperlinks, extract structured tags, and parse JSON-LD schemas in microseconds—bypassing the latency, memory allocations, and heap pressure of traditional heavy DOM parsers.

```java
// Quick Start — Microsecond Text Scraping
byte[] rawHtml = ...; // 5MB HTML buffer downloaded from web
FastScrape scraper = FastScrape.open();

// Strips CSS/JS, normalizes spacing, and reformats block-level tags in under 5ms
String cleanText = scraper.extractReadableText(rawHtml);
```

[![Status](https://img.shields.io/badge/status-v0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastScrape/releases/tag/v0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

## Table of Contents
- [Key Features](#key-features)
- [Performance](#performance)
- [API Quick Reference](#api-quick-reference)
- [Installation](#installation)
- [Technical Examples & Hero Demos](#technical-examples--hero-demos)
- [Platform Support](#platform-support)
- [Modular Ecosystem](#modular-ecosystem)
- [License](#license)

---

## Key Features
- **⚡ SIMD/AVX2 Acceleration**: Loads 32-byte chunks into CPU vector registers to skip tags and whitespace instantly.
- **🔍 Zero-Copy Region Locking**: Employs `GetPrimitiveArrayCritical` JNI regions to lock the GC and parse Java arrays directly on the native C++ heap.
- **🤖 LLM & RAG Optimized**: Strips `<script>`, `<style>`, and comments while inserting block layout newlines to form clean readable text.
- **⚙️ Dynamic Runtime CPU Detection**: Auto-detects AVX2 using `__cpuid` at startup with seamless scalar fallback routines for non-AVX2 hardware.

---

## 📊 Performance (v0.1.0)

Measured on **Intel/AMD x64 Hardware** with AVX2 instruction support.

| Operation | Input Size | Java (Regex / Standard) | FastScrape Native (v0.1.0) | Speedup |
|-----------|------------|-------------------------|---------------------------|---------|
| **Text Strip** | 5 MB Page  | ~210 ms                 | **~5 ms**                 | **42x** |
| **Link Scan**  | 5 MB Page  | ~45 ms                  | **~2 ms**                 | **22x** |
| **JSON-LD Pull**| 5 MB Page  | ~38 ms                 | **~1 ms**                 | **38x** |

> [!NOTE]
> Speedups scale directly with document size due to AVX2 vector unrolling and zero-heap instantiation during native parsing.

---

## API Quick Reference

| Method | Description | Target Path |
|--------|-------------|-------------|
| `extractReadableText(...)` | Cleans document markup and reformats block spacing for LLMs. | [Reference →](REFERENCE.md#extractreadabletext) |
| `extractLinks(...)` | Scans for anchor elements and aggregates hyper-links natively. | [Reference →](REFERENCE.md#extractlinks) |
| `extractByTag(...)` | Finds all elements matching target name and extracts inner content. | [Reference →](REFERENCE.md#extractbytag) |
| `extractJsonLD(...)` | Isolates all linked JSON-LD metadata schemas concurrently. | [Reference →](REFERENCE.md#extractjsonld) |

> [!TIP]
> Use `FastScrape.open()` to obtain the thread-safe native implementation class.

---

## Installation

### Option 1: Maven (Recommended)
Add the JitPack repository and the dependencies to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <!-- FastScrape Library -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastscrape</artifactId>
        <version>v0.1.0</version>
    </dependency>

    <!-- FastCore (Required Native Loader) -->
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>fastcore</artifactId>
        <version>v0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:fastscrape:v0.1.0'
    implementation 'com.github.andrestubbe:fastcore:v0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)
Download the latest JARs directly to add them to your classpath:

1. 📦 **[fastscrape-v0.1.0.jar](https://github.com/andrestubbe/FastScrape/releases/download/v0.1.0/fastscrape-v0.1.0.jar)** (The Core Library)
2. ⚙️ **[fastcore-v0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/v0.1.0/fastcore-v0.1.0.jar)** (The Mandatory Native Loader)

> [!IMPORTANT]
> All JARs must be in your classpath for the native JNI calls to function correctly.


## Technical Examples & Hero Demos
Explore the complete source configurations and benchmarks:

* **⚡ Interactive Demo**: [Demo.java](src/main/java/fastscrape/Demo.java) (crawls, cleans, and structures an ANSI color report).
* **⚡ Joint Pipeline Demo**: [PipelineDemo.java](https://github.com/andrestubbe/FastSpider/tree/main/examples/PipelineDemo) (orchestrates FastSpider and FastScrape in unison: fetches asynchronously via WinHTTP and parses HTML via AVX2 in a zero-copy pipeline).
* **📈 Performance Benchmark**: [Benchmark.java](src/main/java/fastscrape/Benchmark.java) (races FastScrape against Java's standard compiler regex engines).
* **🧪 Test Suite**: [FastScrapeTest.java](src/test/java/fastscrape/FastScrapeTest.java) (comprehensive JUnit 5 validation).

Run the hero demo locally from the command line:
```bash
mvn exec:java "-Dexec.mainClass=fastscrape.Demo"
```

For combined crawler & parser pipeline instructions, see the [FastSpider Pipeline Demo](https://github.com/andrestubbe/FastSpider#technical-examples--hero-demos).

---

## Platform Support
| Platform | Status |
|----------|--------|
| Windows 10/11 (x64) | ✅ Fully Supported (WinHTTP + AVX2 Native) |
| Linux | 🚧 Planned |
| macOS | 🚧 Planned |

---

## Modular Ecosystem
Combine FastScrape with other accelerators for maximum efficiency:
* [**FastSpider**](https://github.com/andrestubbe/FastSpider) — Native WinHTTP crawler.
* [**FastCore**](https://github.com/andrestubbe/FastCore) — Native loading substrate.
* [**FastBytes**](https://github.com/andrestubbe/FastBytes) — Hardware-aligned byte arrays.
* [**FastJSON**](https://github.com/andrestubbe/FastJSON) — SIMD-powered JSON parser.

---

**Part of the FastJava Ecosystem** — *Making the JVM faster.*

Made with ⚡ by Andre Stubbe
