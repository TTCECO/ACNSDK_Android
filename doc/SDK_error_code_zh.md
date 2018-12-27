
## 错误码详解
本文档列举出服务端和 SDK 返回的错误码及相应说明。

### 100

```
描述：appId cannot be empty
含义：appId 不能是空字符串
```
### 101

```
描述：SecretKey cannot be empty
含义：SecretKey 不能是空字符串
```

### 102

```
描述：userID cannot be empty
含义：userID 不能是空字符串
```

### 103

```
描述：SDK is not registered, please register
含义：APP开启必须先调用注册接口，才能进行其他操作
```

### 104

```
描述：SDK is disabled
含义：SDK设置为不可用了，调用SDKEnable，开启可以正常使用
```

### 105

```
描述：Update information must be current user
含义：更新用户信息必须是当前用户，查看是否用户不同
```

### 106

```
描述：No logged in user
含义：用户还没有登录，调用登录接口
```
### 107

```
描述：no bound wallet
含义：当前用户尚未绑定钱包
```

### 108

```
描述：Extra is empty
含义：extra不能为空
```

### 109

```
描述：Extra is not json
含义：extra必须是json格式
```

### 110

```
描述：Behavior less than 100
含义：actiontype必须超过100
```

### 200

```
描述：Request data exception, protobuf object failed to encode as data
含义：请求数据异常，protobuf对象未能编码为data
```
### 201

```
描述：The agreement does not exist
含义：该协议不存在
```

### 202

```
描述：Return data error
含义：返回数据错误
```

### 203

```
描述：Return data parsing error
含义：返回数据解析错误
```

### 204

```
描述：Service exception
含义：服务异常
```

### 205

```
描述：The network is successful, but the server returns a error.
含义：网络成功，但是服务器返回错误
```

### 206

```
描述：In request
含义：拉取sdk信息请求正在进行中
```

### 207

```
描述：Error of `URLSession`
含义：网络连接时候出错
```

### 208

```
描述：Error while creating `URLRequest` from `Request`.
含义：请求创建时出错
```

### 209

```
描述：Error while creating `Request.Response` from `(Data, URLResponse)`.
含义：请求回调出错
```

### 300

```
描述：
含义：RPC请求地址余额出错
```
