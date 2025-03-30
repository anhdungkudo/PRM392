package vn.edu.fpt.lovetimerapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView loveTimeText;
    private Button setDateButton;
    private EditText relationshipNameEditText;
    private long startDateInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loveTimeText = findViewById(R.id.loveTimeText);
        setDateButton = findViewById(R.id.setDateButton);
        relationshipNameEditText = findViewById(R.id.relationshipName); // Khai báo EditText

        // Lấy ngày bắt đầu từ SharedPreferences (nếu có)
        SharedPreferences sharedPref = getSharedPreferences("LoveApp", MODE_PRIVATE);
        startDateInMillis = sharedPref.getLong("startDate", 0);

        // Nếu đã lưu ngày bắt đầu, tính toán và hiển thị thời gian
        if (startDateInMillis != 0) {
            updateLoveTime();
        }

        // Mở DatePicker khi nhấn nút Set Start Date
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    // Hiển thị dialog chọn ngày bắt đầu
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // Cập nhật ngày bắt đầu và lưu vào SharedPreferences
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        startDateInMillis = selectedDate.getTimeInMillis();
                        SharedPreferences sharedPref = getSharedPreferences("LoveApp", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong("startDate", startDateInMillis);
                        editor.apply();

                        updateLoveTime();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Cập nhật thời gian yêu đương hiển thị và tên người yêu
    private void updateLoveTime() {
        // Lấy tên mối quan hệ từ EditText
        String relationshipName = relationshipNameEditText.getText().toString().trim();
        if (relationshipName.isEmpty()) {
            relationshipName = "Your Relationship"; // Nếu không có tên, hiển thị mặc định
        }

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Chuyển startDateInMillis về LocalDate
        LocalDate startDate = LocalDate.ofEpochDay(startDateInMillis / (24 * 60 * 60 * 1000));

        // Tính toán sự khác biệt giữa ngày hiện tại và ngày bắt đầu
        Period period = Period.between(startDate, currentDate);

        // Hiển thị tên và thời gian yêu đương
        String timeTogether = String.format("%s\nTime Together: %d years, %d months, %d days",
                relationshipName, period.getYears(), period.getMonths(), period.getDays());

        loveTimeText.setText(timeTogether);
    }
}
