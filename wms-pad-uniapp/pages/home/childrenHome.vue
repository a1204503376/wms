<template>
	<view class="container">
				<u-navbar  leftIcon="arrow-left" :leftIconSize="20" leftIconColor="#fff"
			@leftClick="closePage" :fixed="false"
			:autoBack="false" :title="title" :bgColor="navigationBarBackgroundColor"
			titleStyle="color:#ffffff;font-size:21px">
		</u-navbar>

		<u-grid class="menu" col="3">
			<u-grid-item class="menu-item" v-for="(menu, index2) in childrenMenu" :key="menu.code" @click="navTo(menu)">
				<text class="menu-number">{{index2+1}}</text>
				<text :class="menu.source" class="menu-icon"></text>
				<view class="menu-text">
					{{menu.name}}
				</view>
			</u-grid-item>
		</u-grid>
	</view>
</template>

<script>
	import api from '@/api/user.js'
	import setting from '@/common/setting'
	import tool from '@/utils/tool.js'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				swiperHeight: 0,
				current: 0,
				childrenMenu: uni.getStorageSync('childrenMenu'),
				username: this.$store.state.userName,
				title:''
			};
		},
		onReady() {
		
		},
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			this.title=option.title; 
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			navTo(menu) {
				//跳转页面
				uni.$u.func.route(menu.path+'?title='+menu.name);
			},
			closePage() {
				uni.$u.func.navigateBack();
			},
			scannerCallback(data) {
				this.username = data
			}
		}
	};
</script>

<style lang="scss">
	@import 'home.scss';
</style>
