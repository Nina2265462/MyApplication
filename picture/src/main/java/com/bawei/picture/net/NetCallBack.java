package com.bawei.picture.net;

import com.bawei.picture.entity.HeadEntity;

/*
 *@Auther:cln
 *@Date: 2019/7/6
 *@Description:功能
 * */
public interface NetCallBack {
    void onSuccess(HeadEntity headEntity);
    void onFailurl(String msg);
}
