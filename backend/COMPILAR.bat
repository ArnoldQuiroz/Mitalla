@echo off
cd /d "%~dp0"
echo Descargando dependencias...
call mvn dependency:resolve
echo.
echo Compilando...
call mvn clean compile
echo.
echo ============================================
echo LISTO - Ahora abre IntelliJ y:
echo 1. File - Open - Selecciona carpeta backend
echo 2. Espera a que cargue
echo 3. Los errores desapareceran
echo ============================================
pause
