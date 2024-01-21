import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

class TeachingStaffForm extends JFrame {
    private JTextField teacherIdField, firstNameField, lastNameField, searchField;
    private JComboBox<String> departmentComboBox;
    private JList<String> taughtCoursesList;
    private JButton saveButton, searchButton;
    private DefaultListModel<String> courseListModel;
    private JTable teachingStaffTable;
    private DefaultTableModel tableModel;

    public TeachingStaffForm() {
        setTitle("Teaching Staff Form");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Search area
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Form fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 5, 5));
        teacherIdField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        departmentComboBox = new JComboBox<>(new String[] {"Computer Engineering", "Electrical-Electronics Engineering"});
        courseListModel = new DefaultListModel<>();
        taughtCoursesList = new JList<>(courseListModel);
        saveButton = new JButton("Save");

        // Sample courses
        courseListModel.addElement("Gorsel Programlama");
        courseListModel.addElement("Nesne Tababnlı Programlama");
        courseListModel.addElement("Isletim Sistemleri");
        courseListModel.addElement("Web Programlama");
        courseListModel.addElement("Edebiyat");
        courseListModel.addElement("Matematik");

        taughtCoursesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        formPanel.add(new JLabel("Teacher ID:"));
        formPanel.add(teacherIdField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentComboBox);
        formPanel.add(new JLabel("Taught Courses:"));
        formPanel.add(new JScrollPane(taughtCoursesList));
        formPanel.add(saveButton);

        // Table
        String[] columnNames = {"Teacher ID", "First Name", "Last Name", "Department", "Taught Courses"};
        tableModel = new DefaultTableModel(columnNames, 0);
        teachingStaffTable = new JTable(tableModel);

        add(searchPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.WEST);
        add(new JScrollPane(teachingStaffTable), BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTeachingStaffData();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTeachingStaff();
            }
        });
    }

    private void saveTeachingStaffData() {
        String teacherId = teacherIdField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String department = (String) departmentComboBox.getSelectedItem();

        JSONArray taughtCourses = new JSONArray();
        for (String course : taughtCoursesList.getSelectedValuesList()) {
            taughtCourses.put(course);
        }

        JSONObject teachingStaffData = new JSONObject();
        teachingStaffData.put("teacherId", teacherId);
        teachingStaffData.put("firstName", firstName);
        teachingStaffData.put("lastName", lastName);
        teachingStaffData.put("department", department);
        teachingStaffData.put("taughtCourses", taughtCourses);

        try (FileWriter file = new FileWriter(teacherId + ".json")) {
            file.write(teachingStaffData.toString());
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Add data to the table
        Vector<String> row = new Vector<>();
        row.add(teacherId);
        row.add(firstName);
        row.add(lastName);
        row.add(department);
        row.add(taughtCourses.toString());
        tableModel.addRow(row);

        JOptionPane.showMessageDialog(this, "Teaching Staff Saved: " + teacherId);
        clearForm();
    }

    private void searchTeachingStaff() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search text cannot be empty.");
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean matchFound = false;
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                String cellValue = tableModel.getValueAt(i, j).toString().toLowerCase();
                if (cellValue.contains(searchText)) {
                    matchFound = true;
                    break;
                }
            }

            if (!matchFound) {
                // If no match found, hide this row
                ((DefaultTableModel) teachingStaffTable.getModel()).removeRow(i);
                i--;
            }
        }

        if (teachingStaffTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No results found for the search.");
        }
    }

    private void clearForm() {
        teacherIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        departmentComboBox.setSelectedIndex(0);
        taughtCoursesList.clearSelection();
    }
}
