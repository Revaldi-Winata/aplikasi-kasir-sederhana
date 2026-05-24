package ui;

import model.Kategori;
import service.KategoriService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class KategoriForm extends JPanel {

    private JTextField txtId, txtNama;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private KategoriService service;

    public KategoriForm() {
        service = new KategoriService();
        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
        loadData();
        clear(); // Untuk men-generate ID prediksi awal
    }

    private void initComponents() {
        // Title
        JLabel lblTitle = new JLabel("Manajemen Data Kategori");
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
        JLabel lblId = new JLabel("ID Kategori:");
        lblId.setFont(ThemeUtil.FONT_REGULAR);
        lblId.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblId, gbc);
        
        txtId = new JTextField(20);
        ThemeUtil.styleTextField(txtId);
        txtId.setEditable(false);
        gbc.gridx = 1;
        panelInput.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNama = new JLabel("Nama Kategori:");
        lblNama.setFont(ThemeUtil.FONT_REGULAR);
        lblNama.setForeground(ThemeUtil.TEXT_SECONDARY);
        panelInput.add(lblNama, gbc);
        
        txtNama = new JTextField(20);
        ThemeUtil.styleTextField(txtNama);
        gbc.gridx = 1;
        panelInput.add(txtNama, gbc);

        // Button Panel
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        btnSimpan = new JButton("Simpan Data");
        ThemeUtil.styleButton(btnSimpan, new Color(16, 185, 129)); // Emerald 500
        
        btnUbah = new JButton("Simpan Perubahan");
        ThemeUtil.styleButton(btnUbah, new Color(59, 130, 246)); // Blue 500
        
        btnHapus = new JButton("Hapus Data");
        ThemeUtil.styleButton(btnHapus, new Color(239, 68, 68)); // Red 500
        
        btnClear = new JButton("Bersihkan Form");
        ThemeUtil.styleButton(btnClear, new Color(100, 116, 139)); // Slate 500

        panelBtn.add(btnSimpan);
        panelBtn.add(btnUbah);
        panelBtn.add(btnHapus);
        panelBtn.add(btnClear);
        
        // Initial state for buttons (Option B UX)
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(true);

        JPanel wrapInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapInput.setOpaque(false);
        wrapInput.add(panelInput);

        panelTop.add(wrapInput, BorderLayout.NORTH);
        panelTop.add(panelBtn, BorderLayout.CENTER);
        
        // Wrap panelTop in another panel to align it to the top
        JPanel wrapTop = new JPanel(new BorderLayout());
        wrapTop.setOpaque(false);
        wrapTop.add(panelTop, BorderLayout.WEST);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID Kategori", "Nama Kategori"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Mencegah edit langsung di dalam sel tabel
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

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int row = table.getSelectedRow();
                if(row != -1) {
                    txtId.setText(table.getValueAt(row, 0).toString());
                    txtNama.setText(table.getValueAt(row, 1).toString());
                    
                    // Option B UX: Lock Simpan, Unlock Ubah & Hapus
                    btnSimpan.setEnabled(false);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                }
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Kategori> list = service.getAllKategori();
        for (Kategori k : list) {
            tableModel.addRow(new Object[]{k.getIdKategori(), k.getNamaKategori()});
        }
    }

    private void clear() {
        txtId.setText(service.getNextAutoIncrement());
        txtNama.setText("");
        txtNama.requestFocus();
        table.clearSelection();
        
        // Option B UX: Reset buttons
        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    private void simpan() {
        if(txtNama.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Kategori harus diisi!");
            return;
        }
        Kategori k = new Kategori(0, txtNama.getText());
        if (service.tambahKategori(k)) {
            JOptionPane.showMessageDialog(this, "Data tersimpan!");
            clear();
            loadData();
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

        String oldNama = table.getValueAt(selectedRow, 1).toString();
        String newNama = txtNama.getText();

        if (oldNama.equals(newNama)) {
            JOptionPane.showMessageDialog(this, "Tidak ada perubahan data.");
            return;
        }

        Kategori k = new Kategori(Integer.parseInt(txtId.getText()), newNama);
        if (service.updateKategori(k)) {
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!\n\nNama Kategori: " + oldNama + " -> " + newNama, "Informasi Perubahan", JOptionPane.INFORMATION_MESSAGE);
            clear();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
        }
    }

    private void hapus() {
        if(txtId.getText().isEmpty()) return;
        int id = Integer.parseInt(txtId.getText());
        if (service.hapusKategori(id)) {
            JOptionPane.showMessageDialog(this, "Data dihapus!");
            clear();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data! Mungkin data sedang digunakan di tabel Barang.");
        }
    }
}
