# LightCore Integration Guide

Complete step-by-step guide for integrating LightCore API into your Minecraft plugin.

---

## 🚀 Quick Start (3 Steps)

### Step 1: Build LightCore
```bash
cd LightCore
mvn clean package
```
This creates `target/lightcore-1.0.1.jar`

### Step 2: Add to Your Plugin Project

Create this folder structure in your plugin project:
```
YourPlugin/
├── libs/
│   └── lightcore-1.0.1.jar  ← Place the JAR here
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│           └── plugin.yml
└── pom.xml
```

### Step 3: Configure Your Plugin

#### `pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.yourname</groupId>
    <artifactId>yourplugin</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <java.version>21</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <repositories>
        <repository>
            <id>papermc</id>
            <url>https://repo.papermc.io/repository/maven-public/</url>
        </repository>
        <!-- Local libs folder -->
        <repository>
            <id>local-libs</id>
            <url>file://${project.basedir}/libs</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- Paper API -->
        <dependency>
            <groupId>io.papermc.paper</groupId>
            <artifactId>paper-api</artifactId>
            <version>1.21-R0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- LightCore API -->
        <dependency>
            <groupId>me.lime</groupId>
            <artifactId>lightcore</artifactId>
            <version>1.0.1</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### `plugin.yml`
```yaml
name: YourPlugin
version: 1.0.0
main: com.yourname.yourplugin.YourPlugin
api-version: '1.21'
author: YourName

# IMPORTANT: Depend on LightCore
depend:
  - lightcore
```

---

## 📝 Usage Examples

### Example 1: Simple Startup Message
```java
package com.yourname.yourplugin;

import me.lime.lightCore.api.StartupMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class YourPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Simple colored message
        StartupMessage.print("YourPlugin", "&#00FF00");
    }
}
```

### Example 2: ASCII Art Startup
```java
package com.yourname.yourplugin;

import me.lime.lightCore.api.StartupMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class YourPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Auto-generated ASCII art
        StartupMessage.printWithAscii("EconomyPlus", "&#FFD700");
    }
}
```

### Example 3: Multiple Features
```java
package com.yourname.yourplugin;

import me.lime.lightCore.api.StartupMessage;
import me.lime.lightCore.api.AsciiArt;
import org.bukkit.plugin.java.JavaPlugin;

public class YourPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Main plugin startup
        StartupMessage.printWithAscii("RPG Core", "&#FF1493");
        
        // Feature messages
        getLogger().info("Loading features...");
        logFeature("Combat System", "&#FF0000");
        logFeature("Quest System", "&#00FF00");
        logFeature("Party System", "&#0000FF");
    }
    
    private void logFeature(String featureName, String color) {
        StartupMessage.print(featureName, color);
    }
}
```

### Example 4: Conditional ASCII
```java
package com.yourname.yourplugin;

import me.lime.lightCore.api.StartupMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class YourPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        boolean showAscii = getConfig().getBoolean("startup.show-ascii", true);
        
        if (showAscii) {
            StartupMessage.printWithAscii("MyPlugin", "&#00BFFF");
        } else {
            StartupMessage.print("MyPlugin", "&#00BFFF");
        }
    }
}
```

---

## 🎨 Popular Color Schemes

```java
// Nature Theme
StartupMessage.printWithAscii("NaturePlugin", "&#2ECC71");  // Green

// Fire Theme  
StartupMessage.printWithAscii("FirePlugin", "&#E74C3C");    // Red

// Water Theme
StartupMessage.printWithAscii("WaterPlugin", "&#3498DB");   // Blue

// Gold/Premium Theme
StartupMessage.printWithAscii("PremiumPlugin", "&#F39C12"); // Gold

// Dark/Shadow Theme
StartupMessage.printWithAscii("ShadowPlugin", "&#8E44AD");  // Purple

// Tech Theme
StartupMessage.printWithAscii("TechPlugin", "&#00FFFF");    // Cyan

// Pink/Magic Theme
StartupMessage.printWithAscii("MagicPlugin", "&#FF1493");   // Deep Pink
```

---

## 📦 Server Setup

### Installing Both Plugins

1. **Build your plugin**:
   ```bash
   mvn clean package
   ```

2. **Place in server** `plugins/` folder:
   ```
   server/
   └── plugins/
       ├── lightcore-1.0.1.jar     ← LightCore API (required)
       └── yourplugin-1.0.0.jar    ← Your plugin
   ```

3. **Start server** - LightCore will load first (dependency), then your plugin

---

## ✅ Testing Your Integration

### Test 1: Compile Test
```bash
mvn clean compile
```
✅ Should compile without errors

### Test 2: Package Test
```bash
mvn clean package
```
✅ Should create JAR successfully

### Test 3: Server Test
1. Start your test server
2. Check console for ASCII art
3. Verify colors display correctly

---

## 🐛 Troubleshooting

### Error: "Plugin X depends on lightcore, which is not loaded"
**Solution**: Make sure `lightcore-1.0.1.jar` is in `plugins/` folder

### Error: "Cannot resolve symbol 'StartupMessage'"
**Solution**: 
1. Check `libs/lightcore-1.0.1.jar` exists
2. Reload Maven project (IntelliJ: Right-click `pom.xml` → Maven → Reload)

### Colors not showing
**Solution**: 
1. Requires Minecraft 1.16+
2. Check hex format: `"&#RRGGBB"` (with `&#` prefix)

### ASCII art looks broken
**Solution**: Make sure console font supports ASCII characters (most do)

---

## 📚 Advanced Usage

### Using AsciiArt Directly
```java
import me.lime.lightCore.api.AsciiArt;

// Generate ASCII for any text
String[] asciiLines = AsciiArt.generate("DEMO");

// Use in custom messages
for (String line : asciiLines) {
    Bukkit.broadcastMessage("§6" + line);
}
```

### Custom Color Formatting
```java
import net.md_5.bungee.api.ChatColor;
import me.lime.lightCore.api.StartupMessage;

public class ColoredStartup {
    public void start() {
        // Use gradient colors
        StartupMessage.printWithAscii("RAINBOW", "&#FF0000");
        StartupMessage.print("Feature 1", "&#FF8800");
        StartupMessage.print("Feature 2", "&#FFD700");
        StartupMessage.print("Feature 3", "&#00FF00");
    }
}
```

---

## 🎯 Best Practices

1. ✅ **Always use `scope=provided`** in pom.xml (LightCore will be on server)
2. ✅ **Add `depend: [lightcore]`** in plugin.yml
3. ✅ **Keep ASCII messages short** (better readability)
4. ✅ **Use hex colors** for modern look
5. ✅ **Test on actual server** before release

---

## 📞 Support

- Issues? Check README.md
- Questions? Contact plugin author
- Updates? Watch the repository

**Happy coding!** 🚀
