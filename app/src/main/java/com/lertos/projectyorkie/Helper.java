package com.lertos.projectyorkie;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.adapters.BindDataToView;

import java.util.List;

public class Helper {

    //A wrapper function to create a spannable text object. For convenience
    public static SpannableStringBuilder createSpannable(String str, String appended, int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(str + appended);
        spannable.setSpan(new ForegroundColorSpan(color), str.length(), str.length() + appended.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return spannable;
    }

    //A wrapper function to create a RecyclerView and set the adapter. For convenience
    public static <T extends BindDataToView> void createNewRecyclerView(RecyclerView recyclerView, List<?> arrayList, T viewAdapter, Context context) {
        viewAdapter.setDataList(arrayList);
        recyclerView.setAdapter((RecyclerView.Adapter) viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

}
