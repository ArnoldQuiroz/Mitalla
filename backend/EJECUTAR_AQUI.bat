@echo off
echo ============================================
echo EJECUTANDO BACKEND TALLAS
echo ============================================
echo.
cd /d "%~dp0"
echo Ubicacion: %CD%
echo.
echo Compilando proyecto...
call mvn clean install
echo.
echo Iniciando servidor...
call mvn spring-boot:run
pause
