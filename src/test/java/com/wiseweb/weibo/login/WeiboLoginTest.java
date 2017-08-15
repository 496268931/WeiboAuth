package com.wiseweb.weibo.login;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sane on 2017/8/15.
 */
public class WeiboLoginTest {
    WeiboLogin test = new WeiboLogin();

    @Test
    public void test() throws Exception {

//        String gsid = test.getGsid("zdr748855@sina.cn", "qqq12345");
//        System.out.println(gsid);
//        if (gsid != null) {
//            String token = test.getAccessTokenByGsid(gsid);
//            System.out.println(token);
//        }

        String gsid2 = test.getGsid("rsa9179068@sina.cn", "Cu1YTCz4DO2h");
        System.out.println(gsid2);
        if (gsid2 != null) {
            String token = test.getAccessTokenByGsid(gsid2);
            System.out.println(token);
        }
    }

    @Test
    public void getGsid() throws Exception {

        test.getGsid("zdr748855@sina.cn", "qqq12345");
    }
    @Test
    public void getAccessTokenByGsid() throws Exception {
    }

}