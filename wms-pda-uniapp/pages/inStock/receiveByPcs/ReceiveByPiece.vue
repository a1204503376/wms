<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按件收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="物品" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuCode" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="名称" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuName" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="型号" :required="true" class="left-text-one-line" labelWidth="100">
				<uni-select v-model="params.skuLot2"></uni-select>
			</u-form-item>
			<u-form-item label="数量" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.surplusQty"></u--input>
				<!-- <u-number-box v-model="params.skuCode" @change="valChange"></u-number-box> -->
			</u-form-item>
			<u-form-item label="UOM" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.wsuCode" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="生产批次" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuLot1"></u--input>
			</u-form-item>
			<u-form-item label="箱码" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCode"></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="专用客户" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuLot4"></u--input>
			</u-form-item>
		</u--form>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-submit" @click="submit()">
				确定
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import receive from '@/api/inStock/receiveByPcs.js'
	import uniSelect from '@/components/uni-select.vue'
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {
			uniSelect
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					skuCode: undefined,
					skuName: undefined,
					skuLot2: undefined,
					skuLot4: undefined,
					surplusQty: undefined,
					wsuCode: undefined,
					skuLot1: undefined,
					boxCode: undefined,
					locCode: 'STAGE',
					isSn: ''
				},
				receiveDetailId: '',
				receiveId: '',
				receiveDetailList: [],
				locCode: '',
				boxCode: ''
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.receiveDetailId = parse.receiveDetailId;
			this.receiveId = parse.receiveId;
			this.getDetailByDetailId();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			this.params.locCode = 'STAGE';
		},
		onBackPress(event) {
			// #ifdef APP-PLUS
			if (event.from === 'backbutton') {
				this.esc();
				return true;
			}
			// #endif
		},
		methods: {
			getStockByBoxCode() {
				let params = {
					boxCode: this.params.boxCode,
				};
				receive.getStockByBoxCode(params).then(data => {
					if (tool.isEmpty(data.data[0])) {
						return;
					}
					this.params.locCode = data.data[0].locCode;
					this.locCode = data.data[0].locCode;
					this.boxCode = data.data[0].boxCode;
				})
			},
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.Loc:
						this.params.locCode = barcode.content;
						break;
					case barcodeType.Lpn:
						this.params.boxCode = barcode.content;
						this.getStockByBoxCode();
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			submit() {
				var _this = this;
				uni.$u.throttle(function() {
					var paramsData = {};
					paramsData = _this.params;
					paramsData.locCode = uni.$u.func.parseLocCode(paramsData.locCode);
					paramsData.receiveDetailId = _this.receiveDetailId;
					paramsData.receiveId = _this.receiveId;
					if (tool.isNotEmpty(_this.locCode) && paramsData.locCode != _this.locCode) {
						_this.$u.func.showToast({
							title: '该箱已在' + _this.locCode + ',收货时不能移动',
						});
						return;
					}
					//提交表单数据 收货
					paramsData.whCode = uni.getStorageSync('warehouse').whCode;
					paramsData.whId = uni.getStorageSync('warehouse').whId;
					if (tool.isNotEmpty(paramsData.skuLot2) && tool.isNotEmpty(paramsData.locCode) && tool
						.isNotEmpty(paramsData.boxCode) && tool.isNotEmpty(_this.params.boxCode) && tool
						.isNotEmpty(paramsData.skuLot1) && tool.isInteger(paramsData.surplusQty)) {
						if (_this.params.isSn) {
							uni.$u.func.routeNavigateTo('/pages/inStock/receiveByPcs/collectionSerialNumber', _this
								.params);
							return;
						}
						receive.submitReceiptByPcs(paramsData).then(data => {
							if (data.data.allReceivieIsAccomplish && data.data
								.currentReceivieIsAccomplish) {
								//当前收货单收货收货完毕
								_this.$u.func.showToast({
									title: '当前收货单收货收货完毕',
								});
								uni.navigateBack({
									delta: 2
								});
								return;
							} else if (data.data.currentReceivieIsAccomplish) {
								//当前收货单详情收货收货完毕
								_this.$u.func.showToast({
									title: '当前收货单详情收货收货完毕',
								});
								_this.esc();
								return;
							} else {
								//当前收货单详情收货部分收货,刷新当前页面
								_this.$u.func.showToast({
									title: '当前收货单详情部分收货',
								});
								_this.$u.func.refreshPage()
								return;
							}
						});
						_this.$u.func.refreshPage()
					} else {
						_this.$u.func.showToast({
							title: '收货失败,请输入必填字段或收货数量请输入正整数',
						});
						_this.params.locCode = 'STAGE'
					}
				}, 1000)

			},
			getDetailByDetailId() {
				let params = {
					receiveDetailId: this.receiveDetailId
				};
				receive.getDetailByDetailId(params).then(data => {
					this.params.skuCode = data.data.skuCode;
					this.params.skuLot2 = data.data.skuCode;
					this.params.skuName = data.data.skuName;
					this.params.surplusQty = data.data.surplusQty;
					this.params.wsuCode = data.data.wsuCode;
					this.params.skuLot1 = data.data.skuLot1;
					this.params.skuLot4 = data.data.skuLot4;
					this.params.boxCode = data.data.boxCode;
					this.params.isSn = data.data.isSn;
				})
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			scannerCallback(no) {
				this.analysisCode(no);
			}
		}
	}
</script>

<style>

</style>
