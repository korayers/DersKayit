import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

class StudentForm extends JFrame {
    private JTextField studentNumberField, studentNameField, studentSurnameField, studentDepartmentField;
    private JComboBox<String> coursesComboBox;
    private JButton saveButton, searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable studentsTable;

    public StudentForm() {
        initializeUI();
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        createForm();
        createTable();
        createButtons();
        createListeners();
        addComponentsToFrame();
    }

    private void initializeUI() {
        setTitle("Student Form");
        setSize(400, 300);
    }

    private void createForm() {
        studentNumberField = new JTextField(20);
        studentNameField = new JTextField(20);
        studentSurnameField = new JTextField(20);
        studentDepartmentField = new JTextField(20);
        coursesComboBox = new JComboBox<>();
    }

    private void createTable() {
        String[] columnNames = {"Student Number", "Student Name", "Student Surname", "Department", "Courses"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentsTable = new JTable(tableModel);
    }

    private void createButtons() {
        saveButton = new JButton("Save");
        searchButton = new JButton("Search");
    }

    private void createListeners() {
        saveButton.addActionListener(e -> saveStudentData());
        searchButton.addActionListener(e -> searchStudentData());
    }

    private void addComponentsToFrame() {
        addFormFields();
        addSearchPanel();
        addTable();
    }

    private void addFormFields() {
        add(new JLabel("Student Number:"));
        add(studentNumberField);
        add(new JLabel("Student Name:"));
        add(studentNameField);
        add(new JLabel("Student Surname:"));
        add(studentSurnameField);
        add(new JLabel("Department:"));
        add(studentDepartmentField);
        add(new JLabel("Courses:"));
        add(coursesComboBox);
        add(saveButton);
    }

    private void addSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
    }

    private void addTable() {
        add(new JScrollPane(studentsTable), BorderLayout.CENTER);
    }

    private void saveStudentData() {
        String studentNumber = studentNumberField.getText();
        String studentName = studentNameField.getText();
        String studentSurname = studentSurnameField.getText();
        String studentDepartment = studentDepartmentField.getText();
        String selectedCourse = (String) coursesComboBox.getSelectedItem();

        JSONObject studentData = new JSONObject();
        studentData.put("studentNumber", studentNumber);
        studentData.put("studentName", studentName);
        studentData.put("studentSurname", studentSurname);
        studentData.put("studentDepartment", studentDepartment);
        studentData.put("selectedCourse", selectedCourse);

        try (FileWriter file = new FileWriter(studentNumber + ".json")) {
            file.write(studentData.toString());
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Student Saved: " + studentNumber);
        tableModel.addRow(new Object[]{studentNumber, studentName, studentSurname, studentDepartment, selectedCourse});
        clearForm();
    }

    private void searchStudentData() {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        studentsTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(searchText));
    }

    private void clearForm() {
        studentNumberField.setText("");
        studentNameField.setText("");
        studentSurnameField.setText("");
        studentDepartmentField.setText("");
        coursesComboBox.setSelectedIndex(0); // Select the first item
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentForm().setVisible(true));
    }
}
