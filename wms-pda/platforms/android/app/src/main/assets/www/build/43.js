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
        //????????????
        this.IsShow = 1; //????????????
        // public Iswarehouse: number = 0;//????????????
        this.IsSku = 1; //??????????????????
        this.IsSkuMove = 2; //??????????????????
        this.IsSkuBathList = 3; //???????????????
        this.IsSkuSerial = 4; //?????????????????????
        this.IsSkuSerialInfo = 5; //???????????????????????????
        this.NumberListFalag = 1; //???????????? 1???????????? 2:?????????
        this.sourceLpnCode = ''; //???????????????
        this.sourceLocCode = ''; //???????????????
        this.InNumberLength = 0; //?????????????????????
        this.ScanNumberLength = 0; //????????????????????????
        this.serialNumber = []; //????????????????????????
        this.isTilebool = true;
        this.sysparms = []; //????????????
        //?????????????????? 2020-3-31??????
        // this.nativeService.showLoading('??????????????????...');
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
    //??????????????????
    moveSku.prototype.toggle = function () {
        //this.whName = this.warhouserItem.whName;
    };
    moveSku.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad moveSku');
    };
    moveSku.prototype.clearModel = function () {
        this.IsShow = this.IsSku;
        this.wsuName = '';
        this.sourceLpnCode = ''; //???????????????
        this.sourceLocCode = ''; //???????????????
        this.skuCode = ''; //????????????
        this.resultSkuItem = null; //????????????
        this.moveQty = ''; //????????????
        this.skuName = ''; //????????????
        this.skuSpec = ''; //????????????
        this.stockQty = null; //??????????????????
        this.packageDetails = null; //????????????
        this.defaultPackageDetail = null; //????????????
        this.skuStockLotModel = null; //??????????????????
        this.targetLpnCode = ''; //????????????
        this.targetLocCode = ''; //????????????
        this.serialNumber = [];
        this.stockQtyName = '';
    };
    moveSku.prototype.ionViewWillEnter = function () {
        var _this = this;
        //????????????????????????????????????
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
                        //?????????
                        _this.sourceLpnCode = barcode;
                        _this.getLocCode();
                    }
                    else {
                        //????????????
                        _this.targetLpnCode = barcode;
                    }
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    if (_this.IsShow == _this.IsSku) {
                        //?????????
                        _this.sourceLocCode = barcode;
                    }
                    else {
                        //????????????
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
    * ?????????????????????
    */
    moveSku.prototype.sourceLpnCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            this.getLocCode();
        }
    };
    /**
     * ????????????????????????
     */
    moveSku.prototype.getLocCode = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.sourceLpnCode)) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        var params = {
            sourceLpnCode: this.sourceLpnCode
        };
        //??????????????????
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getLocCode, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, '', function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                //????????????
                _this.sourceLocCode = result.data.sourceLocCode;
            }
        });
    };
    /**
    * sku????????????
    */
    moveSku.prototype.skuCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            this.scanWmsSku();
        }
    };
    /**
    * ?????????????????????
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
    * ???????????????
    */
    moveSku.prototype.numclick = function () {
        this.IsShow = this.IsSkuSerialInfo;
    };
    /*????????????*/
    moveSku.prototype.scanWmsSku = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.skuCode)) {
            this.nativeService.showToast('????????????????????????');
        }
        var params = {
            skuCode: this.skuCode
        };
        //????????????
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].SkuList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            // this.clearModel();
            _this.serialNumber = [];
            _this.ScanNumberLength = 0;
            //????????????????????????
            console.log(result);
            if (result.data.length > 1) {
                //????????????
                _this.openAsnRecordModal(result.data);
            }
            else if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('?????????????????????');
            }
            else {
                _this.resultSkuItem = result.data[0];
                _this.getSkuInfo();
            }
        });
    };
    /**
     *??????????????????
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
     * ????????????????????????
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
        //????????????????????????
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
            _this.defaultPackageDetail = resultData1.defaultPackageDetail; //??????????????????
            _this.defaultPackageDetailTemp = resultData1.defaultPackageDetail; //????????????????????????
            _this.defaultPackageDetailPermanent = resultData1.defaultPackageDetail; //????????????????????????(???????????????????????????)
            _this.stockQtyPermanent = _this.stockQty;
            //??????????????????????????????
            //this.Sn=resultData1;
            //this.wsuName = this.defaultPackageDetail.wsuName;
        });
        //??????????????????
        // let resultData1 = {
        //     skuName: '??????????????????',
        //     skuSpec: '1-10-001',
        //     stockQty: '500',
        //     packageDetails: [
        //         { wspdId: '1', wsuName: '???', wsuCode: '001', convertQty: 1, skuLevel: 1 },
        //         { wspdId: '2', wsuName: '???', wsuCode: '000', convertQty: 10, skuLevel: 2 },
        //         { wspdId: '3', wsuName: '???', wsuCode: '000', convertQty: 100, skuLevel: 3 }
        //     ],
        //     defaultPackageDetail: { wspdId: '1', wsuName: '???', wsuCode: '001', skuLevel: '1', convertQty: '10' }
        // }
        // this.skuName = resultData1.skuName;
        // this.skuSpec = resultData1.skuSpec;
        // this.stockQty = Number(resultData1.stockQty);
        // this.packageDetails = resultData1.packageDetails;
        // this.defaultPackageDetail = resultData1.defaultPackageDetail;//??????????????????
        // this.defaultPackageDetailTemp = resultData1.defaultPackageDetail;//????????????????????????
        // this.defaultPackageDetailPermanent = resultData1.defaultPackageDetail;//????????????????????????(???????????????????????????)
        // this.stockQtyPermanent = this.stockQty;
        // this.wsuName = this.defaultPackageDetail.wsuName;
    };
    /**
     * ????????????
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
 * ???????????????
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
     * ????????????
     */
    moveSku.prototype.btnOk = function () {
        if (this.IsShow == this.IsSku || this.IsShow == this.IsSkuSerial) {
            this.submitSku();
        }
        else if (this.IsShow == this.IsSkuMove) {
            this.submitMoveStockSku();
        }
        else {
            //????????????
            this.submitLotNumberSku();
            // this.IsShow = this.IsSkuMove;
        }
    };
    /**
     * ????????????????????????
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
     * ??????????????????
     */
    moveSku.prototype.submitMoveStockSku = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.skuStockLotModel)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.targetLocCode)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.targetLpnCode)) {
            this.nativeService.showToast('?????????????????????');
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
        //?????????????????????
        if (this.resultSkuItem.isSn == 1) {
            if (this.ScanNumberLength == 0) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            params['moveQty'] = this.ScanNumberLength; //???????????????
            // params['?????????'] = this.serialNumber;//???????????????
            params['wspdId'] = this.defaultPackageDetail.wspdId;
            params['serialList'] = this.serialNumber;
        }
        else {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.moveQty)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
            if (!__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].IsPositiveInt(this.moveQty)) {
                this.nativeService.showToast('??????????????????????????????');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.defaultPackageDetail)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
            params['moveQty'] = this.moveQty;
            params['wspdId'] = this.defaultPackageDetail.wspdId;
        }
        //??????????????????
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitMoveStockSku, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('????????????');
                //????????????
                _this.clearModel();
            }
        });
    };
    /**
     * ?????????????????????
     */
    moveSku.prototype.submitSku = function () {
        var _this = this;
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.sourceLpnCode)) {
                this.nativeService.showToast('???????????????????????????');
                return;
            }
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.sourceLocCode)) {
            this.nativeService.showToast('???????????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.resultSkuItem)) {
            this.nativeService.showToast('???????????????????????????');
            return;
        }
        var params = {
            skuId: this.resultSkuItem.skuId,
            sourceLpnCode: this.sourceLpnCode,
            sourceLocCode: this.sourceLocCode,
        };
        //?????????????????????
        if (this.resultSkuItem.isSn == 1) {
            if (this.ScanNumberLength == 0) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            params['moveQty'] = this.ScanNumberLength;
            params['serialList'] = this.serialNumber;
            params['wspdId'] = this.defaultPackageDetail.wspdId;
            this.moveQty = this.ScanNumberLength.toString();
        }
        else {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.moveQty)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
            if (!__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].IsPositiveInt(this.moveQty)) {
                this.nativeService.showToast('??????????????????????????????');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.defaultPackageDetail)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
            params['moveQty'] = this.moveQty;
            params['wspdId'] = this.defaultPackageDetail.wspdId;
        }
        this.nativeService.showLoading();
        //????????????????????????????????????
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockMove + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitSku, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            var skuStockLotMoveVOs = result.data.skuStockLotMoveVOs;
            //?????????????????????
            if (skuStockLotMoveVOs.length > 1) {
                //?????????????????????????????????
                _this.skuStockLotMoveVOs = skuStockLotMoveVOs;
                //????????????????????????
                _this.skuStockLotModel = _this.skuStockLotMoveVOs[0];
                _this.IsShow = _this.IsSkuBathList;
            }
            else if (skuStockLotMoveVOs.length < 1) {
                _this.nativeService.showToast('????????????????????????????????????');
            }
            else {
                _this.skuStockLotModel = skuStockLotMoveVOs[0];
                _this.IsShow = _this.IsSkuMove;
            }
        });
        // let skuStockLotMoveVOs = [
        //     {
        //         stockQty: '10', lotNumber: '05500555', ownerName: '?????????',
        //         skuLotList: [{ skuLotIndex: '1', skuLotValue: '???????????????1', skuLotLabel: '??????', skuLotEditType: 'yyyy' }, { skuLotIndex: '2', skuLotValue: '????????????2', skuLotLabel: '??????', skuLotEditType: '' }],
        //         packageDetails: [{ wspdId: '1', wsuName: '???', wsuCode: '001' }, { wspdId: '2', wsuName: '???', wsuCode: '000' }],
        //         defaultPackageDetail: { wspdId: '1', wsuName: '???', wsuCode: '001', skuLevel: '1', convertQty: '10' }
        //     },
        //     {
        //         stockQty: '20', lotNumber: '055005558', ownerName: '?????????2???',
        //         skuLotList: [{ skuLotIndex: '1', skuLotValue: '???????????????1', skuLotLabel: '??????', skuLotEditType: '' }, { skuLotIndex: '2', skuLotValue: '????????????2', skuLotLabel: '??????', skuLotEditType: '' }],
        //         packageDetails: [{ wspdId: '1', wsuName: '???', wsuCode: '001' }, { wspdId: '2', wsuName: '???', wsuCode: '000' }],
        //         defaultPackageDetail: { wspdId: '1', wsuName: '???', wsuCode: '001', skuLevel: '1', convertQty: '10' }
        //     }
        // ];
        //?????????????????????
        // if (skuStockLotMoveVOs.length > 1) {
        //     //?????????????????????????????????
        //     this.skuStockLotMoveVOs = skuStockLotMoveVOs;
        //     //????????????????????????
        //     this.skuStockLotModel = this.skuStockLotMoveVOs[0];
        //     this.IsShow = this.IsSkuBathList;
        // } else if (skuStockLotMoveVOs.length < 1) {
        //     this.nativeService.showToast('????????????????????????????????????');
        // } else {
        //     this.skuStockLotModel = skuStockLotMoveVOs[0];
        //     this.IsShow = this.IsSkuMove;
        // }
    };
    /**
     * ????????????
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
            this.IsShow = this.IsSku; //???????????????????????????????????????????????????
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
    * ?????????????????????
    * @param item
    */
    moveSku.prototype.removeItem = function (item) {
        this.serialNumber = this.serialNumber.filter(function (x) { return x != item; });
        //????????????
        this.ScanNumberLength = this.serialNumber.length;
    };
    moveSku = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-movesku',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesku\movesku.html"*/'<ion-header>\n\n    <ion-navbar color="wmscolor">\n\n        <ion-title>\n\n            ????????????\n\n            <!-- <div class="title_f_name">(?????????)</div> -->\n\n        </ion-title>\n\n        <!-- <ion-buttons end (click)="detailed()">\n\n            <button ion-button icon-only>\n\n                <ion-icon name="menu"></ion-icon>\n\n            </button>\n\n        </ion-buttons> -->\n\n    </ion-navbar>\n\n    <div class="title-bt">\n\n        <div class="nav-tile">\n\n            <!-- <ul>\n\n                <li>??????</li>\n\n                <li>\n\n                    <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n                </li>\n\n                <li>??????</li>\n\n                <li>\n\n                    <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n                </li>\n\n                <li>????????????</li>\n\n            </ul> -->\n\n            <crumbs></crumbs>\n\n            <div class="order-info">{{whName}}</div>\n\n        </div>\n\n    </div>\n\n</ion-header>\n\n<ion-content>\n\n    <div *ngIf="IsShow===Iswarehouse">\n\n        <div class="list_item">\n\n            <ion-item>\n\n                <ion-label>??? ??????</ion-label>\n\n                <ion-select (ionChange)="toggle()" [(ngModel)]="warhouserItem" interface="action-sheet" cancelText="??????">\n\n                    <div *ngFor="let Item of WarehouserModel">\n\n                        <ion-option [value]="Item">{{Item.whName}}</ion-option>\n\n                    </div>\n\n                </ion-select>\n\n            </ion-item>\n\n        </div>\n\n    </div>\n\n    <div\n\n        *ngIf="IsShow===IsSkuBathList||IsShow===IsSkuMove||IsShow===IsSku||IsShow===IsSkuSerial||IsShow===IsSkuSerialInfo">\n\n        <!-- ??????????????? -->\n\n        <div class="title-ms clearfix">\n\n            <div class="title-ms-info clearfix">\n\n                <ul>\n\n                    <li class="l-width2">\n\n                        <span class="s-label">???&emsp;??????</span>\n\n                        <span class="s-text">{{skuName}}</span>\n\n                    </li>\n\n                    <li class="l-width1">\n\n                        <span class="s-label">????????????</span>\n\n                        <span class="s-text">{{sourceLocCode}}</span>\n\n                    </li>\n\n                    <li class="l-width1">\n\n                        <span class="s-label">?????????</span>\n\n                        <span class="s-text">{{skuSpec}}</span>\n\n                    </li>\n\n                    <li class="l-width1" *ngIf="sysparms.paramValue==0">\n\n                        <span class="s-label">????????????</span>\n\n                        <span class="s-text">{{sourceLpnCode}}</span>\n\n                    </li>\n\n                    <li class="l-width1">\n\n                        <div *ngIf="IsShow!=IsSkuMove; else elseBlock">\n\n                            <span class="s-label">?????????</span>\n\n                            <span class="s-text">{{stockQtyName}}</span>\n\n                        </div>\n\n                        <ng-template #elseBlock>\n\n                            <span class="s-label">???????????????</span>\n\n                            <span class="s-text">{{moveQty}} {{wsuName}}</span>\n\n                        </ng-template>\n\n                    </li>\n\n                </ul>\n\n            </div>\n\n        </div>\n\n    </div>\n\n\n\n    <!-- ???????????? -->\n\n    <div *ngIf="IsShow==IsSku">\n\n        <ion-item>\n\n            <ion-label>????????????</ion-label>\n\n            <ion-input type="text" [(ngModel)]="sourceLocCode"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item *ngIf="sysparms.paramValue==0">\n\n            <ion-label>????????????</ion-label>\n\n            <ion-input type="text" [(ngModel)]="sourceLpnCode" (keyup)="sourceLpnCode_KeyDown($event)"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item>\n\n            <ion-label>?????????</ion-label>\n\n            <ion-input type="text" (keyup)="skuCode_KeyDown($event)" [(ngModel)]="skuCode"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item>\n\n            <ion-label>?????????</ion-label>\n\n            <ion-input type="number" [(ngModel)]="moveQty"></ion-input>\n\n            <ion-select [(ngModel)]="defaultPackageDetail" (ionChange)="togglePage()" [compareWith]="compareFn"\n\n                interface="action-sheet" cancelText="??????">\n\n                <div *ngFor="let package of packageDetails">\n\n                    <ion-option [value]="package">{{package.wsuName}}</ion-option>\n\n                </div>\n\n            </ion-select>\n\n        </ion-item>\n\n    </div>\n\n    <!-- ??????????????? -->\n\n    <div *ngIf="IsShow==IsSkuSerial">\n\n        <ion-item>\n\n            <ion-label>????????????</ion-label>\n\n            <ion-input type="text" [(ngModel)]="sourceLocCode"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item *ngIf="sysparms.paramValue==0">\n\n            <ion-label>????????????</ion-label>\n\n            <ion-input type="text" [(ngModel)]="sourceLpnCode" (keyup)="sourceLpnCode_KeyDown($event)"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item>\n\n            <ion-label>????????????</ion-label>\n\n            <ion-input type="text" maxlength=\'100\' (keyup)="SkuSerial_KeyDown($event)" [(ngModel)]="serialNumberMode">\n\n            </ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item (click)="numclick()">\n\n            <ion-label>?????????</ion-label>\n\n            <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{ScanNumberLength}}\'>\n\n            </ion-input>\n\n            <span>0/20???</span>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n    </div>\n\n\n\n    <!-- ??????????????? -->\n\n    <div class="list_item_serial" *ngIf="IsShow===IsSkuSerialInfo">\n\n        <div class="list_item_title">\n\n            <span [ngStyle]="{\'border-bottom\':isTilebool === true ? \'3px solid #008080\' : \'0\' }"\n\n                (click)="isTitle(true)">???????????????(<b>{{ScanNumberLength}}</b>)</span>\n\n            <!-- <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n                (click)="isTitle(false)">???????????????(<b>{{InNumberLength}}</b>)</span> -->\n\n        </div>\n\n\n\n    </div>\n\n\n\n    <!-- ?????????????????? -->\n\n    <div *ngIf="NumberListFalag==1 && IsShow===IsSkuSerialInfo ">\n\n        <ion-list class="list_item_div">\n\n            <ion-scroll scrollY="true" style="height:35rem;">\n\n                <ion-item-sliding *ngFor="let item of serialNumber">\n\n                    <ion-item>\n\n                        <h2>{{item}}</h2>\n\n                    </ion-item>\n\n                    <ion-item-options side="right">\n\n                        <button (click)="removeItem(item)" ion-button color="danger">\n\n                            ??????\n\n                        </button>\n\n                    </ion-item-options>\n\n                </ion-item-sliding>\n\n            </ion-scroll>\n\n        </ion-list>\n\n    </div>\n\n\n\n    <!-- ??????????????? -->\n\n    <!-- <div *ngIf="NumberListFalag==2 && IsShow===IsSkuSerialInfo ">\n\n        <ion-list class="list_item_div">\n\n            <ion-scroll scrollY="true" style="height:12rem;">\n\n                <ion-item-sliding *ngFor="let item of Sn">\n\n                    <ion-item>\n\n                        <h2>{{item.snDetailCode}}</h2>\n\n                    </ion-item>\n\n                </ion-item-sliding>\n\n            </ion-scroll>\n\n        </ion-list>\n\n    </div> -->\n\n\n\n\n\n    <!-- ????????? -->\n\n    <div class="list_item" *ngIf="IsShow==IsSkuMove">\n\n        <ion-item>\n\n            <ion-label>???????????????</ion-label>\n\n            <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n        <ion-item>\n\n            <ion-label>???????????????</ion-label>\n\n            <ion-input [(ngModel)]="targetLpnCode" type="text"></ion-input>\n\n            <ion-avatar item-end>\n\n                <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n            </ion-avatar>\n\n        </ion-item>\n\n    </div>\n\n    <!-- ??????????????? -->\n\n    <div *ngIf="IsShow==IsSkuBathList">\n\n        <ion-item class="lot_number">\n\n            <ion-label>????????????</ion-label>\n\n            <ion-select [(ngModel)]="skuStockLotModel" interface="action-sheet" cancelText="??????">\n\n                <div *ngFor="let item of skuStockLotMoveVOs">\n\n                    <ion-option [value]="item">{{item.lotNumber}}</ion-option>\n\n                </div>\n\n            </ion-select>\n\n        </ion-item>\n\n        <div *ngFor="let item of skuStockLotModel.skuLotList;let i = index">\n\n            <ion-item *ngIf="item.skuLotDisp===1">\n\n                <ion-label>{{item.skuLotLabel}}???</ion-label>\n\n                <ion-input [disabled]=\'true\' type="text" [(ngModel)]="item.skuLotValue"></ion-input>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n        </div>\n\n        <ion-item *ngIf="IsSn==0;else elseBlock">\n\n            <ion-label>?????????</ion-label>\n\n            <ion-input type="number" [(ngModel)]="moveQty"></ion-input>\n\n            <ion-select [(ngModel)]="defaultPackageDetail" (ionChange)="togglePage()" [compareWith]="compareFn"\n\n                interface="action-sheet" cancelText="??????" okText="??????">\n\n                <div *ngFor="let package of packageDetails">\n\n                    <ion-option [value]="package">{{package.wsuName}}</ion-option>\n\n                </div>\n\n            </ion-select>\n\n        </ion-item>\n\n        <ng-template #elseBlock>\n\n            <ion-item>\n\n                <ion-label>?????????</ion-label>\n\n                <ion-input [disabled]=\'true\' type="text" readonly="readonly"\n\n                    value=\'{{ScanNumberLength + " /" +InNumberLength}}\'>\n\n                </ion-input>\n\n                <span>0/20???</span>\n\n                <ion-avatar item-end>\n\n                    <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n                </ion-avatar>\n\n            </ion-item>\n\n        </ng-template>\n\n    </div>\n\n</ion-content>\n\n<ion-footer>\n\n    <div class="btn-box">\n\n        <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n            ??????[Esc]\n\n        </button>\n\n        <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n            ??????[Ent]\n\n        </button>\n\n    </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesku\movesku.html"*/,
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

/***/ })

});
//# sourceMappingURL=43.js.map