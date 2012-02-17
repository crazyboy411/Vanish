Vanish
========

This is a simple Bukkit plugin that builds on top of Bukkit's Vanish API (as of Craftbukkit 1.1 RB 4).

This plugin will allow privileged users to list vanished players, as well as see them as if they weren't vanished.

It also has a faux logout/login routine that makes it look like a vanished player left the server.


Setting up Vanish
-----------------

Minimal setup. The four permissions are as follows:

	vanish.vanish      | Allows a player to vanish
	vanish.list        | Allows a player to list vanished players
	vanish.seeall      | Allows a player to see vanished players
	vanish.vanishother | Allows a player to set another player as vanished

All of the permissions are already assigned to OPs

Usage
-----

To vanish, type `/vanish`. To see vanished players, type `/vanish list`. To make another player invisible, type `/vanish <playername>`.