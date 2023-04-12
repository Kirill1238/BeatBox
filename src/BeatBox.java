import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class BeatBox {
    JPanel mainPanel;
    JTextField field;
    ArrayList<JCheckBox> checkboxList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;
    ImageIcon icon;
    ImageIcon iconcheck;
    int count = 999;
    int c = 0;
    Color colorbuttonText = new Color(75, 0, 130);
    Font fontlabel = new Font("Times New Roman", Font.BOLD, 20);
    Font fontbutton = new Font("Times New Roman", Font.BOLD, 38);
    Font fontfield = new Font("Times New Roman", Font.BOLD, 34);
    Font fonttake = new Font("Times New Roman", Font.BOLD,16);
    Font fontlisten = new Font("Times New Roman", Font.BOLD,18);
    String[] instrumentNames = {"Бас-барабан", "Закрытый хай-хэт", "Открытый хай-хэт", "Акустический малый барабан", "Крэш Тарелка", "Хлопок в ладоши", "Высокий Том", "Привет Бонго", "Маракас", "Свисток", "Низкая конга", "Каубелл", "Вибраслэп", "Низкий средний том", "Высокий агого", "Открытый привет Конга"};
    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public static void main(String[] args) {
        new BeatBox().buildGUI();
    }
    public void buildGUI(){
        theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        checkboxList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.setFont(fontbutton);
        start.setForeground(colorbuttonText);
        start.setBackground(Color.WHITE);
        start.setBorderPainted(false);
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.setFont(fontbutton);
        stop.setForeground(colorbuttonText);
        stop.setBackground(Color.WHITE);
        stop.setBorderPainted(false);
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.setFont(fontbutton);
        upTempo.setForeground(colorbuttonText);
        upTempo.setBackground(Color.WHITE);
        upTempo.setBorderPainted(false);
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.setFont(fontbutton);
        downTempo.setForeground(colorbuttonText);
        downTempo.setBackground(Color.WHITE);
        downTempo.setBorderPainted(false);
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        JButton Clear = new JButton("Clear");
        Clear.setFont(fontbutton);
        Clear.setForeground(colorbuttonText);
        Clear.setBackground(Color.WHITE);
        Clear.setBorderPainted(false);
        Clear.addActionListener(new ClearListener());
        buttonBox.add(Clear);

        buttonBox.add(Box.createRigidArea(new Dimension(0,60)));

        JButton serialization = new JButton("Serialization");
        serialization.setFont(fontbutton);
        serialization.setForeground(colorbuttonText);
        serialization.setBackground(Color.WHITE);
        serialization.setBorderPainted(false);
        serialization.addActionListener(new MySendListener());
        buttonBox.add(serialization);

        JButton deserialization = new JButton("Deserialization");
        deserialization.setFont(fontbutton);
        deserialization.setForeground(colorbuttonText);
        deserialization.setBackground(Color.WHITE);
        deserialization.setBorderPainted(false);
        deserialization.addActionListener(new MyReadInListener());
        buttonBox.add(deserialization);

        buttonBox.add(Box.createRigidArea(new Dimension(0,190)));

        JLabel text = new JLabel("Введите число прослушиваний:");
        text.setFont(fontlisten);
        text.setForeground(colorbuttonText);
        buttonBox.add(text);

        JPanel panelfield = new JPanel();
        panelfield.setBackground(Color.WHITE);
        panelfield.setMaximumSize(new Dimension(600,0));

        field = new JTextField(2);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(fontfield);
        field.setForeground(Color.BLACK);
        field.addActionListener(new FieldListener());
        panelfield.add(field);
        buttonBox.add(panelfield);

        JPanel panelt = new JPanel();
        panelt.setBackground(Color.WHITE);

        JButton getCount = new JButton("Take");
        getCount.setFont(fonttake);
        getCount.setForeground(colorbuttonText);
        getCount.setBackground(Color.WHITE);
        getCount.setBorderPainted(false);
        getCount.addActionListener(new GetCount());
        panelt.add(getCount);
        buttonBox.add(panelt);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        nameBox.setForeground(Color.RED);
        nameBox.setFont(fontlabel);

        for (int i = 0; i < 16; i++) {
            nameBox.add(new Label(instrumentNames[i]));
        }
        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);
        background.setBackground(Color.WHITE);


        GridLayout grid = new GridLayout(16,16);
        grid.setHgap(-4);
        grid.setVgap(-4);
        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);
        icon = new ImageIcon("Icon.png");
        iconcheck = new ImageIcon("Iconcheck.png");

        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setIcon(icon);
            c.setBackground(Color.WHITE);
            c.setSelectedIcon(iconcheck);
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu filemenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        newMenuItem.addActionListener(new NewMenuListener());
        saveMenuItem.addActionListener(new SaveMenuListener());
        filemenu.add(newMenuItem);
        filemenu.add(saveMenuItem);
        menuBar.add(filemenu);
        theFrame.setJMenuBar(menuBar);

        setUpMidi();
        theFrame.getContentPane().add(background);
        theFrame.pack();
        theFrame.setLocationRelativeTo(null);
        theFrame.setVisible(true);
    }
    public void setUpMidi(){
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ,4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void buildTrackAndStart(int count){
        int[] trackList = null;
        sequence.deleteTrack(track);
        track = sequence.createTrack();
        for (int i = 0; i < 16; i++) {
            trackList = new int[16];
            int key = instruments[i];
            for (int j = 0; j < 16; j++) {
                JCheckBox jc = (JCheckBox) checkboxList.get(j + (16 * i));
                if(jc.isSelected())
                    trackList[j] = key;
                else trackList[j] = 0;
            }
            makeTracks(trackList);
            track.add(makeEvent(176,1,127,0,16));
        }
        track.add(makeEvent(192,9,1,0,15));
        try{
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(count - 1);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void clearbox(){
        if(sequencer.isRunning())
            sequencer.stop();
        for(JCheckBox el: checkboxList){
            if(el.isSelected())
                el.setSelected(false);
        }
    }

    public void makeTracks(int[] list) {
        for (int i = 0; i < 16; i++) {
            int key = list[i];
            if (key != 0) {
                track.add(makeEvent(144, 9, key, 100, i));
                track.add(makeEvent(128, 9, key, 100, i + 1));
            }
        }
    }
    private void saveFile(File file){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(JCheckBox el : checkboxList){
                if(el.isSelected())
                    writer.write("true" + "\n");
                else writer.write("false" + "\n");
            }
            writer.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd,chan,one,two);
            event = new MidiEvent(a,tick);
        } catch(Exception e){
            e.printStackTrace();
        }
        return event;
    }
    public void loadFile(File file){
        c = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null){
                makeBox(line);
                c++;
            }
            reader.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void makeBox(String line){
        if(line.equals("true"))
            checkboxList.get(c).setSelected(true);
    }
    public class MyStartListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            buildTrackAndStart(count);
        }
    }
    public class MyStopListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            sequencer.stop();
        }
    }
    public class MyUpTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * 1.03));
        }
    }
    public class MyDownTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * .97));
        }
    }
    public class ClearListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            clearbox();
        }
    }
    public class FieldListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            if(sequencer.isRunning())
                sequencer.stop();
            count = Integer.parseInt(a.getActionCommand());
            field.setText("");
        }
    }
    public class GetCount implements ActionListener{
        public void actionPerformed(ActionEvent a) {
            if(sequencer.isRunning())
                sequencer.stop();
            count = Integer.parseInt(field.getText());
            field.setText("");
        }
    }
    public class MySendListener implements ActionListener{
        public void actionPerformed(ActionEvent a) {
            boolean[] checkboxState = new boolean[256];
            for (int i = 0; i < 256; i++) {
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if (check.isSelected())
                    checkboxState[i] = true;
            }
            try {
                FileOutputStream fileStream = new FileOutputStream(new File("Checkbox.ser"));
                ObjectOutputStream os = new ObjectOutputStream(fileStream);
                os.writeObject(checkboxState);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    public class MyReadInListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            if(sequencer.isRunning())
                sequencer.stop();
            boolean[] checkboxState = null;
            try{
                FileInputStream fileIn = new FileInputStream(new File("Checkbox.ser"));
                ObjectInputStream is = new ObjectInputStream(fileIn);
                checkboxState = (boolean[]) is.readObject();
            } catch(Exception ex){
                ex.printStackTrace();
            }
            for (int i = 0; i < 256; i++) {
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if(checkboxState[i])
                    check.setSelected(true);
                else check.setSelected(false);
            }
        }
    }
    public class SaveMenuListener implements ActionListener{
        public void actionPerformed(ActionEvent ev) {
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(theFrame);
            saveFile(fileSave.getSelectedFile());
        }
    }
    public class NewMenuListener implements ActionListener{
        public void actionPerformed(ActionEvent ev) {
            clearbox();
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(theFrame);
            loadFile(fileOpen.getSelectedFile());
        }
    }
}
