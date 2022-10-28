<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="出库复核" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="params">
			<u-search placeholder="请输入发货单编码" v-model.trim="params.no" :show-action="false" @custom="getSoBillList"
				@search="getSoBillList" class="font-in-page" style="margin: 12rpx">
			</u-search>
		</u--form>
		<!-- ${index + 1} -->
		<u-list style="height: 900rpx;" @scrolltolower="scrolltolower">
			<view v-for="(item, index) in receiveDetailList" :key="index" @click="clickItem(item)">
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="8" class="left-text-one-line">
						<view class="demo-layout bg-purple-light font-in-page">{{item.soBillNo}}</view>
					</u-col>
					<u-col span="4">
						<view class="demo-layout bg-purple font-in-page">{{item.billTypeName}}</view>
					</u-col>
				</u-row>
				<u-divider text=""></u-divider>
			</view>
		</u-list>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import picking from '@/api/picking/picking.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {

		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					receiveId: '',
					no: '',
				},
				page: {
					total: 0,
					size: 9,
					current: 1,
					ascs: "", //正序字段集合
					descs: "", //倒序字段集合
				},
				receiveDetailList: [],
				status: 'loadmore',
				loadmore: false,
				noData: false,
			}
		},
		// 只加载一次，监听页面加载
		onLoad: function(option) {},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		// 页面每次出现在屏幕上都触发，包括从下级页面点返回露出当前页面
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			var that = this;
			if (tool.isNotEmpty(that.params.no)) {
				that.getSoBillList();
			}
		},
		// 返回
		onBackPress(event) {
			// #ifdef APP-PLUS
			if (event.from === 'backbutton') {
				this.esc();
				return true;
			}
			// #endif
		},
		methods: {
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.params.no = barcode.content;
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			getSoBillList() {
				if (tool.isNotEmpty(this.params.no)) {
					this.analysisCode(this.params.no);
				}
				this.page.current = 1;
				this.params.whId = uni.getStorageSync('warehouse').whId;
				picking.outStockCheckoutFindSoBill(this.params, this.page)
					.then(data => {
						if (data.data.records.length > 0) {
							this.status = 'loading';
							this.loadmore = true;
							this.noData = false;
						} else {
							this.status = 'nomore';
							this.loadmore = false;
							this.noData = true;
						}
						this.receiveDetailList = data.data.records;
						if (this.receiveDetailList.length < this.page.size) {
							this.loadmore = false;
						}
					})
			},
			clickItem(row) {
				console.log(row)
				uni.$u.func.routeNavigateTo('/pages/picking/review/outstockCheckout', row);
			},
			scannerCallback(no) {
				this.analysisCode(no);
				this.getSoBillList();
			},
			scrolltolower() {
				this.loading = false;
				this.divider = false;
				this.page.current++;
				this.params.whId = uni.getStorageSync('warehouse').whId;
				picking.findAllPickingByNo(this.params, this.page).then(data => {
					if (data.data.records.length > 0) {
						this.status = 'loading';
						data.data.records.forEach((item, index) => { //js遍历数组
							this.receiveDetailList.push(item) //push() 方法可向数组的末尾添加一个或多个元素，并返回新的长度。
						});
					} else {
						this.status = 'nomore';
					}

				})
			}
		}
	}
</script>

<style>

</style>
