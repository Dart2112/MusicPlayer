package net.lapismc.musicplayer;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class SongBuilder {

    Song s;

    public SongBuilder atPath(String path) {
        return fromFile(new File(path));
    }

    public SongBuilder fromFile(File f) {
        try {
            s = new Song(f);
        } catch (ReadOnlyFileException | IOException | TagException | InvalidAudioFrameException | CannotReadException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Song build() {
        return s;
    }

}
