//package com.example.stock.chart.notimportant;
//
//import android.Manifest;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//
//import com.github.mikephil.charting.charts.Chart;
//import com.google.android.material.snackbar.Snackbar;
//
//public abstract class DemoBase extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback{
//    protected final String[] months = new String[] {
//            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
//    };
//
//    protected final String[] parties = new String[] {
//            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
//            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
//            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
//            "Party Y", "Party Z"
//    };
//
//    private static final int PERMISSION_STORAGE = 0;
//
//    protected Typeface tfRegular;
//    protected Typeface tfLight;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    protected float getRandom(float range, float start) {
//        return (float) (Math.random() * range) + start;
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        // Votre implémentation ici
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
//    }
//
//    protected void requestStoragePermission(View view) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            Snackbar.make(view, "Write permission is required to save image to gallery", Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
//                        }
//                    }).show();
//        } else {
//            Toast.makeText(requireActivity(), "Permission Required!", Toast.LENGTH_SHORT)
//                    .show();
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
//        }
//    }
//
//    protected void saveToGallery(Chart chart, String name) {
//        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70))
//            Toast.makeText(requireActivity(), "Saving SUCCESSFUL!",
//                    Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(requireActivity(), "Saving FAILED!", Toast.LENGTH_SHORT)
//                    .show();
//    }
//
//    protected abstract void saveToGallery();
//}
