# easyuse依赖模块工程

## easyuse 文件夹，存放源文件

## app 文件夹，存放测试工程

## easyuse以aar包形式向外提供，输出时已经进行混淆，所以外部请勿混淆
-keep class com.cai.easyuse.** {*;}

## 输出请使用gradle命令（assembleRelease），产物会输出到out文件夹

## baseBiz存放基本业务逻辑依赖

## 注意事项
1. 外部的编译sdk版本也需要是25
2.基本工具列表：
2.1： eventbus 转发消息
2.2： fastjson 解析json
2.3： glide 加载图片
2.4： greendao 数据库管理
2.5： okhttp 网络请求
2.6： okio 网络请求依赖
2.7： umeng，utdid4all 统计
2.8： zxing 扫码
2.9： butterknife 注解view
2.10： retrifit&rx 网络加强  待添加