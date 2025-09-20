<template>
	<view>
		<StarPostHotList :list="postList" :option="postOption" @selectPost="selectPost"></StarPostHotList>
	</view>
</template>

<script>
	import StarPostHotList from '@/uni_modules/star-community/components/star-post-hot-list/star-post-hot-list.vue'

	export default {
		components: {
			StarPostHotList
		},
		data() {
			return {
				postList: [],
				postOption: {

				},
			};
		},
		// 原生下拉刷新，推荐
		async onPullDownRefresh() {
			this.$request({
				url:'/user/post/hot',
			}).then(res => {
				this.postList = res.data
			})
			uni.stopPullDownRefresh()
		},
		async onLoad() {
			this.$request({
				url:'/user/post/hot',
			}).then(res => {
				this.postList = res.data
			})
		},
		methods: {
			// 选中帖子
			selectPost(post) {
				console.log('selectPost');
				this.goto(`../post-detail/post-detail?id=${post.id}`)
			},
			goto(url) {
				uni.navigateTo({
					url
				})
			},
		}
	}
</script>

<style lang="scss">

</style>