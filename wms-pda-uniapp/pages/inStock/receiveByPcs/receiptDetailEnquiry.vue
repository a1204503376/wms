<template>
	<view>
		<u-navbar  leftIconColor="#fff"
			@leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按件收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="params">
			<u-form-item label="物品" borderBottom class="left-text-one-line font-in-page" labelWidth="100">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
		</u--form>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">未收货列表</h4>
		<u-divider text=""></u-divider>
		<!-- ${index + 1} -->
		<u-list style="height: 960rpx;">
			<u-list-item v-for="(item, index) in receiveDetailList" :key="index">
				<view @click="clickItem(item)">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="10" class="left-text-one-line">
							<view class="demo-layout bg-purple-light font-in-page">{{index+1}}-{{item.skuCode}}</view>
						</u-col>
						<u-col span="2">
							<view class="demo-layout bg-purple font-in-page">{{item.surplusQty}}</view>
						</u-col>
					</u-row>
					<u-divider text=""></u-divider>
				</view>
			</u-list-item>
		</u-list>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import receive from '@/api/inStock/receiveByPcs.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					receiveId: '',
					skuCode: '',
				},
				receiveDetailList: []
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params.receiveId = parse.receiveId
			this.getReceiveDetailList();
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
					case barcodeType.UnKnow:
						this.params.skuCode = barcode.content;
						break;
					case barcodeType.Sku:
						this.params.skuCode = barcode.content;
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			esc() {
				uni.$u.func.route('/pages/inStock/receiveByPcs/receiptHeaderEnquiry');
			},
			getReceiveDetailList() {
				if(tool.isNotEmpty(this.params.skuCode)){
					this.analysisCode(this.params.skuCode);
				}
				receive.getReceiveDetailList(this.params).then(data => {
					this.receiveDetailList = data.data;
					if(data.data.length==1){
						data.data[0].receiveId=this.params.receiveId;
						uni.$u.func.route('/pages/inStock/receiveByPcs/ReceiveByPiece', data.data[0]);
					}
				})
			},
			clickItem(row) {
				row.receiveId=this.params.receiveId;
				uni.$u.func.route('/pages/inStock/receiveByPcs/ReceiveByPiece', row);
			},
			scannerCallback(no) {
				this.analysisCode(no);
				this.getReceiveDetailList();
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.getReceiveDetailList();
				}
			}
		}
	}
</script>

<style>

</style>
