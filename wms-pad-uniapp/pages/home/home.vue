<template>
	<view class="container">
		<u-navbar :leftText="username" leftIcon="account-fill" :leftIconSize="40" leftIconColor="#fff"
			@leftClick="userSetting" rightIcon="close" :rightIconSize="40" @rightClick="goOut" :fixed="false"
			:autoBack="false" title="主页" :bgColor="navigationBarBackgroundColor"
			titleStyle="color:#ffffff;font-size:21px">
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
	import setting from '@/common/setting'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				swiperHeight: 0,
				current: 0,
				menuList: [],
				username: uni.getStorageSync('username')
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
			}
		}
	};
</script>

<style lang="scss">
	@import 'home.scss';
</style>
