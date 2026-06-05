@echo off
    echo [ERROR] Build failed!
    pause 
    exit /b 
)
echo [FastScrape] Running Demo (via JitPack)...
call mvn exec:java -Dexec.mainClass=fastscrape.Demo
pause

