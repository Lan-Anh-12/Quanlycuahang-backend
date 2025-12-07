package example.com.model;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "nhanvien")
public class NhanVien {
    @Id
    @Column(name = "MaNV", length = 20)
    private String maNV;

    @Column(name = "HoTen")
    private String tenNV;
    @Column(name = "SoDienThoai")
    private String sDT;
    @Column(name = "email")
    private String email;
    @Column(name = "NgayVaoLam")
    private LocalDate ngayVaoLam;
    @Column(name = "TrangThai")
    private String trangThai;

    @Column(name = "MaTK", length = 20)
    private String maTK; // Foreign key to taikhoan

    @OneToOne
    @JoinColumn(name = "maTK") // cột FK trong bảng nhanvien
    private TaiKhoan taiKhoan;

    public NhanVien() {}  
    
    
    public NhanVien(String MaNV, String TenNV, String SDT, String Email, LocalDate NgayVaoLam, String MaTK, String trangThai) {
        this.maNV = MaNV;
        this.tenNV = TenNV;
        this.sDT = SDT;
        this.email = Email;
        this.ngayVaoLam = NgayVaoLam;
        this.maTK = MaTK;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaNV() {
        return maNV;
    }
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    public String getTenNV() {
        return tenNV;
    }
    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }
    public String getSDT() {
        return sDT;
    }
    public void setSDT(String sDT) {
        this.sDT = sDT;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }
    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }
    public String getMaTK() {
        return maTK;
    }
    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }
    public String getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }
    
    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

}
