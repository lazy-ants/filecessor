package com.lazyants.filecessor.service;

import de.androidpit.colorthief.ColorThief;
import de.androidpit.colorthief.MMCQ;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class ColorFinder {

    public static final int REQUIRED_WIDTH = 300;

    public String[] findColors(File img) {
        try {
            return findColors(ImageIO.read(img));
        } catch (IOException e) { /* do nothing */ }

        return new String[0];
    }

    public String[] findColors(BufferedImage img) {
        BufferedImage scaledImg = Scalr.resize(img, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, REQUIRED_WIDTH, img.getHeight() * REQUIRED_WIDTH / img.getWidth());
        MMCQ.CMap result = ColorThief.getColorMap(scaledImg, 5);
        Set<Color> colors = new HashSet<>();
        for (MMCQ.VBox vbox : result.vboxes) {
            int[] rgb = vbox.avg(true);
            colors.add(nearestColor(new Color(rgb[0], rgb[1], rgb[2]), getPalette()));
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
