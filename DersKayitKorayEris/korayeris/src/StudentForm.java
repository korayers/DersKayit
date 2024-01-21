import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

class OgrenciForm extends JFrame {
    private JTextField ogrenciNumaraAlanı, ogrenciAdiAlanı, ogrenciSoyadiAlanı, ogrenciBolumAlanı;
    private JComboBox<String> derslerComboBox;
    private JButton kaydetButonu, araButonu;
    private JTextField aramaAlanı;
    private DefaultTableModel tabloModel;
    private JTable ogrenciTablosu;

    public OgrenciForm() {
        initializeUI();
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        formuOlustur();
        tabloyuOlustur();
        butonlariOlustur();
        dinleyicileriOlustur();
        frameEkle();
    }

    private void initializeUI() {
        setTitle("Öğrenci Formu");
        setSize(400, 300);
    }

    private void formuOlustur() {
        ogrenciNumaraAlanı = new JTextField(20);
        ogrenciAdiAlanı = new JTextField(20);
        ogrenciSoyadiAlanı = new JTextField(20);
        ogrenciBolumAlanı = new JTextField(20);
        derslerComboBox = new JComboBox<>();
    }

    private void tabloyuOlustur() {
        String[] sutunAdlari = {"Öğrenci Numarası", "Öğrenci Adı", "Öğrenci Soyadı", "Bölüm", "Dersler"};
        tabloModel = new DefaultTableModel(sutunAdlari, 0);
        ogrenciTablosu = new JTable(tabloModel);
    }

    private void butonlariOlustur() {
        kaydetButonu = new JButton("Kaydet");
        araButonu = new JButton("Ara");
    }

    private void dinleyicileriOlustur() {
        kaydetButonu.addActionListener(e -> ogrenciBilgisiniKaydet());
        araButonu.addActionListener(e -> ogrenciBilgisiAra());
    }

    private void frameEkle() {
        formAlanlariniEkle();
        aramaPaneliniEkle();
        tabloyuEkle();
    }

    private void formAlanlariniEkle() {
        add(new JLabel("Öğrenci Numarası:"));
        add(ogrenciNumaraAlanı);
        add(new JLabel("Öğrenci Adı:"));
        add(ogrenciAdiAlanı);
        add(new JLabel("Öğrenci Soyadı:"));
        add(ogrenciSoyadiAlanı);
        add(new JLabel("Bölüm:"));
        add(ogrenciBolumAlanı);
        add(new JLabel("Dersler:"));
        add(derslerComboBox);
        add(kaydetButonu);
    }

    private void aramaPaneliniEkle() {
        JPanel aramaPaneli = new JPanel();
        aramaAlanı = new JTextField(20);
        aramaPaneli.add(aramaAlanı);
        aramaPaneli.add(araButonu);
        add(aramaPaneli, BorderLayout.NORTH);
    }

    private void tabloyuEkle() {
        add(new JScrollPane(ogrenciTablosu), BorderLayout.CENTER);
    }

    private void ogrenciBilgisiniKaydet() {
        String ogrenciNumara = ogrenciNumaraAlanı.getText();
        String ogrenciAdi = ogrenciAdiAlanı.getText();
        String ogrenciSoyadi = ogrenciSoyadiAlanı.getText();
        String ogrenciBolum = ogrenciBolumAlanı.getText();
        String secilenDers = (String) derslerComboBox.getSelectedItem();

        JSONObject ogrenciBilgisi = new JSONObject();
        ogrenciBilgisi.put("ogrenciNumara", ogrenciNumara);
        ogrenciBilgisi.put("ogrenciAdi", ogrenciAdi);
        ogrenciBilgisi.put("ogrenciSoyadi", ogrenciSoyadi);
        ogrenciBilgisi.put("ogrenciBolum", ogrenciBolum);
        ogrenciBilgisi.put("secilenDers", secilenDers);

        try (FileWriter dosya = new FileWriter(ogrenciNumara + ".json")) {
            dosya.write(ogrenciBilgisi.toString());
            dosya.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Öğrenci Kaydedildi: " + ogrenciNumara);
        tabloModel.addRow(new Object[]{ogrenciNumara, ogrenciAdi, ogrenciSoyadi, ogrenciBolum, secilenDers});
        formuTemizle();
    }

    private void ogrenciBilgisiAra() {
        String aramaMetni = aramaAlanı.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> siralayici = new TableRowSorter<>(tabloModel);
        ogrenciTablosu.setRowSorter(siralayici);
        siralayici.setRowFilter(RowFilter.regexFilter(aramaMetni));
    }

    private void formuTemizle() {
        ogrenciNumaraAlanı.setText("");
        ogrenciAdiAlanı.setText("");
        ogrenciSoyadiAlanı.setText("");
        ogrenciBolumAlanı.setText("");
        derslerComboBox.setSelectedIndex(0); // İlk öğeyi seç
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OgrenciForm().setVisible(true));
    }
}
