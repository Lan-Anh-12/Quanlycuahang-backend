package example.com.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import java.util.List;



import java.time.LocalDateTime;

@Entity
@Table(name = "nhapkho")
public class NhapKho {
    @Id
    @Column(name = "MaNK", length = 20)
    private String maNK;

    @Column(name = "MaNV", length = 20)
    private String maNV; // Foreign key to nhanvien

    @Column(name = "NhaCungCap")
    private String nhaCungCap;
    @Column(name = "NgayNhap")
    private LocalDateTime ngayNhap;
    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @OneToMany(mappedBy = "nhapKho", cascade = CascadeType.ALL)
    private List<CT_NhapKho> chiTietNhapKhos;

    public NhapKho() {}

    
    public NhapKho( String MaNK, String MaNV, String NhaCungCap, BigDecimal TongTien) {
        
        this.maNK = MaNK;
        this.maNV = MaNV;
        this.nhaCungCap = NhaCungCap;
        this.tongTien = TongTien;
    }
    // Getters and Setters
    public String getMaNK() {
        return maNK;
    }
    public void setMaNK(String maNK) {
        this.maNK = maNK;
    }
    public List<CT_NhapKho> getChiTietNhapKhos() {
        return chiTietNhapKhos;
    }
    public void setChiTietNhapKhos(List<CT_NhapKho> chiTietNhapKhos) {
        this.chiTietNhapKhos = chiTietNhapKhos;
    }
    public String getMaNV() {
        return maNV;
    }
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    public String getNhaCungCap() {
        return nhaCungCap;
    }
    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }
    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }
    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
    public BigDecimal getTongTien() {
        return tongTien;
    }
    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

}
