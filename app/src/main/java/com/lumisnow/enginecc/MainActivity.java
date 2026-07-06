package com.lumisnow.enginecc;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilDuongKinh, tilHanhTrinh, tilSoXilanh, tilRPM;
    private TextInputEditText edtDuongKinh, edtHanhTrinh, edtSoXilanh, edtRPM;
    private TextView txtKetQua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        tilDuongKinh = findViewById(R.id.tilDuongKinh);
        tilHanhTrinh = findViewById(R.id.tilHanhTrinh);
        tilSoXilanh = findViewById(R.id.tilSoXilanh);
        tilRPM = findViewById(R.id.tilRPM);

        edtDuongKinh = findViewById(R.id.edtDuongKinh);
        edtHanhTrinh = findViewById(R.id.edtHanhTrinh);
        edtSoXilanh = findViewById(R.id.edtSoXilanh);
        edtRPM = findViewById(R.id.edtRPM);

        txtKetQua = findViewById(R.id.txtKetQua);
        MaterialButton btnTinh = findViewById(R.id.btnTinh);

        btnTinh.setOnClickListener(v -> tinhToan());

        // Hỗ trợ tính nhanh khi nhấn Done ở ô cuối (RPM)
        edtRPM.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tinhToan();
                return true;
            }
            return false;
        });
    }

    private void tinhToan() {
        tilDuongKinh.setError(null);
        tilHanhTrinh.setError(null);

        String strDK = edtDuongKinh.getText().toString().trim();
        String strHT = edtHanhTrinh.getText().toString().trim();
        String strSL = edtSoXilanh.getText().toString().trim();
        String strRPM = edtRPM.getText().toString().trim();

        if (strDK.isEmpty()) {
            tilDuongKinh.setError("Nhập dữ liệu");
            return;
        }
        if (strHT.isEmpty()) {
            tilHanhTrinh.setError("Nhập dữ liệu");
            return;
        }

        try {
            double dK = Double.parseDouble(strDK);
            double hT = Double.parseDouble(strHT);
            int n = strSL.isEmpty() ? 1 : Integer.parseInt(strSL);
            double rpm = strRPM.isEmpty() ? 0 : Double.parseDouble(strRPM);

            if (dK <= 0 || hT <= 0 || n <= 0) {
                Toast.makeText(this, "Thông số không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            double areaMm2 = (Math.PI / 4.0) * Math.pow(dK, 2);
            double ccSingle = areaMm2 * hT / 1000.0;
            double ccTotal = ccSingle * n;
            double boreStrokeRatio = dK / hT;

            StringBuilder sb = new StringBuilder();

            // --- DUNG TÍCH ---
            sb.append("┃ DUNG TÍCH\n");
            if (n > 1) {
                sb.append(String.format(Locale.getDefault(), "  Mỗi xi-lanh: %.2f cc\n", ccSingle));
                sb.append(String.format(Locale.getDefault(), "  Tổng (%d xi-lanh): %.2f cc\n", n, ccTotal));
            } else {
                sb.append(String.format(Locale.getDefault(), "  %.2f cc\n", ccTotal));
            }
            
            sb.append("  Công thức: V = (π × D² × S) / 4\n");
            sb.append(String.format(Locale.getDefault(), "  = (π × %.1f² × %.1f) / 4\n", dK, hT));
            sb.append(String.format(Locale.getDefault(), "  = %.2f cc\n\n", ccSingle));

            // --- DIỆN TÍCH PISTON ---
            sb.append("┃ DIỆN TÍCH PISTON\n");
            sb.append(String.format(Locale.getDefault(), "  %.2f mm² | %.2f cm²\n\n", areaMm2, areaMm2 / 100.0));

            // --- PHÂN LOẠI ---
            sb.append("┃ PHÂN LOẠI ĐỘNG CƠ\n");
            sb.append(String.format(Locale.getDefault(), "  Tỷ lệ Bore/Stroke: %.3f\n", boreStrokeRatio));
            if (boreStrokeRatio < 0.98) {
                sb.append("  ➡ Hành trình dài (Long Stroke)\n");
                sb.append("  ➡ Đặc tính: Ưu tiên mô-men xoắn ở tua máy thấp\n");
            } else if (boreStrokeRatio <= 1.02) {
                sb.append("  ➡ Máy vuông (Square Engine)\n");
                sb.append("  ➡ Đặc tính: Cân bằng lực kéo và tốc độ máy\n");
            } else {
                sb.append("  ➡ Hành trình ngắn (Oversquare)\n");
                sb.append("  ➡ Đặc tính: Ưu tiên công suất ở tua máy cao\n");
            }

            // --- TỐC ĐỘ PISTON ---
            if (rpm > 0) {
                double mps = (2 * hT * rpm) / 60000.0;
                String status;
                if (mps < 15) status = "Bình thường";
                else if (mps < 20) status = "Khá cao";
                else if (mps < 25) status = "Cao";
                else status = "Rất cao (Nguy hiểm)";
                
                sb.append(String.format(Locale.getDefault(), "\n┃ TỐC ĐỘ PISTON (@%.0f RPM)\n", rpm));
                sb.append(String.format(Locale.getDefault(), "  %.2f m/s (%s)\n", mps, status));
            }

            txtKetQua.setText(sb.toString());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Dữ liệu nhập không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
