package com.mhr.mobile.widget.input;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.google.android.material.button.MaterialButton;
import com.mhr.mobile.R;

import android.widget.EditText;

public class KeyboardNumber extends LinearLayout implements View.OnClickListener {

  private InputConnection inputConnection;
  private SparseArray<String> keyValue = new SparseArray<>();
  private MaterialButton button1,
      button2,
      button3,
      button4,
      button5,
      button6,
      button7,
      button8,
      button9,
      buttonA,
      buttonB,
      buttonC;
  private EditText targetEditText; // Tambahkan EditText sebagai target
  private Handler handler = new Handler();
  private Runnable deleteRunnable;

  public KeyboardNumber(Context context) {
    super(context);
    initialization(context);
  }

  public KeyboardNumber(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialization(context);
  }

  public KeyboardNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialization(context);
  }

  private void initialization(Context context) {
    LayoutInflater.from(context).inflate(R.layout.keyboard_number, this, true);

    button1 = findViewById(R.id.b1);
    button2 = findViewById(R.id.b2);
    button3 = findViewById(R.id.b3);
    button4 = findViewById(R.id.b4);
    button5 = findViewById(R.id.b5);
    button6 = findViewById(R.id.b6);
    button7 = findViewById(R.id.b7);
    button8 = findViewById(R.id.b8);
    button9 = findViewById(R.id.b9);
    buttonA = findViewById(R.id.bA);
    buttonB = findViewById(R.id.bB);
    buttonC = findViewById(R.id.delete);

    button1.setOnClickListener(this);
    button2.setOnClickListener(this);
    button3.setOnClickListener(this);
    button4.setOnClickListener(this);
    button5.setOnClickListener(this);
    button6.setOnClickListener(this);
    button7.setOnClickListener(this);
    button8.setOnClickListener(this);
    button9.setOnClickListener(this);
    buttonA.setOnClickListener(this);
    buttonB.setOnClickListener(this);
    buttonC.setOnClickListener(this);

    keyValue.put(R.id.b1, "1");
    keyValue.put(R.id.b2, "2");
    keyValue.put(R.id.b3, "3");
    keyValue.put(R.id.b4, "4");
    keyValue.put(R.id.b5, "5");
    keyValue.put(R.id.b6, "6");
    keyValue.put(R.id.b7, "7");
    keyValue.put(R.id.b8, "8");
    keyValue.put(R.id.b9, "9");
    keyValue.put(R.id.bB, "0");

    setupDeleteButton();
  }

  @Override
  public void onClick(View view) {
    if (inputConnection == null) {
      return;
    }

    // Fokuskan EditText
    if (targetEditText != null) {
      targetEditText.requestFocus();
    }

    if (view.getId() == R.id.delete) {
      deleteCharacter();
    } else {
      String stringValue = keyValue.get(view.getId());
      inputConnection.commitText(stringValue, 1);
    }
  }

  private void deleteCharacter() {
    if (inputConnection == null) {
      return;
    }

    CharSequence selectedText = inputConnection.getSelectedText(0);
    if (TextUtils.isEmpty(selectedText)) {
      inputConnection.deleteSurroundingText(1, 0);
    } else {
      inputConnection.commitText("", 1);
    }
  }

  private void setupDeleteButton() {
    buttonC.setOnTouchListener(
        (v, event) -> {
          switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
              startDeleting();
              return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
              stopDeleting();
              return true;
          }
          return false;
        });
  }

  private void startDeleting() {
    if (deleteRunnable == null) {
      deleteRunnable =
          new Runnable() {
            @Override
            public void run() {
              deleteCharacter();
              handler.postDelayed(this, 100);
            }
          };
    }
    handler.post(deleteRunnable);
  }

  private void stopDeleting() {
    if (deleteRunnable != null) {
      handler.removeCallbacks(deleteRunnable);
    }
  }

  public void setInputConnection(InputConnection inputConnectionLocal) {
    this.inputConnection = inputConnectionLocal;
  }

  // Tambahkan metode untuk mengatur target EditText
  public void setTargetEditText(EditText editText) {
    this.targetEditText = editText;
  }
  
  public void setTextSize(int size){
	  button1.setTextSize(size);
	  button2.setTextSize(size);
	  button3.setTextSize(size);
	  button4.setTextSize(size);
	  button5.setTextSize(size);
	  button6.setTextSize(size);
	  button7.setTextSize(size);
	  button8.setTextSize(size);
	  button9.setTextSize(size);
	  buttonB.setTextSize(size);
  }
}