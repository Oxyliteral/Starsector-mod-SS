# Starsector-mod-SS

Requires LunaLib.


Adds a custom ship to the game. It can be modified extensively using OP. You can add it to the game using Console Commands, or by starting a new game and selecting the custom option this mod provides.


Adds a menu to LunaLib's additional options menu during refitting.

Allows you to spend Story Points to gain maximum OP, and allows you to spend OP to modify ship stats.

Modifiable through data/config/unlocks.csv.

Weapon mounts cannot be added or removed through code. Hence, these ships have unprecedented amounts of weapon mounts, but using them costs an extreme amount of OP. Though, OP itself cannot be modified through code, so I had to create hullmods for each weapon mount and auto-add them if you use a weapon mount. Plans are to add a ship for each potential type (freighter, destroyer, cruiser, capital).

Dynamically initializes hullmods through use of LunaLib's ReflectionUtil. 
