package com.example.istream;

import com.example.istream.util.YoutubeUrlUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class YoutubeUrlUtilsTest {

    @Test
    public void extractVideoId_fromWatchUrl_returnsId() {
        String id = YoutubeUrlUtils.extractVideoId("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        assertEquals("dQw4w9WgXcQ", id);
    }

    @Test
    public void extractVideoId_fromShortUrl_returnsId() {
        String id = YoutubeUrlUtils.extractVideoId("https://youtu.be/dQw4w9WgXcQ");
        assertEquals("dQw4w9WgXcQ", id);
    }

    @Test
    public void extractVideoId_fromInvalidUrl_returnsNull() {
        String id = YoutubeUrlUtils.extractVideoId("https://example.com/video");
        assertNull(id);
    }
}

