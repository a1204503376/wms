<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="接驳区拣货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u-divider></u-divider>
		<u--form labelPosition="left">
			<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">出库接驳库位</h4>
		</u--form>

		<view style="margin-top: 5%;">
			<u-row>
				<template v-for="(item, index) in locList">
					<u-col span="3" v-if="item.isEmpty">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" @click="change(item)">
							{{item.locCodeView}}
						</u-button>
					</u-col>
					<u-col span="3" v-if="!item.isEmpty">
						<u-button style="height: 60px;width: 95%;font-size: 40rpx;" disabled>{{item.locCodeView}}
						</u-button>
					</u-col>
				</template>
			</u-row>
			<u-divider></u-divider>
		</view>

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
	import barCodeService from '@/common/barcodeFunc.js'
	import setting from '@/common/setting'
	import tool from '@/utils/tool.js'
	export default {
		components: {
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				param: {
					lpnType: '',
					whId: 0,
				},
				lpnItem: '',
				locCode: '',
				locList: []
			}
		},
		onLoad: function(option) {

			// var parse = JSON.parse(option.param)
			// this.lpnItem = parse
			// this.param.lpnType = this.lpnItem.lpnType
			// this.param.whId = uni.getStorageSync('warehouse').whId
			// putWay.findLocByLpnType(this.param).then(res => {
			// 	this.locList = res.data
			// })
			// this.lpnItem['locId'] = 0
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onBackPress(event) {
			// #ifdef APP-PLUS
			if (event.from === 'backbutton') {
				this.esc();
				return true;
			}
			// #endif
		},

		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			change(item) {
				this.locCode = item.locCode
				this.lpnItem.locId = item.locId
			},

			submit() {
				this.$u.func.showToast({
					title: '接驳区拣货成功'
				})
				uni.$u.func.routeNavigateTo('/pages/picking/connectionAreaPicking/connectionAreaMove');
			},
			scannerCallback(no) {
				let item = barCodeService.parseBarcode(no)
				if (item.type == barCodeService.BarcodeType.Loc) {
					let param = this.locList.find(u => u.locCode === item.content);
					if (tool.isEmpty(param)) {
						this.$u.func.showToast({
							title: '扫描错误,库位信息不符'
						})
					}
					if (tool.isNotEmpty(param)) {
						this.locCode = item.content;
					}
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
