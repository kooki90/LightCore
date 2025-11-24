# Changelog

All notable changes to LightCore will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.1] - 2025-11-24

### Added
- **Built-in A-Z ASCII art generator** - Automatic text-to-ASCII conversion
- **Static API methods** - No plugin instance needed (Vault-style API)
- **Three startup message methods**:
  - `StartupMessage.print()` - Simple colored message
  - `StartupMessage.printWithAscii()` - Auto-generated ASCII art
  - `StartupMessage.printWithCustomAscii()` - Custom ASCII art support
- **Hex color support** - Full RGB colors for Minecraft 1.16+
- **AsciiArt utility class** - Direct ASCII generation for custom use
- **Complete documentation** - README, Integration Guide, Publish Guide
- **GitHub Actions workflow** - Automatic building on push
- **JitPack configuration** - Professional dependency management

### Features
- Complete A-Z ASCII letter font (uppercase)
- Space character support for multi-word messages
- Automatic color formatting with `&#RRGGBB` hex codes
- Clean console output with proper spacing
- Zero dependencies (uses only Bukkit/Paper API)

### Technical
- Java 21 support
- Paper API 1.21-R0.1-SNAPSHOT
- Maven build system with shade plugin
- Professional project structure

---

## [1.0.0] - 2025-11-24

### Initial Release
- Basic plugin structure
- Core startup message functionality

---

## Upcoming

### Planned for v1.1.0
- [ ] Configurable ASCII art fonts
- [ ] Gradient color support
- [ ] Animation effects
- [ ] Shutdown message support
- [ ] Multi-line text wrapping
- [ ] Custom color schemes

### Planned for v2.0.0
- [ ] Additional ASCII fonts (small, big, fancy)
- [ ] Symbol and number support
- [ ] Formatting codes (bold, italic, etc.)
- [ ] Message templates
- [ ] Configuration file support

---

[1.0.1]: https://github.com/YourUsername/LightCore/releases/tag/v1.0.1
[1.0.0]: https://github.com/YourUsername/LightCore/releases/tag/v1.0.0
