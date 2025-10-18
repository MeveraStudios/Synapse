![](assets/banner.png)
# 🧠 Synapse

[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://openjdk.java.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-Attribution-green.svg)](LICENSE)
[![Release](https://img.shields.io/github/v/release/MeveraStudios/Synapse)](https://github.com/MeveraStudios/Synapse/releases)

> A powerful, modular placeholder translation system designed for dynamic text processing across multiple platforms.

## 📋 Table of Contents

- [Why Synapse?](#-why-synapse)
- [Overview](#-overview)
- [Features](#-features)
- [Documentation](https://docs.mevera.studio/Synapse/)
- [Getting Started](#-getting-started)
- [Platform Support](#-platform-support)
- [Building](#-building)
- [Contributing](#-contributing)
- [License](#-license)

---
## 🤔 Why Synapse?

Compared to traditional placeholder systems:

- ✨ **Modern API**: Built with Java 21+ features and best practices
- 🎯 **Type-Safe**: No runtime surprises with strongly-typed generics
- ⚡ **Performance**: Smart caching and async-first design
- 🔌 **Multi-Platform**: One API works across Bukkit, Bungee, and Velocity
- 🧪 **Well-Tested**: Comprehensive test suite for reliability

---
## 🌟 Overview

Synapse is a high-performance, extensible placeholder translation framework that enables dynamic text processing with context-aware placeholder resolution. Built with a clean, modular architecture, Synapse supports multiple platforms while maintaining type safety and excellent performance through intelligent caching mechanisms.

### Key Concepts

- **🧠 Neurons**: Platform-specific placeholder processors that handle text translation
- **👤 Users**: Context providers that supply necessary information for placeholder resolution
- **🏷️ Namespaces**: Organized categorization system for placeholder management
- **⚡ Async Support**: Built-in asynchronous processing capabilities for non-blocking operations

---
## ✨ Features

### Core Features
- 🚀 **High Performance**: Optimized placeholder resolution with intelligent caching
- 🔧 **Modular Architecture**: Clean separation between core logic and platform implementations
- 🎯 **Type Safety**: Strongly typed generics ensure compile-time safety
- ⚡ **Async Processing**: Non-blocking placeholder resolution with CompletableFuture support
- 🏗️ **Extensible Design**: Easy to add new platforms and custom neurons
- 📝 **Rich API**: Comprehensive API for both synchronous and asynchronous operations
- 🏷️ **Namespace Management**: Organized placeholder categorization and conflict prevention
- 🔄 **Context-Aware Resolution**: Placeholders resolved based on user context and environment
### Advanced Features
- 🔗 **Relational Placeholders**: Placeholders that resolve values based on relationships between 2 Users
- 💾 **Intelligent Caching**: Built-in caching mechanisms with expiration support
- 🧪 **Comprehensive Testing**: Extensive test suite ensuring reliability
- 🔙 **PAPI Backward-Compatibility**: In Bukkit, you can simply call BukkitNeuron#hookToPAPI and we will do the rest
- 🎨 **Adventure Integration**: First-class MiniMessage support with custom tag resolvers

---
## 🚀 Getting Started

### Prerequisites

- **Java 21+** (Required)
- **Gradle 8.x** (For building)
- **Your target platform** (e.g., Paper/Spigot for Bukkit)

---
### Installation

#### For Bukkit/Bungee/Velocity Servers

1. Download the latest `synapse-PLATFORM-*.*.jar` from [Releases](https://github.com/MeveraStudios/Synapse/releases)
2. Place the JAR in your server's `plugins/` directory
3. Restart your server
4. Configure as needed
---
### For Developers
#### 📦 Adding to Your Project

**Gradle (Kotlin DSL)**
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    compileOnly("studio.mevera:synapse-bukkit:VERSION") // Replace VERSION
}
```

**Gradle (Groovy)**
```groovy
repositories {
    mavenCentral()
}

dependencies {
    compileOnly 'studio.mevera:synapse-bukkit:VERSION' // Replace VERSION
}
```

**Maven**
```xml
<dependency>
    <groupId>studio.mevera</groupId>
    <artifactId>synapse-bukkit</artifactId>
    <version>VERSION</version> <!-- Replace VERSION -->
    <scope>provided</scope>
</dependency>
```

> 💡 **Note**: Use `compileOnly`/`provided` scope since Synapse is already on the server. Replace `VERSION` with the [latest release](https://github.com/MeveraStudios/Synapse/releases).
#### 💻 Quick Example
```java
// Create a custom neuron
public class MyNeuron extends BukkitNeuron {
    public MyNeuron(Plugin plugin) {
        super(plugin, Namespace.of("custom"));
        
        // Register placeholders
        register("hello", "Hello World!");
        register("player_name", context -> context.user().getName());
    }
}

// Register and use
@Override
public void onEnable() {
    new MyNeuron(this).register();
    
    BukkitSynapse synapse = BukkitSynapse.get();
    String message = synapse.translate("${custom.hello}, ${custom.player_name}!", player);
    player.sendMessage(message);
}
```
Check out the [Documentation](https://docs.mevera.studio/Synapse/) for detailed guides on integrating Synapse into your projects.

---
## 🎮 Platform Support

### Currently Supported

| Platform         | Status   | Module             | Version |
|------------------|----------|--------------------|---------|
| **Bukkit/Paper** | ✅ Stable | `synapse-bukkit`   | 1.8.8+  |
| **Bungee**       | ✅ Stable | `synapse-bungee`   | Latest  |
| **Velocity**     | ✅ Stable | `synapse-velocity` | 3.0+    |

### Planned Support

| Platform   | Status     | ETA     |
|------------|------------|---------|
| **Fabric** | 📋 Planned | Q2 2026 |
| **Forge**  | 📋 Planned | Q2 2026 |

---
## 🔧 Building

### Prerequisites

- Java 21 or higher
- Git

### Build Steps

```bash
# Clone the repository
git clone https://github.com/MeveraStudios/Synapse.git
cd Synapse

# Build all modules
./gradlew build

# Run tests
./gradlew test

# Generate JARs
./gradlew shadowJar
```

### Build Outputs

- `synapse-core/build/libs/synapse-core-x.x.jar` - Core library
- `synapse-platform/bukkit/build/libs/synapse-bukkit-x.x.jar` - Bukkit plugin
- `synapse-platform/bungee/build/libs/synapse-bungee-x.x.jar` - Bungee plugin
- `synapse-platform/velocity/build/libs/synapse-velocity-x.x.jar` - Velocity plugin

### Development Setup

```bash
# Import into your IDE as a Gradle project
# Ensure Java 21+ is configured
# Run tests to verify setup
./gradlew test
```

---
## 🤝 Contributing

We welcome contributions to Synapse! Here's how you can help:

### Getting Started

1. **Fork** the repository
2. **Clone** your fork locally
3. **Create** a feature branch: `git checkout -b feature/amazing-feature`
4. **Make** your changes
5. **Test** thoroughly: `./gradlew test`
6. **Commit** your changes: `git commit -m 'Add amazing feature'`
7. **Push** to your branch: `git push origin feature/amazing-feature`
8. **Open** a Pull Request

### Contribution Guidelines

- ✅ Follow existing code style and conventions
- ✅ Add tests for new functionality
- ✅ Update documentation as needed
- ✅ Ensure all tests pass
- ✅ Write clear, descriptive commit messages

### Areas We Need Help

- 🔌 **New Platform Support**: Fabric, Forge implementations
- 📝 **Documentation**: Improving guides and examples
- 🧪 **Testing**: Expanding test coverage
- 🐛 **Bug Reports**: Finding and reporting issues
- 💡 **Feature Suggestions**: New ideas and improvements

---
## 📄 License

This project is licensed under a very permissive Attribution License. You may use, modify, and distribute the code for any purpose, but you must mention "Synapse by Mevera Studios" in your documentation, source code, or distribution materials if you copy or use substantial portions of this code.

See the [LICENSE](LICENSE) file for details.

---
## 🙏 Acknowledgments

- Thanks to all contributors who have helped shape Synapse
- Inspired by the need for a modern, type-safe placeholder system
- Built with ❤️ by the Mevera Studios team

---
## 📞 Support

- 🐛 **Bug Reports**: [Create an issue](https://github.com/MeveraStudios/Synapse/issues)
- 💡 **Feature Requests**: [Create an issue](https://github.com/MeveraStudios/Synapse/issues)
- 💬 **Discussion**: [GitHub Discussions](https://github.com/MeveraStudios/Synapse/discussions)
- 📧 **Email**: support@mevera.studio

---

<div align="center">

**Made with ❤️ by [iiAhmedYT](https://github.com/iiAhmedYT) and the Mevera Studios team**

[⭐ Star this repo](https://github.com/MeveraStudios/Synapse) • [🐛 Report Bug](https://github.com/MeveraStudios/Synapse/issues) • [💡 Request Feature](https://github.com/MeveraStudios/Synapse/issues)

</div>
