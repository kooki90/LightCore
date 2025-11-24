

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

**Built with ❤️ by Lime | No plugin instance, no complicated setup - just clean, beautiful startup messages.** 🚀
