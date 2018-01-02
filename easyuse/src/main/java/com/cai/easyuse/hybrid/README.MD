# hybrid桥接器，包括Android端和H5端两部分。

Android端：
- 解析由Prompt发送过来的msg信息，msg是json字符串，格式如下：
```
msg = {
"callback":"回调名",   // 例如callback
"method":"android端要执行的函数",  // 例如startLoading
"actionId":"回调id",  // 例如12138，用来标示请求，可选
"args":"{xxxjson对象的字符串}"    // 给android端传递的参数
}
```


H5端：
- 判断UA，如果UA中包含easyuse-hybrid字符串，使用prompt进行和外部交互。
- 传递给android的msg使用json格式，包含上诉几个节点
- 给h5端传递的两个参数，actionId和result
- result必须要的字段是code,0表示成功，其他表示失败。如下所示
```
result={
"code":0,
"data":"数据"
}
```
// callback 是传递给android的callback的值
function callback(actionId,result){
    // h5在这里处理回调
}


内置打开网页的浏览器，InnerBrowseActivity.open(context,url);