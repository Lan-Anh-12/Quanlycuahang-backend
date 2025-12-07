package example.com.Service.inventory;

import java.util.List;

import example.com.Dto.khohang.NhapKhoRequest;
import example.com.Dto.khohang.NhapKhoResponse;
import example.com.Dto.sanpham.SanPhamRequest;
import example.com.Dto.sanpham.SanPhamResponse;

public interface InventoryService {

    // Nhập kho từ FE
    NhapKhoResponse nhapKho(NhapKhoRequest request);

    // Lấy danh sách phiếu nhập
    List<NhapKhoResponse> layTatCaPhieuNhap();

    // // Lấy 1 phiếu nhập
    NhapKhoResponse layPhieuNhapTheoMa(String maNK);

    // Cập nhật phiếu nhập 
    NhapKhoResponse capNhatPhieuNhap(String maNK, NhapKhoRequest request);

    // kiểm tra tồn kho sản phẩm
    int xemTonKho(String maSP);

    int kiemTraSoLuongSpTheoTen(String keyword);

    // Sản phẩm
    List<SanPhamResponse> sanPhamConBan();
    
    // Lấy URL hình ảnh theo mã sản phẩm
    String layUrlTheoMa(String maSp);

    // CRUD sản phẩm
    // Tạo sản phẩm mới
    SanPhamResponse taoSanPhamMoi(SanPhamRequest req);

    // Xóa sản phẩm
    void xoaSanPham(String maSP);

    // Cập nhật sản phẩm
    SanPhamResponse capNhatSanPham(String maSP, SanPhamRequest req);

    // lấy theo mã nhập hoặc nhà cung cấp
    List<NhapKhoResponse> layTheoMaNKOrNCC(String keyword);

    // lấy sản phẩm theo tên
    List<SanPhamResponse> timSanPhamTheoTen(String tenSP);

}
