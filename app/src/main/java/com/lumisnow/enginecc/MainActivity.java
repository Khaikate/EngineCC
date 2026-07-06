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

    private TextInputLayout tilDuongKinh, tilHanhTrinh, tilSoXilanh;
    private TextInputEditText edtDuongKinh, edtHanhTrinh, edtSoXilanh;
    private TextView txtKetQua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        // Layouts for error display
        tilDuongKinh = findViewById(R.id.tilDuongKinh);
        tilHanhTrinh = findViewById(R.id.tilHanhTrinh);
        tilSoXilanh = findViewById(R.id.tilSoXilanh);

        // Input fields
        edtDuongKinh = findViewById(R.id.edtDuongKinh);
        edtHanhTrinh = findViewById(R.id.edtHanhTrinh);
        edtSoXilanh = findViewById(R.id.edtSoXilanh);

        txtKetQua = findViewById(R.id.txtKetQua);
        MaterialButton btnTinh = findViewById(R.id.btnTinh);

        btnTinh.setOnClickListener(v -> tinhDungTich());

        edtSoXilanh.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tinhDungTich();
                return true;
            }
            return false;
        });
    }

    private void tinhDungTich() {
        // Clear previous errors
        tilDuongKinh.setError(null);
        tilHanhTrinh.setError(null);
        tilSoXilanh.setError(null);

        String strDK = edtDuongKinh.getText().toString().trim();
        String strHT = edtHanhTrinh.getText().toString().trim();
        String strSL = edtSoXilanh.getText().toString().trim();

        boolean hasError = false;

        if (strDK.isEmpty()) {
            tilDuongKinh.setError("Nhập đường kính");
            hasError = true;
        }
        if (strHT.isEmpty()) {
            tilHanhTrinh.setError("Nhập hành trình");
            hasError = true;
        }

        if (hasError) return;

        int soXilanh = strSL.isEmpty() ? 1 : Integer.parseInt(strSL);
        if (strSL.isEmpty()) edtSoXilanh.setText("1");

        try {
            double duongKinh = Double.parseDouble(strDK);
            double hanhTrinh = Double.parseDouble(strHT);

            if (duongKinh <= 0 || hanhTrinh <= 0 || soXilanh <= 0) {
                Toast.makeText(this, "Giá trị phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate CC
            double dungTichCc = (Math.PI / 4.0) * Math.pow(duongKinh, 2) * hanhTrinh * soXilanh / 1000.0;

            // Result presentation
            String resultText = String.format(Locale.getDefault(),
                    "KẾT QUẢ: %.2f cc\n\n(Ø%.1f × %.1f × %d xi-lanh)",
                    dungTichCc, duongKinh, hanhTrinh, soXilanh);
            
            txtKetQua.setText(resultText);
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lỗi định dạng số", Toast.LENGTH_SHORT).show();
        }
    }
}
