# LightCore API

A lightweight Spigot/Paper library for:
- **Startup Messages** - Beautiful ASCII art startup messages
- **Messaging System** - Chat, ActionBar, Title, BossBar with hex colors & placeholders

---

## Startup Message Example

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
        <version>v1.0.6</version>
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

## Messaging API

### Quick Usage

```java
import me.lime.lightCore.api.messaging.Message;

// Chat message with hex colors
Message.chat(player, "&#00FF00Hello &#FFFFFFWorld!");

// Chat with placeholders
Message.chat(player, "&#FF0000Hello {player}!", "{player}", player.getName());

// Action bar
Message.actionbar(player, "&#FFD700+50 Coins!");

// Title
Message.title(player, "&#00FF00Welcome!", "&#FFFFFFEnjoy your stay");

// Title with timing (fadeIn, stay, fadeOut in ticks)
Message.title(player, "&#FF0000Game Over!", "&#FFFFFFTry again", 10, 70, 20);

// Boss bar (auto-hides after duration)
Message.bossbar(player, plugin, "&#FFD700Loading...", BossBar.Color.YELLOW, 100);

// Sound
Message.sound(player, Sound.ENTITY_PLAYER_LEVELUP);
Message.sound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f); // with volume & pitch
```

### Builder Pattern (Advanced)

```java
import me.lime.lightCore.api.messaging.MessageBuilder;

// Complex message with multiple features
MessageBuilder.create()
    .to(player)
    .message("&#00FF00Welcome {player}!")
    .placeholders("{player}", player.getName())
    .sound(Sound.ENTITY_PLAYER_LEVELUP)
    .volume(1.0f)
    .pitch(1.2f)
    .sendChat();

// Title with full control
MessageBuilder.create()
    .to(player)
    .title("&#FFD700Level Up!", "&#FFFFFFYou are now level {level}")
    .placeholders("{level}", "10")
    .titleTimes(10, 70, 20)
    .sendTitle();

// Boss bar with progress
MessageBuilder.create()
    .to(player)
    .plugin(plugin)
    .bossbar("&#FF0000Boss Health")
    .bossbarProgress(0.5f)
    .bossbarColor(BossBar.Color.RED)
    .bossbarDuration(200)
    .sendBossbar();
```

---

**Built with ❤️ by Lime | Clean, lightweight APIs for Spigot/Paper plugins.** 🚀
