<template>
	<view>
		<view class="logodiv">
			<image src="/static/images/login.png" style="width: 100px;height: 100px;" mode="widthFix"></image>
		</view>
		<view class="cell">
			<view>
				<u--form>
					<u-form-item label="地址" borderBottom>
						<u--input v-model="address" border="none"></u--input>
					</u-form-item>
				</u--form>
				<view class="input-box">
				</view>
				<button class="submit_button" @click="submit">确认</button>
			</view>
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
				this.address=data
			},
			submit() {
				if (this.address === this.oldAddress) {
					uni.$u.func.navigateBack();
					return;
				}
                
                setting.apiUrl = this.address;
				this.$u.vuex('baseUrl', this.address);
				uni.$u.toast('修改配置中，修改配置完成自动退出');
                plus.runtime.quit();
			}
		}
	}
</script>

<style lang="scss">
	@import 'reviseIp.scss';
	@import '@/static/common.scss';
</style>
