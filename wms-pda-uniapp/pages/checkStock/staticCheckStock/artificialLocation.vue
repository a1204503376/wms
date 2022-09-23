<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="差异处理(零散库区)" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u-divider text="" style="margin-top:0rpx;"></u-divider>
		<u-divider text="暂无数据" v-if="noData"></u-divider>
		<u-list style="height: 950rpx;" @scrolltolower="scrolltolower">
			<u-row customStyle="margin-bottom: 10px">
				<u-col span="2" class="left-text-one-line font-in-page">
					<u--text class="demo-layout bg-purple-light" v-text="'箱号'"></u--text>
				</u-col>
				<u-col span="10">
					<u-input v-model.trim="params.boxCode"></u-input>
				</u-col>
			</u-row>
			<u-row>
				<u-col span="2" class="left-text-one-line font-in-page">
					<view>
						<u--text class="demo-layout bg-purple-light" v-text="'库位'"></u--text>
					</view>
				</u-col>
				<u-col span="10">
					<u-input v-model.trim="params.locCode"></u-input>
				</u-col>
			</u-row>
			<view v-for="(item, index) in receiveList.pdaBoxQtyResponseList" :key="index">
				<u-row customStyle="margin-bottom: 10px">
					<u-col span="1" class="left-text-one-line font-in-page">
						<u-icon name="checkbox-mark" color="green" v-if="item.isValid"></u-icon>
					</u-col>
					<u-col span="7">
						<u--text class="demo-layout bg-purple  font-in-page"
							v-text="item.boxCode+' ('+item.totalQty+')'"></u--text>
					</u-col>
					<u-col span="2">
						<u-button type="error" :plain="true" text="修改" @click="updateLocQty(item)" v-if="item.isButton">
						</u-button>
					</u-col>
					<u-col span="2">
						<u-button type="success" :plain="true" text="无误" @click="updateStatesIsDiff(item,'success')">
						</u-button>
					</u-col>
				</u-row>
				<u-divider text=""></u-divider>
			</view>
			<!-- 	<u-loadmore :status="status" v-if="loadmore" /> -->
		</u-list>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-cancle" @click="submit()">
				提交
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import staticCheckStock from '@/api/checkStock/staticCheckStock.js'
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					locCode: '',
					boxCode: ''
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
				title: '开始盘点',
				defaultList: [], //差异的数据 
			}
		},
		onLoad: function(option) {
			uni.setStorageSync('isListAsDefault', '')
			uni.setStorageSync('defaultRow', '')
			var parse = JSON.parse(option.param);
			this.receiveList = parse;
			this.params.locCode = parse.locCode;
			this.params.boxCode = parse.boxCode;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			let isListAsDefault = uni.getStorageSync('isListAsDefault')
			let defaultRow = uni.getStorageSync('defaultRow');
			if (tool.isNotEmpty(isListAsDefault)) {
				if (tool.isNotEmpty(isListAsDefault.differDefaultList)) {
					let stockBalance = 0;
					isListAsDefault.differDefaultList.forEach((item, index) => { //js遍历数组
						stockBalance = Number(stockBalance) + Number(item.stockBalance)
						this.defaultList.push(item);
					});
					this.receiveList.pdaBoxQtyResponseList.forEach((item, index) => { //js遍历数组
						if (item.boxCode == defaultRow.boxCode) {
							item.totalQty = stockBalance;
							item.isValid = true;
							item.isButton = false;
						}
					});
				}
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
			submit() {
				if (this.isValid()) {
					//调用生成从盘点记录
					let params = {}
					params.countReportList = this.defaultList
					staticCheckStock.generateCountReport(params).then(data => {
						uni.navigateBack({
							delta: 2
						});
					})
				} else {
					this.$u.func.showToast({
						title: '请确认当前库存的差异状况，无误请点击无误，有差异请修改差异',
					});
				}
			},
			isValid() {
				for (let i = 0; i < this.receiveList.pdaBoxQtyResponseList.length; i++) {
					if (this.receiveList.pdaBoxQtyResponseList[i].isValid == false) {
						return false;
					}
				}
				return true;
			},
			updateStatesIsDiff(item, bool) {
				if (bool == 'success') {
					item.isValid = true;
					this.defaultList.push(item)
				} else {
					item.isValid = false;
				}
			},
			updateLocQty(row) {
				uni.setStorageSync('defaultRow', row)
				uni.$u.func.routeNavigateTo('/pages/checkStock/staticCheckStock/updateLocQty', row);
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			search() {
				uni.$u.throttle(this.getReceiveList(), 1000)
			},
			scannerCallback(no) {
				this.search();
			}
		}
	}
</script>

<style>

</style>
