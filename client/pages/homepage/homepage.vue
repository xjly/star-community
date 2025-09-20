<template>
	<view class="homepage">
		<mescroll-body ref="mescrollRef" :down="downOption" :up="upOption" @init="mescrollInit" @down="downCallback"
			@up="upCallback">
			<view class="user-wrap">
				<!-- 此处添加背景图片标签 -->
				<view class="background-mask"></view>
				<view class="user-container">
					<view class="user-box">
						<image class="avatar" :src="user.avatar"
							@click.stop="previewImage(user.avatar,0)"></image>
						<view class="avatar-right">
							<view class="nickname">
								{{user.nickname}}
							</view>
						</view>
						<view class="action-box">
							<view v-if="isFocusUser" class="chat-box" @click.stop="clickChatUser">
								私聊
							</view>
							<view v-if="isFocusUser" class="cancel-focus-box" @click.stop="clickFocusUser">
								取关
							</view>
							<view v-else class="focus-box" @click.stop="clickFocusUser">
								关注
							</view>
						</view>
					</view>
					<view class="introduction">
						<text user-select>这个人很懒，什么也没留下</text>
					</view>
					<!-- <view class="tag-list-box">
						<view class="tag-box">
							
						</view>
					</view> -->
					<view class="footer">
						<view class="count-warp">
							<view class="count-container">
								<view class="count-box">
									<view class="count">
										150
									</view>
									<view class="text">
										关注
									</view>
								</view>
							</view>
							<view class="count-container">
								<view class="count-box">
									<view class="count">
										100
									</view>
									<view class="text">
										粉丝
									</view>
								</view>
							</view>
							<view class="count-container">
								<view class="count-box">
									<view class="count">
										150
									</view>
									<view class="text">
										获赞和收藏
									</view>
								</view>
							</view>
						</view>
						<view class="edit-info" @click.stop="goto('../my-info/my-info')">
							编辑资料
						</view>
					</view>
				</view>
			</view>
			<view class="list-container">
				<view class="top-box">
					<view class="">
						帖子
					</view>
				</view>
				<StarPostList :list="postList" :option="postOption" :my_user="vuex_user" @selectPost="selectPost"
					@longpressPost="longpressPost" @clickUserAvatar="clickPostUserAvatar"
					@clickUserNickname="clickPostUserNickname" @clickChatUser="clickPostChatUser" @clickMore="clickMore"
					@clickSharePost="clickSharePost" @clickCommentPost="clickCommentPost" @toggleLike="togglePostLike">
				</StarPostList>
			</view>
		</mescroll-body>
	</view>
</template>

<script>
	import MescrollMixin from '@/uni_modules/mescroll-uni/components/mescroll-uni/mescroll-mixins.js'
	import StarPostList from '@/uni_modules/star-community/components/star-post-list/star-post-list.vue'


	export default {
		mixins: [MescrollMixin], // 使用mixin
		components: {
			StarPostList,
		},
		data() {
			return {
				user:{
					id: null,
					avatar: "http://dummyimage.com/180x150",
					nickname: "我"
				},
				isFocusUser: false,
				postList: [],
				postOption: {
					lineShow: 6, // 文本超出多少行显示省略号
					showFocus: false,
					// iconShare: '/static/share.svg',
					// iconView: '/static/view.svg',
					// iconComment: '/static/comment.svg',
					// iconLike: '/static/like.svg',
					// iconLiked: '/static/liked.svg',
				},
				downOption: {
					use: false, // 不启用mescroll下拉刷新，使用页面原生下拉刷新
				},
				upOption: {
					isBounce: false,
					noMoreSize: 4, // 如果列表已无数据,可设置列表的总数量要大于半页才显示无更多数据;避免列表数据过少(比如只有一条数据),显示无更多数据会不好看; 默认5
					// toTop: {
					// 	right: '20rpx',
					// 	bottom: '140rpx',
					// 	safearea: true,
					// 	width: '80rpx',
					// 	radius: '50%',
					// }
				}
			};
		},
		// 原生下拉刷新，推荐
		onPullDownRefresh() {
			this.mescroll.resetUpScroll()
			uni.stopPullDownRefresh()
		},
		async onLoad(option) {
			console.log('option.id', option?.id)
			this.user.id = option?.id
			this.$request({
				url:'/user',
				data:{
					userId: this.user.id
				}
			}).then(res => {
				this.user.nickname = res.data.nickname
				this.user.avatar = res.data.avatar
			})
		},
		methods: {
			// 上拉加载的回调: 其中page.num:当前页 从1开始, page.size:每页数据条数,默认10
			async upCallback(page) {
				try {
					// 请求数据列表
					this.$request({
						url:'/user/post/home',
						data:{
							userId :this.user.id
						}
					}).then(res => {
						this.postList = res.data
					})
				} catch (err) {
					console.log(err)
				}
			},
			// 点击关注用户
			clickFocusUser() {
				this.isFocusUser = !this.isFocusUser
			},
			// 点击私聊用户
			clickChatUser() {
				console.log('clickPostChatUser')
				this.goto(`../chat/chat?userId=${this.user.id}`)
			},
			// 选中贴子
			selectPost(post) {
				console.log(post);
				this.goto(`../post-detail/post-detail?id=${post.id}`)
			},
			// 长按帖子
			longpressPost(post) {
				console.log('longpressPost');
				this.showPostMoreActionSheet(post)
			},
			// 点击帖子更多
			clickMore(post) {
				console.log('clickMore');
				this.showPostMoreActionSheet(post)
			},
			// 点击评论帖子
			clickCommentPost(post) {
				console.log('clickCommentPost');
				// 建议跳转后自动下滑到评论区
				this.goto(`../post-detail/post-detail?id=${post.id}&comment`)
			},
			// 点赞帖子
			togglePostLike(post, isLiked) {
				console.log('togglePostLike', isLiked);
			},
			// 显示帖子更多操作菜单选项
			showPostMoreActionSheet(post) {
				uni.showActionSheet({
					itemList: ['举报'],
					success: (res) => {
						console.log('选中了第' + (res.tapIndex + 1) + '个按钮');
						uni.showToast({
							title: '举报成功'
						})
					},
					fail: (res) => {
						console.log(res.errMsg);
					}
				});
			},
			goto(url) {
				uni.navigateTo({
					url
				})
			},
			// 点击预览图片
			previewImage(urls, current) {
				uni.previewImage({
					urls,
					current
				})
			},
		}
	}
</script>

<style scoped lang="scss">
	.homepage {
		box-sizing: border-box;

		.user-wrap {
			position: relative;

			.background-mask {
				z-index: -1;
				position: absolute;
				height: 100%;
				width: 100%;
				background-color: #545454;
				opacity: .4;
			}

			.user-container {
				padding: 30rpx 30rpx 80rpx;

				.user-box {
					width: 100%;
					display: inline-flex;

					.avatar {
						flex-shrink: 0;
						height: 130rpx;
						width: 130rpx;
						border-radius: 50%;
						margin-right: 30rpx;
					}

					.avatar-right {
						width: 100%;
						display: flex;
						align-items: center;

						.nickname {
							font-size: 34rpx;
							font-weight: 700;
						}
					}

					.action-box {
						flex-shrink: 0;
						display: inline-flex;
						align-items: center;

						.focus-box {
							margin-left: 30rpx;
							border: 2rpx #55aaff solid;
							border-radius: 100rpx;
							color: #55aaff;
							font-size: 28rpx;
							padding: 6rpx 20rpx;
						}

						.cancel-focus-box {
							@extend .focus-box;
							color: #ff0000;
							border-color: #ff0000;
						}

						.chat-box {
							@extend .focus-box;
						}
					}
				}

				.introduction {
					margin-top: 30rpx;
					color: #4b4b4b;
				}

				.footer {
					margin-top: 30rpx;
					width: 100%;
					display: inline-flex;
					align-items: center;

					.count-warp {
						flex: 1;
						display: inline-flex;

						.count-container {
							width: 130rpx;
							display: inline-flex;

							.count-box {
								font-size: 24rpx;
								text-align: center;

								// display: flex;
								// flex-direction: column;
								// justify-content: center;
								// align-items: center;

								.count {}

								.text {
									margin-top: 10rpx;
								}
							}
						}
					}

					.edit-info {
						border: 2rpx #616161 solid;
						padding: 10rpx 20rpx;
						border-radius: 100rpx;
					}
				}
			}
		}

		.list-container {
			margin-top: -50rpx;
			background-color: #fff;
			border-radius: 30rpx 30rpx 0 0;
			overflow: hidden;

			.top-box {
				padding-top: 20rpx;
				width: 100%;
				display: inline-flex;
				justify-content: center;
				font-size: 34rpx;
			}
		}
	}
</style>