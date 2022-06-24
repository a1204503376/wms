<template>
	<view>
		<u--form>
			<u-form-item label="物品" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.skuCode" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="名称" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.skuName" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="型号" class="left-text-one-line" labelWidth="100">
				<uni-select v-model="params.skuLot2"></uni-select>
			</u-form-item>
			<u-form-item label="数量" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.surplusQty"></u--input>
				<!-- <u-number-box v-model="params.skuCode" @change="valChange"></u-number-box> -->
			</u-form-item>
			<u-form-item label="UOM" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.umName" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="生产批次" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.skuLot1"></u--input>
			</u-form-item>
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.boxCode"></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.locCode"></u--input>
			</u-form-item>
		</u--form>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-submit" @click="submit()" :throttleTime="1000">
				确定
			</view>
		</view>
	</view>
</template>

<script>
	import receive from '@/api/inStock/receiveByPcs.js'
	import uniSelect from '@/components/uni-select.vue'
	import barcodeFunc from '@/common/barcodeFunc.js'
	export default {
		components: {
			uniSelect
		},
		data() {
			return {
				params: {
					skuCode: undefined,
					skuName: undefined,
					skuLot2: undefined,
					surplusQty: undefined,
					umName: undefined,
					skuLot1: undefined,
					boxCode: undefined,
					locCode: 'STAGE',
				},
				receiveDetailId: '',
				receiveDetailList: [],
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.receiveDetailId = parse.receiveDetailId;
			this.getDetailByDetailId();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.Loc:
						this.params.locCode = barcode.content;
						break;
					case barcodeType.Lpn:
						this.params.boxCode = barcode.content;
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			submit() {
				uni.$u.throttle(function() {
					this.params.locCode = uni.getStorageSync('warehouse').whCode + this.params.locCode;
					if (this.params.isSn == 1) {
						uni.$u.func.route('/pages/inStock/receiveByPcs/receiptDetailEnquiry', this.params);
						return;
					}
					console.log(this.params)
					//提交表单数据 收货
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
					this.params.umName = data.data.umName;
					this.params.skuLot1 = data.data.skuLot1;
					this.params.boxCode = data.data.boxCode;
				})
			},
			esc() {
				this.$u.func.navigateBack();
			},
			scannerCallback(no) {
               this.analysisCode(no);
			}
		}
	}
</script>

<style>

</style>
