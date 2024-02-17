# Unofficial Bitwarden CLI
Adds support for [rbw](https://github.com/doy/rbw) to fill in passwords.

In order for this plugin to detect your passwords, the *name* (not website) needs to be `Runescape` (including capitalization).

You can list all accounts available to you using `rbw list --fields name,user | grep -E "$(printf '^Runescape\t')"`.

Remember to first log in using `rbw login`.
