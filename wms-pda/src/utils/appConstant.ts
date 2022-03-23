/**
 * 用于存放一些常量
 * @type
 */
export let APPCONSTANT = {

  //用户登陆接口
  postLoginin: 'UserLogin/',

  //请求超时时间,单位为毫秒
  REQUEST_TIMEOUT: 600000,

  ExtranetServerIP: 'lims.yili.com:90',
  ServerIP: '192.168.1.221:8088',
  ServerIP1: 'localhost:8088',
  ServerIP3: '10.168.0.238:8088',
  TENANTID: '000000',
  APPID: 'PDA001',
  version: '1.0.0.8(beta7)',

  ch: {
    /** 每周第一天，0代表周日 */
    firstDayOfWeek: 0,
    /** 每周天数正常样式 */
    dayNames: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
    /** 每周天数短样式（位置较小时显示） */
    dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
    /** 每周天数最小样式 */
    dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
    /** 每月月份正常样式 */
    monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
    /** 每月月份短样式 */
    monthNamesShort: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]
  },

  //日历控件年选择范围
  yearrange: "2000:2050",
};

export let ContenType = {
  Form: 'application/x-www-form-urlencoded;charset=UTF-8',
  Json: 'application/json;charset=UTF-8',
  Content: ''
}
export let Method = {
  Get: 'GET',
  Post: 'POST'
}
export let Api = {

  ///"登录接口"
  Login: "/blade-auth/oauth/token",

  ///"入库接口"
  // 条码管理
  Instock: "/api/ApiPDA/InStockPDA",

  //通过收货单编码模糊查询
  AsnHeaderList: "/AsnHeaderList",
  //通过收货单编码查询收货单详细信息
  AsnHeaderGetAsnHeaderDetail: "/getAsnHeaderDetail",
  //收货提交
  submitAsnHeader: "/AsnHeaderSubmitAsnHeader",

  submitAsnHeaderWithOrder:"/submitAsnHeaderWithOrder",

  submitPickInfoWithOrder:'/submitPickInfoWithOrder',
  //收货创建类型单据列表
  getAsnHeaderList: "/getAsnHeaderList",
  getAsnHeaderAndDetails: "/getAsnHeaderAndDetails",
  takewayList: "/ApiPDA/takewayList",
  //获取已提交序列号
  snlistInfo: "/SnListInfo",
  //通过容器编码查询库存信息
  queryStockByLpnCode: "/AsnHeaderQueryStockByLpnCode",
  //上架提交
  submitPutaway: "/AsnHeaderSubmitPutaway",
  //按箱查询
  getStockInfoPutawayForBox: "/getStockInfoPutawayForBox",
  //按箱上架提交
  submitPutawayForBox: "/submitPutawayForBox",
  //新按箱上架提交
  submitPutawayForBoxNew: "/submitPutawayForBoxNew",
  //批量按托上架
  submitPutawayNew:'/submitPutawayNew',
  //新按箱移动提交
  submitMoveForBoxNew:"/submitMoveForBoxNew",
  //新按箱根据箱号获取推荐信息
  getInfoMoveByBoxCode: '/getInfoMoveByBoxCode',
  //通过LPN获取物品明细
  GetAsnLpnDetail: "/getAsnLpnDetail",
  //按托收货提交
  StockAddByLPN: "/stockAddByLPN",
  //入库序列号验证
  isHasSerial: "/isHasSerial",
  //获取按箱收货的单据信息
  getAsnHeaderInfoForBox: '/getAsnHeaderInfoForBox',
  //通过收货单编号查询详情
  getAsnHeaderDetailForBox: '/getAsnHeaderDetailForBox',
  //复制标签根据扫描物品箱码信息查询库存
  getLabelInfoWithStock: '/getLabelInfoWithStock',
  //按箱收货提交
  submitAsnHeaderForBox: '/submitAsnHeaderForBox',
  //按托收货提交
  submitAsnHeaderForTray:'/submitAsnHeaderForTray',

  submitTrayPickInfo:'/submitTrayPickInfo',
  //清点记录查询
  ContainerLogList: '/ContainerLogList',
  //更新箱号
  getBoxCode: '/getBoxCode',
  //根据箱号获取推荐信息
  getRecommendInfoByBoxCode: '/getRecommendInfoByBoxCode',
  //根据托盘号获取信息
  getInfoByLpnCode: '/getInfoByLpnCode',
  ///"公共接口"
  // 接口
  api: "/api",

  // 获取货主
  ApiPDA: "/ApiPDA/Warehouseselect",
  // 登录
  token: "/ApiPDA/token",
  //获取权限
  routes: "/ApiPDA/routes",
  //修改密码
  UpdatePassword: "/ApiPDA/UpdatePassword",
  // 获取版本号和地址
  UpdateVerDetail: "/ApiPDA/UpdateVerDetail",
  //获取库存明细
  StockDetail: "/ApiPDA/StockDetail",
  //条码规则列表
  BarcodeRuleList: "/ApiPDA/BarcodeRuleList",
  //物品详情(盘点用)
  skuDetail: "/ApiPDA/skuDetail",
  //库存查询
  StockQuery: "/ApiPDA/StockQuery",
  frozenForSku:"/ApiPDA/frozenForSku",
  //物品列表(盘点用)
  SkuList: "/ApiPDA/skuList",
  getSkuBaseUm:'/ApiPDA/getSkuBaseUm',
  //拼托查询出库暂存区的物品
  querySku: "/ApiPDA/querySku",
  //根据容器编码查询库位(库存)
  stockGetLocByLpnCode: "/ApiPDA/stockGetLocByLpnCode",
  //获取在线用户列表
  UserOnlineList: '/ApiPDA/userOnlineList',
  //获取在线用户签到日志
  UserRegisterList: '/ApiPDA/userRegisterList',
  //新增或修改日志和在线用户
  UserOnlinesubmit: '/ApiPDA/userOnlinesubmit',
  //获取当月日志签到列表
  userRegisterListMoth: '/ApiPDA/userRegisterListMoth',
  //获取打印模板
  printTemplateList: '/ApiPDA/printTemplateList',
  //库存查询
  ParamList: "/ApiPDA/ParamList",
  //根据物品ID获取当前物品的批属性以及验证
  skuLotList: "/skuLotList",

  ///"出库接口"
  //
  Outstock: "/api/ApiPDA/OutStock",

  // 库存转移
  submitMOVE: "/submitMOVE",
  //按件获得拣货信息
  getPickInfo: "/getPickInfo",
  //按箱拣货获得拣货信息
  getPickInfoByBox: "/getPickInfoByBox",

 //按单拣货获得拣货信息
 getPickInfoByOrder: "/getPickInfoByOrder",

  //提交拣货信息
  submitPickInfo: '/submitPickInfo',
  //按箱提交拣货信息
  submitPickInfoByBox: '/submitPickInfoByBox',
  getTotalScanQtyBySku: '/getTotalScanQtyBySku',
  //拣货记录列表
  getSopickLog: '/getSopickLog',
  //容器序列号验证
  OutIsHasSerial: '/isHasSerial',
  //出库拼托明细列表
  getOutstockLpnInfo: '/getOutstockLpnInfo',
  //提交出库拼托信息
  submitOutStockLpnInfo: '/submitOutStockLpnInfo',
  //拼托获取卡板号
  getLpnCode: '/getLpnCode',
  ///"盘点接口"
  //
  StockCountPDA: "/api/ApiPDA/StockCountPDA",

  //静态盘点详情
  cyclecountrantask: "/detailPda",
  //获取盘点记录
  countRecords: '/countRecords',

  //随机盘点提交
  randomCountSubmit: "/randomCountSubmit",
  //欣天新随机盘点提交
  randomCheckSubmit: "/randomCheckSubmit",
  //欣天新盘点任务提交
  randomCheckTaskSubmit: "/randomCheckTaskSubmit",
  //随机盘点获取库存
  stockListPda: "/stockListPda",
  //单据盘点提交
  billCountSubmit: "/billCountSubmit",
  //盘点容器序列号验证
  CountPDAisHasSerial: "/isHasSerial",
  //完成盘点单库位
  completeTask: '/completeTaskPda',


  ///"装车接口"
  //
  LoadingPDA: "/api/ApiPDA/LoadingPDA",
  //删除装车明细
  deleteTruckDetail: '/deleteTruckDetail',
  //根据车次编码查询车次信息
  getTruckHeader: '/getTruckHeader',
  //根据LPNCode查询物品简要信息
  getSkuStock: '/getSkuStock',
  //根据LPNCode查询物品简要信息
  saveSoTruckDetail: '/saveSoTruckDetail',
  //根据LPNCode查询物品简要信息
  getSkuStockDetailList: '/getSkuStockDetailList',
  //更改发运状态
  // confirmSo: '/confirmSo',
  //提交发货
  SaveconfirmSo: '/SaveconfirmSo',
  //根据装车牌获取信息
  soRegisterDetail: '/soRegisterDetail',
  //新增发运
  soTruckHeaderSubmit: '/soTruckHeaderSubmit',
  //获取明细
  Lodingdetail: '/Lodingdetail',

  ///"库存移动"
  //
  StockMove: "/api/ApiPDA/StockMove",

  //通过库位编码查询容器编码
  getLocCode: "/getLocCode",
  //查询物品信息
  getSkuInfo: "/getSkuInfo",
  //提交物品信息获取库存批次
  submitSku: '/submitSku',
  //批次提交验证数量
  submitLotNumberSku: '/submitLotNumberSku',
  //物品移动提交接口
  submitMoveStockSku: '/submitMoveStockSku',
  //容器序列号验证
  StockIsHasSerial: '/isHasSerial',


  ///"任务接口"
  //
  taskPDA: "/api/ApiPDA/taskPDA",

  //获取任务集合
  taskPDAList: '/list',
  //关闭任务
  taskPDAClose: '/close',


  ///库内接口
  //
  stockInner: '/api/ApiPDA/stockInner',

  //待拣货列表
  upPickList: '/upPickList',
  //提交拣货信息
  stockInnerSubmitPickInfo: '/submitPickInfo',
  //待打包物品
  upPack: '/upPack',
  //提交打包信息
  submitPackInfo: '/submitPackInfo',
  //打包物品列表
  packStockList: '/packStockList',
  //按箱移动-查询待移动库存
  getMoveStock: '/getMoveStock',
  //按箱移动-物品移动验证
  verifyStockForMoveByBox: '/verifyStockForMoveByBox',
  //按箱移动-提交库存移动信息
  submitStockForMoveByBox: '/submitStockForMoveByBox',
  //补货-获得补货明细信息
  getReplenishmenInfo: '/getReplenishmenInfo',
  //补货-获得物品列表
  getSkuListByCode: '/getSkuListByCode',
  //补货-提交补货信息
  submitReplenishmen: '/submitReplenishmen',
  //补货-查询已完成补货任务明细
  getUnfinishReplenishmenList: '/getUnfinishReplenishmenList',



  ///字典接口
  //
  bladeSystem: '/blade-system',

  dictList: '/dict/dictionary'
};

export let BaseCodeConstant = {
  //静态盘点(未分配)
  UnAllot: "10",

  //静态盘点(未完成)
  UnComplate: "11",

  //静态盘点(已完成)
  Complated: "12",

  // 到货登记任务
  task_type_101: "101",

  // 入库清点
  task_type_102: "102",

  // 拣货任务
  task_type_103: "103",

  //盘点任务
  task_type_104: "104",

  //打包任务
  task_type_106: "106",

  //拼托任务
  task_type_107: "107",

  //拼托任务
  task_type_108: "108",

  // 入库单 序列号状态(待入库)
  SN_STATUS_1: "1",

  // 入库单 序列号状态(已清点)
  SN_STATUS_2: "2",

  // 容器位状态（空闲）
  CTL_STATE_FREE: "40",

  // 容器位状态（占用）
  CTL_STATE_OCCUPY: "41",

  // 数据字典(停用)
  IS_ACTIVE_N: "N",

  // 数据字典(在用)
  IS_ACTIVE_Y: "Y",

  // 空格字符
  SPACE_STRING: " ",

  // 空字符
  NULL_STRING: "",

  // 指定字符分割字符串
  //  public const char SPLIT_STRING: '|',

  // 流程 - 执行中
  IS_ACTIVE_ING: "1",

  // 流程 -已关闭
  IS_ACTIVE_CLOSED: "0",


  /// 出库方式
  // 出库方式编码
  OUTSTORE_TYPE: "110",

  // 出库方式编码（杂项出库）
  OUTSTORE_TYPE_10: "10",

  // 出库方式编码（调拨出库）
  OUTSTORE_TYPE_20: "20",

  // 出库方式编码（借货出库）
  OUTSTORE_TYPE_30: "30",

  // 出库方式编码（常规出库）
  OUTSTORE_TYPE_40: "40",

  // 出库方式编码（维修出库）
  OUTSTORE_TYPE_50: "50",


  /// "入库方式"
  // 入库方式分组编码
  IN_STORE_TYPE: "103",

  // 入库方式（采购入库）
  IN_STORE_TYPE_PURCHASE: "5",

  //入库方式（清点单入库）
  IN_STORE_Inventory_PURCHASE: "11",

  //入库方式（质检复合单入库）
  IN_STORE_QUALITY__PURCHASE: "12",

  // 入库方式（常规入库）
  IN_STORE_TYPE_ROUTINE: "10",

  // 入库方式（杂项入库）
  IN_STORE_TYPE_NOT_BILL: "20",

  // 入库方式（调拨入库）
  IN_STORE_TYPE_ALLOT: "30",

  // 入库方式（归还入库）
  IN_STORE_TYPE_BORROW: "40",

  // 入库方式（维修入库）
  IN_STORE_TYPE_REPAIR: "50",

  // 入库方式（退库入库）
  IN_STORE_TYPE_RETURN: "60",


  /// "时间常量"
  // 时间转换格式
  DATE_TIME: "yyyy/MM/dd",

  // 时间转换格式
  DATE_TIME_TWO: "yyyyMMdd",

  // 时间转换格式
  DATE_TIME_OTHER: "yyyy-MM-dd",


  ///"报废管理"
  /// "报废类型"
  // 报废类型（在库报废）
  SCRAP_TYPE_10: "10",

  // 报废类型（现场报废）
  SCRAP_TYPE_20: "20",


  ///"包装关系"
  // 默认添加层级1

  DEFAULT_SKU_LEVEL: 1,

  // 默认换算倍率1

  DEFAULT_CONVERT_QTY: 1,


  /// "盘点货位状态"

  // 盘点货位状态-已完成

  COUNT_LOCATION_COMPLETE: "L2",

  // 未盘点单据状态
  //  NOT_COUNT_STATE: { "10", "30" },




  /// 出库单状态
  // 出库单状态
  SO_BILL_STATE: "105",

  // 单据创建
  SO_BILL_STATE_10: "10",

  // 处理中
  SO_BILL_STATE_20: "20",

  // 部分发货
  SO_BILL_STATE_30: "30",

  // 已完成
  SO_BILL_STATE_40: "40",

  // 已撤销(2天内)
  SO_BILL_STATE_91: "91",

  // 挂起
  SO_BILL_STATE_92: "92",

  // 已撤销
  SO_BILL_STATE_93: "93",

  // 未关闭单据状态
  //  SO_NOT_COMPLETE_STATE: { "10", "20", "30" },



  /// "入库单据状态"
  // 收货单单据状态
  IN_STORE_STATE: "104",

  // 收货单单据状态-单据创建
  IN_STORE_STATE_CREAT: "10",

  // 收货单单据状态-处理中
  IN_STORE_STATE_HANDLE: "20",

  // 收货单单据状态-完成
  IN_STORE_STATE_COMPLETE: "40",

  // 收货单单据状态-撤销
  IN_STORE_STATE_RETURN: "90",

  // 未关闭单据状态
  //  IN_STORE_NOT_COMPLETE_STATE: { "10", "20", "30", "90" },

  // 到货登记状态
  //  IN_STORE_REGISTER_STATE: { "10", "30" },


  /// "盘点单据状态"
  // 盘点单单据状态
  COUNT_STATE: "111",

  // 盘点单单据状态-单据创建
  COUNT_STATE_CREAT: "10",

  // 盘点单单据状态-正在盘点
  COUNT_STATE_HANDLE: "30",

  // 盘点单单据状态-盘点完成
  COUNT_STATE_COMPLETE: "40",

  // 盘点单单据状态-已作废
  COUNT_STATE_ABNADONED: "50",

  // 盘点单单据状态-已调整
  COUNT_STATE_EXECUTE: "90",

  // 未关闭单据状态
  //  NOT_COMPLETE_STATE: { "10", "20", "30", "40", "60", "70" },


  /// "报废单据状态"
  // 报废单单据状态
  SCRAP_STATE: "130",

  // 报废单单据状态(单据创建)
  SCRAP_STATE_10: "10",

  // 报废单单据状态(报废中)
  SCRAP_STATE_20: "20",

  // 报废单单据状态(部分报废)
  SCRAP_STATE_30: "30",

  // 报废单单据状态(已完成)
  SCRAP_STATE_40: "40",

  // 未关闭单据状态
  //  SCRAP_STATE_NOT_COMPLETE_STATE: { "10", "20", "30" },


  /// "移库单据状态"
  // 移库单单据状态
  TRANS_STATE: "117",

  // 移库单单据状态-单据创建
  TRANS_STATE_CREAT: "10",

  // 移库单单据状态-正在移库
  TRANS_STATE_HANDLE: "20",

  // 移库单单据状态-已完成
  TRANS_STATE_COMPLETE: "30",

  // 未关闭单据状态
  //  TRANS_STATE_NOT_COMPLETE_STATE: { "10", "20" },



  // 危险品管理
  MANAGE_MODE_2: "2",

  // 最大单位
  MAX_SKU_LEVEL: "1",

  //按托收货标识
  INVENTORY_0: "0",

  //按件收货标识
  INVENTORY_1: "1",

  //按箱收货标识
  INVENTORY_2: "2",

  //按托上架标识
  PUtAWAY_0: "0",

  //按件上架标识
  PUtAWAY_1: "1",

  //按箱上架标识
  PUtAWAY_2: "2",

  //按件拣货标识
  TAKEAWAY_1: "1",

  //按箱拣货标识
  TAKEAWAY_2: "2",
  
  //按托拣货标识
   TAKEAWAY_3: "3",
};

//自定义字段
export let AppConstant = {
  skuLot: 'skuLot',
  MaxLength: 999,
  Date: 'Date',
  Text: 'Text',
  Batch: 1,//批次类型
  BatchList: 3,//批次列表
  serialNumber: 2,//序列号类型
  serialNumberListNum: 4,//序列号列表
  Warehouse: 0,//库房类型
  buttonAffirm: '确认',
  buttonEnt: '提交',
  /*箱码规则类型*/
  ruleCode_00: 0,//系统自动生成,允许前端修改
  ruleCode_01: 1,//系统自动生成，不允许前端修改
  ruleCode_02: 2,//前端输入
  /*标签解析类型*/
  labelType: {
    sample: 'sample',//样品标签
    process: 'process',//半成品标签
    standard: 'process',//标准标签
    simple: 'simple',//精简标签
    common: 'process',//常用标签
    production: 'process',//生产标签
    other: 'process',//其他标签
  },
}


