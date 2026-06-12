@echo off
echo Adguard Home WebView App - GitHub Upload Helper
echo ================================================
echo.
echo This script will help you upload the project to GitHub.
echo.
echo Prerequisites:
echo 1. Git installed
echo 2. GitHub account
echo 3. GitHub repository created
echo.
echo Steps:
echo 1. Create a new repository on GitHub
echo 2. Run this script
echo 3. Follow the instructions
echo.
pause

echo.
echo Step 1: Initialize Git repository (if not already done)
if not exist ".git" (
    git init
    echo Git repository initialized.
) else (
    echo Git repository already exists.
)

echo.
echo Step 2: Add all files
git add .

echo.
echo Step 3: Commit files
git commit -m "Initial commit: Adguard Home WebView app"

echo.
echo Step 4: Connect to GitHub repository
echo Please enter your GitHub repository URL:
echo Example: https://github.com/username/repo-name.git
set /p REPO_URL="Repository URL: "

git remote add origin %REPO_URL%

echo.
echo Step 5: Push to GitHub
git push -u origin master

echo.
echo Upload complete!
echo You can now check your GitHub repository for the code.
echo GitHub Actions will automatically build the APK.
echo.
pause