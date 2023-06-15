import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MyToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        //TODO: resolve WARN - til.ui.update.LazyUiDisposable - use project as a parent disposable
        JPanel panel = new JPanel(new BorderLayout());

        // Create a panel to hold the input components
        JPanel contentPanel = new JPanel(new GridLayout(0, 2, 5, 5)); // Two columns, 5px horizontal and vertical gaps

        // Add input panels for options
        addOptionInput(contentPanel, "--targetClasses:");
        addOptionInput(contentPanel, "--targetTests:");
        addReportDirInput(contentPanel,"--reportDir:", project);
        addOptionInput(contentPanel, "--includeLaunchClasspath:");
        addOptionInput(contentPanel, "--dependencyDistance:");
        addOptionInput(contentPanel, "--threads:");
        addOptionInput(contentPanel, "--mutateStaticInits:");
        addOptionInput(contentPanel, "--mutators:");
        addOptionInput(contentPanel, "--excludedMethods:");
        addOptionInput(contentPanel, "--excludedClasses:");
        addOptionInput(contentPanel, "--excludedTests:");
        addOptionInput(contentPanel, "--avoidCallsTo:");
        addOptionInput(contentPanel, "--verbose:");
        addOptionInput(contentPanel, "--timeoutFactor:");
        addOptionInput(contentPanel, "--timeoutConst:");
        addOptionInput(contentPanel, "--maxMutationsPerClass:");
        addOptionInput(contentPanel, "--jvmArgs:");
        addOptionInput(contentPanel, "--jvmPath:");
        addOptionInput(contentPanel, "--outputFormats:");
        addOptionInput(contentPanel, "--failWhenNoMutations:");
        addOptionInput(contentPanel, "--classPath:");
        addOptionInput(contentPanel, "--mutableCodePaths:");
        addOptionInput(contentPanel, "--testPlugin:");
        addOptionInput(contentPanel, "--includedGroups:");
        addOptionInput(contentPanel, "--excludedGroups:");
        addOptionInput(contentPanel, "--detectInlinedCode:");
        addOptionInput(contentPanel, "--timestampedReports:");
        addOptionInput(contentPanel, "--mutationThreshold:");
        addOptionInput(contentPanel, "--coverageThreshold:");
        addOptionInput(contentPanel, "--historyInputLocation:");
        addOptionInput(contentPanel, "--historyOutputLocation:");

        // Create a scroll pane and add the content panel to it
        JScrollPane scrollPane = new JBScrollPane (contentPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Create a run button
        JButton runButton = new JButton("Run Pitest");
        runButton.setForeground(Color.GREEN);

        //TODO: pass these inputs to run Pitest
        runButton.addActionListener(e -> {
            // Print the inputs and their corresponding labels
            for (int i = 0; i < contentPanel.getComponentCount(); i += 2) {
                Component labelComponent = contentPanel.getComponent(i);
                Component inputComponent = contentPanel.getComponent(i + 1);

                if (labelComponent instanceof JLabel) {
                    JLabel label = (JLabel) labelComponent;

                    if (inputComponent instanceof JTextField) { // cast JTextField
                        JTextField textField = (JTextField) inputComponent;
                        System.out.println(label.getText() + " " + textField.getText());
                    } else if (inputComponent instanceof TextFieldWithBrowseButton) { // cast TextFieldWithBrowseButton
                        TextFieldWithBrowseButton browseButton = (TextFieldWithBrowseButton) inputComponent;
                        JTextField textField = browseButton.getChildComponent();
                        System.out.println(label.getText() + " " + textField.getText());
                        Disposer.dispose(browseButton);
                    }
                }
            }
        });

        // Add run button to the button panel
        buttonPanel.add(runButton);

        // Add to the bottom of the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the panel to the tool window
        toolWindow.getContentManager().addContent(
                toolWindow.getContentManager().getFactory().createContent(panel, "", false)
        );
    }

    // Adds label and input box to panel
    private void addOptionInput(JPanel panel, String label) {
        JLabel optionLabel = new JLabel(label);
        JTextField optionField = new JTextField(20);

        panel.add(optionLabel);
        panel.add(optionField);
    }

    // Adds label and file selection input to panel
    private void addReportDirInput(JPanel panel, String label, Project project) {
        JLabel reportDirLabel = new JLabel(label);
        TextFieldWithBrowseButton reportDirField = new TextFieldWithBrowseButton();

        // Get file descriptor to directory
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        reportDirField.addBrowseFolderListener(null, null, project, descriptor);

        panel.add(reportDirLabel);
        panel.add(reportDirField);
    }

    // TODO: Boilerplate function for ToolWindow, must think about what I need before running Pitest
//    @Override
//    public void init(ToolWindow window) {
//    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        // Always return true if the tool window should be available for the specified project
        return true;
    }
}

