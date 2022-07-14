# AGV

> v1.0.0

## 订单同步

POST /agv/syncOrder

用于AGV开始执行，结束执行，异常中断时通知调度系统使用。

> Body 请求参数

```json
{
  "jobId": "123456789",
  "type": "BEGIN",
  "agvName": "AGV001",
  "exceptionMsg": "AGV异常时发送该字段"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|jobId|body|string| 是 | JOB ID|由调度系统调用AGV接口：【创建指定名称的订单】时，传入properties内；格式：{”key“:"jobId","value":"1234567890"}|
|agvType|body|string| 是 | 类型|只能是：BEGIN（开始执行），END（结束执行），EXCEPTION（异常中断）；注意：上述英文区分大小写。|
| agvName|body|string| 是 | 车辆名称|AGV方的车辆名称|
| msg|body|string¦null| 是 | 异常消息|type=EXCEPTION时必填|

> 返回示例

> 成功

```json
{
  "code": 0,
  "msg": "操作成功"
}
```

> 失败 

```json
{
  "code": 500,
  "msg": "失败消息"
}
```

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|状态码|0：成功，其他：失败。|
|» msg|string|true|none|返回内容|none|

## 同步非运输过程中的异常

POST /agv/syncException

AGV同步非运输过程中的异常。

> Body 请求参数

```json
{
  "jobId": "123456789",
  "type": "BEGIN",
  "agvName": "AGV001",
  "exceptionMsg": "AGV异常时发送该字段"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|jobId|body|string| 否 | JOB ID|由调度系统调用AGV接口：【创建指定名称的订单】时，传入properties内；格式：{”key“:"jobId","value":"1234567890"}|
| agvName|body|string| 否 | 车辆名称|AGV方的车辆名称|
| code|body|string| 否 | 异常编码|none|
| msg|body|string¦null| 是 | 异常消息|异常消息|

> 返回示例


> 成功

```json
{
  "code": 0,
  "msg": "操作成功"
}
```

> 失败

```json
{
  "code": 500,
  "msg": "失败消息"
}
```

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» code|integer|true|none|状态码|0：成功，其他：失败。|
|» msg|string|true|none|返回内容|none|


