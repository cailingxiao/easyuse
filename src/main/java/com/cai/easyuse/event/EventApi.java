package com.cai.easyuse.event;

import com.cai.easyuse.base.mark.UnConfusion;

import de.greenrobot.event.EventBus;

/**
 * 事件管理机制,基于EventBus
 * eventId从1~1024为easyuse使用
 * <p>
 * Created by cailingxiao on 2016/10/4.
 */
public final class EventApi {

    private EventApi() {

    }

    /**
     * 注册
     *
     * @param obj
     */
    public static void registerEvent(IBuiEvent obj) {
        EventBus.getDefault().register(obj);
    }

    /**
     * 解除注册
     *
     * @param obj
     */
    public static void unregisterEvent(IBuiEvent obj) {
        EventBus.getDefault().unregister(obj);
    }

    /**
     * 发送普通事件
     *
     * @param event
     */
    public static void sendEvent(BuiEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送事件，不带参数
     * <p></p>
     * eventId从1~1024为easyuse使用
     *
     * @param eventId
     */
    public static void sendEvent(int eventId) {
        EventBus.getDefault().post(new BuiEvent(eventId, null));
    }

    /**
     * 注册事件需要实现的方法
     */
    public interface IBuiEvent extends UnConfusion {
        void onEvent(EventApi.BuiEvent event);
    }

    /**
     * 注册事件发送的内容
     */
    public static class BuiEvent {
        public int eventId;
        public Object eventObj;

        public BuiEvent(int eventId, Object eventData) {
            this.eventId = eventId;
            this.eventObj = eventData;
        }
    }

}
