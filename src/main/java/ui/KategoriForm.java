package ui;

import model.Kategori;
import service.KategoriService;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import ui.components.RoundedPanel;
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
        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeUtil.BG_SOFT);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        initComponents();
        loadData();
        clear();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("Manajemen Kategori");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeUtil.OCEAN_BLUE);
        add(lblTitle, BorderLayout.NORTH);

        RoundedPanel panelTop = ThemeUtil.createCardPanel();
        panelTop.setLayout(new BorderLayout(10, 10));

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 15);
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

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        
        btnSimpan = new JButton("Simpan"); ThemeUtil.styleButton(btnSimpan, ThemeUtil.SUCCESS_COLOR);
        btnUbah = new JButton("Ubah"); ThemeUtil.styleButton(btnUbah, ThemeUtil.OCEAN_BLUE);
        btnHapus = new JButton("Hapus"); ThemeUtil.styleButton(btnHapus, ThemeUtil.ERROR_COLOR);
        btnClear = new JButton("Clear"); ThemeUtil.styleButton(btnClear, ThemeUtil.TEXT_SECONDARY);

        panelBtn.add(btnSimpan);
        panelBtn.add(btnUbah);
        panelBtn.add(btnHapus);
        panelBtn.add(btnClear);

        panelTop.add(panelInput, BorderLayout.CENTER);
        panelTop.add(panelBtn, BorderLayout.SOUTH);
        
        add(panelTop, BorderLayout.NORTH);

        RoundedPanel panelBottom = ThemeUtil.createCardPanel();
        panelBottom.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID Kategori", "Nama Kategori"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        ThemeUtil.styleTable(table, scrollPane);

        panelBottom.add(scrollPane, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.CENTER);

        // Events
        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clear());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtNama.setText(tableModel.getValueAt(row, 1).toString());
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
        table.clearSelection();
    }

    private void simpanData() {
        if (txtNama.getText().trim().isEmpty()) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Nama Kategori tidak boleh kosong!");
            return;
        }

        Kategori k = new Kategori();
        k.setNamaKategori(txtNama.getText().trim());
        
        if (service.tambahKategori(k)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil disimpan");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menyimpan data!");
        }
    }

    private void ubahData() {
        if (table.getSelectedRow() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih data yang akan diubah!");
            return;
        }

        Kategori k = new Kategori();
        try {
            k.setIdKategori(Integer.parseInt(txtId.getText()));
        } catch (Exception e) {}
        k.setNamaKategori(txtNama.getText().trim());
        
        if (service.updateKategori(k)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil diubah");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal mengubah data!");
        }
    }

    private void hapusData() {
        if (table.getSelectedRow() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih data yang akan dihapus!");
            return;
        }

        int idKat = 0;
        try {
            idKat = Integer.parseInt(txtId.getText());
        } catch (Exception e) {}

        if (service.hapusKategori(idKat)) {
            Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil dihapus");
            loadData();
            clear();
        } else {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menghapus data!");
        }
    }
}
