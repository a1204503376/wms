webpackJsonp([14],{

/***/ 1372:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CyclecoutrantaskinfoPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__ = __webpack_require__(825);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__ = __webpack_require__(360);
var __assign = (this && this.__assign) || Object.assign || function(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
        s = arguments[i];
        for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
            t[p] = s[p];
    }
    return t;
};
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = y[op[0] & 2 ? "return" : op[0] ? "throw" : "next"]) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [0, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};










/**
 * Generated class for the CyclecoutrantaskinfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CyclecoutrantaskinfoPage = /** @class */ (function () {
    function CyclecoutrantaskinfoPage(navCtrl, navParams, appService, nativeService, modalCtrl, storage, ngZone, events, barcodeService) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.modalCtrl = modalCtrl;
        this.storage = storage;
        this.ngZone = ngZone;
        this.events = events;
        this.barcodeService = barcodeService;
        this.serialNumberList = []; //序列号已扫描集合
        this.disabled = true; //批属显示隐藏
        this.skuConfigArray = [];
        this.sysparms = []; //系统参数
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
            _this.lotNumber = data.filter(function (x) { return x.paramKey == 'account:lotCount'; })[0].paramValue;
        });
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.navParams.get('result'))) {
            this.result = this.navParams.get('result');
            this.locCode = this.result.locCode;
            this.whName = this.result.whName;
        }
        ;
    }
    /**
    * 数据格式化
    */
    CyclecoutrantaskinfoPage.prototype.clearMode = function () {
        this.skuCode = ''; //物品编号
        this.skuName = ''; //物品名称
        this.skuSpec = ''; //物品规格
        this.lpnCode = ''; //容器编码
        this.scanQtynum = ''; //实际数量
        // this.locCode = '';//库位编码
        this.skuPackage = null; //选中包装
        this.skuPackageDetailVOList = null; //包装列表
        this.skuPackageDetailVOItem = null; //包装选项
        this.resultSkuItem = null; //物品详情
        this.skuLot = null; //批属性信息
        this.skuLotVal = null; //批属性验证
        this.countQty = 0; //盘点数量
        this.serialNumberList = []; //序列号已扫描集合
        this.serialNumberModel = ""; //序列号实体
        this.ScanNumberLength = 0; //扫描的序列号数量
        this.stockList = null; //多批次实体
        this.stockItem = null; //批次选项
        this.disabled = true; //批属显示隐藏
        this.IsShow = this.Batch;
    };
    CyclecoutrantaskinfoPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        this.Batch = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].Batch;
        this.serialNumber = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].serialNumber;
        this.serialListNum = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].serialNumberListNum;
        this.BatchList = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].BatchList;
        this.IsShow = this.Batch;
        this.buttonEnt = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].buttonAffirm;
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["b" /* BarcodeType */].SKU) {
                _this.ngZone.run(function () {
                    _this.skuCode = barcode;
                    _this.scanWmsSku(barcode);
                });
            }
            else if (bt == __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["b" /* BarcodeType */].TuoPan) {
                _this.ngZone.run(function () {
                    _this.lpnCode = barcode;
                });
            }
            else {
                _this.ngZone.run(function () {
                    _this.barcodeService.ScanSkuBarcode(_this.scanView).subscribe(function (val) {
                        _this.scanWmsSku(val.skucode);
                    });
                });
            }
        });
    };
    CyclecoutrantaskinfoPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
    * sku回车事件
    */
    CyclecoutrantaskinfoPage.prototype.skuCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            this.scanWmsSku(this.skuCode);
        }
    };
    /**
   * 箱码扫描
   * @param event
   */
    CyclecoutrantaskinfoPage.prototype.scanModelChange = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.scanView)) {
                this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(function (val) {
                    _this.skuCode = val.skucode;
                    _this.scanWmsSku(val.skucode);
                });
            }
            else {
                this.nativeService.showToast('请扫描箱码');
            }
        }
    };
    /**
    * 容器回车事件
    */
    CyclecoutrantaskinfoPage.prototype.lpnCode_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            //获取最近使用的库位
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.lpnCode)) {
                this.nativeService.showToast('请输入容器编码');
            }
            var params = 'lpnCode=' + this.lpnCode;
            this.nativeService.showLoading();
            this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].stockGetLocByLpnCode, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["e" /* ContenType */].Form, function (result) {
                _this.nativeService.hideLoading();
                _this.locCode = result.data.locCode;
                console.log(result);
            });
        }
    };
    /*物品扫描*/
    CyclecoutrantaskinfoPage.prototype.scanWmsSku = function (skuCode) {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(skuCode)) {
            this.nativeService.showToast('物品编码不能为空');
        }
        var params = {
            skuCode: skuCode
        };
        //获取物品
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].SkuList, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            //判断是否多个物品
            _this.nativeService.hideLoading();
            console.log(result);
            if (result.data.length > 1) {
                //选择物品
                _this.openAsnRecordModal(result.data);
            }
            else {
                _this.resultSkuItem = result.data[0];
                if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(_this.resultSkuItem)) {
                    _this.InValAssignment();
                }
                else {
                    _this.nativeService.showToast('没有查询到物品');
                }
            }
        });
    };
    /**
     *查看物品明细
    */
    CyclecoutrantaskinfoPage.prototype.openAsnRecordModal = function (skuList) {
        var _this = this;
        var myModal = this.modalCtrl.create('SkuListModal', {
            skuList: skuList,
        });
        myModal.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(data)) {
                _this.resultSkuItem = data;
                _this.InValAssignment();
            }
        });
        myModal.present();
    };
    /**
    *页面赋值
    */
    CyclecoutrantaskinfoPage.prototype.InValAssignment = function () {
        this.skuName = this.resultSkuItem.skuName;
        this.skuSpec = this.resultSkuItem.spec;
        this.skuPackage = this.resultSkuItem.skuPackage;
        this.skuPackageDetailVOList = this.resultSkuItem.skuPackage.skuPackageDetailVOList;
        this.skuLot = this.resultSkuItem.skuLot;
        this.skuLotVal = this.resultSkuItem.skuLotVal;
        var minPackage = this.skuPackageDetailVOList.filter(function (x) { return x.skuLevel == 1; });
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(minPackage))
            this.skuPackageDetailVOItem = minPackage[0];
        this.skuConfigArray = [];
        for (var index = 1; index <= this.lotNumber; index++) {
            var skuConfig = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["b" /* SkuLotValResultModel */]();
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.skuLot['skuLotLabel' + index])) {
                skuConfig.skuLotLabel = this.skuLot['skuLotLabel' + index];
            }
            else {
                skuConfig.skuLotLabel = '批属性' + index;
            }
            skuConfig.skuLotMixMask = this.skuLotVal['skuLotMixMask' + index];
            skuConfig.getLotEditVal();
            this.skuConfigArray.push(skuConfig);
        }
        //isSn 1为序列号 0为批次号
        if (this.resultSkuItem.isSn == 1) {
            this.IsShow = this.serialNumber;
        }
    };
    /**
     * 提交
     */
    CyclecoutrantaskinfoPage.prototype.btnOk = function () {
        if (this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].Batch) {
            this.randomCountSubmit();
        }
        else if (this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].BatchList) {
            if (this.stockItem == '新增批次号') {
                for (var index = 0; index < this.skuConfigArray.length; index++) {
                    if (this.stockList[index]) {
                        var count = [];
                        for (var i = 1; i <= this.skuConfigArray.length; i++) {
                            var a = 'skuLot' + i;
                            if (this.stockList[index][a] != this.skuConfigArray[i - 1].LotValue) {
                                count.push(true);
                            }
                        }
                        if (count.length == 0) {
                            this.nativeService.showToast('新增批次值与' + this.stockList[index].lotNumber + '重复');
                            return;
                        }
                    }
                }
            }
            this.randomCountSubmit();
        }
        else {
            this.randomCountSubmit();
        }
    };
    //盘点提交
    CyclecoutrantaskinfoPage.prototype.randomCountSubmit = function () {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            var wspdId, userInfo, sku, lotNumber, skuLot, index, params;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, this.storage.get('userInfo')];
                    case 1:
                        userInfo = _a.sent();
                        if (this.sysparms.paramValue == 0) {
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.lpnCode)) {
                                this.nativeService.showToast('容器为空,请扫描容器');
                                return [2 /*return*/];
                            }
                        }
                        else {
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.stockItem))
                                this.lpnCode = this.stockItem.lpnCode;
                        }
                        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.locCode)) {
                            this.nativeService.showToast('库位为空,请扫描库位');
                            return [2 /*return*/];
                        }
                        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.resultSkuItem)) {
                            this.nativeService.showToast('物品信息没有扫描到,请扫描后在提交');
                            return [2 /*return*/];
                        }
                        if (this.resultSkuItem.isSn == 1) {
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.serialNumberList)) {
                                this.nativeService.showToast('序列号为空');
                                return [2 /*return*/];
                            }
                            this.countQty = this.ScanNumberLength;
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.skuPackageDetailVOList)) {
                                this.nativeService.showToast('当前物品选中的包装没有包装单位,请在PC绑定');
                                return [2 /*return*/];
                            }
                            sku = this.skuPackageDetailVOList.filter(function (x) { return x.skuLevel == 1; });
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(sku)) {
                                this.nativeService.showToast('当前物品没有基础计量单位的,请绑定');
                                return [2 /*return*/];
                            }
                            wspdId = sku[0].wspdId; //序列号获取包装
                        }
                        else {
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.skuPackageDetailVOItem)) {
                                this.nativeService.showToast('请选择包装');
                                return [2 /*return*/];
                            }
                            wspdId = this.skuPackageDetailVOItem.wspdId; //批次获取包装
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.countQty)) {
                                this.nativeService.showToast('数量为空,请输入数量');
                                return [2 /*return*/];
                            }
                        }
                        if (this.IsShow == this.BatchList) {
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.stockItem)) {
                                this.nativeService.showToast('请选择批次号');
                                return [2 /*return*/];
                            }
                        }
                        lotNumber = '';
                        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.stockItem) && this.stockItem != '新增批次号') {
                            lotNumber = this.stockItem.lotNumber;
                        }
                        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.stockItem)) {
                            this.boxCode = this.stockItem.boxCode;
                        }
                        skuLot = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["a" /* SkuLot */]();
                        for (index = 0; index < this.skuConfigArray.length; index++) {
                            skuLot['skuLot' + (index + 1)] = this.skuConfigArray[index].LotValue;
                        }
                        params = __assign({ lpnCode: this.lpnCode, locCode: this.locCode, skuId: this.resultSkuItem.skuId, lotNumber: lotNumber, countQty: this.countQty, wspdId: wspdId, boxCode: this.boxCode, userId: userInfo.tenant_id, serialNumberList: this.serialNumberList, 
                            // skuLot1: this.skuConfigArray[0].LotValue,
                            // skuLot2: this.skuConfigArray[1].LotValue,
                            // skuLot3: this.skuConfigArray[2].LotValue,
                            // skuLot4: this.skuConfigArray[3].LotValue,
                            // skuLot5: this.skuConfigArray[4].LotValue,
                            // skuLot6: this.skuConfigArray[5].LotValue,
                            // skuLot7: this.skuConfigArray[6].LotValue,
                            countBillNo: this.result.countBillNo, taskId: this.result.taskId }, skuLot);
                        this.nativeService.showLoading();
                        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].StockCountPDA + __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].billCountSubmit, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["e" /* ContenType */].Json, function (result) {
                            _this.nativeService.hideLoading();
                            if (result.data.stockList.length > 1) {
                                _this.stockList = result.data.stockList;
                                console.log(_this.stockList);
                                //选择批次
                                if (_this.IsShow == _this.Batch) {
                                    _this.modelNum = _this.countQty;
                                }
                                else {
                                    _this.modelNum = _this.ScanNumberLength;
                                }
                                _this.IsShow = _this.BatchList;
                            }
                            if (result.data.stockList.length == 0) {
                                _this.nativeService.showToast('提交成功');
                                _this.navCtrl.pop();
                            }
                            else {
                                //是否进行清除和页面跳转
                                _this.nativeService.showToast('提交成功');
                                _this.clearMode();
                            }
                        });
                        return [2 /*return*/];
                }
            });
        });
    };
    /**
    * 批次号切换
    */
    CyclecoutrantaskinfoPage.prototype.toggle = function () {
        if (this.stockItem != '新增批次号') {
            for (var index = 0; index < this.skuConfigArray.length; index++) {
                this.skuConfigArray[index].LotValue = this.stockItem['skuLot' + (index + 1)];
            }
        }
        else {
            for (var index = 0; index < this.skuConfigArray.length; index++) {
                this.skuConfigArray[index].LotValue = "";
                this.disabled = false;
            }
        }
    };
    /**
    * 序列号回车事件
    */
    CyclecoutrantaskinfoPage.prototype.serialNumber_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.serialNumberModel)) {
                var flag_1 = true;
                this.serialNumberList.forEach(function (element) {
                    if (element.toUpperCase() == _this.serialNumberModel.toUpperCase()) {
                        flag_1 = false;
                    }
                });
                if (flag_1) {
                    this.serialNumberList.push(this.serialNumberModel);
                    this.serialNumberModel = '';
                }
                this.ScanNumberLength = this.serialNumberList.length;
            }
        }
    };
    /**
    * 查看序列号
    */
    CyclecoutrantaskinfoPage.prototype.numclick = function () {
        this.IsShow = this.serialListNum;
    };
    /**
   * 删除扫描序列号
   * @param item
   */
    CyclecoutrantaskinfoPage.prototype.removeItem = function (item) {
        this.serialNumberList = this.serialNumberList.filter(function (x) { return x != item; });
        //更新长度
        this.ScanNumberLength = this.serialNumberList.length;
    };
    CyclecoutrantaskinfoPage.prototype.compareFn = function (e1, e2) {
        return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
    };
    CyclecoutrantaskinfoPage.prototype.togglePage = function () {
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetailVOItem) && __WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetailVOItem.convertQty)) {
            this.skuSpec = '1-' + this.skuPackageDetailVOItem.convertQty;
        }
        else {
            this.skuSpec = '';
        }
    };
    /**
     * 返回
     */
    CyclecoutrantaskinfoPage.prototype.exit = function () {
        if (this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].Batch) {
            this.navCtrl.pop();
        }
        else if (this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].serialNumber) {
            this.IsShow = this.Batch;
        }
        else if (this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].serialNumberListNum) {
            this.IsShow = this.serialNumber;
        }
        else if (this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].BatchList) {
            if (this.resultSkuItem.isSn == 0) {
                this.IsShow = this.Batch;
            }
            else {
                this.IsShow = this.serialNumber;
            }
        }
    };
    CyclecoutrantaskinfoPage.prototype.ionViewDidLoad = function () {
        // console.log('ionViewDidLoad CyclecoutrantaskinfoPage');
    };
    CyclecoutrantaskinfoPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-cyclecoutrantaskinfo',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cyclecounthome\cyclecountrantask\cyclecoutrantaskinfo\cyclecoutrantaskinfo.html"*/'<!--\n\n  Generated template for the CyclecoutrantaskinfoPage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title (click)="expression()">\n\n      执行盘点\n\n      <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n    </ion-title>\n\n    <ion-buttons end>\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>首页</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>盘点</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>执行盘点</li>\n\n      </ul> -->\n\n      <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n  <!-- 库房 -->\n\n  <!-- <div *ngIf="IsShow==Warehouse" class="item-warhouser">\n\n    <ion-item>\n\n      <ion-label>库 房：</ion-label>\n\n      <ion-select [(ngModel)]="warhouserItem" interface="action-sheet" cancelText="取消">\n\n        <div *ngFor="let Item of WarehouserModel">\n\n          <ion-option [value]="Item">{{Item.whName}}</ion-option>\n\n        </div>\n\n      </ion-select>\n\n    </ion-item>\n\n  </div> -->\n\n\n\n  <div *ngIf="IsShow!=Warehouse">\n\n    <div class="title-ms clearfix">\n\n      <div class="title-ms-info">\n\n        <div class="title-font-s1">\n\n          <label>{{locCode}}</label>\n\n          <span>{{whName}}</span>\n\n        </div>\n\n        <div>{{skuName}}</div>\n\n        <div class="title-font-small">\n\n          <div class="title-font-s2">\n\n            <label>物品规格：</label>\n\n            <label style="color: black;">{{skuSpec}}</label>\n\n          </div>\n\n          <!-- <div class="received">\n\n              物品数量:\n\n               <span style="color: #F59A23;">100</span>包\n\n            </div> -->\n\n        </div>\n\n      </div>\n\n    </div>\n\n    <!-- 批次 -->\n\n    <div class="list_item" *ngIf="IsShow==Batch">\n\n      <ion-item>\n\n        <ion-label>箱码扫描：</ion-label>\n\n        <ion-input (keyup)="scanModelChange($event)" [(ngModel)]="scanView" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item *ngIf="sysparms.paramValue==0">\n\n        <ion-label>容器：</ion-label>\n\n        <ion-input maxlength=\'30\' [(ngModel)]="lpnCode" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>物品：</ion-label>\n\n        <ion-input (keyup)="skuCode_KeyDown($event)" [(ngModel)]="skuCode" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label> 数量：</ion-label>\n\n        <ion-input type="number" [(ngModel)]="countQty"></ion-input>\n\n        <ion-select (ionChange)="togglePage()" [(ngModel)]="skuPackageDetailVOItem" [compareWith]="compareFn"\n\n          interface="action-sheet" cancelText="取消">\n\n          <div *ngFor="let skuPackage of skuPackageDetailVOList">\n\n            <ion-option [value]="skuPackage">{{skuPackage.wsuName}}</ion-option>\n\n          </div>\n\n        </ion-select>\n\n      </ion-item>\n\n\n\n    </div>\n\n    <!-- 序列号 -->\n\n    <div class="list_item" *ngIf="IsShow===serialNumber">\n\n      <ion-item>\n\n        <ion-label>库位：</ion-label>\n\n        <ion-input [(ngModel)]="locCode" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item *ngIf="sysparms.paramValue==0">\n\n        <ion-label>容器：</ion-label>\n\n        <ion-input [(ngModel)]="lpnCode" type="text"></ion-input>\n\n        <ion-avatar item-end (click)="setServerIp()">\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>序列号：</ion-label>\n\n        <ion-input (keyup)="serialNumber_KeyDown($event)" maxlength=\'50\' [(ngModel)]="serialNumberModel" type="text">\n\n        </ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>数量：</ion-label>\n\n        <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{ScanNumberLength}}\'>\n\n        </ion-input>\n\n        <!-- <span>0/20台</span> -->\n\n        <ion-avatar item-end (click)="numclick()">\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n    </div>\n\n    <!-- 序列号明细 -->\n\n    <div class="list_item_serial" *ngIf="IsShow===serialListNum">\n\n      <div class="list_item_title">\n\n        <span [ngStyle]="{\'border-bottom\':\'3px solid #008080\'}">已扫序列号(<b>{{ScanNumberLength}}</b>)</span>\n\n\n\n      </div>\n\n    </div>\n\n    <!-- 已扫描序列号 -->\n\n    <div *ngIf="IsShow==serialListNum ">\n\n      <ion-list class="list_item_div">\n\n        <ion-scroll scrollY="true" style="height:35rem;">\n\n          <ion-item-sliding *ngFor="let item of serialNumberList">\n\n            <ion-item>\n\n              <h2>{{item}}</h2>\n\n            </ion-item>\n\n            <ion-item-options side="right">\n\n              <button (click)="removeItem(item)" ion-button color="danger">\n\n                删除\n\n              </button>\n\n            </ion-item-options>\n\n          </ion-item-sliding>\n\n        </ion-scroll>\n\n      </ion-list>\n\n    </div>\n\n\n\n    <!-- 批次列表 -->\n\n    <div class="list_item" class="item-warhouser" *ngIf="IsShow===BatchList">\n\n      <ion-item>\n\n        <ion-label>批次号：</ion-label>\n\n        <ion-select (ionChange)="toggle()" [(ngModel)]="stockItem" interface="action-sheet" cancelText="取消">\n\n          <div *ngFor="let item of stockList">\n\n            <ion-option [value]="item">{{item.lotNumber}}</ion-option>\n\n          </div>\n\n          <ion-option [value]="">新增批次号</ion-option>\n\n        </ion-select>\n\n      </ion-item>\n\n      <div *ngFor="let item of skuConfigArray;let i = index">\n\n        <ion-item *ngIf="item.CtlLotEditVal!=\'Date\'; else elseBlock">\n\n          <ion-label>{{item.skuLotLabel}}：</ion-label>\n\n          <ion-input [disabled]=disabled type="text" [(ngModel)]="item.LotValue"></ion-input>\n\n          <ion-avatar item-end>\n\n            <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n          </ion-avatar>\n\n        </ion-item>\n\n        <ng-template #elseBlock>\n\n          <ion-item>\n\n            <ion-label>{{item.skuLotLabel}}：</ion-label>\n\n            <ion-datetime [(ngModel)]="item.LotValue" cancelText="取 消" doneText="确 定" min="1999-01-01" max="2039-12-31"\n\n              pickerFormat="YYYY-MM-DD" displayFormat="YYYY-MM-DD">\n\n            </ion-datetime>\n\n            <ion-avatar item-end>\n\n              <ion-icon class="iconfont iconrili"></ion-icon>\n\n            </ion-avatar>\n\n          </ion-item>\n\n        </ng-template>\n\n      </div>\n\n      <ion-item>\n\n        <ion-label>数量：</ion-label>\n\n        <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{modelNum}}\'>\n\n        </ion-input>\n\n      </ion-item>\n\n    </div>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      <!-- {{buttonEnt}}[Ent] -->\n\n      提交[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cyclecounthome\cyclecountrantask\cyclecoutrantaskinfo\cyclecoutrantaskinfo.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_3__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_5__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_7__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["a" /* BarcodeService */]])
    ], CyclecoutrantaskinfoPage);
    return CyclecoutrantaskinfoPage;
}());

//# sourceMappingURL=cyclecoutrantaskinfo.js.map

/***/ }),

/***/ 777:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CyclecoutrantaskinfoPageModule", function() { return CyclecoutrantaskinfoPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__cyclecoutrantaskinfo__ = __webpack_require__(1372);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var CyclecoutrantaskinfoPageModule = /** @class */ (function () {
    function CyclecoutrantaskinfoPageModule() {
    }
    CyclecoutrantaskinfoPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__cyclecoutrantaskinfo__["a" /* CyclecoutrantaskinfoPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__cyclecoutrantaskinfo__["a" /* CyclecoutrantaskinfoPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CyclecoutrantaskinfoPageModule);
    return CyclecoutrantaskinfoPageModule;
}());

//# sourceMappingURL=cyclecoutrantaskinfo.module.js.map

/***/ }),

/***/ 781:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CrumbsModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__crumbs__ = __webpack_require__(782);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var CrumbsModule = /** @class */ (function () {
    function CrumbsModule() {
    }
    CrumbsModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]],
            imports: [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]),
            ],
            exports: [__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]]
        })
    ], CrumbsModule);
    return CrumbsModule;
}());

//# sourceMappingURL=crumbs.module.js.map

/***/ }),

/***/ 782:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CrumbsComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var CrumbsComponent = /** @class */ (function () {
    function CrumbsComponent(navCtrl) {
        this.navCtrl = navCtrl;
        this.dicts = [
            { id: "HomePage", name: "首页" },
            { id: "TaskviewPage", name: "任务查看" },
            { id: "CheckinhomePage", name: "收货" },
            { id: "CheckinpiecePage", name: "按件收货" },
            { id: "CheckInPage", name: "执行收货" },
            { id: "CheckinboxPage", name: "按箱收货" },
            { id: "SearchOrderPage", name: "按单收货" },
            { id: "CheckinBoxInfoPage", name: "执行收货" },
            { id: "CheckintrayPage", name: "按托收货" },
            { id: "CheckinDetailedPage", name: "收货记录" },
            { id: "CollageTaskPage", name: "拼托任务" },
            { id: "CollageTaskListPage", name: "托盘详情" },
            { id: "CollageTaskBoxPage", name: "托盘详情" },
            { id: "CyclecounthomePage", name: "盘点" },
            { id: "CyclecountrandominfoPage", name: "随机盘点" },
            { id: "CyclecountrantaskPage", name: "盘点任务" },
            { id: "CyclecountBillPage", name: "静态盘点" },
            { id: "CyclecoutrantaskinfoPage", name: "执行盘点" },
            { id: "MovehomePage", name: "移动" },
            { id: "MovesboxPage", name: "按箱移动" },
            { id: "MovesboxInfoPage", name: "按箱移动" },
            { id: "moveSku", name: "物品移动" },
            { id: "moveStock", name: "托盘移动" },
            { id: "moveStocks", name: "多拖移动" },
            { id: "PackageTaskPage", name: "打包任务" },
            { id: "PackageTaskListPage", name: "打包任务" },
            { id: "PackageTaskInfoPage", name: "任务明细" },
            { id: "PutawayhomePage", name: "上架" },
            { id: "PutawayPage", name: "按托上架" },
            { id: "PutawayboxPage", name: "按箱上架" },
            { id: "ReplenishmenhomePage", name: "补货任务" },
            { id: "ReplenishmenDetilePage", name: "补货任务" },
            { id: "SettingPage", name: "个人设置" },
            { id: "SettingPwdPage", name: "修改密码" },
            { id: "SettingThemePage", name: "主题颜色" },
            { id: "ShipmenthomePage", name: "发运" },
            { id: "ShipmentLpnPage", name: "托盘发运" },
            { id: "StockhomePage", name: "库存" },
            { id: "StockdetailPage", name: "库存查询" },
            { id: "StockqueryPage", name: "库存查询" },
            { id: "StockPritPage", name: "库存打印" },
            { id: "TakeawayhomePage", name: "拣货" },
            { id: "TakeawaypiecePage", name: "按件拣货" },
            { id: "TakeawayPage", name: "执行拣货" },
            { id: "TakeawayinPage", name: "执行拣货" },
            { id: "TakeawayindetailedPage", name: "拣货记录" },
            { id: "TakeawaypieceboxPage", name: "按箱拣货" },
            { id: "TakeawayinboxPage", name: "执行拣货" },
            { id: "CougnyPage", name: "库内" },
            { id: "CopyLabelPage", name: "复制标签" },
            { id: "RandomCheckPage", name: "随机盘点" },
            { id: "RandomCheckTaskPage", name: "执行盘点" },
            { id: "ReplenishmenPage", name: "补货任务" },
            { id: "ReplenishmenRecordPage", name: "补货任务" },
            { id: "StockFrozenPage", name: "库存冻结" },
            { id: "CheckOrderPage", name: "执行收货" },
            { id: "UpdateDetailPage", name: "修改明细" },
            { id: "TaskwayMultPage", name: "批量拣货" },
            { id: "BindPrintterPage", name: "绑定打印机" },
            { id: "BindOrderPage", name: "单据绑定" },
            { id: "BindLpnPage", name: "绑定容器" },
            { id: "CyclecountRecordPage", name: "盘点记录" },
            { id: "MergeTrayPage", name: "拼托" }
        ];
        this.alls = [];
    }
    CrumbsComponent.prototype.ngOnInit = function () {
        var _this = this;
        if (this.navCtrl.getViews().length > 0) {
            this.navCtrl.getViews().forEach(function (a) {
                _this.alls.push(_this.distsValue(a));
                _this.alls.push({ id: '_', name: '_' });
            });
        }
        if (this.alls.length > 0 && this.alls[this.alls.length - 1].id == '_') {
            this.alls.pop();
        }
        console.log(this.alls);
    };
    CrumbsComponent.prototype.toPush = function (item) {
        var active = this.navCtrl.getActive();
        if (active) {
            if (active.id != item.id) {
                var a = this.navCtrl.getByIndex(item.index);
                this.navCtrl.popTo(a);
            }
        }
    };
    CrumbsComponent.prototype.distsValue = function (vc) {
        var value;
        this.dicts.forEach(function (a) {
            if (a.id == vc.name) {
                a["index"] = vc.index;
                value = a;
            }
        });
        return value;
    };
    CrumbsComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'crumbs',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\crumbs\crumbs.html"*/'<ul >\n\n   <ng-container *ngFor="let item of alls">\n\n      <li *ngIf="item&&item.id!=\'_\';else elseBlock" (click)="toPush(item)"> \n\n         {{item.name}} \n\n      </li>\n\n      <ng-template #elseBlock>\n\n      <li>\n\n         <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      </ng-template>\n\n   </ng-container>\n\n</ul>\n\n'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\crumbs\crumbs.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */]])
    ], CrumbsComponent);
    return CrumbsComponent;
}());

//# sourceMappingURL=crumbs.js.map

/***/ }),

/***/ 825:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return SkuLotValResultModel; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SkuLot; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__utils_appConstant__ = __webpack_require__(65);


var SkuLotValResultModel = /** @class */ (function () {
    function SkuLotValResultModel() {
        this.getLotEditVal();
    }
    SkuLotValResultModel.prototype.getLotEditVal = function () {
        if (__WEBPACK_IMPORTED_MODULE_0__services_Utils__["a" /* Utils */].isNotEmpty(this.skuLotMixMask)) {
            if (this.skuLotMixMask.indexOf('YYYY') != -1 || this.skuLotMixMask.indexOf('yyyy') != -1) {
                return this.CtlLotEditVal = __WEBPACK_IMPORTED_MODULE_1__utils_appConstant__["c" /* AppConstant */].Date;
            }
            else {
                return this.CtlLotEditVal = __WEBPACK_IMPORTED_MODULE_1__utils_appConstant__["c" /* AppConstant */].Text;
            }
        }
    };
    return SkuLotValResultModel;
}());

var SkuLot = /** @class */ (function () {
    function SkuLot() {
    }
    return SkuLot;
}());

//# sourceMappingURL=SkuLotValResultModel.js.map

/***/ })

});
//# sourceMappingURL=14.js.map