<template>
	<view class="container">
		<u-navbar :leftText="username" leftIcon="account-fill" :leftIconSize="40" leftIconColor="#fff"
			@leftClick="userSetting" :rightText="title" :rightIconSize="40" @rightClick="goOut" :fixed="false"
			:autoBack="false"  :bgColor="navigationBarBackgroundColor"
			titleStyle="color:#ffffff;font-size:21px">
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
				title:''
			};
		},
		onReady() {

		},
		onLoad() {
			uni.showLoading({
				title: '加载中'
			})
			uni.$u.func.registerScanner(this.scannerCallback);
			api.getMenuList().then(data => {
				if (tool.isNotEmpty(data.data) && tool.isArray(data.data)) {
					data.data.forEach((item, index) => {
						if (item.systemTypeName == 'PDA') {
							this.menuLists = item.children;
						}
					})
				}
			})
			uni.hideLoading();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			warehouse.getWarehouseList().then(data=>{
				if(data.data.length>1&&uni.getStorageSync('warehouse').whName==undefined)
				{
				  	uni.$u.func.route('/pages/userSetting/warehouseSetting');
				}
				else{
					this.title=uni.getStorageSync('warehouse').whName;	
				}
			})
		},
		methods: {
			emitKeyDown(e) {
				if (e.key >= 1 && e.key <= 9) {
					this.navTo(this.menuLists[e.key - 1])
				}
			},
			navTo(menu) {
				if (tool.isNotEmpty(menu.children) && menu.children.length > 0) {
					uni.setStorageSync('childrenMenu', menu.children)
					//有子集的自动跳转统一模板
					uni.$u.func.route('/pages/home/childrenHome?title=' + menu.name);
					return;
				}
				//没有子集的跳转到自己的页面
				uni.$u.func.route(menu.path + '?title=' + menu.name);
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
