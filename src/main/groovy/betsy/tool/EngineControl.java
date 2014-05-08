package betsy.tool;

import betsy.api.helper.EngineHelper;
import betsy.data.engines.EngineAPI;
import betsy.data.engines.EngineLifecycle;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The GUI to install, start and stop a local engine or all local engines.
 */
public class EngineControl extends JFrame {

    private final DefaultListModel<String> actions = new DefaultListModel<>();

    public static void main(String[] args) {
        new EngineControl().setVisible(true);
    }

    public EngineControl() {
        this.setLayout(new BorderLayout());
        this.add(createCenterPanel(), BorderLayout.CENTER);
        JList<String> comp = new JList<>(actions);
        comp.setVisibleRowCount(10);
        this.add(new JScrollPane(comp), BorderLayout.NORTH);

        this.setSize(800, 1000);
        this.setTitle("Engine Control Center");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        toast("STARTED");
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();

        final List<EngineAPI> engines = EngineHelper.getEngineAPIs();

        int buttons = 6;
        int columns = 1 + buttons;
        int rows = engines.size() + 2; // one empty, one for all
        panel.setLayout(new GridLayout(rows, columns, 0, 10));

        for (EngineAPI engine : engines) {
            panel.add(new JLabel(engine.getName()));

            panel.add(createButton("install", engine, engine::install));
            panel.add(createButton("uninstall", engine, engine::uninstall));
            panel.add(createButton("isInstalled?", engine, () -> JOptionPane.showMessageDialog(this, engine.isInstalled())));

            panel.add(createButton("startup", engine, engine::startup));
            panel.add(createButton("shutdown", engine, engine::shutdown));
            panel.add(createButton("isStarted?", engine, () -> JOptionPane.showMessageDialog(this, engine.isInstalled())));
        }

        addEmptyRow(panel, columns);

        panel.add(new JLabel("ALL"));
        panel.add(createAllButton("install", EngineLifecycle::install));
        panel.add(createAllButton("uninstall", EngineLifecycle::uninstall));
        panel.add(new JLabel());
        panel.add(createAllButton("startup", EngineLifecycle::startup));
        panel.add(createAllButton("shutdown", EngineLifecycle::shutdown));
        panel.add(new JLabel());

        return panel;
    }

    private JButton createAllButton(String name, Consumer<EngineAPI> f) {
        JButton button = new JButton(name);
        button.addActionListener(e -> {
            for (final EngineAPI engine : EngineHelper.getEngineAPIs()) {
                executeEngineAction(name, engine, () -> f.accept(engine));
            }
        });
        return button;
    }

    private JButton createButton(final String name, final EngineAPI engine, Runnable action) {
        JButton button = new JButton(name);
        button.addActionListener((e) -> executeEngineAction(name, engine, action));
        return button;
    }

    private void executeEngineAction(String name, EngineAPI engine, Runnable action) {
        executeAction(name + " of " + engine.getName(), action);
    }

    private void executeAction(final String name, final Runnable action) {
        new Thread() {
            public void run() {
                toast("EXECUTE Action " + name);
                action.run();
                toast("EXECUTE Action " + name + " COMPLETED");
            }
        }.start();
    }


    private void toast(String message) {
        String toastMessage = "[" + new Date() + "] " + message;
        System.out.println(toastMessage);
        actions.insertElementAt(toastMessage, 0);
        this.validate();
        this.repaint();
    }

    private void addEmptyRow(JPanel panel, int columns) {
        for(int i = 0; i < columns; i++){
            panel.add(new JLabel());
        }
    }
}
