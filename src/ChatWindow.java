import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatWindow extends JFrame {

    public ChatWindow() {

        setTitle(Constants.chatTitle);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setBounds(300, 300, 600, 400);

        JTextPane dialogField = new JTextPane();

        dialogField.setEditorKit(new WrapEditorKit());

        dialogField.setEditable(false);

        JScrollPane dialogScrollPane = new JScrollPane();

        dialogScrollPane.getViewport().add(dialogField);

        JTextArea messageField = new JTextArea();

        messageField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {


            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

                if (keyEvent.getKeyChar() == '\n')
                    sendMessage(dialogField, messageField);
            }
        });

        messageField.setLineWrap(true);

        JScrollPane messageScrollPane = new JScrollPane(messageField);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                dialogScrollPane, messageScrollPane);

        splitPane.setDividerLocation(250);

        splitPane.setPreferredSize(new Dimension(590, 350));

        JPanel panelForSplitPane = new JPanel();

        panelForSplitPane.setPreferredSize(new Dimension(600, 350));

        panelForSplitPane.add(splitPane);

        add(panelForSplitPane);

        JButton sendMessageButton = new JButton(Constants.sendText);

        sendMessageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                sendMessage(dialogField, messageField);
            }
        });

        JPanel panelForButton = new JPanel();

        panelForButton.setLayout(new BoxLayout(panelForButton, BoxLayout.X_AXIS));

        panelForButton.add(Box.createHorizontalGlue());

        panelForButton.add(sendMessageButton);

        panelForButton.add(Box.createHorizontalStrut(40));

        panelForButton.setPreferredSize(new Dimension(600, 50));

        add(panelForButton);

        setVisible(true);
    }

    private void sendMessage(JTextPane dialogField, JTextArea messageField) {

        appendToPane(dialogField, Constants.you + '\n', Color.BLUE);

        appendToPane(dialogField, "   " + messageField.getText() + '\n', Color.BLACK);

        messageField.setText("");
    }

    private void appendToPane(JTextPane textPane, String message, Color color) {

        StyleContext styleContext = StyleContext.getDefaultStyleContext();

        AttributeSet set = styleContext.addAttribute(
                SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        set = styleContext.addAttribute(set, StyleConstants.FontFamily, "Lucida Console");
        set = styleContext.addAttribute(set, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = textPane.getDocument().getLength();

        textPane.setCaretPosition(len);

        textPane.setCharacterAttributes(set, false);

        textPane.setEditable(true);

        textPane.replaceSelection(message);

        textPane.setEditable(false);
    }
}


class WrapEditorKit extends StyledEditorKit {

    ViewFactory defaultFactory = new WrapColumnFactory();

    public ViewFactory getViewFactory() {
        return defaultFactory;
    }
}


class WrapColumnFactory implements ViewFactory {

    @Override
    public View create(Element element) {

        String kind = element.getName();

        if (kind != null) {

            if (kind.equals(AbstractDocument.ContentElementName)) {

                return new WrapLabelView(element);

            } else if (kind.equals(AbstractDocument.ParagraphElementName)) {

                return new ParagraphView(element);

            } else if (kind.equals(AbstractDocument.SectionElementName)) {

                return new BoxView(element, View.Y_AXIS);

            } else if (kind.equals(StyleConstants.ComponentElementName)) {

                return new ComponentView(element);

            } else if (kind.equals(StyleConstants.IconElementName)) {

                return new IconView(element);
            }
        }

        return new LabelView(element);
    }
}


class WrapLabelView extends LabelView {

    public WrapLabelView(Element element) {

        super(element);
    }

    public float getMinimumSpan(int axis) {

        switch (axis) {

            case View.X_AXIS:
                return 0;

            case View.Y_AXIS:
                return super.getMinimumSpan(axis);

            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }
    }
}