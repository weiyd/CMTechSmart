package com.cmtech.android.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmtech.android.center.R;
import com.cmtech.android.common.LogUtil;
import com.cmtech.android.common.MyApplication;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gdmc on 2017/7/10.
 */

public class AccountFragment extends Fragment {
    private static final int START_LOGIN = 1;

    private TextView tvAccountName;
    private RecyclerView rvAccountInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_account, container, false);

        tvAccountName = (TextView) view.findViewById(R.id.tv_accountname);
        rvAccountInfo = (RecyclerView) view.findViewById(R.id.rv_accountinfo);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        AccountInfo account = ((MyApplication)activity.getApplicationContext()).getAccountInfo();
        if(account != null) {
            tvAccountName.setText(account.getName());
            LogUtil.i("Account", "not null");
        }
        else {
            LogUtil.i("Account", "null");
        }

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        rvAccountInfo.setLayoutManager(manager);
        AccountInfoAdapter adapter = new AccountInfoAdapter(this);
        rvAccountInfo.setAdapter(adapter);

        LinearLayout llAccountInfo = (LinearLayout) activity.findViewById(R.id.ll_accountinfo);
        llAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivityForResult(intent, START_LOGIN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case START_LOGIN:
                if(resultCode == RESULT_OK) {
                    AccountInfo account = ((MyApplication)getActivity().getApplicationContext()).getAccountInfo();
                    if(account != null) {
                        tvAccountName.setText(account.getName());
                    }
                }
                break;
            default:
                break;
        }
    }
}
