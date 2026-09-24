# FAQ

### Q: Why can't I save files?
A: Glassnote was intended to be a private and offline journaling/sticky notes app. Glassnotes are meant to be "fragile" (like glass) and as such there is no save function built into it. If you wish to save the glassnotes you have written on, then you can copy and paste them in an app like Obsidian.

### Q: Are there any keyboard shortcuts?
A: Yes
| Action | Keycode |
|  ------ | ------ |
| New Tab | CTRL+T |
| Close Tab | CTRL+W |
| Find | CTRL+F |
| Zoom In | CTRL+= |
| Zoom Out | CTRL+- |
| Bold (highlight, supported fonts only) | CTRL+B |
| Italic (highlight, supported fonts only) | CTRL+I |
| Underline (highlight, supported fonts only) | CTRL+U |
| Strikethrough (highlight, supported fonts only) | Alt+Shift+X |

### Q: How do I change the appearance?
A: On the top menu click "Settings" then click "Config". If this does not work you can modify the config.properties file in a text editor. (C:\Users\{User}\AppData\Local\Glassnote\config.properties)

### Q: What command is run when "disable wifi on startup" is enabled Why does this exist as an option?
A: "netsh wlan disconnect" is ran. This may or may not work depending on your specific Wi-Fi configuration. This option exists to prevent remote monitoring when journaling about personal subjects. It's basically like a "diary" mode for extra privacy.

⚠ This will disconnect you from your Wi-Fi interface when the app starts up. Simply reconnect to your Wi-Fi interface to undo this.

### Q: What font files are supported?
A: .ttf .woff .woff2 .otf
