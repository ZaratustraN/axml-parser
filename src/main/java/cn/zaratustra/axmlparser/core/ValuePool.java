package cn.zaratustra.axmlparser.core;

import cn.zaratustra.axmlparser.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by zaratustra on 2017/12/12.
 */
public class ValuePool {
    private ArrayList<String> mStringPools;
    private ArrayList<Integer> mResourceIds;
    private HashMap<String, String> mUriToNameSpaceMaps;
    private HashMap<String, String> mNamespaceToUriMaps;

    private Stack<Integer> mIntegers;
    private boolean isUTF8 = false;

    public boolean isUTF8() {
        return isUTF8;
    }

    public void setUTF8(boolean UTF8) {
        isUTF8 = UTF8;
    }

//    public String[] mStringCache = {"theme",
//            "label",
//            "icon",
//            "name",
//            "value",
//            "minSdkVersion",
//            "versionCode",
//            "versionName",
//            "targetSdkVersion",
//            "allowBackup",
//            "supportsRtl",
//            "roundIcon",
//            "1.0",
//            "26.1.0",
//            "27.0.0-SNAPSHOT",
//            "action",
//            "activity",
//            "android",
//            "android.arch.lifecycle.VERSION",
//            "android.intent.action.MAIN",
//            "android.intent.category.LAUNCHER",
//            "android.permission.INTERNET",
//            "android.permission.READ_EXTERNAL_STORAGE",
//            "android.permission.WRITE_EXTERNAL_STORAGE",
//            "android.support.VERSION",
//            "application",
//            "category",
//            "cn.zarathustra.net",
//            "cn.zarathustra.net.MainActivity",
//            "http://schemas.android.com/apk/res/android",
//            "intent-filter",
//            "manifest",
//            "meta-data",
//            "package",
//            "uses-permission",
//            "uses-sdk"
//    };
    public String[] mStringCache = {"versionCode",
        "versionName",
        "minSdkVersion",
        "targetSdkVersion",
        "name",
        "allowBackup",
        "icon",
        "label",
        "largeHeap",
        "supportsRtl",
        "theme",
        "screenOrientation",
        "windowSoftInputMode",
        "configChanges",
        "launchMode",
        "exported",
        "hardwareAccelerated",
        "scheme",
        "enabled",
        "permission",
        "resource",
        "value",
        "process",
        "noHistory",
        "priority",
        "persistent",
        "authorities",
        "android",
        "http://schemas.android.com/apk/res/android",
        "",
        "package",
        "platformBuildVersionCode",
        "platformBuildVersionName",
        "manifest",
        "com.flamingo.gpgame",
        "2.4.1",
        "25",
        "7.1.1",
        "uses-sdk",
        "uses-permission",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.MOUNT_UNMOUNT_FILESYSTEMS",
        "android.permission.ACCESS_NETWORK_STATE",
        "android.permission.READ_PHONE_STATE",
        "android.permission.ACCESS_WIFI_STATE",
        "android.permission.BLUETOOTH",
        "android.permission.INTERNET",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.RECEIVE_SMS",
        "android.permission.READ_SMS",
        "android.permission.READ_LOGS",
        "android.permission.RECEIVE_BOOT_COMPLETED",
        "android.permission.RESTART_PACKAGES",
        "android.permission.BROADCAST_STICKY",
        "android.permission.WRITE_SETTINGS",
        "android.permission.RECEIVE_USER_PRESENT",
        "android.permission.WAKE_LOCK",
        "android.permission.KILL_BACKGROUND_PROCESSES",
        "android.permission.GET_TASKS",
        "android.permission.VIBRATE",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.CAMERA",
        "android.webkit.permission.PLUGIN",
        "android.permission.CHANGE_NETWORK_STATE",
        "android.hardware.camera.autofocus",
        "uses-feature",
        "android.hardware.camera",
        "android.permission.CHANGE_WIFI_STATE",
        "android.permission.RECORD_AUDIO",
        "com.google.android.gms.permission.ACTIVITY_RECOGNITION",
        "android.permission.READ_CALENDAR",
        "android.permission.WRITE_CALENDAR",
        "com.android.launcher.permission.INSTALL_SHORTCUT",
        "android.permission.BATTERY_STATS",
        "application",
        "com.flamingo.gpgame.config.GPGameApplication",
        "activity",
        "com.flamingo.gpgame.view.activity.SplashActivity",
        "intent-filter",
        "action",
        "android.intent.action.MAIN",
        "category",
        "android.intent.category.LAUNCHER",
        "com.flamingo.gpgame.view.activity.GPMainActivity",
        "com.flamingo.gpgame.view.activity.GPDialogActivity",
        "receiver",
        "com.flamingo.gpgame.service.GPDownloadNotifyManager$NotificationClickReceiver",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GPDialogReportActivity",
        "com.flamingo.gpgame.view.dialog.DownloadErrorSpaceDialog",
        "com.flamingo.gpgame.view.dialog.NoInstallTaskActivity",
        "com.flamingo.gpgame.view.dialog.InstallErrorSpaceDialog",
        "com.flamingo.gpgame.view.activity.GPUpdateDialogActivity",
        "com.flamingo.gpgame.view.activity.GPTestActivity",
        "com.flamingo.gpgame.view.activity.GPTestDownloadActivity",
        "com.flamingo.gpgame.view.activity.HotOrNewCrackGameListActivity",
        "com.flamingo.gpgame.view.activity.ModuleGameListActivity",
        "com.flamingo.gpgame.view.activity.TagGameListActivity",
        "com.flamingo.gpgame.view.activity.AGameGiftListActivity",
        "com.flamingo.gpgame.view.activity.NotificationDetailActivity",
        "com.flamingo.gpgame.view.activity.GiftDetailActivity",
        "com.flamingo.gpgame.view.activity.MyGiftActivity",
        "com.flamingo.gpgame.view.activity.MyBillActivity",
        "com.flamingo.gpgame.view.activity.MyLevelActivity",
        "com.flamingo.gpgame.module.my.honey.view.activity.MyHoneyActivity",
        "com.flamingo.gpgame.view.activity.TaEventActivity",
        "com.flamingo.gpgame.view.activity.TaVideoActivity",
        "com.flamingo.gpgame.view.activity.CardRecordsActivity",
        "com.flamingo.gpgame.view.activity.MedalListActivity",
        "com.flamingo.gpgame.view.activity.MyVoucherActivity",
        "com.flamingo.gpgame.view.activity.VoucherDetailActivity",
        "com.flamingo.gpgame.view.activity.NotificationActivity",
        "com.flamingo.gpgame.view.activity.MoreGiftListActivity",
        "com.flamingo.gpgame.view.activity.MoreHotGamesWithGiftActivity",
        "com.flamingo.gpgame.view.activity.GiftCenterActivity",
        "com.flamingo.gpgame.view.activity.DownloadMainActivity",
        "com.flamingo.gpgame.module.account.view.activity.LoginActivity",
        "com.flamingo.gpgame.module.account.view.activity.PhoneNumRegister_ForgetPassword_Activity",
        "com.flamingo.gpgame.module.account.view.activity.PhoneRegisterSetPassword_ResetPasswordByMobile_Activity",
        "com.flamingo.gpgame.module.account.view.activity.GuopanNameRegister_ResetPasswordByOldPassword_Activity",
        "com.flamingo.gpgame.module.account.view.activity.GPPhoneBindActivity",
        "com.flamingo.gpgame.module.account.view.activity.MyInfoActivity",
        "com.flamingo.gpgame.module.account.view.activity.SetUserInfoActivity",
        "com.flamingo.gpgame.view.activity.MyHomeActivity",
        "com.flamingo.gpgame.view.activity.GPMyActivity",
        "com.flamingo.gpgame.view.activity.MyPostActivity",
        "com.flamingo.gpgame.view.activity.MyFavouriteActivity",
        "com.flamingo.gpgame.view.activity.MyDraftActivity",
        "com.flamingo.gpgame.view.activity.FollowOrFanActivity",
        "com.flamingo.gpgame.view.activity.PostZanListActivity",
        "com.flamingo.gpgame.view.activity.TaHomeActivity",
        "com.flamingo.gpgame.module.account.view.activity.MyDetailSetSignatureActivity",
        "com.flamingo.gpgame.view.activity.PicChooseActivity",
        "com.flamingo.gpgame.view.activity.GPPicChooseDialogActivity",
        "com.flamingo.gpgame.module.detail.DetailActivity",
        "com.flamingo.gpgame.view.activity.GuideWindowActivity",
        "com.flamingo.gpgame.module.open.view.activity.OpenTestServerActivity",
        "com.flamingo.gpgame.module.subject.view.activity.SubjectListActivity",
        "com.flamingo.gpgame.view.activity.PicClipActivity",
        "com.flamingo.gpgame.view.activity.GPPicChooseMiddleEmptyActivity",
        "com.flamingo.gpgame.module.account.view.activity.LoginEmptyActivity",
        "com.flamingo.gpgame.module.market.view.activity.GPGameBuyEmptyActivity",
        "com.flamingo.gpgame.view.activity.SimpleWebViewActivity",
        "com.flamingo.gpgame.view.activity.SignUpWebViewActivity",
        "com.flamingo.gpgame.view.activity.ActionActivity",
        "com.flamingo.gpgame.view.activity.GameAndWebActivity",
        "com.flamingo.gpgame.view.activity.GPSearchMainActivity",
        "com.flamingo.gpgame.view.activity.CommentListActivity",
        "com.flamingo.gpgame.view.activity.GPSettingActivity",
        "com.flamingo.gpgame.view.activity.GPFeedbackSuggestActivity",
        "com.flamingo.gpgame.view.activity.GPAboutUsActivity",
        "com.flamingo.gpgame.view.activity.LargeViewActivity",
        "com.flamingo.gpgame.view.activity.GPMainViewRecommendActivity",
        "com.flamingo.gpgame.view.activity.MyHeadImageActivity",
        "com.flamingo.gpgame.module.my.message.view.MyMessageActivity",
        "com.flamingo.gpgame.module.my.message.view.ChatListActivity",
        "com.flamingo.gpgame.module.my.message.view.BlockListActivity",
        "com.flamingo.gpgame.module.my.games.view.MyGamesListActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GroupDetailActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GroupPostReportActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.PostDetailActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GroupListActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.MyGroupListActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.PostCommentDetailActivity",
        "com.flamingo.gpgame.module.market.view.activity.VoucherMarketActivity",
        "com.flamingo.gpgame.module.market.view.activity.VIPMarketActivity",
        "com.flamingo.gpgame.module.market.view.activity.VIPMarketWebActivity",
        "com.flamingo.gpgame.module.market.view.activity.HoneyMarketActivity",
        "com.flamingo.gpgame.module.market.view.activity.HoneyExchangeRecordsActivity",
        "com.flamingo.gpgame.module.market.view.activity.HoneyMarketAllActivity",
        "com.flamingo.gpgame.module.market.view.activity.GoodsDetailActivity",
        "com.flamingo.gpgame.module.market.view.activity.ModuleVoucherListActivity",
        "com.flamingo.gpgame.module.market.view.activity.MyVoucherOrderActivity",
        "com.flamingo.gpgame.module.market.view.activity.MyVoucherOrderDetailActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GroupActivityActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GroupVideoActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GroupAllGameVideoActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GroupUserVideoAndHotPostActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.TrashPostActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.PostPublishActivity",
        "com.flamingo.gpgame.view.activity.PlayVideoActivity",
        "com.flamingo.gpgame.view.activity.GPGameConnectActivity",
        "android.intent.action.VIEW",
        "android.intent.category.DEFAULT",
        "android.intent.category.BROWSABLE",
        "data",
        "gpgame",
        "com.flamingo.gpgame.view.activity.GPH5GameActivity",
        "com.flamingo.gpgame.module.rank.view.activity.GPGameRankActivity",
        "com.flamingo.gpgame.utils.permission.PermissionRequestActivity",
        "com.flamingo.gpgame.utils.permission.PermissionSettingActivity",
        "com.flamingo.gpgame.utils.permission.PermissionDialogActivity",
        "com.flamingo.gpgame.module.gpgroup.view.activity.GroupRecommendActivity",
        "com.flamingo.gpgame.module.game.view.CrackGameActivity",
        "com.flamingo.gpgame.module.game.view.RankGameListActivity",
        "com.flamingo.gpgame.module.my.honey.view.activity.BindSettingActivity",
        "com.flamingo.gpgame.module.task.view.activity.TaskExperienceActivity",
        "com.flamingo.gpgame.module.task.view.activity.TaskJuniorMissionActivity",
        "com.flamingo.gpgame.module.task.view.activity.TaskGameTrialActivity",
        "com.flamingo.gpgame.module.task.view.activity.TaskMakeMoneyActivity",
        "com.flamingo.gpgame.module.task.view.activity.TaskMyTaskActivity",
        "com.flamingo.gpgame.module.bind.BindWeChatActivity",
        "com.flamingo.gpgame.receiver.WifiDownloadReceiver",
        "com.flamingo.gpgame.receiver.NetworkChangeReceiver",
        "android.intent.action.BOOT_COMPLETED",
        "android.net.conn.CONNECTIVITY_CHANGE",
        "com.flamingo.gpgame.receiver.PackageInstallReceiver",
        "android.intent.action.PACKAGE_ADDED",
        "android.intent.action.PACKAGE_REPLACED",
        "android.intent.action.PACKAGE_REMOVED",
        "service",
        "com.flamingo.gpgame.service.GPGameAccessibilityService",
        "android.permission.BIND_ACCESSIBILITY_SERVICE",
        "android.accessibilityservice.AccessibilityService",
        "meta-data",
        "android.accessibilityservice",
        "com.flamingo.gpgame.service.GPDownloadService",
        "com.flamingo.gpgame.engine.image.glide.GPGlideModule",
        "GlideModule",
        "com.alipay.sdk.app.H5PayActivity",
        "com.alipay.sdk.auth.AuthActivity",
        "com.flamingo.gpgame.module.pay.ui.GPMainActivity",
        "com.yintong.secure.activity.BaseActivity",
        "com.yintong.secure.service.PayService",
        "com.payeco.android.plugin.PayecoPluginLoadingActivity",
        "com.heepay.plugin.activity.WeChatNotityActivity",
        "com.payeco.android.plugin.PayecoCameraActivity",
        "com.payeco.android.plugin.PayecoVedioActivity",
        "com.payeco.android.plugin.vedio",
        "com.baidu.location.f",
        ":remote",
        "com.baidu.location.service_v2.2",
        "com.baidu.lbsapi.API_KEY",
        "DhNxS47YIr3CNdocGoAcy1ts",
        "com.ipaynow.wechatpay.plugin.inner_plugin.wechat_plugin.activity.WeChatNotifyActivity",
        "com.flamingo.gpgame.wxapi.WXEntryActivity",
        "com.flamingo.gpgame.utils.share.sina.SinaShareCallBackActivity",
        "com.tencent.tauth.AuthActivity",
        "tencent1105114001",
        "com.tencent.connect.common.AssistActivity",
        "com.flamingo.gpgame.utils.share.qq.QQShareCallBackActivity",
        "com.flamingo.gpgame.utils.share.system.SystemShareCallBackActivity",
        "org.egret.egretruntimelauncher.webview.WebViewActivity",
        "org.egret.egretruntimelauncher.GamePlayActivity",
        "com.flamingo.gpgame.module.account.view.activity.GPSetAccountActivity",
        "com.flamingo.gpgame.module.account.view.activity.GPRealNameVerifiedActivity",
        "com.flamingo.gpgame.module.account.view.activity.GPRealNameVerifiedDetailActivity",
        "com.flamingo.gpgame.module.account.view.activity.GPIdVerifiedActivity",
        "com.flamingo.gpgame.module.task.view.activity.TaskChargeActivity",
        "com.flamingo.gpgame.module.subject.view.activity.SubjectGiftDetailActivity",
        "com.flamingo.gpgame.module.subject.view.activity.SubjectGameDetailActivity",
        "com.flamingo.gpgame.module.reservation.view.activity.AllReservationGameList",
        "com.flamingo.gpgame.module.reservation.view.activity.MyReservationGameList",
        "com.devbrackets.android.exomedia.receiver.MediaControlsReceiver",
        "android.intent.action.MEDIA_BUTTON",
        "com.sina.weibo.sdk.web.WeiboSdkWebActivity",
        "com.sina.weibo.sdk.share.WbShareTransActivity",
        "com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY",
        "com.tencent.android.tpush.XGPushActivity",
        "android.intent.action",
        "com.tencent.android.tpush.XGPushReceiver",
        ":xg_service_v3",
        "com.tencent.android.tpush.action.SDK",
        "com.tencent.android.tpush.action.INTERNAL_PUSH_MESSAGE",
        "android.intent.action.USER_PRESENT",
        "android.bluetooth.adapter.action.STATE_CHANGED",
        "android.intent.action.ACTION_POWER_CONNECTED",
        "android.intent.action.ACTION_POWER_DISCONNECTED",
        "android.intent.action.MEDIA_UNMOUNTED",
        "android.intent.action.MEDIA_REMOVED",
        "android.intent.action.MEDIA_CHECKING",
        "android.intent.action.MEDIA_EJECT",
        "file",
        "com.tencent.android.tpush.rpc.XGRemoteService",
        "com.flamingo.gpgame.PUSH_ACTION",
        "com.tencent.android.tpush.service.XGPushServiceV3",
        "com.tencent.android.tpush.service.XGDaemonService",
        "provider",
        "com.tencent.android.tpush.XGPushProvider",
        "com.flamingo.gpgame.AUTH_XGPUSH",
        "com.tencent.android.tpush.SettingsContentProvider",
        "com.flamingo.gpgame.TPUSH_PROVIDER",
        "XG_V2_ACCESS_ID",
        "XG_V2_ACCESS_KEY",
        "AS1GX77R9W9X",
        "com.tencent.mid.api.MidProvider",
        "com.flamingo.gpgame.TENCENT.MID.V3"
    };

    public ValuePool() {
        mStringPools = new ArrayList<>();
//        for (String str : mStringCache) {
//            mStringPools.add(str);
//        }

        mResourceIds = new ArrayList<>();
        mUriToNameSpaceMaps = new HashMap<>();
        mNamespaceToUriMaps = new HashMap<>();
        mIntegers = new Stack<>();
    }

    public void putInteger(Integer integer) {
        mIntegers.push(integer);
    }

    public Integer pullInteger() {
        return mIntegers.pop();
    }

    public int getStringIndex(String string) {
        return mStringPools.indexOf(string);
    }

    public void addString(String string) {
        if (!mStringPools.contains(string)) {
            mStringPools.add(string);
        }
    }

    public String getString(int index) {
        if (index < 0 || index >= mStringPools.size()) {
            if (index == -1) {
                return "";
            }
            return "out of string bounds: " + index;
        }
        return mStringPools.get(index);
    }

    public int getStringSize() {
        return mStringPools.size();
    }

    public void addResourceId(int id) {
        mResourceIds.add(id);
    }

    public int getResourceId(int index) {
        return mResourceIds.get(index);
    }

    public int getResourceIdSize() {
        return mResourceIds.size();
    }


    public ArrayList<Integer> getResourceIds() {
        return mResourceIds;
    }

    public void putUriToNamespace(String uri, String nameSpace) {
        mUriToNameSpaceMaps.put(uri, nameSpace);
    }

    public void putNamespaceToUri(String uri, String nameSpace) {
        mNamespaceToUriMaps.put(nameSpace, uri);
    }

    public int getUri(String namespace) {
        return checkAndAddString(mNamespaceToUriMaps.get(namespace));
    }

    public String getNamespace(String uri) {
        return mUriToNameSpaceMaps.get(uri);
    }

    public HashMap<String, String> getUriToNameSpaceMaps() {
        return mUriToNameSpaceMaps;
    }

    public int checkAndAddString(String string) {
        if (string == null) {
            return -1;
        }
        int index = mStringPools.indexOf(string);
        if (index >= 0) {
            return index;
        } else {
            mStringPools.add(string);
            return mStringPools.size() - 1;
        }
    }

}
