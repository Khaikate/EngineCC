# EngineCC Professional - Máy Tính Động Cơ

**EngineCC Professional** là một công cụ Android mạnh mẽ dành cho kỹ thuật viên, thợ máy và sinh viên cơ khí. Ứng dụng cung cấp các phép tính chính xác về thông số kỹ thuật động cơ với giao diện hiện đại và chuyên nghiệp.

## 🚀 Tính năng nổi bật
- **Tính toán Dung tích (CC):** Hỗ trợ tính toán chi tiết cho động cơ từ 1 đến nhiều xi-lanh.
- **Phân loại Động cơ:** Tự động nhận diện loại máy (Long Stroke, Square, Oversquare) dựa trên tỷ lệ Bore/Stroke.
- **Tốc độ Piston:** Tính toán tốc độ trung bình của piston (m/s) dựa trên RPM và đưa ra cảnh báo an toàn.
- **Diện tích Piston:** Cung cấp thông số diện tích mặt piston (mm² và cm²).
- **Giao diện Material 3:** Thiết kế đồng bộ, hỗ trợ Dark Mode hoàn toàn.
- **Trình bày Kỹ thuật:** Kết quả hiển thị theo phong cách "Line-style" chuyên nghiệp, có kèm diễn giải công thức chi tiết.

## 📐 Công thức kỹ thuật
Ứng dụng áp dụng các công thức chuẩn trong ngành cơ khí động cơ:

### 1. Dung tích xi-lanh (V)
> **V = (π × D² × S) / 4**
- **D**: Đường kính piston (mm)
- **S**: Hành trình piston (mm)

### 2. Tốc độ trung bình Piston (MPS)
> **MPS = (2 × S × RPM) / 60,000**
- Kết quả tính bằng mét trên giây (m/s).

### 3. Tỷ lệ Bore/Stroke
> **Ratio = D / S**

## 🛠 Công nghệ sử dụng
- **Ngôn ngữ:** Java
- **UI Framework:** Material Components for Android (Material 3)
- **Layout:** CoordinatorLayout & ConstraintLayout (Tối ưu không gian hiển thị)
- **Hệ thống màu:** Dynamic Palette hỗ trợ Dark Mode/Light Mode đồng bộ.

## 📥 Cài đặt
1. Clone dự án:
   ```bash
   git clone https://github.com/LumiSnow/EngineCC.git
   ```
2. Mở bằng **Android Studio**.
3. Yêu cầu hệ điều hành Android 10.0+ (API 29 trở lên).

---
Phát triển bởi **LumiSnow** và đây là sản phẩm của **Vibe Coding**
