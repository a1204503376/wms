<template>
	<view class="content">
		<u-toast ref="uToast" />
		<view class="top">
			<div class="logodiv">
				<image src="/static/images/login.png" style="width: 100px;height: 100px;" mode="widthFix"></image>
				<h2>{{name}}</h2>
				<h5>{{pdaVersion}}</h5>	
			</div>
		
			<view>
				<u--form>
					<u-form-item>
						<template>
							<u-icon size="40" name="account-fill"></u-icon>
							<u--input placeholder="请输入账号" v-model="username" border="none"></u--input>
						</template>
					</u-form-item>
					<u-form-item>
						<template>
							<u-icon size="40" name="lock-fill"></u-icon>
							<u--input placeholder="请输入密码" type="password" v-model="password" border="none"></u--input>
						</template>
					</u-form-item>
				</u--form>
			</view>
			<button class="submit" @click="submit">登录</button>
			<button class="quit" @click="gotoAddress">配置</button>
			<button class="quit" @click="quitApp">退出</button>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import md5 from '@/utils/md5.js'
	import api from '@/api/user.js'
	import tool from '@/utils/tool.js'
	import {
		options
	} from '@/http/config.js';

	export default {
		data() {
			return {
				tenantId: setting.tenantId,
				username: uni.getStorageSync('username') || '',
				password: '',
				addressDisplay: true,
				name:setting.name,
				pdaVersion:setting.version
			};
		},
		onLoad() {
			uni.$u.func.registerScanner(this.callback);
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		methods: {
			callback(data) {
				let userInfoList = data.split('@');
				if (userInfoList.length != 2) {
					this.username = data
					return
				}
				this.username = userInfoList[0];
				this.password = userInfoList[1];
			},
			submit() {
				if(tool.isEmpty(this.username)||tool.isEmpty(this.password)){
					this.$u.func.showToast({
						title: '登录失败,用户名或密码不能为空',
					})
					return;
				}
				api.token(this.tenantId, this.username, md5(this.password), this.type).then(data => {
					uni.setStorageSync('accessToken', data.access_token)
					uni.setStorageSync('username', this.username)
					uni.$u.func.login({
						userInfo: data,
						accessToken: data.access_token
					});
				})
			},
			quitApp() {
				plus.runtime.quit();
			},
			gotoAddress() {
				uni.navigateTo({
					url: '/pages/reviseIp/reviseIp'
				});

			}
		}
	};
</script>

<style lang="scss">
	span {
		font-size: 50rpx;
	}

	.logodiv {
		text-align: center;
		display: block;
		flex-wrap: wrap;
		margin-top: 0.8rem;

		h3 {
			font-size: 20px;
			font-weight: 900;
		}

		h5 {
			font-size: 14px;
		}
	}

	.container {
		min-height: 100vh;
		overflow: hidden;

		.set-icon {
			vertical-align: middle;
			width: 41rpx;
			height: auto;
			margin-right: 35rpx;
		}
	}

	.content {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;

		height: 90vh;
		width: 100%;

		.top {
			width: 100%;
		}

		.logo {
			display: block;
			width: 281rpx;
			height: auto;
			margin: 0 auto 120rpx;
		}

		.cell {
			width: 100%;
			padding: 0 85rpx;
			box-sizing: border-box;
			margin-top: 36rpx;

			.name {
				font-size: 32rpx;
				font-family: Source Han Sans CN;
				font-weight: 400;
				color: #3e4a59;
				line-height: 30rpx;
				opacity: 0.72;
			}

			.input-box {
				padding: 30rpx 0;
				border-bottom: 2rpx solid #f6f6f6;
				display: flex;
				align-items: center;

				.code {
					font-size: 22rpx;
					font-family: Source Han Sans CN;
					font-weight: 400;
					color: #0d0d0d;
					line-height: 30rpx;

					text {
						color: #14b9c8;
					}
				}

				.ipt {
					flex: 1;
					// height: 24rpx;
					font-size: 24rpx;
				}

				.hold {
					font-size: 26rpx;
					font-family: Source Han Sans CN;
					font-weight: 400;
					color: #3e4a59;
					line-height: 30px;
					opacity: 0.45;
				}
			}
		}

		.agree {
			margin: 27rpx 95rpx 0;
			font-size: 22rpx;
			font-family: Adobe Heiti Std;
			font-weight: normal;
			color: #cacaca;
			line-height: 34rpx;

			.a {
				color: #000000;
			}
		}

		.submit {
			margin: 30rpx 90rpx 0;
			border: none;
			width: 572rpx;
			height: 86rpx;
			line-height: 86rpx;
			box-sizing: border-box;
			border-radius: 15rpx;
			background-color: #14b9c8;
			color: #ffffff;

			&::after {
				content: none;
			}

			&::before {
				content: none;
			}

			&[disabled='true'] {
				background: #e4e4e4;
				font-size: 36rpx;
				font-family: Source Han Sans CN;
				font-weight: 500;
				color: #ffffff;
			}
		}

		.quit {
			margin: 30rpx 90rpx 0;
			border: none;
			width: 572rpx;
			height: 86rpx;
			line-height: 86rpx;
			box-sizing: border-box;
			border-radius: 15rpx;
			background-color: #ADADAD;
			color: #ffffff;

			&::after {
				content: none;
			}

			&::before {
				content: none;
			}
		}

		.tip {
			margin-top: 30rpx;
			text-align: center;
			font-size: 22rpx;
			font-family: Adobe Heiti Std;
			font-weight: normal;
			color: #cacaca;
			line-height: 34rpx;
		}

		.change {
			margin-top: 20rpx;
			text-align: center;
			font-size: 22rpx;
			font-family: Adobe Heiti Std;
			font-weight: normal;
			color: #14b9c8;
			line-height: 34rpx;
		}

		.tag {
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 22rpx;
			font-family: Adobe Heiti Std;
			font-weight: normal;
			color: #9f9f9f;
			line-height: 34rpx;

			&::before {
				content: '';
				display: block;
				width: 160rpx;
				height: 1px;
				background: #d8d8d8;
				opacity: 0.86;
			}

			&::after {
				content: '';
				display: block;
				width: 160rpx;
				height: 1px;
				background: #d8d8d8;
				opacity: 0.86;
			}
		}

		.chat-arr {
			margin-top: 50rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.icon {
				width: 73rpx;
				height: 73rpx;
			}
		}
	}
</style>
