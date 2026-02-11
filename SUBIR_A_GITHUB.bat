@echo off
cd /d "%~dp0"
echo ============================================
echo SUBIENDO PROYECTO A GITHUB
echo ============================================
echo.

echo [1/6] Añadiendo archivos...
git add .

echo.
echo [2/6] Creando commit...
git commit -m "Proyecto MiTalla completo - App Android + Backend Spring Boot"

echo.
echo [3/6] Verificando rama main...
git branch -M main

echo.
echo [4/6] Verificando remote...
git remote -v

echo.
echo [5/6] Subiendo a GitHub...
git push -u origin main

echo.
echo ============================================
echo LISTO - Proyecto subido a GitHub
echo ============================================
pause
