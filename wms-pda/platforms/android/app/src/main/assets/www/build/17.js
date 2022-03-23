webpackJsonp([17],{

/***/ 1369:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CheckInPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__ = __webpack_require__(825);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__ = __webpack_require__(360);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__services_BarCodeService__ = __webpack_require__(359);
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
 * Generated class for the CheckInPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CheckInPage = /** @class */ (function () {
    function CheckInPage(navCtrl, navParams, appService, nativeService, storage, events, barcodeService, ngZone) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.storage = storage;
        this.events = events;
        this.barcodeService = barcodeService;
        this.ngZone = ngZone;
        this.recList = 1;
        this.isSn = 0; //是否为序列号管理 1,序列号管理  0,,非序列号管理
        this.isMaxLength = false;
        this.serialNumber = []; //序列号已扫描集合
        this.InNumberLength = 0; //已收序列号数量
        this.ScanNumberLength = 0; //已扫描序列号数量
        this.isTilebool = true;
        this.NumberListFalag = 1; //扫描标识 1：已扫描 2:已收货
        this.skuPackageDetailsItem = {}; //包装管理选中
        this.skuConfig = []; //批属性明细
        this.locCode = 'STAGE'; //库位编码
        this.lotArray = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["a" /* SkuLot */]();
        //批属性暂定不动态
        // public skuConfig1: SkuLotValResultModel;
        // public skuConfig2: SkuLotValResultModel;
        this.sysparms = []; //系统参数
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.navParams.get('result'))) {
            this.result = this.navParams.get('result');
            this.dataFilling();
        }
        ;
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    CheckInPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_9__services_BarCodeService__["b" /* BarcodeType */].SKU) {
                _this.ngZone.run(function () {
                    _this.skuCode = barcode;
                    _this.scanWmsSku(barcode);
                });
            }
            else if (bt == __WEBPACK_IMPORTED_MODULE_9__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.locCode = barcode;
                });
            }
            else if (bt == __WEBPACK_IMPORTED_MODULE_9__services_BarCodeService__["b" /* BarcodeType */].TuoPan) {
                _this.ngZone.run(function () {
                    _this.lpnCode = barcode; //容器
                });
            }
        });
    };
    CheckInPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    CheckInPage.prototype.dataFilling = function () {
        this.asnBillNo = this.result.asnBillNo;
        this.sname = this.result.sname;
        this.planCountQty = this.result.asnDetailMinVO.planCountQty;
        this.scanCountQty = this.result.asnDetailMinVO.scanCountQty;
        this.finish = this.result.asnDetailMinVO.finish;
        this.count = this.result.asnDetailMinVO.count;
    };
    /**
     * 通过任务获取订单详情
     */
    CheckInPage.prototype.clearModel = function () {
        // this.skuConfig1 = null;
        this.lotArray = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["a" /* SkuLot */]();
        this.skuConfig = null;
        this.recList = 1;
        this.isSn = 0; //是否为序列号管理 1,序列号管理  0,,非序列号管理
        this.isMaxLength = false;
        this.serialNumber = []; //序列号已扫描集合
        this.Sn = null; //序列号集合
        this.serialNumberMode = ''; //序列号实体
        this.InNumberLength = 0; //已收序列号数量
        this.ScanNumberLength = 0; //已扫描序列号数量
        this.isTilebool = true;
        this.NumberListFalag = 1; //扫描标识 1：已扫描 2:已收货
        // public planCountQty: string;//单据计划
        // public scanCountQty: string;//单据已完成
        this.planQty = ''; //单据明细计划
        this.scanQty = ''; //单据明细已完成
        this.planQtyName = '';
        this.scanQtyName = '';
        this.skuCode = ''; //物品编号
        this.skuName = ''; //物品名称
        this.skuSpec = ''; //物品规格
        this.detailStatus = ''; //接受状态 10:未接收,20:已接收
        //this.sname = '';//供应商名称
        this.skuPackage = null; //包装管理
        this.skuPackageDetails = null; //包装管理集合
        this.skuPackageDetailsItem = {}; //包装管理选中
        this.skuConfig = []; //批属性明细
        this.sku = null; //物品信息
        this.lpnCode = ''; //容器编码
        this.scanQtynum = ''; //实际数量
        this.locCode = 'STAGE'; //库位编码
        this.asnDetails = null;
    };
    CheckInPage.prototype.setServerIp = function () {
        // alert("点你妹");
    };
    /*物品扫描*/
    CheckInPage.prototype.scanWmsSku = function (skuCode) {
        var _this = this;
        var body = "asnBillNo=" + this.result.asnBillNo + "&skuCode=" + encodeURI(skuCode);
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].AsnHeaderGetAsnHeaderDetail, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, body, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            if (result.data.success) {
                var asnInfo = result.data;
                _this.skuConfig = [];
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(asnInfo.asnDetails)) {
                    _this.planQty = asnInfo.asnDetail.planCountQty;
                    _this.scanQty = asnInfo.asnDetail.scanCountQty;
                    _this.planQtyName = asnInfo.asnDetail.planQtyName;
                    _this.scanQtyName = asnInfo.asnDetail.scanQtyName;
                    ;
                    _this.skuName = asnInfo.asnDetail.skuName;
                    _this.scanQty = asnInfo.asnDetail.scanCountQty;
                    _this.skuSpec = asnInfo.asnDetail.skuSpec;
                    _this.detailStatus = asnInfo.asnDetail.detailStatusName;
                    _this.asnDetails = asnInfo.asnDetails[0];
                    //获取30个批属性的值
                    for (var index = 1; index <= 30; index++) {
                        var lotkey = 'skuLot' + index;
                        var lotval = _this.asnDetails[lotkey];
                        _this.lotArray[lotkey] = lotval;
                    }
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(asnInfo.skuConfig)) {
                        _this.getModel(asnInfo.skuConfig);
                    }
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(asnInfo.skuPackage)) {
                        _this.skuPackage = asnInfo.skuPackage;
                    }
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(asnInfo.skuPackageDetails)) {
                        _this.skuPackageDetails = asnInfo.skuPackageDetails;
                        var pageUm = _this.skuPackageDetails.filter(function (x) { return x.wsuCode == _this.asnDetails.umCode; });
                        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(_this.skuPackageDetails.filter(function (x) { return x.wsuCode == _this.asnDetails.umCode; }))) {
                            _this.skuPackageDetailsItem = pageUm[0];
                        }
                    }
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(asnInfo.skuPackageDetails)) {
                        _this.sku = asnInfo.sku;
                        if (_this.sku.isSn == 1) {
                            _this.skuPackageDetailsItem = _this.skuPackageDetails.filter(function (x) { return x.skuLevel == 1; })[0];
                            _this.isSn = asnInfo.sku.isSn;
                            //获取序列号
                            var params = {
                                asnBillId: _this.asnDetails.asnBillId,
                                snStatus: __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["d" /* BaseCodeConstant */].SN_STATUS_2,
                                skuCode: skuCode
                            };
                            _this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].snlistInfo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
                                _this.nativeService.hideLoading();
                                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                                    _this.Sn = result.data;
                                    _this.InNumberLength = _this.Sn.length;
                                }
                                else {
                                    //this.nativeService.showToast(result.data.msg);
                                    _this.InNumberLength = 0;
                                }
                            });
                        }
                    }
                }
                _this.togglePage();
            }
            else {
                _this.nativeService.showToast(result.data.msg);
            }
        });
    };
    /**
    * 数据转换
    */
    CheckInPage.prototype.getModel = function (skuConfig) {
        var _this = this;
        skuConfig.forEach(function (Config) {
            var SkuLotVal = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["b" /* SkuLotValResultModel */]();
            SkuLotVal.LotValue = _this.asnDetails[__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["c" /* AppConstant */].skuLot + Config.skuLotIndex];
            SkuLotVal.skuLotIndex = Config.skuLotIndex;
            SkuLotVal.skuLotLabel = Config.skuLotLabel;
            SkuLotVal.skuLot = Config.skuLot;
            SkuLotVal.skuLotDisp = Config.skuLotDisp;
            SkuLotVal.skuLotMust = Config.skuLotMust;
            SkuLotVal.skuLotMix = Config.skuLotMix;
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(Config.skuLotMixMask)) {
                SkuLotVal.skuLotMixMask = Config.skuLotMixMask.toUpperCase();
            }
            SkuLotVal.skuLen = Config.skuLen;
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(Config.skuLen) || Config.skuLen == 0) {
                Config.skuLen = __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["c" /* AppConstant */].MaxLength;
            }
            _this.isMaxLength = true;
            SkuLotVal.skuLotEditType = Config.skuLotEditType;
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].Contains(Config.skuLotMixMask, 'yyyy') || __WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].Contains(Config.skuLotMixMask, 'YYYY')) {
                SkuLotVal.CtlLotEditVal = __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["c" /* AppConstant */].Date;
                SkuLotVal.CtlLotEditBoole = false;
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(SkuLotVal.LotValue)) {
                    SkuLotVal.LotValue = __WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].getDateBath(Config.skuLotMixMask, SkuLotVal.LotValue);
                }
            }
            else {
                SkuLotVal.CtlLotEditVal = __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["c" /* AppConstant */].Text;
                SkuLotVal.CtlLotEditBoole = true;
            }
            _this.skuConfig.push(SkuLotVal);
        });
        console.log(this.skuConfig);
    };
    CheckInPage.prototype.scrollToTop = function () {
        setTimeout(function () {
            window.scrollTo(0, document.body.scrollTop + 1);
            document.body.scrollTop >= 1 && window.scrollTo(0, document.body.scrollTop - 1);
        }, 10);
    };
    /**
    * sku回车事件
    */
    CheckInPage.prototype.skuCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.skuCode)) {
                this.nativeService.showToast('物品编码不能为空');
                return;
            }
            this.scanWmsSku(this.skuCode);
        }
    };
    /**
    * 序列号回车事件
    */
    CheckInPage.prototype.serialNumber_KeyDown = function (event) {
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
                //   this.Sn.forEach(element => {
                //     if (element.snDetailCode == this.serialNumberMode) {
                //       flag = false;
                //     }
                //   });
                // }
                if (this.serialNumber.length + this.InNumberLength >= parseInt(this.planQty)) {
                    flag_1 = false;
                }
                var body = {
                    asnDetailId: this.asnDetails.asnDetailId,
                    snDetailCode: this.serialNumberMode
                };
                this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].isHasSerial, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, body, '', function (result) {
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
    /**
     * 删除扫描序列号
     * @param item
     */
    CheckInPage.prototype.removeItem = function (item) {
        this.serialNumber = this.serialNumber.filter(function (x) { return x != item; });
        //更新长度
        this.ScanNumberLength = this.serialNumber.length;
    };
    CheckInPage.prototype.isTitle = function (event) {
        this.isTilebool = event;
        if (event) {
            this.NumberListFalag = 1;
        }
        else {
            this.NumberListFalag = 2;
        }
    };
    CheckInPage.prototype.expression = function () {
        if (this.recList == 1) {
            this.recList = 0;
        }
        else {
            this.recList = 1;
        }
    };
    CheckInPage.prototype.detailed = function () {
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.asnBillNo)) {
            this.nativeService.showToast('单据编号为空');
            return;
        }
        this.navCtrl.push('CheckinDetailedPage', { asnBillNo: this.asnBillNo });
    };
    /**
    * 查看序列号
    */
    CheckInPage.prototype.numclick = function () {
        this.isSn = 3;
    };
    /**
    * 收货提交
    */
    CheckInPage.prototype.btnOk = function () {
        var _this = this;
        var arrStr = '';
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.asnDetails)) {
            this.nativeService.showToast('请先扫描物品信息');
            return;
        }
        if (this.isSn == 3) {
            this.isSn = 1;
        }
        if (this.isSn == 1) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.serialNumber)) {
                this.nativeService.showToast('没有扫描序列号');
                return;
            }
            this.serialNumber.forEach(function (element) {
                arrStr += element + ',';
            });
            arrStr = arrStr.substring(0, arrStr.length - 1);
            this.scanQtynum = this.serialNumber.length.toString();
        }
        if (this.isSn == 0) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.skuPackageDetailsItem)) {
                this.nativeService.showToast('请填写包装单位');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.skuCode)) {
                this.nativeService.showToast('请扫描物品编码');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.scanQtynum)) {
                this.nativeService.showToast('请填写数量');
                return;
            }
            if (!__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].IsPositiveInt(this.scanQtynum)) {
                this.nativeService.showToast('数量必须为正整数');
                return;
            }
            if (this.skuCode != this.sku.skuCode) {
                this.nativeService.showToast('当前物品编码和扫描物品编码不一致');
                return;
            }
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.locCode)) {
            this.nativeService.showToast('请填写库位');
            return;
        }
        var params = {
            skuId: this.sku.skuId,
            skuCode: this.sku.skuCode,
            isSn: this.isSn,
            planCountQty: this.planQty,
            scanCountQty: this.scanQty,
            wspId: this.skuPackage.wspId,
            wspdId: this.skuPackageDetailsItem.wspdId,
            snDetailCode: arrStr,
            lpnCode: this.lpnCode,
            asnBillNo: this.asnBillNo,
            scanQty: this.scanQtynum,
            locCode: this.locCode,
            whId: this.result.whId,
            asnDetailSkuLotParam: this.lotArray,
            asnBillId: this.result.asnBillId,
            asnDetailId: this.asnDetails.asnDetailId
        };
        var falseInfo = true;
        for (var index = 0; index < this.skuConfig.length; index++) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.skuConfig[index].skuLotMixMask)) {
                if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].Contains(this.skuConfig[index].skuLotMixMask, 'yyyy') || __WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].Contains(this.skuConfig[index].skuLotMixMask, 'YYYY')) {
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.skuConfig[index].LotValue)) {
                        this.skuConfig[index].LotValue = __WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].postDateBath(this.skuConfig[index].skuLotMixMask, this.skuConfig[index].LotValue);
                    }
                }
            }
            if (this.skuConfig[index].skuLotMust == '1' && __WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.skuConfig[index].LotValue)) {
                this.nativeService.showToast('批属性【' + this.skuConfig[index].skuLotLabel + '】:' + '为必填值');
                falseInfo = false;
                break;
            }
            this.lotArray['skuLot' + this.skuConfig[index].skuLotIndex] = this.skuConfig[index].LotValue;
        }
        if (!falseInfo) {
            return;
        }
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitAsnHeader, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            _this.nativeService.showToast('提交成功');
            _this.clearModel();
            _this.finish = result.data.asnDetail.finish;
            _this.count = result.data.asnDetail.count;
            if (_this.finish == _this.count) {
                _this.navCtrl.getPrevious().data.flag = true;
                _this.navCtrl.pop();
            }
        });
    };
    CheckInPage.prototype.compareFn = function (e1, e2) {
        return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
    };
    CheckInPage.prototype.togglePage = function () {
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetailsItem.convertQty)) {
            this.skuSpec = '1-' + this.skuPackageDetailsItem.convertQty;
        }
        else {
            this.skuSpec = '';
        }
        this.scanQtyNameChange();
        this.planQtyNameChange();
    };
    /**
     * 已接收单位
     */
    CheckInPage.prototype.scanQtyNameChange = function () {
        var _this = this;
        if (this.skuPackageDetailsItem.wspdId == 0 || this.skuPackageDetailsItem == 0 || this.skuPackageDetailsItem.scanQtyName == 0) {
            return;
        }
        var result = "";
        var mainWsuName = "";
        var scanQtynum = parseInt(this.scanQty);
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetails)) {
            this.skuPackageDetails = this.skuPackageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetails)) {
            this.skuPackageDetails.forEach(function (element) {
                if (element.skuLevel == _this.skuPackageDetailsItem.skuLevel) {
                    mainWsuName = element.wsuName;
                }
                if (element.skuLevel <= _this.skuPackageDetailsItem.skuLevel) {
                    var tmp = Math.floor(scanQtynum / element.convertQty);
                    scanQtynum = scanQtynum - element.convertQty * tmp;
                    if (tmp != 0) {
                        result += tmp + element.wsuName;
                    }
                }
            });
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.scanQtyName = 0 + mainWsuName;
            //this.planQtyName = 0 + mainWsuName;
        }
        else {
            this.scanQtyName = result;
            //this.planQtyName = result;
        }
    };
    /**
     * 计划量
     */
    CheckInPage.prototype.planQtyNameChange = function () {
        var _this = this;
        if (this.skuPackageDetailsItem.wspdId == 0 || this.skuPackageDetailsItem == 0 || this.skuPackageDetailsItem.planQtyName == 0) {
            return;
        }
        var result = "";
        var mainWsuName = "";
        var scanQtynum = parseInt(this.planQty);
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetails)) {
            this.skuPackageDetails = this.skuPackageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetails)) {
            this.skuPackageDetails.forEach(function (element) {
                if (element.skuLevel == _this.skuPackageDetailsItem.skuLevel) {
                    mainWsuName = element.wsuName;
                }
                if (element.skuLevel <= _this.skuPackageDetailsItem.skuLevel) {
                    var tmp = Math.floor(scanQtynum / element.convertQty);
                    scanQtynum = scanQtynum - element.convertQty * tmp;
                    if (tmp != 0) {
                        result += tmp + element.wsuName;
                    }
                }
            });
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.planQtyName = 0 + mainWsuName;
            //this.planQtyName = 0 + mainWsuName;
        }
        else {
            this.planQtyName = result;
            //this.planQtyName = result;
        }
    };
    CheckInPage.prototype.setNameLineClass = function () {
        return this.fontStypeSize(this.scanQtyName);
    };
    CheckInPage.prototype.setNameLineScanClass = function () {
        return this.fontStypeSize(this.planQtyName);
    };
    CheckInPage.prototype.fontStypeSize = function (obj) {
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(obj)) {
            return '16px';
        }
        if (obj.length <= 8) {
            return '16px';
        }
        else {
            return '12px;';
        }
    };
    CheckInPage.prototype.exit = function () {
        if (this.isSn === 3) {
            this.isSn = 1;
        }
        else if (this.isSn === 1) {
            this.clearModel();
            this.isSn = 0;
        }
        else {
            this.navCtrl.pop();
        }
    };
    CheckInPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-check-in',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkinpiece\check-in\check-in.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title (click)="expression()">\n\n      按件收货<div class="title_f_name">({{result.whName}})</div>\n\n    </ion-title>\n\n    <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="order">\n\n      <div class="order-number">{{asnBillNo}}</div>\n\n      <div class="order-info">单据收货进度 {{finish}}/{{count}}</div>\n\n    </div>\n\n    <!-- <div class="nav-tile clearfix">\n\n      <ul>\n\n        <li>首页</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>收货</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>按件收货</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>执行收货</li>\n\n      </ul>\n\n    </div> -->\n\n    <div class="nav-tile"> \n\n    <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content no-bounce>\n\n  <div class="title-ms clearfix">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-s1">\n\n        <label>{{skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div class="title-font-s2">\n\n          <label>收货规格：</label>\n\n          <label style="color: black;">{{skuSpec}}</label>\n\n        </div>\n\n        <div class="title-font-s3">\n\n          <label>状 态:</label>\n\n          <label style="color: black;">{{detailStatus}}</label>\n\n        </div>\n\n        <div class="title-font-s4">\n\n          <label>{{sname}}</label>\n\n        </div>\n\n\n\n      </div>\n\n      <div *ngIf="scanQtyName!=\'\'&& scanQtyName!=null" class="received-scan">\n\n        已接收: <span style="color: #F59A23;"> {{scanQtyName ==\'\'?\'0\':scanQtyName}}</span>\n\n      </div>\n\n      <div *ngIf="planQtyName!=\'\'&& planQtyName!=null" class="received-plan">\n\n        计划量: <span style="color: #F59A23;"> {{planQtyName ==\'\'?\'0\':planQtyName}}</span>\n\n      </div>\n\n    </div>\n\n  </div>\n\n  <!-- 批次 -->\n\n  <div class="list_item" *ngIf="isSn===0">\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>容器：</ion-label>\n\n      <ion-input maxlength=\'30\' type="text" [(ngModel)]="lpnCode"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>物品：</ion-label>\n\n      <ion-input (keyup)="skuCode_KeyDown($event)" [(ngModel)]="skuCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label> 数量：</ion-label>\n\n      <ion-input type="number" [(ngModel)]="scanQtynum"></ion-input>\n\n      <ion-select (ionChange)="togglePage()" [(ngModel)]="skuPackageDetailsItem" interface="action-sheet"\n\n        [compareWith]="compareFn" cancelText="取消">\n\n        <div *ngFor="let skuPackage of skuPackageDetails">\n\n          <ion-option [value]="skuPackage">{{skuPackage.wsuName}}</ion-option>\n\n        </div>\n\n      </ion-select>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>库位：</ion-label>\n\n      <ion-input type="text" [(ngModel)]="locCode"></ion-input>\n\n      <ion-avatar item-end (click)="setServerIp()">\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n\n\n    <div *ngFor="let item of skuConfig">\n\n      <ion-item *ngIf="item.skuLotDisp===1">\n\n        <ion-label>{{item.skuLotLabel}}：</ion-label>\n\n        <ion-input maxlength="{{item.skuLen !=(null||undefined)?item.skuLen:999}}" *ngIf="item.CtlLotEditBoole"\n\n          type="text" [(ngModel)]="item.LotValue">\n\n        </ion-input>\n\n        <ion-avatar *ngIf="item.CtlLotEditBoole" item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n        <ion-datetime [(ngModel)]="item.LotValue" *ngIf="!item.CtlLotEditBoole" cancelText="取 消" doneText="确 定"\n\n          min="1999-01-01" max="2039-12-31"\n\n          pickerFormat="{{item.skuLotMixMask !=(null||undefined)?item.skuLotMixMask:\'yyyymmdd\'}}"\n\n          displayFormat="{{item.skuLotMixMask !=(null||undefined)?item.skuLotMixMask:\'yyyymmdd\'}}">\n\n        </ion-datetime>\n\n        <ion-avatar *ngIf="!item.CtlLotEditBoole" item-end>\n\n          <ion-icon class="iconfont iconrili"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n    </div>\n\n  </div>\n\n  <!-- 序列号 -->\n\n  <div class="list_item" *ngIf="isSn===1">\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>容器：</ion-label>\n\n      <ion-input maxlength=\'30\' [(ngModel)]="lpnCode" type="text" (blur)="scrollToTop()"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>序列号：</ion-label>\n\n      <ion-input (keyup)="serialNumber_KeyDown($event)" maxlength=\'50\' [(ngModel)]="serialNumberMode" type="text">\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item on-tap="numclick()">\n\n      <ion-label>数量：</ion-label>\n\n      <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{ScanNumberLength + " /" +InNumberLength}}\'>\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </ion-avatar>\n\n      <button ion-button>\n\n        Right Icon\n\n        <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </button>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>库位：</ion-label>\n\n      <ion-input [(ngModel)]="locCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <div *ngFor="let item of skuConfig">\n\n      <ion-item *ngIf="item.skuLotDisp===1">\n\n        <ion-label>{{item.skuLotLabel}}：</ion-label>\n\n        <ion-input maxlength="{{item.skuLen !=(null||undefined)?item.skuLen:999}}" *ngIf="item.CtlLotEditBoole"\n\n          type="text" [(ngModel)]="item.LotValue">\n\n        </ion-input>\n\n        <ion-avatar *ngIf="item.CtlLotEditBoole" item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n        <ion-datetime [(ngModel)]="item.LotValue" *ngIf="!item.CtlLotEditBoole" cancelText="取 消" doneText="确 定"\n\n          min="1999-01-01" max="2039-12-31"\n\n          pickerFormat="{{item.skuLotMixMask !=(null||undefined)?item.skuLotMixMask:\'yyyymmdd\'}}"\n\n          displayFormat="{{item.skuLotMixMask !=(null||undefined)?item.skuLotMixMask:\'yyyymmdd\'}}">\n\n        </ion-datetime>\n\n        <ion-avatar *ngIf="!item.CtlLotEditBoole" item-end>\n\n          <ion-icon class="iconfont iconrili"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n    </div>\n\n  </div>\n\n\n\n  <!-- 序列号明细 -->\n\n  <div class="list_item_serial" *ngIf="isSn===3">\n\n    <div class="list_item_title">\n\n      <span [ngStyle]="{\'border-bottom\':isTilebool === true ? \'3px solid #008080\' : \'0\' }"\n\n        (click)="isTitle(true)">已扫序列号(<b>{{ScanNumberLength}}</b>)</span>\n\n      <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n        (click)="isTitle(false)">已收序列号(<b>{{InNumberLength}}</b>)</span>\n\n    </div>\n\n\n\n  </div>\n\n\n\n  <!-- 已扫描序列号 -->\n\n  <div *ngIf="NumberListFalag==1 && isSn==3 ">\n\n    <ion-list class="list_item_div">\n\n      <ion-scroll scrollY="true" style="height:35rem;">\n\n        <ion-item-sliding *ngFor="let item of serialNumber">\n\n          <ion-item>\n\n            <h2>{{item}}</h2>\n\n          </ion-item>\n\n          <ion-item-options side="right">\n\n            <button (click)="removeItem(item)" ion-button color="danger">\n\n              删除\n\n            </button>\n\n          </ion-item-options>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n\n\n  <!-- 已收序列号 -->\n\n  <div *ngIf="NumberListFalag==2 && isSn==3 ">\n\n    <ion-list class="list_item_div">\n\n      <ion-scroll scrollY="true" style="height:35rem;">\n\n        <ion-item-sliding *ngFor="let item of Sn">\n\n          <ion-item>\n\n            <h2>{{item.snDetailCode}}</h2>\n\n          </ion-item>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      提交[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkinpiece\check-in\check-in.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_7__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_9__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], CheckInPage);
    return CheckInPage;
}());

//# sourceMappingURL=check-in.js.map

/***/ }),

/***/ 774:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CheckInPageModule", function() { return CheckInPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__check_in__ = __webpack_require__(1369);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var CheckInPageModule = /** @class */ (function () {
    function CheckInPageModule() {
    }
    CheckInPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__check_in__["a" /* CheckInPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__check_in__["a" /* CheckInPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CheckInPageModule);
    return CheckInPageModule;
}());

//# sourceMappingURL=check-in.module.js.map

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
//# sourceMappingURL=17.js.map