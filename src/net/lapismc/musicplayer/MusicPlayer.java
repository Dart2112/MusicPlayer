package net.lapismc.musicplayer;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.util.Scanner;

class MusicPlayer {

    private BasicPlayer player = new BasicPlayer();
    private String songPath = "/home/benjamin/Music/7 Minutes - Dean Lewis.mp3";

    MusicPlayer() {
        new Thread(commandRunnable()).start();
        //TODO: load and shuffle songs
        startSongPlayback(new SongBuilder().atPath(songPath).build());
    }

    private Runnable commandRunnable() {
        return () -> {
            Scanner scan = new Scanner(System.in);
            while (true) {
                String input = scan.nextLine();
                try {
                    switch (input.split(" ")[0]) {
                        case "play":
                            player.play();
                            break;
                        case "pause":
                            player.pause();
                            break;
                        case "kill":
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Command Unknown");
                    }
                } catch (BasicPlayerException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void startSongPlayback(Song song) {
        new Thread(() -> {
            try {
                player.open(song.getFile());
                player.play();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /*TODO
Features I need to have:

Short word and symbol controls from the command line. e.g. start, stop, next, last, previous, skip, >, <
Media buttons on most if not all platforms (Perhaps with a setup option that allows you to bind buttons)
Features I would like to have:

Simple search and play funtionallity, as in "search Love" would print a list of songs with "love" in their metadata, a number could then be used to add these songs to the current playlist. Sort of like how yay deals with package search (Like stupidly simple)
Low resourse usage (maybe store the current play list in a file instead of memory, this could also be the start of a playlist feature to load only certain songs)
Be able to deal with thousands of songs
Maybe a playlist feature (This is more unlikly but would be nice)
A robust system that allows songs to move, maybe an ID made from the song title and file size. This would allow artists to be changed without breaking playlists
     */

}
