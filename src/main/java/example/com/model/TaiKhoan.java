package example.com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "taikhoan")
public class TaiKhoan {
    @Id
    @Column(name = "MaTK", length = 20)
    private String maTK;

    @Column(name = "username")
    private String username;
    @Column(name = "PasswordHash")
    private String matKhau;
    @Column(name = "role")
    private String role;

    public TaiKhoan() {}
    
    
    public TaiKhoan(String MaTK, String username, String MatKhau, String Role) {
        this.maTK = MaTK;
        this.username = username;
        this.matKhau = MatKhau;
        this.role = Role;
    }
    // Getters and Setters
    public String getMaTK() {
        return maTK;
    }
    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMatKhau() {
        return matKhau;
    }
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    
    
}
