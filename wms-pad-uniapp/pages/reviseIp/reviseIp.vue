<template>

	<view>
		<u-navbar  @leftClick="navigateBack"  :fixed="false" :autoBack="false" title="配置">
			<!-- <image slot="right" src="/static/images/home/message.png" class="message-icon" mode="widthFix"></image> -->
		</u-navbar>
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
				<button  :class="vuex_theme" @click="submit">确认</button>
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
		onLoad() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		onUnload() {		
			uni.$u.func.unRegisterScanner();
		},
		methods: {
			scannerCallback(data) {
				this.address=data
			},
			navigateBack(){
				uni.navigateBack({
					delta:1,//返回层数，2则上上页
				})
			},
			submit() {
				if (this.address == setting.apiUrl) {
						//uni.navigateBack()//默认delta:1
						uni.navigateBack({
							delta:1,//返回层数，2则上上页
						})
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

<style lang="scss">
	button {
		margin: 60rpx 90rpx 0;
		border: none;
		width: 572rpx;
		height: 86rpx;
		line-height: 86rpx;
		box-sizing: border-box;
		border-radius: 15rpx;

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
