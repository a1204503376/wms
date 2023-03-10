webpackJsonp([8,68],{

/***/ 1374:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return TakeawayinboxPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_App_service__ = __webpack_require__(157);
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
 * Generated class for the TakeawayinboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var TakeawayinboxPage = /** @class */ (function () {
    function TakeawayinboxPage(navCtrl, navParams, nativeService, appService, modalCtrl, storage, barcodeService, ngZone, events, popoverCtrl) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.appService = appService;
        this.modalCtrl = modalCtrl;
        this.storage = storage;
        this.barcodeService = barcodeService;
        this.ngZone = ngZone;
        this.events = events;
        this.popoverCtrl = popoverCtrl;
        // @ViewChild('ceshi') ceshi: ElementRef<HTMLElement>;;
        this.recList = 1;
        this.IsShow = 0; //????????????
        this.IsSku = 0; //????????????
        this.IsSkuList = 1; //??????????????????
        this.IsSkuSerial = 4; //?????????????????????
        this.IsSkuSerialInfo = 5; //???????????????????????????
        this.InNumberLength = 0; //?????????????????????
        this.ScanNumberLength = 0; //????????????????????????
        this.serialNumber = []; //????????????????????????
        this.InserialNumber = []; //??????????????????
        this.NumberListFalag = 1; //???????????? 1???????????? 2:?????????
        this.isTilebool = true;
        this.totalPlanQty = 0;
        this.totalScanQty = 0;
        this.targetLocCode = 'PICK'; //????????????
        this.sysparms = []; //????????????
        this.scanModelRecords = [];
        this.pickPlansModel = this.navParams.get('pickPlansModel');
        this.PickInfoResult = this.navParams.get('PickInfoResult');
        this.pickPlansList = this.navParams.get('pickPlansList');
        this.wellenNo = this.navParams.get('wellenNo');
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    TakeawayinboxPage.prototype.ionViewDidLoad = function () {
        this.labelTypecom.ComponentFlag = true;
        this.labelTypecom.locCodeFlag = false;
        this.HeadModel();
    };
    TakeawayinboxPage.prototype.initModel = function () {
        this.labelTypecom.ComponentFlag = true;
        this.labelTypecom.locCodeFlag = false;
        this.pickPlansInModel();
        this.firstLoadCom();
    };
    TakeawayinboxPage.prototype.clearModel = function () {
        this.targetLpnCode = '';
        this.skuCodeModel = '';
        this.lotNumber = '';
        this.pickQty = '';
        this.sourceLocCode = "";
        this.serialNumber = [];
        this.ScanNumberLength = 0;
        this.scanView = '';
        this.targetLocCode = 'PICK';
        this.scanModel = null;
    };
    TakeawayinboxPage.prototype.clearModelLabel = function () {
        this.targetLocCode = "PICK";
        this.sourceLpnCode = "";
        this.sourceLocCode1 = "";
        this.targetLpnCode = "";
        this.labelTypecom.scanModel.skucode = "";
        this.labelTypecom.scanModel.skuname = "";
        this.labelTypecom.scanModel.qty = "";
        this.labelTypecom.scanModel.um = "";
    };
    /**
     * ???????????????
     */
    TakeawayinboxPage.prototype.HeadModel = function () {
        //??????
        this.count = this.PickInfoResult.count;
        this.finish = this.PickInfoResult.finish;
        //???????????????
        this.sourceLocCode = this.pickPlansModel.sourceLocCode;
        this.skuName = this.pickPlansModel.skuName;
        this.skuCode = this.pickPlansModel.skuCode;
        this.orderNo = this.PickInfoResult.orderNo;
        this.sobillNo = this.PickInfoResult.sobillNo;
        this.cName = this.PickInfoResult.cName;
        this.transportDate = this.PickInfoResult.transportDate;
        this.skuSpec = this.pickPlansModel.skuSpec;
        this.skuLot = this.pickPlansModel.skuLot;
        this.lotNumberTitle = this.pickPlansModel.lotNumber;
        this.planCountQty = this.pickPlansModel.planCountQty;
        this.totalPlanQty = this.pickPlansModel.totalPlanQty;
        this.totalScanQty = this.pickPlansModel.totalScanQty;
        this.realCountQty = this.pickPlansModel.realCountQty;
        this.planQtyName = this.pickPlansModel.planQtyName;
        this.realQtyName = this.pickPlansModel.realQtyName;
    };
    TakeawayinboxPage.prototype.pickPlansInModel = function () {
        var _this = this;
        this.HeadModel();
        //????????????????????????
        this.sourceLocCode = this.pickPlansModel.sourceLocCode;
        this.sourceLpnCode = this.pickPlansModel.sourceLpnCode;
        this.packageDetails = this.pickPlansModel.packageDetails;
        //?????????????????????????????????
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.scanModel)) {
            this.defaultpackageDetail = this.pickPlansModel.packageDetails.find(function (x) { return x.wsuName == _this.scanModel.um; });
        }
        else {
            this.defaultpackageDetail = this.pickPlansModel.defaultPackageDetail;
        }
        this.InserialNumber = this.pickPlansModel.serialList;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.pickPlansModel.serialList)) {
            this.InNumberLength = this.pickPlansModel.serialList.length;
        }
        //????????????????????????
        // if (this.pickPlansModel.isSn == '1') {
        //   this.IsShow = this.IsSkuSerial;
        // } else {
        //   this.IsShow = this.IsSku;
        // }
        this.IsShow = this.IsSku;
    };
    TakeawayinboxPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        this.labelTypecom.qtyDisabled = false;
        //????????????????????????????????????
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].BoxCode) {
                _this.ngZone.run(function () {
                    _this.getAsnHeaderDetailForBox(barcode);
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.sourceLocCode1 = barcode;
                });
            }
        });
    };
    TakeawayinboxPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    TakeawayinboxPage.prototype.scanModelChange = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.scanView)) {
                this.getAsnHeaderDetailForBox(this.scanView);
            }
            else {
                this.nativeService.showToast('???????????????');
            }
        }
    };
    /**
     * ????????????????????????
     */
    TakeawayinboxPage.prototype.firstLoadCom = function () {
        //????????????
        this.labelTypecom.scanModel.skucode = this.pickPlansModel.skuCode;
        this.labelTypecom.scanModel.skuname = this.pickPlansModel.skuName;
        // this.labelTypecom.scanModel.qty = this.planCountQty;
        this.labelTypecom.scanModel.um = this.defaultpackageDetail ? this.defaultpackageDetail.wsuName : "";
    };
    /**
     * ???????????????????????????
     * @param model
     */
    TakeawayinboxPage.prototype.getAsnHeaderDetailForBox = function (model) {
        var _this = this;
        this.barcodeService.ScanSkuBarcode(model).subscribe(function (val) {
            _this.scanModel = val;
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                for (var index = 1; index <= 30; index++) {
                    val.skuLotModel["skuLotName" + index] = _this.pickPlansModel.skuLotValue["skuLotLabel" + index];
                }
                var pickPlansModel = _this.pickPlansList.filter(function (x) {
                    if (x.skuCode != val.skucode)
                        return false;
                    return true;
                });
                if (pickPlansModel.length <= 0) {
                    _this.nativeService.showToast('????????????????????????????????????????????????!');
                }
                else {
                    // this.scanModel.um = this.labelTypecom.scanModel.um;
                    _this.pickPlansModel = pickPlansModel[0];
                    _this.getTotalScanQtyBySku(val);
                }
            }
        });
    };
    /**
     * ??????????????????
     */
    TakeawayinboxPage.prototype.onClickItem = function () {
        this.IsShow = this.IsSkuList;
        this.pickPlansModelTemp = this.pickPlansModel;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.pickPlansList)) {
            for (var _i = 0, _a = this.pickPlansList; _i < _a.length; _i++) {
                var item = _a[_i];
                item['isCheck'] = false;
            }
        }
    };
    /**
     * ????????????
     */
    TakeawayinboxPage.prototype.detailed = function () {
        var _this = this;
        var params = {
            wellenNo: this.wellenNo
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getSopickLog, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data.pickPlans)) {
                _this.navCtrl.push('TakeawayindetailedPage', { pickPlans: result.data.pickPlans, finish: _this.finish,
                    count: _this.count, wellenNo: _this.wellenNo, orderNo: _this.orderNo, sobillNo: _this.sobillNo });
            }
            else {
                _this.nativeService.showToast('?????????????????????');
            }
        });
    };
    /**
     * ??????????????????
     */
    TakeawayinboxPage.prototype.headSelected = function (item) {
        this.pickPlansModelTemp = item;
    };
    TakeawayinboxPage.prototype.togglePage = function () {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.defaultpackageDetail.convertQty)) {
            this.skuSpec = '1-' + this.defaultpackageDetail.convertQty;
        }
        else {
            this.skuSpec = '';
        }
        this.planQtyPage();
        this.realQtyPage();
    };
    TakeawayinboxPage.prototype.planQtyPage = function () {
        var _this = this;
        var result = "";
        var mainWsuName = "";
        var planQtynum = parseInt(this.planCountQty);
        this.packageDetails = this.packageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        this.packageDetails.forEach(function (element) {
            if (element.skuLevel == _this.defaultpackageDetail.skuLevel) {
                mainWsuName = element.wsuName;
            }
            if (element.skuLevel <= _this.defaultpackageDetail.skuLevel) {
                var tmp = Math.floor(planQtynum / element.convertQty);
                planQtynum = planQtynum - element.convertQty * tmp;
                if (tmp != 0) {
                    result += tmp + element.wsuName;
                }
            }
        });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.planQtyName = 0 + mainWsuName;
        }
        else {
            this.planQtyName = result;
        }
    };
    TakeawayinboxPage.prototype.realQtyPage = function () {
        var _this = this;
        var result = "";
        var mainWsuName = "";
        var planQtynum = parseInt(this.realCountQty);
        this.packageDetails = this.packageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        this.packageDetails.forEach(function (element) {
            if (element.skuLevel == _this.defaultpackageDetail.skuLevel) {
                mainWsuName = element.wsuName;
            }
            if (element.skuLevel <= _this.defaultpackageDetail.skuLevel) {
                var tmp = Math.floor(planQtynum / element.convertQty);
                planQtynum = planQtynum - element.convertQty * tmp;
                if (tmp != 0) {
                    result += tmp + element.wsuName;
                }
            }
        });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.realQtyName = 0 + mainWsuName;
        }
        else {
            this.realQtyName = result;
        }
    };
    /**
    * ??????????????????
    */
    TakeawayinboxPage.prototype.skuCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.skuCodeModel)) {
                this.nativeService.showToast('??????????????????????????????');
                return;
            }
            // let params = {
            //   skuCode: this.skuCodeModel
            // }
            //????????????
            // this.resultSkuItem=null;
            // this.nativeService.showLoading();
            // this.appService.httpRequest(Api.api + Api.SkuList, Method.Post, params, ContenType.Json, result => {
            //   this.nativeService.hideLoading();
            //   //????????????????????????
            //   console.log(result);
            //   if (result.data.length > 1) {
            //     //????????????
            //     this.openAsnRecordModal(result.data);
            //   } else if (Utils.isEmpty(result.data)) {
            //     this.nativeService.showToast('?????????????????????');
            //   } else {
            //     this.resultSkuItem = result.data[0];
            //     this.getSkuInfo();
            //   }
            // });
            this.getSkuInfo();
        }
    };
    TakeawayinboxPage.prototype.getSkuInfo = function () {
        var _this = this;
        //????????????????????????
        var model = this.pickPlansList.filter(function (x) { return x.skuCode == _this.skuCodeModel && x.sourceLocCode == _this.sourceLocCode && x.sourceLpnCode == _this.sourceLpnCode; });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(model)) {
            //this.nativeService.showToast('?????????????????????????????????');
            return;
        }
        this.pickPlansModel = model[0];
        this.pickPlansInModel();
    };
    /**
    *??????????????????
    */
    // openAsnRecordModal(skuList) {
    //   let myModal = this.modalCtrl.create('SkuListModal', {
    //     skuList: skuList,
    //   });
    //   myModal.onDidDismiss(data => {
    //     if (Utils.isNotEmpty(data)) {
    //       this.resultSkuItem = data;
    //       this.getSkuInfo();
    //     }
    //   });
    //   myModal.present();
    // }
    /**
     * ????????????
     */
    TakeawayinboxPage.prototype.btnOk = function () {
        if (this.IsShow == this.IsSkuList) {
            this.pickPlansModel = this.pickPlansModelTemp;
            this.labelTypecom.ComponentFlag = true;
            this.labelTypecom.locCodeFlag = false;
            this.IsShow = this.IsSku;
            this.HeadModel();
        }
        else if (this.IsShow == this.IsSkuSerialInfo) {
            this.submitPickInfo();
        }
        else {
            //????????????
            this.submitPickInfo();
        }
    };
    TakeawayinboxPage.prototype.presentPopover = function (myEvent) {
        var _this = this;
        var popover = this.popoverCtrl.create('PopoverPage', { show_item_3: false, show_item_4: true });
        popover.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data) && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.pickPlansList)) {
                console.log(_this.pickPlansList);
                if (data == 'allCheck') {
                    //??????
                    for (var index = 0; index < _this.pickPlansList.length; index++) {
                        _this.pickPlansList[index].isCheck = true;
                    }
                }
                else if (data == 'clearCheck') {
                    //????????????
                    for (var index = 0; index < _this.pickPlansList.length; index++) {
                        _this.pickPlansList[index].isCheck = false;
                    }
                }
                else if (data == 'complated') {
                    //????????????????????????
                    if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.pickPlansList)) {
                        var item = _this.pickPlansList.find(function (x) { return x.isCheck; });
                        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(item)) {
                            for (var _i = 0, _a = _this.pickPlansList; _i < _a.length; _i++) {
                                var item1 = _a[_i];
                                if (item1.isCheck) {
                                    _this.complatedClick1(item1);
                                }
                            }
                        }
                        else {
                            _this.nativeService.showToast('?????????????????????????????????');
                        }
                    }
                    else {
                        _this.nativeService.showToast('?????????????????????????????????');
                    }
                }
            }
        });
        popover.present({
            ev: myEvent
        });
    };
    /**
     * ????????????
     */
    TakeawayinboxPage.prototype.skuStockIsCheckDel = function () {
        this.pickPlansList = this.pickPlansList.filter(function (x) { return x.isCheck === false; });
    };
    TakeawayinboxPage.prototype.getTotalScanQtyBySku = function (val) {
        var _this = this;
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getTotalScanQtyBySku, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, 'wellenNo=' + this.wellenNo +
            '&skuCode=' + this.pickPlansModel.skuCode, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            if (result.success) {
                _this.totalScanQty = result.data.totalScanQty;
                _this.totalPlanQty = result.data.totalPlanQty;
                _this.pickPlansModel.totalPlanQty = _this.totalPlanQty;
                _this.pickPlansModel.totalScanQty = _this.totalScanQty;
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(_this.scanModelRecords)) {
                    _this.scanModelRecords.push(val);
                }
                else {
                    for (var _i = 0, _a = _this.scanModelRecords; _i < _a.length; _i++) {
                        var item = _a[_i];
                        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isObjectValueEqual1(val, item, ['skuname', 'qty', 'setLotModel', 'setLotValue'])) {
                            if (Number.isNaN(item.qty) || __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(item.qty)) {
                                item.qty = Number.parseInt(val.qty);
                            }
                            else {
                                item.qty = Number.parseInt(item.qty) + Number.parseInt(val.qty);
                            }
                            val = item;
                        }
                        else {
                            if (!__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].contains(_this.scanModelRecords, val, ['skuname', 'qty', 'setLotModel', 'setLotValue'])) {
                                _this.scanModelRecords.push(val);
                                break;
                            }
                        }
                    }
                }
                _this.labelTypecom.changeRun(val);
                _this.initModel();
                // this.labelTypecom.scanModel.qty = val.qty;
            }
        });
    };
    /**
     * ??????????????????
     */
    TakeawayinboxPage.prototype.complatedClick1 = function (item) {
        var _this = this;
        var lots = {};
        for (var prop in item.skuLot) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(item.skuLot[prop]))
                continue;
            lots[prop] = item.skuLot[prop];
        }
        var params = {
            wellenNo: this.wellenNo,
            pickPlanId: item.pickPlanId,
            sourceLocCode: item.sourceLocCode,
            skuCode: item.skuCode,
            lotNumber: item.lotNumber,
            pickQty: Number.parseInt(item.planCountQty) - Number.parseInt(item.realCountQty),
            targetLocCode: this.targetLocCode,
            wspdId: item.packageDetails[0].wspdId,
            isSn: this.pickPlansModel.isSn,
            lots: lots
        };
        if (this.sysparms.paramValue == 0) {
            params['targetLpnCode'] = this.targetLpnCode;
            params['sourceLpnCode'] = item.sourceLpnCode;
        }
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitPickInfoByBox, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            _this.PickInfoResult = result.data;
            _this.pickPlansList = _this.PickInfoResult.pickPlans;
            var model = _this.pickPlansList.filter(function (x) { return x.skuCode == _this.labelTypecom.scanModel.skucode && x.skuName == _this.labelTypecom.scanModel.skuname; });
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(model)) {
                _this.pickPlansModel = model[0];
            }
            else if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.pickPlansList)) {
                _this.pickPlansModel = _this.pickPlansList[0];
            }
            else {
                _this.navCtrl.pop();
            }
            _this.count = _this.PickInfoResult.count;
            _this.finish = _this.PickInfoResult.finish;
        });
    };
    /**
     * ????????????
     */
    TakeawayinboxPage.prototype.submitPickInfo = function () {
        var _this = this;
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLpnCode)) {
                this.nativeService.showToast('????????????????????????');
                return;
            }
        }
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.sourceLpnCode)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.sourceLocCode1)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.lotNumberTitle)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLocCode)) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.labelTypecom.scanModel.um)) {
            this.nativeService.showToast('??????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.wellenNo)) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.defaultpackageDetail)) {
            this.nativeService.showToast('????????????????????????????????????????????????');
            return;
        }
        var lots = {};
        for (var key in this.labelTypecom.scanModel.skuLotModel) {
            if (key.indexOf('skuLotName') == -1) {
                var str = key.substring(6, key.length);
                lots["skuLot" + str] = this.labelTypecom.scanModel.skuLotModel[key];
            }
        }
        var params = {
            wellenNo: this.wellenNo,
            pickPlanId: this.pickPlansModel.pickPlanId,
            sourceLocCode: this.sourceLocCode1,
            skuCode: this.labelTypecom.scanModel.skucode,
            lotNumber: this.lotNumberTitle,
            pickQty: this.labelTypecom.scanModel.qty,
            targetLocCode: this.targetLocCode,
            // wsuName: this.labelTypecom.scanModel.um,
            wspdId: this.defaultpackageDetail.wspdId,
            isSn: this.pickPlansModel.isSn,
            lots: lots
        };
        if (this.sysparms.paramValue == 0) {
            params['targetLpnCode'] = this.targetLpnCode;
            params['sourceLpnCode'] = this.sourceLpnCode;
        }
        // if (this.pickPlansModel.isSn == '1') {
        //   if (this.ScanNumberLength == 0) {
        //     this.nativeService.showToast('??????????????????');
        //     return;
        //   }
        //   params['serialList'] = this.serialNumber;
        //   params['pickQty'] = this.serialNumber.length.toString();
        // } else {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.labelTypecom.scanModel.um)) {
            this.nativeService.showToast('??????????????????');
            return;
        }
        // }
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitPickInfoByBox, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            //????????????
            _this.PickInfoResult = result.data; //?????????
            _this.pickPlansList = _this.PickInfoResult.pickPlans; //????????????
            var model = _this.pickPlansList.filter(function (x) { return x.skuCode == _this.labelTypecom.scanModel.skucode && x.skuName == _this.labelTypecom.scanModel.skuname; });
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(model)) {
                _this.pickPlansModel = model[0];
            }
            else if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.pickPlansList)) {
                _this.pickPlansModel = _this.pickPlansList[0];
            }
            else {
                _this.navCtrl.pop();
            }
            _this.HeadModel();
            _this.clearModelLabel();
            _this.nativeService.showToast('????????????');
        });
    };
    /**
     * ????????????
     */
    TakeawayinboxPage.prototype.exit = function () {
        if (this.IsShow == this.IsSkuList) {
            this.IsShow = this.IsSku;
            // this.initModel();
        }
        else if (this.IsShow == this.IsSku) {
            this.navCtrl.pop();
        }
        else if (this.IsShow == this.IsSkuSerial) {
            this.lotNumber = '';
            this.targetLpnCode = '';
            this.IsShow = this.IsSku;
        }
        else if (this.IsShow == this.IsSkuSerialInfo) {
            this.IsShow = this.IsSkuSerial;
        }
    };
    TakeawayinboxPage.prototype.compareFn = function (e1, e2) {
        return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
    };
    TakeawayinboxPage.prototype.isTitle = function (event) {
        this.isTilebool = event;
        if (event) {
            this.NumberListFalag = 1;
        }
        else {
            this.NumberListFalag = 2;
        }
    };
    /**
    * ?????????????????????
    */
    TakeawayinboxPage.prototype.SkuSerial_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.serialNumberMode)) {
                var flag_1 = true;
                this.serialNumber.forEach(function (element) {
                    if (element.toUpperCase() == _this.serialNumberMode.toUpperCase()) {
                        flag_1 = false;
                    }
                });
                var body = {
                    skuId: this.pickPlansModel.skuId,
                    serialNumber: this.serialNumberMode
                };
                if (this.sysparms.paramValue != 0) {
                    body["lpnCode"] = this.sourceLpnCode;
                }
                this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].OutIsHasSerial, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, body, '', function (result) {
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
    TakeawayinboxPage.prototype.setNameLineClass = function () {
        return this.fontStypeSize(this.realQtyName);
    };
    TakeawayinboxPage.prototype.setNameLineScanClass = function () {
        return this.fontStypeSize(this.planQtyName);
    };
    TakeawayinboxPage.prototype.fontStypeSize = function (obj) {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(obj)) {
            return '16px';
        }
        if (obj.length <= 8) {
            return '16px';
        }
        else {
            return '12px;';
        }
    };
    /**
    * ???????????????
    */
    TakeawayinboxPage.prototype.numclick = function () {
        this.IsShow = this.IsSkuSerialInfo;
    };
    /**
    * ?????????????????????
    * @param item
    */
    TakeawayinboxPage.prototype.removeItem = function (item) {
        this.serialNumber = this.serialNumber.filter(function (x) { return x != item; });
        //????????????
        this.ScanNumberLength = this.serialNumber.length;
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewChild"])('labelTypecom'),
        __metadata("design:type", Object)
    ], TakeawayinboxPage.prototype, "labelTypecom", void 0);
    TakeawayinboxPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-takeawayinbox',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiecebox\takeawayinbox\takeawayinbox.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      ??????????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n    <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="order">\n\n      <div class="order-number">{{orderNo||sobillNo}}</div>\n\n      <div class="order-info">?????????????????? {{finish}}/{{count}}</div>\n\n    </div>\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>????????????</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>??????????????????</li>\n\n      </ul> -->\n\n      <crumbs></crumbs>\n\n      <button ion-button icon-only (click)="presentPopover($event)" *ngIf="IsShow===IsSkuList">\n\n        <ion-icon name="md-more"></ion-icon>\n\n      </button>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n\n\n<ion-content class="no-scroll" overflow-scroll="true" style="overflow: hidden">\n\n  <!-- ??????????????? -->\n\n  <!-- <div class="title-ms clearfix" *ngIf="IsShow===IsSku||IsShow===IsSkuSerial||IsShow===IsSkuSerialInfo"\n\n    (tap)="onClickItem()">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-s1">\n\n        <label>{{skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div>\n\n          <label>{{skuCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label>{{lotNumberTitle}}</label>\n\n        </div>\n\n\n\n        <div class="specs_code">\n\n          ???????????????<span style="color: black;">{{skuSpec}}</span>\n\n        </div>\n\n        <div class="ionic_right">\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </div>\n\n      </div>\n\n    </div>\n\n    <div *ngIf="realQtyName!=\'\'&& realQtyName!=null " [ngStyle]="{\'font-size\':setNameLineClass()}"\n\n      class="received-scan">\n\n      ?????????: <span style="color: #F59A23;"> {{realQtyName ==\'\'?\'0\':realQtyName}}</span>\n\n    </div>\n\n    <div *ngIf="planQtyName!=\'\'&& planQtyName!=null " [ngStyle]="{\'font-size\':setNameLineScanClass()}"\n\n      class="received-plan">\n\n      ?????????: <span style="color: #F59A23;"> {{planQtyName ==\'\'?\'0\':planQtyName}}</span>\n\n    </div>\n\n  </div> -->\n\n  <div class="top-plan" *ngIf="IsShow===IsSku||IsShow===IsSkuSerial||IsShow===IsSkuSerialInfo"\n\n  (tap)="onClickItem()">\n\n    <div class="top-plan_1">\n\n      <span>{{skuName}}</span>\n\n      <span>{{skuCode}}</span> \n\n      <span>{{sourceLocCode}}</span> \n\n      <span>{{transportDate}}</span>\n\n      <span *ngIf="skuLot&&skuLot.skuLot2">{{skuLot.skuLot2}}</span> \n\n      <span *ngIf="skuLot&&skuLot.skuLot4">{{skuLot.skuLot4}}</span> \n\n      <span>{{cName}}</span>\n\n    </div>\n\n    <div class="top-plan_2">\n\n      <div class="top-plan_2_1">\n\n        <span >????????????:<span style="color: black;">{{skuSpec}}</span></span>\n\n        <span *ngIf="realQtyName!=\'\'&& realQtyName!=null "\n\n              [ngStyle]="{\'font-size\':setNameLineClass()}"\n\n        >?????????:<span style="color: #F59A23;">{{realQtyName ==\'\'?\'0\':realQtyName}}</span></span>\n\n        <span *ngIf="planQtyName!=\'\'&& planQtyName!=null " \n\n              [ngStyle]="{\'font-size\':setNameLineScanClass()}"\n\n        >?????????:<span style="color: #F59A23;">{{planQtyName ==\'\'?\'0\':planQtyName}}</span></span>\n\n        <div *ngIf="totalPlanQty>0" class="received-total">\n\n          <span style="color: #F59A23;"> {{totalScanQty}}/{{totalPlanQty}}</span>\n\n        </div>\n\n      </div>\n\n      <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n    </div>\n\n    \n\n  </div> \n\n  <!-- ??????????????? -->\n\n  <div class="list_item" [hidden]="IsShow!=IsSku">\n\n    <div>\n\n      <ion-item>\n\n        <ion-label>???????????????</ion-label>\n\n        <ion-input (keyup)="scanModelChange($event)" [(ngModel)]="scanView" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item *ngIf="sysparms.paramValue==0">\n\n        <ion-label>???????????????</ion-label>\n\n        <ion-input maxlength=\'30\' type="text" [(ngModel)]="targetLpnCode"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>????????????</ion-label>\n\n        <ion-input  [(ngModel)]="sourceLocCode1" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item *ngIf="sysparms.paramValue==0">\n\n        <ion-label>????????????</ion-label>\n\n        <ion-input maxlength=\'30\' [disabled]=\'true\' [(ngModel)]="sourceLpnCode" type="text"></ion-input>\n\n        <ion-avatar item-end (click)="setServerIp()">\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>???????????????</ion-label>\n\n        <ion-input [disabled]=\'true\' [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n    </div>\n\n    <page-check-in-box-label-type #labelTypecom>\n\n\n\n    </page-check-in-box-label-type>\n\n  </div>\n\n\n\n  <!-- ????????? -->\n\n  <div class="list_item" *ngIf="IsShow==IsSkuSerial">\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>???????????????</ion-label>\n\n      <ion-input maxlength=\'30\' type="text" [(ngModel)]="targetLpnCode"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input [disabled]=\'true\' [(ngModel)]="sourceLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input maxlength=\'30\' [disabled]=\'true\' [(ngModel)]="sourceLpnCode" type="text"></ion-input>\n\n      <ion-avatar item-end (click)="setServerIp()">\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="text" maxlength=\'100\' (keyup)="SkuSerial_KeyDown($event)" [(ngModel)]="serialNumberMode">\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input [(ngModel)]="lotNumber" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item (click)="numclick()">\n\n      <ion-label>?????????</ion-label>\n\n      <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{ScanNumberLength + " /" +InNumberLength}}\'>\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>???????????????</ion-label>\n\n      <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n  </div>\n\n  <!-- ??????????????? -->\n\n  <div class="list_item_serial" *ngIf="IsShow===IsSkuSerialInfo">\n\n    <div class="list_item_title">\n\n      <span [ngStyle]="{\'border-bottom\':isTilebool === true ? \'3px solid #008080\' : \'0\' }"\n\n        (click)="isTitle(true)">???????????????(<b>{{ScanNumberLength}}</b>)</span>\n\n      <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n        (click)="isTitle(false)">???????????????(<b>{{InNumberLength}}</b>)</span>\n\n    </div>\n\n\n\n  </div>\n\n\n\n  <!-- ?????????????????? -->\n\n  <div *ngIf="NumberListFalag==1 && IsShow===IsSkuSerialInfo ">\n\n    <ion-list class="list_item_div">\n\n      <ion-scroll scrollY="true" style="height:35rem;">\n\n        <ion-item-sliding *ngFor="let item of serialNumber">\n\n          <ion-item>\n\n            <h2>{{item}}</h2>\n\n          </ion-item>\n\n          <ion-item-options side="right">\n\n            <button (click)="removeItem(item)" ion-button color="danger">\n\n              ??????\n\n            </button>\n\n          </ion-item-options>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n\n\n  <!-- ??????????????? -->\n\n  <div *ngIf="NumberListFalag==2 && IsShow===IsSkuSerialInfo ">\n\n    <ion-list class="list_item_div">\n\n      <ion-scroll scrollY="true" style="height:35rem;">\n\n        <ion-item-sliding *ngFor="let item of InserialNumber">\n\n          <ion-item>\n\n            <h2>{{item}}</h2>\n\n          </ion-item>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n\n\n\n\n  <!-- ???????????? -->\n\n  <div class="title-ms-list" *ngIf="IsShow===IsSkuList">\n\n    <ion-list>\n\n      <ion-item-sliding *ngFor="let item of pickPlansList;let i = index"  >\n\n        <ion-item  style="height: auto !important;" (click)="headSelected(item)"\n\n        [ngClass]="{selected:pickPlansModelTemp.pickPlanId == item.pickPlanId}">\n\n          <div class="nodes-list-item">\n\n            <div class="left">\n\n              <div class="chk_wrapper">\n\n                <input type="checkbox" id="{{\'checkbox_\'+i}}" class="chk_1" [(ngModel)]="item.isCheck">\n\n                <label for="{{\'checkbox_\'+i}}"></label>\n\n              </div>\n\n              <div class="left-2">\n\n                <span>{{item.skuName}}</span>\n\n                <span>{{item.skuCode}}</span>\n\n                <span>{{item.sourceLocCode}}</span>\n\n                <span  *ngIf="item.skuLot.skuLot2!=\'\'">{{item.skuLot.skuLot2}}</span>\n\n                <span  *ngIf="item.skuLot.skuLot4!=\'\'">{{item.skuLot.skuLot4}}</span>\n\n              </div>\n\n              \n\n            </div>\n\n            <div class="right">\n\n              <div>\n\n                <span>???????????????</span>\n\n                <span>{{item.skuSpec}}</span>\n\n              </div>\n\n              <div>\n\n                <span>?????????:</span>\n\n                <span style="color: #F59A23;">{{item.realCountQty}}/{{item.planCountQty}}</span>\n\n              </div>\n\n            </div>\n\n          </div>\n\n        </ion-item>\n\n        <ion-item-options side="right">\n\n          <button ion-button color="secondary" (click)="complatedClick1(item)">\n\n            <ion-icon name="checkmark" md="md-checkmark"></ion-icon>\n\n            ??????\n\n          </button>\n\n        </ion-item-options>\n\n      </ion-item-sliding>\n\n    </ion-list>\n\n    <!-- <div class="title-ms-info-list" [ngClass]="{selected:pickPlansModelTemp.pickPlanId == item.pickPlanId}"\n\n      (click)="headSelected(item)" *ngFor="let item of pickPlansList">\n\n      <div class="title-font-s1">\n\n        <label>{{item.skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div>\n\n          <label>{{item.skuCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label>?????????:{{item.sourceLocCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label style="color: black;" *ngIf="item.skuLot.skuLot4!=\'\'">\n\n            {{item.skuLot.skuLot4}}\n\n          </label>\n\n        </div>\n\n        <div class="list_specs_code">\n\n          ???????????????<span style="color: black;">{{item.skuSpec}}</span>\n\n        </div>\n\n        <div class="list_received">\n\n          ?????????: <span style="color: #F59A23;">{{item.realCountQty}}</span>/{{item.planCountQty}}\n\n        </div>\n\n      </div>\n\n    </div> -->\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      ??????[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiecebox\takeawayinbox\takeawayinbox.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["p" /* PopoverController */]])
    ], TakeawayinboxPage);
    return TakeawayinboxPage;
}());

//# sourceMappingURL=takeawayinbox.js.map

/***/ }),

/***/ 701:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CheckInBoxLabelTypePageModule", function() { return CheckInBoxLabelTypePageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__ = __webpack_require__(792);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var CheckInBoxLabelTypePageModule = /** @class */ (function () {
    function CheckInBoxLabelTypePageModule() {
    }
    CheckInBoxLabelTypePageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */]),
            ],
            exports: [
                __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */]
            ]
        })
    ], CheckInBoxLabelTypePageModule);
    return CheckInBoxLabelTypePageModule;
}());

//# sourceMappingURL=check-in-box-label-type.module.js.map

/***/ }),

/***/ 779:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "TakeawayinboxPageModule", function() { return TakeawayinboxPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__takeawayinbox__ = __webpack_require__(1374);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__shared_check_in_box_label_type_check_in_box_label_type_module__ = __webpack_require__(701);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};





var TakeawayinboxPageModule = /** @class */ (function () {
    function TakeawayinboxPageModule() {
    }
    TakeawayinboxPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__takeawayinbox__["a" /* TakeawayinboxPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_3__shared_check_in_box_label_type_check_in_box_label_type_module__["CheckInBoxLabelTypePageModule"],
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__takeawayinbox__["a" /* TakeawayinboxPage */]),
                __WEBPACK_IMPORTED_MODULE_4__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], TakeawayinboxPageModule);
    return TakeawayinboxPageModule;
}());

//# sourceMappingURL=takeawayinbox.module.js.map

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
            { id: "HomePage", name: "??????" },
            { id: "TaskviewPage", name: "????????????" },
            { id: "CheckinhomePage", name: "??????" },
            { id: "CheckinpiecePage", name: "????????????" },
            { id: "CheckInPage", name: "????????????" },
            { id: "CheckinboxPage", name: "????????????" },
            { id: "SearchOrderPage", name: "????????????" },
            { id: "CheckinBoxInfoPage", name: "????????????" },
            { id: "CheckintrayPage", name: "????????????" },
            { id: "CheckinDetailedPage", name: "????????????" },
            { id: "CollageTaskPage", name: "????????????" },
            { id: "CollageTaskListPage", name: "????????????" },
            { id: "CollageTaskBoxPage", name: "????????????" },
            { id: "CyclecounthomePage", name: "??????" },
            { id: "CyclecountrandominfoPage", name: "????????????" },
            { id: "CyclecountrantaskPage", name: "????????????" },
            { id: "CyclecountBillPage", name: "????????????" },
            { id: "CyclecoutrantaskinfoPage", name: "????????????" },
            { id: "MovehomePage", name: "??????" },
            { id: "MovesboxPage", name: "????????????" },
            { id: "MovesboxInfoPage", name: "????????????" },
            { id: "moveSku", name: "????????????" },
            { id: "moveStock", name: "????????????" },
            { id: "moveStocks", name: "????????????" },
            { id: "PackageTaskPage", name: "????????????" },
            { id: "PackageTaskListPage", name: "????????????" },
            { id: "PackageTaskInfoPage", name: "????????????" },
            { id: "PutawayhomePage", name: "??????" },
            { id: "PutawayPage", name: "????????????" },
            { id: "PutawayboxPage", name: "????????????" },
            { id: "ReplenishmenhomePage", name: "????????????" },
            { id: "ReplenishmenDetilePage", name: "????????????" },
            { id: "SettingPage", name: "????????????" },
            { id: "SettingPwdPage", name: "????????????" },
            { id: "SettingThemePage", name: "????????????" },
            { id: "ShipmenthomePage", name: "??????" },
            { id: "ShipmentLpnPage", name: "????????????" },
            { id: "StockhomePage", name: "??????" },
            { id: "StockdetailPage", name: "????????????" },
            { id: "StockqueryPage", name: "????????????" },
            { id: "StockPritPage", name: "????????????" },
            { id: "TakeawayhomePage", name: "??????" },
            { id: "TakeawaypiecePage", name: "????????????" },
            { id: "TakeawayPage", name: "????????????" },
            { id: "TakeawayinPage", name: "????????????" },
            { id: "TakeawayindetailedPage", name: "????????????" },
            { id: "TakeawaypieceboxPage", name: "????????????" },
            { id: "TakeawayinboxPage", name: "????????????" },
            { id: "CougnyPage", name: "??????" },
            { id: "CopyLabelPage", name: "????????????" },
            { id: "RandomCheckPage", name: "????????????" },
            { id: "RandomCheckTaskPage", name: "????????????" },
            { id: "ReplenishmenPage", name: "????????????" },
            { id: "ReplenishmenRecordPage", name: "????????????" },
            { id: "StockFrozenPage", name: "????????????" },
            { id: "CheckOrderPage", name: "????????????" },
            { id: "UpdateDetailPage", name: "????????????" },
            { id: "TaskwayMultPage", name: "????????????" },
            { id: "BindPrintterPage", name: "???????????????" },
            { id: "BindOrderPage", name: "????????????" },
            { id: "BindLpnPage", name: "????????????" },
            { id: "CyclecountRecordPage", name: "????????????" },
            { id: "MergeTrayPage", name: "??????" }
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

/***/ 792:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CheckInBoxLabelTypePage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__ = __webpack_require__(158);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__models_DataBase_Model__ = __webpack_require__(361);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__ = __webpack_require__(359);
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
 * Generated class for the CheckInBoxLabelTypePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CheckInBoxLabelTypePage = /** @class */ (function () {
    function CheckInBoxLabelTypePage(navCtrl, navParams, nativeService, appService, barcodeService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.appService = appService;
        this.barcodeService = barcodeService;
        this.skuLotModel = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__["a" /* SkuLotModel */](); //???????????????
        this.ComponentFlag = true; //????????????
        this.EidtFlag = true; //???????????????
        this.qtyDisabled = true; //?????????????????????
        this.locDisabled = false; //?????????????????????
        this.locCode = 'STAGE'; //????????????
        this.locCodeFlag = true; //????????????
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_7__models_DataBase_Model__["a" /* ScanModel */](); //??????????????????
        this.isCopyLabel = false;
        this.outer = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
        this.labelType = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].labelType;
        console.log('constructor CheckInBoxLabelTypePage');
    }
    CheckInBoxLabelTypePage.prototype.ionViewDidLoad = function () {
        console.log('?????????---ionViewDidLoad---CheckInBoxLabelTypePage');
    };
    CheckInBoxLabelTypePage.prototype.ionViewWillLeave = function () {
        console.log('?????????---ionViewWillLeave---CheckInBoxLabelTypePage');
    };
    /**
     * ???????????????
     * @param scan
     */
    CheckInBoxLabelTypePage.prototype.changeRun = function (scan) {
        //??????????????????
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(scan)) {
            this.ComponentFlag = true;
            this.scanModel = scan;
            this.outer.emit('');
        }
    };
    /**
     * ???????????????
     */
    CheckInBoxLabelTypePage.prototype.clearModel = function () {
        this.skuCode = '';
        this.scanQty = 0;
        this.wsuName = '';
        this.instockDate = null;
        this.produceDate = null;
        this.typeCode = '';
        this.lotNumber = '';
        this.version = '';
        this.remarks = '';
        this.type = '';
        this.labelCode = '';
        this.locCode = 'STAGE';
        this.skuLotModel = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__["a" /* SkuLotModel */]();
        this.ComponentFlag = false;
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Output"])(),
        __metadata("design:type", Object)
    ], CheckInBoxLabelTypePage.prototype, "outer", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
        __metadata("design:type", Object)
    ], CheckInBoxLabelTypePage.prototype, "ruleCode", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
        __metadata("design:type", String)
    ], CheckInBoxLabelTypePage.prototype, "asnBillNo", void 0);
    CheckInBoxLabelTypePage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-check-in-box-label-type',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\shared\check-in-box-label-type\check-in-box-label-type.html"*/'<div *ngIf="ComponentFlag">\n\n  <ion-item *ngIf="locCodeFlag">\n\n    <ion-label>???????????????</ion-label>\n\n    <ion-input [disabled]=\'locDisabled\' maxlength=\'30\' type="text" [(ngModel)]="locCode">\n\n    </ion-input>\n\n  </ion-item>\n\n  <div class="list_item">\n\n    <ion-item *ngIf="skuCodeFlag">\n\n      <ion-label>???????????????</ion-label>\n\n      <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skucode"></ion-input>\n\n    </ion-item>\n\n  </div>\n\n  <ion-item *ngIf="skuNameFlag">\n\n    <ion-label>???????????????</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuname">\n\n    </ion-input>\n\n  </ion-item>\n\n  <ion-item>\n\n    <ion-label>?????????</ion-label>\n\n    <ion-input [disabled]=\'qtyDisabled\' maxlength=\'30\' type="number" [(ngModel)]="scanModel.qty">\n\n    </ion-input>\n\n    <ion-select [disabled]=\'EidtFlag\' [(ngModel)]="scanModel.um" cancelText="??????">\n\n      <ion-option [value]="scanModel.um">{{scanModel.um}}</ion-option>\n\n    </ion-select>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName1!=null &&scanModel.skuLotModel.skulot1!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName1}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot1">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName2!=null &&scanModel.skuLotModel.skulot2!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName2}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot2">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName3!=null &&scanModel.skuLotModel.skulot3!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName3}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot3">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName4!=null &&scanModel.skuLotModel.skulot4!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName4}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot4">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName5!=null&&scanModel.skuLotModel.skuLotName5!=\'\' &&scanModel.skuLotModel.skulot5!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName5}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot5">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName6!=null &&scanModel.skuLotModel.skulot6!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName6}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot6">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName7!=null &&scanModel.skuLotModel.skulot7!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName7}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot7">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName8!=null &&scanModel.skuLotModel.skulot8!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotNam8}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot8">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName9!=null &&scanModel.skuLotModel.skulot9!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName9}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot9">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName10!=null &&scanModel.skuLotModel.skulot10!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName10}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot10">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName1!=null &&scanModel.skuLotModel.skuLotName1!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName1}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot1">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName2!=null &&scanModel.skuLotModel.skuLotName2!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName2}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot2">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName3!=null &&scanModel.skuLotModel.skuLotName3!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName3}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot3">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName4!=null &&scanModel.skuLotModel.skuLotName4!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName4}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot4">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName5!=null &&scanModel.skuLotModel.skuLotName5!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName5}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot5">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName6!=null &&scanModel.skuLotModel.skuLotName6!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName6}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot6">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName7!=null &&scanModel.skuLotModel.skuLotName7!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName7}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot7">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName8!=null &&scanModel.skuLotModel.skuLotName8!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotNam8}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot8">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName9!=null &&scanModel.skuLotModel.skuLotName9!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName9}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot9">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName10!=null &&scanModel.skuLotModel.skuLotName10!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName10}}???</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot10">\n\n    </ion-input>\n\n  </ion-item>\n\n</div>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\shared\check-in-box-label-type\check-in-box-label-type.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_5__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["a" /* BarcodeService */]])
    ], CheckInBoxLabelTypePage);
    return CheckInBoxLabelTypePage;
}());

//# sourceMappingURL=check-in-box-label-type.js.map

/***/ })

});
//# sourceMappingURL=8.js.map