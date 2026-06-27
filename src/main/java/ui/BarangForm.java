package ui;

import model.Barang;
import model.Kategori;
import service.BarangService;
import service.KategoriService;
import ui.components.Toast;
import util.Formatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import util.ValidationUtil;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BarangForm extends JPanel {

    private JTextField txtId, txtNama, txtHargaBeli, txtHargaJual, txtStok, txtSearch;
    private JComboBox<String> cbKategori, comboSatuan, cbFilterKategori;
    private JButton btnSimpan, btnHapus, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private BarangService barangService;
    private KategoriService kategoriService;
    private List<Kategori> listKategori;

    public BarangForm() {
        barangService = new BarangService();
        kategoriService = new KategoriService();
        
        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        
        initComponents();
        loadComboKategori();
        loadData();
        clear();
    }

    private void initComponents() {


        RoundedPanel panelTop = ThemeUtil.createCardPanel();
        panelTop.setLayout(new BorderLayout(10, 10));

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; addLabel(panelInput, "ID Barang:", gbc);
        txtId = new JTextField(20); ThemeUtil.styleTextField(txtId);
        gbc.gridx = 1; panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; addLabel(panelInput, "Nama Barang:", gbc);
        txtNama = new JTextField(20); ThemeUtil.styleTextField(txtNama);
        ValidationUtil.addRequiredValidation(txtNama);
        gbc.gridx = 1; panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; addLabel(panelInput, "Kategori:", gbc);
        cbKategori = new JComboBox<>(); ThemeUtil.styleComboBox(cbKategori);
        util.AutoCompletion.enable(cbKategori);
        gbc.gridx = 1; panelInput.add(cbKategori, gbc);

        gbc.gridy = 3; gbc.gridx = 0; addLabel(panelInput, "Satuan:", gbc);
        String[] daftarSatuan = {
            "Pcs", "Pack", "Lusin", "Kodi", "Gross", "Dus", "Karton", "Box", 
            "Slop", "Rim", "Kg", "Gram", "Liter", "Mililiter", "Meter", "Centimeter", 
            "Botol", "Sachet", "Bungkus", "Ikat", "Roll", "Bal", "Karung", 
            "Lembar", "Set", "Pasang", "Tube", "Kaleng", "Galon"
        };
        comboSatuan = new JComboBox<>(daftarSatuan); 
        ThemeUtil.styleComboBox(comboSatuan);
        gbc.gridx = 1; panelInput.add(comboSatuan, gbc);

        gbc.gridx = 0; gbc.gridy = 4; addLabel(panelInput, "Harga Beli:", gbc);
        txtHargaBeli = new JTextField(20); ThemeUtil.styleTextField(txtHargaBeli);
        ThemeUtil.makeCurrencyField(txtHargaBeli);
        ValidationUtil.addNumericValidation(txtHargaBeli);
        gbc.gridx = 1; panelInput.add(txtHargaBeli, gbc);

        gbc.gridx = 0; gbc.gridy = 5; addLabel(panelInput, "Harga Jual:", gbc);
        txtHargaJual = new JTextField(20); ThemeUtil.styleTextField(txtHargaJual);
        ThemeUtil.makeCurrencyField(txtHargaJual); // Menggunakan fitur auto Rp yang baru dibuat
        ValidationUtil.addNumericValidation(txtHargaJual);
        gbc.gridx = 1; panelInput.add(txtHargaJual, gbc);

        gbc.gridx = 0; gbc.gridy = 6; addLabel(panelInput, "Stok:", gbc);
        txtStok = new JTextField(20); ThemeUtil.styleTextField(txtStok);
        ThemeUtil.makeNumberOnly(txtStok);
        ValidationUtil.addNumericValidation(txtStok);
        gbc.gridx = 1; panelInput.add(txtStok, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        
        btnSimpan = new JButton("Simpan"); ThemeUtil.styleButton(btnSimpan, ThemeUtil.SUCCESS_COLOR);
        btnHapus = new JButton("Hapus"); ThemeUtil.styleButton(btnHapus, ThemeUtil.ERROR_COLOR);
        btnClear = new JButton("Clear"); ThemeUtil.styleButton(btnClear, ThemeUtil.TEXT_SECONDARY);

        panelBtn.add(btnSimpan);
        panelBtn.add(btnHapus);
        panelBtn.add(btnClear);

        panelTop.add(panelInput, BorderLayout.WEST);
        panelTop.add(panelBtn, BorderLayout.SOUTH);
        
        add(panelTop, BorderLayout.NORTH);

        RoundedPanel panelBottom = ThemeUtil.createCardPanel();
        panelBottom.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID Barang", "Nama Barang", "Kategori", "Satuan", "Harga Beli", "Harga Jual", "Stok"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        ThemeUtil.styleTable(table, scrollPane);

        // Search Panel
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelSearch.setOpaque(false);
        
        JLabel lblFilter = new JLabel("Filter Kategori:");
        lblFilter.setFont(ThemeUtil.FONT_REGULAR);
        cbFilterKategori = new JComboBox<>();
        util.AutoCompletion.enable(cbFilterKategori);
        ThemeUtil.styleComboBox(cbFilterKategori);

        JLabel lblSearch = new JLabel("Cari:");
        lblSearch.setFont(ThemeUtil.FONT_REGULAR);
        txtSearch = new JTextField(15);
        ThemeUtil.styleTextField(txtSearch);

        panelSearch.add(lblFilter);
        panelSearch.add(cbFilterKategori);
        panelSearch.add(lblSearch);
        panelSearch.add(txtSearch);

        panelBottom.add(panelSearch, BorderLayout.NORTH);
        panelBottom.add(scrollPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.CENTER);

        // ==========================================
        // [Mekanisme Tombol & Keyboard] (EVENT LISTENERS)
        // ==========================================

        // [Event Filter] Jika dropdown filter kategori diubah, langsung jalankan pencarian
        Runnable doSearch = () -> {
            if (cbFilterKategori.getSelectedItem() != null) {
                loadData(txtSearch.getText(), cbFilterKategori.getSelectedItem().toString());
            }
        };
        cbFilterKategori.addActionListener(e -> doSearch.run());

        // [Event Search] Jika tulisan di dalam kotak pencarian (txtSearch) berubah, langsung jalankan pencarian (Real-Time)
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); } // Saat huruf ditambah
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); } // Saat huruf dihapus
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); } // Saat properti teks berubah
        });

        // [Aksi Tombol Utama]
        btnSimpan.addActionListener(e -> simpanData()); // Mengeksekusi simpan/update ke database
        btnHapus.addActionListener(e -> hapusData()); // Mengeksekusi delete ke database
        btnClear.addActionListener(e -> clear()); // Mengosongkan isian di layar

        // [Aksi Keyboard] Saat menekan tombol Enter di kotak teks, otomatis bertindak seperti menekan tombol "Simpan"
        ActionListener enterSubmit = e -> {
            btnSimpan.doClick();
        };
        txtNama.addActionListener(enterSubmit);
        txtHargaBeli.addActionListener(enterSubmit);
        txtHargaJual.addActionListener(enterSubmit);
        txtStok.addActionListener(enterSubmit);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtId.setEditable(false);
                    txtNama.setText(tableModel.getValueAt(row, 1).toString());
                    cbKategori.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    comboSatuan.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    txtHargaBeli.setText(tableModel.getValueAt(row, 4).toString().replace(".0", ""));
                    txtHargaJual.setText(tableModel.getValueAt(row, 5).toString().replace(".0", ""));
                    txtStok.setText(tableModel.getValueAt(row, 6).toString());
                }
            }
        });
    }

    private void addLabel(JPanel p, String text, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(ThemeUtil.FONT_REGULAR);
        lbl.setForeground(ThemeUtil.TEXT_SECONDARY);
        p.add(lbl, gbc);
    }

    public void loadComboKategori() {
        // Inisialisasi listKategori DULU sebelum memanipulasi combobox
        // karena manipulasi combobox akan men-trigger event pencarian
        // yang membutuhkan listKategori (jika tidak, akan NPE)
        listKategori = kategoriService.getAllKategori();

        cbKategori.removeAllItems();
        cbFilterKategori.removeAllItems();
        cbFilterKategori.addItem("Semua Kategori");
        
        for (Kategori k : listKategori) {
            cbKategori.addItem(k.getNamaKategori());
            cbFilterKategori.addItem(k.getNamaKategori());
        }
    }

    public void loadData() {
        loadData("", "Semua Kategori");
    }

    // [Logika UI] Mengisi ulang isi tabel dari database
    public void loadData(String keyword, String filterKategori) {
        tableModel.setRowCount(0); // Kosongkan tabel dulu
        
        // [Logika Database] Panggil service untuk meminta data dari database
        List<Barang> list = barangService.searchBarang(keyword, filterKategori);
        
        for (Barang b : list) {
            String namaKat = "";
            // Mencari nama kategori dari ID kategori yang tersimpan di tb_barang
            for(Kategori k : listKategori) {
                if(k.getIdKategori() == b.getIdKategori()) {
                    namaKat = k.getNamaKategori();
                    break;
                }
            }
            // Masukkan satu baris ke dalam tabel layar
            tableModel.addRow(new Object[]{
                b.getIdBarang(), b.getNamaBarang(), namaKat, 
                b.getSatuan(), Formatter.formatRupiah(b.getHargaBeli()), Formatter.formatRupiah(b.getHargaJual()), b.getStok()
            });
        }
    }

    // [Logika UI] Fungsi untuk mereset kolom-kolom inputan kembali kosong seperti awal
    private void clear() {
        txtId.setText(barangService.generateId()); // Bikin ID baru secara otomatis (misal: BRG001)
        txtId.setEditable(false); // Cegah User ganti ID manual
        txtNama.setText("");
        comboSatuan.setSelectedIndex(0);
        txtHargaBeli.setText("");
        txtHargaJual.setText("");
        txtStok.setText("");
        if(cbKategori.getItemCount() > 0) cbKategori.setSelectedIndex(0);
        table.clearSelection();
    }

    // [Logika Utama] Proses Simpan Data / Update Data saat tombol Simpan Ditekan
    private void simpanData() {
        if (txtNama.getText().trim().isEmpty()) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Nama Barang tidak boleh kosong!");
            return;
        }

        // Jika user klik sebuah baris di tabel, maka aksi tombol simpan berubah menjadi "Update" (isUpdate = true)
        // Jika tidak ada baris yang diklik, maka artinya "Tambah Baru" (isUpdate = false)
        boolean isUpdate = table.getSelectedRow() >= 0;

        try {
            // [Mekanisme] Kumpulkan data dari UI dan simpan dalam kardus (Object) Barang
            Barang b = new Barang();
            b.setIdBarang(txtId.getText());
            b.setNamaBarang(txtNama.getText().trim());
            b.setIdKategori(listKategori.get(cbKategori.getSelectedIndex()).getIdKategori()); // Ambil ID Kategori dari indeks pilihan combo box
            b.setSatuan(comboSatuan.getSelectedItem().toString());
            b.setHargaBeli(Formatter.parseCurrencySafe(txtHargaBeli.getText())); // Hilangkan format 'Rp' agar jadi angka utuh
            b.setHargaJual(Formatter.parseCurrencySafe(txtHargaJual.getText()));
            b.setStok(Integer.parseInt(txtStok.getText().trim()));
            
            // [Logika Database] Lempar object 'b' ke BarangService untuk dikirim via SQL ke Database
            if (isUpdate) {
                if (barangService.updateBarang(b)) { // Lakukan eksekusi perintah UPDATE sql
                    Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil diupdate");
                    loadData(); // Segarkan tampilan tabel
                    clear(); // Bersihkan kembali kotak input
                } else {
                    Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal mengupdate data!");
                }
            } else {
                if (barangService.tambahBarang(b)) { // Lakukan eksekusi perintah INSERT sql
                    Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil disimpan");
                    loadData();
                    clear();
                } else {
                    Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menyimpan data!");
                }
            }
        } catch (NumberFormatException ex) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Harga/Stok harus angka!");
        }
    }

    private void hapusData() {
        if (table.getSelectedRow() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih data yang akan dihapus!");
            return;
        }

        if (barangService.hapusBarang(txtId.getText())) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil dihapus");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menghapus data!");
        }
    }
}
