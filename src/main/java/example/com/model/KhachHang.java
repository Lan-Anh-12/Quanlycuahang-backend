package example.com.model;
import jakarta.persistence.*;
import java.util.List;




@Entity
@Table(name = "khachhang")
public class KhachHang {
    @Id
    @Column(name = "MaKH", length = 20)
    private String maKH;

    @Column(name = "HoTen")
    private String tenKH;   
    @Column(name = "NamSinh")
    private int namSinh;
    @Column(name = "DiaChi")
    private String diaChi;
    @Column(name = "sdt")
    private String sdt;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL)
    private java.util.List<DonHang> donHangs;
    
    public KhachHang() {}

    
    public KhachHang(String MaKH, String TenKH, int NamSinh, String DiaChi, String sdt) {
        this.maKH = MaKH;
        this.tenKH = TenKH;
        this.namSinh = NamSinh;
        this.diaChi = DiaChi;
        this.sdt = sdt;
    }
    // Getters and Setters
    public String getMaKH() {
        return maKH;
    }
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    public String getTenKH() {
        return tenKH;
    }
    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }
    public int getNamSinh() {
        return namSinh;
    }   
    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }
    public String getDiaChi() {
        return diaChi;
    }
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
    public String getsdt() {
        return sdt;
    }
    public void setsdt(String sdt) {
        this.sdt = sdt;
    }
    public List<DonHang> getDonHangs() {
        return donHangs;
    }

    public void setDonHangs(List<DonHang> donHangs) {
        this.donHangs = donHangs;
    }


}
