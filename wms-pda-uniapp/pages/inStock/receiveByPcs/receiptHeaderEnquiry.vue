<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按件收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<template>
			<u-search placeholder="请输入收货单编码/上游编码" v-model="params.no" :show-action="false" @custom="search"
				@search="search" class="font-in-page" style="margin: 12rpx">
			</u-search>
		</template>
		<u-divider text="" style="margin-top:0rpx;"></u-divider>
		<u-divider text="暂无数据" v-if="noData"></u-divider>
		<u-list style="height: 950rpx;"  @scrolltolower="scrolltolower">
			<u-list-item v-for="(item, index) in receiveList" :key="item.receiveNo">
				<view @click="clickItem(item)">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="6" class="left-text-one-line font-in-page">
							<u--text class="demo-layout bg-purple-light" v-text="item.receiveNo"></u--text>
						</u-col>
						<u-col span="6">
							<u--text class="demo-layout bg-purple  font-in-page" v-text="item.billTypeName"></u--text>
						</u-col>
					</u-row>
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="12" class="left-text-one-line">
							<u--text class="demo-layout bg-purple font-in-page" v-text="item.supplierName"></u--text>
						</u-col>
					</u-row>
					<u-divider text=""></u-divider>
				</view>
			</u-list-item>
			<u-loadmore :status="status" v-if="loadmore" />
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
	import receive from '@/api/inStock/receiveByPcs.js'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					no: '',
				},
				receiveList: [],
				page: {
					total: 0,
					size: 7,
					current: 1,
					ascs: "", //正序字段集合
					descs: "", //倒序字段集合
				},
				status: 'loadmore',
				loadmore: false,
				noData: false,
			}
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
				uni.$u.func.navigateBackTo(1);
			},
			getReceiveList() {
				this.noData = false;
				this.receiveList = [];
				this.loadmore = true;
				this.status = 'loading';
				this.page.current = 1;
				this.analysisCode(this.params.no);
				this.params.whId = uni.getStorageSync('warehouse').whId;
				receive.getReceiveList(this.params, this.page).then(data => {
					if (data.data.records.length > 0) {
						this.status = 'loading';
						this.loadmore = true;
						this.noData = false;
					} else {
						this.loadmore = false;
						this.noData = true;
					}
					this.receiveList = data.data.records;
					if (this.receiveList.length < 7) {
						this.loadmore = false;
					}
				})
			},
			search() {
				uni.$u.throttle(this.getReceiveList(), 1000)
			},
			clickItem(item) {
				uni.$u.func.routeNavigateTo('/pages/inStock/receiveByPcs/receiptDetailEnquiry', item);
			},
			scannerCallback(no) {
				this.analysisCode(no);
				this.search();
			},
			scrolltolower() {
				this.loading = false;
				this.divider = false;
				this.page.current++;
				this.params.whId = uni.getStorageSync('warehouse').whId;
				receive.getReceiveList(this.params, this.page).then(data => {
					if (data.data.records.length > 0) {
						this.status = 'loading';
						data.data.records.forEach((item, index) => { //js遍历数组
							this.receiveList.push(item) //push() 方法可向数组的末尾添加一个或多个元素，并返回新的长度。
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
