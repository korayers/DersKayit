import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

class DersFormu extends JFrame {
    private JTextField dersKoduAlani, dersAdiAlani, dersDonemAlani, ogretmenAlani, araField;
    private JButton kaydetButonu, araButonu;
    private DefaultTableModel tabloModel;
    private JTable derslerTablosu;

    public DersFormu() {
        setTitle("Ders Formu");
        setSize(400, 300);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        dersKoduAlani = new JTextField(20);
        dersAdiAlani = new JTextField(20);
        dersDonemAlani = new JTextField(20);
        ogretmenAlani = new JTextField(20);
        kaydetButonu = new JButton("Kaydet");

        add(new JLabel("Ders Kodu:"));
        add(dersKoduAlani);
        add(new JLabel("Ders Adı:"));
        add(dersAdiAlani);
        add(new JLabel("Ders Dönemi:"));
        add(dersDonemAlani);
        add(new JLabel("Öğretmen:"));
        add(ogretmenAlani);
        add(kaydetButonu);

        // Arama alanı
        JPanel araPanel = new JPanel();
        araField = new JTextField(20);
        araButonu = new JButton("Ara");
        araPanel.add(araField);
        araPanel.add(araButonu);

        // Tablo
        String[] kolonIsimleri = {"Ders Kodu", "Ders Adı", "Ders Dönemi", "Öğretmen"};
        tabloModel = new DefaultTableModel(kolonIsimleri, 0);
        derslerTablosu = new JTable(tabloModel);

        add(araPanel, BorderLayout.NORTH);
        add(new JScrollPane(derslerTablosu), BorderLayout.CENTER);

        kaydetButonu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dersVerisiniKaydet();
            }
        });

        araButonu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dersVerisiAra();
            }
        });

        kaydetButonu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dersVerisiniKaydet();
            }
        });
    }

    private void dersVerisiniKaydet() {
        String dersKodu = dersKoduAlani.getText();
        String dersAdi = dersAdiAlani.getText();
        String dersDonemi = dersDonemAlani.getText();
        String ogretmen = ogretmenAlani.getText();

        JSONObject dersVerisi = new JSONObject();
        dersVerisi.put("dersKodu", dersKodu);
        dersVerisi.put("dersAdi", dersAdi);
        dersVerisi.put("dersDonemi", dersDonemi);
        dersVerisi.put("ogretmen", ogretmen);

        try (FileWriter dosya = new FileWriter(dersKodu + ".json")) {
            dosya.write(dersVerisi.toString());
            dosya.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Ders Kaydedildi: " + dersKodu);
        tabloModel.addRow(new Object[]{dersKodu, dersAdi, dersDonemi, ogretmen});
        SharedData.dersListesi.add(dersKodu + " - " + dersAdi);
        formuTemizle();
    }

    private void dersVerisiAra() {
        String aramaMetni = araField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> siralayici = new TableRowSorter<>(tabloModel);
        derslerTablosu.setRowSorter(siralayici);
        siralayici.setRowFilter(RowFilter.regexFilter(aramaMetni));
    }

    private void formuTemizle() {
        dersKoduAlani.setText("");
        dersAdiAlani.setText("");
        dersDonemAlani.setText("");
        ogretmenAlani.setText("");
    }
}
