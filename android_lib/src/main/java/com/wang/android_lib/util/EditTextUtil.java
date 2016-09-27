package com.wang.android_lib.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.wang.java_util.TextUtil;

/**
 * by 王荣俊 on 2016/6/10.
 */
public class EditTextUtil {

    public static void setClearButton(final EditText editText, View btnClear) {
        btnClear.setVisibility(View.GONE);
        editText.addTextChangedListener(new MyTextWatcher(editText, btnClear));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    static class MyTextWatcher implements TextWatcher {

        private EditText et;
        private View btn;

        public MyTextWatcher(EditText et, View btn) {
            this.et = et;
            this.btn = btn;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtil.isEmpty(et.getText().toString())) {
                btn.setVisibility(View.GONE);
            } else {
                btn.setVisibility(View.VISIBLE);
            }
        }
    }
}
