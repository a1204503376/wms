<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="接驳区移动" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="目标LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.targetLocCode"></u--input>
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
	import pick from '@/api/picking/connectionAreaPicking.js'
	import uniSelect from '@/components/uni-select.vue'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {
			uniSelect
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					sourceLocId: undefined,
					sourceLocCode: '',
					targetLocCode: '',
					whId: uni.getStorageSync('warehouse').whId
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
			this.params.sourceLocId = parse.locId;
			this.params.sourceLocCode = parse.locCode;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
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
						this.params.targetLocCode = barcode.content;
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
					if(_this.params.targetLocCode){
						pick.connectionAreaMove(_this.params).then(data => {
							console.log(data.data)
							_this.$u.func.showToast({
								title: '移动成功'
							});
						})
					}
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
			scannerCallback(no) {
				this.analysisCode(no);
			}
		}
	}
</script>

<style>

</style>
