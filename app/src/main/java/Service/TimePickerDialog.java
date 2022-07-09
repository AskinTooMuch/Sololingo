package Service;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sololingo.R;

import java.sql.Time;

public class TimePickerDialog extends Dialog{
    public TimePickerDialog(Context context, TimePickerDialog.TimePickListener listener) {
        super(context);
        this.context = context;
        this.timePickListener = listener;
    }

    private Context context;
    private TimePicker timePicker;
    private Button btnApply, btnCancel, btnDisableNotification;

    private void bindingView(){
        timePicker = findViewById(R.id.timePicker);
        btnApply = findViewById(R.id.btnApply);
        btnCancel = findViewById(R.id.btnCancel);
        btnDisableNotification = findViewById(R.id.btnDisableNotification);
    }

    private void bindingAction(){
        btnApply.setOnClickListener(this::applyData);
        btnCancel.setOnClickListener(this::cancelAction);
        btnDisableNotification.setOnClickListener(this::disableNotification);
    }

    private void disableNotification(View view) {
        SharedPreferences pref = getContext().getSharedPreferences("notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("notiMode", false);
        editor.apply();
        if (timePickListener!=null){
            timePickListener.onTimePicked(false);
        }
        this.dismiss();
    }

    private void cancelAction(View view) {
        this.dismiss();
    }

    private void applyData(View view) {
        SharedPreferences pref = getContext().getSharedPreferences("notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("notiMode", true);
        editor.putInt("hour",timePicker.getHour());
        editor.putInt("minute",timePicker.getMinute());
        editor.apply();
        if (timePickListener!=null){
            timePickListener.onTimePicked(true);
        }
        this.dismiss();
    }

    private TimePickListener timePickListener;
    public interface TimePickListener {
        void onTimePicked(boolean timePicked);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.time_picker_dialog);

        bindingView();
        bindingAction();
    }

    public TimePickerDialog(@NonNull Context context) {
        super(context);
    }
}
