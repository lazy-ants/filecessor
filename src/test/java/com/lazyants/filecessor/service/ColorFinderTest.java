package com.lazyants.filecessor.service;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class ColorFinderTest {
    @Test
    public void test_nearestColor() {
        ColorFinder finder = new ColorFinder();

        assertEquals(finder.nearestColor(new Color(51, 51, 51), new Color[]{Color.BLACK, Color.DARK_GRAY, Color.BLUE, Color.WHITE, Color.PINK}), Color.DARK_GRAY);

        assertEquals(finder.nearestColor(new Color(41, 41, 175), new Color[]{Color.BLACK, Color.DARK_GRAY, Color.BLUE, Color.WHITE, Color.PINK}), Color.BLUE);

        assertEquals(finder.nearestColor(new Color(214, 151, 151), new Color[]{Color.BLACK, Color.DARK_GRAY, Color.BLUE, Color.WHITE, Color.PINK}), Color.PINK);
    }
}
