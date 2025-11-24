# LightCore API

A lightweight Spigot/Paper API for creating beautiful startup messages with built-in ASCII art generator (A-Z).

## Features

✨ **Static API** - No plugin instance needed, just like Vault API  
🎨 **Built-in ASCII Art** - Complete A-Z letter generator  
🌈 **Hex Color Support** - Full RGB color support for Minecraft 1.16+  
⚡ **Simple & Clean** - Easy to use, no dependencies

---

## For Plugin Developers - How to Use in Your Project

### Step 1: Add LightCore Repository & Dependency

#### Maven (`pom.xml`)

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.YourGitHubUsername</groupId>
        <artifactId>LightCore</artifactId>
        <version>1.0.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

> **Note:** Replace `YourGitHubUsername` with your actual GitHub username

#### Gradle (`build.gradle`)

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.YourGitHubUsername:LightCore:1.0.1'
}
```

### Step 2: Add Dependency to Your `plugin.yml`

```yaml
depend: [lightcore]
```

### Step 3: Download LightCore Plugin

Download the latest `lightcore-1.0.1.jar` from:
- **GitHub Releases**: [Releases Page](https://github.com/YourUsername/LightCore/releases)
- **JitPack**: Automatically built from repository

Place `lightcore-1.0.1.jar` in your **server's** `plugins/` folder (not your project!)

### Step 4: Use the API in Your Code

```java
import me.lime.lightCore.api.StartupMessage;
import me.lime.lightCore.api.AsciiArt;
import org.bukkit.plugin.java.JavaPlugin;

public class YourPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Option 1: Simple colored message
        StartupMessage.print("YourPlugin", "&#00FF00");
        
        // Option 2: Auto-generated ASCII art (recommended!)
        StartupMessage.printWithAscii("YourPlugin", "&#FF5733");
        
        // Option 3: Custom ASCII art
        StartupMessage.printWithCustomAscii(
            "YourPlugin",
            "&#FFD700",
            "  ____          _                  ",
            " |  _ \\ ___ ___| |_ __ ___   ___  ",
            " | |_) / _ \\_  / __/ _` \\ \\ / / | ",
            " |  __/  __// /| || (_| |\\ V /| | ",
            " |_|   \\___/___\\__\\__,_| \\_/ |_| "
        );
    }
}
```

---

## API Reference

### `StartupMessage` Class

#### Method 1: Simple Message
```java
StartupMessage.print(String name, String hexColor)
```
- **name**: Plugin/feature name
- **hexColor**: Hex color code (format: `"&#RRGGBB"`)

**Example:**
```java
StartupMessage.print("MyPlugin", "&#FF5733");
```

---

#### Method 2: Auto-Generated ASCII Art
```java
StartupMessage.printWithAscii(String name, String hexColor)
```
- **name**: Text to convert to ASCII art (A-Z, spaces supported)
- **hexColor**: Hex color code (format: `"&#RRGGBB"`)

**Example:**
```java
StartupMessage.printWithAscii("Hello World", "&#00BFFF");
```

**Output:**
```
 _   _      _ _        __        __         _     _ 
| | | | ___| | | ___   \ \      / /__  _ __| | __| |
| |_| |/ _ \ | |/ _ \   \ \ /\ / / _ \| '__| |/ _` |
|  _  |  __/ | | (_) |   \ V  V / (_) | |  | | (_| |
|_| |_|\___|_|_|\___/     \_/\_/ \___/|_|  |_|\__,_|

Hello World has been enabled!
```

---

#### Method 3: Custom ASCII Art
```java
StartupMessage.printWithCustomAscii(String name, String hexColor, String... asciiLines)
```
- **name**: Plugin/feature name
- **hexColor**: Hex color code
- **asciiLines**: Your custom ASCII art lines

**Example:**
```java
StartupMessage.printWithCustomAscii(
    "Epic Plugin",
    "&#FFD700",
    "  _____ ____ ___ ____ ",
    " | ____|  _ \\_ _/ ___|",
    " |  _| | |_) | | |    ",
    " | |___|  __/| | |___ ",
    " |_____|_|  |___\\____|"
);
```

---

### `AsciiArt` Class

#### Generate ASCII Art Directly
```java
String[] asciiLines = AsciiArt.generate("Your Text");
```

**Example:**
```java
String[] ascii = AsciiArt.generate("DEMO");
for (String line : ascii) {
    System.out.println(line);
}
```

#### Get ASCII Art as String
```java
String asciiString = AsciiArt.generateString("Your Text");
System.out.println(asciiString);
```

---

## Color Examples

```java
// Red
StartupMessage.printWithAscii("RedPlugin", "&#FF0000");

// Green
StartupMessage.printWithAscii("GreenPlugin", "&#00FF00");

// Blue
StartupMessage.printWithAscii("BluePlugin", "&#0000FF");

// Orange
StartupMessage.printWithAscii("OrangePlugin", "&#FF8800");

// Purple
StartupMessage.printWithAscii("PurplePlugin", "&#9900FF");

// Gold
StartupMessage.printWithAscii("GoldPlugin", "&#FFD700");

// Cyan
StartupMessage.printWithAscii("CyanPlugin", "&#00FFFF");
```

---

## Building LightCore

### Maven
```bash
mvn clean package
```

### Gradle
```bash
./gradlew build
```

The compiled JAR will be in `target/lightcore-1.0.1.jar`

---

## Requirements

- **Minecraft**: 1.16+ (for hex color support)
- **Java**: 21+
- **Server**: Spigot, Paper, or any fork

---

## Support

- **Version**: 1.0.1
- **Author**: Lime
- **License**: MIT

---

## Examples in the Wild

```java
// Fantasy RPG Plugin
StartupMessage.printWithAscii("Fantasy RPG", "&#FF1493");

// Economy System
StartupMessage.printWithAscii("Vault Plus", "&#FFD700");

// Minigame
StartupMessage.printWithAscii("BedWars", "&#FF6347");

// Administration Tool
StartupMessage.printWithAscii("Admin Tools", "&#4169E1");
```

---

**That's it! No plugin instance, no complicated setup - just clean, beautiful startup messages.** 🚀
