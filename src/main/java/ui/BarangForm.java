package ui;

import model.Barang;
import model.Kategori;
import service.BarangService;
import service.KategoriService;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import util.ThemeUtil;
import ui.components.RoundedPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BarangForm extends JPanel {

    private JTextField txtId, txtNama, txtSatuan, txtHargaJual, txtStok;
    private JComboBox<String> cbKategori;
    private JButton btnSimpan, btnUbah, btnHapus, btnClear;
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
        gbc.gridx = 1; panelInput.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; addLabel(panelInput, "Kategori:", gbc);
        cbKategori = new JComboBox<>(); ThemeUtil.styleComboBox(cbKategori);
        gbc.gridx = 1; panelInput.add(cbKategori, gbc);

        gbc.gridx = 0; gbc.gridy = 3; addLabel(panelInput, "Satuan:", gbc);
        txtSatuan = new JTextField(20); ThemeUtil.styleTextField(txtSatuan);
        gbc.gridx = 1; panelInput.add(txtSatuan, gbc);

        gbc.gridx = 0; gbc.gridy = 4; addLabel(panelInput, "Harga Jual:", gbc);
        txtHargaJual = new JTextField(20); ThemeUtil.styleTextField(txtHargaJual);
        gbc.gridx = 1; panelInput.add(txtHargaJual, gbc);

        gbc.gridx = 0; gbc.gridy = 5; addLabel(panelInput, "Stok:", gbc);
        txtStok = new JTextField(20); ThemeUtil.styleTextField(txtStok);
        gbc.gridx = 1; panelInput.add(txtStok, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBtn.setOpaque(false);
        btnSimpan = new JButton("Simpan"); ThemeUtil.styleButton(btnSimpan, ThemeUtil.SUCCESS_COLOR);
        btnUbah = new JButton("Ubah"); ThemeUtil.styleButton(btnUbah, ThemeUtil.OCEAN_BLUE);
        btnHapus = new JButton("Hapus"); ThemeUtil.styleButton(btnHapus, ThemeUtil.ERROR_COLOR);
        btnClear = new JButton("Clear"); ThemeUtil.styleButton(btnClear, ThemeUtil.TEXT_SECONDARY);

        panelBtn.add(btnSimpan); panelBtn.add(btnUbah); panelBtn.add(btnHapus); panelBtn.add(btnClear);

        panelTop.add(panelInput, BorderLayout.WEST);
        panelTop.add(panelBtn, BorderLayout.SOUTH);
        
        add(panelTop, BorderLayout.NORTH);

        RoundedPanel panelBottom = ThemeUtil.createCardPanel();
        panelBottom.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID Barang", "Nama Barang", "Kategori", "Satuan", "Harga Jual", "Stok"}, 0) {
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

        ActionListener enterSubmit = e -> {
            if (table.getSelectedRow() >= 0) btnUbah.doClick();
            else btnSimpan.doClick();
        };
        txtNama.addActionListener(enterSubmit);
        txtSatuan.addActionListener(enterSubmit);
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
                    txtSatuan.setText(tableModel.getValueAt(row, 3).toString());
                    txtHargaJual.setText(tableModel.getValueAt(row, 4).toString().replace(".0", ""));
                    txtStok.setText(tableModel.getValueAt(row, 5).toString());
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
        cbKategori.removeAllItems();
        listKategori = kategoriService.getAllKategori();
        for (Kategori k : listKategori) {
            cbKategori.addItem(k.getNamaKategori());
        }
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Barang> list = barangService.getAllBarang();
        for (Barang b : list) {
            String namaKat = "";
            for(Kategori k : listKategori) {
                if(k.getIdKategori() == b.getIdKategori()) {
                    namaKat = k.getNamaKategori();
                    break;
                }
            }
            tableModel.addRow(new Object[]{
                b.getIdBarang(), b.getNamaBarang(), namaKat, 
                b.getSatuan(), b.getHargaJual(), b.getStok()
            });
        }
    }

    private void clear() {
        txtId.setText(barangService.generateId());
        txtId.setEditable(false);
        txtNama.setText("");
        txtSatuan.setText("");
        txtHargaJual.setText("");
        txtStok.setText("");
        if(cbKategori.getItemCount() > 0) cbKategori.setSelectedIndex(0);
        table.clearSelection();
    }

    private void simpanData() {
        if (txtNama.getText().trim().isEmpty()) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Nama Barang tidak boleh kosong!");
            return;
        }

        try {
            Barang b = new Barang();
            b.setIdBarang(txtId.getText());
            b.setNamaBarang(txtNama.getText().trim());
            b.setIdKategori(listKategori.get(cbKategori.getSelectedIndex()).getIdKategori());
            b.setSatuan(txtSatuan.getText().trim());
            b.setHargaJual(Double.parseDouble(txtHargaJual.getText().trim()));
            b.setStok(Integer.parseInt(txtStok.getText().trim()));
            
            if (barangService.tambahBarang(b)) {
                Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil disimpan");
                loadData();
                clear();
            } else {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal menyimpan data!");
            }
        } catch (NumberFormatException ex) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Harga/Stok harus angka!");
        }
    }

    private void ubahData() {
        if (table.getSelectedRow() < 0) {
            Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Pilih data yang akan diubah!");
            return;
        }

        try {
            Barang b = new Barang();
            b.setIdBarang(txtId.getText());
            b.setNamaBarang(txtNama.getText().trim());
            b.setIdKategori(listKategori.get(cbKategori.getSelectedIndex()).getIdKategori());
            b.setSatuan(txtSatuan.getText().trim());
            b.setHargaJual(Double.parseDouble(txtHargaJual.getText().trim()));
            b.setStok(Integer.parseInt(txtStok.getText().trim()));
            
            if (barangService.updateBarang(b)) {
                Toast.showSuccess((JFrame) SwingUtilities.getWindowAncestor(this), "Data berhasil diubah");
                loadData();
                clear();
            } else {
                Toast.showError((JFrame) SwingUtilities.getWindowAncestor(this), "Gagal mengubah data!");
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
