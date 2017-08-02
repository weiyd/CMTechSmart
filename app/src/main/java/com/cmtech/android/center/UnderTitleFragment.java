package com.cmtech.android.center;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gdmc on 2017/7/10.
 */

public class UnderTitleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_undertitle, container, false);

        RecyclerView titleView = (RecyclerView) view.findViewById(R.id.undertitle_view);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), MainActivity.itemList.size());
        titleView.setLayoutManager(manager);
        UnderTitleAdapter adapter = new UnderTitleAdapter(MainActivity.itemList, MainActivity.INITPOS, getActivity());
        titleView.setAdapter(adapter);
        return view;
    }
}
