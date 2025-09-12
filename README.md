# 🧠 Synapse

[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://openjdk.java.net/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0-brightgreen.svg)](https://github.com/MeveraStudios/Synapse/releases)

> A powerful, modular placeholder translation system designed for dynamic text processing across multiple platforms.

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [Usage Examples](#-usage-examples)
- [Platform Support](#-platform-support)
- [Building](#-building)
- [Contributing](#-contributing)
- [License](#-license)

## 🌟 Overview

Synapse is a high-performance, extensible placeholder translation framework that enables dynamic text processing with context-aware placeholder resolution. Built with a clean, modular architecture, Synapse supports multiple platforms while maintaining type safety and excellent performance through intelligent caching mechanisms.

### Key Concepts

- **🧠 Neurons**: Platform-specific placeholder processors that handle text translation
- **👤 Users**: Context providers that supply necessary information for placeholder resolution
- **🏷️ Namespaces**: Organized categorization system for placeholder management
- **⚡ Async Support**: Built-in asynchronous processing capabilities for non-blocking operations

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
- 🔙 **PAPI Backward-Compatibility**: In Bukkit you could just call BukkitNeuron#hookToPAPI and we will do the rest

## 🚀 Getting Started

### Prerequisites

- **Java 21+** (Required)
- **Gradle 8.x** (For building)
- **Your target platform** (e.g., Paper/Spigot for Bukkit)

### Installation

#### For Bukkit/Paper Servers

1. Download the latest `synapse-bukkit-*.*.jar` from [Releases](https://github.com/MeveraStudios/Synapse/releases)
2. Place the JAR in your server's `plugins/` directory
3. Restart your server
4. Configure as needed

#### For Developers (Maven)

```xml
<dependency>
    <groupId>studio.mevera</groupId>
    <artifactId>synapse-core</artifactId>
    <version>1.0</version>
</dependency>
```

#### For Developers (Gradle)

```kotlin
dependencies {
    implementation("studio.mevera:synapse-core:1.0")
    // For Bukkit platform
    implementation("studio.mevera:synapse-bukkit:1.0")
}
```

### Quick Start

```java
// Obtain the Synapse instance for Bukkit
BukkitSynapse synapse = BukkitSynapse.get();

// Register neurons with Synapse
synapse.registerNeuron(new ServerNeuron());
synapse.registerNeuron(new CustomNeuron());

// Synchronous translation with contextual placeholder
String result = synapse.translate("Hello ${player.name}!", player);

// Synchronous translation with static placeholder
String version = synapse.translate("Running Synapse v${server.version}", player);

// Asynchronous translation
synapse.translateAsync("Loading ${server.online} players...", console)
    .thenAccept(console::sendMessage);
```

## 💡 Usage Examples

### Static vs Contextual Placeholders

Static placeholders are registered inside neurons and do not require user context. Contextual placeholders depend on runtime data (like player info) and are also registered inside neurons.

```java
// Define a neuron for static placeholders
public class ServerNeuron extends BukkitNeuron {
    public ServerNeuron() {
        super(Namespace.of("server"));
        register("version", () -> "1.21.6");
    }
}

// Define a neuron for contextual placeholders
public class PlayerNeuron extends BukkitNeuron {
    public PlayerNeuron() {
        super(Namespace.of("player"));
        register("name", ctx -> ctx.user().getName());
    }
}

// Register neurons
synapse.registerNeuron(new ServerNeuron());
synapse.registerNeuron(new PlayerNeuron());

// Usage
String version = synapse.translate("Running Synapse v${server.version}", player);
String welcome = synapse.translate("Welcome ${player.name}!", player);
```

### Basic Placeholder Translation

```java
// Simple placeholder replacement
String welcome = synapse.translate("Welcome ${player.name}!", player);
// Result: "Welcome Steve!"

// Multiple placeholders
String status = synapse.translate("${player.name} has ${player.health} health", player);
// Result: "Steve has 20.0 health"
```

### Custom Neuron Creation

```java
public class CustomNeuron extends BukkitNeuron {
    public CustomNeuron() {
        super(Namespace.of("custom"));
        // Register placeholders
        register("time", this::getCurrentTime);
        register("weather", this::getWeather);
    }
    private String getCurrentTime(Context<BukkitUser> context) {
        return LocalTime.now().toString();
    }
    private String getWeather(Context<BukkitUser> context) {
        World world = context.user().getPlayer().getWorld();
        return world.hasStorm() ? "Stormy" : "Clear";
    }
}
```

### Asynchronous Processing

```java
// Non-blocking placeholder resolution
synapse.translateAsync("Loading ${database.playerCount} players...", console)
    .thenAccept(result -> {
        console.sendMessage(result);
    })
    .exceptionally(throwable -> {
        console.sendMessage("Failed to load data: " + throwable.getMessage());
        return null;
    });
```

### Context-Aware Placeholders

```java
// Placeholders that depend on user context
public class LocationNeuron extends BukkitNeuron {
    public LocationNeuron() {
        super(Namespace.of("location"));
        register("x", ctx -> String.valueOf(ctx.user().getLocation().getX()));
        register("y", ctx -> String.valueOf(ctx.user().getLocation().getY()));
        register("z", ctx -> String.valueOf(ctx.user().getLocation().getZ()));
        register("world", ctx -> ctx.user().getLocation().getWorld().getName());
    }
}

// Usage
String location = synapse.translate("You are at ${location.x}, ${location.y}, ${location.z} in ${location.world}", player);
```

### Relational Placeholders

Relational placeholders allow you to resolve values based on relationships between multiple context objects. For example, you can compare two users, or display information that depends on both a user and a server.

```java
// Define a relational neuron for comparing two users
public class CompareUsersNeuron extends BukkitNeuron {
    public CompareUsersNeuron() {
        super(Namespace.of("compare"));
        registerRelational("isSame", ctx -> ctx.user().getName().equals(ctx.other().getName()) ? "Same user" : "Different users");
    }
}

// Register the neuron
synapse.registerNeuron(new CompareUsersNeuron());

// Usage: compare two players
String result = synapse.translate("Comparison: ${compare.isSame}", player1, player2);
// Result: "Comparison: Different users"
```

### Advanced Placeholder Customization

You can further customize placeholders using the options builder in the `register` method. This allows you to enable async resolution, caching, refresh intervals, and more.

```java
public class CustomNeuron extends BukkitNeuron {
    public CustomNeuron() {
        super(Namespace.of("custom"));
        AtomicInteger i = new AtomicInteger();
        // Static placeholder with async and refresh
        register("meows", () -> "refreshes: " + i.getAndIncrement(),
            options -> options.async(true).delay(500, TimeUnit.MILLISECONDS).refresh(true));

        // Contextual placeholder with caching and TTL
        register("cached", ctx -> UUID.randomUUID().toString(),
            options -> {
                options.cache(true);
                options.cacheTTL(50, TimeUnit.SECONDS);
            });

        // Contextual placeholder with custom argument parsing
        register("arguments", ctx -> {
            String[] args = ctx.arguments();
            return "Arguments(" + args.length + "): " + String.join(", ", args);
        });
    }
}
```

**Features:**
- `.cache(true)`: Caches the resolved value.
- `.cacheTTL(ttl, unit)`: Sets cache time-to-live.
- `.refresh(true)`: Enables periodic refresh of the value.
- `.delay(ms, unit)`: Sets the delay between refreshes.
- `.async(true)`: Resolves the placeholder asynchronously when refreshing.

## 🎮 Platform Support

### Currently Supported

| Platform         | Status | Module             | Version |
|------------------|--------|--------------------|---------|
| **Bukkit/Paper** | ✅ Stable | `synapse-bukkit`   | 1.21.6+ |
| **Bungee**       | ✅ Stable | `synapse-bungee`   | ------- |
| **Velocity**     | ✅ Stable | `synapse-velocity` | ------- |

### Planned Support

| Platform | Status | ETA |
|----------|--------|-----|
| **Fabric** | 📋 Planned | Q2 2026 |
| **Forge** | 📋 Planned | Q2 2026 |

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

### Development Setup

```bash
# Import into your IDE as a Gradle project
# Ensure Java 21+ is configured
# Run tests to verify setup
./gradlew test
```

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

## 📄 License

This project is licensed under a very permissive Attribution License. You may use, modify, and distribute the code for any purpose, but you must mention "Synapse by Mevera Studios" in your documentation, source code, or distribution materials if you copy or use substantial portions of this code.

See the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Thanks to all contributors who have helped shape Synapse
- Inspired by the need for a modern, type-safe placeholder system
- Built with ❤️ by the Mevera Studios team

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
