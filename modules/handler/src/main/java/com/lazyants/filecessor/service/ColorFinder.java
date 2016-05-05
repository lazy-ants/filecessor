package com.lazyants.filecessor.service;

import de.androidpit.colorthief.ColorThief;
import de.androidpit.colorthief.MMCQ;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class ColorFinder {
    public String[] findColors(BufferedImage img) {
        MMCQ.CMap result = ColorThief.getColorMap(img, 5);
        Set<Color> colors = new HashSet<>();
        for (MMCQ.VBox vbox : result.vboxes) {
            int[] rgb = vbox.avg(true);
            colors.add(new Color(rgb[0], rgb[1], rgb[2]));
        }

        return colors
                .stream()
                .map((Color x) -> "#" + Integer.toHexString(x.getRGB()).substring(2))
                .toArray(String[]::new);
    }

    public Color nearestColor(Color given, Color[] palette) {
        Color nearestColor = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Color paletteColor : palette) {
            double distance = Math.sqrt(
                    Math.pow(given.getRed() - paletteColor.getRed(), 2)
                            + Math.pow(given.getGreen() - paletteColor.getGreen(), 2)
                            + Math.pow(given.getBlue() - paletteColor.getBlue(), 2)
            );

            if (nearestDistance > distance) {
                nearestColor = paletteColor;
                nearestDistance = distance;
            }
        }

        return nearestColor;
    }

    public Color[] getPalette() {
        String[] hexs = new String[]{
                "#d6d6d6", "#afafaf", "#ead5c7", "#f0c0cc", "#ef2d8e", "#f1d5a6", "#c07a66", "#4dbcc5", "#5585d7",
                "#3b60af", "#ffbc3e", "#fbda64", "#fff188", "#a1d88b", "#9065b5", "#d92b31", "#f88f8c", "#ece8b2",
                "#73ae57", "#29d4c7", "#8cf5f8"
        };

        return asList(hexs).stream().map(Color::decode).toArray(Color[]::new);
    }
}
