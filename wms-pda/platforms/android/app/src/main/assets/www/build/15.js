webpackJsonp([15],{

/***/ 1371:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CyclecountrandominfoPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__ = __webpack_require__(825);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__ionic_native_keyboard__ = __webpack_require__(160);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__ = __webpack_require__(360);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__services_BarCodeService__ = __webpack_require__(359);
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
 * Generated class for the CyclecountrandominfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CyclecountrandominfoPage = /** @class */ (function () {
    function CyclecountrandominfoPage(navCtrl, navParams, appService, nativeService, modalCtrl, storage, barCodeService, keyboard, events, ngZone) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.modalCtrl = modalCtrl;
        this.storage = storage;
        this.barCodeService = barCodeService;
        this.keyboard = keyboard;
        this.events = events;
        this.ngZone = ngZone;
        this.serialNumberList = []; //????????????????????????
        this.disabled = true; //??????????????????
        this.sysparms = []; //????????????
        this.skuConfigArray = [];
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
            _this.lotNumber = data.filter(function (x) { return x.paramKey == 'account:lotCount'; })[0].paramValue;
        });
        //??????????????????
        // this.nativeService.showLoading('???????????????????????????????????????');
        // this.appService.httpRequest(Api.api + Api.ApiPDA, Method.Post, '', ContenType.Form, result => {
        //   this.nativeService.hideLoading();
        //   if (Utils.isNotEmpty(result.data)) {
        //     this.WarehouserModel = result.data;
        //   }
        // });
    }
    CyclecountrandominfoPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        this.Batch = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].Batch;
        this.serialNumber = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].serialNumber;
        this.serialListNum = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].serialNumberListNum;
        // this.Warehouse = AppConstant.Warehouse;
        this.BatchList = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].BatchList;
        this.IsShow = this.Batch;
        this.buttonEnt = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].buttonAffirm;
        //????????????????????????????????????
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barCodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_10__services_BarCodeService__["b" /* BarcodeType */].SKU) {
                _this.ngZone.run(function () {
                    _this.skuCode = barcode;
                    _this.scanWmsSku(barcode);
                });
            }
            else if (bt == __WEBPACK_IMPORTED_MODULE_10__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.locCode = barcode;
                });
            }
            else if (bt == __WEBPACK_IMPORTED_MODULE_10__services_BarCodeService__["b" /* BarcodeType */].TuoPan) {
                _this.ngZone.run(function () {
                    _this.lpnCode = barcode; //??????
                });
            }
            else {
                _this.ngZone.run(function () {
                    _this.barCodeService.ScanSkuBarcode(barcode).subscribe(function (val) {
                        if (_this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].BatchList) {
                            for (var index = 0; index < _this.skuConfigArray.length; index++) {
                                _this.skuConfigArray[index].LotValue = val.skuLotModel["skulot" + (index + 1)];
                            }
                        }
                        else {
                            _this.skuCode = val.skucode;
                            _this.scanWmsSku(val.skucode);
                        }
                    });
                });
            }
        });
    };
    CyclecountrandominfoPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
     * ????????????
     * @param event
     */
    CyclecountrandominfoPage.prototype.scanModelChange = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.scanView)) {
                this.barCodeService.ScanSkuBarcode(this.scanView).subscribe(function (val) {
                    _this.ngZone.run(function () {
                        if (_this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].BatchList) {
                            for (var index = 0; index < _this.skuConfigArray.length; index++) {
                                _this.skuConfigArray[index].LotValue = val.skuLotModel["skulot" + (index + 1)];
                            }
                        }
                        else {
                            _this.skuCode = val.skucode;
                            _this.scanWmsSku(val.skucode);
                        }
                    });
                });
            }
            else {
                this.nativeService.showToast('???????????????');
            }
        }
    };
    /**
     * ???????????????
     */
    CyclecountrandominfoPage.prototype.clearMode = function () {
        // this.WarehouserModel=null;//????????????
        // this.warhouserItem = null;//????????????
        this.skuCode = ''; //????????????
        this.skuName = ''; //????????????
        this.skuSpec = ''; //????????????
        this.lpnCode = ''; //????????????
        this.scanQtynum = ''; //????????????
        this.locCode = ''; //????????????
        this.skuPackage = null; //????????????
        this.skuPackageDetailVOList = null; //????????????
        this.skuPackageDetailVOItem = null; //????????????
        this.resultSkuItem = null; //????????????
        this.skuLot = null; //???????????????
        this.skuLotVal = null; //???????????????
        this.countQty = 0; //????????????
        this.serialNumberList = []; //????????????????????????
        this.serialNumberModel = ""; //???????????????
        this.ScanNumberLength = 0; //????????????????????????
        this.stockList = null; //???????????????
        this.stockItem = null; //????????????
        this.disabled = true; //??????????????????
        this.IsShow = this.Batch;
    };
    CyclecountrandominfoPage.prototype.expression = function () {
        // if (this.recList == 1) {
        //   this.recList = 0;
        // } else {
        //   this.recList = 1;
        // }
    };
    /**
    * sku????????????
    */
    CyclecountrandominfoPage.prototype.skuCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.skuCode)) {
                this.nativeService.showToast('????????????????????????');
                return;
            }
            this.scanWmsSku(this.skuCode);
        }
    };
    /**
    * ??????????????????
    */
    CyclecountrandominfoPage.prototype.lpnCode_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            //???????????????????????????
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.lpnCode)) {
                this.nativeService.showToast('?????????????????????');
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
    /*????????????*/
    CyclecountrandominfoPage.prototype.scanWmsSku = function (skuCode) {
        var _this = this;
        var params = {
            skuCode: skuCode
        };
        //????????????
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].SkuList, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            //????????????????????????
            _this.nativeService.hideLoading();
            console.log(result);
            if (result.data.length > 1) {
                //????????????
                _this.openAsnRecordModal(result.data);
            }
            else {
                _this.resultSkuItem = result.data[0];
                if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(_this.resultSkuItem)) {
                    _this.InValAssignment();
                }
                else {
                    _this.nativeService.showToast('?????????????????????');
                }
            }
        });
    };
    /**
     *??????????????????
    */
    CyclecountrandominfoPage.prototype.openAsnRecordModal = function (skuList) {
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
    *????????????
    */
    CyclecountrandominfoPage.prototype.InValAssignment = function () {
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
                skuConfig.skuLotLabel = '?????????' + index;
            }
            skuConfig.skuLotMixMask = this.skuLotVal['skuLotMixMask' + index];
            skuConfig.getLotEditVal();
            this.skuConfigArray.push(skuConfig);
        }
        //this.skuConfigArray.push({});
        //isSn 1???????????? 0????????????
        if (this.resultSkuItem.isSn == 1) {
            this.IsShow = this.serialNumber;
        }
    };
    /**
     * ??????
     */
    CyclecountrandominfoPage.prototype.btnOk = function () {
        if (this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].Batch) {
            //this.randomCountSubmit();
            this.stockListPda();
        }
        else if (this.IsShow == __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].BatchList) {
            if (this.stockItem == '???????????????') {
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
                            this.nativeService.showToast('??????????????????' + this.stockList[index].lotNumber + '??????');
                            return;
                        }
                    }
                }
            }
            this.randomCountSubmit();
        }
        else {
            //this.randomCountSubmit();
            this.stockListPda();
        }
    };
    /**
     * ????????????????????????
     */
    CyclecountrandominfoPage.prototype.stockListPda = function () {
        var _this = this;
        if (!this.checkDataSubmit()) {
            return;
        }
        var params = {
            //  whId: this.warhouserItem.whId
            locCode: this.locCode,
            lpnCode: this.lpnCode,
            skuId: this.resultSkuItem.skuId,
        };
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].StockCountPDA + __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].stockListPda, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.stockList = result.data;
            if (result.data.length == 0) {
                _this.stockItem = "???????????????";
            }
            else {
                _this.stockItem = result.data[0];
            }
            _this.toggle();
            _this.IsShow = _this.BatchList;
            //????????????
            if (_this.IsShow == _this.Batch) {
                _this.modelNum = _this.ScanNumberLength;
            }
            else {
                _this.modelNum = _this.countQty;
            }
        });
    };
    CyclecountrandominfoPage.prototype.checkDataSubmit = function () {
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.lpnCode)) {
                this.nativeService.showToast('????????????,???????????????');
                return false;
            }
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.stockItem))
                this.lpnCode = this.stockItem.lpnCode;
        }
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.locCode)) {
            this.nativeService.showToast('????????????,???????????????');
            return false;
        }
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.resultSkuItem)) {
            this.nativeService.showToast('???????????????????????????,?????????????????????');
            return false;
        }
        if (this.resultSkuItem.isSn == 1) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.serialNumberList)) {
                this.nativeService.showToast('???????????????');
                return false;
            }
            this.countQty = this.ScanNumberLength;
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.skuPackageDetailVOList)) {
                this.nativeService.showToast('?????????????????????????????????????????????,??????PC??????');
                return false;
            }
        }
        else {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.skuPackageDetailVOItem)) {
                this.nativeService.showToast('???????????????');
                return false;
            }
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.countQty)) {
                this.nativeService.showToast('????????????,???????????????');
                return false;
            }
        }
        if (this.IsShow == this.BatchList) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.stockItem)) {
                this.nativeService.showToast('??????????????????');
                return false;
            }
        }
        return true;
    };
    //????????????
    CyclecountrandominfoPage.prototype.randomCountSubmit = function () {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            var wspdId, sku, userInfo, lotNumber, skuLot, index, params;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (this.resultSkuItem.isSn == 1) {
                            sku = this.skuPackageDetailVOList.filter(function (x) { return x.skuLevel == 1; });
                            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(sku)) {
                                this.nativeService.showToast('???????????????????????????????????????,?????????');
                                return [2 /*return*/, false];
                            }
                            wspdId = sku[0].wspdId; //?????????????????????
                        }
                        else {
                            wspdId = this.skuPackageDetailVOItem.wspdId; //??????????????????
                        }
                        return [4 /*yield*/, this.storage.get('userInfo')];
                    case 1:
                        userInfo = _a.sent();
                        lotNumber = '';
                        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.stockItem) && this.stockItem != '???????????????') {
                            lotNumber = this.stockItem.lotNumber;
                        }
                        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.stockItem)) {
                            this.boxCode = this.stockItem.boxCode;
                        }
                        skuLot = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["a" /* SkuLot */]();
                        this.skuConfigArray.forEach(function (element) {
                        });
                        for (index = 0; index < this.skuConfigArray.length; index++) {
                            skuLot['skuLot' + (index + 1)] = this.skuConfigArray[index].LotValue;
                        }
                        params = __assign({ 
                            //whId: this.warhouserItem.whId,
                            lpnCode: this.lpnCode, locCode: this.locCode, skuId: this.resultSkuItem.skuId, lotNumber: lotNumber, countQty: this.countQty, wspdId: wspdId, userId: userInfo.tenant_id, boxCode: this.boxCode, serialNumberList: this.serialNumberList }, skuLot);
                        this.nativeService.showLoading();
                        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].StockCountPDA + __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["b" /* Api */].randomCountSubmit, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["e" /* ContenType */].Json, function (result) {
                            _this.nativeService.hideLoading();
                            if (_this.IsShow == _this.BatchList) {
                                //?????????????????????????????????
                                _this.nativeService.showToast('????????????');
                                _this.clearMode();
                            }
                            else {
                                // if (result.data.stockList.length > 1) {
                                //   this.stockList = result.data.stockList;
                                //   //????????????
                                //   if (this.IsShow == this.Batch) {
                                //     this.modelNum = this.countQty;
                                //   } else {
                                //     this.modelNum = this.ScanNumberLength;
                                //   }
                                //   this.IsShow = this.BatchList;
                                // } else {
                                //   //?????????????????????????????????
                                //   this.nativeService.showToast('????????????');
                                //   this.clearMode();
                                // }
                            }
                        });
                        return [2 /*return*/];
                }
            });
        });
    };
    /**
    * ???????????????
    */
    CyclecountrandominfoPage.prototype.toggle = function () {
        if (this.stockItem != '???????????????') {
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
    * ?????????????????????
    */
    CyclecountrandominfoPage.prototype.serialNumber_KeyDown = function (event) {
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
    * ???????????????
    */
    CyclecountrandominfoPage.prototype.numclick = function () {
        this.IsShow = this.serialListNum;
    };
    /**
   * ?????????????????????
   * @param item
   */
    CyclecountrandominfoPage.prototype.removeItem = function (item) {
        this.serialNumberList = this.serialNumberList.filter(function (x) { return x != item; });
        //????????????
        this.ScanNumberLength = this.serialNumberList.length;
    };
    CyclecountrandominfoPage.prototype.compareFn = function (e1, e2) {
        return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
    };
    CyclecountrandominfoPage.prototype.togglePage = function () {
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetailVOItem) && __WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.skuPackageDetailVOItem.convertQty)) {
            this.skuSpec = '1-' + this.skuPackageDetailVOItem.convertQty;
        }
        else {
            this.skuSpec = '';
        }
    };
    /**
     * ??????
     */
    CyclecountrandominfoPage.prototype.exit = function () {
        // if (this.IsShow == AppConstant.Warehouse) {
        //   this.navCtrl.pop();
        // } 
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
    CyclecountrandominfoPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-cyclecountrandominfo',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cyclecounthome\cyclecountrandominfo\cyclecountrandominfo.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title (click)="expression()">\n\n      ????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n    <ion-buttons end>\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>????????????</li>\n\n      </ul> -->\n\n      <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n  <!-- ?????? -->\n\n  <div *ngIf="IsShow==Warehouse" class="item-warhouser">\n\n    <ion-item>\n\n      <ion-label>??? ??????</ion-label>\n\n      <ion-select [(ngModel)]="warhouserItem" interface="action-sheet" cancelText="??????">\n\n        <div *ngFor="let Item of WarehouserModel">\n\n          <ion-option [value]="Item">{{Item.whName}}</ion-option>\n\n        </div>\n\n      </ion-select>\n\n    </ion-item>\n\n  </div>\n\n\n\n  <div *ngIf="IsShow!=Warehouse">\n\n    <div class="top-plan">\n\n    <!--  <div class="title-ms-info">\n\n        <div class="title-font-s1">\n\n          <label>{{skuName}}</label>\n\n        </div>\n\n        <div class="title-font-small">\n\n          <div class="title-font-s2">\n\n            <label>???????????????</label>\n\n            <label style="color: black;">{{skuSpec}}</label>\n\n          </div>\n\n        </div>\n\n      </div> -->\n\n      <span>{{skuName}}</span>\n\n      <span>????????????:<span style="color: black;">{{skuSpec}}</span></span>\n\n    </div>\n\n    <!-- ?????? -->\n\n    <div class="list_item" >\n\n      <ion-item>\n\n        <ion-label>???????????????</ion-label>\n\n        <ion-input (keyup)="scanModelChange($event)" [(ngModel)]="scanView" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>?????????</ion-label>\n\n        <ion-input [(ngModel)]="locCode" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item *ngIf="sysparms.paramValue==0">\n\n        <ion-label>?????????</ion-label>\n\n        <ion-input maxlength=\'30\' [(ngModel)]="lpnCode" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>?????????</ion-label>\n\n        <ion-input (keyup)="skuCode_KeyDown($event)" [(ngModel)]="skuCode" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label> ?????????</ion-label>\n\n        <ion-input type="number" [(ngModel)]="countQty"></ion-input>\n\n        <ion-select (ionChange)="togglePage()" [(ngModel)]="skuPackageDetailVOItem" [compareWith]="compareFn"\n\n          interface="action-sheet" cancelText="??????">\n\n          <div *ngFor="let skuPackage of skuPackageDetailVOList">\n\n            <ion-option [value]="skuPackage">{{skuPackage.wsuName}}</ion-option>\n\n          </div>\n\n        </ion-select>\n\n      </ion-item>\n\n\n\n    </div>\n\n    <!-- ????????? -->\n\n    <div class="list_item" *ngIf="IsShow===serialNumber">\n\n      <ion-item>\n\n        <ion-label>?????????</ion-label>\n\n        <ion-input [(ngModel)]="locCode" type="text"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item *ngIf="sysparms.paramValue==0">\n\n        <ion-label>?????????</ion-label>\n\n        <ion-input [(ngModel)]="lpnCode" type="text"></ion-input>\n\n        <ion-avatar item-end (click)="setServerIp()">\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>????????????</ion-label>\n\n        <ion-input (keyup)="serialNumber_KeyDown($event)" maxlength=\'50\' [(ngModel)]="serialNumberModel" type="text">\n\n        </ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>?????????</ion-label>\n\n        <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{ScanNumberLength}}\'>\n\n        </ion-input>\n\n        <!-- <span>0/20???</span> -->\n\n        <ion-avatar item-end (click)="numclick()">\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n    </div>\n\n    <!-- ??????????????? -->\n\n    <div class="list_item_serial" *ngIf="IsShow===serialListNum">\n\n      <div class="list_item_title">\n\n        <span [ngStyle]="{\'border-bottom\':\'3px solid #008080\'}">???????????????(<b>{{ScanNumberLength}}</b>)</span>\n\n\n\n      </div>\n\n    </div>\n\n    <!-- ?????????????????? -->\n\n    <div *ngIf="IsShow==serialListNum ">\n\n      <ion-list class="list_item_div">\n\n        <ion-scroll scrollY="true" style="height:35rem;">\n\n          <ion-item-sliding *ngFor="let item of serialNumberList">\n\n            <ion-item>\n\n              <h2>{{item}}</h2>\n\n            </ion-item>\n\n            <ion-item-options side="right">\n\n              <button (click)="removeItem(item)" ion-button color="danger">\n\n                ??????\n\n              </button>\n\n            </ion-item-options>\n\n          </ion-item-sliding>\n\n        </ion-scroll>\n\n      </ion-list>\n\n    </div>\n\n\n\n    <!-- ???????????? -->\n\n    <div class="list_item" class="item-warhouser" *ngIf="IsShow===BatchList">\n\n      <ion-item>\n\n        <ion-label>????????????</ion-label>\n\n        <ion-select (ionChange)="toggle()" [(ngModel)]="stockItem" interface="action-sheet" cancelText="??????">\n\n          <div *ngFor="let item of stockList">\n\n            <ion-option [value]="item">{{item.lotNumber}}</ion-option>\n\n          </div>\n\n          <ion-option [value]="">???????????????</ion-option>\n\n        </ion-select>\n\n      </ion-item>\n\n      <div *ngFor="let item of skuConfigArray;let i = index">\n\n        <ion-item *ngIf="item.CtlLotEditVal!=\'Date\'; else elseBlock">\n\n          <ion-label>{{item.skuLotLabel}}???</ion-label>\n\n          <ion-input [disabled]=disabled type="text" [(ngModel)]="item.LotValue"></ion-input>\n\n          <ion-avatar item-end>\n\n            <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n          </ion-avatar>\n\n        </ion-item>\n\n        <ng-template #elseBlock>\n\n          <ion-item>\n\n            <ion-label>{{item.skuLotLabel}}???</ion-label>\n\n            <ion-datetime [(ngModel)]="item.LotValue" cancelText="??? ???" doneText="??? ???" min="1999-01-01" max="2039-12-31"\n\n              pickerFormat="YYYY-MM-DD" displayFormat="YYYY-MM-DD">\n\n            </ion-datetime>\n\n            <ion-avatar item-end>\n\n              <ion-icon class="iconfont iconrili"></ion-icon>\n\n            </ion-avatar>\n\n          </ion-item>\n\n        </ng-template>\n\n      </div>\n\n      <ion-item>\n\n        <ion-label>?????????</ion-label>\n\n        <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{modelNum}}\'>\n\n        </ion-input>\n\n      </ion-item>\n\n    </div>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      <!-- {{buttonEnt}}[Ent] -->\n\n      ??????[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cyclecounthome\cyclecountrandominfo\cyclecountrandominfo.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_3__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_5__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_7__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_10__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_8__ionic_native_keyboard__["a" /* Keyboard */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], CyclecountrandominfoPage);
    return CyclecountrandominfoPage;
}());

//# sourceMappingURL=cyclecountrandominfo.js.map

/***/ }),

/***/ 776:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CyclecountrandominfoPageModule", function() { return CyclecountrandominfoPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__cyclecountrandominfo__ = __webpack_require__(1371);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var CyclecountrandominfoPageModule = /** @class */ (function () {
    function CyclecountrandominfoPageModule() {
    }
    CyclecountrandominfoPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__cyclecountrandominfo__["a" /* CyclecountrandominfoPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__cyclecountrandominfo__["a" /* CyclecountrandominfoPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CyclecountrandominfoPageModule);
    return CyclecountrandominfoPageModule;
}());

//# sourceMappingURL=cyclecountrandominfo.module.js.map

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
//# sourceMappingURL=15.js.map