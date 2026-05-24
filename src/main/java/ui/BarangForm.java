package ui;

import model.Barang;
import model.Kategori;
import service.BarangService;
import service.KategoriService;
import util.Formatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BarangForm extends JPanel {

    private JTextField txtId, txtNama, txtSatuan, txtHarga, txtStok;
    private JComboBox<Kategori> cbKategori;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear, btnRefresh;
    private JTable table;
    private DefaultTableModel tableModel;
    private BarangService service;
    private KategoriService kategoriService;

    public BarangForm() {
        service = new BarangService();
        kategoriService = new KategoriService();
        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
        loadComboKategori();
        loadData();
        clear(); // Untuk men-generate ID awal
    }

    private void initComponents() {
        // Title
        JLabel lblTitle = new JLabel("Manajemen Data Barang");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Input Panel
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setOpaque(false);

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblId = new JLabel("ID Barang:");
        lblId.setFont(ThemeUtil.FONT_REGULAR);
        lblId.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblId, gbc);
        txtId = new JTextField(20);
        ThemeUtil.styleTextField(txtId);
        txtId.setEditable(false);
        gbc.gridx = 1;
        panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblKategori = new JLabel("Kategori:");
        lblKategori.setFont(ThemeUtil.FONT_REGULAR);
        lblKategori.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblKategori, gbc);
        cbKategori = new JComboBox<>();
        ThemeUtil.styleComboBox(cbKategori);
        gbc.gridx = 1;
        panelInput.add(cbKategori, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblNama = new JLabel("Nama Barang:");
        lblNama.setFont(ThemeUtil.FONT_REGULAR);
        lblNama.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblNama, gbc);
        txtNama = new JTextField(20);
        ThemeUtil.styleTextField(txtNama);
        gbc.gridx = 1;
        panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblSatuan = new JLabel("Satuan:");
        lblSatuan.setFont(ThemeUtil.FONT_REGULAR);
        lblSatuan.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblSatuan, gbc);
        txtSatuan = new JTextField(20);
        ThemeUtil.styleTextField(txtSatuan);
        gbc.gridx = 1;
        panelInput.add(txtSatuan, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblHarga = new JLabel("Harga:");
        lblHarga.setFont(ThemeUtil.FONT_REGULAR);
        lblHarga.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblHarga, gbc);
        txtHarga = new JTextField(20);
        ThemeUtil.styleTextField(txtHarga);
        gbc.gridx = 1;
        panelInput.add(txtHarga, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblStok = new JLabel("Stok:");
        lblStok.setFont(ThemeUtil.FONT_REGULAR);
        lblStok.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblStok, gbc);
        txtStok = new JTextField(20);
        ThemeUtil.styleTextField(txtStok);
        gbc.gridx = 1;
        panelInput.add(txtStok, gbc);

        // Button Panel
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        btnSimpan = new JButton("Simpan Data");
        ThemeUtil.styleButton(btnSimpan, new Color(16, 185, 129));
        
        btnUbah = new JButton("Simpan Perubahan");
        ThemeUtil.styleButton(btnUbah, new Color(59, 130, 246));
        
        btnHapus = new JButton("Hapus Data");
        ThemeUtil.styleButton(btnHapus, new Color(239, 68, 68));
        
        btnClear = new JButton("Bersihkan Form");
        ThemeUtil.styleButton(btnClear, new Color(100, 116, 139));
        btnRefresh = new JButton("Refresh Kategori");
        ThemeUtil.styleButton(btnRefresh, new Color(245, 158, 11)); // Amber 500

        panelBtn.add(btnSimpan);
        panelBtn.add(btnUbah);
        panelBtn.add(btnHapus);
        panelBtn.add(btnClear);
        panelBtn.add(btnRefresh);
        
        // Initial state for buttons (Option B UX)
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);

        JPanel wrapInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapInput.setOpaque(false);
        wrapInput.add(panelInput);

        panelTop.add(wrapInput, BorderLayout.NORTH);
        panelTop.add(panelBtn, BorderLayout.CENTER);

        JPanel wrapTop = new JPanel(new BorderLayout());
        wrapTop.setOpaque(false);
        wrapTop.add(panelTop, BorderLayout.WEST);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID Barang", "Nama Kategori", "Nama Barang", "Satuan", "Harga", "Stok"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        ThemeUtil.styleTable(table, scrollPane);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(wrapTop, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Actions
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnClear.addActionListener(e -> clear());
        btnRefresh.addActionListener(e -> loadComboKategori());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int row = table.getSelectedRow();
                if(row != -1) {
                    txtId.setText(table.getValueAt(row, 0).toString());
                    int catId = Integer.parseInt(table.getValueAt(row, 1).toString());
                    for (int i = 0; i < cbKategori.getItemCount(); i++) {
                        if (cbKategori.getItemAt(i).getIdKategori() == catId) {
                            cbKategori.setSelectedIndex(i);
                            break;
                        }
                    }
                    txtNama.setText(table.getValueAt(row, 2).toString());
                    txtSatuan.setText(table.getValueAt(row, 3).toString());
                    txtHarga.setText(table.getValueAt(row, 4).toString());
                    txtStok.setText(table.getValueAt(row, 5).toString());
                    
                    // Option B UX: Lock Simpan, Unlock Ubah & Hapus
                    btnSimpan.setEnabled(false);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void loadComboKategori() {
        cbKategori.removeAllItems();
        List<Kategori> list = kategoriService.getAllKategori();
        for (Kategori k : list) {
            cbKategori.addItem(k);
        }
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Barang> list = service.getAllBarang();
        for (Barang b : list) {
            tableModel.addRow(new Object[]{
                b.getIdBarang(), b.getIdKategori(), b.getNamaBarang(), 
                b.getSatuan(), b.getHargaJual(), b.getStok()
            });
        }
    }

    private void clear() {
        txtId.setText(service.generateId());
        txtNama.setText("");
        txtSatuan.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        if(cbKategori.getItemCount() > 0) cbKategori.setSelectedIndex(0);
        table.clearSelection();
        
        // Option B UX: Reset buttons
        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    private Barang readForm() {
        Kategori kat = (Kategori) cbKategori.getSelectedItem();
        return new Barang(
            txtId.getText(),
            kat != null ? kat.getIdKategori() : 0,
            txtNama.getText(),
            txtSatuan.getText(),
            Formatter.parseDoubleSafe(txtHarga.getText()),
            Formatter.parseIntSafe(txtStok.getText())
        );
    }

    private void simpan() {
        Barang b = readForm();
        if (service.tambahBarang(b)) {
            JOptionPane.showMessageDialog(this, "Data tersimpan!");
            clear(); loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data!");
        }
    }

    private void ubah() {
        if(txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!");
            return;
        }

        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris data di tabel terlebih dahulu!");
            return;
        }

        String oldNama = table.getValueAt(selectedRow, 2).toString();
        String oldHarga = table.getValueAt(selectedRow, 4).toString();
        String oldStok = table.getValueAt(selectedRow, 5).toString();

        Barang b = readForm();
        if (service.updateBarang(b)) {
            String msg = "Data Barang berhasil diperbarui!\n\n"
                       + "Nama Barang: " + oldNama + " -> " + b.getNamaBarang() + "\n"
                       + "Harga: " + oldHarga + " -> " + b.getHargaJual() + "\n"
                       + "Stok: " + oldStok + " -> " + b.getStok();
            JOptionPane.showMessageDialog(this, msg, "Informasi Perubahan", JOptionPane.INFORMATION_MESSAGE);
            clear(); loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
        }
    }

    private void hapus() {
        if(txtId.getText().isEmpty()) return;
        if (service.hapusBarang(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "Data dihapus!");
            clear(); loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
        }
    }
}
