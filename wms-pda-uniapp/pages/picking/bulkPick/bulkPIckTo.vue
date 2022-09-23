<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="零散拣货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form v-if="formDisabled">
			<u-form-item label="箱码" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCode" disabled></u--input>
			</u-form-item>
			<u-form-item label="物品" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuCode" disabled></u--input>
			</u-form-item>
			<u-form-item label="批次" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuLot1" disabled></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.locCode" disabled></u--input>
			</u-form-item>
			<u-form-item label="数量" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.qty"></u--input>
			</u-form-item>
		</u--form>
		<u--form v-if="!formDisabled">
			<u-form-item label="箱码" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCode"></u--input>
			</u-form-item>
			<u-form-item label="物品" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="批次" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuLot1"></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="数量" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.qty"></u--input>
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
	import barcodeFunc from '@/utils/barcodeFunc.js'
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
					whId: uni.getStorageSync('warehouse').whId
				},
				defaultParams: {
					soBillId: undefined,
					boxCode: undefined,
					whId: uni.getStorageSync('warehouse').whId
				},
				soBillId: '',
				formDisabled: false
			}
		},
		onLoad: function(option) {
			var soDetail = JSON.parse(option.param)
			this.defaultParams.soBillId = soDetail.soBillId;
			this.defaultParams.boxCode = soDetail.boxCode;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			this.getPickPlanBySoBillIdAndBoxCode();
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
			getPickPlanBySoBillIdAndBoxCode() {
				pick.getPickPlanBySoBillIdAndBoxCode(this.defaultParams).then(data => {
					if (data.data.length > 0) {
						this.formDisabled = true;
						this.params = data.data[0];
						this.params.skuLot1 = data.data[0].lotNumber;
						this.params.qty = data.data[0].surplusQty;
					} else {
						this.formDisabled = false;
						this.params.boxCode = this.defaultParams.boxCode;
						this.params.soBillId = this.defaultParams.soBillId;
					}
				});
			},
			getPickPlan() {
				pick.getPickPlanBySoBillIdAndBoxCode(this.defaultParams).then(data => {
					if (data.data.length > 0) {
						this.formDisabled = true;
						this.params = data.data[0];
						this.params.skuLot1 = data.data[0].lotNumber;
						this.params.qty = data.data[0].surplusQty;
					} else {
						this.formDisabled = false;
						this.params.boxCode = this.defaultParams.boxCode;
						this.params.soBillId = this.defaultParams.soBillId;
						uni.navigateBack({
							delta: 2
						});
					}
				});
			},
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
						if (tool.isNotEmpty(_this.params.skuCode) &&
							tool.isNotEmpty(_this.params.skuLot1) &&
							tool.isNotEmpty(_this.params.boxCode) &&
							tool.isNotEmpty(_this.params.locCode) &&
							tool.isNotEmpty(_this.params.qty) &&
							tool.isInteger(_this.params.qty)
						) {
							if (data.data) {
								uni.$u.func.routeNavigateTo(
									'/pages/picking/bulkPick/bulkPickSerialNumber',
									_this
									.params);
								return;
							} else {
								// _this.params.soBillId = _this.defaultParams.soBillId;
								pick.bulkPick(_this.params).then(data => {
									_this.$u.func.showToast({
										title: '拣货完成'
									});
									_this.getPickPlan();

								});
							}
						} else {
							_this.$u.func.showToast({
								title: '请按照要求输入必填属性'
							});
						}

					})

				}, 1000)

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
