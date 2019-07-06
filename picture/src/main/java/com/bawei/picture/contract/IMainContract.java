package com.bawei.picture.contract;

import com.bawei.picture.entity.HeadEntity;
import com.bawei.picture.net.NetCallBack;

import java.util.HashMap;

/*
 *@Auther:cln
 *@Date: 2019/7/6
 *@Description:功能
 * */
public interface IMainContract {

    public interface IMainModel {
        void request(HashMap<String, String> params, NetCallBack netCallBack);
    }

    public interface IMainView {
        void getPreData(HeadEntity headEntity);
    }

    public interface IMainPresenter {
        void showData(HashMap<String, String> params);
    }
}
