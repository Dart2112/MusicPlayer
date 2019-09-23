# MusicPlayer

A simple java music player, used to implement my shuffle algorithm

The idea of this music player is to have all of your music in a folder or in nested folders. The player will then load, shuffle then play the music. 

It is intended to be simple to start and require little to no user interaction. But I have some more power user kind of ideas that I might implement over time

Features I need to have:
- Short word and symbol controls from the command line. e.g. start, stop, next, last, previous, skip, >, <
- Media buttons on most if not all platforms (Perhaps with a setup option that allows you to bind buttons)

Features I would like to have:
- Simple search and play funtionallity, as in "search Love" would print a list of songs with "love" in their metadata, a number could then be used to add these songs to the current playlist. Sort of like how yay deals with package search (Like stupidly simple)
- Low resourse usage (maybe store the current play list in a file instead of memory, this could also be the start of a playlist feature to load only certain songs)
- Be able to deal with thousands of songs
- Maybe a playlist feature (This is more unlikly but would be nice)
- A robust system that allows songs to move, maybe an ID made from the song title and file size. This would allow artists to be changed without breaking playlists

Notes:
https://stackoverflow.com/a/22305518
