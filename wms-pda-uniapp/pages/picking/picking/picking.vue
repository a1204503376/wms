<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="拣货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="params">
			<u-search placeholder="请输入发货单编码/上游编码/任务号" v-model="params.no" :show-action="false"
				@custom="getReceiveDetailList" @search="getReceiveDetailList" class="font-in-page"
				style="margin: 12rpx">
			</u-search>
		</u--form>
		<!-- ${index + 1} -->
		<u-list style="height: 900rpx;" @scrolltolower="scrolltolower">
			<view v-for="(item, index) in receiveDetailList" :key="index" @click="clickItem(item)">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="10" class="left-text-one-line">
							<view class="demo-layout bg-purple-light font-in-page">{{item.soBillNo}}</view>
						</u-col>
						<u-col span="2">
							<view class="demo-layout bg-purple font-in-page">{{item.billTypeCd}}</view>
						</u-col>
					</u-row>
					<u-divider text=""></u-divider>
			</view>
		</u-list>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
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
	import picking from '@/api/picking/picking.js'
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
		onLoad: function(option) {
			// var parse = JSON.parse(option.param)
			// this.params.receiveId = parse.receiveId
			// this.getReceiveDetailList();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			var that = this;
			that.emitKeyDown = function(e) {
				if (e.key == 'Enter') {
					that.getReceiveDetailList();
				}
			};
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
			submit() {
				//TODO 等页面测试通过之后删除
				this.clearEmitKeyDown();
				uni.$u.func.routeNavigateTo('/pages/picking/picking/pickingByPcs');
			},
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
				this.clearEmitKeyDown();
				uni.$u.func.navigateBackTo(1);
			},
			getReceiveDetailList() {
				if (tool.isNotEmpty(this.params.no)) {
					this.analysisCode(this.params.no);
				}
				this.page.current = 1;
				this.params.whId = uni.getStorageSync('warehouse').whId;
				picking.findAllPickingByNo(this.params, this.page).then(data => {
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
				this.clearEmitKeyDown();
				uni.setStorageSync('soDetail', '');
				uni.$u.func.routeNavigateTo('/pages/picking/picking/pickingByPcs', row);
			},
			scannerCallback(no) {
				this.analysisCode(no);
				this.getReceiveDetailList();
			},
			clearEmitKeyDown() {
				this.emitKeyDown = function() {};
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.getReceiveDetailList();
				}
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
