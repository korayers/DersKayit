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

class OgretimGorevlisiFormu extends JFrame {
    private JTextField ogretimGorevlisiIdAlani, adAlani, soyadAlani, aramaAlani;
    private JComboBox<String> bolumComboBox;
    private JList<String> verdigiDerslerListesi;
    private JButton kaydetButonu, araButonu;
    private DefaultListModel<String> dersListModel;
    private JTable ogretimGorevlisiTablosu;
    private DefaultTableModel tabloModeli;

    public OgretimGorevlisiFormu() {
        setTitle("Öğretim Görevlisi Formu");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

       
        JPanel aramaPaneli = new JPanel();
        aramaAlani = new JTextField(20);
        araButonu = new JButton("Ara");
        aramaPaneli.add(aramaAlani);
        aramaPaneli.add(araButonu);

        
        JPanel formPaneli = new JPanel();
        formPaneli.setLayout(new GridLayout(5, 2, 5, 5));
        ogretimGorevlisiIdAlani = new JTextField();
        adAlani = new JTextField();
        soyadAlani = new JTextField();
        bolumComboBox = new JComboBox<>(new String[] {"Bilgisayar Mühendisliği", "Elektrik-Elektronik Mühendisliği"});
        dersListModel = new DefaultListModel<>();
        verdigiDerslerListesi = new JList<>(dersListModel);
        kaydetButonu = new JButton("Kaydet");

        // Farklı üniversite bilgisayar bölümü dersleri
        dersListModel.addElement("Algoritma ve Veri Yapıları");
        dersListModel.addElement("Bilgisayar Ağları");
        dersListModel.addElement("Veritabanı Yönetim Sistemleri");
        dersListModel.addElement("Mobil Uygulama Geliştirme");
        dersListModel.addElement("Bilgisayar Grafikleri");
        dersListModel.addElement("Yapay Zeka");

        verdigiDerslerListesi.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        formPaneli.add(new JLabel("Öğretim Görevlisi ID:"));
        formPaneli.add(ogretimGorevlisiIdAlani);
        formPaneli.add(new JLabel("Ad:"));
        formPaneli.add(adAlani);
        formPaneli.add(new JLabel("Soyad:"));
        formPaneli.add(soyadAlani);
        formPaneli.add(new JLabel("Bölüm:"));
        formPaneli.add(bolumComboBox);
        formPaneli.add(new JLabel("Verdiği Dersler:"));
        formPaneli.add(new JScrollPane(verdigiDerslerListesi));
        formPaneli.add(kaydetButonu);

     
        String[] sutunAdlari = {"Öğretim Görevlisi ID", "Ad", "Soyad", "Bölüm", "Verdiği Dersler"};
        tabloModeli = new DefaultTableModel(sutunAdlari, 0);
        ogretimGorevlisiTablosu = new JTable(tabloModeli);

        add(aramaPaneli, BorderLayout.NORTH);
        add(formPaneli, BorderLayout.WEST);
        add(new JScrollPane(ogretimGorevlisiTablosu), BorderLayout.CENTER);

        kaydetButonu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ogretimGorevlisiVerisiKaydet();
            }
        });

        araButonu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ogretimGorevlisiVerisiAra();
            }
        });
    }

    private void ogretimGorevlisiVerisiKaydet() {
        String ogretimGorevlisiId = ogretimGorevlisiIdAlani.getText();
        String ad = adAlani.getText();
        String soyad = soyadAlani.getText();
        String bolum = (String) bolumComboBox.getSelectedItem();

        JSONArray verdigiDersler = new JSONArray();
        for (String ders : verdigiDerslerListesi.getSelectedValuesList()) {
            verdigiDersler.put(ders);
        }

        JSONObject ogretimGorevlisiVerisi = new JSONObject();
        ogretimGorevlisiVerisi.put("ogretimGorevlisiId", ogretimGorevlisiId);
        ogretimGorevlisiVerisi.put("ad", ad);
        ogretimGorevlisiVerisi.put("soyad", soyad);
        ogretimGorevlisiVerisi.put("bolum", bolum);
        ogretimGorevlisiVerisi.put("verdigiDersler", verdigiDersler);

        try (FileWriter dosya = new FileWriter(ogretimGorevlisiId + ".json")) {
            dosya.write(ogretimGorevlisiVerisi.toString());
            dosya.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

      
        Vector<String> satir = new Vector<>();
        satir.add(ogretimGorevlisiId);
        satir.add(ad);
        satir.add(soyad);
        satir.add(bolum);
        satir.add(verdigiDersler.toString());
        tabloModeli.addRow(satir);

        JOptionPane.showMessageDialog(this, "Öğretim Görevlisi Kaydedildi: " + ogretimGorevlisiId);
        formuTemizle();
    }

    private void ogretimGorevlisiVerisiAra() {
        String aramaMetni = aramaAlani.getText().toLowerCase();
        if (aramaMetni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Arama metni boş olamaz.");
            return;
        }

        for (int i = 0; i < tabloModeli.getRowCount(); i++) {
            boolean eslesmeBulundu = false;
            for (int j = 0; j < tabloModeli.getColumnCount(); j++) {
                String hucreDegeri = tabloModeli.getValueAt(i, j).toString().toLowerCase();
                if (hucreDegeri.contains(aramaMetni)) {
                    eslesmeBulundu = true;
                    break;
                }
            }

            if (!eslesmeBulundu) {

                ((DefaultTableModel) ogretimGorevlisiTablosu.getModel()).removeRow(i);
                i--;
            }
        }

        if (ogretimGorevlisiTablosu.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Arama için sonuç bulunamadı.");
        }
    }

    private void formuTemizle() {
        ogretimGorevlisiIdAlani.setText("");
        adAlani.setText("");
        soyadAlani.setText("");
        bolumComboBox.setSelectedIndex(0);
        verdigiDerslerListesi.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OgretimGorevlisiFormu().setVisible(true));
    }
}
