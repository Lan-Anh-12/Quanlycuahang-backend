package example.com.Dto.donhang;
import java.util.List;


import lombok.Data;


@Data
public class DonHangRequest {

    private String maKH; // null nếu khách mới
    private String tenKH; // chỉ cần khi tạo khách mới
    private String maNV;
    private Integer namSinh; // chỉ cần khi tạo khách mới
    private String diaChi;// chỉ cần khi tạo khách mới
    private String sdt; // chỉ cần khi tạo khách mới
    private List<ChiTietDonHangRequest> chiTietDonHangs;
}


