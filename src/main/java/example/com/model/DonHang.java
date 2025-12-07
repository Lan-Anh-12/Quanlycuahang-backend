package example.com.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;




@Entity
@Table(name = "donhang")
public class DonHang {

    @Id
    @Column(name = "MaDH", length = 20)
    private String maDH;

    @Column(name = "MaKH", length = 20)
    private String maKH;

    @Column(name = "MaNV", length = 20)
    private String maNV;

    @Column(name = "NgayLap")
    private LocalDateTime ngayLap;

    @Column(name = "TongTien", nullable = false)
    private BigDecimal tongTien = BigDecimal.ZERO;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
    private List<CT_DonHang> chiTietDonHangs;

    @ManyToOne
    @JoinColumn(name = "MaKH", insertable = false, updatable = false)
    @JsonIgnore
    private KhachHang khachHang;


    public DonHang() {}
    
   


    public DonHang(String MaDH, String MaKH, String MaNV, LocalDateTime NgayLap, BigDecimal TongTien) {
        this.maDH = MaDH;
        this.maKH = MaKH;
        this.maNV = MaNV;
        this.ngayLap = NgayLap;
        this.tongTien = TongTien;
    }
    // Getters and Setters
    public String getMaDH() {
        return maDH;
    }
    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }
    public String getMaKH() {
        return maKH;
    }
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    public String getMaNV() {
        return maNV;
    }
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    public LocalDateTime getNgayLap() {
        return ngayLap;
    }
    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }
    public BigDecimal getTongTien() {
        return tongTien;
    }
    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }
    public List<CT_DonHang> getChiTietDonHangs() {
        return chiTietDonHangs;
    }
    public void setChiTietDonHangs(List<CT_DonHang> chiTietDonHangs) {
        this.chiTietDonHangs = chiTietDonHangs;
    }
}
