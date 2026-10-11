package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Reusable searchable autocomplete combo box for CampusConnect.
 * FREE_TEXT mode allows custom values; STRICT mode validates against allowed options.
 */
public class AutoCompleteComboBox extends JComboBox<String> {

    public enum Mode { FREE_TEXT, STRICT }

    private String[] allItems;
    private final Mode mode;
    private boolean updating = false;
    private JTextComponent editor;

    public AutoCompleteComboBox(String[] items, Mode mode) {
        super(items);
        this.allItems = items.clone();
        this.mode = mode;
        setEditable(true);
        AppTheme.styleComboBox(this);

        // Grab the internal text editor
        Component editorComp = getEditor().getEditorComponent();
        if (editorComp instanceof JTextComponent) {
            editor = (JTextComponent) editorComp;
            editor.setFont(AppTheme.BODY);
            editor.setForeground(AppTheme.TEXT);
            editor.setBackground(AppTheme.isDarkMode ? new Color(18, 24, 38) : Color.WHITE);
        }

        if (editor != null) {
            // Filter suggestions on every keystroke
            editor.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { onTextChanged(); }
                public void removeUpdate(DocumentEvent e) { onTextChanged(); }
                public void changedUpdate(DocumentEvent e) { onTextChanged(); }
            });

            // Keyboard: Up/Down to navigate, Enter to accept, Escape to close
            editor.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_DOWN) {
                        if (!isPopupVisible()) {
                            showPopup();
                        }
                        int idx = getSelectedIndex();
                        if (idx < getItemCount() - 1) {
                            e.consume();
                            setSelectedIndex(idx + 1);
                        }
                    } else if (key == KeyEvent.VK_UP) {
                        int idx = getSelectedIndex();
                        if (idx > 0) {
                            e.consume();
                            setSelectedIndex(idx - 1);
                        }
                    } else if (key == KeyEvent.VK_ENTER) {
                        if (isPopupVisible()) {
                            // Accept current highlighted item
                            Object sel = getSelectedItem();
                            if (sel != null) {
                                updating = true;
                                editor.setText(sel.toString());
                                editor.setCaretPosition(sel.toString().length());
                                updating = false;
                            }
                            hidePopup();
                            e.consume();
                        }
                        // Fire action event for Enter key submission
                        fireActionEvent();
                    } else if (key == KeyEvent.VK_ESCAPE) {
                        hidePopup();
                        e.consume();
                    }
                }
            });

            // STRICT mode: on focus lost, revert to nearest valid item
            if (mode == Mode.STRICT) {
                editor.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        validateStrictValue();
                    }
                });
            }
        }

        // When a list item is selected by mouse click or keyboard, update editor text
        addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && !updating) {
                SwingUtilities.invokeLater(() -> {
                    Object sel = getSelectedItem();
                    if (editor != null && sel != null) {
                        updating = true;
                        String text = sel.toString();
                        editor.setText(text);
                        editor.setCaretPosition(text.length());
                        updating = false;
                    }
                });
            }
        });
    }

    private void onTextChanged() {
        if (updating) return;
        SwingUtilities.invokeLater(() -> {
            if (updating) return;
            updating = true;
            String typed = (editor != null) ? editor.getText() : "";
            filterSuggestions(typed);
            if (editor != null) {
                int pos = Math.min(typed.length(), editor.getDocument().getLength());
                editor.setCaretPosition(pos);
            }
            updating = false;
        });
    }

    private void filterSuggestions(String typed) {
        String lower = typed.toLowerCase();
        List<String> matches = new ArrayList<String>();
        for (String item : allItems) {
            if (typed.isEmpty() || item.toLowerCase().contains(lower)) {
                matches.add(item);
            }
        }
        DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<String>();
        for (String m : matches) {
            newModel.addElement(m);
        }
        setModel(newModel);
        // Restore typed text (model update clears it)
        if (editor != null) {
            editor.setText(typed);
        }
        if (!matches.isEmpty() && !typed.isEmpty()) {
            if (!isPopupVisible() && isShowing()) {
                showPopup();
            }
        } else {
            hidePopup();
        }
    }

    private void validateStrictValue() {
        if (editor == null) return;
        String typed = editor.getText().trim();
        for (String item : allItems) {
            if (item.equalsIgnoreCase(typed)) {
                updating = true;
                setSelectedItem(item);
                editor.setText(item);
                updating = false;
                return;
            }
        }
        // Not a valid value — revert to first item
        if (allItems != null && allItems.length > 0) {
            updating = true;
            setSelectedItem(allItems[0]);
            editor.setText(allItems[0]);
            updating = false;
        }
    }

    /** Get the current typed/selected value. */
    public String getValue() {
        if (editor != null) return editor.getText().trim();
        Object sel = getSelectedItem();
        return sel != null ? sel.toString().trim() : "";
    }

    /** Set the value programmatically without triggering filter. */
    public void setValue(String value) {
        updating = true;
        if (value != null) {
            setSelectedItem(value);
            if (editor != null) editor.setText(value);
        }
        updating = false;
    }

    /** Dynamically replace the suggestions list and repopulate the combo box. */
    public void setItems(String[] newItems) {
        updating = true;
        this.allItems = (newItems != null) ? newItems.clone() : new String[0];
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
        for (String item : this.allItems) {
            model.addElement(item);
        }
        setModel(model);
        if (this.allItems.length > 0) {
            setSelectedIndex(0);
            if (editor != null) editor.setText(this.allItems[0]);
        } else {
            if (editor != null) editor.setText("");
        }
        updating = false;
    }

    /** Factory: free-text autocomplete for College, Location, Category, Department fields. */
    public static AutoCompleteComboBox freeText(String[] items) {
        return new AutoCompleteComboBox(items, Mode.FREE_TEXT);
    }

    /** Factory: strict autocomplete for Role, Status — reverts to valid value on focus-loss. */
    public static AutoCompleteComboBox strict(String[] items) {
        return new AutoCompleteComboBox(items, Mode.STRICT);
    }
}
