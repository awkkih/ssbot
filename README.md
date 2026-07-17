# Skin Sprite Bot

A Discord bot that turns Minecraft skins into rendered character sprites.

Upload a skin file or give a Minecraft username, and the bot fetches the skin, validates it, and returns a rendered avatar sprite.

> [!NOTE]
> The original project was built by [マイクラ思考 (mcthnk)](https://x.com/mcthnk). You can find it here: https://sss.1m3.jp/,

## Features

- **File uploads** — send a `.png` skin (64x64 or 128x128, max 1MB) and get a rendered sprite back
- **Username lookup** — provide a Minecraft username and the bot fetches the skin automatically
- **Live status checks** — see whether the external services the bot depends on are online
- Rendered sprites are uploaded to Cloudflare for a shareable download link

## Commands

| Command            | Description                                                                             |
|--------------------|-----------------------------------------------------------------------------------------|
| `/sprite file`     | Upload a `.png` skin and generate a sprite                                              |
| `/sprite username` | Generate a sprite from a Minecraft username                                             |
| `/status`          | Check the status of external services (MCHeads, Skin Sprite Studio, Cloudflare Workers) |
| `/info`            | Usage guide and credits                                                                 |

## Tech Stack

- **Kotlin** + [JDA](https://github.com/discord-jda/JDA) via [JDA-KTX](https://github.com/MinnDevelopment/jda-ktx)
- **Ktor Client** for HTTP calls to external services
- **Coroutines** for async command handling

## External Services

This bot relies on the following external services:

- [MCHeads](https://mcheads.org) — Minecraft skin/avatar rendering
- [Skin Sprite Studio](https://sss.1m3.jp/) — skin-to-sprite conversion
- Cloudflare Workers — file hosting for generated sprites

Service availability can be checked at any time with `/status`.

## Setup

### Prerequisites

- JDK 21
- A Discord bot token ([Discord Developer Portal](https://discord.com/developers/applications))
- A Cloudflare Worker
- A Cloudflare R2 Bucket

### Configuration

Create a `.env` file (or set environment variables) with:

```
DISCORD_TOKEN=your_bot_token_here
WORKER_BASE_URL=https://your-worker.workers.dev
WORKER_UPLOAD_SECRET=your_upload_secret_here
```

## Project Structure

```
src/main/kotlin/dev/akkih/ssbot/
├── command/         # Slash command handlers (/sprite, /status, /info)
├── service/         # External API clients (MCHeads, Skin Sprite Studio, Cloudflare)
├── util/            # Shared utilities (embeds, image helpers, error types)
└── Bot.kt           # Bot entrypoint and shared client instances
```

---

Built by **akkih**!