# easyuse依赖模块

封装了事件转发（eventbus）、json解析(fastjson)，图片加载(glide)，数据库(greendao)，网络请求(okhttp)，友盟统计(umeng-analytics)，二维码扫描功能(zxing)。

# 主要包功能如下：
1. app，内部包含基础的Application、Activity、AlertDialog、Dialog、Fragment；
2. base，内部包含对ListView的Adapter的优化使用方式holder；简单的回调接口mark；通用抽象自定义view和viewgroup，mvp模式使用的基础Model,View,Presenter；
3. config，基于配置assets目录下配置文件的读取；
4. database，基于db模块的数据库封装，内部有详细的说明；
5. event，基于eventbus的事件分发；
6. http，基于okhttp的网络请求库封装；
7. hybrid，基于网页的Prompt消息的桥接协议；
8. image，基于glide的图片引擎；
9. json，基于fastjson的json解析引擎；
10. language，对app应用语言切换的封装；
11. music，对app应用音乐播放的封装；
12. route，对app应用各activity间跳转的封装；
13. scan，基于zxing的扫码方式；
14. sp，对sharedpreference的封装；
15. statistics，对数据统计的封装；
16. update，对升级的封装，升级检测地址在assets的配置文件中；
17. util，工具；
18. video，视频播放器模块；
19. widget，自定义组件模块，包含：圆形进度条，倒计时，灰色保活，下拉刷新，圆形imageview，通用title，波动drawable
20. 接入butterknife，配合AS的 Zelezny 插件使用，无敌
