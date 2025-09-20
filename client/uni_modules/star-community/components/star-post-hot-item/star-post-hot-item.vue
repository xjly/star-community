<template>
	<view class="star-post-hot-item" @click="selectPost">
		<view class="index-box" :style="{ color: getIndexColor(index) }">
			{{ padWithZero(index+1,2) }}
		</view>
		<view class="index-right">
			<view class="main-box">
				<view class="main-text star-line-3">
					<text v-if="post.title" class="title">{{ post.title + ' ' }}</text>
					<text class="content">{{ post.content }}</text>
				</view>
				<view class="footer">
					<view class="view-box">
						<text>围观数 {{ numberFormat(post.viewCount) }}</text>
					</view>
					<view class="comment-box">
						<text>评论数 {{ numberFormat(post.commentCount) }}</text>
					</view>
				</view>
			</view>
			<image class="img" v-if="post.imgList.length>0" :src="post.imgList[0]" :mode="propOption.imgMode"></image>
		</view>
	</view>
</template>

<script>
	import numberFormat from '../../utils/numberFormat.js'

	export default {
		props: {
			index: {
				type: Number,
				default: 1
			},
			post: {
				type: Object,
				default: () => ({})
			},
			option: {
				type: Object,
				default: () => ({})
			}
		},
		data() {
			return {
				propOption: {
					imgMode: 'aspectFill',
				},
			};
		},
		created() {
			this.propOption = {
				...this.propOption,
				...this.option
			}
		},
		methods: {
			numberFormat,
			// 选中帖子
			selectPost() {
				this.$emit('selectPost', this.post)
			},
			// 指定几位，前面补零
			padWithZero(number, width) {
				const numberString = number.toString();
				const zeroPadding = "0".repeat(Math.max(0, width - numberString.length));
				return zeroPadding + numberString;
			},
			getIndexColor(index) {
				if (index === 0) {
					return "#ff0000"; // 前三名为红色
				} else if (index === 1) {
					return "#dc5858"; // 前三名为红色
				} else if (index === 2) {
					return "#e17f7f"; // 前三名为红色
				} else {
					return "gray"; // 前三名为红色
				}
			}
		}
	}
</script>

<style scoped lang="scss">
	/* start--文本行数限制--start */
	.star-line-1 {
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}

	.star-line-2 {
		-webkit-line-clamp: 2;
	}

	.star-line-3 {
		-webkit-line-clamp: 3;
	}

	.star-line-4 {
		-webkit-line-clamp: 4;
	}

	.star-line-5 {
		-webkit-line-clamp: 5;
	}

	.star-line-6 {
		-webkit-line-clamp: 6;
	}

	.star-line-7 {
		-webkit-line-clamp: 7;
	}

	.star-line-8 {
		-webkit-line-clamp: 8;
	}

	.star-line-9 {
		-webkit-line-clamp: 9;
	}

	.star-line-2,
	.star-line-3,
	.star-line-4,
	.star-line-5,
	.star-line-6,
	.star-line-7,
	.star-line-8,
	.star-line-9 {
		overflow: hidden;
		word-break: break-all;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
	}

	/* end--文本行数限制--end */
	.star-post-hot-item {
		width: 100%;
		display: flex;
		box-sizing: border-box;
		padding: 24rpx 20rpx;
		border-bottom: 1rpx solid #f0f0f0;
		transition: background-color 0.2s ease;
		background-color: #fff;

		&:active {
			background-color: #f9f9f9;
		}

		.index-box {
			margin-right: 24rpx;
			font-size: 32rpx;
			font-weight: 600;
			line-height: 1.4;
			min-width: 60rpx;
			text-align: center;
		}

		.index-right {
			flex: 1;
			display: flex;
			align-items: center;
			gap: 20rpx;

			.main-box {
				flex: 1;
				display: flex;
				flex-direction: column;
				justify-content: space-between;
				min-height: 180rpx;

				.main-text {
					line-height: 1.5;
					font-size: 32rpx;
					color: #333;
				}

				.title {
					font-weight: 600;
					color: #222;
				}

				.content {
					font-weight: 400;
					color: #555;
				}

				.footer {
					margin-top: 20rpx;
					display: flex;
					align-items: center;
					font-size: 26rpx;
					color: #999;

					.view-box {
						margin-right: 30rpx;
						display: flex;
						align-items: center;
						color: #a83f39;

						&::before {
							content: "👁️";
							margin-right: 8rpx;
							font-size: 24rpx;
						}
					}

					.comment-box {
						display: flex;
						align-items: center;

						&::before {
							content: "💬";
							margin-right: 8rpx;
							font-size: 24rpx;
						}
					}
				}
			}

			.img {
				flex-shrink: 0;
				height: 180rpx;
				width: 180rpx;
				border-radius: 12rpx;
				object-fit: cover;
				box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
			}
		}
	}
</style>