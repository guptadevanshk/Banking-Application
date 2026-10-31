# Initialize local git repository (if not already done)
git init

# Add all project files
git add .

# Commit your changes
git commit -m "Initial commit"

# Rename the default branch to main
git branch -M main

# Link to your remote GitHub repository
git remote add origin https://github.com/guptadevanshk/Banking-Application.git

# Push your code up to GitHub
git push -u origin main
