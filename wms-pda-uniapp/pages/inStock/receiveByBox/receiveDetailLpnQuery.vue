<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按箱收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left">
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.boxCode" @confirm="getReceiveDetailList"></u--input>
			</u-form-item>
		</u--form>

		<view style="margin-top: 5%;" v-if="this.param.num !=0" @click="clickItem">
			<u-row customStyle="margin-bottom: 10px">
				<u-col span="6">
					<view class="demo-layout bg-purple-light">箱码:{{param.boxCode}}</view>
				</u-col>
				<u-col span="6">
					<view class="demo-layout bg-purple">总数:{{param.num}}</view>
				</u-col>
			</u-row>
		</view>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import receive from '@/api/inStock/receiveByBox.js'
	import barCodeService from '@/common/barcodeFunc.js'
	import setting from '@/common/setting'
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

			}
		},

		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			this.param = {
				boxCode: '',
				num: 0,
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
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			getReceiveDetailList() {
				receive.getReceiveDetailLpn(this.param.boxCode).then(res => {
					this.param = res.data
					if (this.param.num != 0) {
						this.clickItem()
					}
				})
			},
			clickItem() {
				uni.$u.func.routeNavigateTo('/pages/inStock/receiveByBox/receiveByBox', this.param);
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
