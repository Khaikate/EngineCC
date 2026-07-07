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

    private TextInputLayout tilDuongKinh, tilHanhTrinh;
    private TextInputEditText edtDuongKinh, edtHanhTrinh, edtSoXilanh, edtRPM;
    private TextView tvMainCC, tvEngineType, tvPistonSpeed, tvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        tilDuongKinh = findViewById(R.id.tilDuongKinh);
        tilHanhTrinh = findViewById(R.id.tilHanhTrinh);

        edtDuongKinh = findViewById(R.id.edtDuongKinh);
        edtHanhTrinh = findViewById(R.id.edtHanhTrinh);
        edtSoXilanh = findViewById(R.id.edtSoXilanh);
        edtRPM = findViewById(R.id.edtRPM);

        tvMainCC = findViewById(R.id.tvMainCC);
        tvEngineType = findViewById(R.id.tvEngineType);
        tvPistonSpeed = findViewById(R.id.tvPistonSpeed);
        tvDetails = findViewById(R.id.tvDetails);
        
        MaterialButton btnTinh = findViewById(R.id.btnTinh);

        btnTinh.setOnClickListener(v -> tinhToan());

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
            tilDuongKinh.setError("!");
            return;
        }
        if (strHT.isEmpty()) {
            tilHanhTrinh.setError("!");
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

            // 1. Update CC (Main Dashboard)
            tvMainCC.setText(String.format(Locale.getDefault(), "%.2f cc", ccTotal));

            // 2. Update Engine Type
            String engineType;
            String description;
            if (boreStrokeRatio < 0.98) {
                engineType = "Long Stroke";
                description = "Ưu tiên mô-men xoắn ở tua máy thấp";
            } else if (boreStrokeRatio <= 1.02) {
                engineType = "Square Engine";
                description = "Cân bằng lực kéo và tốc độ máy";
            } else {
                engineType = "Oversquare";
                description = "Ưu tiên công suất ở tua máy cao";
            }
            tvEngineType.setText(engineType);

            // 3. Update Piston Speed
            double mps = 0;
            String status = "";
            if (rpm > 0) {
                mps = (2 * hT * rpm) / 60000.0;
                if (mps < 15) status = "Bình thường";
                else if (mps < 20) status = "Khá cao";
                else if (mps < 25) status = "Cao";
                else status = "Nguy hiểm";
                tvPistonSpeed.setText(String.format(Locale.getDefault(), "%.1f m/s", mps));
            } else {
                tvPistonSpeed.setText("---");
            }

            // 4. Update Detailed Analysis
            StringBuilder details = new StringBuilder();
            details.append("┃ PHÂN TÍCH CHI TIẾT\n\n");
            if (n > 1) {
                details.append(String.format(Locale.getDefault(), "  • Mỗi xi-lanh: %.2f cc\n", ccSingle));
                details.append(String.format(Locale.getDefault(), "  • Tổng %d xi-lanh: %.2f cc\n", n, ccTotal));
            }
            details.append(String.format(Locale.getDefault(), "  • Diện tích Piston: %.1f mm²\n", areaMm2));
            details.append(String.format(Locale.getDefault(), "  • Tỷ lệ B/S: %.3f\n", boreStrokeRatio));
            details.append("  • Đặc tính: ").append(description).append("\n\n");
            
            // --- CÔNG THỨC DUNG TÍCH ---
            details.append("┃ CÔNG THỨC DUNG TÍCH\n");
            details.append("  V = (π × D² × S) / 4\n");
            details.append(String.format(Locale.getDefault(), "  V = (π × %.1f² × %.1f) / 4\n", dK, hT));
            details.append(String.format(Locale.getDefault(), "  = %.2f cc / xi-lanh\n", ccSingle));

            if (n > 1) {
                details.append(String.format(
                        Locale.getDefault(),
                        "  Tổng = %.2f × %d = %.2f cc\n\n",
                        ccSingle,
                        n,
                        ccTotal
                ));
            } else {
                details.append("\n");
            }
            // --- CÔNG THỨC TỐC ĐỘ PISTON ---
            if (rpm > 0) {
                details.append("┃ CÔNG THỨC TỐC ĐỘ PISTON\n");
                details.append("  MPS = (2 × S × RPM) / 60000\n");
                details.append(String.format(Locale.getDefault(), "  MPS = (2 × %.1f × %.0f) / 60000\n", hT, rpm));
                details.append(String.format(Locale.getDefault(), "  = %.2f m/s (%s)", mps, status));
            }
            
            tvDetails.setText(details.toString());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Dữ liệu nhập không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
