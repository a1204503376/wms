<template>
	<view>
		<view class="logodiv">
			<image src="/static/images/tyicon.png" style="width: 100px;height: 100px;" mode="widthFix"></image>
		</view>
		
		<view class="cell">
				<u--form>
					<u-form-item label="地址" borderBottom>
						<u--input v-model.trim="address" border="none"></u--input>
					</u-form-item>
				</u--form>
		</view>
		
		<view class="footer">
			<view class="btn-submit" @click="submit">确认</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting.js'

	export default {
		data() {
			return {
				address: this.$store.state.baseUrl || setting.apiUrl,
				oldAddress: this.$store.state.baseUrl || setting.apiUrl
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
				this.address = data
			},
			submit() {
				if (this.address === this.oldAddress) {
					uni.$u.func.navigateBack();
					return;
				}

				setting.apiUrl = this.address;
				this.$u.vuex('baseUrl', this.address);
				uni.$u.toast('修改配置中，修改配置完成自动退出');
				
				// #ifdef APP-PLUS
				//退出App
			    plus.runtime.quit();
				// #endif
				
			}
		}
	}
</script>

<style lang="scss">
	@import 'reviseIp.scss';
</style>
