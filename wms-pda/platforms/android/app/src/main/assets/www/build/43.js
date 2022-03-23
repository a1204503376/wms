webpackJsonp([43],{

/***/ 1373:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return moveSku; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__ = __webpack_require__(360);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};









/**
 * Generated class for the moveSku page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var moveSku = /** @class */ (function () {
    function moveSku(navCtrl, navParams, appService, nativeService, modalCtrl, storage, ngZone, events, barcodeService) {
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
        //界面标识
        this.IsShow = 1; //显隐标识
        // public Iswarehouse: number = 0;//仓库显示
        this.IsSku = 1; //批次物品显示
        this.IsSkuMove = 2; //物品移动显示
        this.IsSkuBathList = 3; //批次号显示
        this.IsSkuSerial = 4; //序列号物品显示
        this.IsSkuSerialInfo = 5; //序列号物品明细显示
        this.NumberListFalag = 1; //扫描标识 1：已扫描 2:已收货
        this.sourceLpnCode = ''; //原容器编码
        this.sourceLocCode = ''; //原库位编码
        this.InNumberLength = 0; //已收序列号数量
        this.ScanNumberLength = 0; //已扫描序列号数量
        this.serialNumber = []; //序列号已扫描集合
        this.isTilebool = true;
        this.sysparms = []; //系统参数
        //获取库房列表 2020-3-31去掉
        // this.nativeService.showLoading('正在获取库房...');
        // this.appService.httpRequest(Api.api + Api.ApiPDA, Method.Post, '', ContenType.Form, result => {
        //     this.nativeService.hideLoading();
        //     if (Utils.isNotEmpty(result.data)) {
        //         this.WarehouserModel = result.data;
        //     }
        // });
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    //更改库房状态
    moveSku.prototype.toggle = function () {
        //this.whName = this.warhouserItem.whName;
    };
    moveSku.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad moveSku');
    };
    moveSku.prototype.clearModel = function () {
        this.IsShow = this.IsSku;
        this.wsuName = '';
        this.sourceLpnCode = ''; //原容器编码
        this.sourceLocCode = ''; //原库位编码
        this.skuCode = ''; //物品编码
        this.resultSkuItem = null; //物品详情
        this.moveQty = ''; //填写数量
        this.skuName = ''; //物品名称
        this.skuSpec = ''; //物品规格
        this.stockQty = null; //物品库存数量
        this.packageDetails = null; //包装明细
        this.defaultPackageDetail = null; //默认包装
        this.skuStockLotModel = null; //物品库存信息
        this.targetLpnCode = ''; //目标容器
        this.targetLocCode = ''; //目标容器
        this.serialNumber = [];
        this.stockQtyName = '';
    };
    moveSku.prototype.ionViewWillEnter = function () {
        var _this = this;
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].SKU) {
                _this.ngZone.run(function () {
                    _this.skuCode = barcode;
                    _this.scanWmsSku();
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].TuoPan) {
                _this.ngZone.run(function () {
                    if (_this.IsShow == _this.IsSku) {
                        //原容器
                        _this.sourceLpnCode = barcode;
                        _this.getLocCode();
                    }
                    else {
                        //目标容器
                        _this.targetLpnCode = barcode;
                    }
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    if (_this.IsShow == _this.IsSku) {
                        //原库位
                        _this.sourceLocCode = barcode;
                    }
                    else {
                        //目标库位
                        _this.targetLocCode = barcode;
                    }
                });
            }
        });
    };
    moveSku.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
    * 原容器回车事件
    */
    moveSku.prototype.sourceLpnCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            this.getLocCode();
        }
    };
    /**
     * 根据容器获取库位
     */
    moveSku.prototype.getLocCode = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.sourceLpnCode)) {
            this.nativeService.showToast('容器编码不能为空');
            return;
        }
        var params = {
            sourceLpnCode: this.sourceLpnCode
        };
        //获取库位编码
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getLocCode, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, '', function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                //获取库位
                _this.sourceLocCode = result.data.sourceLocCode;
            }
        });
    };
    /**
    * sku回车事件
    */
    moveSku.prototype.skuCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            this.scanWmsSku();
        }
    };
    /**
    * 序列号回车事件
    */
    moveSku.prototype.SkuSerial_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.serialNumberMode)) {
                var flag_1 = true;
                this.serialNumber.forEach(function (element) {
                    if (element.toUpperCase() == _this.serialNumberMode.toUpperCase()) {
                        flag_1 = false;
                    }
                });
                // if (Utils.isNotEmpty(this.Sn)) {
                //     this.Sn.forEach(element => {
                //         if (element.snDetailCode == this.serialNumberMode) {
                //             flag = false;
                //         }
                //     });
                // }
                var body = {
                    lpnCode: this.sourceLpnCode,
                    skuId: this.resultSkuItem.skuId,
                    serialNumber: this.serialNumberMode
                };
                this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockIsHasSerial, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, body, '', function (result) {
                    if (flag_1) {
                        _this.serialNumber.push(_this.serialNumberMode);
                        _this.serialNumberMode = '';
                    }
                    _this.ScanNumberLength = _this.serialNumber.length;
                });
            }
            ;
        }
    };
    moveSku.prototype.isTitle = function (event) {
        this.isTilebool = event;
        if (event) {
            this.NumberListFalag = 1;
        }
        else {
            this.NumberListFalag = 2;
        }
    };
    /**
    * 查看序列号
    */
    moveSku.prototype.numclick = function () {
        this.IsShow = this.IsSkuSerialInfo;
    };
    /*物品扫描*/
    moveSku.prototype.scanWmsSku = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.skuCode)) {
            this.nativeService.showToast('物品编码不能为空');
        }
        var params = {
            skuCode: this.skuCode
        };
        //获取物品
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].SkuList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            // this.clearModel();
            _this.serialNumber = [];
            _this.ScanNumberLength = 0;
            //判断是否多个物品
            console.log(result);
            if (result.data.length > 1) {
                //选择物品
                _this.openAsnRecordModal(result.data);
            }
            else if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('没有查询到物品');
            }
            else {
                _this.resultSkuItem = result.data[0];
                _this.getSkuInfo();
            }
        });
    };
    /**
     *查看物品明细
    */
    moveSku.prototype.openAsnRecordModal = function (skuList) {
        var _this = this;
        var myModal = this.modalCtrl.create('SkuListModal', {
            skuList: skuList,
        });
        myModal.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(data)) {
                _this.resultSkuItem = data;
                _this.getSkuInfo();
            }
        });
        myModal.present();
    };
    /**
     * 获取物品库存接口
     */
    moveSku.prototype.getSkuInfo = function () {
        var _this = this;
        var skuId = '';
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.resultSkuItem)) {
            this.skuName = this.resultSkuItem.skuName;
            skuId = this.resultSkuItem.skuId;
        }
        var params = {
            skuId: skuId,
            sourceLpnCode: this.sourceLpnCode,
            sourceLocCode: this.sourceLocCode
        };
        this.IsSn = this.resultSkuItem.isSn;
        //判断是否为序列号
        if (this.resultSkuItem.isSn == 1) {
            this.IsShow = this.IsSkuSerial;
        }
        else {
            this.IsShow = this.IsSku;
        }
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getSkuInfo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, '', function (result) {
            var resultData1 = result.data;
            // this.skuName = resultData1.skuName;
            _this.stockQtyName = resultData1.stockQtyName;
            _this.skuSpec = resultData1.skuSpec;
            _this.stockQty = Number(resultData1.stockQty);
            _this.packageDetails = resultData1.packageDetails;
            _this.defaultPackageDetail = resultData1.defaultPackageDetail; //默认包装单位
            _this.defaultPackageDetailTemp = resultData1.defaultPackageDetail; //临时存放计量单位
            _this.defaultPackageDetailPermanent = resultData1.defaultPackageDetail; //默认不变计量单位(用于切换小数点丢失)
            _this.stockQtyPermanent = _this.stockQty;
            //获取物品已扫描序列号
            //this.Sn=resultData1;
            //this.wsuName = this.defaultPackageDetail.wsuName;
        });
        //构造测试数据
        // let resultData1 = {
        //     skuName: '测试物品名称',
        //     skuSpec: '1-10-001',
        //     stockQty: '500',
        //     packageDetails: [
        //         { wspdId: '1', wsuName: '个', wsuCode: '001', convertQty: 1, skuLevel: 1 },
        //         { wspdId: '2', wsuName: '包', wsuCode: '000', convertQty: 10, skuLevel: 2 },
        //         { wspdId: '3', wsuName: '箱', wsuCode: '000', convertQty: 100, skuLevel: 3 }
        //     ],
        //     defaultPackageDetail: { wspdId: '1', wsuName: '个', wsuCode: '001', skuLevel: '1', convertQty: '10' }
        // }
        // this.skuName = resultData1.skuName;
        // this.skuSpec = resultData1.skuSpec;
        // this.stockQty = Number(resultData1.stockQty);
        // this.packageDetails = resultData1.packageDetails;
        // this.defaultPackageDetail = resultData1.defaultPackageDetail;//默认包装单位
        // this.defaultPackageDetailTemp = resultData1.defaultPackageDetail;//临时存放计量单位
        // this.defaultPackageDetailPermanent = resultData1.defaultPackageDetail;//默认不变计量单位(用于切换小数点丢失)
        // this.stockQtyPermanent = this.stockQty;
        // this.wsuName = this.defaultPackageDetail.wsuName;
    };
    /**
     * 切换包装
     */
    moveSku.prototype.togglePage = function () {
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.defaultPackageDetail.convertQty)) {
            this.skuSpec = '1-' + this.defaultPackageDetail.convertQty;
        }
        else {
            this.skuSpec = '';
        }
        this.scanQtyNameChange();
    };
    /**
 * 已接收单位
 */
    moveSku.prototype.scanQtyNameChange = function () {
        var _this = this;
        if (this.defaultPackageDetail.wspdId == 0 || this.defaultPackageDetail == 0 || this.defaultPackageDetail.scanQtyName == 0) {
            return;
        }
        var result = "";
        var mainWsuName = "";
        var scanQtynum = this.stockQty;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.defaultPackageDetail)) {
            this.packageDetails = this.packageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.packageDetails)) {
            this.packageDetails.forEach(function (element) {
                if (element.skuLevel == _this.defaultPackageDetail.skuLevel) {
                    mainWsuName = element.wsuName;
                }
                if (element.skuLevel <= _this.defaultPackageDetail.skuLevel) {
                    var tmp = Math.floor(scanQtynum / element.convertQty);
                    scanQtynum = scanQtynum - element.convertQty * tmp;
                    if (tmp != 0) {
                        result += tmp + element.wsuName;
                    }
                }
            });
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.stockQtyName = 0 + mainWsuName;
        }
        else {
            this.stockQtyName = result;
        }
    };
    /**
     * 提交按钮
     */
    moveSku.prototype.btnOk = function () {
        if (this.IsShow == this.IsSku || this.IsShow == this.IsSkuSerial) {
            this.submitSku();
        }
        else if (this.IsShow == this.IsSkuMove) {
            this.submitMoveStockSku();
        }
        else {
            //批次提交
            this.submitLotNumberSku();
            // this.IsShow = this.IsSkuMove;
        }
    };
    /**
     * 批次提交验证数量
     */
    moveSku.prototype.submitLotNumberSku = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.sourceLpnCode)) {
            this.sourceLpnCode = '';
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.sourceLocCode)) {
            this.sourceLocCode = '';
        }
        var params = {
            //whId: this.warhouserItem.whId,
            skuId: this.resultSkuItem.skuId,
            sourceLpnCode: this.sourceLpnCode,
            sourceLocCode: this.sourceLocCode,
            wspdId: this.defaultPackageDetail.wspdId,
            moveQty: this.moveQty,
            lotNumber: this.skuStockLotModel.lotNumber
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitLotNumberSku, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.IsShow = _this.IsSkuMove;
            }
        });
    };
    /**
     * 物品移动提交
     */
    moveSku.prototype.submitMoveStockSku = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.skuStockLotModel)) {
            this.nativeService.showToast('库存信息不存在');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.targetLocCode)) {
            this.nativeService.showToast('请输入目标库位');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.targetLpnCode)) {
            this.nativeService.showToast('请输入目标容器');
            return;
        }
        var params = {
            // whId: this.warhouserItem.whId,
            skuId: this.resultSkuItem.skuId,
            sourceLpnCode: this.sourceLpnCode,
            sourceLocCode: this.sourceLocCode,
            //wspdId: this.defaultPackageDetail.wspdId,
            //moveQty: this.moveQty,
            lotNumber: this.skuStockLotModel.lotNumber,
            targetLocCode: this.targetLocCode,
            targetLpnCode: this.targetLpnCode,
        };
        //判断序列号物品
        if (this.resultSkuItem.isSn == 1) {
            if (this.ScanNumberLength == 0) {
                this.nativeService.showToast('请扫描序列号');
                return;
            }
            params['moveQty'] = this.ScanNumberLength; //序列号长度
            // params['序列号'] = this.serialNumber;//序列号实体
            params['wspdId'] = this.defaultPackageDetail.wspdId;
            params['serialList'] = this.serialNumber;
        }
        else {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.moveQty)) {
                this.nativeService.showToast('请填写转移数量');
                return;
            }
            if (!__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].IsPositiveInt(this.moveQty)) {
                this.nativeService.showToast('转移数量必须为正整数');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.defaultPackageDetail)) {
                this.nativeService.showToast('请选择计量单位');
                return;
            }
            params['moveQty'] = this.moveQty;
            params['wspdId'] = this.defaultPackageDetail.wspdId;
        }
        //物品转移提交
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitMoveStockSku, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('提交成功');
                //清楚数据
                _this.clearModel();
            }
        });
    };
    /**
     * 获取库存批次号
     */
    moveSku.prototype.submitSku = function () {
        var _this = this;
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.sourceLpnCode)) {
                this.nativeService.showToast('原容器编码不能为空');
                return;
            }
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.sourceLocCode)) {
            this.nativeService.showToast('原库位编码不能为空');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.resultSkuItem)) {
            this.nativeService.showToast('没有查询到物品信息');
            return;
        }
        var params = {
            skuId: this.resultSkuItem.skuId,
            sourceLpnCode: this.sourceLpnCode,
            sourceLocCode: this.sourceLocCode,
        };
        //判断序列号物品
        if (this.resultSkuItem.isSn == 1) {
            if (this.ScanNumberLength == 0) {
                this.nativeService.showToast('请扫描序列号');
                return;
            }
            params['moveQty'] = this.ScanNumberLength;
            params['serialList'] = this.serialNumber;
            params['wspdId'] = this.defaultPackageDetail.wspdId;
            this.moveQty = this.ScanNumberLength.toString();
        }
        else {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.moveQty)) {
                this.nativeService.showToast('请填写转移数量');
                return;
            }
            if (!__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].IsPositiveInt(this.moveQty)) {
                this.nativeService.showToast('转移数量必须为正整数');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.defaultPackageDetail)) {
                this.nativeService.showToast('请选择计量单位');
                return;
            }
            params['moveQty'] = this.moveQty;
            params['wspdId'] = this.defaultPackageDetail.wspdId;
        }
        this.nativeService.showLoading();
        //提交物品获取库存批次数量
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitSku, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            var skuStockLotMoveVOs = result.data.skuStockLotMoveVOs;
            //判断是否为多个
            if (skuStockLotMoveVOs.length > 1) {
                //多个跳转到批次列表暂无
                _this.skuStockLotMoveVOs = skuStockLotMoveVOs;
                //默认选中库存批次
                _this.skuStockLotModel = _this.skuStockLotMoveVOs[0];
                _this.IsShow = _this.IsSkuBathList;
            }
            else if (skuStockLotMoveVOs.length < 1) {
                _this.nativeService.showToast('当前库房没有查询到该物品');
            }
            else {
                _this.skuStockLotModel = skuStockLotMoveVOs[0];
                _this.IsShow = _this.IsSkuMove;
            }
        });
        // let skuStockLotMoveVOs = [
        //     {
        //         stockQty: '10', lotNumber: '05500555', ownerName: '徐吉岩',
        //         skuLotList: [{ skuLotIndex: '1', skuLotValue: '测试属性值1', skuLotLabel: '时间', skuLotEditType: 'yyyy' }, { skuLotIndex: '2', skuLotValue: '测试属性2', skuLotLabel: '供编', skuLotEditType: '' }],
        //         packageDetails: [{ wspdId: '1', wsuName: '个', wsuCode: '001' }, { wspdId: '2', wsuName: '包', wsuCode: '000' }],
        //         defaultPackageDetail: { wspdId: '1', wsuName: '个', wsuCode: '001', skuLevel: '1', convertQty: '10' }
        //     },
        //     {
        //         stockQty: '20', lotNumber: '055005558', ownerName: '徐吉岩2号',
        //         skuLotList: [{ skuLotIndex: '1', skuLotValue: '测试属性值1', skuLotLabel: '入库', skuLotEditType: '' }, { skuLotIndex: '2', skuLotValue: '测试属性2', skuLotLabel: '出库', skuLotEditType: '' }],
        //         packageDetails: [{ wspdId: '1', wsuName: '个', wsuCode: '001' }, { wspdId: '2', wsuName: '包', wsuCode: '000' }],
        //         defaultPackageDetail: { wspdId: '1', wsuName: '个', wsuCode: '001', skuLevel: '1', convertQty: '10' }
        //     }
        // ];
        //判断是否为多个
        // if (skuStockLotMoveVOs.length > 1) {
        //     //多个跳转到批次列表暂无
        //     this.skuStockLotMoveVOs = skuStockLotMoveVOs;
        //     //默认选中库存批次
        //     this.skuStockLotModel = this.skuStockLotMoveVOs[0];
        //     this.IsShow = this.IsSkuBathList;
        // } else if (skuStockLotMoveVOs.length < 1) {
        //     this.nativeService.showToast('当前库房没有查询到该物品');
        // } else {
        //     this.skuStockLotModel = skuStockLotMoveVOs[0];
        //     this.IsShow = this.IsSkuMove;
        // }
    };
    /**
     * 返回按钮
     */
    moveSku.prototype.exit = function () {
        if (this.IsShow == this.IsSku) {
            this.navCtrl.pop();
        }
        else if (this.IsShow == this.IsSkuSerial) {
            this.IsShow = this.IsSku;
        }
        else if (this.IsShow == this.IsSkuSerialInfo) {
            this.IsShow = this.IsSkuSerial;
        }
        else if (this.IsShow == this.IsSkuMove) {
            this.IsShow = this.IsSku; //留个坑，未来加一个是否序列号的判断
            if (this.IsSn == '1') {
                this.IsShow = this.IsSkuSerial;
            }
            else {
                this.IsShow = this.IsSku;
            }
        }
        else if (this.IsShow == this.IsSkuBathList) {
            this.IsShow = this.IsSku;
        }
    };
    moveSku.prototype.compareFn = function (e1, e2) {
        return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
    };
    /**
    * 删除扫描序列号
    * @param item
    */
    moveSku.prototype.removeItem = function (item) {
        this.serialNumber = this.serialNumber.filter(function (x) { return x != item; });
        //更新长度
        this.ScanNumberLength = this.serialNumber.length;
    };
    moveSku = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-movesku',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesku\movesku.html"*/'<ion-header>\n\n    <ion-navbar color="wmscolor">\n\n        <ion-title>\n\n            物品移动\n\n            <!-- <div class="title_f_name">(欣天新)</div> -->\n\n        </ion-title>\n\n        <!-- <ion-buttons end (click)="detailed()">\n\n            <button ion-button icon-only>\n\n                <ion-icon name="menu"></ion-icon>\n\n            </button>\n\n        </ion-buttons> -->\n\n    </ion-navbar>\n\n    <div class="title-bt">\n\n        <div class="nav-tile">\n\n            <!-- <ul>\n\n                <li>首页</li>\n\n                <li>\n\n                    <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n                </li>\n\n                <li>移动</li>\n\n                <li>\n\n                    <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n                </li>\n\n                <li>物品移动</li>\n\n            </ul> -->\n\n            <crumbs></crumbs>\n\n            <div class="order-info">{{whName}}</div>\n\n        </div>\n\n    </div>\n\n</ion-header>\n\n<ion-content>\n\n    <div *ngIf="IsShow===Iswarehouse">\n\n        <div class="list_item">\n\n            <ion-item>\n\n                <ion-label>库 房：</ion-label>\n\n                <ion-select (ionChange)="toggle()" [(ngModel)]="warhouserItem" interface="action-sheet" cancelText="取消">\n\n                    <div *ngFor="let Item of WarehouserModel">\n\n                        <ion-option [value]="Item">{{Item.whName}}</ion-option>\n\n                    </div>\n\n                </ion-select>\n\n            </ion-item>\n\n        </div>\n\n    </div>\n\n    <div\n\n        *ngIf="IsShow===IsSkuBathList||IsShow===IsSkuMove||IsShow===IsSku||IsShow===IsSkuSerial||IsShow===IsSkuSerialInfo">\n\n        <!-- 物品选显卡 -->\n\n        <div class="title-ms clearfix">\n\n            <div class="title-ms-info clearfix">\n\n                <ul>\n\n                    <li class="l-width2">\n\n                        <span class="s-label">物&emsp;品：</span>\n\n                        <span class="s-text">{{skuName}}</span>\n\n                    </li>\n\n                    <li class="l-width1">\n\n                        <span class="s-label">原库位：</span>\n\n                        <span class="s-text">{{sourceLocCode}}</span>\n\n                    </li>\n\n                    <li class="l-width1">\n\n                        <span class="s-label">规格：</span>\n\n                        <span class="s-text">{{skuSpec}}</span>\n\n                    </li>\n\n                    <li class="l-width1" *ngIf="sysparms.paramValue==0">\n\n                        <span class="s-label">原容器：</span>\n\n                        <span class="s-text">{{sourceLpnCode}}</span>\n\n                    </li>\n\n                    <li class="l-width1">\n\n                        <div *ngIf="IsShow!=IsSkuMove; else elseBlock">\n\n                            <span class="s-label">数量：</span>\n\n                            <span class="s-text">{{stockQtyName}}</span>\n\n                        </div>\n\n                        <ng-template #elseBlock>\n\n                            <span class="s-label">转移数量：</span>\n\n                            <span class="s-text">{{moveQty}} {{wsuName}}</span>\n\n                        </ng-template>\n\n                    </li>\n\n                </ul>\n\n            </div>\n\n        </div>\n\n    </div>\n\n\n\n    <!-- 批次物品 -->\n\n    <div *ngIf="IsShow==IsSku">\n\n        <ion-item>\n\n            <ion-label>原库位：</ion-label>\n\n            <ion-input type="text" [(ngModel)]="sourceLocCode"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item *ngIf="sysparms.paramValue==0">\n\n            <ion-label>原容器：</ion-label>\n\n            <ion-input type="text" [(ngModel)]="sourceLpnCode" (keyup)="sourceLpnCode_KeyDown($event)"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item>\n\n            <ion-label>物品：</ion-label>\n\n            <ion-input type="text" (keyup)="skuCode_KeyDown($event)" [(ngModel)]="skuCode"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item>\n\n            <ion-label>数量：</ion-label>\n\n            <ion-input type="number" [(ngModel)]="moveQty"></ion-input>\n\n            <ion-select [(ngModel)]="defaultPackageDetail" (ionChange)="togglePage()" [compareWith]="compareFn"\n\n                interface="action-sheet" cancelText="取消">\n\n                <div *ngFor="let package of packageDetails">\n\n                    <ion-option [value]="package">{{package.wsuName}}</ion-option>\n\n                </div>\n\n            </ion-select>\n\n        </ion-item>\n\n    </div>\n\n    <!-- 序列号物品 -->\n\n    <div *ngIf="IsShow==IsSkuSerial">\n\n        <ion-item>\n\n            <ion-label>原库位：</ion-label>\n\n            <ion-input type="text" [(ngModel)]="sourceLocCode"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item *ngIf="sysparms.paramValue==0">\n\n            <ion-label>原容器：</ion-label>\n\n            <ion-input type="text" [(ngModel)]="sourceLpnCode" (keyup)="sourceLpnCode_KeyDown($event)"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item>\n\n            <ion-label>序列号：</ion-label>\n\n            <ion-input type="text" maxlength=\'100\' (keyup)="SkuSerial_KeyDown($event)" [(ngModel)]="serialNumberMode">\n\n            </ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item (click)="numclick()">\n\n            <ion-label>数量：</ion-label>\n\n            <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{ScanNumberLength}}\'>\n\n            </ion-input>\n\n            <span>0/20台</span>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n    </div>\n\n\n\n    <!-- 序列号明细 -->\n\n    <div class="list_item_serial" *ngIf="IsShow===IsSkuSerialInfo">\n\n        <div class="list_item_title">\n\n            <span [ngStyle]="{\'border-bottom\':isTilebool === true ? \'3px solid #008080\' : \'0\' }"\n\n                (click)="isTitle(true)">已扫序列号(<b>{{ScanNumberLength}}</b>)</span>\n\n            <!-- <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n                (click)="isTitle(false)">已收序列号(<b>{{InNumberLength}}</b>)</span> -->\n\n        </div>\n\n\n\n    </div>\n\n\n\n    <!-- 已扫描序列号 -->\n\n    <div *ngIf="NumberListFalag==1 && IsShow===IsSkuSerialInfo ">\n\n        <ion-list class="list_item_div">\n\n            <ion-scroll scrollY="true" style="height:35rem;">\n\n                <ion-item-sliding *ngFor="let item of serialNumber">\n\n                    <ion-item>\n\n                        <h2>{{item}}</h2>\n\n                    </ion-item>\n\n                    <ion-item-options side="right">\n\n                        <button (click)="removeItem(item)" ion-button color="danger">\n\n                            删除\n\n                        </button>\n\n                    </ion-item-options>\n\n                </ion-item-sliding>\n\n            </ion-scroll>\n\n        </ion-list>\n\n    </div>\n\n\n\n    <!-- 已收序列号 -->\n\n    <!-- <div *ngIf="NumberListFalag==2 && IsShow===IsSkuSerialInfo ">\n\n        <ion-list class="list_item_div">\n\n            <ion-scroll scrollY="true" style="height:12rem;">\n\n                <ion-item-sliding *ngFor="let item of Sn">\n\n                    <ion-item>\n\n                        <h2>{{item.snDetailCode}}</h2>\n\n                    </ion-item>\n\n                </ion-item-sliding>\n\n            </ion-scroll>\n\n        </ion-list>\n\n    </div> -->\n\n\n\n\n\n    <!-- 转移至 -->\n\n    <div class="list_item" *ngIf="IsShow==IsSkuMove">\n\n        <ion-item>\n\n            <ion-label>目标库位：</ion-label>\n\n            <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item>\n\n            <ion-label>目标容器：</ion-label>\n\n            <ion-input [(ngModel)]="targetLpnCode" type="text"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n    </div>\n\n    <!-- 批属性界面 -->\n\n    <div *ngIf="IsShow==IsSkuBathList">\n\n        <ion-item class="lot_number">\n\n            <ion-label>批次号：</ion-label>\n\n            <ion-select [(ngModel)]="skuStockLotModel" interface="action-sheet" cancelText="取消">\n\n                <div *ngFor="let item of skuStockLotMoveVOs">\n\n                    <ion-option [value]="item">{{item.lotNumber}}</ion-option>\n\n                </div>\n\n            </ion-select>\n\n        </ion-item>\n\n        <div *ngFor="let item of skuStockLotModel.skuLotList;let i = index">\n\n            <ion-item *ngIf="item.skuLotDisp===1">\n\n                <ion-label>{{item.skuLotLabel}}：</ion-label>\n\n                <ion-input [disabled]=\'true\' type="text" [(ngModel)]="item.skuLotValue"></ion-input>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n        </div>\n\n        <ion-item *ngIf="IsSn==0;else elseBlock">\n\n            <ion-label>数量：</ion-label>\n\n            <ion-input type="number" [(ngModel)]="moveQty"></ion-input>\n\n            <ion-select [(ngModel)]="defaultPackageDetail" (ionChange)="togglePage()" [compareWith]="compareFn"\n\n                interface="action-sheet" cancelText="取消" okText="确定">\n\n                <div *ngFor="let package of packageDetails">\n\n                    <ion-option [value]="package">{{package.wsuName}}</ion-option>\n\n                </div>\n\n            </ion-select>\n\n        </ion-item>\n\n        <ng-template #elseBlock>\n\n            <ion-item>\n\n                <ion-label>数量：</ion-label>\n\n                <ion-input [disabled]=\'true\' type="text" readonly="readonly"\n\n                    value=\'{{ScanNumberLength + " /" +InNumberLength}}\'>\n\n                </ion-input>\n\n                <span>0/20台</span>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n        </ng-template>\n\n    </div>\n\n</ion-content>\n\n<ion-footer>\n\n    <div class="btn-box">\n\n        <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n            返回[Esc]\n\n        </button>\n\n        <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n            提交[Ent]\n\n        </button>\n\n    </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesku\movesku.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["a" /* BarcodeService */]])
    ], moveSku);
    return moveSku;
}());

//# sourceMappingURL=movesku.js.map

/***/ }),

/***/ 778:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "moveSkuModule", function() { return moveSkuModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__movesku__ = __webpack_require__(1373);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var moveSkuModule = /** @class */ (function () {
    function moveSkuModule() {
    }
    moveSkuModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__movesku__["a" /* moveSku */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__movesku__["a" /* moveSku */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], moveSkuModule);
    return moveSkuModule;
}());

//# sourceMappingURL=movesku.module.js.map

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

/***/ })

});
//# sourceMappingURL=43.js.map