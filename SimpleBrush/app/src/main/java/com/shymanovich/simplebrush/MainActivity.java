package com.shymanovich.simplebrush;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ramotion.fluidslider.FluidSlider;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int GALLERY_REQUEST = 1002;
    private BottomSheetDialog shapeDialog;
    private BottomSheetDialog brushSizeDialog;
    private ColorPickerDialog colorPickerDialog;
    private CanvasView canvas;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.canvas = findViewById(R.id.canvas);
        this.setUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setUp() {
        setUpMenu();
        requestPermissions();
    }

    public void setUpMenu() {
        setSupportActionBar(findViewById(R.id.bottomAppBar));
        setUpShapeDialog();
        setupBrushSizeDialog();
        setUpColorPickerDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawShape:
                shapeDialog.show();
                return true;
            case R.id.brushSizePicker:
                brushSizeDialog.show();
                return true;
            case R.id.colorPicker:
                colorPickerDialog.show();
                return true;
            case R.id.eraser:
                canvas.setDrawPaintColor(Color.WHITE);
                return true;
            case R.id.uploadImage:
                uploadImage();
                return true;
            case R.id.save:
                saveImage();
                return true;
        }
        return false;
    }

    public void setUpShapeDialog() {
        this.shapeDialog = new BottomSheetDialog(this);
        this.shapeDialog.setContentView(getLayoutInflater().inflate(R.layout.shape_select, null));

        TextView squareShape = this.shapeDialog.findViewById(R.id.squareShape);
        squareShape.setOnClickListener(v -> {
            canvas.setDrawShape(CanvasView.SQUARE);
            shapeDialog.dismiss();
        });

        TextView circleShape = this.shapeDialog.findViewById(R.id.circleShape);
        circleShape.setOnClickListener(v -> {
            canvas.setDrawShape(CanvasView.CIRCLE);
            shapeDialog.dismiss();
        });

        TextView lineShape = this.shapeDialog.findViewById(R.id.lineShape);
        lineShape.setOnClickListener(v -> {
            canvas.setDrawShape(CanvasView.LINE);
            shapeDialog.dismiss();
        });

        TextView curvedLineShape = this.shapeDialog.findViewById(R.id.curvedLineShape);
        curvedLineShape.setOnClickListener(v -> {
            canvas.setDrawShape(CanvasView.PEN);
            shapeDialog.dismiss();
        });

        TextView rectangleShape = this.shapeDialog.findViewById(R.id.rectangleShape);
        rectangleShape.setOnClickListener(v -> {
            canvas.setDrawShape(CanvasView.RECTANGLE);
            shapeDialog.dismiss();
        });
    }

    private void uploadImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    private void saveImage() {
        String fileName = System.currentTimeMillis() + ".png";
        String savePath = getExternalFilesDir("Saved").getAbsolutePath();
        String pathToShow = "/Android/data/SimpleBrush/files/Saved";
        File file = new File(savePath, fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            Bitmap bitmap = canvas.getCanvasBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Snackbar.make(canvas, "Saved to \"" + pathToShow + "\"", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        } catch (IOException e) {
            Log.d("TAG", "Exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void setupBrushSizeDialog() {
        brushSizeDialog = new BottomSheetDialog(this);
        brushSizeDialog.setTitle("Brush width");
        brushSizeDialog.setContentView(getLayoutInflater().inflate(R.layout.brush_size_dialog, null));
        brushSizeDialog.setCanceledOnTouchOutside(false);
        brushSizeDialog.setCancelable(false);

        FluidSlider fluidSlider = brushSizeDialog.findViewById(R.id.fluid_slider);

        Button cancelButton = brushSizeDialog.findViewById(R.id.brush_size_cancel_button);
        cancelButton.setOnClickListener(v -> {
            brushSizeDialog.dismiss();
        });
        Button confirmButton = brushSizeDialog.findViewById(R.id.brush_size_confirm_button);
        confirmButton.setOnClickListener(v -> {
            canvas.setStrokeWidth(fluidSlider.getPosition() * 100);
            brushSizeDialog.dismiss();
        });
    }

    private void setUpColorPickerDialog() {
        colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this);
        colorPickerDialog.hideColorComponentsInfo();
        colorPickerDialog.hideHexaDecimalValue();
        colorPickerDialog.setOnColorPickedListener((color, hexVal) -> {
            this.canvas.setDrawPaintColor(color);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "Permissions received");
            } else {
                Snackbar.make(canvas, "Error", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    //Вызвать окно редактирования картинки
                    CropImage.activity(selectedImage)
                            .setAspectRatio(canvas.getWidthView(), canvas.getHeightView())
                            .setFixAspectRatio(true)
                            .start(this);
                } else {
                    //Показать сообщение об ошибке
                    Snackbar.make(canvas, "Upload error", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                try {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    canvas.setBitmap(scaleBitmap(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmap) {
        int srcW = bitmap.getWidth();
        int srcH = bitmap.getHeight();
        int viewH = canvas.getHeightView();
        int viewW = canvas.getWidthView();
        int dstW = srcW;
        int dstH = srcH;

        if (dstW > viewW || dstH > viewH) {
            if (srcH == srcW) {
                dstW = (srcH * viewH) / srcH;
                dstH = dstW;
                bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstW, true);

            } else {
                if (dstH > viewH) {
                    dstH = viewH;
                    dstW = (srcW * dstH) / srcH;
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
                }
                if (dstW > viewW) {
                    dstW = viewW;
                    dstH = (dstW * srcH) / srcW;
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
                }
            }
        }

        if (dstW < viewW || dstH < viewH) {
            if (srcH == srcW) {
                dstW = (srcH * viewH) / srcH;
                bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstW, true);

            } else {
                if (dstH < viewH) {
                    dstH = viewH;
                    dstW = (srcW * dstH) / srcH;
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
                }
                if (dstW < viewW) {
                    dstW = viewW;
                    dstH = (dstW * srcH) / srcW;
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstW, dstH, true);
                }
            }
        }

        return bitmap;
    }
}