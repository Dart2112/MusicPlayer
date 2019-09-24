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
    private boolean volumeFading = false;
    private boolean panFading;
    private float volume = 0.25f;
    private float pan = 0.5f;
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
                            player.resume();
                            System.out.println("Resumed playback");
                            break;
                        case "pause":
                            player.pause();
                            System.out.println("Paused playback");
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
                            fade(i / 100f, true);
                            break;
                        case "pan":
                        case "p":
                            float j = Float.parseFloat(input.split(" ")[1]);
                            fade(j / 100f, false);
                            break;
                        case "stop":
                        case "kill":
                            player.stop();
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Command Unknown");
                    }
                } catch (BasicPlayerException | InterruptedException | IllegalArgumentException ignored) {

                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Please enter a valid volume (0-100)");
                }
            }
        };
    }

    private void fade(float target, boolean gain) throws BasicPlayerException, InterruptedException {
        if (gain) {
            target = Math.max(0.01f, Math.min(1.0f, target));
        } else {
            target = Math.max(-1.0f, Math.min(1.0f, target));
        }
        int steps = 25;
        float changeAmount;
        float currentVolume = volume;
        float currentPan = pan;
        if (gain) {
            while (volumeFading) {
                Thread.sleep(100);
            }
            volumeFading = true;
            float difference = target - currentVolume;
            changeAmount = (difference / steps);
        } else {
            while (panFading) {
                Thread.sleep(100);
            }
            panFading = true;
            float difference = target - currentPan;
            changeAmount = (difference / steps);
        }
        for (int i = 0; i < steps; i++) {
            if (gain) {
                currentVolume = currentVolume + changeAmount;
                player.setGain(currentVolume);
            } else {
                currentPan = currentPan + changeAmount;
                player.setPan(currentPan);
            }
            Thread.sleep(2000 / steps);
        }
        if (gain) {
            volume = target;
            player.setGain(target);
            volumeFading = false;
        } else {
            pan = target;
            player.setPan(target);
            panFading = false;
        }
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
        if (playlist.isEmpty()) {
            System.out.println("No Songs Loaded");
            System.exit(0);
        }
        Song s = (Song) playlist.get(0);
        System.out.println("Now Playing: " + s.getTitle() + " - " + s.getArtist(false));
        if (playlist.size() == 1) {
            new Thread(this::repopulatePlaylist).start();
        }
        startSongPlayback(s);
        playlist.remove(s);
    }

    private void repopulatePlaylist() {
        System.out.print("Shuffling to get the next 20 songs, skipping now may cause errors \r");
        List<Shuffleable> songList = new ArrayList<>(addDir(new File(musicPath)));
        playlist = new PartialShuffle().shuffle(songList, 20f, 20);
        System.out.println("Shuffle complete, it is now safe to skip tracks");
    }

    private List<Shuffleable> addDir(File dir) {
        List<Shuffleable> list = new ArrayList<>();
        //noinspection ConstantConditions
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith("mp3"))
                list.add(new SongBuilder().fromFile(f).build());
            if (f.isDirectory())
                list.addAll(addDir(f));
        }
        return list;
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
