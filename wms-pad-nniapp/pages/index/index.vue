<template>
	<view>
		<u-toast ref="uToast"></u-toast>
		<u-notify ref="uNotify" message="Hi uView"></u-notify>
		<u-navbar title="智能仓储" bgColor="#33cbcc" :autoBack="false">
			 <view class="u-nav-slot" slot="left">{{nickName}}</view>
		</u-navbar>
		<u-grid class="menu" col="3">
			<u-grid-item class="menu-item" v-for="(menu, index2) in menuList" :key="menu.code" @click="navTo(menu.path)">
				<text class="menu-number" >{{index2+1}}</text>
				<text :class="menu.source" class="menu-icon"></text>
				<view class="menu-text">
					{{menu.name}}
				</view>
			</u-grid-item>
		</u-grid>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				nickName:'测试人',
				jobNo: '',
				menuList: [
					{
						"code":"1",
						"name":"任务查看",
						"source":"iconfont icon-daishouhuo",
						"path":"/putAwayTask"
					}
				]
			}
		},
		onLoad() {
		},
		methods: {
			
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
</style>
