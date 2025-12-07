package example.com.Dto.donhang;

import java.util.List;

import lombok.Data;

@Data
public class CapNhatDonHangRequest {
    private String maDH;
    private List<ChiTietDonHangRequest> chiTiet;
}
