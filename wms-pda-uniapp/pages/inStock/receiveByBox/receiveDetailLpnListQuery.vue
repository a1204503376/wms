<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="多箱收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left">
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.boxCode" @confirm="getReceiveDetailList"></u--input>
			</u-form-item>
		</u--form>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">未收货列表</h4>
		<u-list style="height: 500rpx;">
			<view style="margin-top: 5%;" v-for="(item, index) in detailLpnList">
				<u-row>
					<u-col span="5">
						<view class="demo-layout bg-purple-light">箱码:{{item.boxCode}}</view>
					</u-col>
					<u-col span="4">
						<view class="demo-layout bg-purple">总数:{{item.num}}</view>
					</u-col>
					<u-col span="3">
						<u-button text="删除" :hairline="true" @click="deleteItem(index)"></u-button>
					</u-col>
				</u-row>

				<view style="margin-top: 5%;" v-for="(items, indexs) in item.receiveDetailLpnItemDtoList">
					<u-row>
						<u-col span="5">
							<view class="demo-layout bg-purple-light" style="margin-left: 30rpx;">{{items.skuName}}
								{{items.skuSpec}}
							</view>
						</u-col>
						<u-col span="4">
							<view class="demo-layout bg-purple">批次:{{items.skuLot1}}</view>
						</u-col>
						<u-col span="3">
							<view class="demo-layout bg-purple">量:{{items.planQty}}</view>
						</u-col>
					</u-row>
				</view>
			</view>
	</view>
	<view class="footer">
		<view class="btn-cancle" @click="esc()">
			返回
		</view>
		<view class="btn-submit" @click="clickItem()">
			确定
		</view>
	</view>
	</view>
</template>

<script>
	import receive from '@/api/inStock/receiveByBox.js'
	import barCodeService from '@/utils/barcodeFunc.js'
	import setting from '@/common/setting'
	import tool from '@/utils/tool.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				param: {
					boxCode: '',
					num: 0,
				},
				detailLpnList: [],

			}
		},

		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			// this.param = {
			// 		boxCode: '',
			// 		num: 0,
			// 	},
			// 	this.detailLpnList = []
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
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			getReceiveDetailList() {
				if (tool.isNotEmpty(this.detailLpnList) && tool.isNotEmpty(this.param.boxCode) &&
					this.detailLpnList[0].boxCode.substring(0, 1) != this.param.boxCode.substring(0, 1)) {
					this.$u.func.showToast({
						title: '多箱收货只能收同种箱型的箱子'
					})
					return;
				}
				receive.getReceiveDetailLpn(this.param.boxCode).then(res => {
					let param = res.data
					let currentSku = this.detailLpnList.find(item => item.boxCode === param.boxCode);
					if (tool.isNotEmpty(currentSku)) {
						this.$u.func.showToast({
							title: '该箱码已存在,请勿重复扫描'
						})
						return
					}
					if (tool.isNotEmpty(this.detailLpnList) && this.detailLpnList[0].receiveDetailLpnItemDtoList[0]
						.skuSpec !=
					 param.receiveDetailLpnItemDtoList[0].skuSpec
					) {
						this.$u.func.showToast({
							title: '多箱收货只能收同种规格的物品'
						})
						return
					} else {
						this.detailLpnList.push(param);
					}
				})
			},
			deleteItem(index) {
				this.detailLpnList.splice(index, 1)
			},
			clickItem() {
				if (tool.isEmpty(this.detailLpnList)) {
					this.$u.func.showToast({
						title: '收货失败,箱码为空'
					})
					return
				}
				uni.$u.func.routeNavigateTo('/pages/inStock/receiveByBox/receiveByMultiBox', this.detailLpnList);
			},
			scannerCallback(no) {
				let item = barCodeService.parseBarcode(no)
				if (item.type == barCodeService.BarcodeType.UnKnow || item.type == barCodeService.BarcodeType.Lpn) {
					this.param.boxCode = item.content;
					this.getReceiveDetailList();
				} else {
					this.$u.func.showToast({
						title: '无法识别,不支持的条码类型'
					})
					return
				}

			},
		}
	}
</script>

<style>

</style>
