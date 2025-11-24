# Publishing LightCore API to JitPack

Complete guide to make your API publicly available for other developers.

---

## 📋 Prerequisites

- ✅ GitHub account
- ✅ Git installed
- ✅ Your LightCore project ready

---

## 🚀 Publishing Steps

### Step 1: Create GitHub Repository

1. Go to [GitHub](https://github.com/new)
2. Create new repository:
   - **Name**: `LightCore`
   - **Description**: "Lightweight Spigot API for beautiful startup messages with built-in ASCII art"
   - **Visibility**: Public
   - **Don't** initialize with README (you already have one)

### Step 2: Push Your Code to GitHub

Open terminal in your project directory:

```bash
# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit: LightCore API v1.0.1"

# Add remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/LightCore.git

# Push to GitHub
git branch -M main
git push -u origin main
```

### Step 3: Create a Release Tag

```bash
# Create tag for version 1.0.1
git tag -a v1.0.1 -m "Release v1.0.1"

# Push tag to GitHub
git push origin v1.0.1
```

Or create release via GitHub web interface:
1. Go to your repository
2. Click **Releases** → **Create a new release**
3. **Tag**: `v1.0.1`
4. **Title**: `LightCore v1.0.1`
5. **Description**:
   ```
   ## Features
   - Static API (no plugin instance needed)
   - Built-in A-Z ASCII art generator
   - Hex color support for Minecraft 1.16+
   - Simple integration
   
   ## Installation
   Add to your pom.xml:
   ```xml
   <dependency>
       <groupId>com.github.YOUR_USERNAME</groupId>
       <artifactId>LightCore</artifactId>
       <version>1.0.1</version>
       <scope>provided</scope>
   </dependency>
   ```
   
   See README.md for full documentation.
   ```
6. Upload `target/lightcore-1.0.1.jar` as release asset
7. Click **Publish release**

### Step 4: Test on JitPack

1. Go to [https://jitpack.io](https://jitpack.io)
2. Enter your repository: `https://github.com/YOUR_USERNAME/LightCore`
3. Click **Look up**
4. Find version `v1.0.1` and click **Get it**
5. Wait for build to complete (green checkmark ✅)

### Step 5: Update README with Your GitHub Username

Replace `YourGitHubUsername` in README.md with your actual username:

```xml
<dependency>
    <groupId>com.github.YOUR_ACTUAL_USERNAME</groupId>
    <artifactId>LightCore</artifactId>
    <version>1.0.1</version>
    <scope>provided</scope>
</dependency>
```

Commit and push the change:
```bash
git add README.md
git commit -m "Update README with GitHub username"
git push
```

---

## 📦 Server Installation Guide

### For Server Owners:

1. Download `lightcore-1.0.1.jar` from GitHub Releases
2. Place in `plugins/` folder
3. Restart server

### For Plugin Developers:

1. Add JitPack repository to `pom.xml`
2. Add LightCore dependency
3. Add `depend: [lightcore]` to `plugin.yml`
4. Use the API in code
5. Ensure server has `lightcore-1.0.1.jar` installed

---

## 🔄 Updating to New Versions

### When you want to release v1.0.2:

1. **Update version in `pom.xml`:**
   ```xml
   <version>1.0.2</version>
   ```

2. **Commit changes:**
   ```bash
   git add pom.xml
   git commit -m "Bump version to 1.0.2"
   git push
   ```

3. **Create new tag:**
   ```bash
   git tag -a v1.0.2 -m "Release v1.0.2"
   git push origin v1.0.2
   ```

4. **Create GitHub Release** for v1.0.2

5. **JitPack will automatically build** the new version!

Users can then update their dependency:
```xml
<version>1.0.2</version>
```

---

## 📊 Monitoring Your API

### GitHub Insights
- Stars: See who's interested
- Forks: Track derivatives
- Network: View dependency graph
- Issues: Support users

### JitPack Dashboard
- Build logs: Check compilation
- Download stats: See usage
- Build status: Monitor health

---

## 🎯 Best Practices

### Version Naming
- `v1.0.0` - Initial release
- `v1.0.1` - Bug fixes
- `v1.1.0` - New features
- `v2.0.0` - Breaking changes

### Changelog
Create `CHANGELOG.md`:
```markdown
# Changelog

## [1.0.2] - 2025-11-24
### Added
- New method for shutdown messages

### Fixed
- Color parsing bug

## [1.0.1] - 2025-11-24
### Added
- Built-in A-Z ASCII art generator
- Static API methods

## [1.0.0] - 2025-11-24
- Initial release
```

### Documentation
- Keep README.md updated
- Add code examples
- Include troubleshooting
- Update version numbers

---

## 🔧 Troubleshooting

### "JitPack build failed"
1. Check build log on JitPack
2. Ensure `pom.xml` is valid
3. Verify Java 21 compatibility
4. Check `jitpack.yml` configuration

### "Dependency not found"
1. Wait 5-10 minutes after creating release
2. Clear Maven cache: `mvn clean`
3. Check JitPack build status
4. Verify repository is public

### "Version not available"
1. Ensure tag exists: `git tag -l`
2. Tag must be pushed: `git push origin v1.0.1`
3. Create GitHub release for the tag

---

## 📢 Promoting Your API

### SpigotMC
1. Post in Resources section
2. Link to GitHub repository
3. Include documentation

### PaperMC Forums
1. Share in Development section
2. Provide examples

### Discord Servers
1. Minecraft development servers
2. Share usage examples

### GitHub Topics
Add topics to repository:
- `minecraft`
- `spigot`
- `paper`
- `bukkit`
- `api`
- `ascii-art`

---

## ✅ Final Checklist

Before announcing your API:

- [ ] Code is on GitHub
- [ ] Release v1.0.1 created
- [ ] JitPack build successful ✅
- [ ] README.md has correct GitHub username
- [ ] INTEGRATION_GUIDE.md updated
- [ ] Example usage tested
- [ ] Changelog created
- [ ] License added (MIT recommended)
- [ ] `.gitignore` includes `target/`, `.idea/`, etc.

---

## 🎉 You're Live!

Once complete, developers can use your API:

```xml
<dependency>
    <groupId>com.github.YOUR_USERNAME</groupId>
    <artifactId>LightCore</artifactId>
    <version>1.0.1</version>
    <scope>provided</scope>
</dependency>
```

**No manual JAR downloads needed!** 🚀

---

## 📞 Support

**For Users:** Direct to GitHub Issues  
**For Contributors:** Pull requests welcome  
**Updates:** Watch repository for notifications
