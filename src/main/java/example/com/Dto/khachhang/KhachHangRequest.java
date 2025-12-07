package example.com.Dto.khachhang;

import lombok.Data;

@Data
public class KhachHangRequest {
    private String tenKH;
    private Integer namSinh;
    private String diaChi;
    private String sdt;
}