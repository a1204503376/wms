<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="选择" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px;">
		</u-navbar>
		<view style="margin-top: 5%;" v-for="(item, index) in stockList">
			<template v-for="(itemOne,indexOne) in item.boxList">
				<u-row @click="clickItem(index)" customStyle="margin-left: 5%;">
					<u-col span="8">
						<view class="demo-layout bg-purple-light">{{itemOne.boxCode}}</view>
					</u-col>
					<u-col span="4" v-if="indexOne===0">
						<view class="demo-layout bg-purple-light">总数：{{item.qty}}</view>
					</u-col>
				</u-row>
			</template>
			<u-divider></u-divider>
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
				stockList: []

			}
		},
		onLoad: function(option) {

			var parse = JSON.parse(option.param)
			this.stockList = parse
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


			clickItem(index) {
				if(this.stockList[index].lpnType==='0'){
					this.$u.func.showToast({
						title: '箱码类型错误，请重新选择'
					})
					return
				}
				uni.$u.func.routeNavigateTo('/pages/inStock/callAgv/callAgv',this.stockList[index]);
			},


		}
	}
</script>

<style>

</style>
