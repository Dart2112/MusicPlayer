package net.lapismc.musicplayer;

import javazoom.jlgui.basicplayer.*;
import net.lapismc.shuffle.PartialShuffle;
import net.lapismc.shuffle.Shuffleable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class MusicPlayer implements BasicPlayerListener {

    private BasicPlayer player = new BasicPlayer();
    private List<Shuffleable> playlist = new ArrayList<>();
    private float volume = 0.25f;
    private String musicPath = "/home/benjamin/Music/";

    MusicPlayer() {
        player.addBasicPlayerListener(this);
        new Thread(commandRunnable()).start();
        playNextSong();
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
                        case "skip":
                        case "next":
                        case ">":
                            player.stop();
                            playNextSong();
                            break;
                        case "volume":
                        case "v":
                            float i = Float.parseFloat(input.split(" ")[1]);
                            volume = i / 100f;
                            player.setGain(volume);
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
                player.setGain(volume);
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void playNextSong() {
        if (playlist.isEmpty()) {
            repopulatePlaylist();
        }
        Song s = (Song) playlist.get(0);
        if (playlist.size() == 1) {
            new Thread(this::repopulatePlaylist).start();
        }
        System.out.println("Now Playing: " + s.getTitle() + " - " + s.getArtist(false));
        startSongPlayback(s);
        playlist.remove(s);
    }

    private void repopulatePlaylist() {
        System.out.print("Shuffling to get the next 10 songs, skipping now may cause errors \r");
        List<Shuffleable> songList = new ArrayList<>();
        //noinspection ConstantConditions
        for (File f : new File(musicPath).listFiles()) {
            if (f.getName().endsWith("mp3"))
                songList.add(new SongBuilder().fromFile(f).build());
        }
        playlist = new PartialShuffle().shuffle(songList, 20f, 10);
        System.out.print("Shuffle complete, it is now safe to skip \r");
    }

    public void stateUpdated(BasicPlayerEvent event) {
        if (event.getCode() == BasicPlayerEvent.EOM) {
            playNextSong();
        }
    }

    public void opened(Object o, Map map) {

    }

    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
    }

    public void setController(BasicController var1) {

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
