package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Reusable image upload and management field for administrative forms and profile editing.
 * Supports selecting, previewing, and removing images.
 */
public class ImagePickerField extends JPanel {

    private String currentImagePath = "";
    private final JLabel previewLabel = new JLabel("No image attached (optional)");
    private final JLabel statusBadge = new JLabel();
    private final ModernButton uploadBtn = new ModernButton("Upload Image...", ModernButton.Variant.SECONDARY);
    private final ModernButton removeBtn = new ModernButton("Remove Image", ModernButton.Variant.DANGER);

    public ImagePickerField() {
        this("");
    }

    public ImagePickerField(String initialPath) {
        setLayout(new BorderLayout(10, 8));
        setOpaque(false);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        controls.setOpaque(false);

        uploadBtn.setCornerRadius(6);
        uploadBtn.addActionListener(e -> chooseImage());
        controls.add(uploadBtn);

        removeBtn.setCornerRadius(6);
        removeBtn.addActionListener(e -> clearImage());
        controls.add(removeBtn);

        add(controls, BorderLayout.WEST);

        JPanel previewBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        previewBox.setOpaque(false);
        previewLabel.setFont(AppTheme.SMALL);
        previewLabel.setForeground(AppTheme.MUTED);

        statusBadge.setFont(AppTheme.SMALL.deriveFont(java.awt.Font.BOLD));
        statusBadge.setVisible(false);

        previewBox.add(previewLabel);
        previewBox.add(statusBadge);
        add(previewBox, BorderLayout.CENTER);

        setImagePath(initialPath);
    }

    public String getImagePath() {
        return currentImagePath != null ? currentImagePath.trim() : "";
    }

    public boolean hasImage() {
        return currentImagePath != null && !currentImagePath.trim().isEmpty();
    }

    public void setImagePath(String path) {
        this.currentImagePath = path != null ? path.trim() : "";
        refreshPreview();
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Image (PNG, JPG, JPEG, GIF, WEBP)");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files (*.png, *.jpg, *.jpeg, *.gif, *.webp)",
                "png", "jpg", "jpeg", "gif", "webp");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            if (selected != null && selected.exists()) {
                if (ImageUtil.isValidImage(selected.getAbsolutePath())) {
                    setImagePath(selected.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "The selected file could not be loaded as a valid image. Please select a different image file.",
                            "Invalid Image File", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public void clearImage() {
        this.currentImagePath = "";
        refreshPreview();
    }

    private void refreshPreview() {
        if (hasImage()) {
            BufferedImage img = ImageUtil.loadImage(currentImagePath);
            if (img != null) {
                // Show thumbnail
                ImageIcon thumb = ImageUtil.createAvatarIcon(currentImagePath, 36);
                previewLabel.setIcon(thumb);
                File f = new File(currentImagePath);
                previewLabel.setText("Attached: " + f.getName());
                previewLabel.setForeground(AppTheme.TEXT);

                statusBadge.setText("✓ Ready");
                statusBadge.setForeground(AppTheme.SUCCESS);
                statusBadge.setVisible(true);

                removeBtn.setEnabled(true);
                removeBtn.setVisible(true);
                return;
            }
        }

        // No image or invalid image
        previewLabel.setIcon(null);
        previewLabel.setText("No image attached (optional)");
        previewLabel.setForeground(AppTheme.MUTED);
        statusBadge.setVisible(false);
        removeBtn.setEnabled(false);
    }
}
