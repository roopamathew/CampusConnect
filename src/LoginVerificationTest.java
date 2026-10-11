import data.DummyData;
import model.User;
import ui.LoginFrame;
import ui.ModernButton;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginVerificationTest {

    private static int check(String name, boolean condition) {
        if (condition) {
            System.out.println("PASS - " + name);
            return 0;
        } else {
            System.err.println("FAIL - " + name);
            return 1;
        }
    }

    public static void main(String[] args) {
        DummyData.load();
        int failures = 0;
        System.out.println("=== RUNNING LOGIN PAGE VERIFICATION TESTS ===");

        LoginFrame frame = new LoginFrame();

        // 1. Collect all components in frame
        List<Component> allComponents = new ArrayList<Component>();
        collectComponents(frame.getContentPane(), allComponents);

        // 2. Verify Demo Buttons and Autofill are COMPLETELY GONE
        boolean hasDemoStudentBtn = false;
        boolean hasDemoAdminBtn = false;
        boolean hasDemoSuperAdminBtn = false;
        boolean hasDemoAccountText = false;
        boolean hasGoogleBtn = false;
        boolean hasRememberMe = false;

        for (Component c : allComponents) {
            if (c instanceof JButton) {
                String text = ((JButton) c).getText();
                if (text != null) {
                    if (text.equalsIgnoreCase("Student") || text.equalsIgnoreCase("Demo Student")) hasDemoStudentBtn = true;
                    if (text.equalsIgnoreCase("College Admin") || text.equalsIgnoreCase("Admin")) hasDemoAdminBtn = true;
                    if (text.equalsIgnoreCase("Super Admin")) hasDemoSuperAdminBtn = true;
                    if (text.toLowerCase().contains("google")) hasGoogleBtn = true;
                }
            }
            if (c instanceof JCheckBox) {
                String text = ((JCheckBox) c).getText();
                if (text != null && text.toLowerCase().contains("remember")) hasRememberMe = true;
            }
            if (c instanceof JLabel) {
                String text = ((JLabel) c).getText();
                if (text != null && text.toLowerCase().contains("demo accounts")) hasDemoAccountText = true;
            }
        }

        failures += check("No demo Student button", !hasDemoStudentBtn);
        failures += check("No demo College Admin button", !hasDemoAdminBtn);
        failures += check("No demo Super Admin button", !hasDemoSuperAdminBtn);
        failures += check("No 'Demo Accounts' section", !hasDemoAccountText);
        failures += check("No Google sign-in button", !hasGoogleBtn);
        failures += check("No Remember Me checkbox", !hasRememberMe);

        // 3. Verify Required Text and Branding Content
        boolean hasBrandName = false;
        boolean hasTagline = false;
        boolean hasIntro = false;
        boolean hasFeature1 = false;
        boolean hasFeature2 = false;
        boolean hasFeature3 = false;
        boolean hasWelcomeBack = false;
        boolean hasUsernameLabel = false;
        boolean hasPasswordLabel = false;
        boolean hasForgotPassLink = false;
        boolean hasCreateAccountLink = false;
        boolean hasRequestAdminLink = false;
        boolean hasSignInButton = false;

        for (Component c : allComponents) {
            if (c instanceof JLabel) {
                String t = ((JLabel) c).getText();
                if (t != null) {
                    if (t.contains("CampusConnect")) hasBrandName = true;
                    if (t.contains("Discover Beyond Your Campus")) hasTagline = true;
                    if (t.contains("Discover campus events, connect with student communities")) hasIntro = true;
                    if (t.contains("Discover Events")) hasFeature1 = true;
                    if (t.contains("Connect with Communities")) hasFeature2 = true;
                    if (t.contains("Stay Connected")) hasFeature3 = true;
                    if (t.contains("Welcome back")) hasWelcomeBack = true;
                    if (t.contains("Username or institutional email")) hasUsernameLabel = true;
                    if (t.contains("Password")) hasPasswordLabel = true;
                    if (t.contains("Forgot password?")) hasForgotPassLink = true;
                    if (t.contains("Create an account")) hasCreateAccountLink = true;
                    if (t.contains("Request admin access")) hasRequestAdminLink = true;
                }
            }
            if (c instanceof JButton) {
                String t = ((JButton) c).getText();
                if (t != null && t.equalsIgnoreCase("Sign In")) hasSignInButton = true;
            }
        }

        failures += check("Brand name visible", hasBrandName);
        failures += check("Tagline visible", hasTagline);
        failures += check("Mission intro visible", hasIntro);
        failures += check("Feature 1: Discover Events", hasFeature1);
        failures += check("Feature 2: Connect with Communities", hasFeature2);
        failures += check("Feature 3: Stay Connected", hasFeature3);
        failures += check("Heading 'Welcome back' visible", hasWelcomeBack);
        failures += check("Username or email label visible", hasUsernameLabel);
        failures += check("Password label visible", hasPasswordLabel);
        failures += check("Forgot password link visible", hasForgotPassLink);
        failures += check("Sign In button visible", hasSignInButton);
        failures += check("Create an account link visible", hasCreateAccountLink);
        failures += check("Request admin access link visible", hasRequestAdminLink);

        // 4. Test Show / Hide Password toggle
        try {
            Field passField = LoginFrame.class.getDeclaredField("passwordField");
            passField.setAccessible(true);
            JPasswordField pwd = (JPasswordField) passField.get(frame);

            Field showHideBtnField = LoginFrame.class.getDeclaredField("showHideBtn");
            showHideBtnField.setAccessible(true);
            JButton showHide = (JButton) showHideBtnField.get(frame);

            failures += check("Initial echo char is hidden ('•')", pwd.getEchoChar() == '•');
            failures += check("Initial button text is 'Show'", "Show".equalsIgnoreCase(showHide.getText()));

            // Click show/hide
            showHide.doClick();
            failures += check("Password revealed after click", pwd.getEchoChar() == (char) 0);
            failures += check("Button text updated to 'Hide'", "Hide".equalsIgnoreCase(showHide.getText()));

            // Click again to hide
            showHide.doClick();
            failures += check("Password concealed again", pwd.getEchoChar() == '•');
            failures += check("Button text reverted to 'Show'", "Show".equalsIgnoreCase(showHide.getText()));
        } catch (Exception e) {
            e.printStackTrace();
            failures++;
        }

        // 5. Test Window Resizing Stability
        try {
            frame.setSize(920, 600);
            frame.validate();
            frame.setSize(1080, 680);
            frame.validate();
            frame.setSize(1366, 768);
            frame.validate();
            failures += check("Window resizing handled without errors", true);
        } catch (Exception e) {
            e.printStackTrace();
            failures++;
        }

        frame.dispose();

        System.out.println("=============================================");
        if (failures == 0) {
            System.out.println("ALL LOGIN VERIFICATION CHECKS PASSED (100%)!");
        } else {
            System.err.println("FAILED WITH " + failures + " ERRORS.");
            System.exit(1);
        }
    }

    private static void collectComponents(Container container, List<Component> list) {
        for (Component c : container.getComponents()) {
            list.add(c);
            if (c instanceof Container) {
                collectComponents((Container) c, list);
            }
        }
    }
}
