package com.example.ozner.mvpdemo.Update.HttpHelper;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ozner.mvpdemo.Update.HoYoPreference;
import com.example.ozner.mvpdemo.Update.HttpHelper.bean.NetJsonObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by xinde on 2016/3/3.
 */
public class HoYoRequestData {
    public interface HttpCallback {
        void HandleResult(NetJsonObject result);
    }

    //获取订单列表
//    public static void getOrderList(Context mContext, String usertoken, int pagesize, int pageindex, String action, HttpCallback httpCallback) {
//        new GetOrderListAsyncTask(mContext, httpCallback).execute(usertoken, String.valueOf(pagesize), String.valueOf(pageindex), action);
//    }

    //获取用户信息
    public static void getUserInfo(Context mContext, String usertoken, HttpCallback httpCallback) {
        new GetUserInfoAsyncTask(mContext, httpCallback).execute(usertoken);
    }

    //抢单
    public static RobOrderAsyncTask httpRobOrder(Context mContext, String usertoken, String orderid, HttpCallback httpCallback) {
        RobOrderAsyncTask roborderTask = new RobOrderAsyncTask(mContext, httpCallback);
        roborderTask.execute(usertoken, orderid);
        return roborderTask;
    }

    //刷新首页信息
    public static void refreshIndexData(Context context, String usertoken, HttpCallback callback) {
        new RefreshIndexAsyncTask(context, callback).execute(usertoken);
    }

    //上传极光推送注册ID
    public static void bindJPushNotifyId(Context context, String usertoken, String notifyid, HttpCallback callback) {
        new BindJPushAsyncTask(context, callback).execute(usertoken, notifyid);
    }

    //上传用户信息修改
    public static void updateUserInfo(Context context, List<NameValuePair> httpParms, HttpCallback callback) {
        new UpdateUserInfoAsyncTask(context, httpParms, callback).execute();
    }

    //提交上门时间
    public static void submitTime(Context context, String orderid, String time, String remark, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/SubmitTime";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("orderid", orderid));
        httpParms.add(new BasicNameValuePair("time", time));
        httpParms.add(new BasicNameValuePair("remark", remark));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //获取历史提交的上门时间
    public static void getHomeTimeList(Context context, String orderId, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetHomeTimelist";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("orderid", orderId));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //获取个人的所有评价
    public static void getMyScoreDetails(Context context, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetMyScoreDetails";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //组（合伙人）成员操作
    public static void selectIdentity(Context context, String commandAction, String scope, String groupNum, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/PartnerCommand";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("commandaction", commandAction));
        httpParms.add(new BasicNameValuePair("scope", scope));
        httpParms.add(new BasicNameValuePair("groupnumber", groupNum));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //升级权限
    public static void upgradeAuth(Context context, List<NameValuePair> httpParms, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/UpgradeAuthority";
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //获取当前权限的信息或者审核进度，获取团队成员信息
    public static void getNowAuthOrityDetail(Context context, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetNowAuthorityDetail";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //绑定银行卡
    public static void bindNewBlankCard(Context context, String realname, String cardtype, String cardid, String cardphone, String code, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "Command/BindNewBlankCard";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("realname", realname));
        httpParms.add(new BasicNameValuePair("cardtype", cardtype));
        httpParms.add(new BasicNameValuePair("cardid", cardid));
        httpParms.add(new BasicNameValuePair("cardphone", cardphone));
        httpParms.add(new BasicNameValuePair("code", code));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //获取手机验证码
    public static void getPhoneCode(Context context, String phone, String order, String scope, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "Command/SendPhoneCode";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("mobile", phone));
        httpParms.add(new BasicNameValuePair("order", order));
        httpParms.add(new BasicNameValuePair("scope", scope));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //获取我绑定的银行卡信息
    public static void getOwenBindBlankCard(Context context, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "Command/GetOwenBindBlankCard";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    //忘记密码
    public static void reSetPassword(Context context, String phone, String code, String password, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "FamilyAccount/ResetPassword";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("phone", phone));
        httpParms.add(new BasicNameValuePair("code", code));
        httpParms.add(new BasicNameValuePair("password", password));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *  获取我的账户余额
    */
    public static void getOwenMoney(Context context, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetOwenMoney";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *分页获取我的账户明细
    *@param index
    *        当前页数
    *@param pageSize
    *        每页数据条数
     */
    public static void getOwenMoneyDetail(Context context, String index, String pageSize, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetOwenMoneyDetails";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("index", index));
        httpParms.add(new BasicNameValuePair("pagesize", pageSize));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *工程师提现，提交提现信息
    *
    * @param cardid,绑定银行卡id
    * @param wdMoney,提现金额
    * @param callback,回调函数
     */
    public static void withDrawMoney(Context context, String cardid, String wdMoney, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/WithDraw";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("blankid", cardid));
        httpParms.add(new BasicNameValuePair("WDMoney", wdMoney));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *工程师分页获取个人提现信息
    *
    * @param pageIndex,第几页
    * @param pageSize,每页多少条数据
    * @param callback,回调函数
     */
    public static void getWwinMoneyWithDrawDetails(Context context, String pageIndex, String pageSize, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetowinMoneyWithDrawDetails";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("Pageindex", pageIndex));
        httpParms.add(new BasicNameValuePair("Pagesize", pageSize));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    * 分页获取浩泽产品配件信息
    * searchValue有值时，模糊搜索材料和配件
     */
    public static void getPriceTable(Context context, String pageIndex, String pageSize, String serarchValue, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "Command/GetPriceTable";
        List<NameValuePair> httpParms = new ArrayList<>();

        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("Pageindex", pageIndex));
        httpParms.add(new BasicNameValuePair("Pagesize", pageSize));
        if (serarchValue != null && !serarchValue.isEmpty()) {
            httpParms.add(new BasicNameValuePair("search", serarchValue));
        }
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }


    /*
    *删除团队相关的一切（用于解散团队/团队拒绝后重新申请前的操作）
     */
    public static void removeGroup(Context context, String groupNum, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/RemoveGroup";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("GroupNumber", groupNum));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *审核团队成员
     */
    public static void aduitGroupMember(Context context, String groupNum, String userid, String operRes, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/AuditGroupMember";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("GroupNumber", groupNum));
        httpParms.add(new BasicNameValuePair("userid", userid));
        httpParms.add(new BasicNameValuePair("result", operRes));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *移除（踢出）团队成员/用户退出当前团队
     */
    public static void removeGroupMember(Context context, String GroupNumber, String userid, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/RemoveGroupMember";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("GroupNumber", GroupNumber));
        httpParms.add(new BasicNameValuePair("userid", userid));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *获取我的团队成员
     */
    public static void getMyGroupMembers(Context context, String pagesize, String index, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetMyGroupMembers";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("pagesize", pagesize));
        httpParms.add(new BasicNameValuePair("index", index));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *获取当前的实名认证信息
     */
    public static void getCurrentrealNameInfo(Context context, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetCurrentRealnameInfo";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *校验服务直通车id
    * @serviceid 服务直通车ID Guid
     */
    public static void checkServiceTrain(Context context, String serviceid, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/CheckServiceTrain";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("serviceid", serviceid));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *更新服务直通车绑定信息
    * @serviceid 服务直通车ID Guid
    * @machinKind 机器种类
    * @MachineBrand 机器品牌
    * @UserPhone 联系人手机号
     */
    public static void updateServiceTrainMachine(Context context, String serviceid, String machinKind, String machineBrand, String userPhone, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/UpdateServiceTrainMachine";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("serviceid", serviceid));
        httpParms.add(new BasicNameValuePair("MachineKind", machinKind));
        httpParms.add(new BasicNameValuePair("MachineBrand", machineBrand));
        httpParms.add(new BasicNameValuePair("UserPhone", userPhone));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *获取绩效排行
    * @model 传0：团队排名/传1：全国排名 int
    * @pageindex 页码，默认1
    * @pagesize 页面数据，默认10
     */
    public static void getPreRandDetails(Context context, String model, String pageindex, String pagesize, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetPreRankDetails";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("model", model));
        httpParms.add(new BasicNameValuePair("pageindex", pageindex));
        httpParms.add(new BasicNameValuePair("pagesize", pagesize));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *获取订单详细信息
    * @param orderId 订单ID
     */
    public static void getOrderDetails(Context context, String orderId, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetOrderDetails";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("orderid", orderId));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }

    /*
    *通过服务直通车查看服务记录
     */
    public static void getServiceRecordByServiceTrain(Context context, String servicecode, int pageindex, int pagesize, HttpCallback callback) {
        String url = HoYoPreference.ServerAddress(context) + "AppInterface/GetServiceRecordByServiceTrain";
        List<NameValuePair> httpParms = new ArrayList<>();
        httpParms.add(new BasicNameValuePair("usertoken", HoYoPreference.getUserToken(context)));
        httpParms.add(new BasicNameValuePair("servicecode", servicecode));
        httpParms.add(new BasicNameValuePair("pageindex", String.valueOf(pageindex)));
        httpParms.add(new BasicNameValuePair("pagesize", String.valueOf(pagesize)));
        new NormalAsyncTask(context, url, httpParms, callback).execute();
    }


    //获取订单列表异步任务
//    private static class GetOrderListAsyncTask extends AsyncTask<String, Integer, NetJsonObject> {
//        private Context mContext;
//        private HttpCallback callback;
//
//        public GetOrderListAsyncTask(Context context, HttpCallback callback) {
//            this.mContext = context;
//            this.callback = callback;
//        }
//
//        @Override
//        protected NetJsonObject doInBackground(String... params) {
//            String usertoken = params[0];
//            String pagesize = params[1];
//            String pageindex = params[2];
//            String action = params[3];
//
//            String orderListUrl = HoYoPreference.ServerAddress(mContext) + "AppInterface/GetOrderList";
//            List<NameValuePair> httpParms = new LinkedList<>();
//            httpParms.add(new BasicNameValuePair("usertoken", usertoken));
//            httpParms.add(new BasicNameValuePair("pagesize", pagesize));
//            httpParms.add(new BasicNameValuePair("pageindex", pageindex));
//            httpParms.add(new BasicNameValuePair("action", action));
//            if (action.equals(OrderAciton.KqAction)) {
//                try {
//                    MyLocationInfo locationInfo = JSON.parseObject(UserDataPreference.GetUserData(mContext, UserDataPreference.MyLocationInfo, ""), MyLocationInfo.class);
//                    String orderby = UserDataPreference.GetUserData(mContext, UserDataPreference.OrderListShort, HomeOrderBy.Time);
//                    httpParms.add(new BasicNameValuePair("orderby", orderby));
//                    httpParms.add(new BasicNameValuePair("Province", locationInfo.getProvince()));
//                    httpParms.add(new BasicNameValuePair("lat", String.valueOf(locationInfo.getBdLat())));
//                    httpParms.add(new BasicNameValuePair("lng", String.valueOf(locationInfo.getBdLng())));
//                    httpParms.add(new BasicNameValuePair("City", locationInfo.getCity()));
//                    httpParms.add(new BasicNameValuePair("Country", locationInfo.getCountry()));
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//            StringBuffer sb = new StringBuffer();
//            sb.append(orderListUrl + "?");
//            for (NameValuePair valuePair : httpParms) {
//                sb.append(valuePair.getName() + "=" + valuePair.getValue() + "&");
//            }
//            if (LogUtilsLC.APP_DBG) {
//                LogUtilsLC.E("tag", "GetOrderListAsyncTask:" + sb.toString());
//            }
//            return HoYoDataHttp.HoYoWebServer(mContext, orderListUrl, httpParms);
//        }
//
//        @Override
//        protected void onPostExecute(NetJsonObject netJsonObject) {
//            if (callback != null) {
//                callback.HandleResult(netJsonObject);
//            }
//            super.onPostExecute(netJsonObject);
//        }
//    }

    //获取用户信息异步任务
    private static class GetUserInfoAsyncTask extends AsyncTask<String, Integer, NetJsonObject> {
        private Context mContext;
        private HttpCallback callback;

        public GetUserInfoAsyncTask(Context context, HttpCallback callback) {
            this.mContext = context;
            this.callback = callback;
        }

        @Override
        protected NetJsonObject doInBackground(String... params) {
            String usertoken = params[0];
            String userInfoUrl = HoYoPreference.ServerAddress(mContext) + "AppInterface/GetCurrentUserInfo";
            List<NameValuePair> httpParms = new LinkedList<>();
            httpParms.add(new BasicNameValuePair("usertoken", usertoken));
            NetJsonObject result = HoYoDataHttp.HoYoWebServer(mContext, userInfoUrl, httpParms);
            return result;
        }

        @Override
        protected void onPostExecute(NetJsonObject netJsonObject) {
            if (callback != null) {
                callback.HandleResult(netJsonObject);
            }
            super.onPostExecute(netJsonObject);
        }
    }

    //抢单异步任务
    public static class RobOrderAsyncTask extends AsyncTask<String, Integer, NetJsonObject> {
        private Context mContext;
        private HttpCallback httpCallback;

        public RobOrderAsyncTask(Context context, HttpCallback httpCallback) {
            this.mContext = context;
            this.httpCallback = httpCallback;
        }

        @Override
        protected NetJsonObject doInBackground(String... params) {
            String usertoken = params[0];
            String orderid = params[1];
            String robOrderUrl = HoYoPreference.ServerAddress(mContext) + "AppInterface/RobOrder";
            List<NameValuePair> httpParms = new LinkedList<>();
            httpParms.add(new BasicNameValuePair("usertoken", usertoken));
            httpParms.add(new BasicNameValuePair("orderid", orderid));
            return HoYoDataHttp.HoYoWebServer(mContext, robOrderUrl, httpParms);
        }

        @Override
        protected void onPostExecute(NetJsonObject netJsonObject) {
            httpCallback.HandleResult(netJsonObject);
        }
    }

    //刷新首页信息
    private static class RefreshIndexAsyncTask extends AsyncTask<String, Integer, NetJsonObject> {
        private Context mContext;
        private HttpCallback httpCallback;

        public RefreshIndexAsyncTask(Context context, HttpCallback httpCallback) {
            this.mContext = context;
            this.httpCallback = httpCallback;
        }

        @Override
        protected NetJsonObject doInBackground(String... params) {
            String usertoken = params[0];
            String refreshUrl = HoYoPreference.ServerAddress(mContext) + "AppInterface/RefreshIndex";
            List<NameValuePair> httpParms = new LinkedList<>();
            httpParms.add(new BasicNameValuePair("usertoken", usertoken));
            return HoYoDataHttp.HoYoWebServer(mContext, refreshUrl, httpParms);
        }

        @Override
        protected void onPostExecute(NetJsonObject netJsonObject) {
            if (httpCallback != null) {
                httpCallback.HandleResult(netJsonObject);
            }
        }
    }

    //绑定极光推送注册ID
    private static class BindJPushAsyncTask extends AsyncTask<String, Integer, NetJsonObject> {
        private Context mContext;
        private HttpCallback httpCallback;

        public BindJPushAsyncTask(Context context, HttpCallback callback) {
            this.mContext = context;
            this.httpCallback = callback;
        }

        @Override
        protected NetJsonObject doInBackground(String... params) {
            String usertoken = params[0];
            String notifyid = params[1];
            String httpUrl = HoYoPreference.ServerAddress(mContext) + "Command/BingJgNotifyId";
            List<NameValuePair> httpPars = new LinkedList<>();
            httpPars.add(new BasicNameValuePair("usertoken", usertoken));
            httpPars.add(new BasicNameValuePair("notifyid", notifyid));
            return HoYoDataHttp.HoYoWebServer(mContext, httpUrl, httpPars);
        }

        @Override
        protected void onPostExecute(NetJsonObject netJsonObject) {
            if (httpCallback != null) {
                httpCallback.HandleResult(netJsonObject);
            }
        }
    }

    //上传用户修改的信息
    private static class UpdateUserInfoAsyncTask extends AsyncTask<String, Integer, NetJsonObject> {
        private Context mContext;
        private List<NameValuePair> httpParms;
        private HttpCallback httpCallback;

        public UpdateUserInfoAsyncTask(Context context, List<NameValuePair> httpParms, HttpCallback callback) {
            this.mContext = context;
            this.httpParms = httpParms;
            this.httpCallback = callback;
        }

        @Override
        protected NetJsonObject doInBackground(String... params) {
            String httpUrl = HoYoPreference.ServerAddress(mContext) + "FamilyAccount/UpdateUserInfo";
            return HoYoDataHttp.HoYoWebServer(mContext, httpUrl, httpParms);
        }

        @Override
        protected void onPostExecute(NetJsonObject netJsonObject) {
            if (httpCallback != null) {
                httpCallback.HandleResult(netJsonObject);
            }
        }
    }

    //通用异步请求任务
    private static class NormalAsyncTask extends AsyncTask<String, Integer, NetJsonObject> {
        private Context mContext;
        private HttpCallback httpCallback;
        private String httpUrl;
        private List<NameValuePair> httpParms;

        public NormalAsyncTask(Context context, String httpUrl, List<NameValuePair> httpParms, HttpCallback callback) {
            this.mContext = context;
            this.httpCallback = callback;
            this.httpUrl = httpUrl;
            this.httpParms = httpParms;
        }

        @Override
        protected NetJsonObject doInBackground(String... params) {
            return HoYoDataHttp.HoYoWebServer(mContext, httpUrl, httpParms);
        }

        @Override
        protected void onPostExecute(NetJsonObject netJsonObject) {
            if (httpCallback != null) {
                httpCallback.HandleResult(netJsonObject);
            }
        }
    }
}