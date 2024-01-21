import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

class CourseForm extends JFrame {
    private JTextField courseCodeField, courseNameField, courseTermField, instructorField, searchField;
    private JButton saveButton, searchButton;
    private DefaultTableModel tableModel;
    private JTable coursesTable;

    public CourseForm() {
        setTitle("Course Form");
        setSize(400, 300);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        courseCodeField = new JTextField(20);
        courseNameField = new JTextField(20);
        courseTermField = new JTextField(20);
        instructorField = new JTextField(20);
        saveButton = new JButton("Save");

        add(new JLabel("Course Code:"));
        add(courseCodeField);
        add(new JLabel("Course Name:"));
        add(courseNameField);
        add(new JLabel("Course Term:"));
        add(courseTermField);
        add(new JLabel("Instructor:"));
        add(instructorField);
        add(saveButton);

        // Search area
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Table
        String[] columnNames = {"Course Code", "Course Name", "Course Term", "Instructor"};
        tableModel = new DefaultTableModel(columnNames, 0);
        coursesTable = new JTable(tableModel);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(coursesTable), BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCourseData();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCourseData();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCourseData();
            }
        });
    }

    private void saveCourseData() {
        String courseCode = courseCodeField.getText();
        String courseName = courseNameField.getText();
        String courseTerm = courseTermField.getText();
        String instructor = instructorField.getText();

        JSONObject courseData = new JSONObject();
        courseData.put("courseCode", courseCode);
        courseData.put("courseName", courseName);
        courseData.put("courseTerm", courseTerm);
        courseData.put("instructor", instructor);

        try (FileWriter file = new FileWriter(courseCode + ".json")) {
            file.write(courseData.toString());
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Course Saved: " + courseCode);
        tableModel.addRow(new Object[]{courseCode, courseName, courseTerm, instructor});
        SharedData.courseList.add(courseCode + " - " + courseName);
        clearForm();
    }

    private void searchCourseData() {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        coursesTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(searchText));
    }

    private void clearForm() {
        courseCodeField.setText("");
        courseNameField.setText("");
        courseTermField.setText("");
        instructorField.setText("");
    }
}
