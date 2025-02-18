package com.teach.javafxclient.controller.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;import javax.imageio.ImageIO;

public class AuthCodeUtils {
    private final static Random random = new Random();
    private final static int[] fontStyle = { Font.HANGING_BASELINE, Font.ITALIC, Font.LAYOUT_LEFT_TO_RIGHT, Font.LAYOUT_NO_LIMIT_CONTEXT, Font.LAYOUT_NO_START_CONTEXT,
            Font.LAYOUT_RIGHT_TO_LEFT, Font.CENTER_BASELINE, Font.PLAIN, Font.ROMAN_BASELINE, Font.TRUETYPE_FONT, Font.TYPE1_FONT, Font.BOLD };
    private final static String[] font = { "宋体","华文楷体","黑体","微软雅黑" };
    private final static int[] xarr = { 23, 24, 19, 20, 21 };
    private final static int[] yarr = { 20, 21, 22, 23, 24, 25, 16, 17, 18 };
    private final static int[] font_size = { 28, 29, 30, 21, 22, 23, 24, 27, 26, 25 };
    public static void draw(OutputStream out, String value) throws IOException {
        int width = 110, height = 40;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(randColor(240, 255));
        g.fillRect(0, 0, width, height);
        g.drawRect(1, 1, width - 2, height - 2);
        for (int i = 0; i < 10; i++) {
            g.setColor(randColor(150, 250));
            g.drawOval(random.nextInt(110), random.nextInt(24), 5 + random.nextInt(10), 5 + random.nextInt(10));
        }
        g.setFont(getFont());
        g.setColor(randColor(xarr[(int) (Math.random() * 5)], 254));
        for (int i = 0, len = value.length(); i < len; i++) {
            String rand = String.valueOf(value.charAt(i));
            int degree = random.nextInt(23);
            if (i % 2 == 0) {
                degree = degree * (-1);
            }
            int x = xarr[(int) (Math.random() * 5)] * i, y = yarr[(int) (Math.random() * 8)];
            g.rotate(Math.toRadians(degree), x, y);
            g.setColor(randColor(48, 254));
            g.drawString(rand, x + 8, y + 10);
            g.rotate(-Math.toRadians(degree), x, y);
        }
        g.setColor(randColor(0, 200));
        BasicStroke bs = new BasicStroke(2);
        g.setStroke(bs);
        QuadCurve2D.Double curve = new QuadCurve2D.Double(0d, random.nextInt(height - 8) + 4, width / 2, height / 2, width, random.nextInt(height - 8) + 4);
        g.draw(curve);
        g.dispose();
        ImageIO.write(bi, "png", out);
    }
    private static Font getFont() {
        return new Font(font[(int) (Math.random() * 4)], fontStyle[(int) (Math.random() * 12)], font_size[(int) (Math.random() * 10)]);
    }
    private static Color randColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
    private static char[] c = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '3', '5', '6', '7', '8', '9' };
    public static String getRandom(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(c[Math.abs(random.nextInt()) % c.length]);
        }
        return sb.toString();
    }
    public static void main(String[] args) throws IOException {
        for (int i = 1; i < 2; i++) {
            FileOutputStream out = new FileOutputStream("是你存放图片的地址"+ i +".png");
            draw(out, getRandom(4));
        }
    }
}

