package com.example.myapplication.utils;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.ScaleGestureDetector;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

public class ZoomImageView extends AppCompatImageView {
    private Matrix matrix = new Matrix();
    private float scale = 1f; // Mặc định 1x khi mở giao diện
    private final float minScale = 1f;
    private final float maxScale = 5f;
    private ScaleGestureDetector scaleDetector;

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);

        // Khởi tạo ScaleGestureDetector để xử lý pinch zoom
        scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                scale *= scaleFactor;

                // Giới hạn scale từ minScale đến maxScale
                scale = Math.max(minScale, Math.min(scale, maxScale));

                // Cập nhật ma trận để zoom ảnh
                matrix.setScale(scale, scale, getWidth() / 2f, getHeight() / 2f);
                setImageMatrix(matrix);
                return true;
            }
        });

        // Đặt ảnh về trạng thái 1x khi mới mở
        resetZoom();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        return true;
    }

    // Hàm đặt lại ảnh về kích thước ban đầu (1x)
    private void resetZoom() {
        scale = 1f;
        matrix.reset();
        setImageMatrix(matrix);
    }
}
