<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按件收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
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
				<u--input v-model="params.wsuCode" border="0" disabled></u--input>
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
					skuCode: undefined,
					skuName: undefined,
					skuLot2: undefined,
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
					_this.params.locCode = uni.$u.func.parseLocCode(_this.params.locCode);
					_this.params.receiveDetailId = _this.receiveDetailId;
					_this.params.receiveId = _this.receiveId;
					if (tool.isNotEmpty(_this.locCode) && _this.params.locCode != _this.locCode) {
						_this.$u.func.showToast({
							title: '该箱已在' + _this.locCode + ',收货时不能移动',
						});
						return;
					}
					if (_this.params.isSn) {
						uni.$u.func.route('/pages/inStock/receiveByPcs/collectionSerialNumber', _this.params);
						return;
					}
					//提交表单数据 收货
					_this.params.whCode = uni.getStorageSync('warehouse').whCode;
					_this.params.whId = uni.getStorageSync('warehouse').whId;
					receive.submitReceiptByPcs(_this.params).then(data => {
						if (data.data.allReceivieIsAccomplish && data.data.currentReceivieIsAccomplish) {
							//当前收货单收货收货完毕
							_this.$u.func.route('/pages/inStock/receiveByPcs/receiptHeaderEnquiry');
							return;
						} else if (data.data.currentReceivieIsAccomplish) {
							//当前收货单详情收货收货完毕
							_this.$u.func.route(
								'/pages/inStock/receiveByPcs/receiptDetailEnquiry', {
									receiveId: _this.receiveId
								});
							return;
						} else {
							//当前收货单详情收货部分收货,刷新当前页面
							_this.$u.func.refreshPage()
							return;
						}
					});
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
				uni.$u.func.route('/pages/inStock/receiveByPcs/receiptDetailEnquiry', {
					receiveId: this.receiveId
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
