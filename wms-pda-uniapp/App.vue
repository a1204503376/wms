<script>
	import api from '@/api/user.js'
	import setting from '@/common/setting'
	export default {
		onLaunch: function() {
			// #ifdef APP-PLUS
			//App自动跟新判断
			api.CheckUpdate().then(data => {
				if (data.data.verName != setting.version) {
					uni.showModal({
						//提醒用户更新  
						title: "更新提示",
						content: '是否更新',
						showCancel: false,
						success: (res) => {
							if (res.confirm) {
								plus.runtime.openURL(data.data.updateUrl)
							}
						}
					})
				}
			})
			// #endif
		},
		onShow: function() {
		},
		onHide: function() {
		}
	}
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "@/uni_modules/uview-ui/index.scss";
	@import url("./static/icon/iconfont.scss");	
</style>
