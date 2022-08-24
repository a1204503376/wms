<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false" rightIcon="order"
			rightIconColor="#fff" @rightClick="gotoDetails" :bgColor="navigationBarBackgroundColor" title="按件拣货"
			titleStyle="color:#ffffff;font-size:21px" style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="物品" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="批次" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.skuLot1"></u--input>
			</u-form-item>
			<u-form-item label="箱码" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.boxCode"></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="数量" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.qty"></u--input>
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
	import pick from '@/api/picking/picking.js'
	import uniSelect from '@/components/uni-select.vue'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	import picking from '@/api/picking/picking.js'
	import sku from '@/api/sku.js';

	export default {
		components: {
			uniSelect
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					skuCode: undefined,
					skuLot1: undefined,
					boxCode: undefined,
					locCode: undefined,
					isSn: undefined,
					qty: undefined,
					billTypeCd: undefined,
					soBillId: undefined,
					soBillNo: undefined,
					soDetailId: undefined,
					whId: uni.getStorageSync('warehouse').whId
				},
				soBillId: ''
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.soBillId = parse.soBillId;
			this.params.soBillNo = parse.soBillNo;
			this.params.soBillId = parse.soBillId;
			this.params.billTypeName = parse.billTypeName;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			this.params.skuCode = undefined;
			this.params.skuLot1 = undefined;
			this.params.boxCode = undefined;
			this.params.locCode = undefined;
			this.params.qty = undefined;
			uni.$u.func.registerScanner(this.scannerCallback);
			var soDetail = uni.getStorageSync('soDetail');
			if (tool.isNotEmpty(soDetail)) {
				this.params.skuCode = soDetail.skuCode;
				this.params.qty = soDetail.surplusQty;
				this.params.skuLot1 = soDetail.skuLot1;
				this.params.soDetailId = soDetail.soDetailId;
			}
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
				_this.params.isSn = true;
				uni.$u.throttle(function() {
					let params = {}
					params.skuCode = _this.params.skuCode;
					sku.findSkuIsSnBySkuCode(params).then(data => {
						if (data.data) {
							uni.$u.func.routeNavigateTo('/pages/picking/picking/pickingSerialNumber', _this
								.params);
							return;
						} else {
							pick.pickByPcs(_this.params).then(data => {
								_this.$u.func.showToast({
									title: '拣货完成'
								});
								if (data.data) {
									this.esc();
								} else {
									this.params.skuCode = undefined;
									this.params.skuLot1 = undefined;
									this.params.boxCode = undefined;
									this.params.locCode = undefined;
									this.params.qty = undefined;
								}
							});
						}
					})

				}, 1000)

			},
			getDetailByDetailId() {
				let params = {
					receiveDetailId: this.receiveDetailId
				};
				receive.getDetailByDetailId(params).then(data => {
					this.params.skuCode = data.data.skuCode;
					this.params.skuName = data.data.skuName;
					this.params.surplusQty = data.data.surplusQty;
					this.params.wsuCode = data.data.wsuCode;
					this.params.skuLot1 = data.data.skuLot1;
					this.params.boxCode = data.data.boxCode;
					this.params.isSn = data.data.isSn;
				})
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			gotoDetails() {
				uni.$u.func.routeNavigateTo('/pages/picking/picking/pickingDetails', this.params);
			},
			scannerCallback(no) {
				this.analysisCode(no);
			}
		}
	}
</script>

<style>

</style>
