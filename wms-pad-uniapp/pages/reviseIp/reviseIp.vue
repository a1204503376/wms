<template>

	<view>
		<div class="logodiv">
			<image src="/static/images/login.png" style="width: 100px;height: 100px;" mode="widthFix"></image>
		</div>
		<view class="cell">
			<div>
				<u--form>
					<u-form-item label="地址" borderBottom>
						<u--input v-model="address" border="none"></u--input>
					</u-form-item>
				</u--form>
				<view class="input-box">
				</view>
				<button class="submit" @click="submit">登录</button>
			</div>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	export default {
		data() {
			return {
				address: setting.apiUrl,
			}
		},
		methods: {
			submit() {
				if (this.address == setting.apiUrl) {
					 uni.$u.toast('请修改请求地址');
	                 return;
				}
                uni.$u.toast('修改配置中，修改配置完成自动退出');
                setting.apiUrl = this.address;
                uni.setStorageSync('address', this.address)
                plus.runtime.quit();
			}
		}
	}
</script>

<style>
	.submit {
		margin: 60rpx 90rpx 0;
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
</style>
