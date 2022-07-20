<template>
	<view class="container">
		<u-navbar leftIcon="arrow-left" :leftIconSize="20" leftIconColor="#fff" @leftClick="closePage" :fixed="false"
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
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
	</view>
</template>

<script>
	import api from '@/api/user.js'
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
				swiperHeight: 0,
				current: 0,
				childrenMenu: uni.getStorageSync('childrenMenu'),
				username: this.$store.state.userName,
				title: ''
			};
		},
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			var parse = JSON.parse(option.param)
			this.title = parse.name;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			var that = this;
			that.emitKeyDown = function(e) {
				if (e.key >= 1 && e.key <= 9) {
					that.navTo(that.childrenMenu[e.key - 1])
				}
			};
		},
		onBackPress(event) {
			// #ifdef APP-PLUS
			if (event.from === 'backbutton') {
				this.closePage();
				return true;
			}
			// #endif
		},
		methods: {
			clearEmitKeyDown() {
				this.emitKeyDown = function(){};
			},
			navTo(menu) {
				this.clearEmitKeyDown();
				//跳转页面
				uni.$u.func.routeNavigateTo(menu.path);
			},
			closePage() {
				this.clearEmitKeyDown();
				uni.$u.func.navigateBackTo(1);
			},
			scannerCallback(data) {
				this.username = data
			},
			emitKeyDown(e) {
				if (e.key >= 1 && e.key <= 9) {
					this.navTo(this.childrenMenu[e.key - 1])
				}
			}
		}
	};
</script>

<style lang="scss">
	@import 'home.scss';
</style>
