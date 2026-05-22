@echo off
chcp 65001 > nul
echo ⚡ Building Main Project (Quiet Mode)...
call mvn clean package -DskipTests -q
if %ERRORLEVEL% NEQ 0 ( 
    echo ❌ Build failed!
    pause 
    exit /b 
)
echo 🚀 Running Hero Demo...
call mvn exec:java -Dexec.mainClass=fastscrape.Demo -q
pause
