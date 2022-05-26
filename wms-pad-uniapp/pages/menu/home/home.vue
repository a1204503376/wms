<template>
	<view class="container">
			<u-navbar :leftText="username" leftIcon="account-fill" :leftIconSize="40" @leftClick="userSetting"
				rightIcon="setting-fill" :rightIconSize="80" @rightClick="goOut" :fixed="false" :autoBack="false"
				title="主页" :bgColor="bgcolor">
				<!-- <image slot="right" src="/static/images/home/message.png" class="message-icon" mode="widthFix"></image> -->
			</u-navbar>
		<swiper id="swiperBox" :style="{ height: swiperHeight + 'px' }" :current="current" @change="tabsChange">
			<swiper-item class="swiper-item">
				<scroll-view scroll-y style="width: 100%;height: 100%;" @scrolltolower="onreachBottom">
					<view class="content">
						<u-grid :col="3" :border="true">
							<u-grid-item v-for="(_item, _index) in menuList" :key="_index"
								:customStyle="{height:220+'rpx'}">
								<navigator :url=" _item.path" hover-class="none" class="gitem">
									<u-icon :name="_item.source" size="60"></u-icon>
									<view class="name">
										<h2>{{ _item.name }}【{{_index+1}}】</h2>
									</view>
								</navigator>
							</u-grid-item>
						</u-grid>
					</view>
				</scroll-view>
			</swiper-item>
		</swiper>
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
	.container {
		min-height: 100vh;
		overflow: hidden;
	}
	
	.swiper-box {
		height: 400vh;
	}

	.message-icon {
		width: 32rpx;
		height: auto;
		margin-right: 27rpx;
	}

	.tab {
		margin-bottom: 10rpx;
	}

	.gitem {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;

		.img {
			width: 54rpx;
			height: auto;
			margin-bottom: 26rpx;
		}

		font-size: 28rpx;
		font-family: PingFang SC;
		font-weight: bold;
		color: #030305;
		line-height: 36rpx;
	}
</style>
