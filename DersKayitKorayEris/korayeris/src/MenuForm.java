import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm extends JFrame {
    private JButton courseButton;
    private JButton studentButton;
    private JButton instructorButton;

    public MainForm() {
        initializeUI();
        setLayout(new FlowLayout());
        createButtons();
        addButtonsToFrame();
        addListeners();
    }

    private void initializeUI() {
        setTitle("Main Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createButtons() {
        courseButton = new JButton("Course Form");
        studentButton = new JButton("Student Form");
        instructorButton = new JButton("Instructor Form");
    }

    private void addButtonsToFrame() {
        add(courseButton);
        add(studentButton);
        add(instructorButton);
    }

    private void addListeners() {
        courseButton.addActionListener(e -> openCourseForm());
        studentButton.addActionListener(e -> openStudentForm());
        instructorButton.addActionListener(e -> openInstructorForm());
    }

    private void openCourseForm() {
        CourseForm courseForm = new CourseForm();
        courseForm.setVisible(true);
    }

    private void openStudentForm() {
        StudentForm studentForm = new StudentForm();
        studentForm.setVisible(true);
    }

    private void openInstructorForm() {
        InstructorForm instructorForm = new InstructorForm();
        instructorForm.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainForm().setVisible(true));
    }
}
