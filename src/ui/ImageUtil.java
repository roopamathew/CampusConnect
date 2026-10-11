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
 * Utility for loading and displaying images in the Swing UI.
 * Invalid or missing images return null so callers can omit the image component.
 */
public class ImageUtil {

    /**
     * Loads an image from a filesystem path or classpath resource.
     * Relative filesystem paths are resolved from the application's working directory.
     */
    public static BufferedImage loadImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            return null;
        }

        String clean = path.trim();

        // 1. Try the path as supplied (absolute or relative to the working directory).
        try {
            File file = new File(clean);
            if (file.isFile() && file.canRead()) {
                BufferedImage image = ImageIO.read(file);
                if (isUsable(image)) {
                    return image;
                }
            }
        } catch (Exception ignored) {
            // Try classpath locations below.
        }

        // 2. Try as a classpath resource.
        String resource = clean.replace('\\', '/');
        while (resource.startsWith("./")) {
            resource = resource.substring(2);
        }
        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }

        try (InputStream in = ImageUtil.class.getResourceAsStream(resource)) {
            if (in != null) {
                BufferedImage image = ImageIO.read(in);
                if (isUsable(image)) {
                    return image;
                }
            }
        } catch (Exception ignored) {
            // Try the context class loader below.
        }

        // 3. Try the thread context class loader (useful when resources are packaged).
        try {
            String classLoaderPath = resource.startsWith("/") ? resource.substring(1) : resource;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                try (InputStream in = loader.getResourceAsStream(classLoaderPath)) {
                    if (in != null) {
                        BufferedImage image = ImageIO.read(in);
                        if (isUsable(image)) {
                            return image;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Return null if the image cannot be loaded.
        }

        return null;
    }

    private static boolean isUsable(BufferedImage image) {
        return image != null && image.getWidth() > 0 && image.getHeight() > 0;
    }

    public static boolean isValidImage(String path) {
        return loadImage(path) != null;
    }

    /**
     * Creates a rounded image component. Returns null if the image cannot be loaded.
     * The image is scaled proportionally and centered, so it is not stretched.
     */
    public static JComponent createImageViewer(
            String path, final int maxW, final int maxH, final int cornerRadius) {
        final BufferedImage image = loadImage(path);
        if (image == null || maxW <= 0 || maxH <= 0) {
            return null;
        }

        final double scale = Math.min(
                (double) maxW / image.getWidth(),
                (double) maxH / image.getHeight());
        final int displayW = Math.max(1, (int) Math.round(image.getWidth() * scale));
        final int displayH = Math.max(1, (int) Math.round(image.getHeight() * scale));

        return new JComponent() {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(displayW, displayH));
                setMinimumSize(new Dimension(Math.min(60, displayW), Math.min(40, displayH)));
                setMaximumSize(new Dimension(maxW, maxH));
            }

            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                int componentW = getWidth();
                int componentH = getHeight();
                if (componentW <= 0 || componentH <= 0) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) graphics.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);

                    if (cornerRadius > 0) {
                        g2.setClip(new RoundRectangle2D.Float(
                                0, 0, componentW, componentH,
                                cornerRadius, cornerRadius));
                    }

                    // Fit the full image inside the component without distortion.
                    double paintScale = Math.min(
                            (double) componentW / image.getWidth(),
                            (double) componentH / image.getHeight());
                    int paintW = Math.max(1, (int) Math.round(image.getWidth() * paintScale));
                    int paintH = Math.max(1, (int) Math.round(image.getHeight() * paintScale));
                    int x = (componentW - paintW) / 2;
                    int y = (componentH - paintH) / 2;
                    g2.drawImage(image, x, y, paintW, paintH, null);

                    if (cornerRadius > 0) {
                        g2.setClip(null);
                        g2.setColor(AppTheme.isDarkMode
                                ? AppTheme.DARK_BORDER : AppTheme.LIGHT_BORDER);
                        g2.drawRoundRect(0, 0, componentW - 1, componentH - 1,
                                cornerRadius, cornerRadius);
                    }
                } finally {
                    g2.dispose();
                }
            }
        };
    }

    /**
     * Creates a circular avatar icon or returns null if the image is missing.
     */
    public static ImageIcon createAvatarIcon(String path, int size) {
        BufferedImage image = loadImage(path);
        if (image == null || size <= 0) {
            return null;
        }

        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(image, 0, 0, size, size, null);
        } finally {
            g2.dispose();
        }

        return new ImageIcon(scaled);
    }
}
