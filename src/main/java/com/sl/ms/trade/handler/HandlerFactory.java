package com.sl.ms.trade.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.sl.ms.trade.annotation.PayChannel;
import com.sl.ms.trade.enums.PayChannelEnum;

import java.util.Map;

/**
 * Handler工厂，用于获取指定类型的具体渠道的实例对象
 */
public class HandlerFactory {

    private HandlerFactory() {

    }

    /**
     * 根据指定的支付渠道获取对应的具体渠道的实例
     * @param payChannel 支付渠道
     * @param handler    具体渠道的实例对象
     * @return 具体渠道的实例对象
     */
    public static <T> T get(PayChannelEnum payChannel, Class<T> handler) {
        // SpringUtil.getBeansOfType :获取所有指定类型的实例对象（被spring管理的且实现了指定类型的对象）
        Map<String, T> beans = SpringUtil.getBeansOfType(handler);
        for (Map.Entry<String, T> entry : beans.entrySet()) {
            // PayChannel 是PayChannelEnum枚举类型
            //entry.getValue() - 获取Spring容器中某个bean实例
            //     .getClass() - 获取该bean实例的Class对象
            //     .getAnnotation(PayChannel.class) - 从该Class对象上获取PayChannel注解
            PayChannel payChannelAnnotation = entry.getValue().getClass().getAnnotation(PayChannel.class);
            if (ObjectUtil.isNotEmpty(payChannelAnnotation) && ObjectUtil.equal(payChannel, payChannelAnnotation.type())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static <T> T get(String payChannel, Class<T> handler) {
        return get(PayChannelEnum.valueOf(payChannel), handler);
    }
}
