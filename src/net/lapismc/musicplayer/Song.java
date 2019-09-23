package net.lapismc.musicplayer;

import net.lapismc.shuffle.SongFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class Song extends SongFile {

    public Song(File f) throws ReadOnlyFileException, IOException, TagException, InvalidAudioFrameException, CannotReadException {
        super(f);
    }

}
