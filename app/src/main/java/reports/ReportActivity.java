package reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.jakegrace.exercyse.HomeActivity;
import com.example.jakegrace.exercyse.R;

/**
 * "Home screen" for the reporting feature.
 */
public class ReportActivity extends AppCompatActivity {
    /**
     * Button for switching to NewReportActivity.
     */
    private Button btnNewReport;
    /**
     * Button for switching to HomeActivity.
     */
    private Button btnBack;
    /**
     * Button for switching to ViewReportActivity.
     */
    private Button btnViewReport;

    /**
     * onCreate for ReportActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        btnBack = (Button)findViewById(R.id.btn_Back);
        btnNewReport = (Button)findViewById(R.id.btn_mkReport);
        btnViewReport = (Button)findViewById(R.id.btn_getReport);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(ReportActivity.this, HomeActivity.class);
                ReportActivity.this.startActivity(toHome);
            }
        });

        btnNewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toNewReport = new Intent(ReportActivity.this, NewReportActivity.class);
                ReportActivity.this.startActivity(toNewReport);
            }
        });

        btnViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toViewReports = new Intent(ReportActivity.this, ViewReportActivity.class);
                ReportActivity.this.startActivity(toViewReports);
            }
        });

    }
}
