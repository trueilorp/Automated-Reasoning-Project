@echo off
rem Compila il file Java
javac Main.java

rem Verifica se la compilazione è avvenuta con successo
if errorlevel 1 (
    echo Errore nella compilazione!
    exit /b
)

rem Esegui il programma Java
java Main

