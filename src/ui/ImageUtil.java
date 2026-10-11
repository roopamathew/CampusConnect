package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * Utility for safe, resilient image loading and conditional rendering.
 * Enforces the strict rule:
 * - If a valid image exists: renders smoothly with high-quality scaling.
 * - If no image or loading fails: returns null and renders NOTHING (zero reserved space).
 */
public class ImageUtil {

    /**
     * Safely load an image from a filesystem path or classpath resource.
     * Returns null on any failure without throwing exceptions or showing broken placeholders.
     */
    public static BufferedImage loadImage(String path) {
        if (path == null) {
            return null;
        }
        String clean = path.trim();
        if (clean.isEmpty()) {
            return null;
        }

        // 1. Try direct file path
        try {
            File file = new File(clean);
            if (file.exists() && file.isFile() && file.canRead()) {
                BufferedImage img = ImageIO.read(file);
                if (img != null && img.getWidth() > 0 && img.getHeight() > 0) {
                    return img;
                }
            }
        } catch (Throwable ignored) {
        }

        // 2. Try classpath resource
        try {
            InputStream in = ImageUtil.class.getResourceAsStream(clean.startsWith("/") ? clean : "/" + clean);
            if (in != null) {
                try {
                    BufferedImage img = ImageIO.read(in);
                    if (img != null && img.getWidth() > 0 && img.getHeight() > 0) {
                        return img;
                    }
                } finally {
                    in.close();
                }
            }
        } catch (Throwable ignored) {
        }

        return null;
    }

    /**
     * Check whether the path points to a genuinely loadable image.
     */
    public static boolean isValidImage(String path) {
        return loadImage(path) != null;
    }

    /**
     * Creates a conditional image component.
     * Returns null if path is invalid or loading fails, allowing the caller to completely
     * omit it from the layout without leaving empty space or gaps.
     */
    public static JComponent createImageViewer(String path, final int maxW, final int maxH, final int cornerRadius) {
        final BufferedImage img = loadImage(path);
        if (img == null) {
            return null;
        }

        return new JComponent() {
            {
                setOpaque(false);
                int w = Math.min(img.getWidth(), maxW);
                int h = Math.min(img.getHeight(), maxH);
                // Maintain aspect ratio within bounds
                double aspect = (double) img.getWidth() / (double) img.getHeight();
                if (w / aspect <= maxH) {
                    h = (int) Math.round(w / aspect);
                } else {
                    w = (int) Math.round(maxH * aspect);
                    h = maxH;
                }
                Dimension dim = new Dimension(w, h);
                setPreferredSize(dim);
                setMinimumSize(new Dimension(60, 40));
                setMaximumSize(new Dimension(maxW, maxH));
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int w = getWidth();
                int h = getHeight();

                if (cornerRadius > 0) {
                    g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
                }

                // Scale image to fill component area
                g2.drawImage(img, 0, 0, w, h, null);

                // Subtle inner border
                if (cornerRadius > 0) {
                    g2.setClip(null);
                    g2.setColor(AppTheme.isDarkMode ? AppTheme.DARK_BORDER : AppTheme.LIGHT_BORDER);
                    g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
                }

                g2.dispose();
            }
        };
    }

    /**
     * Creates a conditional circular or rounded avatar icon if the image exists and loads,
     * otherwise returns null so the caller can fall back to InitialsAvatar.
     */
    public static ImageIcon createAvatarIcon(String path, int size) {
        BufferedImage img = loadImage(path);
        if (img == null) {
            return null;
        }

        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, size, size, null);
        g2.dispose();

        return new ImageIcon(scaled);
    }
}
