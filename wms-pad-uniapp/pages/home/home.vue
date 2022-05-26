<template>
	<view class="container">
		<u-navbar :leftText="username" leftIcon="account-fill" :leftIconSize="40" @leftClick="userSetting"
			rightIcon="setting-fill" :rightIconSize="80" @rightClick="goOut" :fixed="false" :autoBack="false" title="主页"
			:bgColor="bgcolor">
			<!-- <image slot="right" src="/static/images/home/message.png" class="message-icon" mode="widthFix"></image> -->
		</u-navbar>

		<u-grid class="menu" col="3">
			<u-grid-item class="menu-item" v-for="(menu, index2) in menuList" :key="menu.code"
				@click="navTo(menu.path)">
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
	export default {
		data() {
			return {
				swiperHeight: 0,
				current: 0,
				menuList: [],
				username: uni.getStorageSync('username'),
				backgroundColor: uni.getStorageSync('backgroundColor') || '#14b9c8',
				bgcolor: vuex_theme
			};
		},
		onReady() {
			let that = this;
			uni.getSystemInfo({
				success(e) {
					let {
						windowWidth,
						windowHeight,
						safeArea
					} = e;
					const query = uni.createSelectorQuery().in(that);
					query
						.select('#swiperBox')
						.boundingClientRect(data => {
							that.swiperHeight = safeArea.bottom - data.top;
						})
						.exec();
				}
			});
		},
		onLoad() {
			uni.$u.func.registerScanner(this.scannerCallback);

			plus.navigator.setStatusBarBackground("#4CD964");
			plus.key.addEventListener('keydown', function(KeyEvent) {
				this.$u.func.showToast({
					title: "按下了键：" + JSON.stringify(KeyEvent),
				})
				this.$u.func.showToast({
					title: "按下了键：" + KeyEvent.keyCode,
				})
			});
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			this.menuList = uni.getStorageSync('menuList');
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			navTo(url) {
				uni.$u.func.route(url);
			},
			userSetting() {
				uni.$u.func.route('/pages/userSetting/userSetting');
			},
			goOut() {
				uni.$u.func.logout();
			},
			scannerCallback(data) {
				this.username = data
			},
			tabsChange(e) {
				this.current = e.detail.current;
			},
			change(index) {
				this.current = index;
			}
		}
	};
</script>

<style lang="scss">
	@import 'home.scss';
</style>
