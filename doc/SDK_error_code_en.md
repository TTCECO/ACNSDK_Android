
## Detailed error code
This document lists the error codes and corresponding instructions returned by the server and SDK.

### 100

```
description：appId cannot be empty
meaning：appId cannot be an empty string
```
### 101

```
description：SecretKey cannot be empty
meaning：SecretKey cannot be an empty string
```

### 102

```
description：userID cannot be empty
meaning：userID cannot be an empty string
```

### 103

```
description：SDK is not registered, please register
meaning：The APP must first call the registration interface to perform other operations.
```

### 104

```
description：SDK is disabled
meaning：The SDK is set to be unavailable, call SDKEnable, it can be used normally.
```

### 105

```
description：Update information must be current user
meaning：Update user information must be current user to see if the user is different
```

### 106

```
description：No logged in user
meaning：The user has not logged in yet, and the login interface is called.
```
### 107

```
description：no bound wallet
meaning：The current user has not yet bound the wallet
```

### 108

```
description：Extra is empty
meaning：Extra cannot be empty
```

### 109

```
description：Extra is not json
meaning：Extra must be in json format
```

### 110

```
描述：Behavior less than 100
含义：Actiontype must exceed 100
```

### 200

```
description：Request data exception, protobuf object failed to encode as data
meaning：Request data exception, protobuf object failed to encode as data
```
### 201

```
description：The agreement does not exist
meaning：The agreement does not exist
```

### 202

```
description：Return data error
meaning：Return data error
```

### 203

```
description：Return data parsing error
meaning：Return data parsing error
```

### 204

```
description：Service exception
meaning：Service exception
```

### 205

```
description：The network is successful, but the server return a error.
meaning：The network is successful, but the server returned an error
```

### 206

```
description：In request
meaning：Get sdk information request is in progress
```

### 207

```
description：Error of `URLSession`
meaning：Error when connecting to the network
```

### 208

```
description：Error while creating `URLRequest` from `Request`.
meaning：Error while requesting creation
```

### 209

```
description：Error while creating `Request.Response` from `(Data, URLResponse)`.
meaning：Request callback error
```

### 300

```
description：
meaning：RPC request address balance error
```
