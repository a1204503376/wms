<template>
	<view class="parentClass">
		<br />
		<view class="date">
			{{date}}
		</view>
		<view>
			<view class="bigRound">
				<view class="smallRound" @click="onClick">
					<span>{{type}}</span>
				</view>
			</view>
		</view>
		<view id="footer">
			<view class="signType">
				签到状态
				<!-- <u--text text="签到状态" size="18px" :bold="true" margin="10px"></u--text> -->
			</view>
			<view id="line"></view>
		</view>
		<!-- <view class="u-page">
			<u-list @scrolltolower="scrolltolower" >
				<u-list-item v-for="(item, index) in list" :key="index">
					<u-cell :title="`item.name`">
						<u-avatar slot="icon" shape="square" size="35" :src="item.url"
							customStyle="margin: -3px 5px -3px 0"></u-avatar>
					</u-cell>
				</u-list-item>
			</u-list>
		</view> -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				date: '',
				type: "签到",
				list: [{
						icon: "我是头像",
						name: "小王",
						createTime: '2022-05-26 17:24:33',
						type: "签到"
					},
					{
						icon: "我是头像",
						name: "小王",
						createTime: '2022-05-26 17:30:33',
						type: "签退"
					},
					{
						icon: "我是头像",
						name: "小王",
						createTime: '2022-05-26 17:24:33',
						type: "签到"
					},
					{
						icon: "我是头像",
						name: "小王",
						createTime: '20442-05-26 17:30:33',
						type: "签退"
					},
					{
						icon: "我是头像",
						name: "小王",
						createTime: '2033-05-26 17:24:33',
						type: "签到"
					},
					{
						icon: "我是头像",
						name: "小王",
						createTime: '2010-05-26 17:30:33',
						type: "签退"
					},
				]
			}
		},
		methods: {
			getListData() {
				this.list = null;
			},
			onClick() {
				this.type = this.type === "签到" ? "签退" : "签到"
			},
			// 列表触底触发的事件
			scrolltolower() {

			},
			// 时间格式化
			dateFormat() {
				var date = new Date()
				var year = date.getFullYear()

				// 在日期格式中，月份是从0开始的
				// 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05

				var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
				var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
				var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
				var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
				var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
				let week = date.getDay() // 星期
				let weekArr = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
				// 拼接 时间格式处理
				return year + '年' + month + '月' + day + '日 ' + hours + '时' + minutes + '分' + seconds + '秒'
			}
		},
		// 挂载时间
		mounted() {
			this.timer = setInterval(() => {
				this.date = this.dateFormat()
			})
		},
		// 销毁时清除计时器
		beforeDestroy() {
			if (this.timer) {
				clearInterval(this.timer)
			}
		}
	}
</script>

<style lang="scss">
	@import 'signSetting.scss';
</style>
