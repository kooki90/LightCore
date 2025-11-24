# LightCore API

[![](https://jitpack.io/v/kooki90/LightCore.svg)](https://jitpack.io/#kooki90/LightCore)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.16+-green.svg)](https://papermc.io/)

A lightweight Spigot/Paper library for creating beautiful startup messages with built-in ASCII art generator.

---

## Example

```java
import me.lime.lightCore.api.StartupMessage;

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        StartupMessage.printWithAscii("MyPlugin", "&#00FF00");
    }
}
```

**Output:**
```
 __  __       ____  _             _       
|  \/  |_   _|  _ \| |_   _  __ _(_)_ __  
| |\/| | | | | |_) | | | | |/ _` | | '_ \ 
| |  | | |_| |  __/| | |_| | (_| | | | | |
|_|  |_|\__, |_|   |_|\__,_|\__, |_|_| |_|
        |___/               |___/          

MyPlugin has been enabled!
```

---

## Usage

### Add to `pom.xml`

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.kooki90</groupId>
        <artifactId>LightCore</artifactId>
        <version>1.0.1</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.5.3</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <relocations>
                            <relocation>
                                <pattern>me.lime.lightCore.api</pattern>
                                <shadedPattern>com.yourplugin.libs.lightcore</shadedPattern>
                            </relocation>
                        </relocations>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Code

```java
import me.lime.lightCore.api.StartupMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class YourPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        StartupMessage.printWithAscii("YourPlugin", "&#00FF00");
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

## 🔧 How It Works

LightCore uses **shading/relocation** to bundle into your plugin:

1. **Developer adds** LightCore as a Maven/Gradle dependency
2. **Shade plugin** bundles LightCore classes into your plugin JAR
3. **Relocation** prevents conflicts with other plugins
4. **Server owner installs** only YOUR plugin (no LightCore.jar needed!)

### Example Build Output:
```
YourPlugin-1.0.0.jar
├── com/yourname/yourplugin/
│   ├── YourPlugin.class
│   └── libs/
│       └── lightcore/  ← LightCore bundled here
│           ├── StartupMessage.class
│           └── AsciiArt.class
└── plugin.yml (no depend: [lightcore] needed!)
```

**Result:** Server owners only install `YourPlugin-1.0.0.jar` - that's it! ✅

---

## 🆚 Library vs Plugin Dependency

| Aspect | LightCore (Library) | Traditional Plugin Dependency |
|--------|---------------------|------------------------------|
| **Server Setup** | Install only YOUR plugin | Install YOUR plugin + LightCore plugin |
| **plugin.yml** | No `depend: [lightcore]` | Requires `depend: [lightcore]` |
| **File Count** | 1 JAR file | 2 JAR files |
| **Updates** | Update your plugin only | Update both plugins |
| **Conflicts** | Prevented via relocation | Possible version conflicts |
| **Server Owner** | ✅ Simpler | ❌ More complex |
| **Use Case** | ✅ LightCore (100KB library) | ✅ Large APIs like Vault |

**Why Library?** LightCore is tiny (~100KB), so bundling it is more convenient than requiring a separate plugin installation.

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

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Development Setup
```bash
git clone https://github.com/YourUsername/LightCore.git
cd LightCore
mvn clean install
```

---

## 🐛 Bug Reports & Feature Requests

Found a bug or have an idea? [Open an issue](https://github.com/YourUsername/LightCore/issues)

**Bug Report Template:**
- Minecraft version
- Server software (Spigot/Paper)
- LightCore version
- Steps to reproduce
- Expected vs actual behavior

---

## 📝 Changelog

See [CHANGELOG.md](CHANGELOG.md) for a list of changes in each version.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🌟 Show Your Support

Give a ⭐️ if this project helped you!

---

## 📚 Documentation

- **[QUICKSTART.md](QUICKSTART.md)** - ⚡ Get started in 5 minutes (recommended!)
- **[README.md](README.md)** - Main documentation (you are here)
- **[INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md)** - Step-by-step integration tutorial
- **[PUBLISH_GUIDE.md](PUBLISH_GUIDE.md)** - How to publish your own fork
- **[CHANGELOG.md](CHANGELOG.md)** - Version history

---

**Built with ❤️ by Lime | No plugin instance, no complicated setup - just clean, beautiful startup messages.** 🚀
