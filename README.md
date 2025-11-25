# LightCore API

A lightweight Spigot/Paper library for:
- **Startup Messages** - Beautiful ASCII art startup messages
- **Messaging System** - Chat, ActionBar, Title, BossBar with hex colors & placeholders
- **World Utilities** - World, Block, and Chunk helpers
- **Text Colors** - MiniMessage & legacy color support with caching
- **Console Logging** - Colored log levels (info, warn, error, debug, fatal)
- **Command Helpers** - Tab completion utilities and player/world filters
- **Config Utilities** - ConfigService with caching and ConfigMigrator for updates

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
        <version>v1.0.10</version>
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

## World Utilities API

### WorldUtil

```java
import me.lime.lightCore.api.world.WorldUtil;

// Check world environment
WorldUtil.isOverworld(world);  // true if normal world
WorldUtil.isNether(world);     // true if nether
WorldUtil.isEnd(world);        // true if end

// Compare locations/worlds
WorldUtil.isSameWorld(locA, locB);
WorldUtil.isLocationInWorld(loc, world);

// Get world info
WorldUtil.getName(world);      // "world" or "unknown"
WorldUtil.getType(world);      // "overworld", "nether", "end", "custom"
WorldUtil.getSpawn(world);     // spawn location
```

### BlockUtil

```java
import me.lime.lightCore.api.world.BlockUtil;

// Check block properties
BlockUtil.isAir(block);        // air or empty
BlockUtil.isSolid(block);      // solid (not liquid)
BlockUtil.isLiquid(block);     // water or lava
BlockUtil.isDangerous(block);  // lava, fire, cactus, magma, etc.
BlockUtil.isSafe(block);       // safe to stand on
BlockUtil.isContainer(block);  // chest, barrel, hopper, etc.

// Get block info
BlockUtil.getTypeName(block);  // "STONE", "GRASS_BLOCK", etc.
BlockUtil.isSameType(blockA, blockB);
```

### ChunkUtil

```java
import me.lime.lightCore.api.world.ChunkUtil;

// Async chunk loading
ChunkUtil.getChunkAsync(world, chunkX, chunkZ)
    .thenAccept(chunk -> {
        // Do something with chunk
    });

// Find safe spawn Y coordinate
ChunkUtil.createSafeYAsync(world, x, z)
    .thenAccept(safeY -> {
        if (safeY != Integer.MIN_VALUE) {
            Location safe = new Location(world, x, safeY, z);
            player.teleport(safe);
        }
    });

// Check if position is safe
ChunkSnapshot snapshot = ChunkUtil.snapshot(chunk);
boolean safe = ChunkUtil.isSafe(snapshot, localX, y, localZ, minY, maxY);
```

---

## Text Color API

Supports MiniMessage tags, legacy `&` codes, and hex `&#RRGGBB` colors with automatic caching.

### Basic Usage

```java
import me.lime.lightCore.api.color.Text;

// String to Component (supports MiniMessage & legacy)
Component msg = Text.color("<gradient:red:blue>Hello World!");
Component msg2 = Text.color("&cRed &aGreen &#FF00FFPurple");

// For legacy APIs (returns String with § codes)
String legacy = Text.colorLegacy("&cHello &#00FF00World");

// Send to player
player.sendMessage(Text.color("<green>Welcome, <gold>{player}!"));
```

### Supported Formats

| Format | Example |
|--------|---------|
| **Legacy &** | `&c` (red), `&l` (bold), `&o` (italic) |
| **Hex &#** | `&#FF0000` (red), `&#00FF00` (green) |
| **MiniMessage** | `<red>`, `<gradient:red:blue>`, `<rainbow>` |
| **Click/Hover** | `<click:run_command:/help>Click</click>` |

### List Operations

```java
// Colorize list of strings
List<Component> lore = Text.colorList(Arrays.asList(
    "&7Line 1",
    "<gradient:gold:yellow>Line 2",
    "&#FF00FFLine 3"
));

// For legacy (item lore compatibility)
List<String> legacyLore = Text.colorLegacyList(stringList);
```

### Conversion

```java
// Component to legacy string
String legacy = Text.toLegacy(component);

// Component to MiniMessage string
String mini = Text.toMiniMessage(component);
```

---

## Console Logger API

Colored console logging with different log levels.

```java
import me.lime.lightCore.api.logging.ConsoleLogger;

// Simple logging
ConsoleLogger.info("Server started!");           // Light blue
ConsoleLogger.warn("Low memory warning!");       // Yellow/Orange
ConsoleLogger.error("Failed to load config!");   // Light red
ConsoleLogger.debug("Player joined: Steve");     // Bright green
ConsoleLogger.fatal("Critical error!");          // Bright red
ConsoleLogger.success("Plugin loaded!");         // Green

// With plugin prefix
ConsoleLogger.info("MyPlugin", "Loaded 10 items");
ConsoleLogger.error("MyPlugin", "Database connection failed");
```

### Log Levels

| Level | Color | Use Case |
|-------|-------|----------|
| `INFO` | Light Blue | General information |
| `WARN` | Yellow | Warnings |
| `ERROR` | Light Red | Errors |
| `DEBUG` | Bright Green | Debug messages |
| `FATAL` | Bright Red | Critical errors |
| `SUCCESS` | Green | Success messages |

---

## Command Helper API

Utilities for tab completions and command operations.

> ⚠️ **Note:** This is a lightweight alternative to ACF (Aikar Command Framework). If you need full ACF features like annotations and auto-registration, add ACF separately.

```java
import me.lime.lightCore.api.command.CommandHelper;

// Built-in completions
List<String> players = CommandHelper.getOnlinePlayers();
List<String> worlds = CommandHelper.getWorlds();
List<String> bools = CommandHelper.getBooleans();  // ["true", "false"]
List<String> nums = CommandHelper.getNumbers(1, 10);

// Filtered by visibility (vanish support)
List<String> visiblePlayers = CommandHelper.getOnlinePlayers(viewingPlayer);

// Filter by partial input (for tab completion)
List<String> filtered = CommandHelper.filter(options, args[0]);
List<String> filteredPlayers = CommandHelper.filterPlayers(player, args[0]);
List<String> filteredWorlds = CommandHelper.filterWorlds(args[0]);

// Register custom completions
CommandHelper.registerCompletion("warps", () -> warpManager.getWarpNames());
CommandHelper.registerCompletion("ranks", () -> Arrays.asList("vip", "mvp", "admin"));

// Use custom completion
Collection<String> warps = CommandHelper.getCompletion("warps");

// Create simple TabCompleter
TabCompleter completer = CommandHelper.playerTabCompleter();
```

### Command Sender Helpers

```java
// Check if sender is player
if (CommandHelper.isPlayer(sender)) {
    Player player = CommandHelper.asPlayer(sender);
}

// Permission check
if (CommandHelper.hasPermission(sender, "myplugin.admin")) {
    // Do admin stuff
}
```

---

## Config API

### ConfigService

A wrapper around YamlConfiguration with automatic caching.

```java
import me.lime.lightCore.api.config.ConfigService;

// Create config service
ConfigService config = ConfigService.create(plugin, "config.yml");
// Or for default config.yml
ConfigService config = ConfigService.create(plugin);

// Get values (cached)
String name = config.getString("name");
int amount = config.getInt("amount", 10);
boolean enabled = config.getBoolean("enabled");
List<String> list = config.getStringList("items");

// Set values
config.set("player.coins", 100);
config.save();

// Reload from disk
config.reload();

// Complex types
Location spawn = config.getLocation("spawn");
ItemStack item = config.getItemStack("reward");
ConfigurationSection section = config.getSection("settings");
```

### ConfigMigrator

Automatically migrate configs when your plugin updates. Preserves user values while adding new options.

```java
import me.lime.lightCore.api.config.ConfigMigrator;

// In your onEnable()
ConfigMigrator.builder(plugin)
    .fileName("config.yml")
    .versionPath("config-version")  // Path to version number in config
    .backupPath("backups")          // Where to store backups
    .makeBackup(true)               // Create backup before migration
    .build()
    .migrate();

// Or simple usage
ConfigMigrator.of(plugin, "config.yml", "config-version").migrate();
```

**Your config.yml should have a version number:**
```yaml
# Don't change this!
config-version: 1

settings:
  enabled: true
  # ... other settings
```

When you release an update, increment the version in your resource config. The migrator will:
1. Detect the version mismatch
2. Create a backup (if enabled)
3. Copy the new config from resources
4. Restore user's custom values

---

**Built with ❤️ by Lime | Clean, lightweight APIs for Spigot/Paper plugins.** 🚀
