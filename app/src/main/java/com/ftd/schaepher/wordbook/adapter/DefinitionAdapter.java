package com.ftd.schaepher.wordbook.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.ftd.schaepher.wordbook.R;
import com.ftd.schaepher.wordbook.pojo.Word;
import com.ftd.schaepher.wordbook.tools.Loger;

import java.util.List;

/**
 * Created by Schaepher on 2016/7/1.
 */
public class DefinitionAdapter extends ArrayAdapter<Word> {
    Context context;
    private int resourceId;
    private viewHolder viewHolder;
    private int positionItemFocused;
    private boolean editable = false;
    private static final int TYPE_CHINESE = 0;
    private static final int TYPE_CN_WORD = 1;
    private static final int TYPE_EXAMPLE = 2;

    public DefinitionAdapter(Context context, int resource, List<Word> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Word word = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new viewHolder();
            viewHolder.tvEnglish = (TextView) view.findViewById(R.id.tv_english);
            viewHolder.edtChinese = (EditText) view.findViewById(R.id.edt_chinese);
            viewHolder.edtCnWord = (EditText) view.findViewById(R.id.edt_cn_word);
            viewHolder.edtExample = (EditText) view.findViewById(R.id.edt_example);

            viewHolder.edtChinese.setOnFocusChangeListener(new myFocusChangeListener(TYPE_CHINESE));
            viewHolder.edtCnWord.setOnFocusChangeListener(new myFocusChangeListener(TYPE_CN_WORD));
            viewHolder.edtExample.setOnFocusChangeListener(new myFocusChangeListener(TYPE_EXAMPLE));


            viewHolder.edtChinese.addTextChangedListener(new myTextWatcher(TYPE_CHINESE));
            viewHolder.edtCnWord.addTextChangedListener(new myTextWatcher(TYPE_CN_WORD));
            viewHolder.edtExample.addTextChangedListener(new myTextWatcher(TYPE_EXAMPLE));

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (DefinitionAdapter.viewHolder) view.getTag();
        }

        String english = setColor(word.getEnglish());
        viewHolder.tvEnglish.setText(Html.fromHtml(english));
        viewHolder.edtChinese.setText(word.getChinese());
        viewHolder.edtCnWord.setText(word.getCnWord());
        viewHolder.edtExample.setText(word.getExample());

        viewHolder.edtChinese.setId(position);
        viewHolder.edtCnWord.setId(position);
        viewHolder.edtExample.setId(position);

        viewHolder.edtChinese.setEnabled(editable);
        viewHolder.edtCnWord.setEnabled(editable);
        viewHolder.edtExample.setEnabled(editable);

        return view;
    }

    private String setColor(String str) {
        return str.replaceAll("#(.+?)#", "<font color=#33ff00>$1</font>");
    }

    public void setEditTextEditable(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    private class myFocusChangeListener implements View.OnFocusChangeListener {
        private int type;

        public myFocusChangeListener(int type) {
            this.type = type;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                positionItemFocused = v.getId();
            } else {
                positionItemFocused = -1;
            }
            switch (type) {
                case TYPE_CHINESE:
                    viewHolder.edtChineseFocused = hasFocus;
                    Loger.d(String.valueOf(positionItemFocused), String.valueOf(hasFocus));
                    break;
                case TYPE_CN_WORD:
                    viewHolder.edtCnWordFocused = hasFocus;
                    break;
                case TYPE_EXAMPLE:
                    viewHolder.edtExampleFocused = hasFocus;
                    break;
                default:
                    break;
            }
        }
    }


    private class myTextWatcher implements TextWatcher {
        private int type;

        public myTextWatcher(int type) {
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (positionItemFocused == -1) {
                return;
            }

            Word word = getItem(positionItemFocused);
            switch (type) {
                case TYPE_CHINESE:
                    if (viewHolder.edtChineseFocused) {
                        Loger.d("Position:", String.valueOf(word.getId()));
                        Loger.d("setChinese:", s.toString());
                        word.setChinese(s.toString());
                    }
                    break;
                case TYPE_CN_WORD:
                    if (viewHolder.edtCnWordFocused) {
                        word.setCnWord(s.toString());
                    }
                    break;
                case TYPE_EXAMPLE:
                    if (viewHolder.edtExampleFocused) {
                        word.setExample(s.toString());
                    }
                    break;
                default:
                    break;
            }
        }
    }


    class viewHolder {
        TextView tvEnglish;
        EditText edtChinese;
        EditText edtCnWord;
        EditText edtExample;

        boolean edtChineseFocused = false;
        boolean edtCnWordFocused = false;
        boolean edtExampleFocused = false;
    }

}
