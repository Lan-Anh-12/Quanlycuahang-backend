package example.com.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;

@Entity
@Table(name = "ct_nhapkho")
public class CT_NhapKho {
    @Id
    @Column(name = "MaCTNK", length = 20)
    private String maCTNK;

    @Column(name = "MaNK", length = 20)
    private String maNK; // Foreign key to nhapkho

    @Column(name = "MaSP", length = 20)
    private String maSP;
    @Column(name = "SoLuong")
    private int soLuong;
    @Column(name = "DonGia")
    private BigDecimal donGia;
    @Column(name = "ThanhTien")
    private BigDecimal thanhTien;

    @ManyToOne
    @JoinColumn(name = "MaNK", insertable = false, updatable = false)
    @JsonIgnore
    private NhapKho nhapKho;

    public CT_NhapKho() {}

   

    public CT_NhapKho(String MaCTNK, String MaNK, String MaSP, int SoLuong, BigDecimal DonGia, BigDecimal ThanhTien) {
        this.maCTNK = MaCTNK;
        this.maNK = MaNK;
        this.maSP = MaSP;
        this.soLuong = SoLuong;
        this.donGia = DonGia;
        this.thanhTien = ThanhTien;
    }

    // Getters and Setters
    public String getMaCTNK() {
        return maCTNK;
    }
    public void setMaCTNK(String maCTNK) {
        this.maCTNK = maCTNK;
    }
    public String getMaNK() {
        return maNK;
    }
    public void setMaNK(String maNK) {
         this.maNK = maNK;
    }
    public String getMaSP() {
        return maSP;
    }
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }
    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    public BigDecimal getDonGia() {
        return donGia;
    }
    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
    public BigDecimal getThanhTien() {
        return thanhTien;
    }
    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }
    

}
