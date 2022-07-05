<template>
	<view class="container">
		<u-navbar :leftText="username" leftIcon="account-fill" :leftIconSize="40" leftIconColor="#fff"
			@leftClick="userSetting" :rightText="title" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u-grid class="menu" col="3">
			<u-grid-item class="menu-item" v-for="(menu, index2) in menuLists" :key="menu.code" @click="navTo(menu)">
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
	import warehouse from '@/api/warehouse.js'
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
				menuLists: [],
				username: this.$store.state.userName,
				title: ''
			};
		},
		onLoad() {
			this.menuLists = uni.getStorageSync('menuList');
		},
		onShow() {
			this.title = uni.getStorageSync('warehouse').whName;
		},
		methods: {
			emitKeyDown(e) {
				if (e.key >= 1 && e.key <= 9) {
					this.navTo(this.menuLists[e.key - 1])
				}
			},
			navTo(menu) {
				if (tool.isNotEmpty(menu.children) && menu.children.length > 0) {
					this.$destroy('keyboardListener')
					uni.setStorageSync('childrenMenu', menu.children)
					//有子集的自动跳转统一模板
					uni.$u.func.route('/pages/home/childrenHome', menu);
					return;
				}
				else if(tool.isNotEmpty(menu.path)){
					this.$destroy('keyboardListener')
					//没有子集的跳转到自己的页面
					uni.$u.func.route(menu.path, {
						path: '/pages/home/home'
					});
				}else{
					this.$u.func.showToast({
						title: '请联系管理员为您配置相关权限'
					});
				}
			
			},
			userSetting() {
				this.$destroy('keyboardListener')
				uni.$u.func.route('/pages/userSetting/userSetting');
			},
			goOut() {
				uni.$u.func.logout();
			},
		}
	};
</script>

<style lang="scss">
	@import 'home.scss';
</style>
