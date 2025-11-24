# LightCore API

A lightweight Spigot/Paper library for creating beautiful startup messages with built-in ASCII art generator.

---

## Example

```java
import me.lime.lightCore.api.StartupMessage;

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Automatic - gets version, authors from plugin.yml and server info
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

Plugin Information:
 • Version: 1.0.0
 • Authors: YourName

Server Information:
 • Software: Paper
 • Version: 1.21.8-R0.1-SNAPSHOT
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
        <artifactId>lightcore</artifactId>
        <version>v1.0.4</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.2.4</version>
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

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Automatic - gets version, authors from plugin.yml and server info
        StartupMessage.printWithAscii("MyPlugin", "&#00FF00");
    }
}
```

---

**Built with ❤️ by Lime | No plugin instance, no complicated setup - just clean, beautiful startup messages.** 🚀
