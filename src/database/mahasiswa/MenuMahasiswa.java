package database.mahasiswa;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuMahasiswa extends JFrame {

	private JPanel contentPane;
	private JTextField txtNIM;
	private JTextField txtNama;
	private JTable tabel;
	
	String header[] = {"NIM","Nama","Jurusan","Alamat"}; 
	DefaultTableModel tabelModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuMahasiswa frame = new MenuMahasiswa();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MenuMahasiswa() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNim = new JLabel("NIM");
		lblNim.setBounds(10, 11, 46, 14);
		contentPane.add(lblNim);
		
		JLabel lblNama = new JLabel("NAMA");
		lblNama.setBounds(10, 36, 46, 14);
		contentPane.add(lblNama);
		
		JLabel lblJurusan = new JLabel("JURUSAN");
		lblJurusan.setBounds(10, 61, 46, 14);
		contentPane.add(lblJurusan);
		
		JLabel lblAlamat = new JLabel("ALAMAT");
		lblAlamat.setBounds(10, 86, 46, 14);
		contentPane.add(lblAlamat);
		
		txtNIM = new JTextField();
		txtNIM.setBounds(66, 8, 358, 20);
		contentPane.add(txtNIM);
		txtNIM.setColumns(10);
		
		txtNama = new JTextField();
		txtNama.setBounds(66, 33, 358, 20);
		contentPane.add(txtNama);
		txtNama.setColumns(10);
		
		JComboBox cbJurusan = new JComboBox();
		cbJurusan.setModel(new DefaultComboBoxModel(new String[] {"TI", "SI", "Ekonomi"}));
		cbJurusan.setBounds(66, 58, 180, 20);
		contentPane.add(cbJurusan);
		
		JTextArea textAlamat = new JTextArea();
		textAlamat.setBounds(66, 86, 358, 38);
		contentPane.add(textAlamat);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(66, 135, 358, 92);
		contentPane.add(scrollPane);
		
		tabelModel = new DefaultTableModel(null,header); 
		tabel = new JTable(); 
		tabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				getData();
			}
		});
		tabel.setModel(tabelModel);
		scrollPane.setViewportView(tabel);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.setBounds(335, 227, 89, 23);
		contentPane.add(btnDelete);
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String jurusan = "";
				if(cbJurusan.getSelectedIndex() == 0){
					jurusan = "TI";
				}else if(cbJurusan.getSelectedIndex() == 1){
					jurusan = "SI";
				}else if(cbJurusan.getSelectedIndex() == 2){
					jurusan = "Ekonomi";
				}try{
					Connection konek = (Connection) Koneksi.getKoneksi();
					String query = "UPDATE mahasiswa SET Nama = ?, Jurusan = ?, Alamat = ? WHERE NIM = ?";
					PreparedStatement prepare = (PreparedStatement) konek.prepareStatement(query);
					
					prepare.setInt(1,Integer.parseInt(txtNIM.getText()));
					prepare.setString(2, txtNama.getText());
					prepare.setString(3, jurusan);
					prepare.setString(4, textAlamat.getText());
					
					prepare.executeUpdate();
					JOptionPane.showMessageDialog(null,"Data berhasil diupdate");
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null,"Data gagal diupdate");
					System.out.println(ex);
				}finally{
					getDataTable();
				}
			}
		});
		btnUpdate.setBounds(236, 227, 89, 23);
		contentPane.add(btnUpdate);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String jurusan = "";
				if(cbJurusan.getSelectedIndex() == 0){
					jurusan = "TI";
				}else if(cbJurusan.getSelectedIndex() == 1){
					jurusan = "SI";
				}else if(cbJurusan.getSelectedIndex() == 2){
					jurusan = "Ekonomi";
				}try{
					Connection konek = (Connection) Koneksi.getKoneksi();
					String query = "INSERT INTO mahasiswa VALUES(?,?,?,?)";
					PreparedStatement prepare = (PreparedStatement) konek.prepareStatement(query);
					
					prepare.setInt(1,Integer.parseInt(txtNIM.getText()));
					prepare.setString(2, txtNama.getText());
					prepare.setString(3, jurusan);
					prepare.setString(4, textAlamat.getText());
					
					prepare.executeUpdate();
					JOptionPane.showMessageDialog(null,"Data berhasil ditambahkan ke database");
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null,"Data gagal ditambahkan ke database");
					System.out.println(ex);
				}finally{
					getDataTable();
				}
			}
		});
		btnSave.setBounds(137, 227, 89, 23);
		contentPane.add(btnSave);
		getDataTable();
	}
	
	public void getDataTable(){
		try{
			Connection konek = (Connection) Koneksi.getKoneksi();
			Statement state = konek.createStatement();
			String query = "SELECT * FROM mahasiswa";
			ResultSet rs = state.executeQuery(query);
			while(rs.next()){
				Object obj[] = new Object[4];
				obj[0] = rs.getInt(1);
				obj[1] = rs.getString(2);
				obj[2] = rs.getString(3);
				obj[3] = rs.getString(4);
				tabelModel.addRow(obj);
			}
			rs.close();
			state.close();
		}catch(Exception ex){
			
		}
	}
	
	public void getData(){
		int pilih = tabel.getSelectedRow();
		if(pilih == -1){
			return; 
		}
		int nim = (int) tabelModel.getValueAt(pilih, 0);
		txtNIM.setText("" + nim);
		String nama = (String) tabelModel.getValueAt(pilih, 1);
		txtNama.setText(nama);
		String jurusan = (String) tabelModel.getValueAt(pilih, 2);
		cbJurusan.setSelectedItem(jurusan);
		String alamat = (String) tabelModel.getValueAt(pilih, 3);
		textAlamat.setText(alamat);
	}
}
