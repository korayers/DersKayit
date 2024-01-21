import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnaForm extends JFrame {
    private JButton dersButonu;
    private JButton ogrenciButonu;
    private JButton ogretmenButonu;

    public AnaForm() {
        initializeUI();
        setLayout(new FlowLayout());
        butonlariOlustur();
        frameEkle();
        dinleyicileriEkle();
    }

    private void initializeUI() {
        setTitle("Ana Menü");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void butonlariOlustur() {
        dersButonu = new JButton("Ders Formu");
        ogrenciButonu = new JButton("Öğrenci Formu");
        ogretmenButonu = new JButton("Öğretmen Formu");
    }

    private void frameEkle() {
        add(dersButonu);
        add(ogrenciButonu);
        add(ogretmenButonu);
    }

    private void dinleyicileriEkle() {
        dersButonu.addActionListener(e -> dersFormunuAc());
        ogrenciButonu.addActionListener(e -> ogrenciFormunuAc());
        ogretmenButonu.addActionListener(e -> ogretmenFormunuAc());
    }

    private void dersFormunuAc() {
        DersFormu dersFormu = new DersFormu();
        dersFormu.setVisible(true);
    }

    private void ogrenciFormunuAc() {
        OgrenciForm ogrenciFormu = new OgrenciForm();
        ogrenciFormu.setVisible(true);
    }

    private void ogretmenFormunuAc() {
        OgretmenFormu ogretmenFormu = new OgretmenFormu();
        ogretmenFormu.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnaForm().setVisible(true));
    }
}
